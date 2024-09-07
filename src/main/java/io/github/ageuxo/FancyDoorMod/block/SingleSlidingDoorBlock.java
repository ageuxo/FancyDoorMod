package io.github.ageuxo.FancyDoorMod.block;

import io.github.ageuxo.FancyDoorMod.adastra.SlidingDoorBlock;
import io.github.ageuxo.FancyDoorMod.block.entity.SingleSlidingDoorBlockEntity;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class SingleSlidingDoorBlock extends SlidingDoorBlock {

    public SingleSlidingDoorBlock(Properties properties) {
        super(properties);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return state.getValue(PART).isController() ? new SingleSlidingDoorBlockEntity(pos, state) : null;
    }
}
