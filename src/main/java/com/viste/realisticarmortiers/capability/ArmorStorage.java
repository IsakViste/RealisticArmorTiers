package com.viste.realisticarmortiers.capability;

import com.viste.realisticarmortiers.data.Potion;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;

public class ArmorStorage implements IStorage<IArmor> {

	@Override
	public NBTBase writeNBT(Capability<IArmor> capability, IArmor instance, EnumFacing side) {
        NBTTagCompound bigCompoundList = new NBTTagCompound();
        NBTTagList itemTagList = new NBTTagList();
                
        for (ItemStack item : instance.getItems()) {
            NBTTagCompound tag = new NBTTagCompound();
            tag = item.writeToNBT(tag);
                    
            itemTagList.appendTag(tag);
        }
        
        NBTTagList potionTagList = new NBTTagList();
        
        for (Potion potion: instance.getPotionEffect()) {
        	NBTTagCompound tag = new NBTTagCompound();
        	tag.setInteger("effect", potion.effect);
        	tag.setInteger("efficiency", potion.efficiency);
        	potionTagList.appendTag(tag);
        }
                
        NBTTagList usedPotionTagList = new NBTTagList();
        for (Potion potion: instance.getPotionEffect()) {
        	NBTTagCompound tag = new NBTTagCompound();
        	tag.setInteger("effect", potion.effect);
        	tag.setInteger("efficiency", potion.efficiency);
        	tag.setInteger("duration", potion.duration);
        	potionTagList.appendTag(tag);
        }
        
        
        bigCompoundList.setTag("items", itemTagList);
        bigCompoundList.setTag("potions", potionTagList);
        bigCompoundList.setFloat("speed", instance.getSpeed());
        bigCompoundList.setTag("usedPotions", usedPotionTagList);
        
        return bigCompoundList;
	}

	@Override
	public void readNBT(Capability<IArmor> capability, IArmor instance, EnumFacing side, NBTBase nbt) {
		NBTTagCompound bigCompoundList = (NBTTagCompound)nbt;
		NBTTagList itemList = bigCompoundList.getTagList("items", net.minecraftforge.common.util.Constants.NBT.TAG_LIST);
		
        for (int i = 0; i < itemList.tagCount(); i++) {
        	new ItemStack(itemList.getCompoundTagAt(i));
        }
        
        NBTTagList potionList = bigCompoundList.getTagList("potions", net.minecraftforge.common.util.Constants.NBT.TAG_LIST);
        
        for(int i = 0; i < potionList.tagCount(); i++) {
        	new Potion(potionList.getCompoundTagAt(i).getInteger("effect"), potionList.getCompoundTagAt(i).getInteger("efficiency"), 0);
        }
		
        NBTTagList usedPotionList = bigCompoundList.getTagList("usedPotions", net.minecraftforge.common.util.Constants.NBT.TAG_LIST);
        for(int i=0; i < usedPotionList.tagCount(); i++) {
        	new Potion(usedPotionList.getCompoundTagAt(i).getInteger("effect"), usedPotionList.getCompoundTagAt(i).getInteger("efficiency"), usedPotionList.getCompoundTagAt(i).getInteger("duration"));
        }
        
        new Float(bigCompoundList.getFloat("speed")); 
	}
}