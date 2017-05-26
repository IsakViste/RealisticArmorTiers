package dk.viste.isak.redstoneguns;

import org.apache.logging.log4j.Logger;

import dk.viste.isak.redstoneguns.proxy.CommonProxy;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = RedstoneGuns.MODID, name = RedstoneGuns.MODNAME, version = RedstoneGuns.MODVERSION, useMetadata = true)
public class RedstoneGuns {

    public static final String MODID = "redstoneguns";
    public static final String MODNAME = "RedstoneGuns";
    public static final String MODVERSION = "1.0";

    @SidedProxy(clientSide = "dk.viste.isak.redstoneguns.proxy.ClientProxy", serverSide = "dk.viste.isak.redstoneguns.proxy.ServerProxy")
    public static CommonProxy proxy;

    @Mod.Instance
    public static RedstoneGuns instance;

    public static Logger logger;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        logger = event.getModLog();
        proxy.preInit(event);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent e) {
        proxy.init(e);
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent e) {
        proxy.postInit(e);
    }
}