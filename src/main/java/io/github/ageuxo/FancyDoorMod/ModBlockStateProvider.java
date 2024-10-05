package io.github.ageuxo.FancyDoorMod;

import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.BlockModelBuilder;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.BiConsumer;

public class ModBlockStateProvider extends BlockStateProvider {

    public ModBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, FancyDoorsMod.MOD_ID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        sizedBlockWithItem("3x3", this::singleSlidingDoor, FancyDoorsMod.IRON_SINGLE_3X3_SLIDING_DOOR);
        sizedBlockWithItem("3x3", this::doubleSlidingDoor, FancyDoorsMod.IRON_DOUBLE_3X3_SLIDING_DOOR);

//        sizedBlockWithItem("2x3", this::singleSlidingDoor, FancyDoorsMod.IRON_SINGLE_2X3_SLIDING_DOOR);
//        sizedBlockWithItem("2x3", this::doubleSlidingDoor, FancyDoorsMod.IRON_DOUBLE_2X3_SLIDING_DOOR);

        alLVariantsExistingWithItem(FancyDoorsMod.DOUBLE_3X3_SLIDING_DOOR, "block/sliding_doors/double_3x3");
        alLVariantsExistingWithItem(FancyDoorsMod.DOUBLE_3X3_CAUTION_SLIDING_DOOR, "block/sliding_doors/double_3x3_caution");
    }

    public void alLVariantsExistingWithItem(RegistryObject<Block> blockObj, String path) {
        ModelFile.ExistingModelFile model = models().getExistingFile(FancyDoorsMod.modRL(path));
        allVariants(blockObj, model);
        simpleBlockItem(blockObj.get(), model);
    }

    public void allVariants(RegistryObject<Block> blockObj, ModelFile model) {
        getVariantBuilder(blockObj.get()).forAllStates((state)->
                ConfiguredModel.builder()
                        .modelFile(model)
                        .build());
    }

    public void singleSlidingDoor(String size, RegistryObject<Block> blockObj) {
        String path = blockObj.getId().getPath();
        ResourceLocation texture = modLoc("block/sliding_door/" + path);
        BlockModelBuilder baseModel = models()
                .withExistingParent(path, modLoc("block/single_"+ size + "_sliding_door"))
                .texture("0", texture)
                .texture("particle", texture);

        getVariantBuilder(blockObj.get()).forAllStates((state)->
                ConfiguredModel.builder()
                        .modelFile(baseModel)
                        .build());
    }

    public void doubleSlidingDoor(String size, RegistryObject<Block> blockObj) {
        String path = blockObj.getId().getPath();
        ResourceLocation texture = modLoc("block/sliding_door/" + path);
        BlockModelBuilder baseModel = models()
                .withExistingParent(path, modLoc("block/double_"+ size +"_sliding_door"))
                        .texture("0", texture)
                        .texture("particle", texture);

        getVariantBuilder(blockObj.get()).forAllStates((state)->
                ConfiguredModel.builder()
                        .modelFile(baseModel)
                        .build());
    }

    public void sizedBlockWithItem(String size, BiConsumer<String, RegistryObject<Block>> blockModelGenerator, RegistryObject<Block> blockObj) {
        blockModelGenerator.accept(size, blockObj);
        simpleBlockItem(blockObj.get(), models().getExistingFile(modLoc("block/" + blockObj.getId().getPath())));
    }
}
