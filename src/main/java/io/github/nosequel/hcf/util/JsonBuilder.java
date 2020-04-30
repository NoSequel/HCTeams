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
     * @param path the path
     * @param val  the value
     * @param <V>  the type of the value
     * @return the current instanceof JsonBuilder
     */
    public <V> JsonBuilder addProperty(String path, V val) {

        if (val instanceof String) {
            object.addProperty(path, (String) val);
        } else if (val instanceof Character) {
            object.addProperty(path, (Character) val);
        } else if (val instanceof Boolean) {
            object.addProperty(path, (Boolean) val);
        } else if (val instanceof Number) {
            object.addProperty(path, (Number) val);
        } else {
            object.addProperty(path, val.toString());
        }

        return this;
    }

    public JsonObject get() { return this.object; }

}
