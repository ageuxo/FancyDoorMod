package io.github.ageuxo.FancyDoorMod.model.animation;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.ageuxo.FancyDoorMod.model.BakedGroup;

import java.util.Map;

public record Animation(float length, Map<String, Keyframes> bones) {
    public static final Codec<Animation> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.FLOAT.fieldOf("animation_length").forGetter(Animation::length),
            Codec.unboundedMap(Codec.STRING, Keyframes.CODEC).fieldOf("bones").forGetter(Animation::bones)
    ).apply(instance, Animation::new));

    public static final Animation EMPTY = new Animation(0, Map.of());

    public Keyframes get(BakedGroup group) {
        return get(group.name());
    }

    public Keyframes get(String name) {
        return this.bones.getOrDefault(name, Keyframes.EMPTY);
    }
}
