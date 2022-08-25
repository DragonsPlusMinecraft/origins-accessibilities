package dev.limonblaze.oriacs.core.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.limonblaze.oriacs.client.render.WaterRenderHelper;
import dev.limonblaze.oriacs.common.item.LandwalkingHelmetItem;
import dev.limonblaze.oriacs.common.registry.OriacsItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.color.item.ItemColors;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.Fluids;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HumanoidArmorLayer.class)
public class HumanoidArmorLayerMixin {
    
    @Inject(
        method = "renderArmorPiece",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/renderer/entity/layers/HumanoidArmorLayer;renderModel(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;IZLnet/minecraft/client/model/Model;FFFLnet/minecraft/resources/ResourceLocation;)V",
            ordinal = 2,
            remap = false
        )
    )
    private void oriacs$renderLandwalkingHelmet(PoseStack poseStack, MultiBufferSource buffer, LivingEntity living, EquipmentSlot slot, int light, HumanoidModel<?> model, CallbackInfo ci) {
        ItemStack stack = living.getItemBySlot(slot);
        if(!(stack.getItem() instanceof LandwalkingHelmetItem)) return;
        poseStack.pushPose();
        model.head.translateAndRotate(poseStack);
        int color = Minecraft.getInstance().getItemColors().getColor(stack, 0);
        WaterRenderHelper.renderBox(-0.3F, -0.55F, -0.3F, 0.3F, 0.05F, 0.3F, poseStack, buffer, light, color < 0 ? 0x3F76E4 : color);
        poseStack.popPose();
    }
    
}
