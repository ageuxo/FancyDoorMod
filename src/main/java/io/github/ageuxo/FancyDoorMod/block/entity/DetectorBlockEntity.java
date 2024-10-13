package io.github.ageuxo.FancyDoorMod.block.entity;

import com.mojang.logging.LogUtils;
import io.github.ageuxo.FancyDoorMod.FancyDoorsMod;
import io.github.ageuxo.FancyDoorMod.network.CollectionDiff;
import io.github.ageuxo.FancyDoorMod.network.packet.C2SDetectorPacket;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.*;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class DetectorBlockEntity extends BlockEntity {

    public static final Logger LOGGER = LogUtils.getLogger();
    private int xValue = 1;
    private int xMax = 5;
    private int xMin = 1;

    private int yValue = 1;
    private int yMax = 5;
    private int yMin = 1;

    private int zValue = 1;
    private int zMax = 5;
    private int zMin = 1;

    private AABB area;
    private List<ExtraCodecs.TagOrElementLocation> filterList = new ArrayList<>();
    private List<String> filterStrings = new ArrayList<>();
    public boolean renderBox = false;

    public DetectorBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(FancyDoorsMod.DETECTOR_BE.get(), pPos, pBlockState);
    }

    @Override
    public void load(CompoundTag tag) {
        CompoundTag beTag = tag.getCompound("fancydoors:detector");

        if (!beTag.isEmpty()){
            this.xValue = beTag.getInt("xValue");
            this.xMin = beTag.getInt("xMin");
            this.xMax = beTag.getInt("xMax");

            this.yValue = beTag.getInt("yValue");
            this.yMin = beTag.getInt("yMin");
            this.yMax = beTag.getInt("yMax");

            this.zValue = beTag.getInt("zValue");
            this.zMin = beTag.getInt("zMin");
            this.zMax = beTag.getInt("zMax");

            ListTag filters = beTag.getList("filters", Tag.TAG_STRING);
            this.filterList = deserializeFilters(filters);
        }

        super.load(tag);
    }

    private List<ExtraCodecs.TagOrElementLocation> deserializeFilters(ListTag filters) {
        List<ExtraCodecs.TagOrElementLocation> ret = new ArrayList<>();
        List<String> stringList = new ArrayList<>();
        for (Tag tag : filters) {
            String str = tag.getAsString();
            stringList.add(str);
            var rl = new ResourceLocation(str);
            ExtraCodecs.TagOrElementLocation entry;
            if (str.startsWith("#")) {
                entry = new ExtraCodecs.TagOrElementLocation(rl, true);
            } else {
                entry = new ExtraCodecs.TagOrElementLocation(rl, false);
            }
            ret.add(entry);
        }
        this.filterStrings = stringList;
        return ret;
    }

    @Override
    public void onLoad() {
        updateArea();
    }

    @Override
    protected void saveAdditional(CompoundTag pTag) {
        CompoundTag beTag = new CompoundTag();

        beTag.putInt("xValue", this.xValue);
        beTag.putInt("xMin", this.xMin);
        beTag.putInt("xMax", this.xMax);

        beTag.putInt("yValue", this.yValue);
        beTag.putInt("yMin", this.yMin);
        beTag.putInt("yMax", this.yMax);

        beTag.putInt("zValue", this.zValue);
        beTag.putInt("zMin", this.zMin);
        beTag.putInt("zMax", this.zMax);

        List<StringTag> filterList = this.filterList.stream()
                .map(ExtraCodecs.TagOrElementLocation::toString)
                .distinct()
                .map(StringTag::valueOf)
                .toList();

        ListTag filters = new ListTag();
        filters.addAll(filterList);

        beTag.put("filters", filters);

        pTag.put("fancydoors:detector", beTag);
        super.saveAdditional(pTag);
    }

    public void setFromPacket(C2SDetectorPacket packet) {
        int oldX = this.xValue;
        int oldY = this.yValue;
        int oldZ = this.zValue;

        this.xValue = packet.x();
        this.yValue = packet.y();
        this.zValue = packet.z();

        CollectionDiff.handle(packet.diff(), this.filterStrings);

        if (oldX != this.xValue || oldY != this.yValue || oldZ != this.zValue){
            updateArea();
        }

        if (level != null) {
            LOGGER.debug("setBlock");
            level.setBlock(this.getBlockPos(), this.getBlockState(), Block.UPDATE_ALL);
        }

        this.setChanged();
    }

    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag tag = new CompoundTag();
        tag.putInt("x", this.xValue);
        tag.putInt("y", this.yValue);
        tag.putInt("z", this.zValue);
        return tag;
    }

    @Override
    public void handleUpdateTag(CompoundTag tag) {
        this.xValue = tag.getInt("x");
        this.yValue = tag.getInt("y");
        this.zValue = tag.getInt("z");
        updateArea();
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    private void updateArea() {
        Direction facing = getBlockState().getValue(BlockStateProperties.FACING);
        BlockPos.MutableBlockPos pos = this.getBlockPos().mutable().move(facing);
        Direction.Axis axis = facing.getAxis();
        if (axis == Direction.Axis.X) {
            pos.move(facing, this.xValue / 2);
        } else if (axis == Direction.Axis.Y) {
            pos.move(facing, this.yValue / 2);
        } else {
            pos.move(facing, this.zValue / 2);
        }
        this.area = AABB.ofSize(pos.getCenter(), this.xValue, this.yValue, this.zValue);
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

    public List<String> filterStrings() {
        return filterStrings;
    }

    public static void serverTick(Level pLevel, BlockPos pPos, BlockState pState, DetectorBlockEntity be) {
        if (pLevel.getGameTime() % 4 == 0) { // Don't check every tick, for performance
            Predicate<Entity> filter = entity -> {
                EntityType<?> entityType = entity.getType();
                return be.filterList.stream()
                        .anyMatch(entry -> {
                            if (entry.tag()) {
                                //noinspection deprecation
                                return entityType.is(new TagKey<>(BuiltInRegistries.ENTITY_TYPE.key(), entry.id()));
                            } else {
                                //noinspection deprecation
                                if (BuiltInRegistries.ENTITY_TYPE.containsKey(entry.id())) {
                                    //noinspection deprecation
                                    return BuiltInRegistries.ENTITY_TYPE.getKey(entityType).equals(entry.id());
                                } else {
                                    return false;
                                }
                            }
                        });
            };
            List<Entity> entities = pLevel.getEntities(null, be.area);

            if (( be.filterList.isEmpty() && !entities.isEmpty() ) || entities.stream().anyMatch(filter)) {
                pLevel.setBlockAndUpdate(pPos, pState.setValue(BlockStateProperties.POWERED, true));
            } else {
                pLevel.setBlockAndUpdate(pPos, pState.setValue(BlockStateProperties.POWERED, false));
            }
        }
    }

    public AABB area() {
        return area;
    }
}
