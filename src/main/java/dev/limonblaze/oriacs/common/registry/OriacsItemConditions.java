package dev.limonblaze.oriacs.common.registry;

import dev.limonblaze.oriacs.common.Oriacs;
import dev.limonblaze.oriacs.common.OriacsServerConfig;
import io.github.edwinmindcraft.apoli.api.power.factory.ItemCondition;
import io.github.edwinmindcraft.apoli.api.registry.ApoliRegistries;
import io.github.edwinmindcraft.apoli.common.condition.item.SimpleItemCondition;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class OriacsItemConditions {
    
    public static final DeferredRegister<ItemCondition<?>> REGISTRY = DeferredRegister.create(ApoliRegistries.ITEM_CONDITION_KEY, Oriacs.ID);
    
    public static RegistryObject<SimpleItemCondition> STRICT_DIET = REGISTRY.register("strict_diet",
        () -> new SimpleItemCondition(stack -> OriacsServerConfig.CONFIG.STRICT_DIET.get()));
    
}
