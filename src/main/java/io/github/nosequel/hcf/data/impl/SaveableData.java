package io.github.nosequel.hcf.data.impl;

import com.google.gson.JsonObject;
import io.github.nosequel.hcf.data.Data;

public interface SaveableData extends Data {

    /**
     * Get the path where the JsonObject needs to be saved
     *
     * @return the path
     */
    String getSavePath();

    /**
     * Transfer the data to a JsonObject
     *
     * @return the JsonObject
     */
    JsonObject toJson();

}