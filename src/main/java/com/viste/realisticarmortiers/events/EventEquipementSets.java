package com.viste.realisticarmortiers.events;

import net.minecraft.entity.player.PlayerCapabilities;
import net.minecraft.init.Items;
import net.minecraft.init.PotionTypes;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionType;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;

public class EventEquipementSets {
	int maxPotions = 28; // Amount of potions + 1
	int potionDur = 20; // 20 ticks ~= 1 second
	
	boolean completeSetBonus = true;
	
	// Leather, Chainmail, Iron, Gold, Diamond
	// 5 = strength | 8 = jump boost | 11 = resistance | 12 = fire-resistance
	int setPotionEff[][] = {{8, 3}, {12, 3}, {11, 1}, {5, 2}, {11, 2}};
	
	float newPlayerSpeed;
	
	@SubscribeEvent
	public void onArmorUpdate(PlayerTickEvent evt) {
		
		Item armors[] = new Item[4];
		ItemStack stacks[] = new ItemStack[4];
		
		int potionEff[] = new int[maxPotions];
		
		newPlayerSpeed = 0.1f;
		
		int setID[] = new int[5];
		
		// 0: boots | 1: leggings | 2: chestplate | 3: helmet
		for(int i = 0; i < 4; i++) {
			armors[i] = null;
			stacks[i] = evt.player.inventory.armorItemInSlot(i);
			
			if(stacks[i] != null) {
				armors[i] = stacks[i].getItem();
				
				// Check Leather
				if(armors[i] == Items.LEATHER_HELMET || armors[i] == Items.LEATHER_CHESTPLATE || armors[i] == Items.LEATHER_LEGGINGS || armors[i] == Items.LEATHER_BOOTS) {
					setID[0] += 1;
					newPlayerSpeed += 0.012f;
					continue;
				}
				
				// Check Chain
				if(armors[i] == Items.CHAINMAIL_HELMET || armors[i] == Items.CHAINMAIL_CHESTPLATE || armors[i] == Items.CHAINMAIL_LEGGINGS || armors[i] == Items.CHAINMAIL_BOOTS) {
					setID[1] += 1;
					newPlayerSpeed += 0.008f;
					continue;
				}
				
				// Check Iron
				if(armors[i] == Items.IRON_HELMET || armors[i] == Items.IRON_CHESTPLATE || armors[i] == Items.IRON_LEGGINGS || armors[i] == Items.IRON_BOOTS) {
					setID[2] += 1;
					newPlayerSpeed -= 0.005f;
					continue;
				}
				
				// Check Gold
				if(armors[i] == Items.GOLDEN_HELMET || armors[i] == Items.GOLDEN_CHESTPLATE || armors[i] == Items.GOLDEN_LEGGINGS || armors[i] == Items.GOLDEN_BOOTS) {
					setID[3] += 1;
					newPlayerSpeed -= 0.0025f;
					continue;
				}
				
				// Check Diamond
				if(armors[i] == Items.DIAMOND_HELMET || armors[i] == Items.DIAMOND_CHESTPLATE || armors[i] == Items.DIAMOND_LEGGINGS || armors[i] == Items.DIAMOND_BOOTS) {
					setID[4] += 1;
					newPlayerSpeed -= 0.008f;
					continue;
				}
			}
		}
		
		// Movement Speed
		evt.player.capabilities.setPlayerWalkSpeed(newPlayerSpeed);
		
		// Set Bonus
		if(completeSetBonus == true) {
			for(int i = 0; i < 5; i++) {
				if(setID[i] >= 4) {
					evt.player.addPotionEffect(new PotionEffect(Potion.getPotionById(setPotionEff[i][0]), potionDur, setPotionEff[i][1] - 1));
				}
			}
		}
	}
}
