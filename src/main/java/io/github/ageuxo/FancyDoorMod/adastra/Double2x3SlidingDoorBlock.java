package io.github.ageuxo.FancyDoorMod.adastra;

import io.github.ageuxo.FancyDoorMod.FancyDoorsMod;
import io.github.ageuxo.FancyDoorMod.block.DoorPart;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Locale;

/*

    This class is in the Ad Astra package because most of it is a direct copy of SlidingDoorBlock
    The PART constant replacing the SlidingDoorBlock.PART constant used in the super class

 */
@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class Double2x3SlidingDoorBlock extends SlidingDoorBlock implements Wrenchable {
    public static final EnumProperty<Sliding2x3DoorPart> PART = EnumProperty.create("part", Sliding2x3DoorPart.class);

    public Double2x3SlidingDoorBlock(Properties properties) {
        super(properties);
        registerDefaultState(this.getStateDefinition().any()
                .setValue(PART, Sliding2x3DoorPart.BOTTOM_RIGHT)
                .setValue(SlidingDoorBlock.FACING, Direction.NORTH)
                .setValue(SlidingDoorBlock.POWERED, false)
                .setValue(SlidingDoorBlock.OPEN, false)
                .setValue(SlidingDoorBlock.LOCKED, false)
        );
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(PART, SlidingDoorBlock.FACING, SlidingDoorBlock.OPEN, SlidingDoorBlock.LOCKED, SlidingDoorBlock.POWERED);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return pState.getValue(PART).isController() ? new SlidingDoorBlockEntity(pPos, pState) : null;
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        var controllerPos = getController(state, pos);
        var controllerState = level.getBlockState(controllerPos);
        if (controllerState.isAir()) return InteractionResult.PASS;
        boolean locked = controllerState.getValue(LOCKED);
        if (level.isClientSide()) return locked ? InteractionResult.PASS : InteractionResult.SUCCESS;
        if (locked) return InteractionResult.PASS;

        // set all parts to the same state
        var direction = state.getValue(FACING).getClockWise();
        for (var part : Sliding2x3DoorPart.values()) {
            var partPos = controllerPos.relative(direction, part.xOffset()).above(part.yOffset());
            var partState = level.getBlockState(partPos);
            level.setBlockAndUpdate(partPos, partState.cycle(OPEN));
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
        var direction = state.getValue(FACING).getClockWise();
        for (var part : Sliding2x3DoorPart.values()) {
            var partPos = pos.relative(direction.getOpposite(), part.xOffset()).above(part.yOffset());
            level.setBlock(partPos, state.setValue(PART, part), Block.UPDATE_CLIENTS);
        }
    }

    @Override
    public void neighborChanged(BlockState state, Level level, BlockPos pos, Block neighborBlock, BlockPos neighborPos, boolean movedByPiston) {
        if (!level.isClientSide()) {
            BlockPos controllerPos = getController(state, pos);
            BlockState controllerState = level.getBlockState(controllerPos);
            if (controllerState.getBlock() instanceof SlidingDoorBlock) {
                level.setBlock(controllerPos, controllerState.setValue(POWERED, level.hasNeighborSignal(pos)), Block.UPDATE_CLIENTS);
            }
        }
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        if (!Block.canSupportRigidBlock(level, pos.below())) return false;
        Direction direction = state.getValue(FACING).getClockWise();
        for (var part : Sliding2x3DoorPart.values()) {
            BlockPos offset = pos.relative(direction, part.xOffset()).above(part.yOffset());
            if (!level.getBlockState(offset).isAir()) return false;
        }
        return true;
    }

    @Override
    public void playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
        destroy(level, pos, state);
        super.playerWillDestroy(level, pos, state, player);
    }

    @Override
    public void wasExploded(Level level, BlockPos pos, Explosion explosion) {
        if (!level.isClientSide()) {
            for (var direction : Direction.values()) {
                BlockPos offset = pos.relative(direction);
                BlockState state = level.getBlockState(offset);
                if (state.getBlock().equals(this)) {
                    destroy(level, offset, state);
                    break;
                }
            }
        }
        super.wasExploded(level, pos, explosion);
    }

    private void destroy(Level level, BlockPos pos, BlockState state) {
        var direction = state.getValue(FACING).getClockWise();
        var controllerPos = getController(state, pos);
        for (var part : Sliding2x3DoorPart.values()) {
            var partPos = controllerPos.relative(direction, part.xOffset()).above(part.yOffset());
            level.destroyBlock(partPos, true);
        }
    }

    private BlockPos getController(BlockState state, BlockPos pos) {
        var part = state.getValue(PART);
        var direction = state.getValue(FACING).getClockWise();
        return pos.relative(direction.getOpposite(), -part.xOffset()).below(part.yOffset());
    }

    @Override
    public void onWrench(Level level, BlockPos pos, BlockState state, Direction side, Player user, Vec3 hitPos) {
        var controllerPos = getController(state, pos);
        var direction = state.getValue(FACING).getClockWise();
        for (var part : Sliding2x3DoorPart.values()) {
            var partPos = controllerPos.relative(direction, part.xOffset()).above(part.yOffset());
            var partState = level.getBlockState(partPos);
            level.setBlockAndUpdate(partPos, partState.cycle(LOCKED));

            if (partState.getValue(LOCKED)) { // FancyDoors: Change location of components
                user.displayClientMessage(FancyDoorsMod.DOOR_UNLOCKED, true);
            } else {
                user.displayClientMessage(FancyDoorsMod.DOOR_LOCKED, true);
            }
        } // FancyDoors: Change location of wrench sound
        level.playSound(null, pos, FancyDoorsMod.WRENCH_SOUND.get(), SoundSource.BLOCKS, 1, level.random.nextFloat() * 0.2f + 0.9f);

    }

    public enum Sliding2x3DoorPart implements StringRepresentable, DoorPart<Sliding2x3DoorPart> {
        TOP_RIGHT(0, 2),
        TOP_LEFT(-1, 2),
        MIDDLE_RIGHT(0, 1),
        MIDDLE_LEFT(-1, 1),
        BOTTOM_RIGHT(0, 0),
        BOTTOM_LEFT(-1, 0);

        private final int xOffset;
        private final int yOffset;

        Sliding2x3DoorPart(int xOffset, int yOffset) {
            this.xOffset = xOffset;
            this.yOffset = yOffset;
        }

        public int xOffset() {
            return xOffset;
        }

        public int yOffset() {
            return yOffset;
        }

        public boolean isController() {
            return this == BOTTOM_RIGHT;
        }

        @Override
        public String getSerializedName() {
            return this.name().toLowerCase(Locale.ROOT);
        }

    }
}
