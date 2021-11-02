package com.viste.realisticarmortiers.data;

import java.util.List;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;

public class Armor {
	private final Sets set;
	private int armorPieces = 0;
	private final int armorPiecesLength;
	private final List<PotionEffect> potionEffects;
	
	public Armor(Sets sets, List<PotionEffect> potionEffects) {
		this.set = sets;		
		this.armorPiecesLength = this.set.armorSize();
		this.potionEffects = potionEffects;
	}
	
	private void checkArmor(ServerPlayerEntity player) {
		// Maybe use ... to get armors and check against this
		// player.getArmorSlots()

		ArmorItem chestplate = (ArmorItem)player.getItemBySlot(EquipmentSlotType.CHEST).getItem().getItem();
		if(this.set.getChestplates().contains(chestplate)) {
			this.armorPieces++;
		}

		ArmorItem boots = (ArmorItem)player.getItemBySlot(EquipmentSlotType.FEET).getItem().getItem();
		if(set.getBoots().contains(boots)) {
			this.armorPieces++;
		}

		ArmorItem leggings = (ArmorItem)player.getItemBySlot(EquipmentSlotType.LEGS).getItem().getItem();
		if(set.getLeggings().contains(leggings)) {
			this.armorPieces++;
		}

		ArmorItem helmet = (ArmorItem)player.getItemBySlot(EquipmentSlotType.HEAD).getItem().getItem();
		if(set.getHelmets().contains(helmet)) {
			this.armorPieces++;
		}
	}
	
	public List<PotionEffect> getPotionEffects() {
		return this.potionEffects;
	}
	
	public void resetPieces() {
		this.armorPieces = 0;
	}
	
	public boolean isFullSet(ServerPlayerEntity player) {
		this.resetPieces();
		this.checkArmor(player);
		return this.armorPieces == this.armorPiecesLength;
	}
}