package io.github.ageuxo.FancyDoorMod.model.animation;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.util.ExtraCodecs;
import org.joml.Quaternionf;
import org.joml.Quaternionfc;
import org.joml.Vector3f;
import org.joml.Vector3fc;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.HashMap;
import java.util.Map;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class Transform {
    public static final Codec<Transform> CODEC = RecordCodecBuilder.create(instance-> instance.group(
            ExtraCodecs.VECTOR3F.optionalFieldOf("translation", new Vector3f()).forGetter(t->t.translation),
            ExtraCodecs.QUATERNIONF.optionalFieldOf("rotation", new Quaternionf()).forGetter(t->t.rotation),
            ExtraCodecs.VECTOR3F.optionalFieldOf("scale", new Vector3f()).forGetter(t->t.scale)
    ).apply(instance, Transform::new));

    public static final Transform ZERO = new Transform(new Vector3f(), new Quaternionf(), new Vector3f());

    private final Vector3f translation;
    private final Quaternionf rotation;
    private final Vector3f scale;

    public Transform(Vector3f translation, Quaternionf rotation, Vector3f scale) {
        this.translation = translation;
        this.rotation = rotation;
        this.scale = scale;
    }

    public Vector3fc translation() {
        return translation;
    }

    public Quaternionfc rotation() {
        return rotation;
    }

    public Vector3fc scale() {
        return scale;
    }

    public record Set(Map<Float, Vector3f> translations, Map<Float, Vector3f> rotations, Map<Float, Vector3f> scales) {
        public static final Codec<Set> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                Codec.unboundedMap(Codec.FLOAT, ExtraCodecs.VECTOR3F).optionalFieldOf("position", Set.defaultTransformMap()).forGetter(Set::translations),
                Codec.unboundedMap(Codec.FLOAT, ExtraCodecs.VECTOR3F).optionalFieldOf("rotation", Set.defaultTransformMap()).forGetter(Set::rotations),
                Codec.unboundedMap(Codec.FLOAT, ExtraCodecs.VECTOR3F).optionalFieldOf("scale", Set.defaultTransformMap()).forGetter(Set::scales)
        ).apply(instance, Set::new));

        public Keyframes toKeyframes() {
            Map<Float, Builder> frames = new HashMap<>();

            extractTranslations(frames);
            extractRotations(frames);
            extractScales(frames);

            Map<Integer, Transform> transformMap = new HashMap<>();

            frames.values().forEach(b -> transformMap.put((int)(b.frame * 20), b.build()));

            return new Keyframes(transformMap);
        }

        private void extractTranslations(Map<Float, Builder> frames) {
            for (var entry : this.translations.entrySet()) {
                Builder frame = frames.computeIfAbsent(entry.getKey(), Builder::new);
                frame.translation(entry.getValue());
            }
        }

        private void extractRotations(Map<Float, Builder> frames) {
            for (var entry : this.rotations.entrySet()) {
                Builder frame = frames.computeIfAbsent(entry.getKey(), Builder::new);
                frame.rotation(entry.getValue());
            }
        }

        private void extractScales(Map<Float, Builder> frames) {
            for (var entry : this.scales.entrySet()) {
                Builder frame = frames.computeIfAbsent(entry.getKey(), Builder::new);
                frame.scale(entry.getValue());
            }
        }

        public static Set fromKeyframes(Keyframes keyframes) {

            Map<Float, Vector3f> translations = new HashMap<>();
            Map<Float, Vector3f> rotations = new HashMap<>();
            Map<Float, Vector3f> scales = new HashMap<>();

            for (var keyframe : keyframes.keyframes().entrySet()) {
                float frame = (float) keyframe.getKey() / 20;
                Transform transform = keyframe.getValue();

                translations.put(frame, transform.translation);
                rotations.put(frame, transform.rotation.getEulerAnglesXYZ(new Vector3f())); // TODO make sure this is right rotation order
                scales.put(frame, transform.scale);

            }

            return new Set(translations, rotations, scales);
        }

        private static Map<Float, Vector3f> defaultTransformMap() {
            HashMap<Float, Vector3f> map = new HashMap<>();
            map.put(0f, new Vector3f());
            return map;
        }

    }

    public static class Builder {

        private final float frame;
        private Vector3f translation;
        private Vector3f rotation;
        private Vector3f scale;

        public Builder(float frame) {
            this.frame = frame;
        }

        public Builder translation(Vector3f translation) {
            this.translation = translation;

            return this;
        }

        public Builder rotation(Vector3f rotation) {
            this.rotation = rotation;

            return this;
        }

        public Builder scale(Vector3f scale) {
            this.scale = scale;

            return this;
        }

        public Transform build() {
            return new Transform(this.translation, rotationQuat(), this.scale);
        }

        private Quaternionf rotationQuat() {
            float yaw = rotation.y;
            float pitch = rotation.x;
            float roll = rotation.z;

            return new Quaternionf().rotateXYZ(pitch, yaw, roll); // TODO make sure this is right rotation order
        }

    }
}
