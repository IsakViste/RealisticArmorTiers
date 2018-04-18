package com.viste.realisticarmortiers.capability;

import java.util.ArrayList;
import java.util.List;

import com.viste.realisticarmortiers.data.Potion;

import net.minecraft.item.ItemStack;

public class Armor implements IArmor {
	
	private List<ItemStack> items = new ArrayList<>();
	private List<Potion> potions = new ArrayList<>();
	private List<Potion> usedPotion = new ArrayList<>();
	private float speed = 0.1f;
	
	@Override
	public void addItem(ItemStack item) {
		items.add(item);
	}

	@Override
	public void removeItem(ItemStack item) {
		items.remove(item);
	}

	@Override
	public void addPotionEffect(Potion potion) {
		potions.add(potion);
	}
	
	public void addPotionEffectList(List<Potion> potions) {
		this.potions.addAll(potions);
	}

	@Override
	public void removePotionEffect(Potion potion) {
		potions.remove(potion);
	}
	
	@Override
	public float getSpeed()
	{
		return this.speed;
	}
	
	@Override
	public void setSpeed(float speed)
	{
		this.speed = speed;
	}
	
	
	@Override
	public void removeAllItems()
	{
		items.removeAll(items);
		potions.removeAll(potions);
	}

	@Override
	public List<Potion> getPotionEffect() {
		return this.potions;
	}

	@Override
	public List<Potion> getUsedPotionEffect() {
		return this.usedPotion;
	}
	
	@Override
	public void removeUsedPotion(Potion potion)
	{
		this.usedPotion.remove(potion);
	}
	
	@Override
	public void addUsedPotion(Potion potion)
	{
		this.usedPotion.add(potion);
	}
	
	@Override
	public List<ItemStack> getItems() {

		return this.items;
	}

}
