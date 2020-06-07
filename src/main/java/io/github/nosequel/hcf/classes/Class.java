package io.github.nosequel.hcf.classes;

import io.github.nosequel.hcf.HCTeams;
import io.github.nosequel.hcf.classes.ability.Ability;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public abstract class Class<T extends ClassData> implements Listener {


    private final String className;
    private final Material[] requiredArmor;

    private final List<Ability> abilities = new ArrayList<>();
    private final List<Player> equipped = new ArrayList<>();

    private Map<Player, T> classData = new HashMap<>();

    /**
     * Constructor for creating a new Class object
     *
     * @param className     the name of the class
     * @param requiredArmor the armor which is required to equip the class
     */
    public Class(String className, Material[] requiredArmor) {
        this.className = className;
        this.requiredArmor = requiredArmor;
        this.getAbilities().forEach(ability -> Bukkit.getPluginManager().registerEvents(ability, HCTeams.getInstance()));
    }

    /**
     * Handle the event of equipping a class
     *
     * @param player the player
     */
    public void onEquip(Player player) {
        player.sendMessage(ChatColor.AQUA + "Class: " + ChatColor.AQUA + ChatColor.BOLD.toString() + this.getClassName() + ChatColor.GRAY + " --> " + ChatColor.GREEN + "Enabled!");
        this.equipped.add(player);
    }

    /**
     * Handle the event of unequipping the class
     *
     * @param player the player
     */
    public void onUnequip(Player player) {
        player.sendMessage(ChatColor.AQUA + "Class: " + ChatColor.AQUA + ChatColor.BOLD.toString() + this.getClassName() + ChatColor.GRAY + " --> " + ChatColor.RED + "Disabled!");
        this.equipped.remove(player);
    }
}