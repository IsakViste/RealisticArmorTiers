package com.viste.realisticarmortiers;

import com.viste.realisticarmortiers.data.ArmorSet;
import com.viste.realisticarmortiers.data.ArmorSetsParser;
import com.viste.realisticarmortiers.data.PotionEffect;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingEquipmentChangeEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;


@Mod(RealisticArmorTiers.MODID)
public class RealisticArmorTiers {
    public static final String MODID = "realisticarmortiers";
    public static final String NAME = "Realistic Armor Tiers";

    public static final Logger LOGGER = LogManager.getLogger(NAME);

    private final int POTION_DURATION = Integer.MAX_VALUE; // 20 ticks ~= 1 second
    private final ArrayList<ItemStack> EMPTY_CURATIVE_ITEMS_LIST = new ArrayList<>();
    private final ArmorSetsParser armorSetsParser;

    public RealisticArmorTiers() {
        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);

        armorSetsParser = new ArmorSetsParser();
    }



    /**
     * Function called whenever a player logs in
     * @param event the PlayerLoggedInEvent fired
     */
    @SubscribeEvent
    public void onPlayerLogOut(PlayerEvent.PlayerLoggedOutEvent event) {
        if (event.getPlayer() instanceof ServerPlayerEntity) {
             /* Clear active potion effects of Players joining world, this is to prevent the player of keeping their
                quasi-infinite potion effect applied by the armor set.
              */
            ServerPlayerEntity playerEntity = (ServerPlayerEntity)event.getEntityLiving();
            ArmorSet armorSet = armorSetsParser.getArmorSets().getFullSetArmorThatPlayerIsWearing(playerEntity);
            LOGGER.info("Removing set effects of " + armorSet + " from player " + playerEntity.getDisplayName().getString());
            if (armorSet != null) {
                for (PotionEffect potionEffect : armorSet.getPotionEffects()) {
                    removeSetEffectFromPlayer(playerEntity, potionEffect);
                }
            }
        }
    }

    /**
     * Function called whenever a living entity changes equipment
     * @param event the LivingEquipmentChangeEvent fired
     */
    @SubscribeEvent
    public void onPlayerInventoryChange(LivingEquipmentChangeEvent event) {
        // Must be player, don't care about other living entities
        if (event.getEntityLiving() instanceof ServerPlayerEntity) {
            ServerPlayerEntity playerEntity = (ServerPlayerEntity)event.getEntityLiving();
            // We only care about changes to the Head, Chest, Legs and Feet slots
            // > Main andi off-hand items support can be added here!
            if (event.getSlot() == EquipmentSlotType.HEAD || event.getSlot() == EquipmentSlotType.CHEST
                    || event.getSlot() == EquipmentSlotType.LEGS || event.getSlot() == EquipmentSlotType.FEET) {

                // Get changed item to find out what ArmorSet the player was wearing before the change
                // This could probably be much better if we stored per player the active set effect
                // This is especially heavy if we have many, many armor sets we need to go through
                ArmorSet oldArmorSet = armorSetsParser.getArmorSets().getOldArmorSetPlayerWasWearing(playerEntity, event.getFrom().getItem().getItem(), event.getSlot());
                if (oldArmorSet != null) {
                    // Remove potion effects that were added by the previous ArmorSet
                    for (PotionEffect potionEffect : oldArmorSet.getPotionEffects()) {
                        removeSetEffectFromPlayer(playerEntity, potionEffect);
                    }
                }

                // Get the current equipped ArmorSet from the armor slots
                // Again possibly heavy operation if we have many, many armors sets we need to go through
                ArmorSet armorSet = armorSetsParser.getArmorSets().getFullSetArmorThatPlayerIsWearing(playerEntity);
                if (armorSet != null) {
                    // Apply all the potion effects the set gives to the player
                    for (PotionEffect potionEffect : armorSet.getPotionEffects()) {
                        // Get the Effect from the registry of Potions
                        Effect effect = ForgeRegistries.POTIONS.getValue(new ResourceLocation(potionEffect.id));
                        if(effect == null) {
                            LOGGER.warn("(Potion Effect) Could not find " + potionEffect.id + " to apply to " + playerEntity.getDisplayName().getString());
                            continue;
                        }

                        // Create new effect instance with the duration and efficiency/amplitude
                        EffectInstance effectInstance = new EffectInstance(effect, POTION_DURATION, potionEffect.efficiency - 1);

                        // Make so that no item can cure the effect (milk bucket is by default, so we remove it)
                        effectInstance.setCurativeItems(EMPTY_CURATIVE_ITEMS_LIST);
                        playerEntity.addEffect(effectInstance);
                    }
                }
            }
        }
    }

    /**
     * Remove a set effect that the player has been given by wearing a full set
     * If the player does not have the potion effect applied, log it and do nothing
     * @param player the player of which to remove the effect
     * @param potionEffect the potion effect to remove
     */
    private void removeSetEffectFromPlayer(ServerPlayerEntity player, PotionEffect potionEffect) {
        // Get Effect from the registry of Potions
        Effect effect = ForgeRegistries.POTIONS.getValue(new ResourceLocation(potionEffect.id));
        if(effect == null) {
            LOGGER.warn("Could not remove set effect " + potionEffect.id);
            return;
        }

        // Get the EffectInstance from the player corresponding to the potion Effect we want to remove
        // The effect instance corresponds
        EffectInstance playerEffect = player.getEffect(effect);

        if(playerEffect == null) {
            LOGGER.warn("Set effect " + potionEffect.id + " could not be removed on player "
                    + player.getDisplayName().getString());
            return;
        }

        // Only remove the effect of same efficiency/amplifier as the one from the set
        // This prevents us from removing potion effects that are better than the set effect
        // E.G. Set gives speed 2, but speed 3 potion has been consumed, don't remove speed 3 effect
        if(playerEffect.getAmplifier() == potionEffect.efficiency - 1) {
            // PROBLEM: Does not remove potion effect from hidden potion effects
            // E.G. in the above example, speed 2 is saved in the hidden potion effects, but not removed by this call
            // meaning that one could remove their set, and wait for the hidden potion effect to be reapplied when
            // the parent potion effect (speed 3 here) runs out. This would give infinite speed 2 even without the set.
            player.removeEffect(effect);
        }
    }
}