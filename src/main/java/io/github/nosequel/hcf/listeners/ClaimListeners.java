package io.github.nosequel.hcf.listeners;

import io.github.nosequel.hcf.controller.Controllable;
import io.github.nosequel.hcf.team.Team;
import io.github.nosequel.hcf.team.TeamController;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerMoveEvent;

public class ClaimListeners implements Listener, Controllable<TeamController> {

    private final TeamController controller = this.getController();

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        final Player player = event.getPlayer();

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

    @EventHandler
    public void onBreak(BlockBreakEvent event) {
        final Player player = event.getPlayer();
        final Location blockLocation = event.getBlock().getLocation();
        final Team team = controller.findTeam(blockLocation);

        if (team != null && !team.canInteract(player)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBreak(BlockPlaceEvent event) {
        final Player player = event.getPlayer();
        final Location blockLocation = event.getBlock().getLocation();
        final Team team = controller.findTeam(blockLocation);

        if (team != null && !team.canInteract(player)) {
            event.setCancelled(true);
        }
    }
}