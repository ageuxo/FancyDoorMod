package io.github.ageuxo.FancyDoorMod.model;

import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.core.Direction;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

import java.util.List;
import java.util.Map;

public record BakedGroup(String name, Vector3f offset, Vector3f rotation, Map<Direction, List<BakedQuad>> quadMap) {

    public List<BakedQuad> quads() {
        return quads(null);
    }

    public List<BakedQuad> quads(@Nullable Direction direction) {
        return quadMap.get(direction);
    }

}
