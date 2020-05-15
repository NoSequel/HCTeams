package io.github.nosequel.hcf.team.enums;

import io.github.nosequel.hcf.controller.Controllable;
import io.github.nosequel.hcf.team.Team;
import io.github.nosequel.hcf.team.TeamController;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public enum TeamType implements Controllable<TeamController> {

    PLAYER_TEAM(false) {
        @Override
        public String formatName(Team team, Player player) {
            return (getController().findTeam(player).equals(team) ? ChatColor.GREEN : ChatColor.RED) + team.getFormattedName();
        }
    },

    SAFEZONE_TEAM(false) {
        @Override
        public String formatName(Team team, Player player) {
            return (team.getColor() == null ? ChatColor.GREEN : team.getColor()) + team.getFormattedName();
        }
    },

    WILDERNESS_TEAM(true) {
        @Override
        public String formatName(Team team, Player player) {
            return (team.getColor() == null ? ChatColor.GRAY : team.getColor()) + "The " + team.getFormattedName();
        }
    },

    KOTH_TEAM(false) {
        @Override
        public String formatName(Team team, Player player) {
            return (team.getColor() == null ? ChatColor.AQUA : team.getColor()) + team.getFormattedName() + ChatColor.GOLD + " KOTH";
        }
    },

    ROAD_TEAM(false) {
        @Override
        public String formatName(Team team, Player player) {
            return (team.getColor() == null ? ChatColor.GOLD : team.getColor()) + team.getFormattedName().replace("Road", " Road");
        }
    },

    SYSTEM_TEAM(false) {
        @Override
        public String formatName(Team team, Player player) {
            return (team.getColor() == null ? ChatColor.WHITE : team.getColor()) + team.getFormattedName();
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