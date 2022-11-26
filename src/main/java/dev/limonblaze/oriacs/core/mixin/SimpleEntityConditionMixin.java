package dev.limonblaze.oriacs.core.mixin;

import dev.limonblaze.oriacs.common.item.UmbrellaItem;
import dev.limonblaze.oriacs.common.registry.OriacsMobEffects;
import io.github.apace100.origins.power.factory.condition.EntityConditions;
import io.github.apace100.origins.util.SerializableData;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = EntityConditions.class, remap = false)
public class SimpleEntityConditionMixin {
    
    @Inject(method = "lambda$register$10", at = @At("HEAD"), cancellable = true)
    private static void oriacs$checkSunResistanceAndUmbrella(SerializableData.Instance data, LivingEntity entity, CallbackInfoReturnable<Boolean> cir) {
        if(entity.hasEffect(OriacsMobEffects.SUNLIGHT_RESISTANCE.get()) || UmbrellaItem.canKeepOutSunlight(entity)) {
            cir.setReturnValue(false);
        }
    }
    
}
