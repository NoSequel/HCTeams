package io.github.nosequel.hcf.team.claim;

import io.github.nosequel.hcf.util.Cuboid;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Claim {

    private final ClaimPriority priority;
    private final Cuboid cuboid;

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

}