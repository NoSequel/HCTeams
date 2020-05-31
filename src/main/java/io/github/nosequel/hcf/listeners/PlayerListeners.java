package io.github.nosequel.hcf.listeners;

import io.github.nosequel.hcf.HCTeams;
import io.github.nosequel.hcf.controller.Controllable;
import io.github.nosequel.hcf.player.PlayerData;
import io.github.nosequel.hcf.player.PlayerDataController;
import io.github.nosequel.hcf.player.data.SpawnProtectionData;
import io.github.nosequel.hcf.player.data.deathban.DeathbanData;
import io.github.nosequel.hcf.team.Team;
import io.github.nosequel.hcf.team.TeamController;
import io.github.nosequel.hcf.team.data.impl.player.PlayerTeamData;
import io.github.nosequel.hcf.timers.TimerController;
import io.github.nosequel.hcf.timers.impl.SpawnProtectionTimer;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerListeners implements Listener, Controllable<PlayerDataController> {

    private final PlayerDataController controller = this.getController();
    private final TeamController teamController = HCTeams.getInstance().getHandler().findController(TeamController.class);
    private final TimerController timerController = HCTeams.getInstance().getHandler().findController(TimerController.class);


    @EventHandler(priority = EventPriority.LOWEST)
    public void onJoin(PlayerJoinEvent event) {
        final Player player = event.getPlayer();
        event.setJoinMessage(null);

        PlayerData playerData = controller.findPlayerData(player.getUniqueId());

        if (playerData == null) {
            playerData = new PlayerData(player.getUniqueId());
        }

        if (playerData.hasData(DeathbanData.class)) {
            final DeathbanData deathbanData = playerData.findData(DeathbanData.class);

            if (deathbanData.getExpiration() - System.currentTimeMillis() <= 0) {
                playerData.removeData(deathbanData);
                timerController.findTimer(SpawnProtectionTimer.class).start(player);

                return;
            }

            playerData.findData(DeathbanData.class).kickPlayer(player);
        }

        if(!player.hasPlayedBefore()) {
            timerController.findTimer(SpawnProtectionTimer.class).start(player);
        }

        if(playerData.hasData(SpawnProtectionData.class)) {
            final SpawnProtectionData data = playerData.findData(SpawnProtectionData.class);
            timerController.findTimer(SpawnProtectionTimer.class).start(player, data.getDurationLeft());
        }

        if (teamController.findTeam(player) != null) {
            final Team team = teamController.findTeam(player);
            final PlayerTeamData playerTeamData = team.findData(PlayerTeamData.class);

            playerTeamData.broadcast(ChatColor.GREEN + "Member Online: " + ChatColor.WHITE + player.getName());
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        final Player player = event.getPlayer();
        event.setQuitMessage(null);

        if (teamController.findTeam(player) != null) {
            final Team team = teamController.findTeam(player);
            final PlayerTeamData playerTeamData = team.findData(PlayerTeamData.class);

            playerTeamData.broadcast(ChatColor.GREEN + "Member Online: " + ChatColor.WHITE + player.getName());
        }
    }
}