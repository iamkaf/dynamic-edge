package com.iamkaf.dynamicedge;

import com.iamkaf.amber.api.core.AmberMod;
import com.iamkaf.dynamicedge.api.event.AugmentEvent;
import com.iamkaf.dynamicedge.augment.Augment;
import com.iamkaf.dynamicedge.command.DynamicEdgeCommands;
import com.iamkaf.dynamicedge.component.AugmentContainer;
import com.iamkaf.dynamicedge.registry.Augments;
import com.iamkaf.dynamicedge.registry.CreativeModeTabs;
import com.iamkaf.dynamicedge.registry.DataComponents;
import com.iamkaf.dynamicedge.registry.Items;
import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.logging.LogUtils;
import dev.architectury.registry.client.keymappings.KeyMappingRegistry;
import net.minecraft.client.KeyMapping;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;

public class DynamicEdge extends AmberMod {
    public static final String MOD_ID = "dynamicedge";
    public static final Logger LOGGER = LogUtils.getLogger();

    public static final KeyMapping TOOLTIP_KEYBIND = new KeyMapping(
            "key.dynamicedge.expand_tooltip",
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_G,
            "key.dynamicedge.category"
    );

    public DynamicEdge() {
        super(MOD_ID);
    }

    public static void init() {
        LOGGER.info("Perk Perk Hooray!");

        // Registries
        Items.init();
        CreativeModeTabs.init();
        DataComponents.init();
        Augments.init();
        DynamicEdgeCommands.init();
        KeyMappingRegistry.register(TOOLTIP_KEYBIND);
        AugmentEvent.AUGMENT_PROGRESS.register(DynamicEdge::onAugmentProgress);
    }

    private static void onAugmentProgress(ItemStack stack, Player player, Augment augment, Integer progress) {
        DataComponentType<AugmentContainer> component = DataComponents.AUGMENTS_DATA_COMPONENT.get();
        AugmentContainer augmentContainer = stack.getOrDefault(component, AugmentContainer.EMPTY);
        AugmentContainer newComponent = augmentContainer.addProgress(augment, progress);
        stack.set(component, newComponent);
    }


    /**
     * Creates resource location in the mod namespace with the given path.
     */
    public static ResourceLocation resource(String path) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
    }
}
