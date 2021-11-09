package com.viste.realisticarmortiers.data;

import java.util.List;

public class JsonSets {
	public final String name;
	public final List<PotionEffect> potionEffects;
	public final List<String> helmet;
	public final List<String> chestplate;
	public final List<String> leggings;
	public final List<String> boots;

	/**
	 * Store data from the JSON in this object
	 * The IDs are in the form of "minecraft:leather_boots" readable by the ResourceLocation
	 * @param name the name of the set
	 * @param potionEffects a list containing all the potion effects to apply
	 * @param helmet a list of IDs corresponding to the helmets part of this set
	 * @param chestplate a list of IDs corresponding to the chestplates part of this set
	 * @param leggings a list of IDs corresponding to the leggings part of this set
	 * @param boots a list of IDs corresponding to the boots part of this set
	 * @see net.minecraft.util.ResourceLocation
	 */
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
