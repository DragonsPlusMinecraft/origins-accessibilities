package dev.limonblaze.oriacs.core.mixin;

import com.simibubi.create.content.contraptions.components.fan.IAirCurrentSource;
import com.simibubi.create.content.contraptions.processing.InWorldProcessing;
import com.simibubi.create.foundation.utility.VecHelper;
import dev.limonblaze.oriacs.common.registry.OriacsMobEffects;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Pseudo
@Mixin(targets = "com.simibubi.create.content.contraptions.components.fan.AirCurrent", remap = false)
public abstract class AirCurrentMixin {
    
    @Shadow public abstract InWorldProcessing.Type getSegmentAt(float offset);
    
    @Shadow @Final public IAirCurrentSource source;
    
    @ModifyVariable(
        method = "tickAffectedEntities",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;setDeltaMovement(Lnet/minecraft/world/phys/Vec3;)V", remap = true),
        name = "entity"
    )
    private Entity oriacs$applyFreshAirEffect(Entity entity) {
        if(entity.tickCount % 20 == 0 && entity instanceof LivingEntity living) {
            InWorldProcessing.Type type = getSegmentAt((float) entity.position().distanceTo(VecHelper.getCenterOf(this.source.getAirCurrentPos())) - 0.5F);
            if(type == null || type == InWorldProcessing.Type.NONE) {
                living.addEffect(new MobEffectInstance(OriacsMobEffects.FRESH_AIR.get(), 30, 0, true, false, true));
            }
        }
        return entity;
    }
    
}
