package com.viste.realisticarmortiers.capability;

import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ArmorProvider implements ICapabilitySerializable<INBT> {

	@CapabilityInject(IArmor.class)
	public static final Capability<IArmor> Armor = null;

	private final IArmor instance = Armor.getDefaultInstance();

	@Nonnull
	@Override
	public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
		return cap == Armor ? (LazyOptional<T>) this.instance : LazyOptional.empty();
	}

	@Nonnull
	@Override
	public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap) {
		 return getCapability(cap, null);
	}

	 
	@Override
	public INBT serializeNBT() {
		return Armor.getStorage().writeNBT(Armor, this.instance, null);

	}
	 
	@Override
	public void deserializeNBT(INBT nbt) {
		Armor.getStorage().readNBT(Armor, this.instance, null, nbt);

	}
}