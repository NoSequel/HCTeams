package io.github.nosequel.hcf.team.data.impl;

import com.google.gson.JsonObject;
import io.github.nosequel.hcf.team.Team;
import io.github.nosequel.hcf.util.JsonBuilder;
import lombok.Getter;

@Getter
public class CosmeticTeamData implements SaveableTeamData {

    private final long createTime;

    public CosmeticTeamData(Team team) {
        this.createTime = System.currentTimeMillis();
    }

    public CosmeticTeamData(Team team, JsonObject object) {
        this.createTime = object.get("createTime").getAsInt();
    }

    @Override
    public String getSavePath() {
        return "cosmetic";
    }

    @Override
    public JsonObject toJson() {
        return new JsonBuilder()
                .addProperty("createTime", createTime)
                .get();
    }
}