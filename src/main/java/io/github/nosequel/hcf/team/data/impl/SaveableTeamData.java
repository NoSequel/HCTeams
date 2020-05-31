package io.github.nosequel.hcf.team.data.impl;

import com.google.gson.JsonObject;
import io.github.nosequel.hcf.data.impl.SaveableData;
import io.github.nosequel.hcf.team.data.TeamData;

public abstract class SaveableTeamData implements TeamData, SaveableData {

    public SaveableTeamData() {}
    public SaveableTeamData(JsonObject object) {}

}