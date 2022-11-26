package dev.limonblaze.oriacs.core.mixin;

import dev.limonblaze.oriacs.common.item.UmbrellaItem;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public class EntityMixin {
    
    @Inject(method = "isInRain", at = @At("HEAD"), cancellable = true)
    private void oriacs$checkUmbrella(CallbackInfoReturnable<Boolean> cir) {
        if((Object)this instanceof LivingEntity && UmbrellaItem.canKeepOutRain((LivingEntity)(Object)this)) {
            cir.setReturnValue(false);
        }
    }
    
}