package io.github.nosequel.hcf.team.data.impl.general;

import com.google.gson.JsonObject;
import io.github.nosequel.hcf.team.Team;
import io.github.nosequel.hcf.team.data.impl.SaveableTeamData;
import io.github.nosequel.hcf.util.JsonBuilder;

public class GeneralTeamData implements SaveableTeamData {

    private final Team team;

    /**
     * Constructor for creating a new GeneralTeamData object
     *
     * @param team the team
     */
    public GeneralTeamData(Team team) {
        this.team = team;
    }

    @Override
    public String getSavePath() {
        return "general";
    }

    @Override
    public JsonObject toJson() {
        return new JsonBuilder()
                .addProperty("name", team.getName())
                .addProperty("uuid", team.getUuid().toString())
                .addProperty("color", team.getColor() == null ? "WHITE" : team.getColor().name())
                .get();
    }
}
