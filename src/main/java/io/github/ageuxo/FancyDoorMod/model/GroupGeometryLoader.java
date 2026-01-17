package io.github.ageuxo.FancyDoorMod.model;

import com.google.gson.*;
import io.github.ageuxo.FancyDoorMod.FancyDoorsMod;
import net.minecraft.client.renderer.block.model.BlockElement;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraftforge.client.model.geometry.IGeometryLoader;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;

public class GroupGeometryLoader implements IGeometryLoader<GroupGeometry> {
    public static final GroupGeometryLoader INSTANCE = new GroupGeometryLoader();
    public static final ResourceLocation ID = FancyDoorsMod.modRL("group");

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

        return new UnbakedGroup(name, getVector3f(jsonObject, "offset"), getVector3f(jsonObject, "rotation"), elements);
    }

    protected Vector3f getVector3f(JsonObject jsonObject, String fieldName) {
        JsonArray jsonarray = GsonHelper.getAsJsonArray(jsonObject, fieldName);
        if (jsonarray.size() != 3) {
            throw new JsonParseException("Expected 3 " + fieldName + " values, found: " + jsonarray.size());
        } else {
            float[] floats = new float[3];

            for(int i = 0; i < floats.length; ++i) {
                floats[i] = GsonHelper.convertToFloat(jsonarray.get(i), fieldName + "[" + i + "]");
            }

            return new Vector3f(floats[0], floats[1], floats[2]);
        }
    }
}
