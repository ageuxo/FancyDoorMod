package io.github.ageuxo.FancyDoorMod.block;

import io.github.ageuxo.FancyDoorMod.adastra.SlidingDoorBlock;
import io.github.ageuxo.FancyDoorMod.block.parts.DoorPart;
import io.github.ageuxo.FancyDoorMod.block.parts.DoorParts;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

public class AlignedSlidingDoorBlock<T extends Enum<T> & DoorPart & StringRepresentable> extends SlidingDoorBlock<T> {

    public AlignedSlidingDoorBlock(DoorParts<T> doorParts, Properties properties) {
        super(doorParts, properties);
    }

    @Override
    public @NotNull VoxelShape getShape(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        Direction facing = state.getValue(FACING);
        return switch (facing) {
            case EAST -> DoorPart.ALIGNED_THIN_EAST_SHAPE;
            case WEST -> DoorPart.ALIGNED_THIN_WEST_SHAPE;
            case SOUTH -> DoorPart.ALIGNED_THIN_SOUTH_SHAPE;
            default -> DoorPart.ALIGNED_THIN_NORTH_SHAPE;
        };
    }
}
