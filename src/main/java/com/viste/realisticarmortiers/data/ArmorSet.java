package com.viste.realisticarmortiers.data;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;

import java.util.List;

public class ArmorSet {

	private final String name;
	private final List<Item> helmets;
	private final List<Item> chestplates;
	private final List<Item> leggings;
	private final List<Item> boots;
	private final List<PotionEffect> potionEffects;

	private int numberOfArmorPiecesToWear= 0;

	/**
	 * Store an armor set in this object.
	 * Any of the Item in the following list will fill the requirement wearing a part of the set for that specific slot
	 * @param setName name of the set
	 * @param helmets list of helmets part of the set
	 * @param chestplates list of chestplates part of the set
	 * @param leggings list of leggings part of this set
	 * @param boots list of boots prat of this set
	 */
	public ArmorSet(String setName, List<Item> helmets, List<Item> chestplates, List<Item> leggings, List<Item> boots, List<PotionEffect> potionEffects) {
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

	public String getName() {
		return this.name;
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
	public List<Item> getHelmets() {
		return this.helmets;
	}

	/**
	 * @return List of chestplates part of this set
	 */
	public List<Item> getChestplates() {
		return this.chestplates;
	}

	/**
	 * @return List of leggings part of this set
	 */
	public List<Item> getLeggings() {
		return this.leggings;
	}

	/**
	 * @return List of Boots part of this set
	 */
	public List<Item> getBoots() {
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
	 * @return Number of pieces of the set worn by the player (between 0 and 4)
	 */
	private int numberOfCurrentlyWornArmorPiecesByPlayer(ServerPlayerEntity player) {
		Item helmet = player.getItemBySlot(EquipmentSlotType.HEAD).getItem().getItem();
		Item chestplate = player.getItemBySlot(EquipmentSlotType.CHEST).getItem().getItem();
		Item leggings = player.getItemBySlot(EquipmentSlotType.LEGS).getItem().getItem();
		Item boots = player.getItemBySlot(EquipmentSlotType.FEET).getItem().getItem();

		return getNumberOfArmorPiecesBelongingToSet(helmet, chestplate, leggings, boots);
	}

	/**
	 * For the following items, check if they are part of the set or not
	 * @param helmet the item to check against the helmets of the set
	 * @param chestplate the item to check against the chestplates of the set
	 * @param leggings the item to check against the leggings of the set
	 * @param boots the item to check against the boots of the set
	 * @return the number of the items part of the set (between 0 and 4)
	 */
	private int getNumberOfArmorPiecesBelongingToSet(Item helmet, Item chestplate, Item leggings, Item boots) {
		int numberOfCurrentlyWornArmorPieces = 0;

		if(this.getHelmets().contains(helmet)) {
			numberOfCurrentlyWornArmorPieces++;
		}

		if(this.getChestplates().contains(chestplate)) {
			numberOfCurrentlyWornArmorPieces++;
		}

		if(this.getLeggings().contains(leggings)) {
			numberOfCurrentlyWornArmorPieces++;
		}

		if(this.getBoots().contains(boots)) {
			numberOfCurrentlyWornArmorPieces++;
		}

		return numberOfCurrentlyWornArmorPieces;
	}


	/**
	 * Check to see if the player is currently fulfilling the requirements of wearing this set
	 * @param player the player entity of which to check the currently worn armor
	 * @return true if the player is wearing the set
	 */
	public boolean isFullSet(ServerPlayerEntity player) {
		int numberOfCurrentlyWornArmorPieces = this.numberOfCurrentlyWornArmorPiecesByPlayer(player);
		return numberOfCurrentlyWornArmorPieces == this.getNumberOfArmorSlotsToFill();
	}

	/**
	 * Check to see if the following items fulfill the requirements of this set
	 * @param helmet the item to check against the helmets of the set
	 * @param chestplate the item to check against the chestplates of the set
	 * @param leggings the item to check against the leggings of the set
	 * @param boots the item to check against the boots of the set
	 * @return true if the items fulfill the requirements of wearing this set
	 */
	public boolean isFullSet(Item helmet, Item chestplate, Item leggings, Item boots) {
		int numberOfItemsBelongingToSet = this.getNumberOfArmorPiecesBelongingToSet(helmet, chestplate, leggings, boots);
		return numberOfItemsBelongingToSet == this.getNumberOfArmorSlotsToFill();
	}

	@Override
	public String toString() {
		return this.name;
	}
}
