package com.viste.realisticarmortiers.capability;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;

public class CapabilityArmorSet {
    @CapabilityInject(ArmorSetCapability.class)
    public static Capability<ArmorSetCapability> CAPABILITY_ARMOR_SET = null;

    public static void register() {
        CapabilityManager.INSTANCE.register(
                ArmorSetCapability.class,
                new ArmorSetCapability.ArmorSetCapabilityNBTStorage(),
                ArmorSetCapability::createADefaultInstance);
    }
}