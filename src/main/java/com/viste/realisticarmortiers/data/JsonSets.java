package com.viste.realisticarmortiers.data;

import java.util.List;

public class JsonSets {
	public String name;
	public List<PotionEffect> potionEffects;
	public List<String> helmet;
	public List<String> chestplate;
	public List<String> leggings;
	public List<String> boots;

	public JsonSets(String name, List<PotionEffect> potionEffects, List<String> helmet, List<String> chestplate, List<String> leggings, List<String> boots) {
		this.name = name;
		this.potionEffects = potionEffects;
		this.helmet = helmet;
		this.chestplate = chestplate;
		this.leggings = leggings;
		this.boots = boots;
	}

	@Override
	public String toString() {
		return this.name;
	}
}
