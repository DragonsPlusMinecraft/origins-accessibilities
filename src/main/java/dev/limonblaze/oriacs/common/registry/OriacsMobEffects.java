package dev.limonblaze.oriacs.common.registry;

import dev.limonblaze.oriacs.common.Oriacs;
import dev.limonblaze.oriacs.common.effect.SimpleMobEffect;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class OriacsMobEffects {
    
    public static final DeferredRegister<Effect> REGISTRY = DeferredRegister.create(ForgeRegistries.POTIONS, Oriacs.ID);
    
    public static final RegistryObject<SimpleMobEffect> WATER_RESISTANCE = REGISTRY.register("water_resistance",
        () -> new SimpleMobEffect(EffectType.BENEFICIAL, 0xAACCFF));
    
    public static final RegistryObject<SimpleMobEffect> SUNLIGHT_RESISTANCE = REGISTRY.register("sunlight_resistance",
        () -> new SimpleMobEffect(EffectType.BENEFICIAL, 0xFFCCAA));
    
    public static final RegistryObject<SimpleMobEffect> FRESH_AIR = REGISTRY.register("fresh_air",
        () -> new SimpleMobEffect(EffectType.BENEFICIAL, 0xFFFFFF));
    
}
