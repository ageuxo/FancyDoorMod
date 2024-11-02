package io.github.ageuxo.FancyDoorMod.render;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.ageuxo.FancyDoorMod.block.entity.DetectorBlockEntity;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.BlockPos;

import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class DetectorBERenderer implements BlockEntityRenderer<DetectorBlockEntity> {

    public DetectorBERenderer(BlockEntityRendererProvider.Context context) {
    }

    @Override
    public void render(DetectorBlockEntity be, float pPartialTick, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight, int pPackedOverlay) {
        if (!be.renderBox) {
            return;
        }

        BlockPos pos = be.getBlockPos();
        LevelRenderer.renderLineBox(pPoseStack, pBuffer.getBuffer(RenderType.lines()), be.area().move(-pos.getX(), -pos.getY(), -pos.getZ()), 1f, 1f, 1f, 1f);
    }
}
