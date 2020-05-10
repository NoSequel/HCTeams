package io.github.nosequel.hcf.commands;

import app.ashcon.intake.Command;
import app.ashcon.intake.bukkit.parametric.annotation.Sender;
import io.github.nosequel.hcf.controller.Controllable;
import io.github.nosequel.hcf.team.Team;
import io.github.nosequel.hcf.team.TeamController;
import io.github.nosequel.hcf.team.data.impl.player.PlayerRole;
import io.github.nosequel.hcf.team.data.impl.player.PlayerTeamData;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class TeamCommand implements Controllable<TeamController> {

    private final TeamController controller = this.getController();

    @Command(aliases = {"help", ""}, desc = "Main command for teams")
    public void help(@Sender Player player) {
        player.sendMessage(new String[]{
                ChatColor.GRAY + ChatColor.STRIKETHROUGH.toString() + StringUtils.repeat("-", 56),

                ChatColor.BLUE + "General Commands",
                
                ChatColor.YELLOW + "/t create <name>" + ChatColor.GRAY + " - Create a new team",
                ChatColor.YELLOW + "/t disband" + ChatColor.GRAY + " - Disband your current team",

                ChatColor.GRAY + ChatColor.STRIKETHROUGH.toString() + StringUtils.repeat("-", 56)
        });
    }

    @Command(aliases = {"create"}, desc = "Command for creating a new team")
    public void create(@Sender Player player, String teamName) {
        if (controller.findTeam(teamName) != null) {
            player.sendMessage(ChatColor.RED + "That team already exists!");
            return;
        }

        if (teamName.length() > 16) {
            player.sendMessage(ChatColor.RED + "Maximum team name length is 16 characters!");
            return;
        }

        if (teamName.length() < 3) {
            player.sendMessage(ChatColor.RED + "Minimum team name length is 3 characters!");
            return;
        }

        if (controller.findTeam(player) != null) {
            player.sendMessage(ChatColor.RED + "You are already in a team!");
            return;
        }

        new Team(null, teamName, player.getUniqueId());

        player.sendMessage(ChatColor.GREEN + "Success.");
        Bukkit.broadcastMessage(ChatColor.YELLOW + "Team " + ChatColor.BLUE + teamName + ChatColor.YELLOW + " has been " + ChatColor.GREEN + "created " + ChatColor.YELLOW + "by " + ChatColor.WHITE + player.getName());
    }

    @Command(aliases = {"disband"}, desc = "Command for disbanding a team")
    public void disband(@Sender Player player) {
        final Team team = controller.findTeam(player);

        if (team == null) {
            player.sendMessage(ChatColor.RED + "You are not in a team!");
            return;
        }

        if (!controller.findTeam(player).findData(PlayerTeamData.class).getRole(player.getUniqueId()).equals(PlayerRole.LEADER)) {
            player.sendMessage(ChatColor.RED + "You must be the leader of your team to disband it!");
            return;
        }

        team.disband();
        player.sendMessage(ChatColor.YELLOW + "You have disbanded the team " + ChatColor.WHITE + team.getName());
    }
}