package com.viste.realisticarmortiers.proxy;

import com.viste.realisticarmortiers.events.EventEquipementSets;

import net.minecraftforge.fml.common.FMLCommonHandler;

public class CommonProxy implements IProxy {
	
	public void preInit() {
		FMLCommonHandler.instance().bus().register(new EventEquipementSets());
	}

	public void init() {
		
	}

	public void postInit() {
		
	}

}
