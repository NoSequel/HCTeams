package io.github.nosequel.hcf.classes.bard.abilities;

import io.github.nosequel.hcf.HCTeams;
import io.github.nosequel.hcf.classes.ClassController;
import io.github.nosequel.hcf.classes.ability.Ability;
import io.github.nosequel.hcf.classes.bard.BardClass;
import io.github.nosequel.hcf.classes.bard.BardClassData;
import io.github.nosequel.hcf.team.Team;
import io.github.nosequel.hcf.team.TeamController;
import io.github.nosequel.hcf.team.data.impl.player.PlayerTeamData;
import io.github.nosequel.hcf.timers.TimerController;
import io.github.nosequel.hcf.timers.impl.BardItemCooldownTimer;
import io.github.nosequel.hcf.util.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class StrengthAbility extends Ability {

    private final TeamController teamController = HCTeams.getInstance().getHandler().findController(TeamController.class);
    private final TimerController timerController = HCTeams.getInstance().getHandler().findController(TimerController.class);
    private final ClassController classController = HCTeams.getInstance().getHandler().findController(ClassController.class);

    private final BardItemCooldownTimer bardItemCooldownTimer = timerController.findTimer(BardItemCooldownTimer.class);

    private final long energy = 80L;

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        final Player player = event.getPlayer();
        final ItemStack itemStack = player.getItemInHand() == null ? event.getItem() : player.getItemInHand();
        final BardClass bard = classController.findClass(BardClass.class);

        if (itemStack != null && itemStack.getType().equals(Material.BLAZE_POWDER)) {
            if (bard.getClassData().containsKey(player)) {
                final BardClassData bardData = bard.getClassData().get(player);

                if (bardData.getEnergy() <= energy) {
                    player.sendMessage(ChatColor.RED + ChatColor.BOLD.toString() + "You do not have enough energy to use that ability.");
                    return;
                }

                if (bardItemCooldownTimer.isOnCooldown(player)) {
                    player.sendMessage(ChatColor.RED + "You are still on a cooldown for " + ChatColor.RED + ChatColor.BOLD.toString() + StringUtils.getFormattedTime(bardItemCooldownTimer.getDuration(player), true));
                    return;
                }

                final Team team = teamController.findTeam(player);

                if (team != null) {
                    final PlayerTeamData data = team.findData(PlayerTeamData.class);

                    data.getOnlineMembers().stream()
                            .filter(target -> target.getLocation().distance(player.getLocation()) < 10)
                            .forEach(target -> target.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 8 * 20, 1)));
                }

                bardData.setEnergy(bardData.getEnergy() - energy);
                bardItemCooldownTimer.start(player);
            }
        }
    }
}