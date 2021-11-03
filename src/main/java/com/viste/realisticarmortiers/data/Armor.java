package com.viste.realisticarmortiers.data;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.Item;

import java.util.List;

public class Armor {
	private int currentlyWornNumberOfArmorPieces = 0;

	private final ArmorSet set;
	private final int numberOfArmorSetPieces;
	private final List<PotionEffect> potionEffects;
	
	public Armor(ArmorSet set, List<PotionEffect> potionEffects) {
		this.set = set;
		this.numberOfArmorSetPieces = this.set.getNumberOfPieces();
		this.potionEffects = potionEffects;
	}
	
	private void checkArmor(ServerPlayerEntity player) {
		Item item = player.getItemBySlot(EquipmentSlotType.HEAD).getItem().getItem();
		if(item instanceof ArmorItem) {
			if (set.getHelmets().contains((ArmorItem) item)) {
				this.currentlyWornNumberOfArmorPieces++;
			}
		}

		item = player.getItemBySlot(EquipmentSlotType.CHEST).getItem().getItem();
		if(item instanceof ArmorItem) {
			if(this.set.getChestplates().contains((ArmorItem) item)) {
				this.currentlyWornNumberOfArmorPieces++;
			}
		}

		item = player.getItemBySlot(EquipmentSlotType.LEGS).getItem().getItem();
		if(item instanceof ArmorItem) {
			if (set.getLeggings().contains((ArmorItem) item)) {
				this.currentlyWornNumberOfArmorPieces++;
			}
		}

		item = player.getItemBySlot(EquipmentSlotType.FEET).getItem().getItem();
		if(item instanceof ArmorItem) {
			if (set.getBoots().contains((ArmorItem) item)) {
				this.currentlyWornNumberOfArmorPieces++;
			}
		}
	}
	
	public List<PotionEffect> getPotionEffects() {
		return this.potionEffects;
	}
	
	public void resetPieces() {
		this.currentlyWornNumberOfArmorPieces = 0;
	}
	
	public boolean isFullSet(ServerPlayerEntity player) {
		this.resetPieces();
		this.checkArmor(player);
		return this.currentlyWornNumberOfArmorPieces == this.numberOfArmorSetPieces;
	}

	@Override
	public String toString() {
		return this.set.toString();
	}
}