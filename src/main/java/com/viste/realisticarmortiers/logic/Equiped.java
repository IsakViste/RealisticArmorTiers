package com.viste.realisticarmortiers.logic;

import java.util.Iterator;
import java.util.List;

import com.viste.realisticarmortiers.capability.ArmorProvider;
import com.viste.realisticarmortiers.capability.IArmor;
import com.viste.realisticarmortiers.data.EventEquipmentGlobalVar;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

public class Equiped {
	
	public static void addSetEffectsArmor(EntityPlayerMP player, int potion_effect, int efficiency){
		com.viste.realisticarmortiers.data.Potion potion = new com.viste.realisticarmortiers.data.Potion(potion_effect, efficiency, 0);
		if(player.hasCapability(ArmorProvider.Armor, null)) {
			IArmor armors = player.getCapability(ArmorProvider.Armor, null);				
			armors.addPotionEffect(potion);
		}
		addPotionEffect(player, potion);
	}
	
	public static void addPotionEffect(EntityPlayerMP player, com.viste.realisticarmortiers.data.Potion potion) {
		EventEquipmentGlobalVar global = new EventEquipmentGlobalVar();		
		if(player.getActivePotionEffect(Potion.getPotionById(potion.effect)) != null) {
			player.removeActivePotionEffect(Potion.getPotionById(potion.effect));
		} 
		player.addPotionEffect(new PotionEffect(Potion.getPotionById(potion.effect), global.getPotionDur(), potion.efficiency - 1));
	}
	
	public static void addUsedPotionEffect(EntityPlayerMP player, List<com.viste.realisticarmortiers.data.Potion> potion, IArmor armors) {
		Iterator<com.viste.realisticarmortiers.data.Potion> i = potion.iterator();
		while (i.hasNext()) {
		   com.viste.realisticarmortiers.data.Potion o = i.next();
		   if(o.duration > 0) {
				if(player.getActivePotionEffect(Potion.getPotionById(o.effect)) == null) {
					player.addPotionEffect(new PotionEffect(Potion.getPotionById(o.effect), o.duration, o.efficiency));
					i.remove();
				}
			} else {
				i.remove();
			}
		    
		}
	}
}

