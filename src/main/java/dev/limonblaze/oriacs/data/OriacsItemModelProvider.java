package dev.limonblaze.oriacs.data;

import dev.limonblaze.oriacs.common.Oriacs;
import dev.limonblaze.oriacs.common.registry.OriacsItems;
import net.minecraft.data.DataGenerator;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemNameBlockItem;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Set;
import java.util.stream.Collectors;

public class OriacsItemModelProvider extends ItemModelProvider {
    
    public OriacsItemModelProvider(DataGenerator generator, ExistingFileHelper existingFileHelper) {
        super(generator, Oriacs.ID, existingFileHelper);
    }
    
    @Override
    protected void registerModels() {
        Set<Item> specialItems = Set.of(OriacsItems.UMBRELLA.get(), OriacsItems.LANDWALKING_HELMET.get());
        Set<Item> basicItems = ForgeRegistries.ITEMS.getValues()
            .stream()
            .filter(item -> item.getRegistryName().getNamespace().equals(Oriacs.ID))
            .filter(item -> !(item instanceof BlockItem) || item instanceof ItemNameBlockItem)
            .collect(Collectors.toSet());
        basicItems.removeAll(specialItems);
        basicItems.forEach(this::basicItem);
    }
    
}
