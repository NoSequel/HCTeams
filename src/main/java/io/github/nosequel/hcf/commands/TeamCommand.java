package io.github.nosequel.hcf.commands;

import io.github.nosequel.hcf.controller.Controllable;
import io.github.nosequel.hcf.team.Team;
import io.github.nosequel.hcf.team.TeamController;
import io.github.nosequel.hcf.team.data.impl.CosmeticTeamData;
import io.github.nosequel.hcf.team.data.impl.player.PlayerRole;
import io.github.nosequel.hcf.team.data.impl.player.PlayerTeamData;
import io.github.nosequel.hcf.team.enums.TeamType;
import io.github.nosequel.hcf.util.command.annotation.Command;
import io.github.nosequel.hcf.util.command.annotation.Parameter;
import io.github.nosequel.hcf.util.command.annotation.Subcommand;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TeamCommand implements Controllable<TeamController> {

    private final TeamController controller = this.getController();

    @Command(label = "faction", aliases = {"f", "team", "t"})
    @Subcommand(label = "help", parentLabel = "faction")
    public void help(Player player) {
        player.sendMessage(new String[]{
                ChatColor.GRAY + ChatColor.STRIKETHROUGH.toString() + StringUtils.repeat("-", 56),

                ChatColor.BLUE + "General Commands",

                ChatColor.YELLOW + "/t create <name>" + ChatColor.GRAY + " - Create a new team",
                ChatColor.YELLOW + "/t disband" + ChatColor.GRAY + " - Disband your current team",
                ChatColor.YELLOW + "/t rename" + ChatColor.GRAY + " - Rename your team's name",

                ChatColor.GRAY + ChatColor.STRIKETHROUGH.toString() + StringUtils.repeat("-", 56)
        });
    }

    @Subcommand(label = "create", parentLabel = "faction")
    public void create(Player player, @Parameter(name = "teamName") String teamName) {
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

    @Subcommand(label = "disband", parentLabel = "faction")
    public void disband(Player player) {
        if (!this.shouldProceed(player, PlayerRole.LEADER)) {
            return;
        }

        final Team team = controller.findTeam(player);

        team.disband();
        player.sendMessage(ChatColor.YELLOW + "You have disbanded the team " + ChatColor.WHITE + team.getName());
    }

    @Subcommand(label = "rename", parentLabel = "faction")
    public void rename(Player player, @Parameter(name = "new name") String name) {
        if (!this.shouldProceed(player, PlayerRole.CO_LEADER)) {
            return;
        }

        final Team team = controller.findTeam(player);

        team.setName(name);
        team.findData(PlayerTeamData.class).broadcast(ChatColor.YELLOW + "Your current team has been renamed to " + ChatColor.WHITE + name);
    }

    @Subcommand(label = "show", parentLabel = "faction")
    public void show(Player player, @Parameter(name = "team", value = "@SELF") Team team) {
        if (team == null) {
            player.sendMessage(ChatColor.RED + "That team does not exist.");
            return;
        }

        if (team.getType().equals(TeamType.PLAYER_TEAM)) {
            final PlayerTeamData data = team.findData(PlayerTeamData.class);
            final CosmeticTeamData cosmeticTeamData = team.findData(CosmeticTeamData.class);

            final OfflinePlayer leader = Bukkit.getOfflinePlayer(data.getLeader());
            final Date currentDate = new Date(cosmeticTeamData.getCreateTime());

            player.sendMessage(new String[]{
                    ChatColor.GRAY + ChatColor.STRIKETHROUGH.toString() + StringUtils.repeat("-", 56),
                    ChatColor.BLUE + team.getName() + ChatColor.DARK_AQUA + " - " + ChatColor.YELLOW + data.getOnlineMembers().size() + "/" + data.getAllMembers().size(),
                    "",

                    ChatColor.YELLOW + " Leader: " + (leader.getPlayer() == null ? ChatColor.GRAY : ChatColor.GREEN) + leader.getName() + (leader.getPlayer() == null ? "" : ChatColor.YELLOW + "[" + ChatColor.GREEN + player.getPlayer().getStatistic(Statistic.PLAYER_KILLS) + ChatColor.YELLOW + "]"),
                    ChatColor.YELLOW + " Balance: " + ChatColor.WHITE + "$" + data.getBalance(),
                    ChatColor.YELLOW + " Home: " + ChatColor.WHITE + "None",

                    "",
                    ChatColor.GRAY + ChatColor.ITALIC.toString() + "Founded on " + new SimpleDateFormat("MM/dd/yyyy").format(currentDate) + " at " + new SimpleDateFormat("hh:mm:ss").format(currentDate),
                    ChatColor.GRAY + ChatColor.STRIKETHROUGH.toString() + StringUtils.repeat("-", 56),
            });
        }
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