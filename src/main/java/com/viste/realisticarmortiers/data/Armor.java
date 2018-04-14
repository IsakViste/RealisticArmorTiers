package com.viste.realisticarmortiers.data;

import java.util.List;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.EntityEquipmentSlot;

public class Armor {
	private Sets set = null;
	private int armorPieces = 0;
	private float speed;
	private int armorPiecesLength = 0;
	private List<Potion> potions;
	
	public Armor(EventEquipmentGlobalVar global, Sets sets, List<Potion> potions, float speed) {
		this.set = sets;		
		this.armorPiecesLength = this.set.armorSize();
		this.potions = potions;
		this.speed = speed;
	}
	
	private void checkArmor(EntityPlayerMP player) {		
		if(this.set.getChestplates().contains(player.getItemStackFromSlot(EntityEquipmentSlot.CHEST).getItem())) {
			this.armorPieces++;
		}
		
		if(set.getBoots().contains(player.getItemStackFromSlot(EntityEquipmentSlot.FEET).getItem())) {
			this.armorPieces++;
		}
		
		if(set.getLeggings().contains(player.getItemStackFromSlot(EntityEquipmentSlot.LEGS).getItem())) {
			this.armorPieces++;
		}
		
		if(set.getHelmets().contains(player.getItemStackFromSlot(EntityEquipmentSlot.HEAD).getItem())) {
			this.armorPieces++;
		}
	}
	
	public List<Potion> getPotions() {
		return this.potions;
	}
	
	public float getSpeed() {
		return this.speed;
	}
	
	public void resetPieces() {
		this.armorPieces = 0;
	}
	
	public boolean isFullSet(EntityPlayerMP player) {
		this.resetPieces();
		this.checkArmor(player);
		if(this.armorPieces == this.armorPiecesLength) {
			return true;
		}
		return false;
	}
}