package com.iamkaf.dynamicedge.augment;

import com.iamkaf.amber.api.level.LevelHelper;
import com.iamkaf.amber.api.math.Chance;
import com.iamkaf.dynamicedge.DynamicEdge;
import com.iamkaf.dynamicedge.registry.DataComponents;
import dev.architectury.event.EventResult;
import dev.architectury.event.events.common.BlockEvent;
import dev.architectury.event.events.common.EntityEvent;
import dev.architectury.utils.value.IntValue;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TieredItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class CakeDestroyerAugment extends Augment {
    public static final String ID = "cake_destroyer";
    // Amount of progress awarded to the tool
    private static final int PROGRESS_PER_CAKE = 1;
    // Chance that sugar will drop (0.05f = 5%)
    private static final float SUGAR_DROP_CHANCE = 0.05f;

    public CakeDestroyerAugment(Integer maxProgress, ChatFormatting color, EquipmentSlotGroup[] slots) {
        super(DynamicEdge.resource(ID), maxProgress, color, slots);
    }

    // Checks if a block state is a coke. Maybe expand this to include modded cakes?
    public static boolean isCake(BlockState state) {
        return state.is(Blocks.CAKE) || state.is(BlockTags.CANDLE_CAKES);
    }

    @Override
    protected void setup() {
        BlockEvent.BREAK.register(this::onCakeBroken);
        EntityEvent.LIVING_HURT.register(this::onAugmentedHit);
    }

    // Checks if an entity was hit by a weapon with this augment, drops sugar if so
    private EventResult onAugmentedHit(LivingEntity livingEntity, DamageSource damageSource, float damage) {
        if (!(livingEntity.level() instanceof ServerLevel level)) return pass();

        if (damageSource.getDirectEntity() instanceof Player player) {
            var handItem = player.getMainHandItem();
            if (level.isClientSide || handItem.isEmpty()) return pass();

            var augmentContainer = handItem.get(DataComponents.AUGMENTS_DATA_COMPONENT.get());
            if (augmentContainer != null && augmentContainer.isActive(this) && Chance.of(SUGAR_DROP_CHANCE) && damage > 2) {
                LevelHelper.dropItem(level, Items.SUGAR, livingEntity.position());
            }
        }

        return pass();
    }

    // Awards augment progress when breaking cakes
    private EventResult onCakeBroken(Level level, BlockPos blockPos, BlockState blockState,
            ServerPlayer player, @Nullable IntValue intValue) {
        var handItem = player.getMainHandItem();

        if (level.isClientSide || handItem.isEmpty() || !isCake(blockState)) return pass();

        if (handItem.getItem() instanceof TieredItem) {
            emit(handItem, player, this, PROGRESS_PER_CAKE);
        }
        return pass();
    }
}
