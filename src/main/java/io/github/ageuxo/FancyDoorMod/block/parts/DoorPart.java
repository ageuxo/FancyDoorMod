package io.github.ageuxo.FancyDoorMod.block.parts;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.shapes.VoxelShape;

public interface DoorPart {
    VoxelShape THIN_N2S_SHAPE = Block.box(7, 0, 0, 9, 16, 16);
    VoxelShape THIN_E2W_SHAPE = Block.box(0, 0, 7, 16, 16, 9);

    VoxelShape THICK_N2S_SHAPE = Block.box(6, 0, 0, 10, 16, 16);
    VoxelShape THICK_E2W_SHAPE = Block.box(0, 0, 6, 16, 16, 10);

    VoxelShape ALIGNED_THIN_NORTH_SHAPE = Block.box(0, 0, 14, 16, 16, 16);
    VoxelShape ALIGNED_THIN_EAST_SHAPE = Block.box(14, 0, 0, 16, 16, 16);
    VoxelShape ALIGNED_THIN_SOUTH_SHAPE = Block.box(0, 0, 0, 16, 16, 2);
    VoxelShape ALIGNED_THIN_WEST_SHAPE = Block.box(0, 0, 0, 2, 16, 16);

    int xOffset();
    int yOffset();
    boolean isController();
}
