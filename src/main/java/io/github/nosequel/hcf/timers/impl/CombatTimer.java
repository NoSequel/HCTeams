package io.github.nosequel.hcf.timers.impl;

import io.github.nosequel.hcf.timers.Timer;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class CombatTimer extends Timer {

    public CombatTimer() {
        super("Combat", ChatColor.RED + ChatColor.BOLD.toString() + "Spawn Tag", false, 30000);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onDamage(EntityDamageByEntityEvent event) {
        if(!event.isCancelled()) {
            if (event.getDamager() instanceof Player && event.getEntity() instanceof Player) {
                final Player player = (Player) event.getEntity();
                final Player damager = (Player) event.getDamager();

                this.start(player);
                this.start(damager);
            }
        }
    }

    @Override
    public void handleTick(Player player) {
    }

    @Override
    public void handleEnd(Player player) {
    }

    @Override
    public void handleCancel(Player player) {
    }
}