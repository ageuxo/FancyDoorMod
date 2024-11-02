package io.github.ageuxo.FancyDoorMod.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import io.github.ageuxo.FancyDoorMod.adastra.SlidingDoorBlock;
import io.github.ageuxo.FancyDoorMod.adastra.SlidingDoorBlockEntity;
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
public class Sliding2WideDoorBERenderer implements BlockEntityRenderer<SlidingDoorBlockEntity> {

    public Sliding2WideDoorBERenderer(BlockEntityRendererProvider.Context ctx) {
    }

    @Override
    public void render(SlidingDoorBlockEntity entity, float partialTick, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
        poseStack.pushPose();
        float slide = Mth.lerp(partialTick, entity.lastSlideTicks(), entity.slideTicks()) / 100.0f;
        var state = entity.getBlockState();
        var direction = state.getValue(SlidingDoorBlock.FACING);
        var minecraft = Minecraft.getInstance();
        BlockRenderDispatcher blockRenderer = minecraft.getBlockRenderer();
        ModelBlockRenderer modelRenderer = blockRenderer.getModelRenderer();
        var model = blockRenderer.getBlockModel(state);

        Direction offsetDir = direction.getClockWise();

        poseStack.translate((float) offsetDir.getStepX() / 2, offsetDir.getStepY() + 1, (float) offsetDir.getStepZ() / 2);
        renderDoor(poseStack, direction.toYRot(), buffer, packedLight, packedOverlay, slide, state, model, modelRenderer, 0.5f, 0, 0.5f);
        renderDoor(poseStack, 180, buffer, packedLight, packedOverlay, slide+slide, state, model, modelRenderer, 0.5f, 0, 0.25f);


        poseStack.popPose();
    }

    public static void renderDoor(PoseStack poseStack, float direction, MultiBufferSource buffer, int packedLight, int packedOverlay,
                                  float slide, BlockState state, BakedModel model, ModelBlockRenderer modelRenderer,
                                  float pivotX, int pivotY, float pivotZ) {
        rotateAroundY(poseStack, pivotX, pivotY, pivotZ, direction);
        renderDoor(poseStack, buffer, packedLight, packedOverlay, slide, state, model, modelRenderer);
    }


    public static void renderDoor(PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay, float slide, BlockState state, BakedModel model, ModelBlockRenderer modelRenderer) {

        float offset = 0.5f;
        poseStack.translate(slide, 0, offset);

        //noinspection deprecation
        modelRenderer.renderModel(poseStack.last(),
                buffer.getBuffer(Sheets.cutoutBlockSheet()),
                state,
                model,
                1f, 1f, 1f,
                packedLight, packedOverlay);

    }

    public static void rotateAroundY(PoseStack poseStack, float pivotX, float pivotY, float pivotZ, float degrees) {
        poseStack.translate(pivotX, pivotY, pivotZ);
        poseStack.mulPose(Axis.YP.rotationDegrees(degrees));
        poseStack.translate(-pivotX, -pivotY, -pivotZ);
    }

}
