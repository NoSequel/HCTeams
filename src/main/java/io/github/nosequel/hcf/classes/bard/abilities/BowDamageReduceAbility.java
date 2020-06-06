package io.github.nosequel.hcf.classes.bard.abilities;

import io.github.nosequel.hcf.classes.ability.Ability;
import org.bukkit.ChatColor;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class BowDamageReduceAbility extends Ability {

    private final double damageReduction = 0.2;
    private final double percentage = damageReduction * 100;

    @Override
    public void handleActivate(Player player) {
        player.sendMessage(ChatColor.YELLOW + "You will now take" + ChatColor.RED + " " + percentage + " " + ChatColor.YELLOW + "less damage from bow impact.");
    }

    @Override
    public void handleDeactivate(Player player) {
        player.sendMessage(ChatColor.YELLOW + "You will take default bow damage now.");
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Player && event.getDamager() instanceof Arrow) {
            final Player player = (Player) event.getEntity();

            if (this.getActivated().contains(player)) {
                event.setDamage(event.getDamage() * 0.2);

                player.sendMessage(ChatColor.YELLOW + "You have been hit by an arrow and you have taken " + ChatColor.RED + percentage + ChatColor.YELLOW + " less damage.");
            }
        }
    }
}