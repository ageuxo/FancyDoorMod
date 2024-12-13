package io.github.ageuxo.FancyDoorMod.block.entity;

import io.github.ageuxo.FancyDoorMod.adastra.SlidingDoorBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class Sliding2WideBlockEntity extends SlidingDoorBlockEntity {
    public Sliding2WideBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBEs.DOUBLE_2X3_SLIDING_DOOR_BE.get(), pPos, pBlockState);
    }
}
