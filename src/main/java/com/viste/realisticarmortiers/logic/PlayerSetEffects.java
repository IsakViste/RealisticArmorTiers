package com.viste.realisticarmortiers.logic;

import com.viste.realisticarmortiers.RealisticArmorTiers;
import com.viste.realisticarmortiers.capability.ArmorSetCapability;
import com.viste.realisticarmortiers.data.PotionEffect;
import com.viste.realisticarmortiers.events.EventEquipmentSets;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class PlayerSetEffects {

    private static final ArrayList<ItemStack> EMPTY_CURATIVE_ITEMS_LIST = new ArrayList<>();

    /**
     * Add a list of set effects to the player
     * @param player the target player to add the effects to
     * @param armorSetCapability the armor set capability manager of the player
     * @param setEffects the list of set effects to add to the player
     */
    public static void addSetEffectsToPlayer(@Nonnull ServerPlayerEntity player, @Nonnull ArmorSetCapability armorSetCapability, @Nonnull List<PotionEffect> setEffects) {
        for (PotionEffect setEffect : setEffects) {
            addSetEffectToPlayer(player, armorSetCapability, setEffect);
        }
    }

    /**
     * Add a set effect to the player. Does not add effect and returns false if another similar effect exist with higher amplifier.
     * If an effect already exists with a similar or lower amplifier, add existing effect to players used effects, and apply new effect.
     * @param player the target player to add the effect to
     * @param armorSetCapability the armor set capability manager of the player
     * @param setEffect the set effect to add to the player
     * @return true if successfully added the set effect to player
     */
    public static boolean addSetEffectToPlayer(@Nonnull ServerPlayerEntity player, @Nonnull ArmorSetCapability armorSetCapability, @Nonnull PotionEffect setEffect) {
        EffectInstance effectInstance = setEffect.effectInstance();
        effectInstance.setCurativeItems(EMPTY_CURATIVE_ITEMS_LIST);

        if (RealisticArmorTiers.DEBUG_MODE) {
            RealisticArmorTiers.LOGGER.debug("Adding Set Effect " + effectInstance + " to " + player.getDisplayName().getString());
        }

        EffectInstance playerEffect = player.getEffect(setEffect.getEffect());
        if (playerEffect != null) {
            // If effect with higher amplifier already exists, don't do anything
            if (playerEffect.getAmplifier() > setEffect.getAmplifier()) {
                if (RealisticArmorTiers.DEBUG_MODE) {
                    RealisticArmorTiers.LOGGER.debug("Did not add Set Effect " + setEffect +
                            ", another more powerful already exist on " + player.getDisplayName().getString());
                }
                return false;
            }

            // If effect with lower amplifier already exists, store existing effect in used effects, and add new effect
            if (playerEffect.getAmplifier() < setEffect.getAmplifier()) {
                PotionEffect usedPotionEffect = new PotionEffect(playerEffect);
                armorSetCapability.addUsedPotionEffect(usedPotionEffect);
                if (RealisticArmorTiers.DEBUG_MODE) {
                    RealisticArmorTiers.LOGGER.debug("Added " + playerEffect + " to used potion effects. " +
                            "Amplifier is less than " + setEffect + " on " + player.getDisplayName().getString());
                }
            }
        }

        EventEquipmentSets.incrementPlayerPotionAddedIgnore();
        if(!player.addEffect(effectInstance)) {
            EventEquipmentSets.decrementPlayerPotionAddedIgnore();
            RealisticArmorTiers.LOGGER.warn("Could not apply set effect " + effectInstance + " to "
                    + player.getDisplayName().getString());
            return false;
        }

        return true;
    }

    /**
     * Remove a list of set effects from the player
     * @param player the target player to remove the effects from
     * @param setEffects the list of set effects to remove from the player
     */
    public static void removeSetEffectsFromPlayer(@Nonnull ServerPlayerEntity player, @Nonnull Set<PotionEffect> setEffects) {
        for (PotionEffect setEffect : setEffects) {
            removeSetEffectFromPlayer(player, setEffect);
        }
    }

    /**
     * Remove a set effect from the player.
     * Only remove if the player has that effect applied already, and of the same amplifier. Otherwise, return false.
     * @param player the target player to remove the effect from
     * @param setEffect the set effect to remove from the player
     * @return true if successfully removed the set effect from the player
     */
    public static boolean removeSetEffectFromPlayer(@Nonnull ServerPlayerEntity player, @Nonnull PotionEffect setEffect) {
        Effect effect = setEffect.getEffect();
        EffectInstance playerEffect = player.getEffect(effect);
        if (playerEffect != null) {
            // Only remove effect if it's of the same amplifier as the set effect, otherwise it's (probably) not a set effect!
            if (playerEffect.getAmplifier() == setEffect.getAmplifier()) {
                if (RealisticArmorTiers.DEBUG_MODE) {
                    RealisticArmorTiers.LOGGER.debug("Removing Set Effect " + playerEffect + " from " + player.getDisplayName().getString());
                }

                EventEquipmentSets.incrementPlayerPotionRemovedIgnore();
                if (!player.removeEffect(effect)) {
                    EventEquipmentSets.decrementPlayerPotionRemovedIgnore();
                    RealisticArmorTiers.LOGGER.warn("Could not remove set effect " + playerEffect + " from "
                            + player.getDisplayName().getString());
                    return false;
                }
                return true;
            } else if (playerEffect.getAmplifier() > setEffect.getAmplifier()) {
                if (RealisticArmorTiers.DEBUG_MODE) {
                    RealisticArmorTiers.LOGGER.debug("Player effect " + playerEffect
                            + " has higher amplifier than set effect " + setEffect
                            + " and will therefore not be removed from " + player.getDisplayName().getString());
                }
            } else {
                if (RealisticArmorTiers.DEBUG_MODE) {
                    RealisticArmorTiers.LOGGER.debug("Player effect " + playerEffect
                            + " has lower amplifier than set effect " + setEffect
                            + " and will therefore not be removed from " + player.getDisplayName().getString());
                }
            }

            // If not of same amplifier, return false
            return false;
        }

        // If the effect is not present on the player
        RealisticArmorTiers.LOGGER.warn("Could not remove set effect " + setEffect + " from " +
                player.getDisplayName().getString() + ". Effect not already present!");
        return false;
    }

    /**
     * Add a list of potion effects to the player
     * @param player the target player to add the effects too
     * @param usedPotionEffects the list of potion effects to add to the player
     */
    public static void addUsedPotionEffectsToPlayer(@Nonnull ServerPlayerEntity player, @Nonnull Set<PotionEffect> usedPotionEffects) {
        for (PotionEffect usedPotionEffect : usedPotionEffects) {
            addUsedPotionEffectToPlayer(player, usedPotionEffect);
        }
    }

    /**
     * Add a potion effect to the player. If the duration is 0 or negative, or couldn't add the effect to the player, return false.
     * @param player the target player to add the effect too
     * @param usedPotionEffect the potion effect to add to the player
     * @return true if successfully added the potion effect to the player
     */
    public static boolean addUsedPotionEffectToPlayer(@Nonnull ServerPlayerEntity player, @Nonnull PotionEffect usedPotionEffect) {
        if (usedPotionEffect.getDuration() <= 0) {
            // Don't add if the potion effect is already expired
            RealisticArmorTiers.LOGGER.warn(usedPotionEffect + " has expired and won't be applied to player. " +
                    "This should not happen unless nbt is edited manually!");
            return false;
        }

        EffectInstance effectInstance = usedPotionEffect.effectInstance();
        EventEquipmentSets.incrementPlayerPotionAddedIgnore();
        if(!player.addEffect(effectInstance)) {
            EventEquipmentSets.decrementPlayerPotionAddedIgnore();
            RealisticArmorTiers.LOGGER.warn("Could not apply used potion effect " + effectInstance + " to "
                    + player.getDisplayName().getString());
            return false;
        }

        return true;
    }
}
