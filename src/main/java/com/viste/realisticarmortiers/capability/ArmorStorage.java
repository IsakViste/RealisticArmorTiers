package com.viste.realisticarmortiers.capability;

import com.viste.realisticarmortiers.data.PotionEffect;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;

import javax.annotation.Nullable;

public class ArmorStorage implements IStorage<IArmor> {

    @Nullable
    @Override
    public INBT writeNBT(Capability<IArmor> capability, IArmor instance, Direction side) {
        NBTTagCompound bigCompoundList = new NBTTagCompound();
        NBTTagList itemTagList = new NBTTagList();
                
        for (ItemStack item : instance.getItems()) {
            NBTTagCompound tag = new NBTTagCompound();
            tag = item.writeToNBT(tag);
                    
            itemTagList.appendTag(tag);
        }
        
        NBTTagList setEffectsTagList = new NBTTagList();
        
        for (PotionEffect potionEffect : instance.getPotionEffects()) {
        	NBTTagCompound tag = new NBTTagCompound();
        	tag.setString("effect", potionEffect.id);
        	tag.setInteger("efficiency", potionEffect.efficiency);
            setEffectsTagList.appendTag(tag);
        }

        NBTTagList usedSetEffectsTagList = new NBTTagList();
        for (PotionEffect potionEffect : instance.getPotionEffects()) {
        	NBTTagCompound tag = new NBTTagCompound();
        	tag.setString("effect", potionEffect.id);
        	tag.setInteger("efficiency", potionEffect.efficiency);
        	tag.setInteger("duration", potionEffect.duration);
            usedSetEffectsTagList.appendTag(tag);
        }
        
        
        bigCompoundList.setTag("items", itemTagList);
        bigCompoundList.setTag("setEffects", setEffectsTagList);
        bigCompoundList.setTag("usedSetEffects", usedSetEffectsTagList);
        
        return bigCompoundList;
	}

    @Override
    public void readNBT(Capability<IArmor> capability, IArmor instance, Direction side, INBT nbt) {
        NBTTagCompound bigCompoundList = (NBTTagCompound) nbt;
        NBTTagList itemList = bigCompoundList.getTagList("items", net.minecraftforge.common.util.Constants.NBT.TAG_LIST);

        for (int i = 0; i < itemList.tagCount(); i++) {
            new ItemStack(itemList.getCompoundTagAt(i));
        }

        NBTTagList potionList = bigCompoundList.getTagList("setEffects", net.minecraftforge.common.util.Constants.NBT.TAG_LIST);

        for (int i = 0; i < potionList.tagCount(); i++) {
            new PotionEffect(potionList.getCompoundTagAt(i).getString("effect"), potionList.getCompoundTagAt(i).getInteger("efficiency"), 0);
        }

        NBTTagList usedPotionList = bigCompoundList.getTagList("usedSetEffects", net.minecraftforge.common.util.Constants.NBT.TAG_LIST);
        for (int i = 0; i < usedPotionList.tagCount(); i++) {
            new PotionEffect(usedPotionList.getCompoundTagAt(i).getString("effect"), usedPotionList.getCompoundTagAt(i).getInteger("efficiency"), usedPotionList.getCompoundTagAt(i).getInteger("duration"));
        }
    }
}