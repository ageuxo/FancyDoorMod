package io.github.ageuxo.FancyDoorMod;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;

public class Utils {
    public static final Codec<Float> FLOAT_KEY = Codec.STRING.comapFlatMap(
            s -> {
                try {
                    return DataResult.success(Float.valueOf(s));
                } catch (NumberFormatException e) {
                    return DataResult.error(()-> s + " is not a float.");
                }
            }, Object::toString);

}
