package com.viste.realisticarmortiers.logic;

import com.viste.realisticarmortiers.RealisticArmorTiers;
import com.viste.realisticarmortiers.data.PotionEffect;
import com.viste.realisticarmortiers.events.EventEquipmentSets;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class PlayerSetEffects {

    private static final ArrayList<ItemStack> EMPTY_CURATIVE_ITEMS_LIST = new ArrayList<>();

    public static void addSetEffectsToPlayer(ServerPlayerEntity player, List<PotionEffect> setEffects) {
        for (PotionEffect setEffect : setEffects) {
            addSetEffectToPlayer(player, setEffect);
        }
    }

    public static void addSetEffectToPlayer(ServerPlayerEntity player, PotionEffect setEffect) {
        Effect effect = setEffect.getEffect();
        EffectInstance effectInstance = new EffectInstance(effect, setEffect.getDuration(), setEffect.getAmplifier());
        effectInstance.setCurativeItems(EMPTY_CURATIVE_ITEMS_LIST);
        RealisticArmorTiers.LOGGER.info("Adding Set Effect " + effectInstance + " to " + player.getDisplayName().getString());
        EventEquipmentSets.incrementPlayerPotionAddedIgnore();
        if(!player.addEffect(effectInstance)) {
            EventEquipmentSets.decrementPlayerPotionAddedIgnore();
        }
    }

    public static void removeSetEffectsFromPlayer(ServerPlayerEntity player, List<PotionEffect> setEffects) {
        for (PotionEffect setEffect : setEffects) {
            removeSetEffectFromPlayer(player, setEffect);
        }
    }

    public static void removeSetEffectFromPlayer(ServerPlayerEntity player, PotionEffect setEffect) {
        Effect effect = setEffect.getEffect();
        if (player.hasEffect(effect)) {
            RealisticArmorTiers.LOGGER.info("Removing Set Effect " + effect.getRegistryName() + " from " + player.getDisplayName().getString());
            EventEquipmentSets.incrementPlayerPotionRemovedIgnore();
            if(!player.removeEffect(effect)) {
                EventEquipmentSets.decrementPlayerPotionRemovedIgnore();
            }
        }
    }

    public static void addUsedPotionEffectsToPlayer(ServerPlayerEntity player, List<PotionEffect> usedPotionEffects) {
        Iterator<PotionEffect> i = usedPotionEffects.iterator();
        while (i.hasNext()) {
            PotionEffect setEffect = i.next();
            if(setEffect.getDuration() > 0) {
                Effect effect = setEffect.getEffect();
                if(!player.hasEffect(effect)) {
                    EventEquipmentSets.incrementPlayerPotionAddedIgnore();
                    if(!player.addEffect(new EffectInstance(effect, setEffect.getDuration(), setEffect.getAmplifier()))) {
                        EventEquipmentSets.decrementPlayerPotionAddedIgnore();
                    }
                    i.remove();
                }
            } else {
                i.remove();
            }
        }
    }
}
