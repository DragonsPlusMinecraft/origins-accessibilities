package dev.limonblaze.oriacs.common.enchantment;

import net.minecraft.enchantment.ProtectionEnchantment;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class FlyingProtectionEnchantment extends ProtectionEnchantment {
    
    public FlyingProtectionEnchantment() {
        super(Rarity.RARE, Type.ALL, EquipmentSlotType.HEAD);
    }
    
    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack) {
        Item item = stack.getItem();
        return item instanceof ArmorItem && item.getEquipmentSlot(stack) == EquipmentSlotType.HEAD;
    }
    
    public boolean isTreasureOnly() {
        return true;
    }
    
    @Override
    public int getDamageProtection(int level, DamageSource source) {
        if(!source.isBypassInvul() && source.getMsgId().contains("flyIntoWall")) {
            return level * 3;
        }
        return 0;
    }
    
}
