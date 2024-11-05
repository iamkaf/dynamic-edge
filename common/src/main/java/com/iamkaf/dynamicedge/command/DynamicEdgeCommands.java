package com.iamkaf.dynamicedge.command;

import com.iamkaf.dynamicedge.DynamicEdge;
import com.iamkaf.dynamicedge.util.LiteralSetHolder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import dev.architectury.event.events.common.CommandRegistrationEvent;
import dev.architectury.platform.Platform;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;

public class DynamicEdgeCommands {
    private static final String MAIN_COMMAND = "edge";

    private static final LiteralSetHolder<AbstractSubCommand> commands = new LiteralSetHolder<>();

    private static final AbstractSubCommand LIST_COMMAND = commands.add(new ListAugmentsCommand());
    private static final AbstractSubCommand AUGMENT_COMMAND = commands.add(new AugmentCommand());

    public static void init() {
        CommandRegistrationEvent.EVENT.register((dispatcher, registry, selection) -> {
            LiteralArgumentBuilder<CommandSourceStack> literalEdge = Commands.literal(MAIN_COMMAND);
            dispatcher.register(literalEdge.executes(DynamicEdgeCommands::displayInfo));
            commands.get().forEach(cmd -> dispatcher.register(cmd.register(literalEdge)));
        });
    }

    private static int displayInfo(CommandContext<CommandSourceStack> context) {
        // TODO: display mod version here
        var version = Platform.getMod(DynamicEdge.MOD_ID).getVersion();
        var amberVersion = Platform.getMod("amber").getVersion();
        context.getSource()
                .sendSuccess(
                        () -> Component.literal(String.format("--- Dynamic Edge v%s ---", version)),
                        false
                );

        commands.get()
                .forEach(cmd -> context.getSource()
                        .sendSuccess(() -> Component.literal(String.format("/%s %s - %s",
                                MAIN_COMMAND,
                                cmd.getName(),
                                cmd.getDescription()
                        )), false));
        context.getSource()
                .sendSuccess(
                        () -> Component.literal(String.format("- Amber v%s", amberVersion)).withStyle(
                                ChatFormatting.GRAY),
                        false
                );
        return 1;
    }
}