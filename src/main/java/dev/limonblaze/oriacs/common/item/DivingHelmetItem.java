package dev.limonblaze.oriacs.common.item;

import dev.limonblaze.oriacs.common.OriacsServerConfig;
import dev.limonblaze.oriacs.common.registry.OriacsItems;
import io.github.apace100.origins.power.OriginsPowerTypes;
import io.github.edwinmindcraft.apoli.api.component.IPowerContainer;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.cauldron.CauldronInteraction;
import net.minecraft.nbt.CompoundTag;
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
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BucketPickup;
import net.minecraft.world.level.block.LayeredCauldronBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class DivingHelmetItem extends OriacsArmorItem {
    
    public DivingHelmetItem(Properties properties) {
        super(OriacsArmorMaterials.DIVING, EquipmentSlot.HEAD, properties);
        CauldronInteraction.WATER.put(this, this::waterCauldronInteraction);
    }
    
    public ItemStack transformToLandwalking(ItemStack original) {
        ItemStack transformed = OriacsItems.LANDWALKING_HELMET.get().getDefaultInstance();
        transformed.setTag(original.getOrCreateTag());
        return transformed;
    }
    
    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        if(player.isSecondaryUseActive()) {
            ItemStack original = player.getItemInHand(hand);
            BlockHitResult hit = getPlayerPOVHitResult(level, player, ClipContext.Fluid.SOURCE_ONLY);
            if(hit.getType() == HitResult.Type.BLOCK) {
                Direction direction = hit.getDirection();
                BlockPos pos = hit.getBlockPos();
                BlockPos pos1 = pos.relative(direction);
                if(level.mayInteract(player, pos) && player.mayUseItemAt(pos1, direction, original)) {
                    BlockState state = level.getBlockState(pos);
                    if(state.getBlock() instanceof BucketPickup pickup &&
                        pickup.pickupBlock(level, pos, state).is(Items.WATER_BUCKET)) {
                        ItemStack transformed = this.transformToLandwalking(original);
                        player.awardStat(Stats.ITEM_USED.get(this));
                        pickup.getPickupSound(state).ifPresent(sound -> player.playSound(sound, 1.0F, 1.0F));
                        level.gameEvent(player, GameEvent.FLUID_PICKUP, pos);
                        return InteractionResultHolder.sidedSuccess(transformed, level.isClientSide);
                    }
                }
            }
            return InteractionResultHolder.pass(original);
        } else {
            return super.use(level, player, hand);
        }
    }
    
    @Override
    public boolean overrideOtherStackedOnMe(ItemStack stack, ItemStack other, Slot slot, ClickAction action, Player player, SlotAccess access) {
        if(action != ClickAction.PRIMARY) return false;
        IFluidHandlerItem handler = other.getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM).orElse(null);
        if(handler == null) return false;
        FluidStack bucket = new FluidStack(Fluids.WATER, FluidType.BUCKET_VOLUME);
        FluidStack simulate = handler.drain(bucket, IFluidHandler.FluidAction.SIMULATE);
        if(simulate.getAmount() >= FluidType.BUCKET_VOLUME) {
            handler.drain(bucket, IFluidHandler.FluidAction.EXECUTE);
            slot.set(this.transformToLandwalking(stack));
            access.set(handler.getContainer());
            player.level.playLocalSound(player.getX(), player.getY(), player.getZ(), SoundEvents.BUCKET_EMPTY, SoundSource.PLAYERS, 1.0F, 1.0F, false);
            return true;
        }
        return false;
    }
    
    @Override
    public void onArmorTick(ItemStack stack, Level level, Player player) {
        if(!level.isClientSide && player.tickCount % 20 == 0) {
            CompoundTag tag = stack.getOrCreateTag();
            int progress = tag.getInt(TRANSFORM_PROGRESS);
            int respiration = stack.getEnchantmentLevel(Enchantments.RESPIRATION);
            if(player.isEyeInFluidType(Fluids.WATER.getFluidType())) {
                if(!IPowerContainer.hasPower(player, OriginsPowerTypes.WATER_BREATHING.get())) {
                   player.addEffect(new MobEffectInstance(MobEffects.WATER_BREATHING, 30, 0, true, false, true));
                }
                tag.putInt(TRANSFORM_PROGRESS, progress + 1);
            } else tag.putInt(TRANSFORM_PROGRESS, Math.max(0, progress -1));
            if(progress > OriacsServerConfig.CONFIG.DIVING_HELMET_TRANSFORM_TIME.get()
                * (1 + respiration * OriacsServerConfig.CONFIG.DIVING_HELMET_RESPIRATION_MULTIPLIER.get())) {
                tag.putInt(TRANSFORM_PROGRESS, 0);
                ItemStack transformed = this.transformToLandwalking(stack);
                player.setItemSlot(EquipmentSlot.HEAD, transformed);
            }
        }
    }
    
    public InteractionResult waterCauldronInteraction(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, ItemStack stack) {
        if(state.getValue(LayeredCauldronBlock.LEVEL) == 3) {
            if(!level.isClientSide) {
                player.setItemInHand(hand, this.transformToLandwalking(stack));
                player.awardStat(Stats.USE_CAULDRON);
                player.awardStat(Stats.ITEM_USED.get(this));
                level.setBlockAndUpdate(pos, Blocks.CAULDRON.defaultBlockState());
                level.playSound(null, pos, SoundEvents.BUCKET_FILL, SoundSource.BLOCKS, 1.0F, 1.0F);
                level.gameEvent(player, GameEvent.FLUID_PICKUP, pos);
                return InteractionResult.sidedSuccess(level.isClientSide);
            }
        }
        return InteractionResult.PASS;
    }
    
}
