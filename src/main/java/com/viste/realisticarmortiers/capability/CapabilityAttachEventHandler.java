package com.viste.realisticarmortiers.capability;

import com.viste.realisticarmortiers.RealisticArmorTiers;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class CapabilityAttachEventHandler {

    // CapabilityAttachEvent is used to attach Capabilities to vanilla objects
    @SubscribeEvent
    public static void attachCapabilityToEntityHandler(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof ServerPlayerEntity) {
            event.addCapability(new ResourceLocation(RealisticArmorTiers.MODID, "armor_set") , new CapabilityProviderArmorSet());
        }
    }
}