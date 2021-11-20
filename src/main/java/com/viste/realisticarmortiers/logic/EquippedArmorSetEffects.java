package com.viste.realisticarmortiers.logic;

import com.viste.realisticarmortiers.capability.ArmorSetCapability;
import com.viste.realisticarmortiers.data.ArmorSet;
import com.viste.realisticarmortiers.data.PotionEffect;
import net.minecraft.entity.player.ServerPlayerEntity;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

public class EquippedArmorSetEffects {
    /**
     * Remove all set effects from the player, and clear the stored set id and effects from the capability manager
     * To be used when a player is no longer wearing an armor set
     * @param player the target player
     * @param armorSetCapability the armor set capability manager of the player
     */
    public static void clearArmorSetFromPlayer(@Nonnull ServerPlayerEntity player, @Nonnull ArmorSetCapability armorSetCapability) {
        clearArmorSetFromPlayer(player, armorSetCapability, null);
    }

    /**
     * Remove all set effects from the player, and from the capability manger: clear the stored set effects & change to new set ID
     * To be used when a player is no longer wearing an armor set (null or empty string) or when changing to another armor set
     * @param player the target player
     * @param armorSetCapability the armor set capability manager of the player
     * @param setID the ID of the new armor set the player is wearing (null or empty if none)
     */
    public static void clearArmorSetFromPlayer(@Nonnull ServerPlayerEntity player, @Nonnull ArmorSetCapability armorSetCapability, @Nullable String setID) {
        PlayerSetEffects.removeSetEffectsFromPlayer(player, armorSetCapability.getSetEffects());
        armorSetCapability.removeAllSetEffects();
        if(setID == null || setID.isEmpty()) {
            armorSetCapability.clearSetID();
        } else {
            armorSetCapability.setSetID(setID);
        }
    }

    /**
     * Apply (quasi) all set effects of an armor set to the player, and store the new set id and effects in the capability manager
     * This clears first the armor set from the player
     * @param player the target player
     * @param armorSetCapability the armor set capability manager of the player
     * @param armorSet the new armor set the player is wearing
     */
    public static List<PotionEffect> applyArmorSetToPlayer(@Nonnull ServerPlayerEntity player, @Nonnull ArmorSetCapability armorSetCapability, @Nonnull ArmorSet armorSet) {
        // Get armor set effects and apply them to player
        List<PotionEffect> setEffects = armorSet.getPotionEffects();
        armorSetCapability.addSetEffects(setEffects);
        return PlayerSetEffects.addSetEffectsToPlayer(player, armorSetCapability, setEffects);
    }

    /**
     * Apply all the used potion effects (effects present before wearing the armor set that would be overwritten) back to the player
     * Otherwise only apply those that would not be overwritten by the new armor set
     * @param player the target player
     * @param armorSetCapability the armor set capability manager of the player
     */
    public static void applyUsedPotionEffectsToPlayer(@Nonnull ServerPlayerEntity player, @Nonnull ArmorSetCapability armorSetCapability, @Nullable List<PotionEffect> conflictingPotionEffects) {
        Set<PotionEffect> potionEffects = new HashSet<>(armorSetCapability.getUsedPotionEffects());
        if(conflictingPotionEffects != null && !conflictingPotionEffects.isEmpty()) {
            conflictingPotionEffects.forEach(potionEffects::remove);
        }
        PlayerSetEffects.addUsedPotionEffectsToPlayer(player, potionEffects);
        armorSetCapability.removeUsedPotionEffects(potionEffects);
    }
}
