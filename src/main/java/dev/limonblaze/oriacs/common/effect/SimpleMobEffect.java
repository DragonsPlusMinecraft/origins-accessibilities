package dev.limonblaze.oriacs.common.effect;

import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.AttributeModifierManager;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class SimpleMobEffect extends Effect {
    
    public SimpleMobEffect(EffectType category, int color) {
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
    public void addAttributeModifiers(LivingEntity pLivingEntity, AttributeModifierManager pAttributeMap, int pAmplifier) {}
    
    @Override
    public void removeAttributeModifiers(LivingEntity pLivingEntity, AttributeModifierManager pAttributeMap, int pAmplifier) {}
    
}
