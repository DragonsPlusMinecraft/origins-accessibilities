package dev.limonblaze.oriacs.core.mixin;

import net.minecraft.item.Item;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionBrewing;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(PotionBrewing.class)
public interface PotionBrewingAccessor {
    
    @Invoker("addMix")
    static void tryAddMix(Potion input, Item ingredient, Potion result) {
        throw new AssertionError();
    }
    
}
