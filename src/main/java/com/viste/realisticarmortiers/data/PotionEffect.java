package com.viste.realisticarmortiers.data;

public class PotionEffect {
	public String potionID;
	public int efficiency;
	public int duration;

	/**
	 * A simple object to store a potion effect
	 * @param potionID the id of the potion effect (e.g. "minecraft:speed")
	 * @param efficiency the efficiency of the effect
	 * @param duration the duration the effect should last
	 */
	public PotionEffect(String potionID, int efficiency, int duration) {
		this.potionID = potionID;
		this.efficiency = efficiency;
		this.duration = duration;
	}
}
