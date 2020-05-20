package io.github.nosequel.hcf.timers.impl;

import io.github.nosequel.hcf.HCTeams;
import io.github.nosequel.hcf.team.Team;
import io.github.nosequel.hcf.team.TeamController;
import io.github.nosequel.hcf.team.data.impl.claim.ClaimTeamData;
import io.github.nosequel.hcf.timers.Timer;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

public class TeleportTimer extends Timer {

    private final TeamController teamController = HCTeams.getInstance().getHandler().findController(TeamController.class);

    public TeleportTimer() {
        super("Home", ChatColor.BLUE + "Home", true, 10000);
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        final Player player = event.getPlayer();

        if (this.isOnCooldown(player)) {
            final Location to = event.getTo();
            final Location from = event.getFrom();

            if (to.getBlockX() != from.getBlockX() || to.getBlockY() != from.getBlockY() || to.getBlockZ() != from.getBlockZ()) {
                super.cancel(player);
            }
        }
    }

    @EventHandler
    public void onTeleport(PlayerTeleportEvent event) {
        final Player player = event.getPlayer();

        if (this.isOnCooldown(player)) {
            this.cancel(player);
        }
    }

    @Override
    public void handleTick(Player player) {
    }

    @Override
    public void handleEnd(Player player) {
        final Team team = teamController.findTeam(player);

        if (team != null) {
            final ClaimTeamData data = team.findData(ClaimTeamData.class);

            if (data != null) {
                player.teleport(data.getHome());
                player.sendMessage(ChatColor.GRAY + "You have been teleported to your team's HQ.");
            }
        }
    }

    @Override
    public void handleCancel(Player player) {
        player.sendMessage(ChatColor.RED + "Your teleport timer has been cancelled.");
    }
}