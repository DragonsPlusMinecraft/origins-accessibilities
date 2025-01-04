package dev.limonblaze.oriacs.data;

import dev.limonblaze.oriacs.common.Oriacs;
import dev.limonblaze.oriacs.common.registry.OriacsItemTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

import java.util.concurrent.CompletableFuture;

public class OriacsItemTagProvider extends ItemTagsProvider {

    public OriacsItemTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, BlockTagsProvider blockTags, ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, blockTags.contentsGetter(), Oriacs.ID, existingFileHelper);
    }


    @Override
    protected void addTags(HolderLookup.Provider provider) {
        tag(OriacsItemTags.VEGETARIAN_DIET).add(
                Items.APPLE,
                Items.BAKED_POTATO,
                Items.BEETROOT,
                Items.BEETROOT_SOUP,
                Items.BREAD,
                Items.CAKE,
                Items.CARROT,
                Items.CHORUS_FRUIT,
                Items.COOKIE,
                Items.DRIED_KELP,
                Items.ENCHANTED_GOLDEN_APPLE,
                Items.GOLDEN_APPLE,
                Items.GOLDEN_CARROT,
                Items.MELON_SLICE,
                Items.MUSHROOM_STEW,
                Items.POISONOUS_POTATO,
                Items.POTATO,
                Items.PUMPKIN_PIE,
                Items.SUSPICIOUS_STEW,
                Items.SWEET_BERRIES,
                Items.GLOW_BERRIES
        );
    }
}
