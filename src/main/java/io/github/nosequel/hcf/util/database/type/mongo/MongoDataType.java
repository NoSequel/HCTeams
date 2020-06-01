package io.github.nosequel.hcf.util.database.type.mongo;

import com.google.gson.JsonObject;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import io.github.nosequel.hcf.data.Data;
import io.github.nosequel.hcf.data.DataController;
import io.github.nosequel.hcf.data.Loadable;
import io.github.nosequel.hcf.data.impl.SaveableData;
import io.github.nosequel.hcf.util.JsonUtils;
import io.github.nosequel.hcf.util.database.type.DataType;
import org.bson.Document;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.UUID;

public class MongoDataType implements DataType<Document, MongoCollection<Document>> {

    @Override
    public void load(Document document, DataController<?, ?> controller, Loadable<?> loadable) {
        document.forEach((key, string) -> {
            if (!(string instanceof String)) {
                return;
            }

            if (key.equals("uuid")) {
                loadable.setUniqueId(UUID.fromString((String) string));
                return;
            }

            if (loadable.getData() == null) {
                loadable.setData(new ArrayList<>());
            }

            final SaveableData data = controller.getRegisteredData().stream()
                    .filter($data -> $data instanceof SaveableData)
                    .filter($data -> ((SaveableData) $data).getSavePath().equals(key))
                    .map(clazz -> {
                        Data $data = null;

                        try {
                            $data = ((SaveableData) clazz).getClass().getConstructor(JsonObject.class).newInstance(JsonUtils.getParser().parse((String) string).getAsJsonObject());
                        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
                            e.printStackTrace();
                        }

                        return $data;
                    })
                    .map(SaveableData.class::cast)
                    .filter(Objects::nonNull)
                    .findFirst().orElse(null);

            loadable.addDataNormal(data);
        });
    }


    @Override
    public void save(MongoCollection<Document> collection, Document document, Loadable<?> loadable) {
        loadable.getData().stream()
                .filter(data -> data instanceof SaveableData)
                .map(SaveableData.class::cast)
                .forEach(saveableData -> document.put(saveableData.getSavePath(), saveableData.toJson().toString()));

        document.put("uuid", loadable.getUniqueId().toString());

        collection.replaceOne(Filters.eq("uuid", loadable.getUniqueId().toString()), document, new ReplaceOptions().upsert(true));
    }
}