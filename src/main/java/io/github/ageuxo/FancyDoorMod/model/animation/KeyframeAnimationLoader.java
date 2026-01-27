package io.github.ageuxo.FancyDoorMod.model.animation;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.mojang.logging.LogUtils;
import com.mojang.serialization.JsonOps;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.slf4j.Logger;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class KeyframeAnimationLoader extends SimpleJsonResourceReloadListener {
    private static final Logger LOGGER = LogUtils.getLogger();

    public static final KeyframeAnimationLoader INSTANCE = new KeyframeAnimationLoader();

    private final Map<ResourceLocation, AnimationSet> animationSets = new HashMap<>();
    private final Map<BlockState, AnimationSet> stateLookupCache = new HashMap<>();

    private KeyframeAnimationLoader() {
        super(new Gson(), "animations");
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> jsons, ResourceManager resourceManager, ProfilerFiller profiler) {
        clear();
        int loadedAnims = 0;

        profiler.push("FancyDoors KeyframeAnimationLoader");

        for (var entry : jsons.entrySet()) {
            try {
                AnimationSet animationSet = new AnimationSet(entry.getKey(),
                        AnimationSet.ANIMATIONS_CODEC.parse(JsonOps.INSTANCE, GsonHelper.getAsJsonObject(entry.getValue().getAsJsonObject(), "animations"))
                                .getOrThrow(false, JsonParseException::new)
                );

                this.animationSets.put(entry.getKey(), animationSet);

                loadedAnims += animationSet.animations().size();
            } catch (Exception e) {
                LOGGER.warn("Failed loading AnimationSet {}. ", entry.getKey(), e);
            }
        }

        LOGGER.info("Loaded {} animations in {} AnimationSets.", loadedAnims, animationSets.size());

        profiler.pop();

    }

    protected void clear() {
        this.animationSets.clear();
    }

    public AnimationSet get(ResourceLocation location) {
        return this.animationSets.getOrDefault(location, AnimationSet.EMPTY);
    }

    public AnimationSet get(BlockState state) {
        return this.stateLookupCache.computeIfAbsent(state, blockState -> {
            Optional<ResourceKey<Block>> keyOptional = blockState.getBlockHolder().unwrapKey();
            AnimationSet set;
            if (keyOptional.isPresent()) {
                set = get(keyOptional.get().location());
            } else {
                set = AnimationSet.EMPTY;
                LOGGER.warn("Attempted to look up state with unbound holder!");
            }
            return set;
        });
    }
}
