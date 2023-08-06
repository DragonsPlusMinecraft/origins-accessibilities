package dev.limonblaze.oriacs.common.item;

import dev.limonblaze.oriacs.common.Oriacs;
import dev.limonblaze.oriacs.common.OriacsServerConfig;
import dev.limonblaze.oriacs.common.registry.OriacsItems;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.enchantment.IVanishable;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.monster.ZombieEntity;
import net.minecraft.item.IDyeableArmorItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.Difficulty;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Random;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
@Mod.EventBusSubscriber(modid = Oriacs.ID)
public class UmbrellaItem extends Item implements IDyeableArmorItem, IVanishable {
    public static final int BAR_COLOR = color(0.4F, 0.4F, 1.0F);
    
    public UmbrellaItem(Properties properties) {
        super(properties.stacksTo(1).setNoRepair());
    }
    
    public boolean canKeepOutRain(ItemStack stack) {
        return OriacsServerConfig.CONFIG.UMBRELLA_CAN_KEEP_OUT_RAIN.get() && stack.getDamageValue() < stack.getMaxDamage();
    }
    
    public static boolean canKeepOutRain(LivingEntity entity) {
        ItemStack stack = entity.getMainHandItem();
        Item item = stack.getItem();
        if(item instanceof UmbrellaItem && ((UmbrellaItem)item).canKeepOutRain(stack))
            return true;
        stack = entity.getOffhandItem();
        item = stack.getItem();
        return item instanceof UmbrellaItem && ((UmbrellaItem)item).canKeepOutRain(stack);
    }
    
    public boolean canKeepOutSunlight(ItemStack stack) {
        if(!OriacsServerConfig.CONFIG.UMBRELLA_CAN_KEEP_OUT_SUNLIGHT.get()) return false;
        int color = this.getColor(stack);
        int max = OriacsServerConfig.CONFIG.UMBRELLA_MAX_KEEP_OUT_SUNLIGHT_COLOR.get();
        return (color >> 16 & 255) <= max && (color >> 8 & 255) <= max && (color & 255) <= max;
    }
    
    public static boolean canKeepOutSunlight(LivingEntity entity) {
        ItemStack stack = entity.getMainHandItem();
        Item item = stack.getItem();
        if(item instanceof UmbrellaItem && ((UmbrellaItem)item).canKeepOutSunlight(stack)) return true;
        stack = entity.getOffhandItem();
        item = stack.getItem();
        return item instanceof UmbrellaItem && ((UmbrellaItem)item).canKeepOutSunlight(stack);
    }
    
    /**
     * Umbrella is made of Phantom Membrane instead of Leather, so the color must be changed
     */
    @Override
    public int getColor(ItemStack pStack) {
        CompoundNBT compoundtag = pStack.getTagElement("display");
        return compoundtag != null && compoundtag.contains("color", 99) ? compoundtag.getInt("color") : 0xDCD9C0;
    }
    
    @Override
    public int getRGBDurabilityForDisplay(ItemStack pStack) {
        return BAR_COLOR;
    }
    
    @Override
    public boolean showDurabilityBar(ItemStack stack) {
        return stack.getDamageValue() > 0;
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
    public static void onLivingUpdate(LivingEvent.LivingUpdateEvent event) {
        LivingEntity entity = event.getEntityLiving();
        if(entity.tickCount % 20 != 0) return;
        World level = entity.level;
        BlockPos pos = entity.blockPosition();
        boolean isInRain = level.isRainingAt(pos) || level.isRainingAt(new BlockPos(pos.getX(), entity.getBoundingBox().maxY, pos.getZ()));
        for(ItemStack stack : entity.getHandSlots()) {
            if(stack.getItem() instanceof UmbrellaItem) {
                if(isInRain) {
                    stack.setDamageValue(MathHelper.clamp(stack.getDamageValue() + 1, 0, stack.getMaxDamage()));
                } else if(level.getBiome(pos).getTemperature(pos) > 1.0F) {
                    stack.setDamageValue(MathHelper.clamp(stack.getDamageValue() - 2, 0, stack.getMaxDamage()));
                } else {
                    stack.setDamageValue(MathHelper.clamp(stack.getDamageValue() - 1, 0, stack.getMaxDamage()));
                }
            }
        }
    }
    
    @SubscribeEvent
    public static void onLivingSpawn(LivingSpawnEvent.SpecialSpawn event) {
        if(event.getEntity() instanceof ZombieEntity) {
            ZombieEntity zombie = (ZombieEntity) event.getEntity();
            Random random = zombie.getRandom();
            if(zombie.level.getDifficulty() == Difficulty.HARD &&
               random.nextFloat() < OriacsServerConfig.CONFIG.UMBRELLA_SPAWN_WITH_ZOMBIE_CHANCE.get() &&
               zombie.getItemInHand(Hand.OFF_HAND).isEmpty()
            ) {
                UmbrellaItem umbrella = OriacsItems.UMBRELLA.get();
                ItemStack stack = umbrella.getDefaultInstance();
                umbrella.setColor(stack, random.nextInt(0xFFFFFF));
                zombie.setItemInHand(Hand.OFF_HAND, stack);
            }
        }
    }

    private static int color(float r, float g, float b) {
        return color(MathHelper.floor(r * 255.0F), MathHelper.floor(g * 255.0F), MathHelper.floor(b * 255.0F));
    }

    private static int color(int r, int g, int b) {
        int a = (r << 8) + g;
        return (a << 8) + b;
    }
    
}
