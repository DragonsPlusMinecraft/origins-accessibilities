package dev.limonblaze.oriacs.common.registry;

import dev.limonblaze.oriacs.common.Oriacs;
import dev.limonblaze.oriacs.common.OriacsServerConfig;
import io.github.apace100.origins.power.factory.condition.ConditionFactory;
import io.github.apace100.origins.registry.ModRegistriesArchitectury;
import io.github.apace100.origins.util.SerializableData;
import net.minecraft.item.ItemStack;

public class OriacsItemConditions {
    
    public static void registerAll() {
        register(new ConditionFactory<>(Oriacs.asResource("strict_diet"), new SerializableData(),
            (data, stack) -> OriacsServerConfig.CONFIG.STRICT_DIET.get()));
    }
    
    private static void register(ConditionFactory<ItemStack> conditionFactory) {
        ModRegistriesArchitectury.ITEM_CONDITION.registerSupplied(conditionFactory.getSerializerId(), () -> conditionFactory);
    }
    
}
