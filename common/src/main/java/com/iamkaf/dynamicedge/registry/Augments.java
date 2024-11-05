package com.iamkaf.dynamicedge.registry;

import com.iamkaf.dynamicedge.DynamicEdge;
import com.iamkaf.dynamicedge.augment.Augment;
import com.iamkaf.dynamicedge.augment.CakeDestroyerAugment;
import com.iamkaf.dynamicedge.augment.OceanicAugment;
import com.mojang.serialization.Lifecycle;
import net.minecraft.ChatFormatting;
import net.minecraft.core.MappedRegistry;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EquipmentSlotGroup;

public class Augments {
    public static final ResourceKey<Registry<Augment>> AUGMENT_REGISTRY_KEY =
            ResourceKey.createRegistryKey(DynamicEdge.resource("augments"));

    public static final Registry<Augment> AUGMENT_REGISTRY =
            new MappedRegistry<>(AUGMENT_REGISTRY_KEY, Lifecycle.stable());

    public static Augment CAKE_DESTROYER = register(new CakeDestroyerAugment(100,
            ChatFormatting.RED,
            new EquipmentSlotGroup[]{EquipmentSlotGroup.MAINHAND}
    ));
    public static Augment OCEANIC = register(new OceanicAugment(7500,
            ChatFormatting.AQUA,
            new EquipmentSlotGroup[]{EquipmentSlotGroup.LEGS}
    ));

    private static Augment register(Augment augment) {
        return Registry.register(AUGMENT_REGISTRY, augment.id(), augment);
    }

    public static void init() {
    }
}
