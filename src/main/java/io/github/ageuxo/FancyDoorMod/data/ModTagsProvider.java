package io.github.ageuxo.FancyDoorMod.data;

import io.github.ageuxo.FancyDoorMod.FancyDoorsMod;
import io.github.ageuxo.FancyDoorMod.adastra.SlidingDoorBlock;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

public class ModTagsProvider {

    public static final List<RegistryObject<? extends SlidingDoorBlock<? extends Enum<? extends Enum<?>>>>> DOORS = List.of(
            FancyDoorsMod.IRON_DOUBLE_2X3_SLIDING_DOOR,
            FancyDoorsMod.IRON_DOUBLE_3X3_SLIDING_DOOR,
            FancyDoorsMod.IRON_SINGLE_3X3_SLIDING_DOOR,
            FancyDoorsMod.DOUBLE_3X3_SLIDING_DOOR,
            FancyDoorsMod.DOUBLE_3X3_CAUTION_SLIDING_DOOR,
            FancyDoorsMod.PORTCULLIS_FLAT_BLOCK,
            FancyDoorsMod.PORTCULLIS_FULL_BLOCK,
            FancyDoorsMod.PORTCULLIS_ALIGNED_FLAT_BLOCK,
            FancyDoorsMod.PORTCULLIS_ALIGNED_FULL_BLOCK
    );
    public final BlockTags blockTags;
    public final ItemTags itemTags;

    public ModTagsProvider(boolean run, DataGenerator generator, CompletableFuture<HolderLookup.Provider> lookup, ExistingFileHelper fileHelper) {
        var output = generator.getPackOutput();
        blockTags = new BlockTags(output, lookup, FancyDoorsMod.MOD_ID, fileHelper);
        generator.addProvider(run, blockTags);
        itemTags = new ItemTags(output, lookup, blockTags.contentsGetter(), FancyDoorsMod.MOD_ID, fileHelper);
        generator.addProvider(run, itemTags);
    }


    public static class BlockTags extends BlockTagsProvider {

        public BlockTags(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, String modId, @Nullable ExistingFileHelper existingFileHelper) {
            super(output, lookupProvider, modId, existingFileHelper);
        }

        @Override
        protected void addTags(HolderLookup.@NotNull Provider pProvider) {
            var pickaxeMinable = tag(net.minecraft.tags.BlockTags.MINEABLE_WITH_PICKAXE);
            FancyDoorsMod.BLOCKS.getEntries().stream().map(Supplier::get).forEach(pickaxeMinable::add);

            var needsIron = tag(net.minecraft.tags.BlockTags.NEEDS_IRON_TOOL);
            DOORS.stream().map(Supplier::get).forEach(needsIron::add);

        }
    }

    public static class ItemTags extends ItemTagsProvider {

        public ItemTags(PackOutput pOutput, CompletableFuture<HolderLookup.Provider> pLookupProvider, CompletableFuture<TagLookup<Block>> pBlockTags, String modId, @Nullable ExistingFileHelper existingFileHelper) {
            super(pOutput, pLookupProvider, pBlockTags, modId, existingFileHelper);
        }

        @Override
        protected void addTags(HolderLookup.@NotNull Provider pProvider) {

        }
    }

}
