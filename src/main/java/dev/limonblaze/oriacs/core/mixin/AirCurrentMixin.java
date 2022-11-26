package dev.limonblaze.oriacs.core.mixin;

import com.simibubi.create.content.contraptions.components.fan.AirCurrent;
import com.simibubi.create.content.contraptions.components.fan.IAirCurrentSource;
import com.simibubi.create.content.contraptions.processing.InWorldProcessing;
import com.simibubi.create.foundation.utility.VecHelper;
import dev.limonblaze.oriacs.common.registry.OriacsMobEffects;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.EffectInstance;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(value = AirCurrent.class, remap = false)
public abstract class AirCurrentMixin {
    
    @Shadow public abstract InWorldProcessing.Type getSegmentAt(float offset);
    
    @Shadow @Final public IAirCurrentSource source;
    
    @ModifyVariable(
        method = "tickAffectedEntities",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;setDeltaMovement(Lnet/minecraft/util/math/vector/Vector3d;)V", remap = true),
        name = "entity"
    )
    private Entity oriacs$applyFreshAirEffect(Entity entity) {
        if(entity.tickCount % 20 == 0 && entity instanceof LivingEntity) {
            LivingEntity living = (LivingEntity)entity;
            InWorldProcessing.Type type = getSegmentAt((float) entity.position().distanceTo(VecHelper.getCenterOf(this.source.getAirCurrentPos())) - 0.5F);
            if(type == null || type == InWorldProcessing.Type.NONE) {
                living.addEffect(new EffectInstance(OriacsMobEffects.FRESH_AIR.get(), 30, 0, true, false, true));
            }
        }
        return entity;
    }
    
}
