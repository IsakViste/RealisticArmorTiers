package com.viste.realisticarmortiers.data;

import com.viste.realisticarmortiers.RealisticArmorTiers;

import java.util.*;

public class JsonSets {
	private final String id;
	private final List<PotionEffectJson> potionEffects;
	private final List<String> helmet;
	private final List<String> chestplate;
	private final List<String> leggings;
	private final List<String> boots;

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
	public JsonSets(String id, List<PotionEffectJson> potionEffects, List<String> helmet, List<String> chestplate, List<String> leggings, List<String> boots) {
		this.id = id;
		this.potionEffects = potionEffects;
		this.helmet = helmet;
		this.chestplate = chestplate;
		this.leggings = leggings;
		this.boots = boots;
	}

	/**
	 *
	 * @return List of potion effects of this set (no duplicates)
	 */
	public List<PotionEffect> validateAndGetPotionEffects() {
		// Assert potion effects that ids are not null, no duplicate ids
		Map<String, PotionEffectJson> potionEffectMap = new HashMap<>();
		for (PotionEffectJson potionEffect : this.potionEffects) {
			if (potionEffect.getAmplifier() <= 0) {
				RealisticArmorTiers.LOGGER.warn("|---> [" + potionEffect
						+ "] has a negative or null amplifier and will not be loaded!");
				continue;
			}

			String id = potionEffect.getId();
			if (potionEffectMap.containsKey(id)) {
				PotionEffectJson potionEffectFromMap = potionEffectMap.get(id);
				// If already in map with equal or higher amplifier, skip this
				if (potionEffectFromMap != null && potionEffectFromMap.getAmplifier() >= potionEffect.getAmplifier()) {
					RealisticArmorTiers.LOGGER.warn("|---> [" + potionEffect
							+ "] already exists [" + potionEffectFromMap + "] in " + this.id + " and will not be loaded!");
					continue;
				}
			}

			// Either add potionEffect to the map
			// Or replace the value in the map if assigned value is null, or it's amplifier is lower than current one
			potionEffectMap.put(id, potionEffect);
		}

		// Go through potionEffectMap, and create a PotionEffect for each of the values
		List<PotionEffect> setEffects = new ArrayList<>();
		for (PotionEffectJson json : potionEffectMap.values()) {
			PotionEffect setEffect = new PotionEffect(json);
			// Make sure they have an effect, meaning that the ID is correct, and we could find the effect to apply
			if (setEffect.getEffect() == null) {
				RealisticArmorTiers.LOGGER.warn("|---> [" + setEffect + "] could not find effect corresponding to ID \""
						+ setEffect.getId() + "\" and will therefore not be loaded!");
				continue;
			}
			setEffects.add(setEffect);
		}

		return setEffects;
	}

	public String getId() {
		return id;
	}

	public List<String> getHelmet() {
		return helmet;
	}

	public List<String> getChestplate() {
		return chestplate;
	}

	public List<String> getLeggings() {
		return leggings;
	}

	public List<String> getBoots() {
		return boots;
	}

	@Override
	public String toString() {
		return this.id;
	}
}
