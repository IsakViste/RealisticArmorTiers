package com.viste.realisticarmortiers.capability;

import com.viste.realisticarmortiers.data.PotionEffect;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;

import javax.annotation.Nullable;

public class ArmorStorage implements IStorage<IArmor> {

    @Nullable
    @Override
    public INBT writeNBT(Capability<IArmor> capability, IArmor instance, Direction side) {
        CompoundNBT bigCompoundList = new CompoundNBT();
        ListNBT itemTagList = new ListNBT();
                
        for (ItemStack item : instance.getItems()) {
            CompoundNBT tag = new CompoundNBT();
            item.setTag(tag);
                    
            itemTagList.add(tag);
        }
        
        ListNBT setEffectsTagList = new ListNBT();
        
        for (PotionEffect potionEffect : instance.getPotionEffects()) {
            CompoundNBT tag = new CompoundNBT();
        	tag.putString("effect", potionEffect.id);
        	tag.putInt("efficiency", potionEffect.efficiency);
            setEffectsTagList.add(tag);
        }

        ListNBT usedSetEffectsTagList = new ListNBT();
        for (PotionEffect potionEffect : instance.getPotionEffects()) {
            CompoundNBT tag = new CompoundNBT();
        	tag.putString("effect", potionEffect.id);
        	tag.putInt("efficiency", potionEffect.efficiency);
        	tag.putInt("duration", potionEffect.duration);
            usedSetEffectsTagList.add(tag);
        }
        
        
        bigCompoundList.put("items", itemTagList);
        bigCompoundList.put("setEffects", setEffectsTagList);
        bigCompoundList.put("usedSetEffects", usedSetEffectsTagList);
        
        return bigCompoundList;
	}

    @Override
    public void readNBT(Capability<IArmor> capability, IArmor instance, Direction side, INBT nbt) {
        CompoundNBT bigCompoundList = (CompoundNBT) nbt;

        ListNBT itemList = bigCompoundList.getList("items", net.minecraftforge.common.util.Constants.NBT.TAG_LIST);
        for(INBT inbt : itemList) {
            CompoundNBT c = (CompoundNBT) inbt;
            instance.addItem(ItemStack.of(c));
        }

        ListNBT potionList = bigCompoundList.getList("setEffects", net.minecraftforge.common.util.Constants.NBT.TAG_LIST);
        for(INBT inbt : potionList) {
            CompoundNBT c = (CompoundNBT) inbt;
            new PotionEffect(c.getString("effect"), c.getInt("efficiency"), 0);
        }

        ListNBT usedPotionList = bigCompoundList.getList("usedSetEffects", net.minecraftforge.common.util.Constants.NBT.TAG_LIST);
        for(INBT inbt : potionList) {
            CompoundNBT c = (CompoundNBT) inbt;
            new PotionEffect(c.getString("effect"), c.getInt("efficiency"), c.getInt("duration"));
        }
    }
}