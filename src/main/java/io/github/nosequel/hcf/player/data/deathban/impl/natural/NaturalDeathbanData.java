package io.github.nosequel.hcf.player.data.deathban.impl.natural;

import io.github.nosequel.hcf.player.data.deathban.DeathbanData;

public class NaturalDeathbanData extends DeathbanData {

    private final NaturalDeathbanType type;

    /**
     * Constructor for creating a DeathbanData object for a player
     *
     * @param type     the killer
     * @param duration the duration
     */
    public NaturalDeathbanData(NaturalDeathbanType type, long duration) {
        super(duration);
        this.type = type;
    }

    @Override
    public String getReason() {
        return type.reason;
    }
}