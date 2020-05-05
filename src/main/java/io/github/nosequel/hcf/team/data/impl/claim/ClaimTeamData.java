package io.github.nosequel.hcf.team.data.impl.claim;

import com.google.gson.JsonObject;
import io.github.nosequel.hcf.team.Team;
import io.github.nosequel.hcf.team.claim.Claim;
import io.github.nosequel.hcf.team.data.impl.SaveableTeamData;
import lombok.Getter;

@Getter
public class ClaimTeamData implements SaveableTeamData {

    private final Team team;
    private final Claim claim;

    public ClaimTeamData(Team team, Claim claim) {
        this.team = team;
        this.claim = claim;
    }

    @Override
    public String getSavePath() {
        return "claim_data";
    }

    @Override
    public JsonObject toJson() {
        return claim.getCuboid().toJson();
    }
}
