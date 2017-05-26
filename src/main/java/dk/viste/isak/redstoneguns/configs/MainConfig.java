package dk.viste.isak.redstoneguns.configs;

import org.apache.logging.log4j.Level;

import dk.viste.isak.redstoneguns.RedstoneGuns;
import dk.viste.isak.redstoneguns.proxy.CommonProxy;
import net.minecraftforge.common.config.Configuration;

public class MainConfig {

    private static final String CATEGORY_GENERAL = "general";
    private static final String CATEGORY_WEAPONS = "weapons";

    // This values below you can access elsewhere in your mod:
    public static int baseDamage = 2;

    // Call this from CommonProxy.preInit(). It will create our config if it doesn't
    // exist yet and read the values if it does exist.
    public static void readConfig() {
        Configuration cfg = CommonProxy.config;
        try {
            cfg.load();
            initGeneralConfig(cfg);
            initWeaponsConfig(cfg);
        } catch (Exception e1) {
            RedstoneGuns.logger.log(Level.ERROR, "Problem loading config file!", e1);
        } finally {
            if (cfg.hasChanged()) {
                cfg.save();
            }
        }
    }

    private static void initGeneralConfig(Configuration cfg) {
        cfg.addCustomCategoryComment(CATEGORY_GENERAL, "General configuration");
        // cfg.getBoolean() will get the value in the config if it is already specified there. If not it will create the value.
        // isThisAGoodTutorial = cfg.getBoolean("goodTutorial", CATEGORY_GENERAL, isThisAGoodTutorial, "Set to false if you don't like this tutorial");
        // yourRealName = cfg.getString("realName", CATEGORY_GENERAL, yourRealName, "Set your real name here");
    }

    private static void initWeaponsConfig(Configuration cfg) {
        cfg.addCustomCategoryComment(CATEGORY_WEAPONS, "Weapons configuration");
        
        // Weapons
        baseDamage = cfg.getInt("weaponBaseDamage", CATEGORY_WEAPONS, baseDamage, 1, 100, "Base damage in half-hearts to calculate real damage.");
    }
}