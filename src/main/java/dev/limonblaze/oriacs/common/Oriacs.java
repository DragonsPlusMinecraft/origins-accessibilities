package dev.limonblaze.oriacs.common;

import dev.limonblaze.oriacs.client.OriacsClient;
import dev.limonblaze.oriacs.common.registry.*;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(Oriacs.ID)
public class Oriacs {
    public static final String ID = "oriacs";
    public static final Logger LOGGER = LogManager.getLogger();
    public static boolean CURIOS_LOADED = false;
    
    public Oriacs() {
        ModLoadingContext modLoadingContext = ModLoadingContext.get();
        modLoadingContext.registerConfig(ModConfig.Type.SERVER, OriacsServerConfig.SPEC);
        CURIOS_LOADED = ModList.get().isLoaded("curios");
        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
        OriacsItems.REGISTRY.register(modBus);
        OriacsMobEffects.REGISTRY.register(modBus);
        OriacsPotions.REGISTRY.register(modBus);
        OriacsEnchantments.REGISTRY.register(modBus);
        OriacsItemConditions.registerAll();
        OriacsEntityConditions.registerAll();
        DistExecutor.safeCallWhenOn(Dist.CLIENT, () -> OriacsClient::new);
        LOGGER.info("Origins: Accessibilities " + ModLoadingContext.get().getActiveContainer().getModInfo().getVersion() + " has initialized. Ready to make your Origins life easier!");
    }
    
    public static ResourceLocation asResource(String path) {
        return new ResourceLocation(ID, path);
    }

}
