package io.github.nosequel.hcf.commands;

import io.github.nosequel.hcf.HCTeams;
import io.github.nosequel.hcf.controller.Controllable;
import io.github.nosequel.hcf.player.PlayerData;
import io.github.nosequel.hcf.player.PlayerDataController;
import io.github.nosequel.hcf.player.data.ClaimSelectionData;
import io.github.nosequel.hcf.team.Team;
import io.github.nosequel.hcf.team.TeamController;
import io.github.nosequel.hcf.team.claim.selection.ClaimSelection;
import io.github.nosequel.hcf.team.data.impl.CosmeticTeamData;
import io.github.nosequel.hcf.team.data.impl.claim.ClaimTeamData;
import io.github.nosequel.hcf.team.data.impl.player.PlayerRole;
import io.github.nosequel.hcf.team.data.impl.player.PlayerTeamData;
import io.github.nosequel.hcf.team.enums.TeamType;
import io.github.nosequel.hcf.util.command.annotation.Command;
import io.github.nosequel.hcf.util.command.annotation.Parameter;
import io.github.nosequel.hcf.util.command.annotation.Subcommand;
import org.apache.commons.lang.StringUtils;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

public class TeamCommand implements Controllable<TeamController> {

    private final TeamController controller = this.getController();
    private final PlayerDataController playerDataController = HCTeams.getInstance().getHandler().findController(PlayerDataController.class);

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
            final ClaimTeamData claimTeamData = team.findData(ClaimTeamData.class);

            final OfflinePlayer leader = Bukkit.getOfflinePlayer(data.getLeader());
            final Date currentDate = new Date(cosmeticTeamData.getCreateTime());

            final String captains = data.getCaptains().stream().map(Bukkit::getOfflinePlayer).filter(Objects::nonNull).map(target -> (target.getPlayer() == null ? ChatColor.GRAY.toString() : ChatColor.GREEN.toString()) + target.getName() + (target.getPlayer() == null ? "" : ChatColor.YELLOW + "[" + ChatColor.GREEN + target.getPlayer().getStatistic(Statistic.PLAYER_KILLS) + ChatColor.YELLOW + "]")).collect(Collectors.joining(ChatColor.YELLOW + ", "));
            final String members = data.getMembers().stream().map(Bukkit::getOfflinePlayer).filter(Objects::nonNull).map(target -> (target.getPlayer() == null ? ChatColor.GRAY.toString() : ChatColor.GREEN.toString()) + target.getName() + (target.getPlayer() == null ? "" : ChatColor.YELLOW + "[" + ChatColor.GREEN + target.getPlayer().getStatistic(Statistic.PLAYER_KILLS) + ChatColor.YELLOW + "]")).collect(Collectors.joining(ChatColor.YELLOW + ", "));
            final String coLeaders = data.getCoLeaders().stream().map(Bukkit::getOfflinePlayer).filter(Objects::nonNull).map(target -> (target.getPlayer() == null ? ChatColor.GRAY.toString() : ChatColor.GREEN.toString()) + target.getName() + (target.getPlayer() == null ? "" : ChatColor.YELLOW + "[" + ChatColor.GREEN + target.getPlayer().getStatistic(Statistic.PLAYER_KILLS) + ChatColor.YELLOW + "]")).collect(Collectors.joining(ChatColor.YELLOW + ", "));

            final List<String> messages = new ArrayList<>(Arrays.asList(
                    ChatColor.GRAY + ChatColor.STRIKETHROUGH.toString() + StringUtils.repeat("-", 56),
                    ChatColor.BLUE + team.getName() + ChatColor.DARK_AQUA + " - " + ChatColor.YELLOW + data.getOnlineMembers().size() + "/" + data.getAllMembers().size(),
                    "",

                    ChatColor.YELLOW + " Leader: " + (leader.getPlayer() == null ? ChatColor.GRAY : ChatColor.GREEN) + leader.getName() + (leader.getPlayer() == null ? "" : ChatColor.YELLOW + "[" + ChatColor.GREEN + player.getPlayer().getStatistic(Statistic.PLAYER_KILLS) + ChatColor.YELLOW + "]")
            ));

            if (!coLeaders.isEmpty()) {
                messages.add(ChatColor.YELLOW + " Co Leaders: " + coLeaders);
            }

            if (!captains.isEmpty()) {
                messages.add(ChatColor.YELLOW + " Captains: " + captains);
            }

            if (!members.isEmpty()) {
                messages.add(ChatColor.YELLOW + " Members: " + members);
            }

            messages.addAll(Arrays.asList(
                    ChatColor.YELLOW + " Balance: " + ChatColor.RED + "$" + data.getBalance(),
                    ChatColor.YELLOW + " Claim: " + ChatColor.RED + (claimTeamData != null ? claimTeamData.getClaim().getCuboid().getChunks().size() : "0") + " chunks" + ChatColor.YELLOW + ", " + "Home: " + ChatColor.RED + "None",

                    "",
                    ChatColor.GRAY + ChatColor.ITALIC.toString() + "Founded on " + new SimpleDateFormat("MM/dd/yyyy").format(currentDate) + " at " + new SimpleDateFormat("hh:mm:ss").format(currentDate),
                    ChatColor.GRAY + ChatColor.STRIKETHROUGH.toString() + StringUtils.repeat("-", 56)
            ));

            messages.forEach(player::sendMessage);
        }
    }

    @Subcommand(label = "sethome", parentLabel = "faction")
    public void sethome(Player player) {
        if (!this.shouldProceed(player, PlayerRole.CAPTAIN)) {
            return;
        }

        final Team team = controller.findTeam(player);
        final PlayerTeamData data = team.findData(PlayerTeamData.class);
        final ClaimTeamData claimData = team.findData(ClaimTeamData.class);

        if (claimData == null) {
            player.sendMessage(ChatColor.RED + "Your team doesn't have a claim yet.");
            return;
        }

        if (data != null) {
            if (!claimData.getClaim().getCuboid().isLocationInCuboid(player.getLocation())) {
                player.sendMessage(ChatColor.RED + "You can only set the team's home in your own claim.");
                return;
            }

            final Location location = player.getLocation();

            data.setHome(location);
            data.broadcast(ChatColor.GRAY + "The team's HQ has been set at (" + location.getBlockX() + ", " + location.getBlockY() + ", " + location.getBlockZ() + ")");
        }
    }

    @Subcommand(label = "home", parentLabel = "faction")
    public void home(Player player) {
        final Team team = controller.findTeam(player);

        if (team != null && team.findData(PlayerTeamData.class) != null) {
            final PlayerTeamData data = team.findData(PlayerTeamData.class);

            if (data.getHome() == null) {
                player.sendMessage(ChatColor.RED + "Your team doesn't have a home set, set it with /team sethome.");
                return;
            }

            player.teleport(data.getHome());
            player.sendMessage(ChatColor.GRAY + "You have been teleported to your team's HQ.");
        }
    }

    @Subcommand(label = "claim", parentLabel = "faction")
    public void execute(Player player) {
        if (!shouldProceed(player, PlayerRole.CAPTAIN)) {
            return;
        }

        final PlayerData playerData = this.playerDataController.findPlayerData(player.getUniqueId());
        final Team team = this.controller.findTeam(player);

        if (playerData.hasData(ClaimSelectionData.class)) {
            player.sendMessage(ChatColor.RED + "You are already claiming.");
            return;
        }

        playerData.addData(new ClaimSelectionData(new ClaimSelection(team)));
        player.sendMessage(new String[]{
                "",
                ChatColor.GREEN + ChatColor.BOLD.toString() + "You are currently claiming for your own faction,",
                ChatColor.GRAY + "* Click " + Action.RIGHT_CLICK_BLOCK.name() + " for the first position",
                ChatColor.GRAY + "* Click " + Action.LEFT_CLICK_BLOCK.name() + " for the second position",
                ChatColor.YELLOW + "To finish your claiming, sneak while you press " + Action.LEFT_CLICK_AIR.name(),
                ChatColor.YELLOW + "To cancel claiming, sneak while you press " + Action.RIGHT_CLICK_AIR.name(),
                ""
        });
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
            player.sendMessage(ChatColor.RED + "No permission.");
            return false;
        }

        return true;
    }
}