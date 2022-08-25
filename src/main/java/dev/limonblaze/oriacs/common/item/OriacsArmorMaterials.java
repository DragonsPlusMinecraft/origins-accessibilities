package dev.limonblaze.oriacs.common.item;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.util.Lazy;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Locale;
import java.util.function.Supplier;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public enum OriacsArmorMaterials implements ArmorMaterial {
    DIVING(() -> Ingredient.of(Items.COPPER_INGOT),
        9,
        new int[]{1, 4, 5, 2},
        1, 0, 9,
        SoundEvents.ARMOR_EQUIP_IRON),
    CHAINMEMBRANE(() -> Ingredient.of(Items.PHANTOM_MEMBRANE),
        25,
        new int[]{1, 4, 5, 2},
        4, 0, 15,
        SoundEvents.ARMOR_EQUIP_CHAIN);
    
    private final Lazy<Ingredient> repairIngredient;
    private final int[] durabilities;
    private final int[] armorValues;
    private final float toughness;
    private final float knockbackResistance;
    private final int enchantmentValue;
    private final SoundEvent equipSound;
    
    OriacsArmorMaterials(Supplier<Ingredient> repairIngredient,
                         int[] durabilities,
                         int[] armorValues,
                         float toughness,
                         float knockbackResistance,
                         int enchantmentValue,
                         SoundEvent equipSound) {
        this.repairIngredient = Lazy.of(repairIngredient);
        this.durabilities = durabilities;
        this.armorValues = armorValues;
        this.toughness = toughness;
        this.knockbackResistance = knockbackResistance;
        this.enchantmentValue = enchantmentValue;
        this.equipSound = equipSound;
    }
    
    OriacsArmorMaterials(Supplier<Ingredient> repairIngredient,
                         int durabilityMultiplier,
                         int[] armorValues,
                         float toughness,
                         float knockbackResistance,
                         int enchantmentValue,
                         SoundEvent equipSound) {
        this(repairIngredient,
            new int[]{
                13 * durabilityMultiplier,
                15 * durabilityMultiplier,
                16 * durabilityMultiplier,
                11 * durabilityMultiplier
            }, armorValues, toughness, knockbackResistance, enchantmentValue, equipSound);
    }
    
    @Override
    public int getDurabilityForSlot(EquipmentSlot slot) {
        return durabilities[slot.getIndex()];
    }
    
    @Override
    public int getDefenseForSlot(EquipmentSlot slot) {
        return armorValues[slot.getIndex()];
    }
    
    @Override
    public int getEnchantmentValue() {
        return enchantmentValue;
    }
    
    @Override
    public SoundEvent getEquipSound() {
        return equipSound;
    }
    
    @Override
    public Ingredient getRepairIngredient() {
        return repairIngredient.get();
    }
    
    @Override
    public String getName() {
        return this.name().toLowerCase(Locale.ROOT);
    }
    
    @Override
    public float getToughness() {
        return toughness;
    }
    
    @Override
    public float getKnockbackResistance() {
        return knockbackResistance;
    }
    
}
