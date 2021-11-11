package com.viste.realisticarmortiers.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.viste.realisticarmortiers.RealisticArmorTiers;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.StringTextComponent;

public class ReloadJSONCommand {
    public static void register(CommandDispatcher<CommandSource> dispatcher) {
        LiteralArgumentBuilder<CommandSource> clearEffectsCommand
                = Commands.literal("realisticarmortiers")
                .requires((commandSource) -> commandSource.hasPermission(2))
                .then(Commands.literal("reload")
                        .executes(ReloadJSONCommand::reloadJSON)
                );

        dispatcher.register(clearEffectsCommand);
    }

    static int reloadJSON(CommandContext<CommandSource> commandContext) {
        Entity entity = commandContext.getSource().getEntity();
        if(entity instanceof ServerPlayerEntity) {
            ServerPlayerEntity player = (ServerPlayerEntity) entity;
            RealisticArmorTiers.reloadJSON(player);
            RealisticArmorTiers.LOGGER.info("Reloaded RealisticArmorTiers JSON");
            player.displayClientMessage(new StringTextComponent("Reloaded RealisticArmorTiers Sets!"), true);
        }

        return 1;
    }
}
