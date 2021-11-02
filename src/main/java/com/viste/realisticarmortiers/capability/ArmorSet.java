package com.viste.realisticarmortiers.capability;

import com.viste.realisticarmortiers.data.PotionEffect;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class ArmorSet implements IArmor {
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

    public static class ArmorSetNBTStorage implements Capability.IStorage<ArmorSet> {
        @Nullable
        @Override
        public INBT writeNBT(Capability<ArmorSet> capability, ArmorSet instance, Direction side) {
            CompoundNBT bigCompoundList = new CompoundNBT();
            ListNBT itemTagList = new ListNBT();

            for (ItemStack item : instance.getItems()) {
                CompoundNBT tag = item.getOrCreateTag();
                itemTagList.add(tag);
            }

            ListNBT setEffectsTagList = new ListNBT();

            for (PotionEffect potionEffect : instance.getPotionEffects()) {
                CompoundNBT tag = new CompoundNBT();
                tag.putString("effect", potionEffect.id);
                tag.putInt("efficiency", potionEffect.efficiency);
                setEffectsTagList.add(tag);
            }

            ListNBT usedPotionEffectsTagList = new ListNBT();
            for (PotionEffect potionEffect : instance.getUsedPotionEffects()) {
                CompoundNBT tag = new CompoundNBT();
                tag.putString("effect", potionEffect.id);
                tag.putInt("efficiency", potionEffect.efficiency);
                tag.putInt("duration", potionEffect.duration);
                usedPotionEffectsTagList.add(tag);
            }


            bigCompoundList.put("items", itemTagList);
            bigCompoundList.put("setEffects", setEffectsTagList);
            bigCompoundList.put("usedPotionEffects", usedPotionEffectsTagList);

            return bigCompoundList;
        }

        @Override
        public void readNBT(Capability<ArmorSet> capability, ArmorSet instance, Direction side, INBT nbt) {
            CompoundNBT bigCompoundList = (CompoundNBT) nbt;

            ListNBT itemList = bigCompoundList.getList("items", net.minecraftforge.common.util.Constants.NBT.TAG_LIST);
            for(INBT inbt : itemList) {
                CompoundNBT c = (CompoundNBT) inbt;
                instance.addItem(ItemStack.of(c));
            }

            ListNBT setEffectList = bigCompoundList.getList("setEffects", net.minecraftforge.common.util.Constants.NBT.TAG_LIST);
            for(INBT inbt : setEffectList) {
                CompoundNBT c = (CompoundNBT) inbt;
                instance.addPotionEffect(new PotionEffect(c.getString("effect"), c.getInt("efficiency"), 0));
            }

            ListNBT usedPotionEffectList = bigCompoundList.getList("usedPotionEffects", net.minecraftforge.common.util.Constants.NBT.TAG_LIST);
            for(INBT inbt : usedPotionEffectList) {
                CompoundNBT c = (CompoundNBT) inbt;
                instance.addUsedPotionEffect(new PotionEffect(c.getString("effect"), c.getInt("efficiency"), c.getInt("duration")));
            }
        }
    }

    public static ArmorSet createADefaultInstance() {
        return new ArmorSet();
    }
}
