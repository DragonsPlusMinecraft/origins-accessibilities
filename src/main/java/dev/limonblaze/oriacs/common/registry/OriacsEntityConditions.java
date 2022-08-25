package dev.limonblaze.oriacs.common.registry;

import dev.limonblaze.oriacs.common.Oriacs;
import dev.limonblaze.oriacs.common.apoli.condition.EquippedCurioCondition;
import io.github.edwinmindcraft.apoli.api.power.factory.EntityCondition;
import io.github.edwinmindcraft.apoli.api.registry.ApoliRegistries;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class OriacsEntityConditions {
    
    public static final DeferredRegister<EntityCondition<?>> REGISTRY = DeferredRegister.create(ApoliRegistries.ENTITY_CONDITION_KEY, Oriacs.ID);
    
    public static final RegistryObject<EquippedCurioCondition> EQUIPPED_CURIO = REGISTRY.register("equipped_curio",
        EquippedCurioCondition::new);
    
}
