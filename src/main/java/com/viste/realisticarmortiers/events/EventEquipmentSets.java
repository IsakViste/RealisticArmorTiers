package com.viste.realisticarmortiers.events;


import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.collect.Lists;
import com.viste.realisticarmortiers.Reference;
import com.viste.realisticarmortiers.capability.ArmorProvider;
import com.viste.realisticarmortiers.capability.IArmor;
import com.viste.realisticarmortiers.data.EquipmentSetsParser;
import com.viste.realisticarmortiers.data.Potion;
import com.viste.realisticarmortiers.logic.Equiped;

import jline.internal.Log;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.PlayerCapabilities;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ServerTickEvent;

public class EventEquipmentSets {	
	private MinecraftServer server;
	private EquipmentSetsParser sets;
	private static final Logger log = LogManager.getLogger(Reference.MODID);
	public EventEquipmentSets() {
		sets = new EquipmentSetsParser();

	}
		
	@SubscribeEvent
	public void onServerTick(ServerTickEvent evt) {		
		if(this.server == null) {
			this.server = FMLCommonHandler.instance().getMinecraftServerInstance();
		}
		List<EntityPlayerMP> list = Lists.newArrayList(this.server.getPlayerList().getPlayers());
		IArmor armors = null;
		List<Potion> potionsEffects = new ArrayList<Potion>();
		boolean foundWhole = false;
		boolean found = false;
		float speed;
		for(int i=0; i < list.size(); i++) {
			int m;
			speed = sets.global.getSpeed();
			EntityPlayerMP player = list.get(i);
			List <ItemStack> stacks = null;
						
			if(player.hasCapability(ArmorProvider.Armor, null)) {
				armors = player.getCapability(ArmorProvider.Armor, null);				
				if(armors != null) {
					stacks = (List<ItemStack>)player.getArmorInventoryList();
					int numberOfStack = 0;
					for(int k=0; k < stacks.size(); k++) {
						if(!stacks.get(k).isEmpty()) {
							numberOfStack++;
						}
					}
					if(numberOfStack == armors.getItems().size()) {
						foundWhole = true;
						for(int k=0; k < stacks.size(); k++) {
							found = false;
							if(!stacks.get(k).isEmpty()) {
								for(int l=0; l < armors.getItems().size(); l++)	{
									if(stacks.get(k).getItem().equals(armors.getItems().get(l).getItem())) {
										found = true;
										break;
									}
								}
								if(found == false) {
									foundWhole = false;
									break;
								}
							}
						}
					}
				}
			}
			if(foundWhole) {				
				potionsEffects = armors.getPotionEffect();
				speed = armors.getSpeed();
			} else {
				if(player.hasCapability(ArmorProvider.Armor, null)) {
					m = 0;
					potionsEffects = armors.getPotionEffect();
					while(m < potionsEffects.size()) {
						player.removePotionEffect((net.minecraft.potion.Potion.getPotionById(potionsEffects.get(m).effect)));
						m++;
					}
					armors.removeAllItems();
					armors.setSpeed(sets.global.getSpeed());
					Collection<PotionEffect> potionEffectsPlayer = player.getActivePotionEffects();
					Iterator<PotionEffect> potionEffects = potionEffectsPlayer.iterator();
					while (potionEffects.hasNext()) {
						PotionEffect o = potionEffects.next();
						Potion usedPotion = new Potion(net.minecraft.potion.Potion.getIdFromPotion(o.getPotion()), o.getAmplifier(), o.getDuration());
						armors.addUsedPotion(usedPotion);
						potionEffects.remove();
					}
					
					stacks = (List<ItemStack>)player.getArmorInventoryList();
					for(int k = 0; k < stacks.size(); k++) {
						if(!stacks.get(k).isEmpty()) {
							ItemStack x = stacks.get(k).copy();
							if(stacks != null) {
								armors.addItem(x);
							}
						}
					}
				} else {
					player.clearActivePotions();
				}
				
				int setNumber = sets.armors.checkIfSet(player);
				if(setNumber != -1) {
					potionsEffects = sets.armors.getPotions(setNumber);
				}
				
				speed = speed + sets.tiers.checkIfTier(player);
				if(speed < 0) {
					speed = 0;
				}
				if(player.hasCapability(ArmorProvider.Armor, null)) {
					if(potionsEffects != null) {
						armors.addPotionEffectList(potionsEffects);
					}
					armors.setSpeed(speed);
				}
			}
			
			if(potionsEffects != null) { 
				m = 0;
				while(m < potionsEffects.size()) {
					Equiped.addPotionEffect(player, armors.getPotionEffect().get(m));
					m++;
				}
			}
			
			if(!foundWhole) {
				potionsEffects = armors.getUsedPotionEffect();
				Equiped.addUsedPotionEffect(player, potionsEffects, armors);
			}
			
			//log.info(player.capabilities.getWalkSpeed());
			
			if(speed != player.capabilities.getWalkSpeed()) {
				PlayerCapabilities cap = ObfuscationReflectionHelper.getPrivateValue(EntityPlayer.class, player, "capabilities", "field_71075_bZ");
				ObfuscationReflectionHelper.setPrivateValue(PlayerCapabilities.class, cap, speed,"walkSpeed", "field_73357_f");
			}
		}	
	}
}
