package io.github.ageuxo.FancyDoorMod.block.entity;

import io.github.ageuxo.FancyDoorMod.FancyDoorsMod;
import io.github.ageuxo.FancyDoorMod.network.packet.C2SDetectorPacket;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class DetectorBlockEntity extends BlockEntity{

    private int xValue = 0;
    private int xMax = 5;
    private int xMin = 0;

    private int yValue = 0;
    private int yMax = 5;
    private int yMin = 0;

    private int zValue = 0;
    private int zMax = 5;
    private int zMin = 0;

    public DetectorBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(FancyDoorsMod.DETECTOR_BE.get(), pPos, pBlockState);
    }

    @Override
    public void load(CompoundTag pTag) {
        this.xValue = pTag.getInt("xValue");
        this.xMin = pTag.getInt("xMin");
        this.xMax = pTag.getInt("xMax");

        this.yValue = pTag.getInt("yValue");
        this.yMin = pTag.getInt("yMin");
        this.yMax = pTag.getInt("yMax");

        this.zValue = pTag.getInt("zValue");
        this.zMin = pTag.getInt("zMin");
        this.zMax = pTag.getInt("zMax");

        super.load(pTag);
    }

    @Override
    protected void saveAdditional(CompoundTag pTag) {
        pTag.putInt("xValue", this.xValue);
        pTag.putInt("xMin", this.xMin);
        pTag.putInt("xMax", this.xMax);

        pTag.putInt("yValue", this.yValue);
        pTag.putInt("yMin", this.yMin);
        pTag.putInt("yMax", this.yMax);

        pTag.putInt("zValue", this.zValue);
        pTag.putInt("zMin", this.zMin);
        pTag.putInt("zMax", this.zMax);
        super.saveAdditional(pTag);
    }

    public void setFromPacket(C2SDetectorPacket packet) {
        this.xValue = packet.x();
        this.yValue = packet.y();
        this.zValue = packet.z();
    }

    public int getXValue() {
        return xValue;
    }

    public int getXMax() {
        return xMax;
    }

    public int getXMin() {
        return xMin;
    }

    public int getYValue() {
        return yValue;
    }

    public int getYMax() {
        return yMax;
    }

    public int getYMin() {
        return yMin;
    }

    public int getZValue() {
        return zValue;
    }

    public int getZMax() {
        return zMax;
    }

    public int getZMin() {
        return zMin;
    }
}
