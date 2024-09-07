package io.github.ageuxo.FancyDoorMod.block.entity;

import io.github.ageuxo.FancyDoorMod.FancyDoorsMod;
import io.github.ageuxo.FancyDoorMod.adastra.SlidingDoorBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class SingleSlidingDoorBlockEntity extends SlidingDoorBlockEntity {

    public SingleSlidingDoorBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(FancyDoorsMod.SINGLE_SLIDING_DOOR_BE.get(), pPos, pBlockState);
    }
}
