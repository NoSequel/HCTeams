package io.github.nosequel.hcf.timers.impl;

import io.github.nosequel.hcf.HCTeams;
import io.github.nosequel.hcf.player.PlayerData;
import io.github.nosequel.hcf.player.PlayerDataController;
import io.github.nosequel.hcf.player.data.SpawnProtectionData;
import io.github.nosequel.hcf.timers.Timer;
import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.HashMap;
import java.util.Map;

public class SpawnProtectionTimer extends Timer {

    private final Map<Player, PlayerData> data = new HashMap<>();
    private final PlayerDataController controller = HCTeams.getInstance().getHandler().findController(PlayerDataController.class);

    public SpawnProtectionTimer() {
        super("SpawnProt", ChatColor.GREEN + ChatColor.BOLD.toString() + "Invincibility", false, 60000*30);
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {
        final Entity entity = event.getEntity();
        final Entity damager = event.getDamager();

        if ((entity instanceof Player && this.isOnCooldown((Player) entity)) || (damager instanceof Player && this.isOnCooldown((Player) damager)) || (damager instanceof Projectile && ((Projectile) damager).getShooter() instanceof Player && this.isOnCooldown((Player) ((Projectile) damager).getShooter()))) {
            event.setCancelled(true);
        }
    }

    @Override
    public void handleTick(Player player) {
        final PlayerData playerData = this.findData(player);

        if (!playerData.hasData(SpawnProtectionData.class)) {
            playerData.addData(new SpawnProtectionData(this.getDefaultDuration()));
        }

        playerData.findData(SpawnProtectionData.class).setDurationLeft(this.getDuration(player));
    }

    @Override
    public void handleEnd(Player player) {
        final PlayerData data = this.findData(player);

        if(data != null && data.hasData(SpawnProtectionData.class)) {
            data.removeData(data.findData(SpawnProtectionData.class));
        }

        this.data.remove(player);
    }

    @Override
    public void handleCancel(Player player) {
        final PlayerData data = this.findData(player);

        if(data != null && data.hasData(SpawnProtectionData.class)) {
            data.removeData(data.findData(SpawnProtectionData.class));
        }

        this.data.remove(player);
    }

    /**
     * Find a data object by a player
     *
     * @param player the player
     * @return the object | or null
     */
    private PlayerData findData(Player player) {
        Map.Entry<Player, PlayerData> $entry = data.entrySet().stream().filter(entry -> entry.getKey().equals(player)).findFirst().orElse(null);
        PlayerData playerData;

        if ($entry == null) {
            final PlayerData foundData = controller.findPlayerData(player.getUniqueId());

            this.data.put(player, controller.findPlayerData(player.getUniqueId()));
            playerData = foundData;
        } else {
            playerData = $entry.getValue();
        }

        return playerData;
    }
}
