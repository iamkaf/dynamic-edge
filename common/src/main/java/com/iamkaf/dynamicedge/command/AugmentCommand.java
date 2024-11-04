package com.iamkaf.dynamicedge.command;

import com.iamkaf.dynamicedge.augment.Augment;
import com.iamkaf.dynamicedge.component.AugmentContainer;
import com.iamkaf.dynamicedge.registry.Augments;
import com.iamkaf.dynamicedge.registry.DataComponents;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.ResourceLocationArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

import java.util.Collection;
import java.util.Objects;

public class AugmentCommand implements AbstractSubCommand {

    public static final String NAME = "add";
    public static final String DESCRIPTION = "Adds an augment to the held item.";

    public static int augment(CommandSourceStack source, Collection<? extends Entity> targets,
            ResourceLocation providedAugmentString, int i) throws CommandSyntaxException {
        if (targets.isEmpty()) {
            return 0;
        }

        Augment augment;

        try {
            augment = Augments.AUGMENT_REGISTRY.get(providedAugmentString);
        } catch (Exception e) {
            throw new DynamicCommandExceptionType(object -> Component.literal("Augment not found: " + object.toString())).create(
                    providedAugmentString);
        }

        if (augment == null) {
            throw new DynamicCommandExceptionType(object -> Component.literal("Augment not found: " + object.toString())).create(
                    providedAugmentString);
        }

        for (Entity entity : targets) {
            if (entity instanceof LivingEntity livingEntity) {
                ItemStack stack = livingEntity.getMainHandItem();
                AugmentContainer augmentContainer =
                        stack.getOrDefault(DataComponents.AUGMENTS_DATA_COMPONENT.get(),
                                AugmentContainer.EMPTY
                        );
                AugmentContainer newComponent = augmentContainer.set(augment, i);
                stack.set(DataComponents.AUGMENTS_DATA_COMPONENT.get(), newComponent);
            }
        }
        if (targets.size() == 1) {
            source.sendSuccess(() -> Component.literal(String.format("Added augment %s to %s.",
                    augment.getName().getString(),
                    Objects.requireNonNull(targets.iterator().next().getDisplayName()).getString()
            )), true);
        } else {
            source.sendSuccess(() -> Component.literal(String.format("Added augment %s to %s entities.",
                    augment,
                    targets.size()
            )), true);
        }
        return 1;
    }

    public LiteralArgumentBuilder<CommandSourceStack> register(LiteralArgumentBuilder<CommandSourceStack> dyn) {
        return dyn.requires(commandSourceStack -> commandSourceStack.hasPermission(2))
                .then(Commands.literal(NAME)
                        .then(Commands.argument("targets", EntityArgument.entities())
                                .then(Commands.argument("augment", ResourceLocationArgument.id())
                                        .executes(commandContext -> AugmentCommand.augment(commandContext.getSource(),
                                                EntityArgument.getEntities(commandContext, "targets"),
                                                ResourceLocationArgument.getId(commandContext, "augment"),
                                                0
                                        ))
                                        .then(Commands.argument("progress", IntegerArgumentType.integer(0))
                                                .executes(commandContext -> AugmentCommand.augment(
                                                        commandContext.getSource(),
                                                        EntityArgument.getEntities(commandContext, "targets"),
                                                        ResourceLocationArgument.getId(commandContext,
                                                                "augment"
                                                        ),
                                                        IntegerArgumentType.getInteger(commandContext,
                                                                "progress"
                                                        )
                                                ))))));
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
