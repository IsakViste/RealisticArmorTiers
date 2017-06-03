package com.viste.realisticarmortiers;

import java.io.File;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.viste.realisticarmortiers.events.EventManager;
import com.viste.realisticarmortiers.proxy.IProxy;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid=Reference.MODID, name=Reference.NAME, version=Reference.VERSION)
public class RealisticArmorTiers {
	
	private static final Logger LOGGER = LogManager.getLogger("RAT|Main");
	
	@SidedProxy(clientSide=Reference.CLIENT_PROXY, serverSide=Reference.SERVER_PROXY)
	public static IProxy proxy;
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		LOGGER.info("PreInitializing.");
		
		EventManager.init(new File(event.getModConfigurationDirectory(), Reference.CONFIG_PATH));
		proxy.preInit();
		
		LOGGER.info("PreInitializing DONE.");
	}
	
	@EventHandler
	public void init(FMLInitializationEvent event) {
		LOGGER.info("Initializing.");
		
		proxy.init();
		
		LOGGER.info("Initializing DONE.");
	}
	
	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		LOGGER.info("PostInitializing.");
		
		proxy.postInit();
		
		LOGGER.info("PostInitializing DONE.");
	}
	
}
