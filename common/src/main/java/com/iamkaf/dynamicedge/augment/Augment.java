package com.iamkaf.dynamicedge.augment;

import com.iamkaf.amber.api.item.SmartTooltip;
import com.iamkaf.dynamicedge.DynamicEdge;
import com.iamkaf.dynamicedge.api.event.AugmentEvent;
import com.iamkaf.dynamicedge.component.AugmentContainer;
import com.iamkaf.dynamicedge.registry.DataComponents;
import dev.architectury.event.EventResult;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TieredItem;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class Augment {
    private final ResourceLocation id;
    private final Integer maxProgress;
    private final ChatFormatting color;
    private final EquipmentSlotGroup[] slots;

    public Augment(ResourceLocation id, Integer maxProgress, ChatFormatting color,
            EquipmentSlotGroup[] slots) {
        this.id = id;
        this.maxProgress = maxProgress;
        this.color = color;
        this.slots = slots;

        // TODO: check for disabled augments here
        setup();
    }

    public static Augment edge(String id, Integer maxProgress, ChatFormatting color,
            EquipmentSlotGroup[] slots) {
        return new Augment(DynamicEdge.resource(id), maxProgress, color, slots);
    }

    public void appendTooltip(ItemStack stack, List<Component> tooltipComponents) {
        MutableComponent nameComponent = getName().withStyle(color());
        DataComponentType<AugmentContainer> component = DataComponents.AUGMENTS_DATA_COMPONENT.get();
        AugmentContainer augmentContainer = stack.get(component);

        //noinspection DataFlowIssue
        if (augmentContainer != null && augmentContainer.contains(id) && augmentContainer.get(id) < maxProgress) {
            nameComponent =
                    nameComponent.append(String.format(" (%s/%s)", augmentContainer.get(id), maxProgress))
                            .withStyle(ChatFormatting.GRAY);
        }

        new SmartTooltip().add(nameComponent)
                .shift(Component.literal("> ").append(getDescription().withStyle(ChatFormatting.WHITE)))
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

    protected boolean isGear(ItemStack stack) {
        var item = stack.getItem();

        List<EquipmentSlotGroup> slotList = Arrays.asList(slots);

        if (item instanceof TieredItem && slotList.contains(EquipmentSlotGroup.HAND)) {
            return true;
        }
        if (item instanceof ArmorItem && slotList.contains(EquipmentSlotGroup.ARMOR)) {
            return true;
        }
        if (item instanceof ArmorItem armorItem) {
            EquipmentSlot itemEquipmentSlot = armorItem.getEquipmentSlot();
            if (itemEquipmentSlot.equals(EquipmentSlot.HEAD) && slotList.contains(EquipmentSlotGroup.HEAD)) {
                return true;
            }
            if (itemEquipmentSlot.equals(EquipmentSlot.CHEST) && slotList.contains(EquipmentSlotGroup.CHEST)) {
                return true;
            }
            if (itemEquipmentSlot.equals(EquipmentSlot.LEGS) && slotList.contains(EquipmentSlotGroup.LEGS)) {
                return true;
            }
            if (itemEquipmentSlot.equals(EquipmentSlot.FEET) && slotList.contains(EquipmentSlotGroup.FEET)) {
                return true;
            }
            if (itemEquipmentSlot.equals(EquipmentSlot.BODY) && slotList.contains(EquipmentSlotGroup.BODY)) {
                return true;
            }
        }
        return false;
    }

    protected void emit(ItemStack gear, Player player, Augment augment, Integer progressAmount) {
        AugmentEvent.AUGMENT_PROGRESS.invoker().progress(gear, player, augment, progressAmount);
    }

    /**
     * Override this to set up the Augment.
     */
    protected void setup() {

    }

    public ResourceLocation id() {
        return id;
    }

    public Integer maxProgress() {
        return maxProgress;
    }

    public ChatFormatting color() {
        return color;
    }

    public EquipmentSlotGroup[] slots() {
        return slots;
    }

    // utility for event handling
    protected EventResult pass() {
        return EventResult.pass();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (Augment) obj;
        return Objects.equals(this.id, that.id) && Objects.equals(this.maxProgress,
                that.maxProgress
        ) && Objects.equals(this.color, that.color);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, maxProgress, color);
    }

    @Override
    public String toString() {
        return "Augment[" + "id=" + id + ", " + "maxProgress=" + maxProgress + ", " + "color=" + color + ","
                + " Slots=" + Arrays.toString(
                slots) + ']';
    }

    protected boolean hasModifier(ItemStack stack, ResourceLocation id) {
        var list = stack.get(net.minecraft.core.component.DataComponents.ATTRIBUTE_MODIFIERS);
        assert list != null;
        return list.modifiers().stream().anyMatch(m -> m.modifier().id().equals(id));
    }
}