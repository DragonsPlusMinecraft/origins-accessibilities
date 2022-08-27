package dev.limonblaze.oriacs.core.mixin;

import dev.limonblaze.oriacs.common.registry.OriacsMobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrownPotion;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ThrownPotion.class)
public class TrownPotionMixin {
    
    @Redirect(method = "applyWater", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;isSensitiveToWater()Z"))
    private boolean oriacs$checkWaterResistance(LivingEntity entity) {
        return entity.isSensitiveToWater() && !entity.hasEffect(OriacsMobEffects.WATER_RESISTANCE.get());
    }
    
}
