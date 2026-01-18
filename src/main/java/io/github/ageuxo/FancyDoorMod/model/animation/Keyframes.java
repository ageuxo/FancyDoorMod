package io.github.ageuxo.FancyDoorMod.model.animation;

import java.util.Map;
import java.util.TreeMap;

public class Keyframes {
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
