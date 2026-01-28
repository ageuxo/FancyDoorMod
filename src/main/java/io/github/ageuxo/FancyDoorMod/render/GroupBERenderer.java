package io.github.ageuxo.FancyDoorMod.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import io.github.ageuxo.FancyDoorMod.adastra.SlidingDoorBlock;
import io.github.ageuxo.FancyDoorMod.adastra.SlidingDoorBlockEntity;
import io.github.ageuxo.FancyDoorMod.model.BakedGroup;
import io.github.ageuxo.FancyDoorMod.model.GroupModel;
import io.github.ageuxo.FancyDoorMod.model.animation.*;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import org.joml.Vector3fc;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Collection;

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
        try {
            GroupModel model = (GroupModel) minecraft.getBlockRenderer().getBlockModel(state);
            poseStack.rotateAround(Axis.YP.rotationDegrees(direction.toYRot()), 0.5f, 0.5f, 0.5f);

            AnimationSet animationSet = KeyframeAnimationLoader.INSTANCE.get(state);
            renderGroups(poseStack, buffer, packedLight, packedOverlay, blockEntity.slideTicks(), animationSet.get("open"), model.groups());
        } catch (ClassCastException e) {
            throw new IllegalStateException("GroupBERenderer tried to render non-GroupModel.");
        }


        poseStack.popPose();
    }

    public void renderGroups(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay, int ticks, Animation animation, Collection<BakedGroup> groups) {
        VertexConsumer buf = bufferSource.getBuffer(RenderType.cutout());

        // Render top level groups
        for (BakedGroup group : groups) {
            renderGroup(poseStack, buf, group, packedLight, packedOverlay, ticks, animation.get(group));

            // Render children
            renderGroups(poseStack, bufferSource, packedLight, packedOverlay, ticks, animation, group.children());
        }
    }

    private void renderGroup(PoseStack poseStack, VertexConsumer buf, BakedGroup group, int packedLight, int packedOverlay, int ticks, Keyframes keyframes) {
        poseStack.pushPose();

        // Calculate animation transforms
        animator.calculate(keyframes, ticks);

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
