package dev.limonblaze.oriacs.core.mixin;

import dev.limonblaze.oriacs.common.item.UmbrellaItem;
import dev.limonblaze.oriacs.common.registry.OriacsMobEffects;
import io.github.edwinmindcraft.apoli.common.condition.entity.SimpleEntityCondition;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = SimpleEntityCondition.class, remap = false)
public class SimpleEntityConditionMixin {
    
    @Inject(method = "isExposedToSun", at = @At("HEAD"), cancellable = true)
    private static void oriacs$checkSunResistanceAndUmbrella(Entity entity, CallbackInfoReturnable<Boolean> cir) {
        if(!(entity instanceof LivingEntity living)) return;
        if(living.hasEffect(OriacsMobEffects.SUNLIGHT_RESISTANCE.get()) ||
           UmbrellaItem.canKeepOutSunlight(living)
        ) {
            cir.setReturnValue(false);
        }
    }
    
}
