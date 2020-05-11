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
                ChatColor.YELLOW + "/t rename" + ChatColor.GRAY + " - Rename your team's name",

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
        if (!this.shouldProceed(player, PlayerRole.LEADER)) {
            return;
        }

        final Team team = controller.findTeam(player);

        team.disband();
        player.sendMessage(ChatColor.YELLOW + "You have disbanded the team " + ChatColor.WHITE + team.getName());
    }

    @Command(aliases = "rename", desc = "Command for renaming a team")
    public void rename(@Sender Player player, String name) {
        if (!this.shouldProceed(player, PlayerRole.CO_LEADER)) {
            return;
        }

        final Team team = controller.findTeam(player);

        team.setName(name);
        team.findData(PlayerTeamData.class).broadcast(ChatColor.YELLOW + "Your current team has been renamed to " + ChatColor.WHITE + name);
    }

    /**
     * Check whether the command should proceed the execution
     *
     * @param player       the player
     * @param requiredRole the role which is required to perform the command
     * @return whether it should proceed
     */
    private boolean shouldProceed(Player player, PlayerRole requiredRole) {
        final Team team = controller.findTeam(player);

        if (team == null) {
            player.sendMessage(ChatColor.RED + "You are not in a team!");
            return false;
        }

        if (requiredRole.isHigher(controller.findTeam(player).findData(PlayerTeamData.class).getRole(player.getUniqueId()))) {
            final String $requiredRole = requiredRole.equals(PlayerRole.MEMBER)
                    ? "a member" : requiredRole.equals(PlayerRole.CAPTAIN)
                    ? "a captain" : requiredRole.equals(PlayerRole.CO_LEADER)
                    ? "a co leader" : "the leader";

            player.sendMessage(ChatColor.RED + "You must be " + $requiredRole + " of your team to execute this command!");
            return false;
        }

        return true;
    }
}