package io.github.nosequel.hcf.player.data;

import io.github.nosequel.hcf.data.Data;
import io.github.nosequel.hcf.team.Team;
import io.github.nosequel.hcf.team.claim.selection.ClaimSelection;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClaimSelectionData implements Data {

    private final ClaimSelection claimSelection;

    public ClaimSelectionData() { this.claimSelection = null; }

    /**
     * Constructor for creating a new ClaimSelectionData class instance
     *
     * @param claimSelection the claim selection
     */
    public ClaimSelectionData(ClaimSelection claimSelection) {
        this.claimSelection = claimSelection;
    }

}
