package io.github.nosequel.hcf.team.enums;

import io.github.nosequel.hcf.team.Team;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public enum TeamType {

    PLAYER_TEAM {
        @Override
        public String formatName(Team team, Player player) {
            return ChatColor.RED + team.getFormattedName();
        }

    },

    SAFEZONE_TEAM {
        @Override
        public String formatName(Team team, Player player) {
            return (team.getColor() == null ? ChatColor.GREEN : team.getColor()) + team.getFormattedName();
        }

    },

    KOTH_TEAM {
        @Override
        public String formatName(Team team, Player player) {
            return (team.getColor() == null ? ChatColor.AQUA : team.getColor()) + team.getFormattedName() + ChatColor.GOLD + " KOTH";
        }

    },

    ROAD_TEAM {
        @Override
        public String formatName(Team team, Player player) {
            return (team.getColor() == null ? ChatColor.GOLD : team.getColor()) + team.getFormattedName().replace("Road", " Road");
        }

    },

    SYSTEM_TEAM {
        @Override
        public String formatName(Team team, Player player) {

            return (team.getColor() == null ? ChatColor.WHITE : team.getColor()) + team.getFormattedName();
        }

    };

    public abstract String formatName(Team team, Player player);
}
