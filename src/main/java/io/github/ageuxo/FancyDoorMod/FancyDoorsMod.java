package io.github.ageuxo.FancyDoorMod;

import io.github.ageuxo.FancyDoorMod.adastra.SlidingDoorBlock;
import io.github.ageuxo.FancyDoorMod.adastra.SlidingDoorBlockEntity;
import io.github.ageuxo.FancyDoorMod.adastra.SlidingDoorBlockEntityRenderer;
import io.github.ageuxo.FancyDoorMod.adastra.SlidingDoorPartProperty;
import io.github.ageuxo.FancyDoorMod.block.DetectorBlock;
import io.github.ageuxo.FancyDoorMod.block.entity.DetectorBlockEntity;
import io.github.ageuxo.FancyDoorMod.block.entity.PortcullisBlockEntity;
import io.github.ageuxo.FancyDoorMod.block.entity.SingleSlidingDoorBlockEntity;
import io.github.ageuxo.FancyDoorMod.block.entity.Sliding2WideBlockEntity;
import io.github.ageuxo.FancyDoorMod.block.parts.DoorPart;
import io.github.ageuxo.FancyDoorMod.block.parts.DoorPart2x3;
import io.github.ageuxo.FancyDoorMod.block.parts.DoorParts;
import io.github.ageuxo.FancyDoorMod.data.*;
import io.github.ageuxo.FancyDoorMod.network.NetRegistry;
import io.github.ageuxo.FancyDoorMod.render.DetectorBERenderer;
import io.github.ageuxo.FancyDoorMod.render.PortcullisRenderer;
import io.github.ageuxo.FancyDoorMod.render.SingleSlidingDoorBERenderer;
import io.github.ageuxo.FancyDoorMod.render.Sliding2WideDoorBERenderer;
import net.minecraft.ChatFormatting;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Set;

@Mod(FancyDoorsMod.MOD_ID)
public class FancyDoorsMod {
    public static final String MOD_ID = "fancydoors";

    @SuppressWarnings("deprecation")
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(BuiltInRegistries.BLOCK.key(), MOD_ID);
    public static final RegistryObject<SlidingDoorBlock<SlidingDoorPartProperty>> IRON_DOUBLE_3X3_SLIDING_DOOR = BLOCKS.register("iron_double_3x3_sliding_door",
            ()-> slidingDoor(DoorParts.DOUBLE_3X3));
    public static final RegistryObject<SlidingDoorBlock<SlidingDoorPartProperty>> IRON_SINGLE_3X3_SLIDING_DOOR = BLOCKS.register("iron_single_3x3_sliding_door",
            ()-> slidingDoor(DoorParts.SINGLE_3X3));

    /*public static final RegistryObject<Block> IRON_SINGLE_2X3_SLIDING_DOOR = BLOCKS.register("iron_single_2x3_sliding_door",
            ()-> slidingDoor(DoorParts.PARTS_2X3));*/
    public static final RegistryObject<SlidingDoorBlock<DoorPart2x3>> IRON_DOUBLE_2X3_SLIDING_DOOR = BLOCKS.register("iron_double_2x3_sliding_door",
            ()-> slidingDoor(DoorParts.DOUBLE_2X3));

    public static final RegistryObject<SlidingDoorBlock<SlidingDoorPartProperty>> DOUBLE_3X3_SLIDING_DOOR = BLOCKS.register("double_3x3_sliding_door",
            ()-> slidingDoor(DoorParts.DOUBLE_3X3));
    public static final RegistryObject<SlidingDoorBlock<SlidingDoorPartProperty>> DOUBLE_3X3_CAUTION_SLIDING_DOOR = BLOCKS.register("double_3x3_caution_sliding_door",
            ()-> slidingDoor(DoorParts.DOUBLE_3X3));

    public static final RegistryObject<SlidingDoorBlock<SlidingDoorPartProperty>> PORTCULLIS_BLOCK = BLOCKS.register("portcullis",
            ()-> slidingDoor(DoorParts.PORTCULLIS_3X3));

    public static final RegistryObject<Block> DETECTOR_BLOCK = BLOCKS.register("detector",
            ()-> new DetectorBlock(BlockBehaviour.Properties.of().strength(0.5f).mapColor(MapColor.COLOR_GRAY)));

    @SuppressWarnings("deprecation")
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(BuiltInRegistries.ITEM.key(), MOD_ID);

    @SuppressWarnings("deprecation")
    public static final DeferredRegister<BlockEntityType<?>> BE_TYPES = DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE.key(), MOD_ID);

    public static final RegistryObject<BlockEntityType<SlidingDoorBlockEntity>> DOUBLE_SLIDING_DOOR_BE = BE_TYPES.register("double_sliding_door",
            ()-> registerBlockEntityType(SlidingDoorBlockEntity::new, IRON_DOUBLE_3X3_SLIDING_DOOR.get(), DOUBLE_3X3_SLIDING_DOOR.get(), DOUBLE_3X3_CAUTION_SLIDING_DOOR.get()));
    public static final RegistryObject<BlockEntityType<SlidingDoorBlockEntity>> SINGLE_SLIDING_DOOR_BE = BE_TYPES.register("single_sliding_door",
            ()-> registerBlockEntityType(SingleSlidingDoorBlockEntity::new, IRON_SINGLE_3X3_SLIDING_DOOR.get()));

    public static final RegistryObject<BlockEntityType<SlidingDoorBlockEntity>> DOUBLE_2X3_SLIDING_DOOR_BE = BE_TYPES.register("double_2x3_sliding_door",
            ()-> registerBlockEntityType(Sliding2WideBlockEntity::new, IRON_DOUBLE_2X3_SLIDING_DOOR.get()));

    public static final RegistryObject<BlockEntityType<SlidingDoorBlockEntity>> PORTCULLIS_BE = BE_TYPES.register("portcullis",
            ()-> registerBlockEntityType(PortcullisBlockEntity::new, PORTCULLIS_BLOCK.get()));

    public static final RegistryObject<BlockEntityType<DetectorBlockEntity>> DETECTOR_BE = BE_TYPES.register("detector",
            ()-> registerBlockEntityType(DetectorBlockEntity::new, DETECTOR_BLOCK.get()));

    @SuppressWarnings("deprecation")
    public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(BuiltInRegistries.SOUND_EVENT.key(), MOD_ID);

    public static final RegistryObject<SoundEvent> WRENCH_SOUND = SOUNDS.register("wrenched", ()-> SoundEvent.createVariableRangeEvent(modRL("wrench")));
    public static final RegistryObject<SoundEvent> SLIDING_DOOR_CLOSE = SOUNDS.register("sliding_door_close", ()-> SoundEvent.createVariableRangeEvent(modRL("sliding_door_close")));
    public static final RegistryObject<SoundEvent> SLIDING_DOOR_OPEN = SOUNDS.register("sliding_door_open", ()-> SoundEvent.createVariableRangeEvent(modRL("sliding_door_open")));

    // Components

    public static final Component DOOR_LOCKED = Component.translatable("fancydoors.sliding.locked");
    public static final Component DOOR_UNLOCKED = Component.translatable("fancydoors.sliding.unlocked");
    public static final Component SLIDING_DOOR_INFO = Component.translatable("fancydoors.sliding.info").withStyle(ChatFormatting.GRAY);
    public static final Component DETECTOR_SCREEN_TITLE = Component.translatable("fancydoors.detector.title");
    public static final Component DETECTOR_SCREEN_BTN_INCR = Component.translatable("fancydoors.detector.narrator.increment");
    public static final Component DETECTOR_SCREEN_BTN_DECR = Component.translatable("fancydoors.detector.narrator.decrement");
    public static final Component DETECTOR_SCREEN_BTN_SUBMIT = Component.translatable("fancydoors.detector.screen.submit");
    public static final Component DETECTOR_SCREEN_RENDER_TOGGLE = Component.translatable("fancydoors.detector.screen.render");
    public static final Component CREATIVE_TAB = Component.translatable("fancydoors.creative_tab");

    public FancyDoorsMod() {
        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();

        BLOCKS.register(modBus);

        BLOCKS.getEntries().forEach( (block)->
                ITEMS.register(block.getId().getPath(), ()-> new BlockItem(block.get(), new Item.Properties()))
        );
        ITEMS.register(modBus);
        SOUNDS.register(modBus);
        BE_TYPES.register(modBus);
        ModCreativeModeTabs.TABS.register(modBus);
        NetRegistry.init();

        modBus.addListener(FancyDoorsMod::onDatagen);
    }

    public static ResourceLocation modRL(String path) {
        return new ResourceLocation(MOD_ID, path);
    }

    public static <BE extends BlockEntity> BlockEntityType<BE> registerBlockEntityType(BlockEntityType.BlockEntitySupplier<BE> supplier, Block... blocks){
        //noinspection DataFlowIssue
        return BlockEntityType.Builder.of(supplier, blocks).build(null);
    }

    private static <P extends Enum<P> & DoorPart & StringRepresentable> @NotNull SlidingDoorBlock<P> slidingDoor(DoorParts<P> doorParts) {
        return new SlidingDoorBlock<>(doorParts, BlockBehaviour.Properties.copy(Blocks.IRON_DOOR).explosionResistance(6).mapColor(MapColor.METAL)){
            @Override
            protected void createBlockStateDefinition(StateDefinition.@NotNull Builder<Block, BlockState> builder) {
                super.createBlockStateDefinition(builder);
                builder.add(doorParts.property());
            }
        };
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
    }

    @Mod.EventBusSubscriber(modid = MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class Client {
        @SubscribeEvent
        public static void onRegisterRenderers(EntityRenderersEvent.RegisterRenderers event){
            event.registerBlockEntityRenderer(DOUBLE_SLIDING_DOOR_BE.get(), SlidingDoorBlockEntityRenderer::new);
            event.registerBlockEntityRenderer(DOUBLE_2X3_SLIDING_DOOR_BE.get(), Sliding2WideDoorBERenderer::new);
            event.registerBlockEntityRenderer(SINGLE_SLIDING_DOOR_BE.get(), SingleSlidingDoorBERenderer::new);
            event.registerBlockEntityRenderer(PORTCULLIS_BE.get(), PortcullisRenderer::new);
            event.registerBlockEntityRenderer(DETECTOR_BE.get(), DetectorBERenderer::new);
        }
    }

}
