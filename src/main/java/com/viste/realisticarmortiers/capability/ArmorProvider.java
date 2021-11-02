package com.viste.realisticarmortiers.capability;

import com.viste.realisticarmortiers.RealisticArmorTiers;
import com.viste.realisticarmortiers.data.Armor;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.NBTTypes;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ArmorProvider implements ICapabilitySerializable<INBT> {

    private ArmorSet armorSet = new ArmorSet();

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if(RealisticArmorTiers.CAPABILITY_ARMOR_SET == cap) {
            return (LazyOptional<T>)LazyOptional.of(()-> armorSet);
        }

        return LazyOptional.empty();
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap) {
        return getCapability(cap, null);
    }

    private final static String ARMOR_NBT = "armor";

    @Override
    public INBT serializeNBT() {
        CompoundNBT nbt = new CompoundNBT();
        INBT armorNBT = RealisticArmorTiers.CAPABILITY_ARMOR_SET.writeNBT(armorSet, null);
        nbt.put(ARMOR_NBT, armorNBT);
        return nbt;
    }

    @Override
    public void deserializeNBT(INBT nbt) {
        if (nbt.getId() != new CompoundNBT().getId()) {
            RealisticArmorTiers.LOGGER.warn("Unexpected NBT type:" + nbt);
            return;  // leave as default in case of error
        }

        CompoundNBT compoundNBT = (CompoundNBT)nbt;
        INBT armorNBT = compoundNBT.get(ARMOR_NBT);
        RealisticArmorTiers.CAPABILITY_ARMOR_SET.readNBT(armorSet, null, armorNBT);
    }
}