package com.viste.realisticarmortiers;

import com.viste.realisticarmortiers.data.Armor;
import com.viste.realisticarmortiers.data.ArmorSetsParser;
import com.viste.realisticarmortiers.data.PotionEffect;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
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

import java.util.List;
import java.util.Set;


@Mod(RealisticArmorTiers.MODID)
public class RealisticArmorTiers {
    public static final String MODID = "realisticarmortiers";
    public static final String NAME = "Realistic Armor Tiers";
    public static final String VERSION = "2.0";

    private final int POTION_DURATION = Integer.MAX_VALUE; // 20 ticks ~= 1 second

    public static final Logger LOGGER = LogManager.getLogger(NAME);

    private final ArmorSetsParser armorSetsParser;

    public RealisticArmorTiers() {
        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);

        armorSetsParser = new ArmorSetsParser();
    }

    @SubscribeEvent
    public void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getPlayer() instanceof ServerPlayerEntity) {
             /* Clear active potion effects of Players joining world, this is to prevent the player of keeping their
                quasi-infinite potion effect applied by the armor set.
              */
            LOGGER.info("Removing all effects from " + event.getPlayer().getDisplayName().getString());
            event.getPlayer().removeAllEffects();
        }
    }

    @SubscribeEvent
    public void onPlayerInventoryChange(LivingEquipmentChangeEvent event) {
        // Must be player, don't care about other living entities
        if (event.getEntityLiving() instanceof ServerPlayerEntity) {
            ServerPlayerEntity playerEntity = (ServerPlayerEntity)event.getEntityLiving();
            // We only care about changes to the Head, Chest, Legs and Feet slots
            // > Main and off-hand items support can be added here!
            if (event.getSlot() == EquipmentSlotType.HEAD || event.getSlot() == EquipmentSlotType.CHEST
                    || event.getSlot() == EquipmentSlotType.LEGS || event.getSlot() == EquipmentSlotType.FEET) {
                Armor armor = armorSetsParser.getArmorSets().getFullSetArmorThatPlayerIsWearing(playerEntity);
                playerEntity.removeAllEffects();
                if (armor != null) {
                    for (PotionEffect potionEffect : armor.getPotionEffects()) {
                        Effect effect = ForgeRegistries.POTIONS.getValue(new ResourceLocation(potionEffect.id));
                        playerEntity.addEffect(new EffectInstance(effect, POTION_DURATION, potionEffect.efficiency - 1));
                    }
                }
            }
        }
    }
}