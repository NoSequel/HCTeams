package io.github.nosequel.hcf.util.database.handler.data;

import com.mongodb.Block;
import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import io.github.nosequel.hcf.data.DataController;
import io.github.nosequel.hcf.data.Loadable;
import io.github.nosequel.hcf.util.database.DatabaseController;
import io.github.nosequel.hcf.util.database.handler.DataHandler;
import io.github.nosequel.hcf.util.database.options.impl.MongoDatabaseOption;
import io.github.nosequel.hcf.util.database.type.mongo.MongoDataType;
import lombok.Getter;
import lombok.Setter;
import org.bson.Document;

import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.UUID;

@Getter
@Setter
public class MongoDataHandler implements DataHandler {

    private final DatabaseController controller;
    private final MongoDatabase database;

    /**
     * Constructor for setting up the MongoDataHandler
     */
    public MongoDataHandler(DatabaseController controller) {
        this.controller = controller;

        final MongoDatabaseOption option = (MongoDatabaseOption) this.controller.getOption();

        final MongoClient client = !option.isAuthenticate() ?
                new MongoClient(option.getHostname(), option.getPort()) :
                new MongoClient(new ServerAddress(option.getHostname(), option.getPort()), Collections.singletonList(MongoCredential.createCredential(option.getUsername(), option.getAuthenticateDatabase(), option.getPassword().toCharArray())));

        this.database = client.getDatabase(option.getAuthenticateDatabase());
    }

    @Override
    public void save(Loadable<?> loadable, String collection) {
        final MongoDataType type = (MongoDataType) this.controller.getType();
        final Document document = database.getCollection(collection).find(Filters.eq("uuid", loadable.getUniqueId().toString())).first();

        type.save(database.getCollection(collection), document == null ? new Document() : document, loadable);
    }

    @Override
    public void load(DataController<?, ?> controller, Loadable<?> loadable, String collectionName) {
        final MongoDataType type = (MongoDataType) this.controller.getType();
        final MongoCollection<Document> collection = database.getCollection(collectionName);
        final Document document = collection.find(Filters.eq("uuid", loadable.getUniqueId().toString())).first();

        if (document != null) {
            collection.deleteOne(document);
        }

        type.load(new Document(), controller, loadable);
    }

    @Override
    public void loadAll(DataController<?, ?> controller, String collectionName, Class<? extends Loadable<?>> loadableType) {
        final MongoDataType type = (MongoDataType) this.controller.getType();
        final MongoCollection<Document> collection = database.getCollection(collectionName);

        collection.find().forEach((Block<? super Document>) document -> {
            Loadable<?> loadable = null;

            try {
                loadable = loadableType
                        .getConstructor(UUID.class)
                        .newInstance(UUID.fromString(document.getString("uuid")));
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                e.printStackTrace();
            } finally {
                type.load(document, controller, loadable);
            }

        });
    }
}