package io.github.ageuxo.FancyDoorMod;

import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.BlockModelBuilder;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Consumer;

public class ModBlockStateProvider extends BlockStateProvider {

    public ModBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, FancyDoorsMod.MOD_ID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        blockWithItem(this::double3x3SlidingDoor, FancyDoorsMod.IRON_DOUBLE_3X3_SLIDING_DOOR);

        blockWithItem(this::single3x3SlidingDoor, FancyDoorsMod.IRON_SINGLE_3X3_SLIDING_DOOR);
    }

    public void single3x3SlidingDoor(RegistryObject<Block> blockObj) {
        String path = blockObj.getId().getPath();
        ResourceLocation texture = modLoc("block/sliding_door/" + path);
        BlockModelBuilder baseModel = models()
                .withExistingParent(path, modLoc("block/single_3x3_sliding_door"))
                .texture("0", texture)
                .texture("particle", texture);

        getVariantBuilder(blockObj.get()).forAllStates((state)->
                ConfiguredModel.builder()
                        .modelFile(baseModel)
                        .build());
    }

    public void double3x3SlidingDoor(RegistryObject<Block> blockObj) {
        String path = blockObj.getId().getPath();
        ResourceLocation texture = modLoc("block/sliding_door/" + path);
        BlockModelBuilder baseModel = models()
                .withExistingParent(path, modLoc("block/double_3x3_sliding_door"))
                        .texture("0", texture)
                        .texture("particle", texture);

        getVariantBuilder(blockObj.get()).forAllStates((state)->
                ConfiguredModel.builder()
                        .modelFile(baseModel)
                        .build());
    }

    public void blockWithItem(Consumer<RegistryObject<Block>> blockModelGenerator, RegistryObject<Block> blockObj) {
        blockModelGenerator.accept(blockObj);
        simpleBlockItem(blockObj.get(), models().getExistingFile(modLoc("block/" + blockObj.getId().getPath())));
    }
}
