package com.viste.realisticarmortiers.data;

import java.util.List;

import net.minecraft.item.ArmorItem;

public class Sets {
	private final String name;
	private final List<ArmorItem> helmets;
	private final List<ArmorItem> chestplates;
	private final List<ArmorItem> leggings;
	private final List<ArmorItem> boots;
	private int numberOfPieces = 0;
	
	public Sets(String setName, List<ArmorItem> helmets, List<ArmorItem> chestplates, List<ArmorItem> leggings, List<ArmorItem> boots) {
		this.name = setName;
		this.helmets = helmets;
		this.chestplates = chestplates;
		this.leggings = leggings;
		this.boots = boots;
		
		if(helmets.size() > 0) {
			this.numberOfPieces++;
		}
		if(chestplates.size() > 0) {
			this.numberOfPieces++;
		}
		if(leggings.size() > 0) {
			this.numberOfPieces++;
		}
		if(boots.size() > 0) {
			this.numberOfPieces++;
		}
	}
	
	public List<ArmorItem> getHelmets() {
		return this.helmets;
	}
	
	public List<ArmorItem> getChestplates() {
		return this.chestplates;
	}
	
	public List<ArmorItem> getLeggings() {
		return this.leggings;
	}
	
	public List<ArmorItem> getBoots() {
		return this.boots;
	}
	
	public int armorSize() {
		return this.numberOfPieces;
	}

	@Override
	public String toString() {
		return this.name;
	}
}
