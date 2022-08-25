package dev.limonblaze.oriacs.common.item;

import dev.limonblaze.oriacs.common.Oriacs;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.Locale;

public class OriacsArmorItem extends ArmorItem {
    public static final String TRANSFORM_PROGRESS = "TransformProgress";
    
    public OriacsArmorItem(ArmorMaterial material, EquipmentSlot slot, Properties properties) {
        super(material, slot, properties);
    }
    
    @Nullable
    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type) {
        return String.format(Locale.ROOT, "%s:textures/models/armor/%s_layer_%d.png",
            Oriacs.ID, material.getName(), slot == EquipmentSlot.LEGS ? 2 : 1);
    }
    
}
