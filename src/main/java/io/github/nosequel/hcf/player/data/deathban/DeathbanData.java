package io.github.nosequel.hcf.player.data.deathban;

import com.google.gson.JsonObject;
import io.github.nosequel.hcf.data.impl.SaveableData;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class DeathbanData implements SaveableData {

    private final long duration;

    /**
     * Constructor for creating a new DeathbanData for a user
     *
     * @param duration the duration
     */
    public DeathbanData(long duration) {
        this.duration = duration;
    }

    /**
     * Get the deathban reason in a string
     *
     * @return the reason
     */
    public abstract String getReason();

    @Override
    public String getSavePath() {
        return "deathban";
    }

    @Override
    public JsonObject toJson() {
        return null;
    }
}