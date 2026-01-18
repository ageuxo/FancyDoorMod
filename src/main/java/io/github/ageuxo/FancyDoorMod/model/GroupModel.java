package io.github.ageuxo.FancyDoorMod.model;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.*;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class GroupModel implements BakedModel {

    protected final Map<String, BakedGroup> groups;
    protected final Map<Direction, List<BakedQuad>> directionCache = new HashMap<>();
    protected final boolean hasAmbientOcclusion;
    protected final boolean isGui3d;
    protected final boolean usesBlockLight;
    protected final TextureAtlasSprite particleIcon;
    protected final ItemTransforms transforms;
    protected final ItemOverrides overrides;

    public GroupModel(Map<String, BakedGroup> groups, boolean hasAmbientOcclusion, boolean isGui3d, boolean usesBlockLight, TextureAtlasSprite particleIcon, ItemTransforms transforms, ItemOverrides overrides) {
        this.groups = groups;
        this.hasAmbientOcclusion = hasAmbientOcclusion;
        this.isGui3d = isGui3d;
        this.usesBlockLight = usesBlockLight;
        this.particleIcon = particleIcon;
        this.transforms = transforms;
        this.overrides = overrides;
    }

    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState pState, @Nullable Direction pDirection, RandomSource pRandom) {
        return directionCache.computeIfAbsent(pDirection, this::initDirectionCache);
    }

    protected List<BakedQuad> initDirectionCache(@Nullable Direction direction) {
        List<BakedQuad> quads = new ArrayList<>();
        for (BakedGroup group : groups.values()) {
            quads.addAll(group.quads(direction));
        }

        return quads;
    }

    public Collection<BakedGroup> groups() {
        return groups.values();
    }

    @Override
    public boolean useAmbientOcclusion() {
        return hasAmbientOcclusion;
    }

    @Override
    public boolean isGui3d() {
        return isGui3d;
    }

    @Override
    public boolean usesBlockLight() {
        return usesBlockLight;
    }

    @Override
    public boolean isCustomRenderer() {
        return false;
    }

    @Override
    public TextureAtlasSprite getParticleIcon() {
        return particleIcon;
    }

    @Override
    public ItemOverrides getOverrides() {
        return overrides;
    }
}
