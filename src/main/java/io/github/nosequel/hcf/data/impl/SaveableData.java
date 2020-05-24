package io.github.nosequel.hcf.data.impl;

import com.google.gson.JsonObject;
import io.github.nosequel.hcf.data.Data;

public interface SaveableData extends Data {

    String getSavePath();
    JsonObject toJson();

}