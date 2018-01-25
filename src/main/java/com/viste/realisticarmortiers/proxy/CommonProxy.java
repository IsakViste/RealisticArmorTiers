package com.viste.realisticarmortiers.proxy;

import com.viste.realisticarmortiers.events.EventEquipmentSets;

import net.minecraftforge.fml.common.FMLCommonHandler;

public class CommonProxy implements IProxy {
	
	public void preInit() {
		
	}

	public void init() {
		
	}

	public void postInit() {
		FMLCommonHandler.instance().bus().register(new EventEquipmentSets());
	}

}
