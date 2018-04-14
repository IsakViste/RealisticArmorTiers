package com.viste.realisticarmortiers.events;


import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElementDecl.GLOBAL;

import com.google.common.collect.Lists;
import com.viste.realisticarmortiers.capability.ArmorProvider;
import com.viste.realisticarmortiers.capability.IArmor;
import com.viste.realisticarmortiers.data.EquipmentSetsParser;
import com.viste.realisticarmortiers.data.Potion;
import com.viste.realisticarmortiers.logic.Equiped;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ServerTickEvent;

public class EventEquipmentSets {	
	private MinecraftServer server;
	private EquipmentSetsParser sets;
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
			speed = 0.1f;
			EntityPlayerMP player = list.get(i);
			List <ItemStack> stacks = null;
						
			if(player.hasCapability(ArmorProvider.Armor, null)) {
				armors = player.getCapability(ArmorProvider.Armor, null);
				
				if(armors != null && armors.getItems().size() > 0) {
					stacks = (List<ItemStack>) player.getArmorInventoryList();
					if(stacks.size() == armors.getItems().size()) {
						foundWhole = true;
						for(int k=0; k < stacks.size(); k++) {
							found = false;
							for(int l=0; l < armors.getItems().size(); l++)	{
								if(stacks.get(k) == armors.getItems().get(l)) {
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
			if(!foundWhole) {
				if(player.hasCapability(ArmorProvider.Armor, null)) {
					armors.removeAllItems();		
					armors.setSpeed(sets.global.getSpeed());
					stacks = (List<ItemStack>)player.getArmorInventoryList();
					for(int k = 0; k < stacks.size(); k++) {
						if(stacks != null) {
							armors.addItem(stacks.get(k));
						}
					}
				}
				
				int setNumber = sets.armors.checkIfSet(player);
				if(setNumber != -1) {
					potionsEffects = sets.armors.getPotions(setNumber);
				}
				
				speed = sets.tiers.checkIfTier(player);
				if(speed == 0) {
					speed = sets.global.getSpeed();
				}
				if(player.hasCapability(ArmorProvider.Armor, null)) {
					if(potionsEffects != null) {
						armors.addPotionEffectList(potionsEffects);
					}
					if(speed > 0) {
						armors.setSpeed(speed);
					} 
				}
			} else {
				potionsEffects = armors.getPotionEffect();
				speed = armors.getSpeed();
			}
			if(potionsEffects != null) { 
				int m = 0;
				while(m < potionsEffects.size()) {
					Equiped.addPotionEffect(player, armors.getPotionEffect().get(m));
					m++;
				}
			}
			if(speed > 0 && speed != player.capabilities.getWalkSpeed()) {
				player.capabilities.setPlayerWalkSpeed(speed);
			}
			
		}	
	}
}
