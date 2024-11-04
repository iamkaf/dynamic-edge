package com.iamkaf.dynamicedge.fabric.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.minecraft.util.FastColor;
import net.minecraft.world.item.Items;

public final class DynamicEdgeFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ColorProviderRegistry.ITEM.register(
                (itemStack, i) -> FastColor.ARGB32.color(125, 255, 0, 80),
                Items.BLAZE_POWDER
        );
    }
}
