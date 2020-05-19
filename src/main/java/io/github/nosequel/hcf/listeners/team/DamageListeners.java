package io.github.nosequel.hcf.listeners.team;

import io.github.nosequel.hcf.HCTeams;
import io.github.nosequel.hcf.team.TeamController;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class DamageListeners implements Listener {

    private final TeamController teamController = HCTeams.getInstance().getHandler().findController(TeamController.class);

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {
        if (!event.getDamager().equals(event.getEntity()) && event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            Player damager = null;

            if (event.getDamager() instanceof Projectile && ((Projectile) event.getDamager()).getShooter() instanceof Player) {
                damager = (Player) ((Projectile) event.getDamager()).getShooter();
            } else if (event.getDamager() instanceof Player) {
                damager = (Player) event.getDamager();
            }

            if (damager != null) {
                if (teamController.findTeam(player) != null && teamController.findTeam(damager) != null && teamController.findTeam(damager).equals(teamController.findTeam(player))) {
                    damager.sendMessage(ChatColor.YELLOW + "You cannot hurt " + ChatColor.DARK_GREEN + player.getName() + ChatColor.YELLOW + ".");
                    event.setCancelled(true);
                }
            }
        }
    }
}