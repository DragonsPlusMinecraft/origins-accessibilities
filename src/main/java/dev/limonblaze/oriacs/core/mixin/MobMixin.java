package dev.limonblaze.oriacs.core.mixin;

import dev.limonblaze.oriacs.common.item.UmbrellaItem;
import dev.limonblaze.oriacs.common.registry.OriacsMobEffects;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MobEntity.class)
public abstract class MobMixin extends LivingEntity {
    
    protected MobMixin(EntityType<? extends LivingEntity> entityType, World level) {
        super(entityType, level);
    }
    
    @Inject(method = "isSunBurnTick", at = @At("HEAD"), cancellable = true)
    private void oriacs$checkSunResistanceAndUmbrella(CallbackInfoReturnable<Boolean> cir) {
        if(this.hasEffect(OriacsMobEffects.SUNLIGHT_RESISTANCE.get()) ||
           UmbrellaItem.canKeepOutSunlight(this)
        ) {
            cir.setReturnValue(false);
        }
    }
    
}
