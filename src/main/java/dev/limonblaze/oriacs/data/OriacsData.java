package dev.limonblaze.oriacs.data;

import dev.limonblaze.oriacs.common.Oriacs;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@Mod.EventBusSubscriber(modid = Oriacs.ID, bus = Bus.MOD)
public class OriacsData {
    
    @SubscribeEvent
    public static void runData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        ExistingFileHelper helper = event.getExistingFileHelper();
        boolean assets = event.includeClient();
        boolean data = event.includeServer();
        generator.addProvider(assets, new OriacsItemModelProvider(generator, helper));
        OriacsBlockTagProvider blockTags = new OriacsBlockTagProvider(generator, helper);
        generator.addProvider(data, blockTags);
        generator.addProvider(data, new OriacsItemTagProvider(generator, blockTags, helper));
        generator.addProvider(data, new OriacsRecipeProvider(generator));
    }
    
}
