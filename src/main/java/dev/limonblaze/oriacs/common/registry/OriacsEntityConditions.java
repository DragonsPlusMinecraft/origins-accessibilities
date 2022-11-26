package dev.limonblaze.oriacs.common.registry;

import dev.limonblaze.oriacs.common.Oriacs;
import io.github.apace100.origins.power.factory.condition.ConditionFactory;
import io.github.apace100.origins.registry.ModRegistriesArchitectury;
import io.github.apace100.origins.util.SerializableData;
import io.github.apace100.origins.util.SerializableDataType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.LazyOptional;
import top.theillusivec4.curios.api.CuriosCapability;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;
import top.theillusivec4.curios.api.type.inventory.ICurioStacksHandler;

import java.util.function.Predicate;

public class OriacsEntityConditions {
    
    @SuppressWarnings({"deprecation", "UnstableApiUsage"})
    public static void registerAll() {
        register(new ConditionFactory<>(Oriacs.asResource("equipped_curio"), new SerializableData()
            .add("curio_slot", SerializableDataType.STRING)
            .add("item_condition", SerializableDataType.ITEM_CONDITION),
            (data, entity) -> {
                if(Oriacs.CURIOS_LOADED) {
                    LazyOptional<ICuriosItemHandler> optional = entity.getCapability(CuriosCapability.INVENTORY);
                    if(optional.isPresent()) {
                        ICuriosItemHandler curiosItemHandler = optional.orElseThrow(AssertionError::new);
                        ICurioStacksHandler curioStacksHandler = curiosItemHandler.getCurios().get(data.get("curio_slot"));
                        if(curioStacksHandler != null) {
                            Predicate<ItemStack> condition = data.get("item_condition");
                            for(int slot = 0; slot < curioStacksHandler.getSlots(); ++slot) {
                                if(condition.test(curioStacksHandler.getStacks().getStackInSlot(slot)))
                                    return true;
                            }
                        }
                    }
                }
                return false;
            }
        ));
    }
    
    private static void register(ConditionFactory<LivingEntity> conditionFactory) {
        ModRegistriesArchitectury.ENTITY_CONDITION.registerSupplied(conditionFactory.getSerializerId(), () -> conditionFactory);
    }
    
}
