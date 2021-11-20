package com.viste.realisticarmortiers.data;

import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Objects;

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

	/**
	 * A simple object to store a potion effect
	 * @param effectInstance the EffectInstance from which to get information: id, duration and amplifier
	 */
	public PotionEffect(EffectInstance effectInstance) {
		this(Objects.requireNonNull(effectInstance.getEffect().getRegistryName()).toString(), effectInstance.getDuration(), effectInstance.getAmplifier());
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

	public EffectInstance effectInstance() {
		return new EffectInstance(this.getEffect(), this.getDuration(), this.getAmplifier());
	}

	@Override
	public String toString() {
		String string = "effect." + this.id.replace(":", ".");

		if (this.amplifier > 0) {
			string += " x " + this.amplifier;
		}

		if (this.duration > 0) {
			string += ", Duration: " + this.duration;
		}

		return string;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		PotionEffect that = (PotionEffect) o;
		return amplifier == that.amplifier && id.equals(that.id);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, amplifier);
	}
}
