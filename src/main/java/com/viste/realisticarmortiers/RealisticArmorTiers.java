package com.viste.realisticarmortiers;

import com.viste.realisticarmortiers.capability.ArmorSetCapability;
import com.viste.realisticarmortiers.capability.CapabilityArmorSet;
import com.viste.realisticarmortiers.data.ArmorSetsParser;
import com.viste.realisticarmortiers.events.StartupCommon;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;


@Mod(RealisticArmorTiers.MODID)
public class RealisticArmorTiers {
    public static final String MODID = "realisticarmortiers";
    public static final String NAME = "Realistic Armor Tiers";

    public static final Logger LOGGER = LogManager.getLogger(NAME);

    public static final ArmorSetsParser ARMOR_SETS_PARSER = new ArmorSetsParser();

    public RealisticArmorTiers() {
        FMLJavaModLoadingContext.get().getModEventBus().register(StartupCommon.class);
    }

    /**
     * Reload RealisticArmorTiers equipment_set.json which contains all sets that players can wear to get
     * potion effects. This allows the player to reload the JSON mid-game, should he/she want to make changes to
     * the JSON without having to restart the whole client!
     */
    public static void reloadJSON(@Nullable ServerPlayerEntity player) {
        ARMOR_SETS_PARSER.loadArmorSets();

        if(player != null) {
            ArmorSetCapability armorSetCapability = player.getCapability(CapabilityArmorSet.CAPABILITY_ARMOR_SET).orElse(null);
//            EventEquipmentSets.checkArmorSetForPlayer(player, armorSet);
        }
    }
}