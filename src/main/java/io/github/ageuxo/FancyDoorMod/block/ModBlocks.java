package io.github.ageuxo.FancyDoorMod.block;

import io.github.ageuxo.FancyDoorMod.FancyDoorsMod;
import io.github.ageuxo.FancyDoorMod.adastra.SlidingDoorBlock;
import io.github.ageuxo.FancyDoorMod.adastra.SlidingDoorPartProperty;
import io.github.ageuxo.FancyDoorMod.block.parts.DoorPart;
import io.github.ageuxo.FancyDoorMod.block.parts.DoorPart2x3;
import io.github.ageuxo.FancyDoorMod.block.parts.DoorParts;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;

public class ModBlocks {
    @SuppressWarnings("deprecation")
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(BuiltInRegistries.BLOCK.key(), FancyDoorsMod.MOD_ID);
    public static final RegistryObject<Block> DETECTOR_BLOCK = BLOCKS.register("detector",
            () -> new DetectorBlock(BlockBehaviour.Properties.of().strength(0.5f).mapColor(MapColor.COLOR_GRAY)));
    public static final RegistryObject<SlidingDoorBlock<SlidingDoorPartProperty>> PORTCULLIS_ALIGNED_FULL_BLOCK = BLOCKS.register("portcullis_aligned_full",
            () -> alignedSlidingDoor(DoorParts.PORTCULLIS_3X3));
    public static final RegistryObject<SlidingDoorBlock<SlidingDoorPartProperty>> PORTCULLIS_ALIGNED_FLAT_BLOCK = BLOCKS.register("portcullis_aligned_flat",
            () -> alignedSlidingDoor(DoorParts.PORTCULLIS_3X3));
    public static final RegistryObject<SlidingDoorBlock<SlidingDoorPartProperty>> PORTCULLIS_FULL_BLOCK = BLOCKS.register("portcullis_full",
            () -> slidingDoor(DoorParts.PORTCULLIS_3X3));
    public static final RegistryObject<SlidingDoorBlock<SlidingDoorPartProperty>> PORTCULLIS_FLAT_BLOCK = BLOCKS.register("portcullis_flat",
            () -> slidingDoor(DoorParts.PORTCULLIS_3X3));
    public static final RegistryObject<SlidingDoorBlock<SlidingDoorPartProperty>> DOUBLE_3X3_CAUTION_SLIDING_DOOR = BLOCKS.register("double_3x3_caution_sliding_door",
            () -> slidingDoor(DoorParts.DOUBLE_3X3));
    public static final RegistryObject<SlidingDoorBlock<SlidingDoorPartProperty>> DOUBLE_3X3_SLIDING_DOOR = BLOCKS.register("double_3x3_sliding_door",
            () -> slidingDoor(DoorParts.DOUBLE_3X3));
    /*public static final RegistryObject<Block> IRON_SINGLE_2X3_SLIDING_DOOR = BLOCKS.register("iron_single_2x3_sliding_door",
                ()-> slidingDoor(DoorParts.PARTS_2X3));*/
    public static final RegistryObject<SlidingDoorBlock<DoorPart2x3>> IRON_DOUBLE_2X3_SLIDING_DOOR = BLOCKS.register("iron_double_2x3_sliding_door",
            () -> slidingDoor(DoorParts.DOUBLE_2X3));
    public static final RegistryObject<SlidingDoorBlock<SlidingDoorPartProperty>> IRON_SINGLE_3X3_SLIDING_DOOR = BLOCKS.register("iron_single_3x3_sliding_door",
            () -> slidingDoor(DoorParts.SINGLE_3X3));
    public static final RegistryObject<SlidingDoorBlock<SlidingDoorPartProperty>> IRON_DOUBLE_3X3_SLIDING_DOOR = BLOCKS.register("iron_double_3x3_sliding_door",
            () -> slidingDoor(DoorParts.DOUBLE_3X3));
    public static final RegistryObject<SlidingDoorBlock<SlidingDoorPartProperty>> DOUBLE_LAYERED = BLOCKS.register("double_layered",
            () -> slidingDoor(DoorParts.MULTIPART));

    private static <P extends Enum<P> & DoorPart & StringRepresentable> @NotNull SlidingDoorBlock<P> slidingDoor(DoorParts<P> doorParts) {
        return new SlidingDoorBlock<>(doorParts, BlockBehaviour.Properties.copy(Blocks.IRON_DOOR).explosionResistance(6).mapColor(MapColor.METAL)){
            @Override
            protected void createBlockStateDefinition(StateDefinition.@NotNull Builder<Block, BlockState> builder) {
                super.createBlockStateDefinition(builder);
                builder.add(doorParts.property());
            }
        };
    }

    private static <P extends Enum<P> & DoorPart & StringRepresentable> @NotNull AlignedSlidingDoorBlock<P> alignedSlidingDoor(DoorParts<P> doorParts) {
        return new AlignedSlidingDoorBlock<>(doorParts, BlockBehaviour.Properties.copy(Blocks.IRON_DOOR).explosionResistance(6).mapColor(MapColor.METAL)){
            @Override
            protected void createBlockStateDefinition(StateDefinition.@NotNull Builder<Block, BlockState> builder) {
                super.createBlockStateDefinition(builder);
                builder.add(doorParts.property());
            }
        };
    }
}
