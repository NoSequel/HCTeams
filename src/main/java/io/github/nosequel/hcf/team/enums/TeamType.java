package io.github.nosequel.hcf.team.enums;

import io.github.nosequel.hcf.team.Team;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public enum TeamType {

    PLAYER_TEAM {
        @Override
        public String formatName(Team team, Player player) {
            final String formattedName = team.getName().replace("_", "");

            return ChatColor.RED + formattedName;
        }

    },

    SAFEZONE_TEAM {
        @Override
        public String formatName(Team team, Player player) {
            final String formattedName = team.getName().replace("_", "");

            return (team.getColor() == null ? ChatColor.GREEN : team.getColor()) + formattedName;
        }

    },

    KOTH_TEAM {
        @Override
        public String formatName(Team team, Player player) {
            final String formattedName = team.getName().replace("_", "");

            return (team.getColor() == null ? ChatColor.AQUA : team.getColor()) + formattedName + ChatColor.GOLD + " KOTH";
        }

    },

    ROAD_TEAM {
        @Override
        public String formatName(Team team, Player player) {
            final String formattedName = team.getName().replace("_", "");

            return (team.getColor() == null ? ChatColor.GOLD : team.getColor()) + formattedName.replace("Road", " Road");
        }

    },

    SYSTEM_TEAM {
        @Override
        public String formatName(Team team, Player player) {
            final String formattedName = team.getName().replace("_", "");

            return (team.getColor() == null ? ChatColor.WHITE : team.getColor()) + formattedName;
        }

    };

    public abstract String formatName(Team team, Player player);
}
