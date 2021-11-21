package com.viste.realisticarmortiers.events;

import com.viste.realisticarmortiers.RealisticArmorTiers;
import com.viste.realisticarmortiers.capability.ArmorSetCapability;
import com.viste.realisticarmortiers.capability.CapabilityArmorSet;
import com.viste.realisticarmortiers.data.ArmorSet;
import com.viste.realisticarmortiers.data.Armors;
import com.viste.realisticarmortiers.data.PotionEffect;
import com.viste.realisticarmortiers.logic.EquippedArmorSetEffects;
import com.viste.realisticarmortiers.logic.PlayerSetEffects;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraftforge.event.entity.living.LivingEquipmentChangeEvent;
import net.minecraftforge.event.entity.living.PotionEvent;
import net.minecraftforge.event.world.NoteBlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.List;
import java.util.Set;

public class EventEquipmentSets {
    private static int playerPotionAddedIgnore = 0;
    private static int playerPotionRemovedIgnore = 0;

    /**
     * Increase the amount of PotionEvent.PotionAddedEvent to ignore
     * For example before applying a potion effect, so that we do not handle the potion added event
     */
    public static void incrementPlayerPotionAddedIgnore() {
        playerPotionAddedIgnore++;
    }

    /**
     * Decrease the amount of PotionEvent.PotionAddedEvent to ignore
     * Should always be preceded by an increment call and only happen if addition failed
     * For example after failing to apply a potion effect to a player (e.g. not applicable to player)
     */
    public static void decrementPlayerPotionAddedIgnore() {
        playerPotionAddedIgnore--;
    }

    /**
     * Increase the amount of PotionEvent.PotionRemoveEvent to ignore
     * For example before removing a potion effect, so that we do not handle the potion removal event
     */
    public static void incrementPlayerPotionRemovedIgnore() {
        playerPotionRemovedIgnore++;
    }

    /**
     * Decrease the amount of PotionEvent.PotionRemoveEvent to ignore
     * Should always be preceded by an increment call and only happen if removal failed
     * For example after failing to remove a potion effect from a player (e.g. not present)
     */
    public static void decrementPlayerPotionRemovedIgnore() {
        playerPotionRemovedIgnore--;
    }

    /**
     * When a player changes items in the armor slots (head, chest, legs, feet), main-hand or off-hand
     * @param event the LivingEquipmentChangeEvent
     */
    @SubscribeEvent
    public void onPlayerInventoryChange(LivingEquipmentChangeEvent event) {
        if(event.getSlot().getType() != EquipmentSlotType.Group.ARMOR) {
            // If we did not change armor slot ( = changed main/off-hand) return early, for now not part of sets
            return;
        }

        LivingEntity entity;
        if (!((entity = event.getEntityLiving()) instanceof ServerPlayerEntity)) {
            return;
        }

        ServerPlayerEntity player = (ServerPlayerEntity) entity;
        ArmorSetCapability armorSetCapability = player.getCapability(CapabilityArmorSet.CAPABILITY_ARMOR_SET).orElse(null);
        if (armorSetCapability == null) {
            return;
        }

        Armors armors = RealisticArmorTiers.ARMOR_SETS_PARSER.getArmorSets();
        for (ArmorSet armorSet : armors.getArmorSets()) {
            if (armorSet.isFullSet(player)) {
                if (armorSet.getName().equals(armorSetCapability.getSetID())) {
                    // If player is still wearing same set, don't do anything
                    return;
                }

                // Clear set effects stored in the capability and store new SetID
                EquippedArmorSetEffects.clearArmorSetFromPlayer(player, armorSetCapability, armorSet.getName());
                List<PotionEffect> conflictingPotionEffects = EquippedArmorSetEffects.applyArmorSetToPlayer(player, armorSetCapability, armorSet);
                EquippedArmorSetEffects.applyUsedPotionEffectsToPlayer(player, armorSetCapability, conflictingPotionEffects);
                return;
            }
        }

        // If no sets is equipped, clear the set effects from the player and add all used potion effects back to player
        // (which also removes all used potion effects from our capability manager)
        EquippedArmorSetEffects.clearArmorSetFromPlayer(player, armorSetCapability);
        EquippedArmorSetEffects.applyUsedPotionEffectsToPlayer(player, armorSetCapability, null);
    }

    /**
     * The moment before a potion effect has been added to the player
     * @param event the PotionAddedEvent
     */
    @SubscribeEvent
    public void onPlayerPotionEffectAdded(PotionEvent.PotionAddedEvent event) {
        if (playerPotionAddedIgnore > 0) {
            playerPotionAddedIgnore--;
            return;
        }

        LivingEntity entity;
        if ((entity = event.getEntityLiving()) instanceof ServerPlayerEntity) {
            ServerPlayerEntity player = (ServerPlayerEntity) entity;
            ArmorSetCapability armorSetCapability = player.getCapability(CapabilityArmorSet.CAPABILITY_ARMOR_SET).orElse(null);
            if (armorSetCapability == null) {
                return;
            }

            RealisticArmorTiers.LOGGER.info(event.getPotionEffect() + " has been added to " + player.getDisplayName().getString());
            Set<PotionEffect> setEffects = armorSetCapability.getSetEffects();
            if (!setEffects.isEmpty()) {
                EffectInstance playerPotionEffect = event.getPotionEffect();
                Effect effect;
                for (PotionEffect setEffect : setEffects) {
                    effect = setEffect.getEffect();
                    if (!playerPotionEffect.getEffect().equals(effect)) {
                        continue;
                    }

                    // If the newly added potion effect is of the same type as the set effect
                    if(playerPotionEffect.getAmplifier() > setEffect.getAmplifier()) {
                        // Clear effect of hidden effects (set effect as hidden effect => infinite effect on timer run out)
                        // TODO: Remove effect before adding it, or stop it from being added!
                        //  the effect hasn't been added yet, but has been stored
                        //  That means it will be used to update existing one instead of creating new one, not good!
                        //  see LivingEntity:addEffect
                        PlayerSetEffects.removeEffectFromPlayer(player, effect);
//                        PlayerSetEffects.addEffectToPlayer(player, playerPotionEffect);
                    } else {
                        // Otherwise, clear set effect of hidden effect (new potion effect hidden inside set effect)
                        // by removing set effect and re-adding it in
                        // TODO: Same as above, just slightly different scenario, but same logic needed here!
                        PlayerSetEffects.removeEffectFromPlayer(player, effect);
                        PlayerSetEffects.addEffectToPlayer(player, setEffect.getEffectInstance());
                    }
                }
            }
        }
    }

    /**
     * The moment before a potion effect has been removed from the player
     * @param event the PotionRemoveEvent
     */
    @SubscribeEvent
    public void onPlayerPotionEffectRemoved(PotionEvent.PotionRemoveEvent event) {
        if (playerPotionRemovedIgnore > 0) {
            playerPotionRemovedIgnore--;
            return;
        }

        LivingEntity entity;
        if ((entity = event.getEntityLiving()) instanceof ServerPlayerEntity) {
            ServerPlayerEntity player = (ServerPlayerEntity) entity;
            ArmorSetCapability armorSetCapability = player.getCapability(CapabilityArmorSet.CAPABILITY_ARMOR_SET).orElse(null);
            if (armorSetCapability == null) {
                return;
            }

            RealisticArmorTiers.LOGGER.info(event.getPotionEffect() + " has been removed from " + player.getDisplayName().getString());

            Set<PotionEffect> setEffects = armorSetCapability.getSetEffects();
            if (!setEffects.isEmpty()) {
                EffectInstance playerPotionEffect = event.getPotionEffect();
                if (playerPotionEffect == null) {
                    return;
                }

                Effect effect;
                for (PotionEffect setEffect : setEffects) {
                    effect = setEffect.getEffect();
                    if (!playerPotionEffect.getEffect().equals(effect)) {
                        continue;
                    }

                    // If the removed potion effect is of the same type as the set effect
                    event.setCanceled(true);
                    PlayerSetEffects.removeEffectFromPlayer(player, effect);
                    PlayerSetEffects.addEffectToPlayer(player, setEffect.getEffectInstance());
                }
            }
        }
    }

    /**
     * The moment before a potion effect expires on a player
     * @param event the PotionExpiryEvent
     */
    @SubscribeEvent
    public void onPlayerPotionEffectExpired(PotionEvent.PotionExpiryEvent event) {
        LivingEntity entity;
        if ((entity = event.getEntityLiving()) instanceof ServerPlayerEntity) {
            ServerPlayerEntity player = (ServerPlayerEntity) entity;
            ArmorSetCapability armorSetCapability = player.getCapability(CapabilityArmorSet.CAPABILITY_ARMOR_SET).orElse(null);
            if (armorSetCapability == null) {
                return;
            }

            RealisticArmorTiers.LOGGER.info(event.getPotionEffect() + " has expired on " + player.getDisplayName().getString());

            Set<PotionEffect> setEffects = armorSetCapability.getSetEffects();
            if (!setEffects.isEmpty()) {
                EffectInstance playerPotionEffect = event.getPotionEffect();
                if (playerPotionEffect == null) {
                    return;
                }

                Effect effect;
                for (PotionEffect setEffect : setEffects) {
                    effect = setEffect.getEffect();
                    if (!playerPotionEffect.getEffect().equals(effect)) {
                        continue;
                    }

                    // If the expired potion effect is of the same type as the set effect
                    PlayerSetEffects.removeEffectFromPlayer(player, effect);
                    PlayerSetEffects.addEffectToPlayer(player, setEffect.getEffectInstance());
                }
            }
        }
    }
}
