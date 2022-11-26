package dev.limonblaze.oriacs.core.mixin;

import dev.limonblaze.oriacs.common.registry.OriacsMobEffects;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {
    
    @Redirect(method = "aiStep", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;isSensitiveToWater()Z"))
    private boolean oriacs$checkWaterResistance(LivingEntity entity) {
        return entity.isSensitiveToWater() && !entity.hasEffect(OriacsMobEffects.WATER_RESISTANCE.get());
    }
    
}
