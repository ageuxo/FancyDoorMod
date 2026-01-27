package io.github.ageuxo.FancyDoorMod.model;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.client.renderer.block.model.BlockElement;
import net.minecraft.client.renderer.block.model.BlockElementRotation;
import net.minecraft.util.GsonHelper;
import net.minecraftforge.client.model.ForgeFaceData;
import org.joml.Vector3f;

import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class ExtendedBlockElementDeserializer extends BlockElement.Deserializer {

    public BlockElement read(JsonElement json, JsonDeserializationContext context) {
        JsonObject jsonObj = json.getAsJsonObject();
        Vector3f from = this.getVector3f(jsonObj, "from");
        Vector3f to = this.getVector3f(jsonObj, "to");
        BlockElementRotation rotation = this.getRotation(jsonObj);
        var faces = this.getFaces(context, jsonObj);

        if (jsonObj.has("shade") && !GsonHelper.isBooleanValue(jsonObj, "shade")) {
            throw new JsonParseException("Expected shade to be a Boolean");
        } else {
            boolean flag = GsonHelper.getAsBoolean(jsonObj, "shade", true);
            var faceData = ForgeFaceData.read(jsonObj.get("forge_data"), ForgeFaceData.DEFAULT);
            //noinspection DataFlowIssue
            return new BlockElement(from, to, faces, rotation, flag, faceData);
        }
    }
}
