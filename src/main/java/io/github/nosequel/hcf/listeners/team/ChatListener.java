package io.github.nosequel.hcf.listeners.team;

import io.github.nosequel.hcf.HCTeams;
import io.github.nosequel.hcf.team.Team;
import io.github.nosequel.hcf.team.TeamController;
import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.Arrays;

public class ChatListener implements Listener {

    private final String[] allowedCharacters = new String[] { "!", "@", "#", "$", "%", "^", "&", "*", "(", ")", "_", "+", ":", "\"", "'", "/", ",", ".", "`", "~", "\\"};
    private final TeamController teamController = HCTeams.getInstance().getHandler().findController(TeamController.class);

    @EventHandler
    public void onAsyncChat(AsyncPlayerChatEvent event) {
        if (!event.isCancelled()) {
            event.setCancelled(true);
            final Player player = event.getPlayer();

            try {
                final String message = event.getMessage();

                if (!this.isAlphanumericSpace(message)) {
                    player.sendMessage(ChatColor.RED + "Messages may only contain English letters.");
                    return;
                }

                final Team team = teamController.findTeam(player);

                event.getRecipients().forEach(recipient -> {
                    final String teamPrefix = team == null ? "" : ChatColor.GOLD + "[" + team.getDisplayName(recipient) + ChatColor.GOLD + "]";

                    recipient.sendMessage(teamPrefix + ChatColor.WHITE + (player.getCustomName() == null ? player.getName() : player.getCustomName()) + ChatColor.WHITE + ": " + message);
                });

            } catch (Exception exception) {
                player.sendMessage(ChatColor.RED + "Something has happend while processing your message");
                exception.printStackTrace();
            }
        }
    }

    /**
     * Check if a string contains allowed characters
     *
     * @param str the string
     * @return whether it only contains allowed characters
     */
    private boolean isAlphanumericSpace(String str) {
        if (str == null) {
            return false;
        } else {
            int sz = str.length();

            for(int i = 0; i < sz; ++i) {
                boolean allowedChar = false;

                for(String string : allowedCharacters) {
                    if (String.valueOf(str.charAt(i)).equals(string)) {
                        allowedChar = true;
                        break;
                    }
                }

                if (!Character.isLetterOrDigit(str.charAt(i)) && str.charAt(i) != ' ' && !allowedChar) {
                    return false;
                }
            }

            return true;
        }
    }

}
