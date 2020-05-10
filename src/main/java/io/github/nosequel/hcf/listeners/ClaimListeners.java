package io.github.nosequel.hcf.listeners;

import io.github.nosequel.hcf.HCTeams;
import io.github.nosequel.hcf.team.Team;
import io.github.nosequel.hcf.team.TeamController;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class ClaimListeners implements Listener {

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        final Player player = event.getPlayer();
        final TeamController controller = HCTeams.getInstance().getHandler().findController(TeamController.class);

        final Location to = event.getTo();
        final Location from = event.getFrom();
        final Team teamTo = controller.findTeam(to);
        final Team teamFrom = controller.findTeam(from);

        if (teamTo != null && teamFrom != null && !teamTo.equals(teamFrom)) {
            player.sendMessage(new String[]{
                    ChatColor.WHITE + "from: " + teamFrom.getDisplayName(player) + ChatColor.GRAY + "[" + teamFrom.getType().name() + "]",
                    ChatColor.WHITE + "to: " + teamTo.getDisplayName(player) + ChatColor.GRAY + "[" + teamTo.getType().name() + "]"
            });
        }
    }
}