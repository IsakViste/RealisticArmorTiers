package com.viste.realisticarmortiers;

import com.viste.realisticarmortiers.capability.CapabilityAttachEventHandler;
import com.viste.realisticarmortiers.capability.ArmorSet;
import com.viste.realisticarmortiers.events.EventEquipmentSets;
import com.viste.realisticarmortiers.events.EventEquipmentSetsClearActivePotion;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
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
	}

	@CapabilityInject(ArmorSet.class)
	public static Capability<ArmorSet> CAPABILITY_ARMOR_SET = null;

	@SubscribeEvent
	public static void onCommonSetupEvent(FMLCommonSetupEvent event) {
		CapabilityManager.INSTANCE.register(
				ArmorSet.class,
				new ArmorSet.ArmorSetNBTStorage(),
				ArmorSet::createADefaultInstance
		);
		MOD_EVENT_BUS.register(new EventEquipmentSetsClearActivePotion());
		MOD_EVENT_BUS.register(CapabilityAttachEventHandler.class);
		MOD_EVENT_BUS.register(new EventEquipmentSets());
	}
}
