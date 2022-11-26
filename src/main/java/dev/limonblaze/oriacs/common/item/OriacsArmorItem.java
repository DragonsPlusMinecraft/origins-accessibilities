package dev.limonblaze.oriacs.common.item;

import dev.limonblaze.oriacs.common.Oriacs;
import net.minecraft.entity.Entity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.IArmorMaterial;
import net.minecraft.item.ItemStack;

import javax.annotation.Nullable;
import java.util.Locale;

public class OriacsArmorItem extends ArmorItem {
    public static final String TRANSFORM_PROGRESS = "TransformProgress";
    
    public OriacsArmorItem(IArmorMaterial material, EquipmentSlotType slot, Properties properties) {
        super(material, slot, properties);
    }
    
    @Nullable
    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlotType slot, String type) {
        return String.format(Locale.ROOT, "%s:textures/models/armor/%s_layer_%d.png",
            Oriacs.ID, material.getName(), slot == EquipmentSlotType.LEGS ? 2 : 1);
    }
    
}
