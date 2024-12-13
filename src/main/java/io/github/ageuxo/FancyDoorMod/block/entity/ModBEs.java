package io.github.ageuxo.FancyDoorMod.block.entity;

import io.github.ageuxo.FancyDoorMod.FancyDoorsMod;
import io.github.ageuxo.FancyDoorMod.adastra.SlidingDoorBlockEntity;
import io.github.ageuxo.FancyDoorMod.block.ModBlocks;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ModBEs {
    @SuppressWarnings("deprecation")
    public static final DeferredRegister<BlockEntityType<?>> BE_TYPES = DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE.key(), FancyDoorsMod.MOD_ID);

    public static final RegistryObject<BlockEntityType<DetectorBlockEntity>> DETECTOR_BE = BE_TYPES.register("detector",
            () -> registerBlockEntityType(DetectorBlockEntity::new, ModBlocks.DETECTOR_BLOCK.get()));

    public static final RegistryObject<BlockEntityType<SlidingDoorBlockEntity>> PORTCULLIS_BE = BE_TYPES.register("portcullis",
            () -> registerBlockEntityType(PortcullisBlockEntity::new, ModBlocks.PORTCULLIS_FLAT_BLOCK.get(), ModBlocks.PORTCULLIS_FULL_BLOCK.get(), ModBlocks.PORTCULLIS_ALIGNED_FLAT_BLOCK.get(), ModBlocks.PORTCULLIS_ALIGNED_FULL_BLOCK.get()));
    public static final RegistryObject<BlockEntityType<SlidingDoorBlockEntity>> DOUBLE_2X3_SLIDING_DOOR_BE = BE_TYPES.register("double_2x3_sliding_door",
            () -> registerBlockEntityType(Sliding2WideBlockEntity::new, ModBlocks.IRON_DOUBLE_2X3_SLIDING_DOOR.get()));
    public static final RegistryObject<BlockEntityType<SlidingDoorBlockEntity>> SINGLE_SLIDING_DOOR_BE = BE_TYPES.register("single_sliding_door",
            () -> registerBlockEntityType(SingleSlidingDoorBlockEntity::new, ModBlocks.IRON_SINGLE_3X3_SLIDING_DOOR.get()));
    public static final RegistryObject<BlockEntityType<SlidingDoorBlockEntity>> DOUBLE_SLIDING_DOOR_BE = BE_TYPES.register("double_sliding_door",
            () -> registerBlockEntityType(SlidingDoorBlockEntity::new, ModBlocks.IRON_DOUBLE_3X3_SLIDING_DOOR.get(), ModBlocks.DOUBLE_3X3_SLIDING_DOOR.get(), ModBlocks.DOUBLE_3X3_CAUTION_SLIDING_DOOR.get()));

    public static <BE extends BlockEntity> BlockEntityType<BE> registerBlockEntityType(BlockEntityType.BlockEntitySupplier<BE> supplier, Block... blocks) {
        //noinspection DataFlowIssue
        return BlockEntityType.Builder.of(supplier, blocks).build(null);
    }
}
