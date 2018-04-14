package com.viste.realisticarmortiers.proxy;

import com.viste.realisticarmortiers.capability.Armor;
import com.viste.realisticarmortiers.capability.ArmorHandler;
import com.viste.realisticarmortiers.capability.ArmorStorage;
import com.viste.realisticarmortiers.capability.IArmor;
import com.viste.realisticarmortiers.events.EventEquipmentSets;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.CapabilityManager;


public class CommonProxy implements IProxy {
	
	public void preInit() {
		
	}

	public void init() {
		
	}

	public void postInit() {
		CapabilityManager.INSTANCE.register(IArmor.class, new ArmorStorage(), Armor.class);
		MinecraftForge.EVENT_BUS.register(new EventEquipmentSets());
		MinecraftForge.EVENT_BUS.register(new ArmorHandler());
	}
}
