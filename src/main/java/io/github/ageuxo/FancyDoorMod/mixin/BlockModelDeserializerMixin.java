package io.github.ageuxo.FancyDoorMod.mixin;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import io.github.ageuxo.FancyDoorMod.model.GroupGeometryLoader;
import net.minecraft.client.renderer.block.model.BlockElement;
import net.minecraft.client.renderer.block.model.BlockModel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Mixin(BlockModel.Deserializer.class)
public abstract class BlockModelDeserializerMixin {

    @Inject(method = "getElements", at = @At("HEAD"), cancellable = true)
    protected void fancydoors$getElementsInject(JsonDeserializationContext context, JsonObject json, CallbackInfoReturnable<List<BlockElement>> cir) {
        if (json.has("loader") && Objects.equals(json.get("loader").getAsString(), GroupGeometryLoader.ID.toString())) {
            cir.setReturnValue(new ArrayList<>());
        }
    }

}
