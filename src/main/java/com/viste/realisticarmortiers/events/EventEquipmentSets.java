package com.viste.realisticarmortiers.events;

import com.viste.realisticarmortiers.RealisticArmorTiers;
import com.viste.realisticarmortiers.capability.ArmorSetCapability;
import com.viste.realisticarmortiers.capability.CapabilityArmorSet;
import com.viste.realisticarmortiers.data.ArmorSet;
import com.viste.realisticarmortiers.data.Armors;
import com.viste.realisticarmortiers.logic.EquippedArmorSetEffects;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.event.entity.living.LivingEquipmentChangeEvent;
import net.minecraftforge.event.entity.living.PotionEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

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
        LivingEntity entity;
        if ((entity = event.getEntityLiving()) instanceof ServerPlayerEntity) {
            ServerPlayerEntity player = (ServerPlayerEntity) entity;
            ArmorSetCapability armorSetCapability = player.getCapability(CapabilityArmorSet.CAPABILITY_ARMOR_SET).orElse(null);
            if (armorSetCapability == null) return;

            Armors armors = RealisticArmorTiers.ARMOR_SETS_PARSER.getArmorSets();
            for (ArmorSet armorSet : armors.getArmorSets()) {
                if (armorSet.isFullSet(player)) {
                    // If player is still wearing same set, don't do anything
                    if (armorSet.getName() == armorSetCapability.getSetID()) {
                        return;
                    }

                    EquippedArmorSetEffects.applyArmorSetEffectToPlayer(player, armorSetCapability, armorSet);
                    return;
                }
            }

            EquippedArmorSetEffects.clearArmorSetEffectFromPlayer(player, armorSetCapability);
        }
    }

    /**
     * When a potion effect has been added to a player
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
            if (armorSetCapability == null) return;

            RealisticArmorTiers.LOGGER.info(event.getPotionEffect() + " has been added to " + player.getDisplayName().getString());
        }
    }

    /**
     * When a potion effect is removed from a player
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
            if (armorSetCapability == null) return;

            RealisticArmorTiers.LOGGER.info(event.getPotionEffect() + " has been removed from " + player.getDisplayName().getString());
        }
    }

    /**
     * When a potion effect expires on a player (does NOT call a PotionRemoveEvent *phew*)
     * @param event the PotionExpiryEvent
     */
    @SubscribeEvent
    public void onPlayerPotionEffectExpired(PotionEvent.PotionExpiryEvent event) {
        LivingEntity entity;
        if ((entity = event.getEntityLiving()) instanceof ServerPlayerEntity) {
            ServerPlayerEntity player = (ServerPlayerEntity) entity;
            ArmorSetCapability armorSetCapability = player.getCapability(CapabilityArmorSet.CAPABILITY_ARMOR_SET).orElse(null);
            if (armorSetCapability == null) return;

            RealisticArmorTiers.LOGGER.info(event.getPotionEffect() + " has expired on " + player.getDisplayName().getString());
        }
    }
}
