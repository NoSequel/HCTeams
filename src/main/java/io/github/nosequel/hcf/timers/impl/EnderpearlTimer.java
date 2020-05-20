package io.github.nosequel.hcf.timers.impl;

import io.github.nosequel.hcf.HCTeams;
import io.github.nosequel.hcf.timers.Timer;
import io.github.nosequel.hcf.timers.TimerController;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class EnderpearlTimer extends Timer {

    private final TimerController timerController = HCTeams.getInstance().getHandler().findController(TimerController.class);

    public EnderpearlTimer() {
        super("Enderpearl", ChatColor.YELLOW + ChatColor.BOLD.toString() + "Enderpearl", true, 16000L);
    }

    @EventHandler
    public void onEnderpearl(PlayerInteractEvent event) {
        final Player player = event.getPlayer();
        final ItemStack item = event.getItem() == null ? player.getItemInHand() : event.getItem();

        if (item != null && player.getItemInHand().getType().equals(Material.ENDER_PEARL) && (event.getAction().equals(Action.RIGHT_CLICK_AIR) || event.getAction().equals(Action.RIGHT_CLICK_BLOCK))) {
            if (this.isOnCooldown(player)) {
                player.sendMessage(ChatColor.RED + "You are currently still on an enderpearl cooldown.");
                event.setCancelled(true);
                return;
            }

            this.start(player);

            final TeleportTimer teleportTimer = timerController.findTimer(TeleportTimer.class);

            if(teleportTimer.isOnCooldown(player)) {
                teleportTimer.cancel(player);
            }
        }
    }

    @Override
    public void handleTick(Player player) {
    }

    @Override
    public void handleEnd(Player player) {
        player.sendMessage(ChatColor.GRAY + "You are no longer on an enderpearl cooldown.");
    }

    @Override
    public void handleCancel(Player player) {
        this.handleEnd(player);
    }
}