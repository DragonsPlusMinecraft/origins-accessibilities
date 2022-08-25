package dev.limonblaze.oriacs.client.render;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexFormat;
import dev.limonblaze.oriacs.common.Oriacs;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.inventory.InventoryMenu;

public class WaterRenderHelper extends RenderStateShard {
    private static final ResourceLocation WATER_STILL = new ResourceLocation("block/water_still");
    private static final RenderType FLUID = RenderType.create(Oriacs.asResource("fluid").toString(),
        DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.QUADS, 256, false, true,
        RenderType.CompositeState.builder()
            .setShaderState(RENDERTYPE_ENTITY_TRANSLUCENT_CULL_SHADER)
            .setTextureState(BLOCK_SHEET_MIPPED)
            .setTransparencyState(TRANSLUCENT_TRANSPARENCY)
            .setLightmapState(LIGHTMAP)
            .setOverlayState(OVERLAY)
            .createCompositeState(true));
    
    private WaterRenderHelper() {
        super(null, null, null);
    }
    
    public static void renderBox(float xMin, float yMin, float zMin,
                                 float xMax, float yMax, float zMax,
                                 PoseStack ms, MultiBufferSource buffer, int light, int color) {
        TextureAtlasSprite texture = Minecraft.getInstance()
            .getTextureAtlas(InventoryMenu.BLOCK_ATLAS)
            .apply(WATER_STILL);
        color = 0xFF000000 | color;
        VertexConsumer builder = buffer.getBuffer(FLUID);
        ms.pushPose();
        for(Direction side : Direction.values()) {
            boolean positive = side.getAxisDirection() == Direction.AxisDirection.POSITIVE;
            if(side.getAxis().isHorizontal()) {
                if(side.getAxis() == Direction.Axis.X) {
                    renderFace(side, zMin, yMin, zMax, yMax, positive ? xMax : xMin,
                        builder, ms, light, color, texture);
                } else {
                    renderFace(side, xMin, yMin, xMax, yMax, positive ? zMax : zMin,
                        builder, ms, light, color, texture);
                }
            } else {
                renderFace(side, xMin, zMin, xMax, zMax, positive ? yMax : yMin,
                    builder, ms, light, color, texture);
            }
        }
        ms.popPose();
    }
    
    public static void renderFace(Direction dir,
                                  float left, float down,
                                  float right, float up,
                                  float depth,
                                  VertexConsumer builder, PoseStack ms,
                                  int light, int color,
                                  TextureAtlasSprite texture)
    {
        boolean positive = dir.getAxisDirection() == Direction.AxisDirection.POSITIVE;
        boolean horizontal = dir.getAxis().isHorizontal();
        boolean x = dir.getAxis() == Direction.Axis.X;
        
        float shrink = texture.uvShrinkRatio() * 0.25f;
        float centerU = texture.getU0() + (texture.getU1() - texture.getU0()) * 0.5f;
        float centerV = texture.getV0() + (texture.getV1() - texture.getV0()) * 0.5f;
        
        float f;
        float x2, y2;
        float u1, u2;
        float v1, v2;
        for(float x1 = left; x1 < right; x1 = x2) {
            f = Mth.floor(x1);
            x2 = Math.min(f + 1, right);
            if(dir == Direction.NORTH || dir == Direction.EAST) {
                f = Mth.ceil(x2);
                u1 = texture.getU((f - x2) * 16);
                u2 = texture.getU((f - x1) * 16);
            } else {
                u1 = texture.getU((x1 - f) * 16);
                u2 = texture.getU((x2 - f) * 16);
            }
            u1 = Mth.lerp(shrink, u1, centerU);
            u2 = Mth.lerp(shrink, u2, centerU);
            for(float y1 = down; y1 < up; y1 = y2) {
                f = Mth.floor(y1);
                y2 = Math.min(f + 1, up);
                if(dir == Direction.UP) {
                    v1 = texture.getV((y1 - f) * 16);
                    v2 = texture.getV((y2 - f) * 16);
                } else {
                    f = Mth.ceil(y2);
                    v1 = texture.getV((f - y2) * 16);
                    v2 = texture.getV((f - y1) * 16);
                }
                v1 = Mth.lerp(shrink, v1, centerV);
                v2 = Mth.lerp(shrink, v2, centerV);
                
                if(horizontal) {
                    if(x) {
                        vertex(builder, ms, depth, y2, positive ? x2 : x1, color, u1, v1, dir, light);
                        vertex(builder, ms, depth, y1, positive ? x2 : x1, color, u1, v2, dir, light);
                        vertex(builder, ms, depth, y1, positive ? x1 : x2, color, u2, v2, dir, light);
                        vertex(builder, ms, depth, y2, positive ? x1 : x2, color, u2, v1, dir, light);
                    } else {
                        vertex(builder, ms, positive ? x1 : x2, y2, depth, color, u1, v1, dir, light);
                        vertex(builder, ms, positive ? x1 : x2, y1, depth, color, u1, v2, dir, light);
                        vertex(builder, ms, positive ? x2 : x1, y1, depth, color, u2, v2, dir, light);
                        vertex(builder, ms, positive ? x2 : x1, y2, depth, color, u2, v1, dir, light);
                    }
                } else {
                    vertex(builder, ms, x1, depth, positive ? y1 : y2, color, u1, v1, dir, light);
                    vertex(builder, ms, x1, depth, positive ? y2 : y1, color, u1, v2, dir, light);
                    vertex(builder, ms, x2, depth, positive ? y2 : y1, color, u2, v2, dir, light);
                    vertex(builder, ms, x2, depth, positive ? y1 : y2, color, u2, v1, dir, light);
                }
            }
        }
    }
    
    private static void vertex(VertexConsumer builder, PoseStack ms,
                               float x, float y, float z,
                               int color, float u, float v,
                               Direction face, int light) {
        
        Vec3i normal = face.getNormal();
        PoseStack.Pose peek = ms.last();
        int a = color >> 24 & 0xff;
        int r = color >> 16 & 0xff;
        int g = color >> 8 & 0xff;
        int b = color & 0xff;
        
        builder.vertex(peek.pose(), x, y, z)
            .color(r, g, b, a)
            .uv(u, v)
            .overlayCoords(OverlayTexture.NO_OVERLAY)
            .uv2(light)
            .normal(peek.normal(), normal.getX(), normal.getY(), normal.getZ())
            .endVertex();
    }
    
}
