package io.github.nosequel.hcf.util.database;

import com.mongodb.Block;
import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import io.github.nosequel.hcf.controller.Controller;
import io.github.nosequel.hcf.data.Data;
import io.github.nosequel.hcf.data.DataController;
import io.github.nosequel.hcf.data.Loadable;
import io.github.nosequel.hcf.util.database.options.DatabaseOption;
import io.github.nosequel.hcf.util.database.options.impl.MongoDatabaseOption;
import io.github.nosequel.hcf.util.database.type.DataType;
import io.github.nosequel.hcf.util.database.type.mongo.MongoDataType;
import org.bson.Document;

import javax.print.Doc;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class DatabaseController implements Controller {

    private final DataType<?, ?> type;
    private final DatabaseOption option;

    private MongoClient client;
    private MongoDatabase database;

    /**
     * Constructor for creating a new DatabaseController
     *
     * @param type the daa type
     */
    public DatabaseController(DatabaseOption option, DataType<?, ?> type) {
        this.type = type;
        this.option = option;
    }

    @Override
    public void enable() {
        if (option instanceof MongoDatabaseOption) {
            this.setupMongo();
        }

    }

    private void setupMongo() {
        final MongoDatabaseOption option = (MongoDatabaseOption) this.option;

        this.client = !option.isAuthenticate() ?
                new MongoClient(option.getHostname(), option.getPort()) :
                new MongoClient(new ServerAddress(option.getHostname(), option.getPort()), Collections.singletonList(MongoCredential.createCredential(option.getUsername(), option.getAuthenticateDatabase(), option.getPassword().toCharArray())));

        this.database = client.getDatabase(option.getAuthenticateDatabase());
    }

    /**
     * Save a loadable object from a collection
     *
     * @param loadable   the loadable object
     * @param collection the collection
     */
    public void save(Loadable<?> loadable, String collection) {
        if (this.type.getClass().equals(MongoDataType.class)) {
            final MongoDataType type = (MongoDataType) this.type;
            final Document document = database.getCollection(collection).find(Filters.eq("uuid", loadable.getUniqueId().toString())).first();

            type.save(database.getCollection(collection), document == null ? new Document() : document, loadable);
        }
    }

    /**
     * Load a DataType from an object
     *
     * @param controller the controller
     * @param loadable   the loadable
     */
    public void load(DataController<?, ?> controller, Loadable<?> loadable, String collectionName) {
        if (this.type.getClass().equals(MongoDataType.class)) {
            final MongoDataType type = (MongoDataType) this.type;
            final MongoCollection<Document> collection = database.getCollection(collectionName);
            final Document document = collection.find(Filters.eq("uuid", loadable.getUniqueId().toString())).first();

            if (document != null) {
                type.load(document, controller, loadable);
            }
        }
    }

    /**
     * Load all documents from a collection
     *
     * @param controller     the controller
     * @param collectionName the collection
     */
    public void loadAll(DataController<?, ?> controller, String collectionName, Class<? extends Loadable<?>> loadableType) {
        if (this.type.getClass().equals(MongoDataType.class)) {
            final MongoDataType type = (MongoDataType) this.type;
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
}