package dev.limonblaze.oriacs.data;

import dev.limonblaze.oriacs.common.Oriacs;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

import java.util.concurrent.CompletableFuture;

@Mod.EventBusSubscriber(modid = Oriacs.ID, bus = Bus.MOD)
public class OriacsData {

    @SubscribeEvent
    public static void runData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput packOutput = generator.getPackOutput();
        ExistingFileHelper helper = event.getExistingFileHelper();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();
        boolean assets = event.includeClient();
        boolean data = event.includeServer();

        generator.addProvider(assets, new OriacsItemModelProvider(packOutput, helper));
        OriacsBlockTagProvider blockTags = new OriacsBlockTagProvider(packOutput, lookupProvider, helper);
        generator.addProvider(data, blockTags);
        generator.addProvider(data, new OriacsItemTagProvider(packOutput, lookupProvider, blockTags, helper));
        generator.addProvider(data, new OriacsRecipeProvider(packOutput));
    }
    
}
