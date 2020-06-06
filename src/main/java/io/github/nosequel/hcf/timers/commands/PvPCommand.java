package io.github.nosequel.hcf.timers.commands;

import io.github.nosequel.hcf.controller.Controllable;
import io.github.nosequel.hcf.timers.TimerController;
import io.github.nosequel.hcf.timers.impl.SpawnProtectionTimer;
import io.github.nosequel.hcf.util.command.annotation.Command;
import io.github.nosequel.hcf.util.command.annotation.Subcommand;
import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class PvPCommand implements Controllable<TimerController> {

    private final TimerController controller = this.getController();
    private final SpawnProtectionTimer spawnProtectionTimer = this.controller.findTimer(SpawnProtectionTimer.class);

    @Command(label = "pvp")
    public void main(Player player) {
        player.sendMessage(new String[]{
                ChatColor.GRAY + ChatColor.STRIKETHROUGH.toString() + StringUtils.repeat("-", 56),
                ChatColor.GOLD + "PvP Help",
                "",
                ChatColor.GRAY + "* /pvp enable",
                ChatColor.GRAY + "* /pvp suicide",
                ChatColor.GRAY + ChatColor.STRIKETHROUGH.toString() + StringUtils.repeat("-", 56)
        });
    }

    @Subcommand(label = "enable", parentLabel = "pvp")
    public void enable(Player player) {
        if (spawnProtectionTimer.isOnCooldown(player)) {
            spawnProtectionTimer.cancel(player);

            player.sendMessage(ChatColor.GREEN + "You have disabled your spawn protection timer.");
        } else {
            player.sendMessage(ChatColor.RED + "You are not on a spawn protection timer.");
        }
    }

    @Subcommand(label = "suicide", parentLabel = "pvp")
    public void suicide(Player player) {
        player.setHealth(0.0);
    }
}