package io.github.nosequel.hcf.classes.bard;

import io.github.nosequel.hcf.classes.Class;
import io.github.nosequel.hcf.classes.bard.abilities.BowDamageReduceAbility;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class BardClass extends Class<BardClassData> {

    /**
     * Constructor for creating a new BardClass object
     */
    public BardClass() {
        super("Bard", new Material[]{
                Material.GOLD_HELMET,
                Material.GOLD_CHESTPLATE,
                Material.GOLD_LEGGINGS,
                Material.GOLD_BOOTS
        });

        this.getAbilities().add(new BowDamageReduceAbility());
    }

    @Override
    public void onEquip(Player player) {
        this.getAbilities().forEach(ability -> ability.handleActivate(player));
        this.getClassData().put(player, new BardClassData());
    }

    @Override
    public void onUnequip(Player player) {
        this.getAbilities().forEach(ability -> ability.handleDeactivate(player));
        this.getClassData().remove(player);
    }
}