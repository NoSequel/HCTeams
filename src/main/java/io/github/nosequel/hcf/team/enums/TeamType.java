package io.github.nosequel.hcf.team.enums;

import io.github.nosequel.hcf.HCTeams;
import io.github.nosequel.hcf.team.Team;
import io.github.nosequel.hcf.team.TeamController;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public enum TeamType {

    PLAYER_TEAM(false) {
        @Override
        public String formatName(Team team, Player player) {
            return (HCTeams.getInstance().getHandler().findController(TeamController.class).findTeam(player).equals(team) ? ChatColor.DARK_GREEN : ChatColor.YELLOW) + team.getFormattedName();
        }
    },

    SAFEZONE_TEAM(false) {
        @Override
        public String formatName(Team team, Player player) {
            return (team.getGeneralData().getColor() == null ? ChatColor.GREEN : team.getGeneralData().getColor()) + team.getFormattedName();
        }
    },

    WILDERNESS_TEAM(true) {
        @Override
        public String formatName(Team team, Player player) {
            return (team.getGeneralData().getColor() == null ? ChatColor.GRAY : team.getGeneralData().getColor()) + "The " + team.getFormattedName();
        }
    },

    KOTH_TEAM(false) {
        @Override
        public String formatName(Team team, Player player) {
            return (team.getGeneralData().getColor() == null ? ChatColor.AQUA : team.getGeneralData().getColor()) + team.getFormattedName() + ChatColor.GOLD + " KOTH";
        }
    },

    ROAD_TEAM(false) {
        @Override
        public String formatName(Team team, Player player) {
            return (team.getGeneralData().getColor() == null ? ChatColor.GOLD : team.getGeneralData().getColor()) + team.getFormattedName().replace("Road", " Road");
        }
    },

    SYSTEM_TEAM(false) {
        @Override
        public String formatName(Team team, Player player) {
            return (team.getGeneralData().getColor() == null ? ChatColor.WHITE : team.getGeneralData().getColor()) + team.getFormattedName();
        }
    };

    public boolean canInteract;

    TeamType(boolean interactable) {
        this.canInteract = interactable;
    }

    /**
     * Format the team name by the type
     *
     * @param team   the team
     * @param player the player
     * @return the formatted team
     */
    public abstract String formatName(Team team, Player player);
}