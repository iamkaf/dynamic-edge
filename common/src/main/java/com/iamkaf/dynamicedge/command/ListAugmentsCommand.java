package com.iamkaf.dynamicedge.command;

import com.iamkaf.dynamicedge.registry.Augments;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;

public class ListAugmentsCommand implements AbstractSubCommand {
    public static final String NAME = "list";
    public static final String DESCRIPTION = "Lists registered augments.";

    public static int list(CommandContext<CommandSourceStack> context) {

        context.getSource().sendSuccess(() -> Component.literal("--- Augments Registered ---"), false);

        Augments.AUGMENT_REGISTRY.stream().forEach(augment -> {
            var line = augment.getName()
                    .withStyle(ChatFormatting.AQUA)
                    .append(" - ")
                    .append(augment.getDescription()
                            .withStyle(ChatFormatting.GRAY)
                            .append(Component.literal(" [" + augment.id().toString() + "]")));

            context.getSource().sendSuccess(() -> line, false);
        });
        return 1;
    }

    public LiteralArgumentBuilder<CommandSourceStack> register(LiteralArgumentBuilder<CommandSourceStack> dyn) {
        return dyn.then(Commands.literal(NAME).executes(ListAugmentsCommand::list));
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public String getDescription() {
        return DESCRIPTION;
    }
}