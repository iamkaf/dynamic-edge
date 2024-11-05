package com.iamkaf.dynamicedge.mixin;

import com.iamkaf.dynamicedge.augment.Augment;
import com.iamkaf.dynamicedge.registry.Augments;
import com.iamkaf.dynamicedge.registry.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Comparator;
import java.util.List;

/**
 * This mixin adds the augments info to the item's tooltip.
 */
@Mixin(value = Item.class, priority = 999)
public class ItemMixin {
    @Inject(method =
            "appendHoverText(Lnet/minecraft/world/item/ItemStack;" + "Lnet/minecraft/world/item" +
                    "/Item$TooltipContext;Ljava/util/List;" + "Lnet/minecraft/world/item/TooltipFlag;)V",
            at = @At("HEAD"))
    public void addItemLevelToTooltip(ItemStack stack, Item.TooltipContext context,
            List<Component> tooltipComponents, TooltipFlag tooltipFlag, CallbackInfo ci) {
        var augmentContainer = stack.get(DataComponents.AUGMENTS_DATA_COMPONENT.get());

        if (augmentContainer == null) {
            return;
        }

        for (var augmentEntry : augmentContainer.augments()
                .entrySet()
                .stream()
                .sorted(Comparator.comparing(a -> a.getKey().getPath()))
                .toList()) {
            Augment augment = Augments.AUGMENT_REGISTRY.get(augmentEntry.getKey());
            if (augment == null) {
                throw new IllegalStateException("wat?");
            }
            augment.appendTooltip(stack, tooltipComponents);
        }
    }
}
