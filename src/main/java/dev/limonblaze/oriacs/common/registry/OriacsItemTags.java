package dev.limonblaze.oriacs.common.registry;

import io.github.apace100.origins.Origins;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.tags.ITagManager;

import java.util.Set;
import java.util.function.Supplier;

public class OriacsItemTags {
    public static final ITagManager<Item> REGISTRY = ForgeRegistries.ITEMS.tags();
    
    public static final TagKey<Item> VEGETARIAN_DIET = register(Origins.identifier("vegetarian_diet"));
    public static final TagKey<Item> GOGGLES = register(new ResourceLocation("forge", "goggles"));
    
    @SafeVarargs
    public static TagKey<Item> register(ResourceLocation name, Supplier<Item>... defaultValues) {
        assert REGISTRY != null;
        return REGISTRY.createOptionalTagKey(name, Set.of(defaultValues));
    }
    
}
