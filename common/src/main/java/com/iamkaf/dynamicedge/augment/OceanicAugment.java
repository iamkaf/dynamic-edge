package com.iamkaf.dynamicedge.augment;

import com.iamkaf.amber.api.level.LevelHelper;
import com.iamkaf.dynamicedge.DynamicEdge;
import com.iamkaf.dynamicedge.component.AugmentContainer;
import com.iamkaf.dynamicedge.registry.DataComponents;
import dev.architectury.event.events.common.TickEvent;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemAttributeModifiers;

import java.util.HashSet;
import java.util.Set;

public class OceanicAugment extends Augment {
    public static final String ID = "oceanic";
    public static final ResourceLocation SWIM_MODIFIER_ID = DynamicEdge.resource("oceanic_swim_speed_bonus");
    public static final double SPEED_INCREASE = 1f;
    public static final float PROGRESS_PER_TICK = 0.1f;

    public OceanicAugment(Integer maxProgress, ChatFormatting color, EquipmentSlotGroup[] slots) {
        super(DynamicEdge.resource(ID), maxProgress, color, slots);
    }

    @Override
    protected void setup() {
        TickEvent.PLAYER_POST.register(this::onDetectSwim);
    }

    private void onDetectSwim(Player player) {
        LevelHelper.runEveryXTicks(player.level(), 10, (gameTime) -> {
            ItemStack leggings = ItemStack.EMPTY;
            for (var item : player.getInventory().armor) {
                if (!item.isEmpty() && item.getItem() instanceof ArmorItem armorItem && armorItem.getType() == ArmorItem.Type.LEGGINGS) {
                    leggings = item;
                }
            }

            if (leggings.isEmpty()) {
                return null;
            }

            if (hasModifier(leggings, SWIM_MODIFIER_ID)) {
                return null;
            } else {
                addModifierIfNeeded(leggings);
            }

            if (player.isUnderWater()) {
                // Progress is multiplied by 10 because this runs every 10 ticks
                emit(leggings, player, this, (int) (PROGRESS_PER_TICK * 10));
            }

            return null;
        });
    }

    private void addModifierIfNeeded(ItemStack leggings) {
        AugmentContainer container = leggings.get(DataComponents.AUGMENTS_DATA_COMPONENT.get());
        if (container != null) {
            if (container.isActive(this)) {
                DataComponentType<ItemAttributeModifiers> attributeModifiersComponent =
                        net.minecraft.core.component.DataComponents.ATTRIBUTE_MODIFIERS;
                var extraModifiers = leggings.get(attributeModifiersComponent);
                assert extraModifiers != null;

                var attributeBuilder = ItemAttributeModifiers.builder();
                var defaultModifiers = ((ArmorItem) leggings.getItem()).getDefaultAttributeModifiers();
                Set<ResourceLocation> added = new HashSet<>();
                for (var mod : defaultModifiers.modifiers()) {
                    if (!added.contains(mod.modifier().id())) {
                        attributeBuilder.add(mod.attribute(), mod.modifier(), mod.slot());
                        added.add(mod.modifier().id());
                    }
                }
                for (var mod : extraModifiers.modifiers()) {
                    if (!added.contains(mod.modifier().id())) {
                        attributeBuilder.add(mod.attribute(), mod.modifier(), mod.slot());
                        added.add(mod.modifier().id());
                    }
                }
                attributeBuilder.add(
                        Attributes.WATER_MOVEMENT_EFFICIENCY,
                        new AttributeModifier(SWIM_MODIFIER_ID,
                                SPEED_INCREASE,
                                AttributeModifier.Operation.ADD_VALUE
                        ),
                        EquipmentSlotGroup.LEGS
                );
                leggings.set(attributeModifiersComponent, attributeBuilder.build());
            }
        }
    }
}
