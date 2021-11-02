package com.viste.realisticarmortiers.data;


public class PotionEffect {
	public String id;
	public int efficiency;
	public int duration;

	public PotionEffect(String effect, int efficiency, int duration) {
		this.id = effect;
		this.efficiency = efficiency;
		this.duration = duration;
	}
}
