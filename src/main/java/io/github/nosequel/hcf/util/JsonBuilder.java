package io.github.nosequel.hcf.util;

import com.google.gson.JsonObject;

public class JsonBuilder {

    private JsonObject object;

    public JsonBuilder() {
        object = new JsonObject();
    }

    /**
     * adds a property to the JsonObject
     *
     * @param path  the path
     * @param value the value
     * @param <V>   the type of the value
     * @return the current instanceof JsonBuilder
     */
    public <V> JsonBuilder addProperty(String path, V value) {
        if (value instanceof String) {
            object.addProperty(path, (String) value);
        } else if (value instanceof Character) {
            object.addProperty(path, (Character) value);
        } else if (value instanceof Boolean) {
            object.addProperty(path, (Boolean) value);
        } else if (value instanceof Number) {
            object.addProperty(path, (Number) value);
        } else {
            object.addProperty(path, value.toString());
        }

        return this;
    }

    public JsonObject get() {
        return this.object;
    }

}