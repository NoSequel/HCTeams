package io.github.nosequel.hcf.player.data.deathban.impl;

import com.google.gson.JsonObject;
import io.github.nosequel.hcf.player.data.deathban.DeathbanData;
import io.github.nosequel.hcf.util.JsonBuilder;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;

import java.util.UUID;

@Getter
@Setter
public class PlayerDeathbanData extends DeathbanData {

    private final UUID killer;

    public PlayerDeathbanData() { this.killer = null; }

    /**
     * Constructor for creating a DeathbanData object for a player
     *
     * @param killer   the killer
     * @param duration the duration
     */
    public PlayerDeathbanData(UUID killer, long duration) {
        super(duration);
        this.killer = killer;
    }

    public PlayerDeathbanData(JsonObject object) {
        super(object);

        this.killer = UUID.fromString(object.get("killer").getAsString());
    }

    @Override
    public JsonObject toJson() {
        return new JsonBuilder(super.toJson())
                .addProperty("killer", killer.toString()).get();
    }

    @Override
    public String getReason() {
        return "has been killed by " + Bukkit.getOfflinePlayer(killer).getName();
    }
}