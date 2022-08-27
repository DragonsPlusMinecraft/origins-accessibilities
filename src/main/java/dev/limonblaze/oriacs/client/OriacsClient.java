package dev.limonblaze.oriacs.client;

import dev.limonblaze.oriacs.common.registry.OriacsItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.color.item.ItemColors;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.world.item.DyeableLeatherItem;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

public class OriacsClient {
    
    public OriacsClient() {
        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
        modBus.addListener(this::registerItemColors);
    }
    
    public void registerItemColors(RegisterColorHandlersEvent.Item event) {
        event.register((stack, index) ->
            index > 0 ? -1 : ((DyeableLeatherItem)stack.getItem()).getColor(stack),
            OriacsItems.UMBRELLA.get()
        );
        event.register((stack, index) -> {
                if(index > 0) return -1;
                ClientLevel level = Minecraft.getInstance().level;
                LocalPlayer player = Minecraft.getInstance().player;
                if(level == null || player == null) return 0x3F76E4;
                return BiomeColors.getAverageWaterColor(level, player.blockPosition());
            },
            OriacsItems.LANDWALKING_HELMET.get()
        );
    }
    
}
