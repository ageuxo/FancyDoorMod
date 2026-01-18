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
}
