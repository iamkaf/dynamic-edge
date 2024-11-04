package com.iamkaf.dynamicedge.registry;

import com.iamkaf.dynamicedge.DynamicEdge;
import com.iamkaf.dynamicedge.component.AugmentContainer;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.DeferredSupplier;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;

public class DataComponents {
    public static final DeferredRegister<DataComponentType<?>> DATA_COMPONENT_TYPES =
            DeferredRegister.create(DynamicEdge.MOD_ID, Registries.DATA_COMPONENT_TYPE);

    public static final DeferredSupplier<DataComponentType<AugmentContainer>> AUGMENTS_DATA_COMPONENT =
            DATA_COMPONENT_TYPES.register(
                    DynamicEdge.resource("augments"),
                    () -> DataComponentType.<AugmentContainer>builder()
                            .persistent(AugmentContainer.CODEC)
                            .build()
            );

    public static void init() {
        DATA_COMPONENT_TYPES.register();
    }
}
