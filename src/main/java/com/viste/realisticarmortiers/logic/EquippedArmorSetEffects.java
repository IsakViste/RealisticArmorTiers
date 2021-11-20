package com.viste.realisticarmortiers.logic;

import com.viste.realisticarmortiers.capability.ArmorSetCapability;
import com.viste.realisticarmortiers.data.ArmorSet;
import com.viste.realisticarmortiers.data.PotionEffect;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.potion.EffectInstance;

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
    public static void applyArmorSetToPlayer(@Nonnull ServerPlayerEntity player, @Nonnull ArmorSetCapability armorSetCapability, @Nonnull ArmorSet armorSet) {
        // Clear set effects stored in the capability and store new SetID
        clearArmorSetFromPlayer(player, armorSetCapability, armorSet.getName());

        // Get armor set effects and apply them to player
        List<PotionEffect> setEffects = armorSet.getPotionEffects();
        armorSetCapability.addSetEffects(setEffects);
        PlayerSetEffects.addSetEffectsToPlayer(player, armorSetCapability, setEffects);
    }

    /**
     * Apply used potion effects (effects present before wearing the armor set that would be overwritten) back to the player
     * If the player is not wearing a set anymore, apply all and clear the list
     * Otherwise only apply those that would not be overwritten by the new armor set
     * @param player the target player
     * @param armorSetCapability the armor set capability manager of the player
     * @param wearingArmorSet if the player is wearing an armor set or not
     */
    public static void applyUsedPotionEffectsToPlayer(@Nonnull ServerPlayerEntity player, @Nonnull ArmorSetCapability armorSetCapability, boolean wearingArmorSet) {
        Set<PotionEffect> potionEffects = new HashSet<>(armorSetCapability.getUsedPotionEffects());

        // If we are wearing an armor set, check used potion effects against the armor set effects
        // do not apply potion effects to player whose amplifier is equal or lower than the set effect
        if (wearingArmorSet) {
            Set<PotionEffect> setEffects = armorSetCapability.getSetEffects();
            Iterator<PotionEffect> i = potionEffects.iterator();
            while (i.hasNext()) {
                PotionEffect usedPotionEffect = i.next();
                for (PotionEffect setEffect : setEffects) {
                    if (!usedPotionEffect.getId().equals(setEffect.getId())) {
                        continue;
                    }

                    if (usedPotionEffect.getAmplifier() <= setEffect.getAmplifier()) {
                        i.remove();
                        break;
                    }
                }
            }
        }

        PlayerSetEffects.addUsedPotionEffectsToPlayer(player, potionEffects);
        // Only removed used potion effects we've applied to the player
        armorSetCapability.removeUsedPotionEffects(potionEffects);
    }





    /** !!! Only store potion effects that will be overwritten by the armor set !!! */
    public static void storeUsedPotionEffectsOfPlayer(@Nonnull ServerPlayerEntity player, @Nonnull ArmorSetCapability armorSetCapability) {
        // Go through active effects on the Player and store them in the usedPotionEffects
        Collection<EffectInstance> potionEffectsPlayer = player.getActiveEffects();
        for (EffectInstance effectInstance : potionEffectsPlayer) {
            armorSetCapability.addUsedPotionEffect(new PotionEffect(effectInstance));
        }
    }
}
