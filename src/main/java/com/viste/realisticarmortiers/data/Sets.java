package com.viste.realisticarmortiers.data;

import java.util.List;

import net.minecraft.item.ItemArmor;

public class Sets {
	private String set;
	private List<ItemArmor> helmet;
	private List<ItemArmor> chestplate;
	private List<ItemArmor> leggings;
	private List<ItemArmor> boots;
	private int numberOfPieces = 0;
	
	public Sets(String set, List<ItemArmor> helmet, List<ItemArmor> chestplate, List<ItemArmor> leggings, List<ItemArmor> boots) {
		this.set = set;
		this.helmet = helmet;
		this.chestplate = chestplate;
		this.leggings = leggings;
		this.boots = boots;
		
		if(helmet.size() > 0) {
			this.numberOfPieces++;
		}
		if(chestplate.size() > 0) {
			this.numberOfPieces++;
		}
		if(leggings.size() > 0) {
			this.numberOfPieces++;
		}
		if(boots.size() > 0) {
			this.numberOfPieces++;
		}
	}
	
	public List<ItemArmor> getHelmets() {
		return this.helmet;
	}
	
	public List<ItemArmor> getChestplates() {
		return this.chestplate;
	}
	
	public List<ItemArmor> getLeggings() {
		return this.leggings;
	}
	
	public List<ItemArmor> getBoots() {
		return this.boots;
	}
	
	public int armorSize() {
		return this.numberOfPieces;
	}
	
	public String set() {
		return this.set;
	}
}
