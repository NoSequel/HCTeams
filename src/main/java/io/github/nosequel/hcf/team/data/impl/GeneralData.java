package io.github.nosequel.hcf.team.data.impl;

import com.google.gson.JsonObject;
import io.github.nosequel.hcf.HCTeams;
import io.github.nosequel.hcf.team.TeamController;
import io.github.nosequel.hcf.team.enums.TeamType;
import io.github.nosequel.hcf.util.JsonBuilder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GeneralData extends SaveableTeamData {

    private TeamType type;
    private long createTime;
    private String name;

    public GeneralData() {
    }

    public GeneralData(String name, TeamType type) {
        this.name = name;
        this.type = type;
        this.createTime = System.currentTimeMillis();
    }

    public GeneralData(JsonObject object) {
        this(object.get("name").getAsString(), TeamType.valueOf(object.get("type").getAsString()));

        this.createTime = object.get("createTime").getAsLong();
    }

    @Override
    public String getSavePath() {
        return "general";
    }

    @Override
    public JsonObject toJson() {
        return new JsonBuilder()
                .addProperty("createTime", createTime)
                .addProperty("name", name)
                .addProperty("type", type.name()).get();
    }
}