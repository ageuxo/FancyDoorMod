package io.github.ageuxo.FancyDoorMod;

import io.github.ageuxo.FancyDoorMod.adastra.SlidingDoorBlockEntityRenderer;
import io.github.ageuxo.FancyDoorMod.block.ModBlocks;
import io.github.ageuxo.FancyDoorMod.block.entity.*;
import io.github.ageuxo.FancyDoorMod.data.*;
import io.github.ageuxo.FancyDoorMod.model.GroupGeometryLoader;
import io.github.ageuxo.FancyDoorMod.model.animation.KeyframeAnimationLoader;
import io.github.ageuxo.FancyDoorMod.network.NetRegistry;
import io.github.ageuxo.FancyDoorMod.render.*;
import net.minecraft.ChatFormatting;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.ModelEvent;
import net.minecraftforge.client.event.RegisterClientReloadListenersEvent;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import java.util.List;
import java.util.Set;

@Mod(FancyDoorsMod.MOD_ID)
public class FancyDoorsMod {
    public static final String MOD_ID = "fancydoors";

    @SuppressWarnings("deprecation")
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(BuiltInRegistries.ITEM.key(), MOD_ID);

    @SuppressWarnings("deprecation")
    public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(BuiltInRegistries.SOUND_EVENT.key(), MOD_ID);

    public static final RegistryObject<SoundEvent> WRENCH_SOUND = SOUNDS.register("wrench", ()-> SoundEvent.createVariableRangeEvent(modRL("wrench")));
    public static final RegistryObject<SoundEvent> SLIDING_DOOR_CLOSE = SOUNDS.register("sliding_door_close", ()-> SoundEvent.createVariableRangeEvent(modRL("sliding_door_close")));
    public static final RegistryObject<SoundEvent> SLIDING_DOOR_OPEN = SOUNDS.register("sliding_door_open", ()-> SoundEvent.createVariableRangeEvent(modRL("sliding_door_open")));

    // Components
    public static final Component DOOR_LOCKED = Component.translatable("info.fancydoors.door.locked");
    public static final Component DOOR_UNLOCKED = Component.translatable("info.fancydoors.door.unlocked");

    public static final Component SLIDING_DOOR_INFO = Component.translatable("info.fancydoors.door.usage").withStyle(ChatFormatting.GRAY);
    public static final Component DETECTOR_INFO = Component.translatable("info.fancydoors.detector.usage").withStyle(ChatFormatting.GRAY);

    public static final Component DETECTOR_SCREEN_TITLE = Component.translatable("gui.fancydoors.detector.title");
    public static final Component DETECTOR_SCREEN_BTN_INCR = Component.translatable("gui.fancydoors.detector.narrator.increment");
    public static final Component DETECTOR_SCREEN_BTN_DECR = Component.translatable("gui.fancydoors.detector.narrator.decrement");
    public static final Component DETECTOR_SCREEN_BTN_SUBMIT = Component.translatable("gui.fancydoors.detector.screen.submit");
    public static final Component DETECTOR_SCREEN_RENDER_TOGGLE = Component.translatable("gui.fancydoors.detector.screen.render");

    public static final Component CREATIVE_TAB = Component.translatable("itemGroup.fancydoors.main");

    public FancyDoorsMod() {
        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();

        ModBlocks.BLOCKS.register(modBus);
        // Make BlockItems
        ModBlocks.BLOCKS.getEntries().forEach( (block)->
                ITEMS.register(block.getId().getPath(), ()-> new BlockItem(block.get(), new Item.Properties()))
        );
        ITEMS.register(modBus);
        SOUNDS.register(modBus);
        ModBEs.BE_TYPES.register(modBus);
        ModCreativeModeTabs.TABS.register(modBus);
        NetRegistry.init();

        modBus.addListener(FancyDoorsMod::onDatagen);
    }

    public static ResourceLocation modRL(String path) {
        return new ResourceLocation(MOD_ID, path);
    }

    public static void onDatagen(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput output = generator.getPackOutput();
        ExistingFileHelper fileHelper = event.getExistingFileHelper();
        generator.addProvider(true, new ModBlockStateProvider(output, fileHelper));
        generator.addProvider(true, new ModRecipeProvider(output));
        new ModTagsProvider(true, generator, event.getLookupProvider(), fileHelper);
        generator.addProvider(true, new LootTableProvider(output, Set.of(), List.of(
                new LootTableProvider.SubProviderEntry(ModBlockLootTables::new, LootContextParamSets.BLOCK)
        )));
        generator.addProvider(true, new SoundProvider(output, fileHelper));
    }

    @Mod.EventBusSubscriber(modid = MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class Client {
        @SubscribeEvent
        public static void onRegisterRenderers(EntityRenderersEvent.RegisterRenderers event){
            event.registerBlockEntityRenderer(ModBEs.DOUBLE_SLIDING_DOOR_BE.get(), SlidingDoorBlockEntityRenderer::new);
            event.registerBlockEntityRenderer(ModBEs.DOUBLE_2X3_SLIDING_DOOR_BE.get(), Sliding2WideDoorBERenderer::new);
            event.registerBlockEntityRenderer(ModBEs.SINGLE_SLIDING_DOOR_BE.get(), SingleSlidingDoorBERenderer::new);
            event.registerBlockEntityRenderer(ModBEs.PORTCULLIS_BE.get(), PortcullisRenderer::new);
            event.registerBlockEntityRenderer(ModBEs.DETECTOR_BE.get(), DetectorBERenderer::new);
            event.registerBlockEntityRenderer(ModBEs.MULTIPART.get(), GroupBERenderer::new);
        }
        @SubscribeEvent
        public static void onRegisterModelLoaders(ModelEvent.RegisterGeometryLoaders event) {
            event.register(GroupGeometryLoader.ID.getPath(), GroupGeometryLoader.INSTANCE);
        }
        @SubscribeEvent
        public static void onRegisterClientReloadListeners(RegisterClientReloadListenersEvent event) {
            event.registerReloadListener(KeyframeAnimationLoader.INSTANCE);
        }
    }

}
