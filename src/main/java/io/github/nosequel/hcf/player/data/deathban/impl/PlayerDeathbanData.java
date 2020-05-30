package io.github.nosequel.hcf.player.data.deathban.impl;

import io.github.nosequel.hcf.player.data.deathban.DeathbanData;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;

import java.util.UUID;

@Getter
@Setter
public class PlayerDeathbanData extends DeathbanData {

    private final UUID killer;

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

    @Override
    public String getReason() {
        return "has been killed by " + Bukkit.getOfflinePlayer(killer).getName();
    }
}