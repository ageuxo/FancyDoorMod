package io.github.ageuxo.FancyDoorMod.render;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.ageuxo.FancyDoorMod.adastra.SlidingDoorBlock;
import io.github.ageuxo.FancyDoorMod.adastra.SlidingDoorBlockEntity;
import io.github.ageuxo.FancyDoorMod.adastra.SlidingDoorBlockEntityRenderer;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
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

        float slide = Mth.lerp(pPartialTick, pBlockEntity.lastSlideTicks(), pBlockEntity.slideTicks()) / 34;
        BlockState state = pBlockEntity.getBlockState();
        Direction direction = state.getValue(SlidingDoorBlock.FACING);
        BlockRenderDispatcher blockRenderer = Minecraft.getInstance().getBlockRenderer();
        BakedModel model = blockRenderer.getBlockModel(state);

        SlidingDoorBlockEntityRenderer.renderDoor(pPoseStack, pBuffer, pPackedLight, pPackedOverlay, direction, slide, state, model, blockRenderer.getModelRenderer());

        pPoseStack.popPose();
    }
}
