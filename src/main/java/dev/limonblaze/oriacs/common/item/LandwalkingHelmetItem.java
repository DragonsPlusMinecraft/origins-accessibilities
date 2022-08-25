package dev.limonblaze.oriacs.common.item;

import dev.limonblaze.oriacs.common.OriacsServerConfig;
import dev.limonblaze.oriacs.common.registry.OriacsItems;
import io.github.apace100.origins.power.OriginsPowerTypes;
import io.github.edwinmindcraft.apoli.api.component.IPowerContainer;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.cauldron.CauldronInteraction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LayeredCauldronBlock;
import net.minecraft.world.level.block.LiquidBlockContainer;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class LandwalkingHelmetItem extends OriacsArmorItem {
    
    public LandwalkingHelmetItem(Properties properties) {
        super(OriacsArmorMaterials.DIVING, EquipmentSlot.HEAD, properties);
        CauldronInteraction.EMPTY.put(this, this::emptyCauldronInteraction);
    }
    
    public ItemStack transformToDiving(ItemStack original) {
        ItemStack transformed = OriacsItems.DIVING_HELMET.get().getDefaultInstance();
        transformed.setTag(original.getOrCreateTag());
        return transformed;
    }
    
    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        if(player.isSecondaryUseActive()) {
            ItemStack original = player.getItemInHand(hand);
            BlockHitResult hit = getPlayerPOVHitResult(level, player, ClipContext.Fluid.NONE);
            if(hit.getType() == HitResult.Type.BLOCK) {
                Direction direction = hit.getDirection();
                BlockPos pos = hit.getBlockPos();
                BlockPos pos1 = pos.relative(direction);
                if(level.mayInteract(player, pos) && player.mayUseItemAt(pos1, direction, original)) {
                    BlockState blockstate = level.getBlockState(pos);
                    pos = blockstate.getBlock() instanceof LiquidBlockContainer container &&
                            container.canPlaceLiquid(level, pos, blockstate, Fluids.WATER) ? pos : pos1;
                    if(Items.WATER_BUCKET instanceof BucketItem bucket &&
                       bucket.emptyContents(player, level, pos, hit)
                    ) {
                        ItemStack transformed = this.transformToDiving(original);
                        if(player instanceof ServerPlayer) {
                            CriteriaTriggers.PLACED_BLOCK.trigger((ServerPlayer)player, pos, original);
                        }
                        player.awardStat(Stats.ITEM_USED.get(this));
                        return InteractionResultHolder.sidedSuccess(transformed, level.isClientSide());
                    }
                }
            }
            return InteractionResultHolder.pass(original);
        } else {
            return super.use(level, player, hand);
        }
    }
    
    @Override
    public void onArmorTick(ItemStack stack, Level level, Player player) {
        if(!level.isClientSide && player.tickCount % 20 == 0) {
            CompoundTag tag = stack.getOrCreateTag();
            int progress = tag.getInt(TRANSFORM_PROGRESS);
            int respiration = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.RESPIRATION, stack);
            if(!player.isEyeInFluid(FluidTags.WATER)) {
                if(IPowerContainer.hasPower(player, OriginsPowerTypes.WATER_BREATHING.get())) {
                   player.addEffect(new MobEffectInstance(MobEffects.WATER_BREATHING, 30, 0, true, false, true));
                }
                tag.putInt(TRANSFORM_PROGRESS, progress + 1);
            } else tag.putInt(TRANSFORM_PROGRESS, Math.max(0, progress -1));
            if(progress > OriacsServerConfig.CONFIG.LANDWALKING_HELMET_TRANSFORM_TIME.get()
                * (1 + respiration * OriacsServerConfig.CONFIG.LANDWALKING_HELMET_RESPIRATION_MULTIPLIER.get())) {
                tag.putInt(TRANSFORM_PROGRESS, 0);
                ItemStack transformed = this.transformToDiving(stack);
                player.setItemSlot(EquipmentSlot.HEAD, transformed);
            }
        }
    }
    
    @Override
    public boolean overrideOtherStackedOnMe(ItemStack stack, ItemStack other, Slot slot, ClickAction action, Player player, SlotAccess access) {
        if(action != ClickAction.PRIMARY) return false;
        IFluidHandlerItem handler = other.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY).orElse(null);
        if(handler == null) return false;
        FluidStack bucket = new FluidStack(Fluids.WATER, FluidAttributes.BUCKET_VOLUME);
        if(handler.fill(bucket, IFluidHandler.FluidAction.SIMULATE) == FluidAttributes.BUCKET_VOLUME) {
            handler.fill(bucket, IFluidHandler.FluidAction.EXECUTE);
            slot.set(this.transformToDiving(stack));
            access.set(handler.getContainer());
            player.level.playLocalSound(player.getX(), player.getY(), player.getZ(), SoundEvents.BUCKET_FILL, SoundSource.PLAYERS, 1.0F, 1.0F, false);
            return true;
        }
        return false;
    }
    
    public InteractionResult emptyCauldronInteraction(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, ItemStack stack) {
        if(!level.isClientSide) {
            player.setItemInHand(hand, this.transformToDiving(stack));
            player.awardStat(Stats.FILL_CAULDRON);
            player.awardStat(Stats.ITEM_USED.get(this));
            level.setBlockAndUpdate(pos, Blocks.WATER_CAULDRON.defaultBlockState().setValue(LayeredCauldronBlock.LEVEL, 3));
            level.playSound(null, pos, SoundEvents.BUCKET_EMPTY, SoundSource.BLOCKS, 1.0F, 1.0F);
            level.gameEvent(null, GameEvent.FLUID_PLACE, pos);
        }
        return InteractionResult.sidedSuccess(level.isClientSide);
    }
    
}
