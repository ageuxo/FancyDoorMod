package io.github.ageuxo.FancyDoorMod.model;

import com.google.gson.*;
import com.mojang.logging.LogUtils;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.codecs.UnboundedMapCodec;
import io.github.ageuxo.FancyDoorMod.FancyDoorsMod;
import io.github.ageuxo.FancyDoorMod.model.animation.Transform;
import net.minecraft.client.renderer.block.model.BlockElement;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraftforge.client.model.geometry.IGeometryLoader;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GroupGeometryLoader implements IGeometryLoader<GroupGeometry> {
    public static final GroupGeometryLoader INSTANCE = new GroupGeometryLoader();
    public static final ResourceLocation ID = FancyDoorsMod.modRL("group");
    public static final UnboundedMapCodec<Integer, Transform> FRAME_TRANSFORM_CODEC = Codec.unboundedMap(Codec.INT, Transform.CODEC);
    public static final Logger LOGGER = LogUtils.getLogger();

    private GroupGeometryLoader() { }

    @Override
    public GroupGeometry read(JsonObject jsonObject, JsonDeserializationContext deserializationContext) throws JsonParseException {
        List<UnbakedGroup> groups = new ArrayList<>();

        for (var entry : GsonHelper.getAsJsonObject(jsonObject, "groups").asMap().entrySet()) {
            groups.add(resolveGroup(deserializationContext, entry.getKey(), entry.getValue().getAsJsonObject()));
        }

        return new GroupGeometry(groups);
    }

    protected UnbakedGroup resolveGroup(JsonDeserializationContext context, String name, JsonObject jsonObject) {

        List<BlockElement> elements = new ArrayList<>();
        for (JsonElement element : GsonHelper.getAsJsonArray(jsonObject, "elements")) {
            elements.add(context.deserialize(element, BlockElement.class));
        }
        Map<Integer, Transform> frameTransforms = FRAME_TRANSFORM_CODEC
                .parse(JsonOps.INSTANCE, GsonHelper.getAsJsonObject(jsonObject, "keyframes"))
                .getOrThrow(true, LOGGER::warn);

        return new UnbakedGroup(name, elements, frameTransforms);
    }

}
