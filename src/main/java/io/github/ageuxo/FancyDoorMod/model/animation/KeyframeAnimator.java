package io.github.ageuxo.FancyDoorMod.model.animation;

import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.joml.Vector3fc;

import java.util.TreeMap;

public class KeyframeAnimator {

    private final Vector3f translation = new Vector3f();
    private final Quaternionf rotation = new Quaternionf();
    private final Vector3f scale = new Vector3f();

    public void calculate(Keyframes keyframes, int frame) {
        TreeMap<Integer, Transform> frames = keyframes.keyframes();
        var fromEntry = frames.floorEntry(frame);
        var toEntry = frames.ceilingEntry(frame);

        float progress = inverseLerpClamped(fromEntry.getKey(), toEntry != null ? toEntry.getKey() : keyframes.lastFrame(), frame);

        Transform from = fromEntry.getValue();
        Transform to = toEntry != null ? toEntry.getValue() : fromEntry.getValue();

        from.translation().lerp(to.translation(), progress, this.translation);
        from.rotation().nlerp(to.rotation(), progress, this.rotation);
        from.scale().lerp(to.scale(), progress, this.scale);
    }

    public Vector3fc translation() {
        return translation;
    }

    public Quaternionf rotation() {
        return rotation;
    }

    public Vector3fc scale() {
        return scale;
    }

    public static float inverseLerpClamped(int from, int to, int current) {
        if (from == to) {
            return 0f;
        }

        float t = (float) (current - from) / (float) (to - from);

        return Math.max(0f, Math.min(1f, t));
    }
}
