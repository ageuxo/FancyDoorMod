package io.github.ageuxo.FancyDoorMod.block.entity;

import io.github.ageuxo.FancyDoorMod.adastra.SlidingDoorBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class PortcullisBlockEntity extends SlidingDoorBlockEntity {

    public PortcullisBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBEs.PORTCULLIS_BE.get(), pPos, pBlockState);
    }

}
