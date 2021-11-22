package com.viste.realisticarmortiers.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.viste.realisticarmortiers.RealisticArmorTiers;
import com.viste.realisticarmortiers.capability.ArmorSetCapability;
import com.viste.realisticarmortiers.capability.CapabilityArmorSet;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.ChatType;
import net.minecraft.util.text.StringTextComponent;

public class ReloadJSONCommand {
    public static void register(CommandDispatcher<CommandSource> dispatcher) {
        LiteralArgumentBuilder<CommandSource> clearEffectsCommand
                = Commands.literal("RAT")
                .requires((commandSource) -> commandSource.hasPermission(2))
                .then(Commands.literal("reload")
                        .executes(ReloadJSONCommand::reloadJSON))
                .then(Commands.literal("clearNBT").then(Commands.argument("player", EntityArgument.player())
                        .executes((commandSource) -> clearNBT(commandSource.getSource(), EntityArgument.getPlayer(commandSource, "player")))))
                ;

        dispatcher.register(clearEffectsCommand);
    }

    static int reloadJSON(CommandContext<CommandSource> commandContext) {
        ServerPlayerEntity player;
        try {
            player = commandContext.getSource().getPlayerOrException();
        } catch (CommandSyntaxException error) {
            return 0;
        }

        RealisticArmorTiers.reloadJSON(player);
        RealisticArmorTiers.LOGGER.info("Reloaded RealisticArmorTiers JSON");
        player.displayClientMessage(new StringTextComponent("Reloaded RealisticArmorTiers Sets!"), true);

        return 1;
    }

    static int clearNBT(CommandSource commandSource, ServerPlayerEntity player) {
        RealisticArmorTiers.LOGGER.info("Clearing NBT from " + player.getDisplayName().getString());

        ArmorSetCapability armorSetCapability = player.getCapability(CapabilityArmorSet.CAPABILITY_ARMOR_SET).orElse(null);
        if (armorSetCapability == null) return 0;

        armorSetCapability.clearAll();
        player.displayClientMessage(new StringTextComponent("Cleared ArmorSet NBT from " + player.getDisplayName().getString()), false);
        return 1;
    }
}
