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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.viste.realisticarmortiers.RealisticArmorTiers;
import com.viste.realisticarmortiers.Reference;
import com.viste.realisticarmortiers.data.JsonSets;

import net.minecraft.item.ItemArmor;

public class EquipmentSetsParser {	
	public EventEquipmentGlobalVar global;
	public Armors armors = new Armors();
	public Tiers tiers = new Tiers();
	private static final Logger log = LogManager.getLogger(Reference.MODID);
	
	public EquipmentSetsParser() {
		this.global = new EventEquipmentGlobalVar();
		// Check / Copy the config files into the config folder
		File configDir = new File(new String(RealisticArmorTiers.instance.configFile.getPath() + Reference.CONFIG_PATH));
		File jsonConfigTiers = new File(new String(RealisticArmorTiers.instance.configFile.getPath() + Reference.JSON_CONFIG_TIERS_PATH));
		File jsonConfigSets = new File(new String(RealisticArmorTiers.instance.configFile.getPath() + Reference.JSON_CONFIG_SETS_PATH));
		
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
		
		// Create TIERS & SETS config
		copyFile(jsonConfigTiers, Reference.ASSET_TIERS_PATH);
		copyFile(jsonConfigSets, Reference.ASSET_SETS_PATH);
		
		// JSON File loading
		try {
			log.info("(JSON File) Loading");
			List<JsonTiers> tiersJson = new ArrayList<JsonTiers>();
			List<JsonSets> sets = new ArrayList<JsonSets>();
			
			log.info("-> (File Read) Reading JSON files");
			try {
				Gson gson = new Gson();
				
				// Tiers
				BufferedReader brTiers = new BufferedReader(new FileReader(new String(RealisticArmorTiers.instance.configFile.getPath() + Reference.JSON_CONFIG_TIERS_PATH)));
				Type typeTiers = new TypeToken<List<JsonTiers>>(){}.getType();
				tiersJson = gson.fromJson(brTiers, typeTiers);
				
				// Sets
				BufferedReader brSets = new BufferedReader(new FileReader(new String(RealisticArmorTiers.instance.configFile.getPath() + Reference.JSON_CONFIG_SETS_PATH)));
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
				List<ItemArmor> helmet = new ArrayList<ItemArmor>();
				List<ItemArmor> chestplate = new ArrayList<ItemArmor>();
				List<ItemArmor> leggings = new ArrayList<ItemArmor>();
				List<ItemArmor> boots = new ArrayList<ItemArmor>();
				for(int i=0; i < sets.size(); i++) {
					helmet = new ArrayList<ItemArmor>();
					chestplate = new ArrayList<ItemArmor>();
					leggings = new ArrayList<ItemArmor>();
					boots = new ArrayList<ItemArmor>();
					
					//All this should be in a function with another for loop per slot but again... couldn't give a damn
					for(int j=0; j < tiersJson.size(); j++) {
						String tiersName = tiersJson.get(j).name; 
						if(tiersName.equalsIgnoreCase(sets.get(i).helmet)) {
							helmet = makeItemArmorListFromStringList(tiersJson.get(j).helmet);
							//we need a better structure but i can't give a damn right now
						}
						if(tiersName.equalsIgnoreCase(sets.get(i).chestplate)) {
							chestplate = makeItemArmorListFromStringList(tiersJson.get(j).chestplate);
							//we need a better structure but i can't give a damn right now
						}
						
						if(tiersName.equalsIgnoreCase(sets.get(i).leggings)) {
							leggings = makeItemArmorListFromStringList(tiersJson.get(j).leggings);
						}
						
						if(tiersName.equalsIgnoreCase(sets.get(i).boots)) {
							boots = makeItemArmorListFromStringList(tiersJson.get(j).boots);
						}
					}
					if(helmet.size() == 0) {
						helmet = makeItemArmorListFromString(sets.get(i).helmet);						
					}
					if(chestplate.size() == 0) {
						chestplate = makeItemArmorListFromString(sets.get(i).chestplate);						
					}
					if(leggings.size() == 0) {
						leggings = makeItemArmorListFromString(sets.get(i).leggings);						
					}
					if(boots.size() == 0) {
						boots = makeItemArmorListFromString(sets.get(i).boots);						
					}
					Armor armor = new Armor(global, new Sets(sets.get(i).name, helmet, chestplate, leggings, boots), sets.get(i).potion, 0);
					armors.addArmor(armor);
				}
				
				//Make Tiers based on JSON
				for(int j=0; j < tiersJson.size(); j++) {
					helmet = makeItemArmorListFromStringList(tiersJson.get(j).helmet);
					chestplate = makeItemArmorListFromStringList(tiersJson.get(j).chestplate);
					leggings = makeItemArmorListFromStringList(tiersJson.get(j).leggings);
					boots = makeItemArmorListFromStringList(tiersJson.get(j).boots);
					Tier tier = new Tier(global, new Sets(sets.get(j).name, helmet, chestplate, leggings, boots), tiersJson.get(j).speed);
					tiers.addTier(tier);
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
	
	
	public List<ItemArmor> makeItemArmorListFromStringList(List<String> armors) {
		List<ItemArmor> items = new ArrayList<ItemArmor>();
		for(int i=0; i<armors.size(); i++) {
			items.add((ItemArmor)ItemArmor.getByNameOrId(armors.get(i)));
		}
		return items;
	}
	
	public List<ItemArmor> makeItemArmorListFromString(String armors) {
		List<ItemArmor> items = new ArrayList<ItemArmor>();
		if(armors.length() > 0) {		
			items.add((ItemArmor)ItemArmor.getByNameOrId(armors));
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





