package io.github.ageuxo.FancyDoorMod.model;

import com.google.gson.*;
import com.mojang.logging.LogUtils;
import com.mojang.serialization.JsonOps;
import io.github.ageuxo.FancyDoorMod.FancyDoorsMod;
import net.minecraft.client.renderer.block.model.BlockElement;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.GsonHelper;
import net.minecraftforge.client.model.geometry.IGeometryLoader;
import org.joml.Vector3f;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class GroupGeometryLoader implements IGeometryLoader<GroupGeometry> {
    public static final GroupGeometryLoader INSTANCE = new GroupGeometryLoader();
    public static final ResourceLocation ID = FancyDoorsMod.modRL("group");
    public static final Logger LOGGER = LogUtils.getLogger();

    private final ExtendedBlockElementDeserializer elementDeserializer = new ExtendedBlockElementDeserializer();

    private GroupGeometryLoader() { }

    @Override
    public GroupGeometry read(JsonObject jsonObject, JsonDeserializationContext deserializationContext) throws JsonParseException {
        List<BlockElement> allElements = new ArrayList<>();
        for (JsonElement element : GsonHelper.getAsJsonArray(jsonObject, "elements")) {
            allElements.add(elementDeserializer.read(element, deserializationContext));
        }

        return new GroupGeometry(
                parseGroups(jsonObject),
                allElements
        );
    }

    private static List<UnbakedGroup> parseGroups(JsonObject jsonObject) {
        ArrayList<UnbakedGroup> groups = new ArrayList<>();
        for (var entry : GsonHelper.getAsJsonArray(jsonObject, "groups")) {
            groups.add(parseGroup(entry.getAsJsonObject()));
        }

        return groups;
    }

    private static UnbakedGroup parseGroup(JsonObject json) {
        String name = GsonHelper.getAsString(json, "name");
        Vector3f origin = readJsonVector(json.get("origin"));

        List<UnbakedGroup> childGroups = new ArrayList<>();
        List<Integer> elementIndices = new ArrayList<>();

        for (JsonElement childEntry : GsonHelper.getAsJsonArray(json, "children")) {
            if (childEntry.isJsonPrimitive()) { // Is index
                JsonPrimitive primitive = childEntry.getAsJsonPrimitive();
                if (primitive.isNumber()) {
                    elementIndices.add(primitive.getAsInt());
                }
            } else { // Is group
                childGroups.add(parseGroup(childEntry.getAsJsonObject()));
            }
        }

        return new UnbakedGroup(name, origin, childGroups, elementIndices);
    }

    public static Vector3f readJsonVector(JsonElement element) {
        return ExtraCodecs.VECTOR3F.parse(JsonOps.INSTANCE, element)
                .getOrThrow(false, LOGGER::warn);
    }

}
