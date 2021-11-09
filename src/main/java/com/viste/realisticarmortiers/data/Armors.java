package com.viste.realisticarmortiers.data;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;

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
     * From the list of armor sets, get the first (topmost) armor set that these items meet the requirements
     * In order from top first to bottom of the equipment_sets.json
     * @param helmet the item to check against the sets helmets
     * @param chestplate the item to check against the sets chestplates
     * @param leggings the item to check against the sets leggings
     * @param boots the item to check against the sets boots
     * @return the first ArmorSet these items fulfill
     */
    @Nullable
    public ArmorSet getArmorSetIfItemsFillRequirements(Item helmet, Item chestplate, Item leggings, Item boots) {
        for(ArmorSet armor : this.armorSets) {
            if(armor.isFullSet(helmet, chestplate, leggings, boots)) {
                return armor;
            }
        }

        return null;
    }

    /**
     * Get the old armor set the player was wearing before changing an item in a specific slot
     * @param player the player for which to get the inventory and armor slots
     * @param itemThatChanged the item that has been changed (was worn before changing)
     * @param slotChanged the slot of the item which was changed
     * @return the ArmorSet the player was wearing before changing
     */
    public ArmorSet getOldArmorSetPlayerWasWearing(ServerPlayerEntity player, Item itemThatChanged, EquipmentSlotType slotChanged) {
        Item helmet = player.getItemBySlot(EquipmentSlotType.HEAD).getItem().getItem();
        Item chestplate = player.getItemBySlot(EquipmentSlotType.CHEST).getItem().getItem();
        Item leggings = player.getItemBySlot(EquipmentSlotType.LEGS).getItem().getItem();
        Item boots = player.getItemBySlot(EquipmentSlotType.FEET).getItem().getItem();

        switch (slotChanged) {
            case HEAD:
                helmet = itemThatChanged;
                break;
            case CHEST:
                chestplate = itemThatChanged;
                break;
            case LEGS:
                leggings = itemThatChanged;
                break;
            case FEET:
                boots = itemThatChanged;
                break;
            default:
                break;
        }

        return getArmorSetIfItemsFillRequirements(helmet, chestplate, leggings, boots);
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
