package com.viste.realisticarmortiers.data;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.EntityEquipmentSlot;

public class Tier {
	private Sets set = null;
	private float speed = 0;
	private float speedTier = 0;
	public Tier(EventEquipmentGlobalVar global, Sets sets, float speed) {
		this.set = sets;		
		this.speed = speed;
	}
	
	private void checkArmor(EntityPlayerMP player) {		
		if(this.set.getChestplates().contains(player.getItemStackFromSlot(EntityEquipmentSlot.CHEST).getItem())) {
			this.speedTier = this.speedTier + this.speed;
		}
		
		if(set.getBoots().contains(player.getItemStackFromSlot(EntityEquipmentSlot.FEET).getItem())) {
			this.speedTier = this.speedTier + this.speed;
		}
		
		if(set.getLeggings().contains(player.getItemStackFromSlot(EntityEquipmentSlot.LEGS).getItem())) {
			this.speedTier = this.speedTier + this.speed;
		}
		
		if(set.getHelmets().contains(player.getItemStackFromSlot(EntityEquipmentSlot.HEAD).getItem())) {
			this.speedTier = this.speedTier + this.speed;
		}
	}	
	
	public float getSpeed() {
		return this.speed;
	}
	
	public float getSpeed(EntityPlayerMP player) {
		this.speedTier = 0;
		this.checkArmor(player);
		return this.speedTier;
	}
}