package dev.limonblaze.oriacs.client;

import dev.limonblaze.oriacs.common.registry.OriacsItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.item.IDyeableArmorItem;
import net.minecraft.world.biome.BiomeColors;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

public class OriacsClient {
    
    public OriacsClient() {
        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
        modBus.addListener(this::registerItemColors);
    }
    
    public void registerItemColors(ColorHandlerEvent.Item event) {
        ItemColors colors = event.getItemColors();
        colors.register((stack, index) ->
            index > 0 ? -1 : ((IDyeableArmorItem)stack.getItem()).getColor(stack),
            OriacsItems.UMBRELLA.get()
        );
        colors.register((stack, index) -> {
                if(index > 0) return -1;
                ClientWorld level = Minecraft.getInstance().level;
                ClientPlayerEntity player = Minecraft.getInstance().player;
                if(level == null || player == null) return 0x3F76E4;
                return BiomeColors.getAverageWaterColor(level, player.blockPosition());
            },
            OriacsItems.LANDWALKING_HELMET.get()
        );
    }
    
}
