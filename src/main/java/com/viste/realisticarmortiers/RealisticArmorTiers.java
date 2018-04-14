package com.viste.realisticarmortiers;

import java.io.File;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.viste.realisticarmortiers.network.ModidPacketHandler;
import com.viste.realisticarmortiers.network.NetworkMessageHandler;
import com.viste.realisticarmortiers.network.NetworkMessageInt;
import com.viste.realisticarmortiers.proxy.IProxy;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

@Mod(modid=Reference.MODID, name=Reference.NAME, version=Reference.VERSION)
public class RealisticArmorTiers {
	static int discriminator = 0; //Hehe racism
	public File configFile = null;
	
	private static final Logger log = LogManager.getLogger(Reference.MODID);
	
	@Instance(Reference.MODID)
    public static RealisticArmorTiers instance;
	
	@SidedProxy(clientSide=Reference.CLIENT_PROXY, serverSide=Reference.SERVER_PROXY)
	public static IProxy proxy;
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		log.info("PreInitializing");
		configFile = event.getModConfigurationDirectory();
		
		proxy.preInit();
	}
	
	@EventHandler
	public void init(FMLInitializationEvent event) {
		log.info("Initializing");
		
		proxy.init();
	}
	
	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		log.info("PostInitializing");
		//ModidPacketHandler.INSTANCE.registerMessage(NetworkMessageHandler.class, NetworkMessageInt.class, ++discriminator, Side.SERVER);
		proxy.postInit();
	}
}
