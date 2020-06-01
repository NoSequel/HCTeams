package io.github.nosequel.hcf.team.data.impl.claim;

import com.google.gson.JsonObject;
import io.github.nosequel.hcf.team.Team;
import io.github.nosequel.hcf.team.claim.Claim;
import io.github.nosequel.hcf.team.data.impl.SaveableTeamData;
import io.github.nosequel.hcf.util.JsonBuilder;
import io.github.nosequel.hcf.util.StringUtils;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;

@Getter
@Setter
public class ClaimTeamData extends SaveableTeamData {

    private final Claim claim;
    private Location home;

    public ClaimTeamData() {
        this.claim = null;
    }

    public ClaimTeamData(Claim claim) {
        this.claim = claim;
    }

    public ClaimTeamData(JsonObject object) {
        this.claim = new Claim(object);

        if(object.get("home") != null) {
            this.home = StringUtils.locationFromString(object.get("home").getAsString());
        }
    }

    /**
     * Get a home as a string
     *
     * @return the string
     */
    public String getHomeAsString() {
        return home == null ? "Not Set" : home.getBlockX() + ", " + home.getBlockY() + ", " + home.getBlockZ();
    }

    @Override
    public String getSavePath() {
        return "claim_data";
    }

    @Override
    public JsonObject toJson() {
        final JsonBuilder builder = new JsonBuilder(this.claim.toJson())
                .addProperty("deathban", this.getClaim().isDeathban());

        if (this.home != null) {
            builder.addProperty("home", StringUtils.toString(home));
        }

        return builder.get();
    }
}
