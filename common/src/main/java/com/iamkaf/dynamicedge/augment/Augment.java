package com.iamkaf.dynamicedge.augment;

import com.iamkaf.amber.api.item.SmartTooltip;
import com.iamkaf.dynamicedge.DynamicEdge;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public record Augment(ResourceLocation id, Integer maxProgress, ChatFormatting color) {
    public static Augment edge(String id, Integer maxProgress, ChatFormatting color) {
        return new Augment(DynamicEdge.resource(id), maxProgress, color);
    }

    public void appendTooltip(List<Component> tooltipComponents) {
        new SmartTooltip().add(getName().withStyle(getColor()))
                .shift(Component.literal("> ").append(getDescription()))
                .shift(Component.literal("? ").withStyle(ChatFormatting.GRAY).append(getLevelingCriteria()))
                .into(tooltipComponents);
    }

    public @NotNull MutableComponent getName() {
        return Component.translatable("augment." + id.getNamespace() + "." + id.getPath());
    }

    public @NotNull MutableComponent getDescription() {
        return Component.translatable("augment." + id.getNamespace() + "." + id.getPath() + ".description");
    }

    public @NotNull MutableComponent getLevelingCriteria() {
        return Component.translatable("augment." + id.getNamespace() + "." + id.getPath() +
                ".leveling_criteria");
    }

    private ChatFormatting getColor() {
        return color;
    }

    public boolean isActive(int progress) {
        return progress >= maxProgress;
    }
}