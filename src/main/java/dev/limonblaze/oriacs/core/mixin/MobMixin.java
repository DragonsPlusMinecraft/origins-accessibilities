package dev.limonblaze.oriacs.core.mixin;

import dev.limonblaze.oriacs.common.item.UmbrellaItem;
import dev.limonblaze.oriacs.common.registry.OriacsMobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Mob.class)
public abstract class MobMixin extends LivingEntity {
    
    protected MobMixin(EntityType<? extends LivingEntity> entityType, Level level) {
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
