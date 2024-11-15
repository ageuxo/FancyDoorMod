package io.github.ageuxo.FancyDoorMod.data;

import io.github.ageuxo.FancyDoorMod.adastra.SlidingDoorBlock;
import io.github.ageuxo.FancyDoorMod.block.parts.DoorPart;
import io.github.ageuxo.FancyDoorMod.block.parts.DoorParts;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

import static io.github.ageuxo.FancyDoorMod.FancyDoorsMod.*;

public class ModBlockLootTables extends BlockLootSubProvider {

    public ModBlockLootTables() {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags());
    }

    @Override
    protected void generate() {
        dropSelf(DETECTOR_BLOCK.get());

        doorPartDrops(IRON_DOUBLE_2X3_SLIDING_DOOR, DoorParts.DOUBLE_2X3);
        doorPartDrops(IRON_DOUBLE_3X3_SLIDING_DOOR, DoorParts.DOUBLE_3X3);
        doorPartDrops(IRON_SINGLE_3X3_SLIDING_DOOR, DoorParts.SINGLE_3X3);
        doorPartDrops(DOUBLE_3X3_SLIDING_DOOR, DoorParts.DOUBLE_3X3);
        doorPartDrops(DOUBLE_3X3_CAUTION_SLIDING_DOOR, DoorParts.DOUBLE_3X3);
        doorPartDrops(PORTCULLIS_FLAT_BLOCK, DoorParts.DOUBLE_3X3);
        doorPartDrops(PORTCULLIS_FULL_BLOCK, DoorParts.DOUBLE_3X3);
        doorPartDrops(PORTCULLIS_ALIGNED_FLAT_BLOCK, DoorParts.DOUBLE_3X3);
        doorPartDrops(PORTCULLIS_ALIGNED_FULL_BLOCK, DoorParts.DOUBLE_3X3);
    }

    protected <T extends Enum<T> & DoorPart & StringRepresentable> void doorPartDrops(RegistryObject<SlidingDoorBlock<T>> blockObj, DoorParts<T> doorParts) {
        LootTable.Builder builder = createSinglePropConditionTable(blockObj.get(), doorParts.property(), doorParts.mainPart());
        this.map.put(blockObj.get().getLootTable(), builder);
    }

    @Override
    protected @NotNull Iterable<Block> getKnownBlocks() {
        return BLOCKS.getEntries().stream().map(RegistryObject::get)::iterator;
    }
}
