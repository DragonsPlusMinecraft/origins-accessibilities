package dev.limonblaze.oriacs.common.apoli.condition;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.limonblaze.oriacs.common.Oriacs;
import io.github.edwinmindcraft.apoli.api.IDynamicFeatureConfiguration;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredItemCondition;
import io.github.edwinmindcraft.apoli.api.power.factory.EntityCondition;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.Entity;
import top.theillusivec4.curios.api.CuriosCapability;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;
import top.theillusivec4.curios.api.type.inventory.ICurioStacksHandler;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class EquippedCurioCondition extends EntityCondition<EquippedCurioCondition.Configuration> {
    public static final Codec<Configuration> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Codec.STRING.fieldOf("curio_slot")
            .forGetter(Configuration::slot),
        ConfiguredItemCondition.required("item_condition")
            .forGetter(Configuration::condition)
    ).apply(instance, Configuration::new));
    
    public EquippedCurioCondition() {
        super(CODEC);
    }
    
    @Override
    protected boolean check(Configuration configuration, Entity entity) {
        if(Oriacs.CURIOS_LOADED) {
            var optional = entity.getCapability(CuriosCapability.INVENTORY);
            if(optional.isPresent()) {
                ICuriosItemHandler curiosItemHandler = optional.orElse(null);
                assert curiosItemHandler != null;
                ICurioStacksHandler curioStacksHandler = curiosItemHandler.getCurios().get(configuration.slot);
                if(curioStacksHandler != null) {
                    for(int slot = 0; slot < curioStacksHandler.getSlots(); ++slot) {
                        if(ConfiguredItemCondition.check(
                            configuration.condition,
                            entity.level,
                            curioStacksHandler.getStacks().getStackInSlot(slot))
                        ) return true;
                    }
                }
            }
        }
        return false;
    }
    
    public record Configuration(String slot, Holder<ConfiguredItemCondition<?, ?>> condition) implements IDynamicFeatureConfiguration {}
    
}
