package io.github.ageuxo.FancyDoorMod;

import io.github.ageuxo.FancyDoorMod.adastra.SlidingDoorBlock;
import io.github.ageuxo.FancyDoorMod.adastra.SlidingDoorBlockEntity;
import io.github.ageuxo.FancyDoorMod.adastra.SlidingDoorBlockEntityRenderer;
import io.github.ageuxo.FancyDoorMod.adastra.Double2x3SlidingDoorBlock;
import io.github.ageuxo.FancyDoorMod.block.SingleSlidingDoorBlock;
import io.github.ageuxo.FancyDoorMod.block.entity.SingleSlidingDoorBlockEntity;
import io.github.ageuxo.FancyDoorMod.render.SingleSlidingDoorBERenderer;
import net.minecraft.ChatFormatting;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.DataGenerator;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

@Mod(FancyDoorsMod.MOD_ID)
public class FancyDoorsMod {
    public static final String MOD_ID = "fancydoors";

    @SuppressWarnings("deprecation")
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(BuiltInRegistries.BLOCK.key(), MOD_ID);
    public static final RegistryObject<Block> IRON_DOUBLE_3X3_SLIDING_DOOR = BLOCKS.register("iron_double_3x3_sliding_door",
            ()->new SlidingDoorBlock(BlockBehaviour.Properties.copy(Blocks.IRON_DOOR).explosionResistance(6).mapColor(MapColor.METAL)));
    public static final RegistryObject<Block> IRON_SINGLE_3X3_SLIDING_DOOR = BLOCKS.register("iron_single_3x3_sliding_door",
            ()->new SingleSlidingDoorBlock(BlockBehaviour.Properties.copy(Blocks.IRON_DOOR).explosionResistance(6).mapColor(MapColor.METAL)));
    public static final RegistryObject<Block> IRON_DOUBLE_2X3_SLIDING_DOOR = BLOCKS.register("iron_double_2x3_sliding_door",
            ()->new Double2x3SlidingDoorBlock(BlockBehaviour.Properties.copy(Blocks.IRON_DOOR).explosionResistance(6).mapColor(MapColor.METAL)));

    @SuppressWarnings("deprecation")
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(BuiltInRegistries.ITEM.key(), MOD_ID);

    @SuppressWarnings("deprecation")
    public static final DeferredRegister<BlockEntityType<?>> BE_TYPES = DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE.key(), MOD_ID);
    public static final RegistryObject<BlockEntityType<SlidingDoorBlockEntity>> DOUBLE_3X3_SLIDING_DOOR_BE = BE_TYPES.register("double_sliding_door",
            ()-> registerBlockEntityType(SlidingDoorBlockEntity::new, IRON_DOUBLE_3X3_SLIDING_DOOR.get(), IRON_DOUBLE_2X3_SLIDING_DOOR.get()));
    public static final RegistryObject<BlockEntityType<SlidingDoorBlockEntity>> SINGLE_3X3_SLIDING_DOOR_BE = BE_TYPES.register("single_sliding_door",
            ()-> registerBlockEntityType(SingleSlidingDoorBlockEntity::new, IRON_SINGLE_3X3_SLIDING_DOOR.get()));

    @SuppressWarnings("deprecation")
    public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(BuiltInRegistries.SOUND_EVENT.key(), MOD_ID);
    public static final RegistryObject<SoundEvent> WRENCH_SOUND = SOUNDS.register("wrenched", ()-> SoundEvent.createVariableRangeEvent(modRL("wrench")));
    public static final RegistryObject<SoundEvent> SLIDING_DOOR_CLOSE = SOUNDS.register("sliding_door_close", ()-> SoundEvent.createVariableRangeEvent(modRL("sliding_door_close")));
    public static final RegistryObject<SoundEvent> SLIDING_DOOR_OPEN = SOUNDS.register("sliding_door_open", ()-> SoundEvent.createVariableRangeEvent(modRL("sliding_door_open")));

    // Components
    public static final Component DOOR_LOCKED = Component.translatable("fancydoors.sliding.locked");
    public static final Component DOOR_UNLOCKED = Component.translatable("fancydoors.sliding.unlocked");
    public static final Component SLIDING_DOOR_INFO = Component.translatable("fancydoors.sliding.info").withStyle(ChatFormatting.GRAY);

    public FancyDoorsMod() {
        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();

        BLOCKS.register(modBus);

        BLOCKS.getEntries().forEach( (block)->
                ITEMS.register(block.getId().getPath(), ()-> new BlockItem(block.get(), new Item.Properties()))
        );
        ITEMS.register(modBus);
        SOUNDS.register(modBus);
        BE_TYPES.register(modBus);

        modBus.addListener(FancyDoorsMod::onDatagen);
    }

    public static ResourceLocation modRL(String path) {
        return new ResourceLocation(MOD_ID, path);
    }

    public static <BE extends BlockEntity> BlockEntityType<BE> registerBlockEntityType(BlockEntityType.BlockEntitySupplier<BE> supplier, Block... blocks){
        //noinspection DataFlowIssue
        return BlockEntityType.Builder.of(supplier, blocks).build(null);
    }

    public static void onDatagen(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        generator.addProvider(true, new ModBlockStateProvider(generator.getPackOutput(), event.getExistingFileHelper()));
    }

    @Mod.EventBusSubscriber(modid = MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class Client {
        @SubscribeEvent
        public static void onRegisterRenderers(EntityRenderersEvent.RegisterRenderers event){
            event.registerBlockEntityRenderer(DOUBLE_3X3_SLIDING_DOOR_BE.get(), SlidingDoorBlockEntityRenderer::new);
            event.registerBlockEntityRenderer(SINGLE_3X3_SLIDING_DOOR_BE.get(), SingleSlidingDoorBERenderer::new);
        }
    }

}
