package dev.limonblaze.oriacs.common.effect;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class SimpleMobEffect extends MobEffect {
    
    public SimpleMobEffect(MobEffectCategory category, int color) {
        super(category, color);
    }
    
    @Override
    public void applyEffectTick(LivingEntity pLivingEntity, int pAmplifier) {}
    
    @Override
    public void applyInstantenousEffect(@Nullable Entity pSource, @Nullable Entity pIndirectSource, LivingEntity pLivingEntity, int pAmplifier, double pHealth) {}
    
    @Override
    public boolean isDurationEffectTick(int pDuration, int pAmplifier) {
        return false;
    }
    
    @Override
    public void addAttributeModifiers(LivingEntity pLivingEntity, AttributeMap pAttributeMap, int pAmplifier) {}
    
    @Override
    public void removeAttributeModifiers(LivingEntity pLivingEntity, AttributeMap pAttributeMap, int pAmplifier) {}
    
}
