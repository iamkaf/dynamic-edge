package com.iamkaf.dynamicedge.registry;

import com.iamkaf.dynamicedge.DynamicEdge;
import com.iamkaf.dynamicedge.augment.Augment;
import com.mojang.serialization.Lifecycle;
import net.minecraft.ChatFormatting;
import net.minecraft.core.MappedRegistry;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

public class Augments {
    public static final ResourceKey<Registry<Augment>> AUGMENT_REGISTRY_KEY =
            ResourceKey.createRegistryKey(DynamicEdge.resource("augments"));

    public static final Registry<Augment> AUGMENT_REGISTRY =
            new MappedRegistry<>(AUGMENT_REGISTRY_KEY, Lifecycle.stable());

    public static Augment CAKE_DESTROYER = register(Augment.edge("cake_destroyer", 100, ChatFormatting.RED));
    public static Augment OCEANIC = register(Augment.edge("oceanic", 1000, ChatFormatting.AQUA));

    private static Augment register(Augment augment) {
        return Registry.register(AUGMENT_REGISTRY, augment.id(), augment);
    }

    public static void init() {
    }
}
