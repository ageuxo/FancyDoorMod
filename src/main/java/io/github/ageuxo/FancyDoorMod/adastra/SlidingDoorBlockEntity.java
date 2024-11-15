package io.github.ageuxo.FancyDoorMod.adastra;

import io.github.ageuxo.FancyDoorMod.FancyDoorsMod;
import io.github.ageuxo.FancyDoorMod.block.AlignedSlidingDoorBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

public class SlidingDoorBlockEntity extends BlockEntity implements TickableBlockEntity {

    private int slideTicks;
    private int lastSlideTicks;
    public boolean aligned = false; // FancyDoors: add field

    public SlidingDoorBlockEntity(BlockPos pos, BlockState state) {
        this(FancyDoorsMod.DOUBLE_SLIDING_DOOR_BE.get(), pos, state); // FancyDoors: Change location of BE Type
    }

    public SlidingDoorBlockEntity(BlockEntityType<?> pType, BlockPos pPos, BlockState pBlockState) {
        super(pType, pPos, pBlockState);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        slideTicks = tag.getInt("SlideTicks");
    }

    @Override
    public void onLoad() { // FancyDoors: set aligned field
        if (this.getBlockState().getBlock() instanceof AlignedSlidingDoorBlock<?>) {
            this.aligned = true;
        }
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.putInt("SlideTicks", slideTicks);
    }

    public int slideTicks() {
        return slideTicks;
    }

    public int lastSlideTicks() {
        return lastSlideTicks;
    }

    @Override
    public void tick(Level level, long time, BlockState state, BlockPos pos) {
        boolean isOpen = getBlockState().getValue(SlidingDoorBlock.OPEN) || getBlockState().getValue(SlidingDoorBlock.POWERED);
        lastSlideTicks = slideTicks;

        if (!level.isClientSide()) {
            if (!isOpen && slideTicks == 97) {
                level.playSound(null, worldPosition, FancyDoorsMod.SLIDING_DOOR_CLOSE.get(), SoundSource.BLOCKS, 0.25f, 1);
            } else if (isOpen && slideTicks == 3) {
                level.playSound(null, worldPosition, FancyDoorsMod.SLIDING_DOOR_OPEN.get(), SoundSource.BLOCKS, 0.25f, 1);
            }
        }
        slideTicks = Mth.clamp(slideTicks + (isOpen ? 3 : -3), 0, 100);
    }

    @SuppressWarnings("unused")
    public AABB getRenderBoundingBox() {
        return new AABB(this.getBlockPos()).inflate(3);
    }
}
