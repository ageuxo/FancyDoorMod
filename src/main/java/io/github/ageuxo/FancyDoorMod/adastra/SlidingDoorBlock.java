package io.github.ageuxo.FancyDoorMod.adastra;

import io.github.ageuxo.FancyDoorMod.FancyDoorsMod;
import io.github.ageuxo.FancyDoorMod.block.parts.DoorPart;
import io.github.ageuxo.FancyDoorMod.block.parts.DoorParts;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class SlidingDoorBlock<T extends Enum<T> & DoorPart & StringRepresentable> extends BasicEntityBlock implements Wrenchable {
    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
    public static final BooleanProperty OPEN = BlockStateProperties.OPEN;
    public static final BooleanProperty LOCKED = BlockStateProperties.LOCKED;
    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
    // FancyDoors: Remove PART constant

    private static final VoxelShape NORTH_SHAPE = Block.box(0, 0, 1, 16, 16, 4);
    private static final VoxelShape EAST_SHAPE = Block.box(12, 0, 0, 15, 16, 16);
    private static final VoxelShape SOUTH_SHAPE = Block.box(0, 0, 12, 16, 16, 15);
    private static final VoxelShape WEST_SHAPE = Block.box(1, 0, 0, 4, 16, 16);

    private final DoorParts<T> doorParts; // FancyDoors: Add doorParts field

    public SlidingDoorBlock(DoorParts<T> doorParts, Properties properties) {
        super(properties.pushReaction(PushReaction.BLOCK), true);
        this.doorParts = doorParts; // FancyDoors: Add doorParts field
        registerDefaultState(stateDefinition.any()
                .setValue(FACING, Direction.NORTH)
                .setValue(OPEN, false)
                .setValue(LOCKED, false)
                .setValue(POWERED, false)
                .setValue(doorParts.property(), doorParts.mainPart()));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, OPEN, LOCKED, POWERED, doorParts.property()); // FancyDoors: Replace with doorPart prop
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return switch (state.getValue(FACING)) {
            default -> NORTH_SHAPE;
            case EAST -> EAST_SHAPE;
            case SOUTH -> SOUTH_SHAPE;
            case WEST -> WEST_SHAPE;
        };
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable BlockGetter level, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(FancyDoorsMod.SLIDING_DOOR_INFO); // FancyDoors: remove indirection
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        var controllerState = level.getBlockState(getController(state, pos));
        if (!controllerState.getValues().containsKey(doorParts.property())) { // FancyDoors: Replace with doorPart prop
            return super.getCollisionShape(state, level, pos, context);
        }
        return controllerState.getValue(OPEN) || controllerState.getValue(POWERED) ?
                Shapes.empty() :
                super.getCollisionShape(state, level, pos, context);
    }

    @Override
    public boolean isPathfindable(BlockState state, BlockGetter level, BlockPos pos, PathComputationType type) {
        return switch (type) {
            case LAND, AIR -> state.getValue(OPEN) || state.getValue(POWERED);
            case WATER -> false;
        };
    }

    @Override
    public @NotNull RenderShape getRenderShape(BlockState state) {
        return RenderShape.ENTITYBLOCK_ANIMATED; // Rendering is done in the BER
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rotation) {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(FACING)));
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        return this.defaultBlockState().setValue(FACING, ctx.getHorizontalDirection().getOpposite());
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return state.getValue(doorParts.property()).isController() ? new SlidingDoorBlockEntity(pos, state) : null; // FancyDoors: remove indirection, replace PART with doorPart prop
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
        for (var part : doorParts.property().getPossibleValues()) { // FancyDoors: Replace with doorPart prop
            var partPos = controllerPos.relative(direction, part.xOffset()).above(part.yOffset());
            var partState = level.getBlockState(partPos);
            level.setBlockAndUpdate(partPos, partState.cycle(OPEN));
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
        var direction = state.getValue(FACING).getClockWise();
        for (var part : doorParts.property().getPossibleValues()) { // FancyDoors: Replace with doorPart prop
            var partPos = pos.relative(direction.getOpposite(), part.xOffset()).above(part.yOffset());
            level.setBlock(partPos, state.setValue(doorParts.property(), part), Block.UPDATE_CLIENTS);
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
        for (var part : doorParts.property().getPossibleValues()) { // FancyDoors: Replace with doorPart prop
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
        for (var part : doorParts.property().getPossibleValues()) { // FancyDoors: Replace with doorPart prop
            var partPos = controllerPos.relative(direction, part.xOffset()).above(part.yOffset());
            level.destroyBlock(partPos, true);
        }
    }

    private BlockPos getController(BlockState state, BlockPos pos) {
        var part = state.getValue(doorParts.property()); // FancyDoors: Replace with doorPart prop
        var direction = state.getValue(FACING).getClockWise();
        return pos.relative(direction.getOpposite(), -part.xOffset()).below(part.yOffset());
    }

    @Override
    public void onWrench(Level level, BlockPos pos, BlockState state, Direction side, Player user, Vec3 hitPos) {
        var controllerPos = getController(state, pos);
        var direction = state.getValue(FACING).getClockWise();
        for (var part : doorParts.property().getPossibleValues()) { // FancyDoors: Replace with doorPart prop
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
}