package io.github.nosequel.hcf.commands;

import io.github.nosequel.hcf.HCTeams;
import io.github.nosequel.hcf.controller.Controllable;
import io.github.nosequel.hcf.player.PlayerData;
import io.github.nosequel.hcf.player.PlayerDataController;
import io.github.nosequel.hcf.player.data.ClaimSelectionData;
import io.github.nosequel.hcf.team.Team;
import io.github.nosequel.hcf.team.TeamController;
import io.github.nosequel.hcf.team.claim.Claim;
import io.github.nosequel.hcf.team.claim.ClaimPriority;
import io.github.nosequel.hcf.team.claim.selection.ClaimSelection;
import io.github.nosequel.hcf.team.data.impl.claim.ClaimTeamData;
import io.github.nosequel.hcf.team.enums.TeamType;
import io.github.nosequel.hcf.util.command.annotation.Command;
import io.github.nosequel.hcf.util.command.annotation.Subcommand;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;

import java.util.Arrays;
import java.util.stream.Collectors;

public class SystemTeamCommand implements Controllable<TeamController> {

    private final TeamController controller = this.getController();
    private final PlayerDataController playerDataController = HCTeams.getInstance().getHandler().findController(PlayerDataController.class);

    @Command(label = "systemteam", aliases = {"systeam", "steam"}, permission = "admin")
    @Subcommand(label = "help", parentLabel = "systemteam")
    public void help(Player player) {
        player.sendMessage(new String[]{
                ChatColor.GRAY + ChatColor.STRIKETHROUGH.toString() + StringUtils.repeat("-", 56),
                ChatColor.DARK_AQUA + ChatColor.BOLD.toString() + "System Team Help",
                ChatColor.GRAY + "Command for helping with system teams",
                "",
                ChatColor.DARK_AQUA + "General Commands:",
                ChatColor.AQUA + "* /systemteam create <name>" + ChatColor.WHITE + " - Create a new system team",
                ChatColor.AQUA + "* /systemteam delete <name>" + ChatColor.WHITE + " - Delete a new system team",
                "",
                ChatColor.DARK_AQUA + "Setup Commands:",
                ChatColor.AQUA + "* /systemteam type <name> <type>" + ChatColor.WHITE + " - Set the type of a system team",
                ChatColor.AQUA + "* /systemteam deathban <name>" + ChatColor.WHITE + " - Toggle a team's deathban status",
                ChatColor.AQUA + "* /systemteam color <name> <color>" + ChatColor.WHITE + " - Set the color of a team",
                ChatColor.AQUA + "* /systemteam claimfor <name>" + ChatColor.WHITE + " - Claim for a system team",
                ChatColor.AQUA + "* /systemteam priority <name> <priority>" + ChatColor.WHITE + " - Set the claim priority of a system team",

                ChatColor.GRAY + ChatColor.STRIKETHROUGH.toString() + StringUtils.repeat("-", 56),
        });
    }

    @Subcommand(label = "create", parentLabel = "systemteam")
    public void create(Player player, String name) {
        if (controller.findTeam(name) != null) {
            player.sendMessage(ChatColor.RED + "That team already exists!");
            return;
        } else if (name.length() > 16) {
            player.sendMessage(ChatColor.RED + "Maximum team name length is 16 characters!");
            return;
        } else if (name.length() < 3) {
            player.sendMessage(ChatColor.RED + "Minimum team name length is 3 characters!");
            return;
        } else if (!StringUtils.isAlphanumeric(name)) {
            player.sendMessage(ChatColor.RED + "Your team name has to be alphanumeric.");
            return;
        }

        new Team(null, name, TeamType.SYSTEM_TEAM);

        player.sendMessage(ChatColor.GREEN + "Success.");
        Bukkit.broadcastMessage(ChatColor.YELLOW + "System team " + ChatColor.BLUE + name + ChatColor.YELLOW + " has been " + ChatColor.GREEN + "created " + ChatColor.YELLOW + "by " + ChatColor.WHITE + player.getName());
    }

    @Subcommand(label = "delete", parentLabel = "systemteam")
    public void delete(Player player, Team team) {
        team.disband();
        Bukkit.broadcastMessage(ChatColor.YELLOW + "System team " + ChatColor.BLUE + team.getFormattedName() + ChatColor.YELLOW + " has been " + ChatColor.RED + "deleted " + ChatColor.YELLOW + "by " + ChatColor.WHITE + player.getName());
    }

    @Subcommand(label = "type", parentLabel = "systemteam")
    public void type(Player player, Team team, String type) {
        if (team.getGeneralData().getType().equals(TeamType.PLAYER_TEAM)) {
            player.sendMessage(ChatColor.RED + "That team is not a system team.");
            return;
        }

        if (Arrays.stream(TeamType.values()).noneMatch($type -> $type.name().equals(type.toUpperCase()))) {
            player.sendMessage(ChatColor.RED + ChatColor.BOLD.toString() + "Type by '" + type + "' not found.");
            player.sendMessage(ChatColor.RED + "Choose between: " + Arrays.stream(TeamType.values()).map(TeamType::name).collect(Collectors.joining(", ")));
            return;
        }

        team.getGeneralData().setType(TeamType.valueOf(type.toUpperCase()));
        Bukkit.broadcastMessage(ChatColor.YELLOW + "Type of " + ChatColor.BLUE + team.getDisplayName(player) + ChatColor.YELLOW + " has been changed to " + ChatColor.WHITE + type.toUpperCase());
    }

    @Subcommand(label = "color", parentLabel = "systemteam")
    public void color(Player player, Team team, String color) {
        if (team.getGeneralData().getType().equals(TeamType.PLAYER_TEAM)) {
            player.sendMessage(ChatColor.RED + "That team is not a system team.");
            return;
        }

        if (Arrays.stream(ChatColor.values()).noneMatch($type -> $type.name().equals(color.toUpperCase()))) {
            player.sendMessage(ChatColor.RED + ChatColor.BOLD.toString() + "Color by '" + color + "' not found.");
            player.sendMessage(ChatColor.RED + "Choose between: " + Arrays.stream(ChatColor.values()).map(ChatColor::name).collect(Collectors.joining(", ")));
            return;
        }

        team.getGeneralData().setColor(ChatColor.valueOf(color.toUpperCase()));
        Bukkit.broadcastMessage(ChatColor.YELLOW + "Color of " + ChatColor.BLUE + team.getDisplayName(player) + ChatColor.YELLOW + " has been changed to " + ChatColor.WHITE + color.toUpperCase());
    }

    @Subcommand(label = "claimfor", parentLabel = "systemteam")
    public void claimfor(Player player, Team team) {
        final PlayerData playerData = this.playerDataController.findPlayerData(player.getUniqueId());

        playerData.addData(new ClaimSelectionData(new ClaimSelection(team)));
        player.sendMessage(new String[]{
                "",
                ChatColor.GREEN + ChatColor.BOLD.toString() + "You are currently claiming for " + team.getFormattedName() + ",",
                ChatColor.GRAY + "* Click " + Action.RIGHT_CLICK_BLOCK.name() + " for the first position",
                ChatColor.GRAY + "* Click " + Action.LEFT_CLICK_BLOCK.name() + " for the second position",
                ChatColor.YELLOW + "To finish your claiming, sneak while you press " + Action.LEFT_CLICK_AIR.name(),
                ChatColor.YELLOW + "To cancel claiming, sneak while you press " + Action.RIGHT_CLICK_AIR.name(),
                ""
        });
    }

    @Subcommand(label = "priority", parentLabel = "systemteam")
    public void priority(Player player, Team team, String priority) {
        if (team.getGeneralData().getType().equals(TeamType.PLAYER_TEAM)) {
            player.sendMessage(ChatColor.RED + "That team is not a system team.");
            return;
        }

        if (Arrays.stream(ClaimPriority.values()).noneMatch($type -> $type.name().equals(priority.toUpperCase()))) {
            player.sendMessage(ChatColor.RED + ChatColor.BOLD.toString() + "Priority by '" + priority + "' not found.");
            player.sendMessage(ChatColor.RED + "Choose between: " + Arrays.stream(ClaimPriority.values()).map(ClaimPriority::name).collect(Collectors.joining(", ")));
            return;
        }

        team.findData(ClaimTeamData.class).getClaim().setPriority(ClaimPriority.valueOf(priority.toUpperCase()));
        Bukkit.broadcastMessage(ChatColor.YELLOW + "Priority of " + ChatColor.BLUE + team.getDisplayName(player) + ChatColor.YELLOW + " has been changed to " + ChatColor.WHITE + priority.toUpperCase());
    }

    @Subcommand(label = "deathban", parentLabel = "systemteam")
    public void deathban(Player player, Team team) {
        final ClaimTeamData claimTeamData = team.findData(ClaimTeamData.class);
        final Claim claim = claimTeamData.getClaim();

        claim.setDeathban(!claim.isDeathban());
        Bukkit.broadcastMessage(ChatColor.YELLOW + "Deathban status of " + ChatColor.BLUE + team.getDisplayName(player) + ChatColor.YELLOW + " has been changed to " + ChatColor.WHITE + (claim.isDeathban() ? "deathban" : "non-deathban"));
    }
}