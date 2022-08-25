package dev.limonblaze.oriacs.common.registry;

import dev.limonblaze.oriacs.common.Oriacs;
import dev.limonblaze.oriacs.core.mixin.PotionBrewingAccessor;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@Mod.EventBusSubscriber(modid = Oriacs.ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class OriacsPotions {
    
    public static final DeferredRegister<Potion> REGISTRY = DeferredRegister.create(ForgeRegistries.POTIONS, Oriacs.ID);
    
    public static final RegistryObject<Potion> WATER_RESISTANCE = REGISTRY.register("water_resistance",
        () -> new Potion("water_resistance", new MobEffectInstance(OriacsMobEffects.WATER_RESISTANCE.get(), 3600)));
    
    public static final RegistryObject<Potion> LONG_WATER_RESISTANCE = REGISTRY.register("long_water_resistance",
        () -> new Potion("water_resistance", new MobEffectInstance(OriacsMobEffects.WATER_RESISTANCE.get(), 9600)));
    
    public static final RegistryObject<Potion> SUNLIGHT_RESISTANCE = REGISTRY.register("sunscreen",
        () -> new Potion("sunscreen", new MobEffectInstance(OriacsMobEffects.SUNLIGHT_RESISTANCE.get(), 3600)));
    
    public static final RegistryObject<Potion> LONG_SUNLIGHT_RESISTANCE = REGISTRY.register("long_sunscreen",
        () -> new Potion("sunscreen", new MobEffectInstance(OriacsMobEffects.SUNLIGHT_RESISTANCE.get(), 9600)));
    
    @SubscribeEvent
    public static void registerBrewingRecipes(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            PotionBrewingAccessor.tryAddMix(Potions.WATER_BREATHING, Items.SCUTE, WATER_RESISTANCE.get());
            PotionBrewingAccessor.tryAddMix(WATER_RESISTANCE.get(), Items.REDSTONE, LONG_WATER_RESISTANCE.get());
            PotionBrewingAccessor.tryAddMix(Potions.NIGHT_VISION, Items.GLOW_INK_SAC, SUNLIGHT_RESISTANCE.get());
            PotionBrewingAccessor.tryAddMix(SUNLIGHT_RESISTANCE.get(), Items.REDSTONE, LONG_SUNLIGHT_RESISTANCE.get());
        });
    }
    
}
