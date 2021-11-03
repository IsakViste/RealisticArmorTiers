package com.viste.realisticarmortiers.data;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.viste.realisticarmortiers.RealisticArmorTiers;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ArmorSetsParser {
	public static final String CONFIG_PATH = FMLPaths.GAMEDIR.get() + "/config/" + RealisticArmorTiers.MODID + "/";
	public static final String JSON_SETS_FILE = "equipment_sets.json";
	public static final String ASSET_SETS_PATH = "/assets/" + RealisticArmorTiers.MODID + "/" + JSON_SETS_FILE;
	public static final String JSON_CONFIG_SETS_PATH = CONFIG_PATH + JSON_SETS_FILE;

	private Armors armorSets;
	private static final Logger log = RealisticArmorTiers.LOGGER;
	
	public ArmorSetsParser() {
		if (!findOrCreateConfigFolder()) {
			return;
		}

		if (!findJSONOrCopyFromAssets()) {
			return;
		}

		this.armorSets = loadArmorSetsFromJSON();
	}

	public Armors getArmorSets() {
		return this.armorSets;
	}

	/**
	 * Create personal sub-folder in the main config folder to hold our JSONs
	 */
	private boolean findOrCreateConfigFolder() {
		// Check / Copy the config files into the config folder
		File configDir = new File(CONFIG_PATH);

		// Create config folder
		if(!configDir.exists()) {
			log.info("(Config Folder) Creating " + CONFIG_PATH);
			try {
				if(!configDir.mkdirs()) {
					log.error("(Config Folder) Creation Failed");
				}
			} catch (Exception e) {
				log.fatal("(Config Folder) Creation Failed Horribly");
				log.fatal(e);
				return false;
			}
			log.info("(Config Folder) Creation Success");
		} else {
			log.info("(Config Folder) Found " + CONFIG_PATH);
		}

		return true;
	}

	/**
	 * Copy the default equipment_sets JSON file from assets to the config folder
	 * Will return early if JSON already exists there
	 */
	private boolean findJSONOrCopyFromAssets() {
		File file = new File(JSON_CONFIG_SETS_PATH);

		if(!file.exists()) {
			log.info("(JSON File) Copying " + ASSET_SETS_PATH);
			try {
				if(!file.createNewFile()) {
					log.warn("(JSON File) Could not create file. Does it already exist?");
					return true;
				}
				
				InputStream instream = this.getClass().getResourceAsStream(ASSET_SETS_PATH);
				FileOutputStream outstream = new FileOutputStream(file);
				
				byte[] buffer = new byte[1024];
				int length;

				if(instream != null) {
					while ((length = instream.read(buffer)) > 0) {
						outstream.write(buffer, 0, length);
					}

					instream.close();
				}

				outstream.close();
			} catch (Exception e) {
				log.fatal("(JSON File) Copy Failed");
				log.fatal(e);
				return false;
			}
			log.info("(JSON File) Copy Success");
		} else {
			log.info("(JSON File) Found " + ASSET_SETS_PATH);
		}

		return true;
	}

	/**
	 * Load the Armor sets from the JSON
	 */
	private Armors loadArmorSetsFromJSON() {
		Armors armors = new Armors();

		// JSON File loading
		try {
			log.info("(JSON File) Loading");
			List<JsonSets> sets;

			log.info("-> (File Read) Reading JSON files");
			try {
				Gson gson = new Gson();

				// Sets
				BufferedReader brSets = new BufferedReader(new FileReader(JSON_CONFIG_SETS_PATH));
				Type typeSets = new TypeToken<List<JsonSets>>(){}.getType();
				sets = gson.fromJson(brSets, typeSets);

			} catch (Exception e) {
				log.fatal("-> (File Read) Reading Failure");
				log.fatal(e);
				return null;
			}
			log.info("-> (File Read) Reading Success");

			try {
				//Make Armors based on JSON
				List<ArmorItem> helmet;
				List<ArmorItem> chestplate;
				List<ArmorItem> leggings;
				List<ArmorItem> boots;
				for (JsonSets set : sets) {
					helmet = makeItemArmorListFromStringList(set.helmet);
					chestplate = makeItemArmorListFromStringList(set.chestplate);
					leggings = makeItemArmorListFromStringList(set.leggings);
					boots = makeItemArmorListFromStringList(set.boots);

					Armor armor = new Armor(new ArmorSet(set.name, helmet, chestplate, leggings, boots), set.potionEffects);
					armors.addArmor(armor);
				}

				log.info("-> (Armors) Loading Successful. Armor Sets Loaded:");
				for(Armor armor : armors.getArmors()) {
					log.info("|-> " + armor);
				}
			} catch (Exception e) {
				log.fatal("-> (Armors) Loading Failure");
				log.fatal(e);
			}
		} catch (Exception e) {
			log.fatal("(JSON File) Loading Failure");
			log.fatal(e);
			return null;
		}
		log.info("(JSON File) Loading Success");

		return armors;
	}

	/**
	 * Convert strings to armor Item
	 * @param armors list of strings corresponding to the armor id
	 * @return a list of ArmorItem
	 * @see ResourceLocation ResourceLocation for the armor id
	 */
	private List<ArmorItem> makeItemArmorListFromStringList(List<String> armors) {
		List<ArmorItem> items = new ArrayList<>();
		for (String armor : armors) {
			if(armor.isEmpty()) {
				continue;
			}

			Item armorItem = ForgeRegistries.ITEMS.getValue(new ResourceLocation(armor));
			if(armorItem == null) {
				log.error("-> (Armors) Could not find " + armor);
				continue;
			}
			items.add((ArmorItem)armorItem);
		}
		return items;
	}
}





