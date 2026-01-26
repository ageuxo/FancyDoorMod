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
import java.util.function.Function;

public record UnbakedGroup(String name, Vector3f origin, List<UnbakedGroup> children, List<Integer> elementIndices) {

    public BakedGroup bake(IGeometryBakingContext context, Function<Material, TextureAtlasSprite> spriteGetter, ModelState modelState,
                           ResourceLocation modelLocation, IQuadTransformer transformer, List<BlockElement> allElements) {
        List<BakedGroup> bakedChildren = new ArrayList<>();
        HashMap<Direction, List<BakedQuad>> quadMap = new HashMap<>();
        bakeElements(context, spriteGetter, modelState, modelLocation, transformer, allElements, quadMap);
        for (UnbakedGroup unbaked : children) {
            bakedChildren.add(unbaked.bake(context, spriteGetter, modelState, modelLocation, transformer, allElements));
        }
        return new BakedGroup(name, quadMap, bakedChildren);
    }

    private void bakeElements(IGeometryBakingContext context, Function<Material, TextureAtlasSprite> spriteGetter,
                                     ModelState modelState, ResourceLocation modelLocation, IQuadTransformer transformer,
                                     List<BlockElement> allElements, HashMap<Direction, List<BakedQuad>> quadMap) {
        for (int index : elementIndices) {
            BlockElement element = allElements.get(index);
            for (Direction direction : element.faces.keySet()) {

                BlockElementFace face = element.faces.get(direction);
                TextureAtlasSprite sprite = spriteGetter.apply(context.getMaterial(face.texture));
                BakedQuad quad = BlockModel.bakeFace(element, face, sprite, direction, modelState, modelLocation);
                transformer.processInPlace(quad);

                addQuadToDirection(direction, quadMap, quad);
                // Null direction includes all directions
                addQuadToDirection(null, quadMap, quad);
            }
        }
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
