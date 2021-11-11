package com.viste.realisticarmortiers;

import com.viste.realisticarmortiers.commands.RegisterCommandEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

public class StartupCommon {
    @SubscribeEvent
    public static void onCommonSetupEvent(FMLCommonSetupEvent event) {
        MinecraftForge.EVENT_BUS.register(RegisterCommandEvent.class);
    }
}
