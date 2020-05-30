package io.github.nosequel.hcf.scoreboard.provider;

import org.bukkit.entity.Player;

import java.util.List;

public interface BoardProvider {

    /**
     * Get the strings of the part
     *
     * @param player the player
     * @return the strings
     */
    List<String> getStrings(Player player);

}
