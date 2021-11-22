package com.viste.realisticarmortiers.data;

import net.minecraft.entity.player.ServerPlayerEntity;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class Armors {
    private final List<ArmorSet> armorSets = new ArrayList<>();

    /**
     * From the list of armor sets, get the first (topmost) armor set that the player is wearing.
     * In order from top first to bottom of the equipment_sets.json
     * @param player the entity of which to check the armor slots
     * @return the first ArmorSet the player is fully wearing
     * @see assets.realisticarmortiers
     */
    @Nullable
    public ArmorSet getFullSetArmorThatPlayerIsWearing(ServerPlayerEntity player) {
        for(ArmorSet armor : this.armorSets) {
            if (armor.isFullSet(player)) {
                return armor;
            }
        }

        return null;
    }

    /**
     * @return the list of all armor sets loaded
     */
    public List<ArmorSet> getArmorSets() {
        return this.armorSets;
    }

    /**
     * Add an armor set to the list of armors
     * @param armorSet the armor to add to the list of loaded armors
     */
    public void addArmorSet(ArmorSet armorSet) {
        this.armorSets.add(armorSet);
    }
}
