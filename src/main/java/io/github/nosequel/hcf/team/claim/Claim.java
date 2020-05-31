package io.github.nosequel.hcf.team.claim;

import com.google.gson.JsonObject;
import io.github.nosequel.hcf.HCTeams;
import io.github.nosequel.hcf.team.Team;
import io.github.nosequel.hcf.team.TeamController;
import io.github.nosequel.hcf.team.data.impl.claim.ClaimTeamData;
import io.github.nosequel.hcf.util.Cuboid;
import io.github.nosequel.hcf.util.JsonBuilder;
import io.github.nosequel.hcf.util.JsonUtils;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Claim {

    private final ClaimPriority priority;
    private final Cuboid cuboid;

    private boolean deathban = true;

    /**
     * Constructor for creating a new Claim with a paramaterized claim priority
     *
     * @param cuboid   the cuboid
     * @param priority the priority
     */
    public Claim(Cuboid cuboid, ClaimPriority priority) {
        this.cuboid = cuboid;
        this.priority = priority;
    }

    /**
     * Constructor for creating a new Claim with the default priority
     * The defaulted priority will be NORMAL
     *
     * @param cuboid the cuboid
     */
    public Claim(Cuboid cuboid) {
        this(cuboid, ClaimPriority.NORMAL);
    }

    public Claim(JsonObject object) {
        System.out.println(object.toString());

        this.cuboid = new Cuboid(JsonUtils.getParser().parse(object.get("cuboid").getAsString()).getAsJsonObject());
        this.priority = ClaimPriority.valueOf(object.get("priority").getAsString());

    }

    public JsonObject toJson() {
        return new JsonBuilder()
                .addProperty("cuboid", this.getCuboid().toJson().toString())
                .addProperty("priority", this.priority.name()).get();
    }

    /**
     * Get the team which is allocated to the team
     *
     * @return the team
     */
    public Team getTeam() {
        return HCTeams.getInstance().getHandler().findController(TeamController.class).getTeams().stream()
                .filter(team -> team.hasData(ClaimTeamData.class) && team.findData(ClaimTeamData.class).getClaim().equals(this))
                .findFirst().orElse(null);
    }
}