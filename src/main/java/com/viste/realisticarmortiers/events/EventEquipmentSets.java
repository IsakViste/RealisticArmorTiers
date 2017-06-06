package com.viste.realisticarmortiers.events;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.viste.realisticarmortiers.RealisticArmorTiers;
import com.viste.realisticarmortiers.Reference;

import net.minecraft.init.Items;
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
		
		Gson gson = new Gson();
		
		try {
			BufferedReader br = new BufferedReader(new FileReader(RealisticArmorTiers.class.getResource(Reference.ASSET_PATH).getPath()));
			Type type = new TypeToken<List<JsonModel>>(){}.getType();
			log.info("JSON file is being loaded");
			List<JsonModel> models = gson.fromJson(br, type);
			try {
				for(int i = 0; i < models.size(); i++) {
					List<Effects> effects = models.get(i).effects;
					List<ItemArmor> piecesArmors = new ArrayList<ItemArmor>();
					for(int j = 0; j < 4; j++) {
						try {
							String path = models.get(i).modId + ":" + models.get(i).pieces.get(j).toLowerCase();
							ItemArmor armor = (ItemArmor)ItemArmor.getByNameOrId(path);
							piecesArmors.add(armor);
						} catch (Exception e) {
							log.fatal("Couldn't initialize a specific armor piece");
							log.fatal(e);
						}
					}
					armours.add(new Armours(global, piecesArmors, effects, models.get(i).speed));
				}
				log.info("All armors have been loaded!");
			} catch (Exception e) {
				log.fatal("Couldn't initialize all armors");
				log.fatal(e);
			}
			
			log.info("JSON file has been loaded");
		} catch (Exception e) {
			log.fatal("JSON file could not be loaded:");
			log.fatal(e);
		}
		
		/*
		List<ItemArmor> leather = Arrays.asList(Items.LEATHER_HELMET, Items.LEATHER_CHESTPLATE, Items.LEATHER_LEGGINGS, Items.LEATHER_BOOTS);
		List<ItemArmor> chain = Arrays.asList(Items.CHAINMAIL_HELMET, Items.CHAINMAIL_CHESTPLATE, Items.CHAINMAIL_LEGGINGS, Items.CHAINMAIL_BOOTS);
		List<ItemArmor> iron = Arrays.asList(Items.IRON_HELMET, Items.IRON_CHESTPLATE, Items.IRON_LEGGINGS, Items.IRON_BOOTS);
		List<ItemArmor> gold = Arrays.asList(Items.GOLDEN_HELMET, Items.GOLDEN_CHESTPLATE, Items.GOLDEN_LEGGINGS, Items.GOLDEN_BOOTS);
		List<ItemArmor> diamond = Arrays.asList(Items.DIAMOND_HELMET, Items.DIAMOND_CHESTPLATE, Items.DIAMOND_LEGGINGS, Items.DIAMOND_BOOTS);

		//Having a JSON file to get all the data will make it more modular, but for now i don't have enough data.'
		List<Effects> effect1 = new ArrayList<Effects>();
		effect1.add(new Effects(8,3));
		List<Effects> effect2 = new ArrayList<Effects>();
		effect2.add(new Effects(12,3));
		List<Effects> effect3 = new ArrayList<Effects>();
		effect3.add(new Effects(11,1));
		List<Effects> effect4 = new ArrayList<Effects>();
		effect4.add(new Effects(5,2));
		List<Effects> effect5 = new ArrayList<Effects>();
		effect5.add(new Effects(11,2));

		armours.add(new Armours(global, leather, effect1,    0.012f));
		armours.add(new Armours(global, chain,   effect2,    0.008f));
		armours.add(new Armours(global, iron,    effect3,   -0.005f));
		armours.add(new Armours(global, gold,    effect4,   -0.0025f));
		armours.add(new Armours(global, diamond, effect5,   -0.008f));
		*/
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
