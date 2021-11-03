package com.viste.realisticarmortiers.data;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.Item;

import java.util.List;

public class ArmorSet {

	private final String name;
	private final List<ArmorItem> helmets;
	private final List<ArmorItem> chestplates;
	private final List<ArmorItem> leggings;
	private final List<ArmorItem> boots;
	private final List<PotionEffect> potionEffects;

	private int numberOfArmorPiecesToWear= 0;

	/**
	 * Store an armor set in this object.
	 * Any of the ArmorItem in the following list will fill the requirement wearing a part of the set for that specific slot
	 * @param setName name of the set
	 * @param helmets list of helmets part of the set
	 * @param chestplates list of chestplates part of the set
	 * @param leggings list of leggings part of this set
	 * @param boots list of boots prat of this set
	 */
	public ArmorSet(String setName, List<ArmorItem> helmets, List<ArmorItem> chestplates, List<ArmorItem> leggings, List<ArmorItem> boots, List<PotionEffect> potionEffects) {
		this.name = setName;
		this.helmets = helmets;
		this.chestplates = chestplates;
		this.leggings = leggings;
		this.boots = boots;
		this.potionEffects = potionEffects;

		if(helmets.size() > 0) {
			this.numberOfArmorPiecesToWear++;
		}
		if(chestplates.size() > 0) {
			this.numberOfArmorPiecesToWear++;
		}
		if(leggings.size() > 0) {
			this.numberOfArmorPiecesToWear++;
		}
		if(boots.size() > 0) {
			this.numberOfArmorPiecesToWear++;
		}
	}

	/**
	 * @return List of potion effects to apply when wearing the whole set
	 */
	public List<PotionEffect> getPotionEffects() {
		return this.potionEffects;
	}

	/**
	 * @return List of helmets part of this set
	 */
	public List<ArmorItem> getHelmets() {
		return this.helmets;
	}

	/**
	 * @return List of chestplates part of this set
	 */
	public List<ArmorItem> getChestplates() {
		return this.chestplates;
	}

	/**
	 * @return List of leggings part of this set
	 */
	public List<ArmorItem> getLeggings() {
		return this.leggings;
	}

	/**
	 * @return List of Boots part of this set
	 */
	public List<ArmorItem> getBoots() {
		return this.boots;
	}

	/**
	 * The number of armor slots to fill to wear the set.
	 * An empty list counts as 0, otherwise 1 no matter how many items in it.
	 * @return Number of pieces to be worn to fulfill the requirement of wearing this set
	 */
	public int getNumberOfArmorSlotsToFill() {
		return this.numberOfArmorPiecesToWear;
	}

	/**
	 * Check every armor slots of the player, to see if the player is wearing a piece of the current armor set.
	 * Each armor slot only counts once.
	 * @param player the player entity of which to check the inventory/armor slots
	 * @return Number of pieces of the set worn by the player
	 */
	private int checkArmor(ServerPlayerEntity player) {
		int numberOfCurrentlyWornArmorPieces = 0;

		Item item = player.getItemBySlot(EquipmentSlotType.HEAD).getItem().getItem();
		if(item instanceof ArmorItem) {
			if (this.getHelmets().contains((ArmorItem) item)) {
				numberOfCurrentlyWornArmorPieces++;
			}
		}

		item = player.getItemBySlot(EquipmentSlotType.CHEST).getItem().getItem();
		if(item instanceof ArmorItem) {
			if(this.getChestplates().contains((ArmorItem) item)) {
				numberOfCurrentlyWornArmorPieces++;
			}
		}

		item = player.getItemBySlot(EquipmentSlotType.LEGS).getItem().getItem();
		if(item instanceof ArmorItem) {
			if (this.getLeggings().contains((ArmorItem) item)) {
				numberOfCurrentlyWornArmorPieces++;
			}
		}

		item = player.getItemBySlot(EquipmentSlotType.FEET).getItem().getItem();
		if(item instanceof ArmorItem) {
			if (this.getBoots().contains((ArmorItem) item)) {
				numberOfCurrentlyWornArmorPieces++;
			}
		}

		return numberOfCurrentlyWornArmorPieces;
	}

	/**
	 * Check to see if the player is currently fulfilling the requirements of wearing this set
	 * @param player the player entity of which to check the currently worn armor
	 * @return true if the player is wearing the set, false otherwise
	 */
	public boolean isFullSet(ServerPlayerEntity player) {
		int numberOfCurrentlyWornArmorPieces = this.checkArmor(player);
		return numberOfCurrentlyWornArmorPieces == this.getNumberOfArmorSlotsToFill();
	}

	@Override
	public String toString() {
		return this.name;
	}
}
