package dev.limonblaze.oriacs.common.registry;

import com.google.common.collect.Sets;
import io.github.apace100.origins.Origins;
import net.minecraft.item.Item;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.Tags;

import java.util.function.Supplier;

public class OriacsItemTags {
    
    public static final Tags.IOptionalNamedTag<Item> VEGETARIAN_DIET = register(Origins.identifier("vegetarian_diet"));
    public static final Tags.IOptionalNamedTag<Item> GOGGLES = register(new ResourceLocation("forge", "goggles"));
    public static final Tags.IOptionalNamedTag<Item> COPPER_INGOTS = register(new ResourceLocation("forge", "ingots/copper"));
    
    @SafeVarargs
    public static Tags.IOptionalNamedTag<Item> register(ResourceLocation name, Supplier<Item>... defaultValues) {
        return ItemTags.createOptional(name, Sets.newHashSet(defaultValues));
    }
    
}
