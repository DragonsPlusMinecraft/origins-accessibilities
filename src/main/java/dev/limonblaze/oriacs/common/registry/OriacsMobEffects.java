package dev.limonblaze.oriacs.common.registry;

import dev.limonblaze.oriacs.common.Oriacs;
import dev.limonblaze.oriacs.common.effect.SimpleMobEffect;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class OriacsMobEffects {
    
    public static final DeferredRegister<MobEffect> REGISTRY = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, Oriacs.ID);
    
    public static final RegistryObject<SimpleMobEffect> WATER_RESISTANCE = REGISTRY.register("water_resistance",
        () -> new SimpleMobEffect(MobEffectCategory.BENEFICIAL, 0xAACCFF));
    
    public static final RegistryObject<SimpleMobEffect> SUNLIGHT_RESISTANCE = REGISTRY.register("sunlight_resistance",
        () -> new SimpleMobEffect(MobEffectCategory.BENEFICIAL, 0xAAFFCC));
    
    public static final RegistryObject<SimpleMobEffect> FRESH_AIR = REGISTRY.register("fresh_air",
        () -> new SimpleMobEffect(MobEffectCategory.BENEFICIAL, 0xFFFFFF));
    
}
