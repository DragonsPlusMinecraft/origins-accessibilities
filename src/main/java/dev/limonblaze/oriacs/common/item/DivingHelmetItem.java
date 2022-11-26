package dev.limonblaze.oriacs.common.item;

import dev.limonblaze.oriacs.common.OriacsServerConfig;
import dev.limonblaze.oriacs.common.registry.OriacsItems;
import io.github.apace100.origins.power.PowerTypes;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.CauldronBlock;
import net.minecraft.block.IBucketPickupHandler;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
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
public class DivingHelmetItem extends OriacsArmorItem {
    
    public DivingHelmetItem(Properties properties) {
        super(OriacsArmorMaterials.DIVING, EquipmentSlotType.HEAD, properties);
    }
    
    public ItemStack transformToLandwalking(ItemStack original) {
        ItemStack transformed = OriacsItems.LANDWALKING_HELMET.get().getDefaultInstance();
        transformed.setTag(original.getOrCreateTag());
        return transformed;
    }
    
    @Override
    public ActionResult<ItemStack> use(World level, PlayerEntity player, Hand hand) {
        if(player.isSecondaryUseActive()) {
            ItemStack original = player.getItemInHand(hand);
            BlockRayTraceResult hit = getPlayerPOVHitResult(level, player, RayTraceContext.FluidMode.SOURCE_ONLY);
            if(hit.getType() == RayTraceResult.Type.BLOCK) {
                Direction direction = hit.getDirection();
                BlockPos pos = hit.getBlockPos();
                BlockPos pos1 = pos.relative(direction);
                if(level.mayInteract(player, pos) && player.mayUseItemAt(pos1, direction, original)) {
                    BlockState state = level.getBlockState(pos);
                    if(state.getBlock() instanceof IBucketPickupHandler) {
                        IBucketPickupHandler pickup = (IBucketPickupHandler) state.getBlock();
                        if(pickup.takeLiquid(level, pos, state).isSame(Fluids.WATER)) {
                            ItemStack transformed = this.transformToLandwalking(original);
                            player.awardStat(Stats.ITEM_USED.get(this));
                            player.playSound(SoundEvents.BUCKET_FILL, 1.0F, 1.0F);
                            return ActionResult.sidedSuccess(transformed, level.isClientSide);
                        }
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
            if(player.isEyeInFluid(FluidTags.WATER)) {
                if(!PowerTypes.WATER_BREATHING.isActive(player)) {
                   player.addEffect(new EffectInstance(Effects.WATER_BREATHING, 30, 0, true, false, true));
                }
                tag.putInt(TRANSFORM_PROGRESS, progress + 1);
            } else tag.putInt(TRANSFORM_PROGRESS, Math.max(0, progress -1));
            if(progress > OriacsServerConfig.CONFIG.DIVING_HELMET_TRANSFORM_TIME.get()
                * (1 + respiration * OriacsServerConfig.CONFIG.DIVING_HELMET_RESPIRATION_MULTIPLIER.get())) {
                tag.putInt(TRANSFORM_PROGRESS, 0);
                ItemStack transformed = this.transformToLandwalking(stack);
                player.setItemSlot(EquipmentSlotType.HEAD, transformed);
            }
        }
    }
    
    public ActionResultType waterCauldronInteraction(BlockState state, World level, BlockPos pos, PlayerEntity player, Hand hand, ItemStack stack) {
        if(state.getValue(CauldronBlock.LEVEL) == 3) {
            if(!level.isClientSide) {
                player.setItemInHand(hand, this.transformToLandwalking(stack));
                player.awardStat(Stats.USE_CAULDRON);
                player.awardStat(Stats.ITEM_USED.get(this));
                level.setBlockAndUpdate(pos, Blocks.CAULDRON.defaultBlockState());
                level.playSound(null, pos, SoundEvents.BUCKET_FILL, SoundCategory.BLOCKS, 1.0F, 1.0F);
            }
            return ActionResultType.sidedSuccess(level.isClientSide);
        }
        return ActionResultType.PASS;
    }
    
}
