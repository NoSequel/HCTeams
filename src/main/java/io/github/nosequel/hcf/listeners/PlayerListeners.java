package io.github.nosequel.hcf.listeners;

import io.github.nosequel.hcf.controller.Controllable;
import io.github.nosequel.hcf.player.PlayerData;
import io.github.nosequel.hcf.player.PlayerDataController;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerListeners implements Listener, Controllable<PlayerDataController> {

    private final PlayerDataController controller = this.getController();

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        final Player player = event.getPlayer();

        PlayerData playerData = controller.findPlayerData(player.getUniqueId());

        if(playerData == null) {
            playerData = new PlayerData(player.getUniqueId());
        }
    }

}
