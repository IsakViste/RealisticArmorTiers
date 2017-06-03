package com.viste.realisticarmortiers;

import java.io.File;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.viste.realisticarmortiers.proxy.IProxy;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid=Reference.MODID, name=Reference.NAME, version=Reference.VERSION)
public class RealisticArmorTiers {
	
	private static final Logger log = LogManager.getLogger(Reference.MODID);
	
	@Instance(Reference.MODID)
    public static RealisticArmorTiers instance;
	
	@SidedProxy(clientSide=Reference.CLIENT_PROXY, serverSide=Reference.SERVER_PROXY)
	public static IProxy proxy;
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		log.info("PreInitializing.");
		
		proxy.preInit();
		
		log.info("PreInitializing DONE.");
	}
	
	@EventHandler
	public void init(FMLInitializationEvent event) {
		log.info("Initializing.");
		
		proxy.init();
		
		log.info("Initializing DONE.");
	}
	
	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		log.info("PostInitializing.");
		
		proxy.postInit();
		
		log.info("PostInitializing DONE.");
	}
	
}
