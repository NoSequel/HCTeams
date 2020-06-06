package io.github.nosequel.hcf.classes.ability;

import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import java.util.ArrayList;
import java.util.List;

@Getter
public abstract class Ability implements Listener {

    private final List<Player> activated = new ArrayList<>();

    /**
     * Handle the activation of the Ability implementation
     *
     * @param player the player to activate it for
     */
    public void handleActivate(Player player) {
        this.activated.add(player);
    }

    /**
     * Handle the event of deactivating the Ability implementation
     *
     * @param player the player
     */
    public void handleDeactivate(Player player) {
        this.activated.remove(player);
    }

}
