package dev.limonblaze.oriacs.core.mixin;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.simibubi.create.foundation.fluid.FluidRenderer;
import dev.limonblaze.oriacs.common.item.LandwalkingHelmetItem;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.layers.BipedArmorLayer;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.entity.LivingEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BipedArmorLayer.class)
public class HumanoidArmorLayerMixin {
    
    @Inject(
        method = "renderArmorPiece",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/renderer/entity/layers/BipedArmorLayer;renderModel(Lcom/mojang/blaze3d/matrix/MatrixStack;Lnet/minecraft/client/renderer/IRenderTypeBuffer;IZLnet/minecraft/client/renderer/entity/model/BipedModel;FFFLnet/minecraft/util/ResourceLocation;)V",
            ordinal = 2,
            remap = false
        )
    )
    private void oriacs$renderLandwalkingHelmet(MatrixStack poseStack, IRenderTypeBuffer buffer, LivingEntity living, EquipmentSlotType slot, int light, BipedModel<?> model, CallbackInfo ci) {
        ItemStack stack = living.getItemBySlot(slot);
        if(!(stack.getItem() instanceof LandwalkingHelmetItem)) return;
        poseStack.pushPose();
        model.head.translateAndRotate(poseStack);
        FluidRenderer.renderTiledFluidBB(new FluidStack(Fluids.WATER, 1000), -0.3F, -0.55F, -0.3F, 0.3F, 0.05F, 0.3F, buffer, poseStack, light, true);
        poseStack.popPose();
    }
    
}
