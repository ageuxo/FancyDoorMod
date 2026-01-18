package io.github.ageuxo.FancyDoorMod.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import io.github.ageuxo.FancyDoorMod.adastra.SlidingDoorBlock;
import io.github.ageuxo.FancyDoorMod.adastra.SlidingDoorBlockEntity;
import io.github.ageuxo.FancyDoorMod.model.BakedGroup;
import io.github.ageuxo.FancyDoorMod.model.GroupModel;
import io.github.ageuxo.FancyDoorMod.model.animation.KeyframeAnimator;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import org.joml.Vector3fc;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class GroupBERenderer implements BlockEntityRenderer<SlidingDoorBlockEntity> {

    private final KeyframeAnimator animator;

    public GroupBERenderer(BlockEntityRendererProvider.Context context) {
        animator = new KeyframeAnimator();
    }

    @Override
    public void render(SlidingDoorBlockEntity blockEntity, float partialTick, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
        poseStack.pushPose();

        var state = blockEntity.getBlockState();
        var direction = state.getValue(SlidingDoorBlock.FACING);
        var minecraft = Minecraft.getInstance();
        var model = minecraft.getBlockRenderer().getBlockModel(state);

        poseStack.rotateAround(Axis.YP.rotationDegrees(direction.toYRot()), 0.5f, 0.5f, 0.5f);

        renderGroups(poseStack, buffer, packedLight, packedOverlay, (GroupModel) model, blockEntity.slideTicks());

        poseStack.popPose();
    }

    public void renderGroups(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay, GroupModel model, int ticks) {
        VertexConsumer buf = bufferSource.getBuffer(RenderType.cutout());

        for (BakedGroup group : model.groups()) {
            poseStack.pushPose();

            // Calculate animation transforms
            animator.calculate(group.keyframes(), ticks);

            // Use animation transforms
            Vector3fc translation = animator.translation();
            poseStack.translate(translation.x(), translation.y(), translation.z());
            poseStack.rotateAround(animator.rotation(), 0.5f, 0.5f, 0.5f);
            Vector3fc scale = animator.scale();
            poseStack.scale(scale.x(), scale.y(), scale.z());

            // Render quads in group
            for (BakedQuad quad : group.quads()) {
                buf.putBulkData(poseStack.last(), quad, 1f, 1f, 1f, packedLight, packedOverlay);
            }

            poseStack.popPose();
        }
    }

}
