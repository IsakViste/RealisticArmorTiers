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
import net.minecraftforge.eventbus.api.Event;
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
        if (playerPotionAddedIgnore >= 0) {
            playerPotionAddedIgnore++;
        } else {
            playerPotionAddedIgnore = 1;
        }
    }

    /**
     * Decrease the amount of PotionEvent.PotionAddedEvent to ignore
     * Should always be preceded by an increment call and only happen if addition failed
     * For example after failing to apply a potion effect to a player (e.g. not applicable to player)
     */
    public static void decrementPlayerPotionAddedIgnore() {
        if (playerPotionAddedIgnore > 0) playerPotionAddedIgnore--;
    }

    /**
     * Increase the amount of PotionEvent.PotionRemoveEvent to ignore
     * For example before removing a potion effect, so that we do not handle the potion removal event
     */
    public static void incrementPlayerPotionRemovedIgnore() {
        if (playerPotionRemovedIgnore >= 0) {
            playerPotionRemovedIgnore++;
        } else {
            playerPotionRemovedIgnore = 1;
        }
    }

    /**
     * Decrease the amount of PotionEvent.PotionRemoveEvent to ignore
     * Should always be preceded by an increment call and only happen if removal failed
     * For example after failing to remove a potion effect from a player (e.g. not present)
     */
    public static void decrementPlayerPotionRemovedIgnore() {
        if (playerPotionRemovedIgnore > 0) playerPotionRemovedIgnore--;
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

        if (armorSetCapability.getSetID().isEmpty()) {
            // Armor set not already present, and no new armor set, do nothing!
            return;
        }

        // If no sets is equipped, clear the set effects from the player and add all used potion effects back to player
        // (which also removes all used potion effects from our capability manager)
        EquippedArmorSetEffects.clearArmorSetFromPlayer(player, armorSetCapability);
        EquippedArmorSetEffects.applyUsedPotionEffectsToPlayer(player, armorSetCapability, null);
    }

    /**
     * The moment before a potion effect has been added to the player, when it's checking whether it's applicable to the entity
     * @param event the PotionApplicableEvent
     */
    @SubscribeEvent
    public void onPlayerPotionEffectApplicable(PotionEvent.PotionApplicableEvent event) {
        LivingEntity entity;
        if (!((entity = event.getEntityLiving()) instanceof ServerPlayerEntity)) {
            return;
        }
        ServerPlayerEntity player = (ServerPlayerEntity) entity;

        if (playerPotionAddedIgnore > 0) {
            playerPotionAddedIgnore--;
            return;
        }

        ArmorSetCapability armorSetCapability = player.getCapability(CapabilityArmorSet.CAPABILITY_ARMOR_SET).orElse(null);
        if (armorSetCapability == null) {
            return;
        }

        Set<PotionEffect> setEffects = armorSetCapability.getSetEffects();
        if (setEffects.isEmpty()) {
            // No set effects currently applied on player, don't do anything
            return;
        }

        EffectInstance playerPotionEffect = event.getPotionEffect();
        Effect effect;
        for (PotionEffect setEffect : setEffects) {
            effect = setEffect.getEffect();
            if (!playerPotionEffect.getEffect().equals(effect)) {
                continue;
            }

            // Deny the adding of the event
            event.setResult(Event.Result.DENY);
            // If the newly added potion effect has higher amplifier than the set effect, remove set effect and apply new effect
            if(playerPotionEffect.getAmplifier() > setEffect.getAmplifier()) {
                // Clear effect of hidden effects (set effect as hidden effect => infinite effect on timer run out)
                PlayerSetEffects.removeEffectFromPlayer(player, effect);
                PlayerSetEffects.applyEffectToPlayer(player, playerPotionEffect);
            } else {
                armorSetCapability.addUsedPotionEffect(new PotionEffect(playerPotionEffect));
            }
            return;
        }
    }

    /**
     * The moment before a potion effect has been removed from the player
     * @param event the PotionRemoveEvent
     */
    @SubscribeEvent
    public void onPlayerPotionEffectRemoved(PotionEvent.PotionRemoveEvent event) {
        LivingEntity entity;
        if (!((entity = event.getEntityLiving()) instanceof ServerPlayerEntity)) {
            return;
        }
        ServerPlayerEntity player = (ServerPlayerEntity) entity;

        if (playerPotionRemovedIgnore > 0) {
            playerPotionRemovedIgnore--;
            return;
        }

        ArmorSetCapability armorSetCapability = player.getCapability(CapabilityArmorSet.CAPABILITY_ARMOR_SET).orElse(null);
        if (armorSetCapability == null) {
            return;
        }

        Set<PotionEffect> setEffects = armorSetCapability.getSetEffects();
        if (setEffects.isEmpty()) {
            // No set effects currently applied on player, don't do anything
            return;
        }

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
            PlayerSetEffects.applyEffectToPlayer(player, setEffect.getEffectInstance());
            return;
        }
    }

    /**
     * The moment before a potion effect expires on a player
     * @param event the PotionExpiryEvent
     */
    @SubscribeEvent
    public void onPlayerPotionEffectExpired(PotionEvent.PotionExpiryEvent event) {
        LivingEntity entity;
        if (!((entity = event.getEntityLiving()) instanceof ServerPlayerEntity)) {
            return;
        }
        ServerPlayerEntity player = (ServerPlayerEntity) entity;

        ArmorSetCapability armorSetCapability = player.getCapability(CapabilityArmorSet.CAPABILITY_ARMOR_SET).orElse(null);
        if (armorSetCapability == null) {
            return;
        }

        Set<PotionEffect> setEffects = armorSetCapability.getSetEffects();
        if (setEffects.isEmpty()) {
            // No set effects currently applied on player, don't do anything
            return;
        }

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
            PlayerSetEffects.applyEffectToPlayer(player, setEffect.getEffectInstance());
            return;
        }
    }
}
