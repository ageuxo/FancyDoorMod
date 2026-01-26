package io.github.ageuxo.FancyDoorMod.model;

import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.core.Direction;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;

public record BakedGroup(String name, Map<Direction, List<BakedQuad>> quadMap, List<BakedGroup> children) {

    public List<BakedQuad> quads() {
        return quads(null);
    }

    public List<BakedQuad> quads(@Nullable Direction direction) {
        return quadMap.get(direction);
    }

}
