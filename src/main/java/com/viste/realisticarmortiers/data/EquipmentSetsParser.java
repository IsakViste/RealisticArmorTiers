package com.viste.realisticarmortiers.data;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import com.viste.realisticarmortiers.RealisticArmorTiers;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.loading.FMLConfig;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.viste.realisticarmortiers.Reference;

import net.minecraft.item.ArmorItem;

public class EquipmentSetsParser {	
	public EventEquipmentGlobalVar global;
	public Armors armors = new Armors();
	private static final Logger log = RealisticArmorTiers.LOGGER;
	
	public EquipmentSetsParser() {
		this.global = new EventEquipmentGlobalVar();
		// Check / Copy the config files into the config folder
		File configDir = new File(FMLConfig.defaultConfigPath() + Reference.CONFIG_PATH);
		File jsonConfigSets = new File(FMLConfig.defaultConfigPath() + Reference.JSON_CONFIG_SETS_PATH);
		
		// Create config folder
		if(!configDir.exists()) {
			log.info("(Config Folder) Creating " + Reference.CONFIG_PATH);
			try {
				configDir.mkdirs();
			} catch (SecurityException se) {
				log.fatal("(Config Folder) Creation Failed");
				log.fatal(se);
				return;
			}
			log.info("(Config Folder) Creation Success");
		} else {
			log.info("(Config Folder) Found " + Reference.CONFIG_PATH);
		}
		
		// Create SETS config
		copyFile(jsonConfigSets, Reference.ASSET_SETS_PATH);
		
		// JSON File loading
		try {
			log.info("(JSON File) Loading");
			List<JsonSets> sets;
			
			log.info("-> (File Read) Reading JSON files");
			try {
				Gson gson = new Gson();
				
				// Sets
				BufferedReader brSets = new BufferedReader(new FileReader(FMLConfig.defaultConfigPath() + Reference.JSON_CONFIG_SETS_PATH));
				Type typeSets = new TypeToken<List<JsonSets>>(){}.getType();
				sets = gson.fromJson(brSets, typeSets);
				
			} catch (IOException ioe) {
				log.fatal("-> (File Read) Reading Failure");
				log.fatal(ioe);
				return;
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

					Armor armor = new Armor(new Sets(set.name, helmet, chestplate, leggings, boots), set.potionEffects);
					armors.addArmor(armor);
				}

				log.info("-> (Armors) Loading Success");
				System.out.println(armors);
			} catch (Exception e) {
				log.fatal("-> (Armors) Loading Failure");
				log.fatal(e);
			}
		} catch (Exception e) {
			log.fatal("(JSON File) Loading Failure");
			log.fatal(e);
			return;
		}
		log.info("(JSON File) Loading Success");
	}

	public List<ArmorItem> makeItemArmorListFromStringList(List<String> armors) {
		List<ArmorItem> items = new ArrayList<>();
		for (String armor : armors) {
			RegistryObject<ArmorItem> armorObj = RegistryObject.of(new ResourceLocation(armor), ForgeRegistries.ITEMS);
			if (!armorObj.isPresent()) {
				log.warn("-> (Armors) Could not find " + armorObj);
			}
			items.add(armorObj.get());
		}
		return items;
	}
		
	// Copy from 'asset' into 'file'
	void copyFile(File file, String asset) {
		if(!file.exists()) {
			log.info("(JSON File) Copying " + asset);
			try {
				file.createNewFile();
				
				InputStream instream = this.getClass().getResourceAsStream(asset);
				FileOutputStream outstream = new FileOutputStream(file);
				
				byte[] buffer = new byte[1024];
				int length;
				
				while ((length = instream.read(buffer)) > 0) {
					outstream.write(buffer, 0, length);
				}
				
				instream.close();
				outstream.close();
			} catch (IOException ioe) {
				log.fatal("(JSON File) Copy Failed");
				log.fatal(ioe);
				return;
			}
			log.info("(JSON File) Copy Success");
		} else {
			log.info("(JSON File) Found " + asset); 
		}
	}
}





