package com.iamkaf.dynamicedge.registry;

import com.iamkaf.dynamicedge.DynamicEdge;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;

public class Items {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(DynamicEdge.MOD_ID, Registries.ITEM);

    public static RegistrySupplier<Item> EXAMPLE_ITEM = ITEMS.register("example_item",
            () -> new Item(new Item.Properties().arch$tab(CreativeModeTabs.DYNAMIC_EDGE))
    );

    public static void init() {
        ITEMS.register();
    }
}