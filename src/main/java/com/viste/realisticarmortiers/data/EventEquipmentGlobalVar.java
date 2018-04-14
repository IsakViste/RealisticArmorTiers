package com.viste.realisticarmortiers.data;

public class EventEquipmentGlobalVar {
	//private int maxPotions = 28; // Amount of potions + 1 ???
	private int potionDur = 20; // 20 ticks ~= 1 second
	private float newPlayerSpeed = 0.1f;

	public float getSpeed(){
		return this.newPlayerSpeed;
	}
	
	public void setSpeed(float speed){
		this.newPlayerSpeed = speed;
	}

	public int getPotionDur(){
		return this.potionDur;
	}
}