package com.viste.realisticarmortiers.events;

import com.viste.realisticarmortiers.capability.CapabilityArmorSet;
import com.viste.realisticarmortiers.capability.CapabilityAttachEventHandler;
import com.viste.realisticarmortiers.commands.RegisterCommandEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

public class StartupCommon {
    @SubscribeEvent
    public static void onCommonSetupEvent(FMLCommonSetupEvent event) {
        MinecraftForge.EVENT_BUS.register(RegisterCommandEvent.class);

        CapabilityArmorSet.register();
        MinecraftForge.EVENT_BUS.register(CapabilityAttachEventHandler.class);

        MinecraftForge.EVENT_BUS.register(new EventEquipmentSets());
    }
}
