package com.iamkaf.dynamicedge.command;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;

public interface AbstractSubCommand {
    String getName();
    String getDescription();

    LiteralArgumentBuilder<CommandSourceStack> register(LiteralArgumentBuilder<CommandSourceStack> dyn);
}
