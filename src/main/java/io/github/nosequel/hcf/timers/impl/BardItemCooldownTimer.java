package io.github.nosequel.hcf.timers.impl;

import io.github.nosequel.hcf.timers.Timer;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class BardItemCooldownTimer extends Timer {

    public BardItemCooldownTimer() {
        super("Bard", ChatColor.AQUA + ChatColor.BOLD.toString() + "Bard Cooldown", true, 30000);
    }

    @Override
    public void handleTick(Player player) { }

    @Override
    public void handleEnd(Player player) { }

    @Override
    public void handleCancel(Player player) { }
}
