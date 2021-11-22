package com.viste.realisticarmortiers.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.viste.realisticarmortiers.RealisticArmorTiers;
import net.minecraft.command.CommandSource;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class RegisterCommandEvent {
    @SubscribeEvent
    public static void onRegisterCommandEvent(RegisterCommandsEvent event) {
        RealisticArmorTiers.LOGGER.info("Registering Commands!");
        CommandDispatcher<CommandSource> commandDispatcher = event.getDispatcher();
        RATCommands.register(commandDispatcher);
    }
}
