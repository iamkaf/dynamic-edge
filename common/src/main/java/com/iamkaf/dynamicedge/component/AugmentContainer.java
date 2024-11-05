package com.iamkaf.dynamicedge.component;

import com.google.common.collect.ImmutableMap;
import com.iamkaf.dynamicedge.augment.Augment;
import com.iamkaf.dynamicedge.registry.DataComponents;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("DataFlowIssue") // trust me bro
public record AugmentContainer(boolean enabled, Map<ResourceLocation, Integer> augments) {
    public static final AugmentContainer EMPTY = new AugmentContainer(true, Map.of());
    private static final int MAX_SIZE = 256;

    public static final Codec<AugmentContainer> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.BOOL.fieldOf("enabled").forGetter(AugmentContainer::enabled),
            Codec.unboundedMap(ResourceLocation.CODEC, Codec.INT)
                    .fieldOf("augments")
                    .forGetter(AugmentContainer::augments)
    ).apply(instance, AugmentContainer::new));


    public AugmentContainer(boolean enabled, Map<ResourceLocation, Integer> augments) {
        if (augments.size() > MAX_SIZE) {
            throw new IllegalArgumentException("Got " + augments.size() + " augments, but maximum is 256");
        } else {
            this.augments = augments;
        }
        this.enabled = enabled;
    }

    public @Nullable Integer get(ResourceLocation key) {
        return augments.get(key);
    }

    public boolean contains(ResourceLocation key) {
        return augments.containsKey(key);
    }

    public AugmentContainer set(Augment augment, int progress) {
        HashMap<ResourceLocation, Integer> copy = new HashMap<>(augments);
        copy.put(augment.id(), Math.min(progress, augment.maxProgress()));
        return new AugmentContainer(enabled, ImmutableMap.copyOf(copy));
    }

    public AugmentContainer delete(ResourceLocation key) {
        HashMap<ResourceLocation, Integer> copy = new HashMap<>(augments);
        copy.remove(key);
        return new AugmentContainer(enabled, ImmutableMap.copyOf(copy));
    }

    public AugmentContainer addProgress(Augment augment, int progress) {
        if (!contains(augment.id())) {
            return set(augment, progress);
        }
        int currentProgress = get(augment.id());
        return set(augment, currentProgress + progress);
    }

    private AugmentContainer copy() {
        return new AugmentContainer(enabled, ImmutableMap.copyOf(augments));
    }

    public boolean isActive(Augment augment) {
        return contains(augment.id()) && get(augment.id()) >= augment.maxProgress();
    }
}
