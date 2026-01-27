package io.github.ageuxo.FancyDoorMod.model.animation;

import com.mojang.serialization.Codec;

import java.util.Map;
import java.util.TreeMap;

public class Keyframes {
    public static final Codec<Keyframes> CODEC = Transform.Set.CODEC.xmap(Transform.Set::toKeyframes, Transform.Set::fromKeyframes);

    public static final Keyframes EMPTY = new Keyframes(Map.of(0, Transform.ZERO));

    private final TreeMap<Integer, Transform> keyframes;
    private final int lastFrame;

    public Keyframes(Map<Integer, Transform> keyframes) {
        if (keyframes.isEmpty()) {
            throw new IllegalStateException("Keyframes need at least one entry");
        }
        this.keyframes = new TreeMap<>(keyframes);
        // Make sure there is a frame zero
        this.keyframes.putIfAbsent(0, Transform.ZERO);

        this.lastFrame = keyframes().lastKey();
    }

    public TreeMap<Integer, Transform> keyframes() {
        return keyframes;
    }

    public Transform from(int frame) {
        return keyframes.floorEntry(frame).getValue();
    }

    public Transform to(int frame) {
        return keyframes.ceilingEntry(frame).getValue();
    }

    public int lastFrame() {
        return lastFrame;
    }
}
