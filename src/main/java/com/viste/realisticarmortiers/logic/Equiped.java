package com.viste.realisticarmortiers.logic;

import com.viste.realisticarmortiers.capability.ArmorProvider;
import com.viste.realisticarmortiers.capability.IArmor;
import com.viste.realisticarmortiers.data.EventEquipmentGlobalVar;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

public class Equiped {
	
	public static void addSetEffectsArmor(EntityPlayerMP player, int potion_effect, int efficiency){
		com.viste.realisticarmortiers.data.Potion potion = new com.viste.realisticarmortiers.data.Potion(potion_effect, efficiency);
		if(player.hasCapability(ArmorProvider.Armor, null)) {
			IArmor armors = player.getCapability(ArmorProvider.Armor, null);				
			
			armors.addPotionEffect(potion);
		}
		addPotionEffect(player, potion);
	}
	
	public static void addPotionEffect(EntityPlayerMP player, com.viste.realisticarmortiers.data.Potion potion) {
		EventEquipmentGlobalVar global = new EventEquipmentGlobalVar();
		player.addPotionEffect(new PotionEffect(Potion.getPotionById(potion.effect), global.getPotionDur(), potion.efficiency - 1));
	}
}
