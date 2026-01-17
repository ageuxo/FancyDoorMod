package io.github.ageuxo.FancyDoorMod.model;

import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.model.QuadTransformers;
import net.minecraftforge.client.model.geometry.IGeometryBakingContext;
import net.minecraftforge.client.model.geometry.IUnbakedGeometry;
import net.minecraftforge.client.model.geometry.UnbakedGeometryHelper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class GroupGeometry implements IUnbakedGeometry<GroupGeometry> {

    private final List<UnbakedGroup> groups;

    public GroupGeometry(List<UnbakedGroup> groups) {
        this.groups = groups;
    }

    @Override
    public BakedModel bake(IGeometryBakingContext context, ModelBaker baker, Function<Material, TextureAtlasSprite> spriteGetter, ModelState modelState, ItemOverrides overrides, ResourceLocation modelLocation) {
        Material particleLocation = context.getMaterial("particle");
        TextureAtlasSprite particle = spriteGetter.apply(particleLocation);

        var postTransform = QuadTransformers.empty();
        var rootTransform = context.getRootTransform();
        if (!rootTransform.isIdentity())
            postTransform = UnbakedGeometryHelper.applyRootTransform(modelState, rootTransform);

        Map<String, BakedGroup> bakedGroups = new HashMap<>();

        for (UnbakedGroup group : groups) {
            BakedGroup baked = group.bake(context, spriteGetter, modelState, modelLocation, postTransform);
            bakedGroups.put(group.name(), baked);

        }

        return new GroupModel(Map.copyOf(bakedGroups), context.useAmbientOcclusion(), context.isGui3d(), context.useBlockLight(), particle, context.getTransforms(), overrides);
    }
}
