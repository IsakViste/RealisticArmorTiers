package dk.viste.isak.redstoneguns.proxy;

import java.io.File;

import dk.viste.isak.redstoneguns.configs.MainConfig;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class CommonProxy {
	// Config instance
    public static Configuration config;
	
    public void preInit(FMLPreInitializationEvent e) {
    	// Config file
    	File directory = e.getModConfigurationDirectory();
        config = new Configuration(new File(directory.getPath(), "RedstoneGuns.cfg"));
        MainConfig.readConfig();
    	
        // Initialization of blocks and items typically goes here:
        //ModBlocks.init();
        //ModItems.init();
        //ModCrafting.init();
    }

    public void init(FMLInitializationEvent e) {

    }

    public void postInit(FMLPostInitializationEvent e) {
    	// Config file
    	if (config.hasChanged()) {
            config.save();
        }
    }
}