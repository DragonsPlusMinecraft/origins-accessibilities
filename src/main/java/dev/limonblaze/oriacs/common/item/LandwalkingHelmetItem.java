package dev.limonblaze.oriacs.common.item;

import dev.limonblaze.oriacs.common.OriacsServerConfig;
import dev.limonblaze.oriacs.common.registry.OriacsItems;
import io.github.apace100.origins.power.PowerTypes;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.CauldronBlock;
import net.minecraft.block.ILiquidContainer;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.BucketItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.stats.Stats;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class LandwalkingHelmetItem extends OriacsArmorItem {
    
    public LandwalkingHelmetItem(Properties properties) {
        super(OriacsArmorMaterials.DIVING, EquipmentSlotType.HEAD, properties);
    }
    
    public ItemStack transformToDiving(ItemStack original) {
        ItemStack transformed = OriacsItems.DIVING_HELMET.get().getDefaultInstance();
        transformed.setTag(original.getOrCreateTag());
        return transformed;
    }
    
    @Override
    public ActionResult<ItemStack> use(World level, PlayerEntity player, Hand hand) {
        if(player.isSecondaryUseActive()) {
            ItemStack original = player.getItemInHand(hand);
            BlockRayTraceResult hit = getPlayerPOVHitResult(level, player, RayTraceContext.FluidMode.NONE);
            if(hit.getType() == RayTraceResult.Type.BLOCK) {
                Direction direction = hit.getDirection();
                BlockPos pos = hit.getBlockPos();
                BlockPos pos1 = pos.relative(direction);
                if(level.mayInteract(player, pos) && player.mayUseItemAt(pos1, direction, original)) {
                    BlockState blockstate = level.getBlockState(pos);
                    boolean canPlaceLiquid = false;
                    if (blockstate.getBlock() instanceof ILiquidContainer) {
                        ILiquidContainer container = (ILiquidContainer) blockstate.getBlock();
                        if (container.canPlaceLiquid(level, pos, blockstate, Fluids.WATER))
                            canPlaceLiquid = true;
                    }
                    pos = canPlaceLiquid ? pos : pos1;
                    BucketItem bucket = (BucketItem) Items.WATER_BUCKET;
                    if(bucket.emptyBucket(player, level, pos, hit)) {
                        ItemStack transformed = this.transformToDiving(original);
                        if(player instanceof ServerPlayerEntity) {
                            CriteriaTriggers.PLACED_BLOCK.trigger((ServerPlayerEntity) player, pos, original);
                        }
                        player.awardStat(Stats.ITEM_USED.get(this));
                        return ActionResult.sidedSuccess(transformed, level.isClientSide());
                    }
                }
            }
            return ActionResult.pass(original);
        } else {
            return super.use(level, player, hand);
        }
    }
    
    @Override
    public void onArmorTick(ItemStack stack, World level, PlayerEntity player) {
        if(!level.isClientSide && player.tickCount % 20 == 0) {
            CompoundNBT tag = stack.getOrCreateTag();
            int progress = tag.getInt(TRANSFORM_PROGRESS);
            int respiration = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.RESPIRATION, stack);
            if(!player.isEyeInFluid(FluidTags.WATER)) {
                if(PowerTypes.WATER_BREATHING.isActive(player)) {
                   player.addEffect(new EffectInstance(Effects.WATER_BREATHING, 30, 0, true, false, true));
                }
                tag.putInt(TRANSFORM_PROGRESS, progress + 1);
            } else tag.putInt(TRANSFORM_PROGRESS, Math.max(0, progress -1));
            if(progress > OriacsServerConfig.CONFIG.LANDWALKING_HELMET_TRANSFORM_TIME.get()
                * (1 + respiration * OriacsServerConfig.CONFIG.LANDWALKING_HELMET_RESPIRATION_MULTIPLIER.get())) {
                tag.putInt(TRANSFORM_PROGRESS, 0);
                ItemStack transformed = this.transformToDiving(stack);
                player.setItemSlot(EquipmentSlotType.HEAD, transformed);
            }
        }
    }
    
    public ActionResultType emptyCauldronInteraction(BlockState state, World level, BlockPos pos, PlayerEntity player, Hand hand, ItemStack stack) {
        if(!level.isClientSide) {
            player.setItemInHand(hand, this.transformToDiving(stack));
            player.awardStat(Stats.FILL_CAULDRON);
            player.awardStat(Stats.ITEM_USED.get(this));
            level.setBlockAndUpdate(pos, Blocks.CAULDRON.defaultBlockState().setValue(CauldronBlock.LEVEL, 3));
            level.playSound(null, pos, SoundEvents.BUCKET_EMPTY, SoundCategory.BLOCKS, 1.0F, 1.0F);
        }
        return ActionResultType.sidedSuccess(level.isClientSide);
    }
    
}
