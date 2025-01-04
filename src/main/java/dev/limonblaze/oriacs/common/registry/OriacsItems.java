package dev.limonblaze.oriacs.common.registry;

import dev.limonblaze.oriacs.common.Oriacs;
import dev.limonblaze.oriacs.common.item.*;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class OriacsItems {
    
    public static final DeferredRegister<Item> REGISTRY = DeferredRegister.create(ForgeRegistries.ITEMS, Oriacs.ID);
    
    public static final RegistryObject<UmbrellaItem> UMBRELLA = REGISTRY.register("umbrella",
        () -> new UmbrellaItem(new Item.Properties()));
    
    public static final RegistryObject<DivingHelmetItem> DIVING_HELMET = REGISTRY.register("diving_helmet",
        () -> new DivingHelmetItem(new Item.Properties()));
    
    public static final RegistryObject<LandwalkingHelmetItem> LANDWALKING_HELMET = REGISTRY.register("landwalking_helmet",
        () -> new LandwalkingHelmetItem(new Item.Properties()));
    
    public static final RegistryObject<OriacsArmorItem> CHAINMEMBRANE_HELMET = REGISTRY.register("chainmembrane_helmet",
        () -> new OriacsArmorItem(
            OriacsArmorMaterials.CHAINMEMBRANE,
                ArmorItem.Type.HELMET,
            new Item.Properties()
        ));
    
    public static final RegistryObject<OriacsArmorItem> CHAINMEMBRANE_CHESTPLATE = REGISTRY.register("chainmembrane_chestplate",
        () -> new OriacsArmorItem(
            OriacsArmorMaterials.CHAINMEMBRANE,
                ArmorItem.Type.CHESTPLATE,
            new Item.Properties()
        ));
    
    public static final RegistryObject<OriacsArmorItem> CHAINMEMBRANE_LEGGINGS = REGISTRY.register("chainmembrane_leggings",
        () -> new OriacsArmorItem(
            OriacsArmorMaterials.CHAINMEMBRANE,
                ArmorItem.Type.LEGGINGS,
            new Item.Properties()
        ));
    
    public static final RegistryObject<OriacsArmorItem> CHAINMEMBRANE_BOOTS = REGISTRY.register("chainmembrane_boots",
        () -> new OriacsArmorItem(
            OriacsArmorMaterials.CHAINMEMBRANE,
            ArmorItem.Type.BOOTS,
            new Item.Properties()
        ));
    
}
