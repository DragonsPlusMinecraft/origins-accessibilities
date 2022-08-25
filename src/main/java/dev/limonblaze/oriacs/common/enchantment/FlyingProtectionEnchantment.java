package dev.limonblaze.oriacs.common.enchantment;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.ProtectionEnchantment;

public class FlyingProtectionEnchantment extends ProtectionEnchantment {
    
    public FlyingProtectionEnchantment() {
        super(Rarity.RARE, Type.ALL, EquipmentSlot.HEAD);
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
