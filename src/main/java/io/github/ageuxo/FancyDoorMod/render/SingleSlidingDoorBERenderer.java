package io.github.ageuxo.FancyDoorMod.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import io.github.ageuxo.FancyDoorMod.adastra.SlidingDoorBlock;
import io.github.ageuxo.FancyDoorMod.adastra.SlidingDoorBlockEntity;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class SingleSlidingDoorBERenderer implements BlockEntityRenderer<SlidingDoorBlockEntity> {

    public SingleSlidingDoorBERenderer(BlockEntityRendererProvider.Context context) {
    }

    @Override
    public void render(SlidingDoorBlockEntity pBlockEntity, float pPartialTick, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight, int pPackedOverlay) {
        pPoseStack.pushPose();

        float slide = Mth.lerp(pPartialTick, pBlockEntity.lastSlideTicks(), pBlockEntity.slideTicks()) / 33;
        BlockState state = pBlockEntity.getBlockState();
        Direction direction = state.getValue(SlidingDoorBlock.FACING);
        BlockRenderDispatcher blockRenderer = Minecraft.getInstance().getBlockRenderer();
        BakedModel model = blockRenderer.getBlockModel(state);

        pPoseStack.translate(0.5f, 1, 0.5f);
        pPoseStack.mulPose(Axis.YP.rotationDegrees(direction.toYRot()));
        pPoseStack.translate(-0.5f, 0, -0.5f);

        pPoseStack.translate(slide, 0, 0.0625f);
        if (direction.getAxis() == Direction.Axis.Z) {
            pPoseStack.translate(0, 0, 0.6875f);
        }

        blockRenderer.getModelRenderer().renderModel(pPoseStack.last(),
                pBuffer.getBuffer(Sheets.cutoutBlockSheet()),
                state,
                model,
                1f, 1f, 1f,
                pPackedLight, pPackedOverlay);


        pPoseStack.popPose();
    }
}
