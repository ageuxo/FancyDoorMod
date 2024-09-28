package io.github.ageuxo.FancyDoorMod.adastra;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.block.ModelBlockRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault // FancyDoors: add annotation
public class SlidingDoorBlockEntityRenderer implements BlockEntityRenderer<SlidingDoorBlockEntity> {

    public SlidingDoorBlockEntityRenderer(BlockEntityRendererProvider.Context ctx) {
    }

    @Override // FancyDoors: Remove lib dependency, simplify renderer, replace constants
    public void render(SlidingDoorBlockEntity entity, float partialTick, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
        poseStack.pushPose();
        float slide = Mth.lerp(partialTick, entity.lastSlideTicks(), entity.slideTicks()) / 81.0f;
        var state = entity.getBlockState();
        var direction = state.getValue(SlidingDoorBlock.FACING);
        var minecraft = Minecraft.getInstance();
        var model = minecraft.getBlockRenderer().getBlockModel(state);

        // FancyDoors: Extract door rendering to static method for use in other BERs
        renderDoor(poseStack, buffer, packedLight, packedOverlay, direction, slide, state, model, minecraft.getBlockRenderer().getModelRenderer());

        poseStack.translate(-slide - slide, 0, 0);

        poseStack.translate(0.5f, 0, 0.5f);
        poseStack.mulPose(Axis.YP.rotationDegrees(180));
        poseStack.translate(-0.5f, 0, -0.5f);
        poseStack.translate(0, 0, 1f); // FancyDoors: Tweak alignment to fit our models

        minecraft.getBlockRenderer().getModelRenderer().renderModel(poseStack.last(),
                buffer.getBuffer(Sheets.cutoutBlockSheet()),
                state,
                model,
                1f, 1f, 1f,
                packedLight, packedOverlay);

        //FancyDoors: Remove SimpleSlidingDoor bits
        poseStack.popPose();
    }

    // FancyDoors: Extract door rendering to static method for use in other BERs
    public static void renderDoor(PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay, Direction direction, float slide, BlockState state, BakedModel model, ModelBlockRenderer modelRenderer) {
        poseStack.translate(0.5f, 1, 0.5f);
        poseStack.mulPose(Axis.YP.rotationDegrees(direction.toYRot()));
        poseStack.translate(-0.5f, 0, -0.5f);

        // FancyDoors: Make offsets fields
        float offset = 0.5f;
        float zOffset = -0.5f;

        poseStack.translate(slide, 0, offset);
        if (direction.getAxis() == Direction.Axis.Z) {
            poseStack.translate(0, 0, offset + zOffset);
        }

        modelRenderer.renderModel(poseStack.last(),
            buffer.getBuffer(Sheets.cutoutBlockSheet()),
                state,
                model,
            1f, 1f, 1f,
                packedLight, packedOverlay);
    }
}