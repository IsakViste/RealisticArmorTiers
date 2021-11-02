package com.viste.realisticarmortiers.capability;

import com.viste.realisticarmortiers.data.PotionEffect;
import net.minecraft.item.ItemStack;

import java.util.List;

public interface IArmor {

    public List<ItemStack> getItems();
    public void addItem(ItemStack item);
    public void removeItem(ItemStack item);

    public List<PotionEffect> getPotionEffects();
    public void addPotionEffect(PotionEffect potionEffect);
    public void addPotionEffectList(List<PotionEffect> potionEffects);
    public void removePotionEffect(PotionEffect potionEffect);

    public List<PotionEffect> getUsedPotionEffects();
    public void addUsedPotionEffect(PotionEffect potionEffect);
    public void removeUsedPotionEffect(PotionEffect potionEffect);

    public void removeAllItems();
}