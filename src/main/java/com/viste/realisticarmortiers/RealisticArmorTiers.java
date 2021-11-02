package com.viste.realisticarmortiers;

import com.viste.realisticarmortiers.capability.Armor;
import com.viste.realisticarmortiers.capability.ArmorHandler;
import com.viste.realisticarmortiers.capability.ArmorStorage;
import com.viste.realisticarmortiers.capability.IArmor;
import com.viste.realisticarmortiers.events.EventEquipmentSets;
import com.viste.realisticarmortiers.events.EventEquipmentSetsClearActivePotion;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraftforge.fml.common.Mod;


@Mod(Reference.MODID)
public class RealisticArmorTiers {
	public static final Logger LOGGER = LogManager.getLogger(Reference.MODID);

	// get a reference to the event bus for this mod;  Registration events are fired on this bus.
	public static IEventBus MOD_EVENT_BUS;

	public RealisticArmorTiers() {

		MOD_EVENT_BUS = FMLJavaModLoadingContext.get().getModEventBus();

		registerCommonEvents();
	}

	public static void registerCommonEvents() {
		CapabilityManager.INSTANCE.register(IArmor.class, new ArmorStorage(), Armor.class);
		MinecraftForge.EVENT_BUS.register(new EventEquipmentSetsClearActivePotion());
		MinecraftForge.EVENT_BUS.register(new ArmorHandler());
		MinecraftForge.EVENT_BUS.register(new EventEquipmentSets());
	}
}
