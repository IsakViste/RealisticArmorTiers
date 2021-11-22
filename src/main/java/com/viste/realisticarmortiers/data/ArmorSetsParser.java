package com.viste.realisticarmortiers.data;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.viste.realisticarmortiers.RealisticArmorTiers;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ArmorSetsParser {
	public static final String CONFIG_PATH = FMLPaths.GAMEDIR.get() + "/config/" + RealisticArmorTiers.MODID + "/";
	public static final String JSON_SETS_FILE = "equipment_sets.json";
	public static final String ASSET_SETS_PATH = "/assets/" + RealisticArmorTiers.MODID + "/" + JSON_SETS_FILE;
	public static final String JSON_CONFIG_SETS_PATH = CONFIG_PATH + JSON_SETS_FILE;

	private Armors armorSets;
	private static final Logger log = RealisticArmorTiers.LOGGER;

	/**
	 * Takes care of creating or finding the JSON from which to load the armor sets
	 */
	public ArmorSetsParser() {
		if (!findOrCreateConfigFolder()) {
			return;
		}

		if (!findJSONOrCopyFromAssets()) {
			return;
		}

		loadArmorSets();
	}

	public Armors getArmorSets() {
		return this.armorSets;
	}

	public void loadArmorSets() {
		this.armorSets = loadArmorSetsFromJSON();
	}

	/**
	 * Create personal sub-folder in the main config folder to hold our JSONs
	 * @see assets.realisticarmortiers
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
	 * Copy the default JSON equipment_sets.json from assets to the config folder
	 * Will return early if the JSON file already exists there
	 * @see assets.realisticarmortiers
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
	 * Load the Armor sets from the JSON equipment_sets.json
	 * @see assets.realisticarmortiers
	 */
	private Armors loadArmorSetsFromJSON() {
		Armors armors = new Armors();

		// JSON File loading
		try {
			log.info("(JSON File) Loading");
			List<JsonSets> sets;

			log.info("-> (File Read) Reading JSON file");
			try {
				Gson gson = new Gson();

				// Sets
				BufferedReader brSets = new BufferedReader(new FileReader(JSON_CONFIG_SETS_PATH));
				Type typeSets = new TypeToken<List<JsonSets>>(){}.getType();
				sets = gson.fromJson(brSets, typeSets);

			} catch (Exception e) {
				log.fatal("-> (File Read) Reading Failure");
				log.fatal(e);
				return armors;
			}
			log.info("-> (File Read) Reading Success");

			// Make Armors based on JSON
			Set<String> ids = new HashSet<>();
			List<Item> helmet;
			List<Item> chestplate;
			List<Item> leggings;
			List<Item> boots;

			log.info("-> (Armors) Loading Sets");
			for (JsonSets set : sets) {
				log.info("| [" + set.getId() + "]");
				if(set.getId().isEmpty() || !ids.add(set.getId())) {
					log.warn("|---> ID: " + set.getId() + " already exists or is empty! " +
							"This set is not loaded, make sure no sets have the same ID in equipment_sets.json");
					continue;
				}
				helmet = makeItemListFromStringList(set.getHelmet());
				chestplate = makeItemListFromStringList(set.getChestplate());
				leggings = makeItemListFromStringList(set.getLeggings());
				boots = makeItemListFromStringList(set.getBoots());

				ArmorSet armorSet = new ArmorSet(set.getId(), helmet, chestplate, leggings, boots, set.validateAndGetPotionEffects());
				armors.addArmorSet(armorSet);
				log.info("|-> successfully added");
			}

			log.info("-> (Armors) Loading Successful.");
		} catch (Exception e) {
			log.fatal("(JSON File) Loading Failure");
			log.fatal(e);
			return armors;
		}

		log.info("(JSON File) Loading Success");
		return armors;
	}

	/**
	 * Convert strings to armor Item
	 * @param armors list of strings corresponding to the armor id
	 * @return a list of Item
	 * @see ResourceLocation ResourceLocation for the armor id
	 */
	private List<Item> makeItemListFromStringList(List<String> armors) {
		List<Item> items = new ArrayList<>();
		for (String armor : armors) {
			if(armor.isEmpty()) {
				continue;
			}

			Item armorItem = ForgeRegistries.ITEMS.getValue(new ResourceLocation(armor));
			if(armorItem == null) {
				log.error("-> (Armors) Could not find " + armor);
				continue;
			}
			items.add(armorItem);
		}
		return items;
	}
}





