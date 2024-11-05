package com.iamkaf.dynamicedge.mixin;

import com.iamkaf.dynamicedge.augment.OceanicAugment;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
    protected LivingEntityMixin(EntityType<? extends LivingEntity> entityType, Level level) {
        super(entityType, level);
    }

    /**
     * Makes it so the Oceanic swim speed bonus only applies while actually swimming.
     *
     * @see OceanicAugment
     */
    @Inject(method = "getAttributeValue(Lnet/minecraft/core/Holder;)D", at = @At("TAIL"), cancellable = true)
    public void edge$getAttributeValue(Holder<Attribute> attribute, CallbackInfoReturnable<Double> cir) {
        if (attribute.equals(Attributes.WATER_MOVEMENT_EFFICIENCY)) {
            boolean hasOceanic = this.getAttributes()
                    .hasModifier(Attributes.WATER_MOVEMENT_EFFICIENCY, OceanicAugment.SWIM_MODIFIER_ID);
            if (hasOceanic) {
                if (!this.isSwimming()) {
                    cir.setReturnValue(cir.getReturnValue() - OceanicAugment.SPEED_INCREASE);
                }
            }
        }
    }

    @Shadow
    public abstract AttributeMap getAttributes();
}
