package dev.limonblaze.oriacs.data;

import dev.limonblaze.oriacs.common.Oriacs;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.forge.event.lifecycle.GatherDataEvent;

@Mod.EventBusSubscriber(modid = Oriacs.ID, bus = Bus.MOD)
public class OriacsData {
    
    @SubscribeEvent
    public static void runData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        ExistingFileHelper helper = event.getExistingFileHelper();
        if(event.includeClient()) {
            generator.addProvider(new OriacsItemModelProvider(generator, helper));
        }
        if(event.includeServer()) {
            OriacsBlockTagProvider blockTags = new OriacsBlockTagProvider(generator, helper);
            generator.addProvider(blockTags);
            generator.addProvider(new OriacsItemTagProvider(generator, blockTags, helper));
            generator.addProvider(new OriacsRecipeProvider(generator));
        }
    }
    
}
