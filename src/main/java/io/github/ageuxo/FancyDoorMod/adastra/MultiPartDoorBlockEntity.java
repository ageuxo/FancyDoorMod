package io.github.ageuxo.FancyDoorMod.adastra;

import io.github.ageuxo.FancyDoorMod.block.entity.ModBEs;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class MultiPartDoorBlockEntity extends SlidingDoorBlockEntity {
    public MultiPartDoorBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBEs.MULTIPART.get(), pPos, pBlockState);
    }
}
