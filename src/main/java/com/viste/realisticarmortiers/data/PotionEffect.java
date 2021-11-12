package com.viste.realisticarmortiers.data;

import net.minecraft.potion.Effect;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

public class PotionEffect {
	private static final int POTION_DURATION = Integer.MAX_VALUE; // 20 ticks ~= 1 second

	private final String id;
	private int amplifier;
	private int duration;

	private Effect effect;

	/**
	 * A simple object to store a potion effect
	 * @param potionID the id of the potion effect (e.g. "minecraft:speed")
	 * @param duration the duration the effect should last
	 * @param amplifier the efficiency of the effect
	 */
	public PotionEffect(String potionID, int duration, int amplifier) {
		this.id = potionID;
		this.setDuration(duration);
		this.setAmplifier(amplifier);
	}

	public String getId() {
		return this.id;
	}

	public void setAmplifier(int amplifier) {
		this.amplifier = amplifier + 1;
	}

	public int getAmplifier() {
		return this.amplifier - 1;
	}

	public int getDuration() {
		return this.duration > 0 ? this.duration : POTION_DURATION;
	}

	public void setDuration(int duration) {
		if(duration <= 0){
			this.duration = POTION_DURATION;
			return;
		}

		this.duration = duration;
	}

	public Effect getEffect() {
		if (this.effect == null ) {
			this.effect = ForgeRegistries.POTIONS.getValue(new ResourceLocation(getId()));
		}

		return this.effect;
	}
}
