package io.github.ageuxo.FancyDoorMod.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.client.model.pipeline.VertexConsumerWrapper;
import org.joml.Vector3d;
import org.joml.Vector3f;

import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public abstract class BoundedSquishRenderer<T extends BlockEntity> implements BlockEntityRenderer<T> {
    public final Vector3f maxBound;
    public final Vector3f minBound;
    public final RenderType renderType;

    public BoundedSquishRenderer(Vector3f maxBound, Vector3f minBound, RenderType renderType) {
        this.maxBound = maxBound;
        this.minBound = minBound;
        this.renderType = renderType;
    }

    @Override
    public void render(T pBlockEntity, float pPartialTick, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight, int pPackedOverlay) {
        BoundedSquishVertexConsumer vertexConsumer = new BoundedSquishVertexConsumer(pBuffer.getBuffer(renderType), this);
        render(pBlockEntity, pPartialTick, pPoseStack, vertexConsumer, pPackedLight, pPackedOverlay);
    }

    public abstract void render(T blockEntity, float partialTick, PoseStack poseStack, BoundedSquishVertexConsumer consumer, int packedLight, int packedOverlay);

    public class BoundedSquishVertexConsumer extends VertexConsumerWrapper{
        public final Vector3f maxBound;
        public final Vector3f minBound;

        public BoundedSquishVertexConsumer(VertexConsumer parent, BoundedSquishRenderer<?> renderer) {
            super(parent);
            this.minBound = renderer.minBound;
            this.maxBound = renderer.maxBound;
        }

        @Override
        public VertexConsumer vertex(double pX, double pY, double pZ) {
            /*Vector3d vec = bounded(pX, pY, pZ);
            return super.vertex(vec.x, vec.y, vec.z);*/
            return super.vertex(pX, pY, pZ);
        }

        @Override
        public VertexConsumer uv(float u, float v) {
            return super.uv(u, v);
        }

        public Vector3d bounded(double x, double y, double z){
            return new Vector3d(
                    Math.max(this.minBound.x, Math.min(this.maxBound.x, x)),
                    Math.max(this.minBound.y, Math.min(this.maxBound.y, y)),
                    Math.max(this.minBound.z, Math.min(this.maxBound.z, z))
            );
        }

        @Override
        public void vertex(float pX, float pY, float pZ, float pRed, float pGreen, float pBlue, float pAlpha, float pTexU, float pTexV, int pOverlayUV, int pLightmapUV, float pNormalX, float pNormalY, float pNormalZ) {
            super.vertex(pX, pY, pZ, pRed, pGreen, pBlue, pAlpha, pTexU, pTexV, pOverlayUV, pLightmapUV, pNormalX, pNormalY, pNormalZ);
        }
    }


}
