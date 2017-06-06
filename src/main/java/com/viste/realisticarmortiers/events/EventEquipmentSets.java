package com.viste.realisticarmortiers.events;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.viste.realisticarmortiers.RealisticArmorTiers;
import com.viste.realisticarmortiers.Reference;

import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;

public class EventEquipmentSets {	
	
	public EventEquipmentGlobalVar global;
	public List<Armours> armours = new ArrayList<Armours>();
	
	private static final Logger log = LogManager.getLogger(Reference.MODID);
	
	public EventEquipmentSets() {
		
		this.global = new EventEquipmentGlobalVar();
		
		// Check if JSON file exists or copy it in
		File configDir = new File(new String(RealisticArmorTiers.instance.configFile.getPath() + Reference.CONFIG_PATH));
		File jsonConfig = new File(new String(RealisticArmorTiers.instance.configFile.getPath() + Reference.JSON_CONFIG_PATH));
		
		if(!configDir.exists()) {
			log.info("(Config Folder) Creating");
			try {
				configDir.mkdir();
			} catch (SecurityException se) {
				log.fatal("(Config Folder) Creation Failed");
				log.fatal(se);
				return;
			}
			log.info("(Config Folder) Creation Sucess");
		}
			
		if(!jsonConfig.exists()) {
			log.info("(JSON File) Copy to Config folder");
			try {
				jsonConfig.createNewFile();
				
				FileInputStream instream = new FileInputStream(this.getClass().getResource(Reference.ASSET_PATH).getPath());
				FileOutputStream outstream = new FileOutputStream(jsonConfig);
				
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
		}
		
		// JSON File loading
		Gson gson = new Gson();
		try {
			BufferedReader br = new BufferedReader(new FileReader(new String(RealisticArmorTiers.instance.configFile.getPath() + Reference.JSON_CONFIG_PATH)));
			Type type = new TypeToken<List<JsonModel>>(){}.getType();
			log.info("(JSON File) Loading");
			List<JsonModel> models = gson.fromJson(br, type);
			try {
				log.info("(Armors) Loading All");
				for(int i = 0; i < models.size(); i++) {
					List<Effects> effects = models.get(i).effects;
					List<ItemArmor> piecesArmors = new ArrayList<ItemArmor>();
					for(int j = 0; j < 4; j++) {
						try {
							String path = models.get(i).modId + ":" + models.get(i).pieces.get(j).toLowerCase();
							ItemArmor armor = (ItemArmor)ItemArmor.getByNameOrId(path);
							piecesArmors.add(armor);
						} catch (Exception e) {
							log.fatal("(Armor Piece) Loading Failure");
							log.fatal(e);
						}
					}
					armours.add(new Armours(global, piecesArmors, effects, models.get(i).speed));
				}
				log.info("(Armors) Loading Sucess");
			} catch (Exception e) {
				log.fatal("(Armors) Loading Failure");
				log.fatal(e);
			}
			
			
		} catch (Exception e) {
			log.fatal("(JSON File) Loading Failure");
			log.fatal(e);
			return;
		}
		log.info("(JSON File) Loading Sucess");
	}

	@SubscribeEvent
	public void onArmorUpdate(PlayerTickEvent evt) {
		
		Item armor = null;
		ItemStack stacks = null;
		
		int j=0;
		
		global.setSpeed(0.1f);
		
		while (j < armours.size()){
			armours.get(j).resetPieces();
			j++;
		}
		
		for(int i = 0; i < 4; i++) {
			stacks = evt.player.inventory.armorItemInSlot(i);
			
			if(stacks != null) {
				armor = stacks.getItem();
				
				j=0;
				while (j < armours.size()){
					if(armours.get(j).checkArmour(armor,evt)){
						break;
					}
					
					j++;
				}
			}
		}
		// Movement Speed
		evt.player.capabilities.setPlayerWalkSpeed(global.getSpeed());
	}
}

class Armours {
	private List<ItemArmor> armours = null;
	private int armourPieces = 0;
	private List<Effects> effects;
	private EventEquipmentGlobalVar global;
	private float speed = 0;
	
	public Armours(EventEquipmentGlobalVar global, List<ItemArmor> items, List<Effects> effects, float speed) {
		this.global = global;
		this.speed = speed;
		this.armours = items;
		this.effects = effects;
	}
	public boolean checkArmour(Item item, PlayerTickEvent evt){
		if(armours.contains(item)){
			this.armourPieces++;
			global.setSpeed(global.getSpeed() + speed);
			this.addSetEffectsArmour(evt);
			return true;
		}
		return false;
	}

	public boolean isFullSet(){
		if(this.armourPieces >= 4){
			return true;
		}
		return false;
	}

	public void resetPieces(){
		this.armourPieces = 0;
	}

	public void addSetEffectsArmour(PlayerTickEvent evt){
		if(this.isFullSet()){
			int i = 0;
			while (i < effects.size()){
				evt.player.addPotionEffect(new PotionEffect(Potion.getPotionById(effects.get(i).potion_effect), global.getPotionDur(), effects.get(i).efficiency - 1));
				i++;
			}
		}
	}
}

class JsonModel {
	public String name;
	public String modId;
	public List<String> pieces;
	public List<Effects> effects;
	public float speed;
	
	public JsonModel(String name, String modId, List<String> pieces, List<Effects> effects, float speed) {
		this.name = name;
		this.modId = modId;
		this.pieces = pieces;
		this.effects = effects;
		this.speed = speed;
	}
}

class Effects {
	public int potion_effect;
	public int efficiency;
	
	public Effects(int potion_effect, int efficiency){
		this.potion_effect = potion_effect;
		this.efficiency = efficiency;
	}
}
class EventEquipmentGlobalVar {
	private int maxPotions = 28; // Amount of potions + 1
	private int potionDur = 20; // 20 ticks ~= 1 second
	private float newPlayerSpeed = 0.1f;

	public float getSpeed(){
		return this.newPlayerSpeed;
	}
	public void setSpeed(float speed){
		this.newPlayerSpeed = speed;
	}

	public int getPotionDur(){
		return this.potionDur;
	}
}
