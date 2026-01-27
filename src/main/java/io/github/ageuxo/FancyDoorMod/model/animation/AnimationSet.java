package io.github.ageuxo.FancyDoorMod.model.animation;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.mojang.serialization.codecs.UnboundedMapCodec;
import io.github.ageuxo.FancyDoorMod.FancyDoorsMod;
import net.minecraft.resources.ResourceLocation;

import java.util.Map;

public record AnimationSet(ResourceLocation id, Map<String, Animation> animations) {
    public static final UnboundedMapCodec<String, Animation> ANIMATIONS_CODEC = Codec.unboundedMap(Codec.STRING, Animation.CODEC);
    public static final Codec<AnimationSet> CODEC = RecordCodecBuilder.create(instance->instance.group(
            ResourceLocation.CODEC.fieldOf("id").forGetter(AnimationSet::id),
            ANIMATIONS_CODEC.fieldOf("animations").forGetter(AnimationSet::animations)
    ).apply(instance, AnimationSet::new));

    public static final AnimationSet EMPTY = new AnimationSet(FancyDoorsMod.modRL("empty"), Map.of());

    public Animation get(String name) {
        return animations.getOrDefault(name, Animation.EMPTY);
    }

}
