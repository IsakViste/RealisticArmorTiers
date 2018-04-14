package com.viste.realisticarmortiers.capability;

import java.util.List;

import com.viste.realisticarmortiers.data.Potion;

import net.minecraft.item.ItemStack;

public interface IArmor {

    public void addItem(ItemStack item);
    public void removeItem(ItemStack item);
    public void addPotionEffect(Potion potion);
    public void addPotionEffectList(List<Potion> potions);
    public void removePotionEffect(Potion potion);
    public void removeAllItems();
    public void setSpeed(float speed);
    
    public List<Potion> getPotionEffect();
    public List<ItemStack> getItems();
	public float getSpeed();
	
    
}
