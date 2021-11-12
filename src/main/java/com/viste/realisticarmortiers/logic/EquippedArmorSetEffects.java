package com.viste.realisticarmortiers.logic;

import com.viste.realisticarmortiers.RealisticArmorTiers;
import com.viste.realisticarmortiers.capability.ArmorSetCapability;
import com.viste.realisticarmortiers.data.ArmorSet;
import com.viste.realisticarmortiers.data.PotionEffect;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.List;

public class EquippedArmorSetEffects {
    public static void clearArmorSetEffectFromPlayer(@Nonnull ServerPlayerEntity player, @Nonnull ArmorSetCapability armorSetCapability) {
        PlayerSetEffects.removeSetEffectsFromPlayer(player, armorSetCapability.getSetEffects());
        armorSetCapability.removeAllSetEffects();
        armorSetCapability.clearSetID();
    }

    public static void applyArmorSetEffectToPlayer(@Nonnull ServerPlayerEntity player, @Nonnull ArmorSetCapability armorSetCapability, @Nonnull ArmorSet armorSet) {
        // Clear set effects stored in the capability and store new SetID
        PlayerSetEffects.removeSetEffectsFromPlayer(player, armorSetCapability.getSetEffects());
        armorSetCapability.removeAllSetEffects();
        armorSetCapability.setSetID(armorSet.getName());

        // Get armor set effects and apply them to player
        List<PotionEffect> setEffects = armorSet.getPotionEffects();
        armorSetCapability.addSetEffectList(setEffects);
        PlayerSetEffects.addSetEffectsToPlayer(player, setEffects);
    }

    public static void storeUsedPotionEffectsOfPlayer(@Nonnull ServerPlayerEntity player, @Nonnull ArmorSetCapability armorSetCapability) {
        // Go through active effects on the Player and store them in the usedPotionEffects
        Collection<EffectInstance> potionEffectsPlayer = player.getActiveEffects();
        for (EffectInstance effectInstance : potionEffectsPlayer) {
            ResourceLocation effectResLoc = effectInstance.getEffect().getRegistryName();
            if (effectResLoc == null) {
                RealisticArmorTiers.LOGGER.warn("Could not find ResourceLocation of " + effectInstance.getDescriptionId());
                continue;
            }
            PotionEffect usedPotion = new PotionEffect(
                    effectResLoc.getNamespace() + ":" + effectInstance.getEffect().getRegistryName().getPath(),
                    effectInstance.getDuration(),
                    effectInstance.getAmplifier());

            armorSetCapability.addUsedPotionEffect(usedPotion);
        }
    }
}
