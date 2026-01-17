package io.github.ageuxo.FancyDoorMod.model;

import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.BlockElement;
import net.minecraft.client.renderer.block.model.BlockElementFace;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.Material;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.model.IQuadTransformer;
import net.minecraftforge.client.model.geometry.IGeometryBakingContext;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public record UnbakedGroup(String name, Vector3f offset, Vector3f rotation, List<BlockElement> elements) {

    public BakedGroup bake(IGeometryBakingContext context, Function<Material, TextureAtlasSprite> spriteGetter, ModelState modelState, ResourceLocation modelLocation, IQuadTransformer transformer) {
        HashMap<Direction, List<BakedQuad>> mutable = new HashMap<>();
        for (BlockElement element : elements) {
            for (Direction direction : element.faces.keySet()) {

                BlockElementFace face = element.faces.get(direction);
                TextureAtlasSprite sprite = spriteGetter.apply(context.getMaterial(face.texture));
                BakedQuad quad = BlockModel.bakeFace(element, face, sprite, direction, modelState, modelLocation);
                transformer.processInPlace(quad);

                addQuadToDirection(direction, mutable, quad);
                // Null direction includes all directions
                addQuadToDirection(null, mutable, quad);
            }
        }

        return new BakedGroup(name, offset, rotation, Map.copyOf(mutable));
    }

    private static void addQuadToDirection(Direction direction, HashMap<Direction, List<BakedQuad>> directionQuads, BakedQuad quad) {
        directionQuads.compute(direction,
                (dir, qList) -> {
                    if (qList == null) {
                        qList = new ArrayList<>();
                    }

                    qList.add(quad);
                    return qList;
                }
        );
    }

}
