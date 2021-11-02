package com.viste.realisticarmortiers.capability;

import java.util.ArrayList;
import java.util.List;

import com.viste.realisticarmortiers.data.PotionEffect;
import net.minecraft.item.ItemStack;

public class Armor implements IArmor {
	
	private List<ItemStack> items = new ArrayList<>();
	private List<PotionEffect> potionEffects = new ArrayList<>();
	private List<PotionEffect> usedPotionEffects = new ArrayList<>();

	@Override
	public List<ItemStack> getItems() {
		return this.items;
	}

	@Override
	public void addItem(ItemStack item) {
		items.add(item);
	}

	@Override
	public void removeItem(ItemStack item) {
		items.remove(item);
	}

	@Override
	public void removeAllItems() {
		items.removeAll(items);
		potionEffects.removeAll(potionEffects);
	}

	@Override
	public List<PotionEffect> getPotionEffects() {
		return this.potionEffects;
	}

	@Override
	public void addPotionEffect(PotionEffect potionEffect) {
		potionEffects.add(potionEffect);
	}

	@Override
	public void addPotionEffectList(List<PotionEffect> potionEffect) {
		this.potionEffects.addAll(potionEffect);
	}

	@Override
	public void removePotionEffect(PotionEffect potionEffect) {
		potionEffects.remove(potionEffect);
	}

	@Override
	public List<PotionEffect> getUsedPotionEffects() {
		return this.usedPotionEffects;
	}

	@Override
	public void addUsedPotionEffect(PotionEffect potionEffect) {
		this.usedPotionEffects.add(potionEffect);
	}
	
	@Override
	public void removeUsedPotionEffect(PotionEffect potionEffect) {
		this.usedPotionEffects.remove(potionEffect);
	}
}
