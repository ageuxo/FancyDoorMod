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
import net.minecraft.client.renderer.block.ModelBlockRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class PortcullisRenderer implements BlockEntityRenderer<SlidingDoorBlockEntity> {

    public PortcullisRenderer(BlockEntityRendererProvider.Context context) {

    }

    @Override
    public void render(SlidingDoorBlockEntity entity, float partialTick, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
        poseStack.pushPose();

        BlockState state = entity.getBlockState();
        float slide = Mth.lerp(partialTick, entity.lastSlideTicks(), entity.slideTicks()) / 100.0f;
        Direction direction = state.getValue(SlidingDoorBlock.FACING);
        BlockRenderDispatcher blockRenderer = Minecraft.getInstance().getBlockRenderer();
        BakedModel model = blockRenderer.getBlockModel(state);

        poseStack.translate(0.5f, 0, 0.5f);
        poseStack.mulPose(Axis.YP.rotationDegrees(direction.toYRot()));
        poseStack.translate(-0.5f, 2, 0.5f);

        poseStack.translate(0, slide/1.5, -0.2);
        renderSegment(poseStack, buffer, packedLight, packedOverlay, slide, 0, state, model, blockRenderer.getModelRenderer() );
        for (float i = 0; i < 2; i++) {
            poseStack.translate(0,  slide-1f, 0.001f);
            renderSegment(poseStack, buffer, packedLight, packedOverlay, slide, 0, state, model, blockRenderer.getModelRenderer() );
        }

        poseStack.popPose();
    }

    public void renderSegment(PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay, float slide, float tilt, BlockState state, BakedModel model, ModelBlockRenderer modelRenderer) {
        poseStack.pushPose();
        if (tilt != 0f) {
            poseStack.translate(0, 1f, 0.15 * slide);
            poseStack.mulPose(Axis.XP.rotationDegrees(tilt));
            poseStack.translate(0, -1f, -0.15 * slide);
        }
        modelRenderer.renderModel(poseStack.last(),
                buffer.getBuffer(Sheets.cutoutBlockSheet()),
                state,
                model,
                1f, 1f, 1f,
                packedLight, packedOverlay);
        poseStack.translate(1f, 0f, 0f);
        modelRenderer.renderModel(poseStack.last(),
                buffer.getBuffer(Sheets.cutoutBlockSheet()),
                state,
                model,
                1f, 1f, 1f,
                packedLight, packedOverlay);
        poseStack.translate(-2, 0f, 0f);
        modelRenderer.renderModel(poseStack.last(),
                buffer.getBuffer(Sheets.cutoutBlockSheet()),
                state,
                model,
                1f, 1f, 1f,
                packedLight, packedOverlay);
        poseStack.popPose();
    }
}
