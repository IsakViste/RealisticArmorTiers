package com.viste.realisticarmortiers.logic;

import java.util.Iterator;
import java.util.List;

import com.viste.realisticarmortiers.RealisticArmorTiers;
import com.viste.realisticarmortiers.capability.IArmor;
import com.viste.realisticarmortiers.data.EventEquipmentGlobalVar;

import com.viste.realisticarmortiers.data.PotionEffect;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.ForgeRegistries;

public class Equiped {
	
	public static void addPotionEffectsArmor(ServerPlayerEntity player, String potion_effect, int efficiency) {
		PotionEffect potionEffect = new PotionEffect(potion_effect, efficiency, 0);
		if(player.getCapability(RealisticArmorTiers.CAPABILITY_ARMOR_SET).isPresent()) {
			IArmor armors = (IArmor) player.getCapability(RealisticArmorTiers.CAPABILITY_ARMOR_SET);
			armors.addPotionEffect(potionEffect);
		}
		addPotionEffect(player, potionEffect);
	}
	
	public static void addPotionEffect(ServerPlayerEntity player, PotionEffect setEffect) {
		EventEquipmentGlobalVar global = new EventEquipmentGlobalVar();

		RegistryObject<Effect> potionEffect = RegistryObject.of(new ResourceLocation(setEffect.id), ForgeRegistries.POTIONS);
		if (player.hasEffect(potionEffect.get())) {
			player.removeEffect(potionEffect.get());
		}
		player.addEffect(new EffectInstance(potionEffect.get(), global.getPotionDur(), setEffect.efficiency - 1));
	}
	
	public static void addUsedPotionEffect(ServerPlayerEntity player, List<PotionEffect> potionEffects, IArmor armors) {
		Iterator<PotionEffect> i = potionEffects.iterator();
		while (i.hasNext()) {
		   PotionEffect setEffect = i.next();
		   if(setEffect.duration > 0 && setEffect.duration < 2000000000) {
			   RegistryObject<Effect> potionEffect = RegistryObject.of(new ResourceLocation(setEffect.id), ForgeRegistries.POTIONS);
			   if(!player.hasEffect(potionEffect.get())) {
				   player.addEffect(new EffectInstance(potionEffect.get(), setEffect.duration, setEffect.efficiency));
				   i.remove();
			   }
			} else {
				i.remove();
			}
		}
	}
}

