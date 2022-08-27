package dev.limonblaze.oriacs.common.item;

import dev.limonblaze.oriacs.common.Oriacs;
import dev.limonblaze.oriacs.common.OriacsServerConfig;
import dev.limonblaze.oriacs.common.registry.OriacsItems;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.cauldron.CauldronInteraction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.item.DyeableLeatherItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Vanishable;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Random;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
@Mod.EventBusSubscriber(modid = Oriacs.ID)
public class UmbrellaItem extends Item implements DyeableLeatherItem, Vanishable {
    public static final int BAR_COLOR = Mth.color(0.4F, 0.4F, 1.0F);
    
    public UmbrellaItem(Properties properties) {
        super(properties.stacksTo(1).setNoRepair());
        CauldronInteraction.WATER.put(this, CauldronInteraction.DYED_ITEM);
    }
    
    public boolean canKeepOutRain(ItemStack stack) {
        return OriacsServerConfig.CONFIG.UMBRELLA_CAN_KEEP_OUT_RAIN.get() && stack.getDamageValue() < stack.getMaxDamage();
    }
    
    public static boolean canKeepOutRain(LivingEntity entity) {
        ItemStack stack = entity.getMainHandItem();
        if(stack.getItem() instanceof UmbrellaItem umbrella && umbrella.canKeepOutRain(stack)) return true;
        stack = entity.getOffhandItem();
        return stack.getItem() instanceof UmbrellaItem umbrella && umbrella.canKeepOutRain(stack);
    }
    
    public boolean canKeepOutSunlight(ItemStack stack) {
        if(!OriacsServerConfig.CONFIG.UMBRELLA_CAN_KEEP_OUT_SUNLIGHT.get()) return false;
        int color = this.getColor(stack);
        int max = OriacsServerConfig.CONFIG.UMBRELLA_MAX_KEEP_OUT_SUNLIGHT_COLOR.get();
        return (color >> 16 & 255) <= max && (color >> 8 & 255) <= max && (color & 255) <= max;
    }
    
    public static boolean canKeepOutSunlight(LivingEntity entity) {
        ItemStack stack = entity.getMainHandItem();
        if(stack.getItem() instanceof UmbrellaItem umbrella && umbrella.canKeepOutSunlight(stack)) return true;
        stack = entity.getOffhandItem();
        return stack.getItem() instanceof UmbrellaItem umbrella && umbrella.canKeepOutSunlight(stack);
    }
    
    /**
     * Umbrella is made of Phantom Membrane instead of Leather, so the color must be changed
     */
    @Override
    public int getColor(ItemStack pStack) {
        CompoundTag compoundtag = pStack.getTagElement("display");
        return compoundtag != null && compoundtag.contains("color", 99) ? compoundtag.getInt("color") : 0xDCD9C0;
    }
    
    @Override
    public int getBarColor(ItemStack pStack) {
        return BAR_COLOR;
    }
    
    @Override
    public boolean canBeDepleted() {
        return false;
    }
    
    @Override
    public int getMaxDamage(ItemStack stack) {
        return OriacsServerConfig.CONFIG.UMBRELLA_MAX_DAMAGE.get();
    }
    
    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return ItemStack.isSameIgnoreDurability(oldStack, newStack);
    }
    
    @SubscribeEvent
    public static void onLivingUpdate(LivingEvent.LivingTickEvent event) {
        LivingEntity entity = event.getEntity();
        if(entity.tickCount % 20 != 0) return;
        Level level = entity.level;
        BlockPos pos = entity.blockPosition();
        boolean isInRain = level.isRainingAt(pos) || level.isRainingAt(new BlockPos(pos.getX(), entity.getBoundingBox().maxY, pos.getZ()));
        for(ItemStack stack : entity.getHandSlots()) {
            if(!(stack.getItem() instanceof UmbrellaItem)) return;
            if(isInRain) {
                stack.setDamageValue(Mth.clamp(stack.getDamageValue() + 1, 0, stack.getMaxDamage()));
            } else if(level.getBiome(pos).value().shouldSnowGolemBurn(pos)) {
                stack.setDamageValue(Mth.clamp(stack.getDamageValue() - 2, 0, stack.getMaxDamage()));
            } else {
                stack.setDamageValue(Mth.clamp(stack.getDamageValue() - 1, 0, stack.getMaxDamage()));
            }
        }
    }
    
    @SubscribeEvent
    public static void onLivingSpawn(LivingSpawnEvent.SpecialSpawn event) {
        if(event.getEntity() instanceof Zombie zombie) {
            RandomSource random = zombie.getRandom();
            if(zombie.level.getDifficulty() == Difficulty.HARD &&
               random.nextFloat() < OriacsServerConfig.CONFIG.UMBRELLA_SPAWN_WITH_ZOMBIE_CHANCE.get() &&
               zombie.getItemInHand(InteractionHand.OFF_HAND).isEmpty()
            ) {
                UmbrellaItem umbrella = OriacsItems.UMBRELLA.get();
                ItemStack stack = umbrella.getDefaultInstance();
                umbrella.setColor(stack, random.nextInt(0xFFFFFF));
                zombie.setItemInHand(InteractionHand.OFF_HAND, stack);
            }
        }
    }
    
}
