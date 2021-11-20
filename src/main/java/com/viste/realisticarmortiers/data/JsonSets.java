package com.viste.realisticarmortiers.data;

import com.viste.realisticarmortiers.RealisticArmorTiers;

import java.util.*;

public class JsonSets {
	public final String id;
	public List<PotionEffect> potionEffects;
	public final List<String> helmet;
	public final List<String> chestplate;
	public final List<String> leggings;
	public final List<String> boots;

	/**
	 * Store data from the JSON in this object
	 * The IDs are in the form of "minecraft:leather_boots" readable by the ResourceLocation
	 * @param id the id of the set
	 * @param potionEffects a list containing all the potion effects to apply
	 * @param helmet a list of IDs corresponding to the helmets part of this set
	 * @param chestplate a list of IDs corresponding to the chestplates part of this set
	 * @param leggings a list of IDs corresponding to the leggings part of this set
	 * @param boots a list of IDs corresponding to the boots part of this set
	 * @see net.minecraft.util.ResourceLocation
	 */
	public JsonSets(String id, List<PotionEffect> potionEffects, List<String> helmet, List<String> chestplate, List<String> leggings, List<String> boots) {
		this.id = id;
		this.potionEffects = potionEffects;
		this.helmet = helmet;
		this.chestplate = chestplate;
		this.leggings = leggings;
		this.boots = boots;
	}

	/**
	 * Validate that the potion effects of this set
	 */
	public void validatePotionEffects() {
		// Assert potion effects that ids are not null, no duplicate ids
		Map<String, PotionEffect> potionEffectMap = new HashMap<>();
		for (PotionEffect potionEffect : this.potionEffects) {
			String id = potionEffect.getId();
			if (potionEffectMap.containsKey(id)) {
				PotionEffect potionEffectFromMap = potionEffectMap.get(id);
				// If already in map with equal or higher amplifier, skip this
				if (potionEffectFromMap != null && potionEffectFromMap.getAmplifier() >= potionEffect.getAmplifier()) {
					RealisticArmorTiers.LOGGER.warn("|---> Potion effect [" + potionEffect
							+ "] already exists [" + potionEffectFromMap + "] and will not be loaded");
					continue;
				}
			}

			// Either add potionEffect to the map
			// Or replace the value in the map if assigned value is null, or it's amplifier is lower than current one
			potionEffectMap.put(id, potionEffect);
		}

		this.potionEffects = new ArrayList<>(potionEffectMap.values());
	}

	@Override
	public String toString() {
		return this.id;
	}
}
