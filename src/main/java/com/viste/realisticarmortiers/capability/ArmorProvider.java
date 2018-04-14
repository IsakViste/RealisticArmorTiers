package com.viste.realisticarmortiers.capability;

import net.minecraft.nbt.NBTBase;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

public class ArmorProvider implements ICapabilitySerializable<NBTBase> {

	 @CapabilityInject(IArmor.class)

	 public static final Capability<IArmor> Armor = null;

	 
	 private IArmor instance = Armor.getDefaultInstance();

	 
	 @Override
	 public boolean hasCapability(Capability<?> capability, EnumFacing facing)
	 {

		 return capability == Armor;

	 }

	 
	 @Override
	 public <T> T getCapability(Capability<T> capability, EnumFacing facing)
	 {

		 return capability == Armor ? Armor.<T> cast(this.instance) : null;
	 }

	 
	 @Override
	 public NBTBase serializeNBT()
	 {

		 return Armor.getStorage().writeNBT(Armor, this.instance, null);

	 }

	 
	 @Override
	 public void deserializeNBT(NBTBase nbt)
	 {

		 Armor.getStorage().readNBT(Armor, this.instance, null, nbt);

	 }
}