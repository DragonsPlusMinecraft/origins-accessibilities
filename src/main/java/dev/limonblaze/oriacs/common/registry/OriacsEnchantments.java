package dev.limonblaze.oriacs.common.registry;

import dev.limonblaze.oriacs.common.Oriacs;
import dev.limonblaze.oriacs.common.enchantment.FlyingProtectionEnchantment;
import net.minecraft.enchantment.Enchantment;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class OriacsEnchantments {
    
    public static final DeferredRegister<Enchantment> REGISTRY = DeferredRegister.create(ForgeRegistries.ENCHANTMENTS, Oriacs.ID);
    
    public static final RegistryObject<FlyingProtectionEnchantment> FLYING_PROTECTION = REGISTRY.register("flying_protection",
        FlyingProtectionEnchantment::new);
    
}
