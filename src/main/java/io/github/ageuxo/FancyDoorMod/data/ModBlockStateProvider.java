package io.github.ageuxo.FancyDoorMod.data;

import io.github.ageuxo.FancyDoorMod.FancyDoorsMod;
import io.github.ageuxo.FancyDoorMod.block.ModBlocks;
import net.minecraft.core.Direction;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
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
        sizedBlockWithItem("3x3", this::singleSlidingDoor, ModBlocks.IRON_SINGLE_3X3_SLIDING_DOOR);
        sizedBlockWithItem("3x3", this::doubleSlidingDoor, ModBlocks.IRON_DOUBLE_3X3_SLIDING_DOOR);

//        sizedBlockWithItem("2x3", this::singleSlidingDoor, FancyDoorsMod.IRON_SINGLE_2X3_SLIDING_DOOR);
        sizedBlockWithItem("2x3", this::doubleSlidingDoor, ModBlocks.IRON_DOUBLE_2X3_SLIDING_DOOR);

        portcullis(ModBlocks.PORTCULLIS_FLAT_BLOCK);
        portcullis(ModBlocks.PORTCULLIS_FULL_BLOCK);

        allVariants(ModBlocks.PORTCULLIS_ALIGNED_FLAT_BLOCK, "block/portcullis_flat");
        parentedItem(ModBlocks.PORTCULLIS_ALIGNED_FLAT_BLOCK, "item/portcullis_flat");
        allVariants(ModBlocks.PORTCULLIS_ALIGNED_FULL_BLOCK, "block/portcullis_full");
        parentedItem(ModBlocks.PORTCULLIS_ALIGNED_FULL_BLOCK, "item/portcullis_full");

        alLVariantsExistingWithItem(ModBlocks.DOUBLE_3X3_SLIDING_DOOR, "block/sliding_doors/double_3x3");
        alLVariantsExistingWithItem(ModBlocks.DOUBLE_3X3_CAUTION_SLIDING_DOOR, "block/sliding_doors/double_3x3_caution");

        facingBlock(ModBlocks.DETECTOR_BLOCK, modLoc("block/detector_front"), modLoc("block/detector_side"));
    }

    public void portcullis(RegistryObject<? extends Block> blockObj) {
        getVariantBuilder(blockObj.get()).forAllStates((state)->
                ConfiguredModel.builder()
                        .modelFile(models().getExistingFile(modLoc("block/"+blockObj.getId().getPath())))
                        .build());
    }

    public void facingBlock(RegistryObject<? extends Block> blockObj, ResourceLocation front, ResourceLocation sides) {
        BlockModelBuilder model = models()
                .cube(blockObj.getId().toString(),
                sides, sides, front, sides, sides, sides)
                .texture("particle", sides);

        getVariantBuilder(blockObj.get()).forAllStates(state -> {
            Direction facing = state.getValue(BlockStateProperties.FACING);
            Direction.Axis axis = facing.getAxis();
            boolean v = axis.isVertical();
            int xRot = v ? facing.getAxisDirection() == Direction.AxisDirection.POSITIVE ? -90 : 90 : 0;

            return ConfiguredModel.builder()
                    .modelFile(model)
                    .rotationY((int) (facing.toYRot() + 180) % 360)
                    .rotationX(xRot)
                    .build();
        });
        simpleBlockItem(blockObj.get(), model);
    }

    public void alLVariantsExistingWithItem(RegistryObject<? extends Block> blockObj, String path) {
        ModelFile.ExistingModelFile model = models().getExistingFile(FancyDoorsMod.modRL(path));
        allVariants(blockObj, model);
        simpleBlockItem(blockObj.get(), model);
    }

    public void allVariants(RegistryObject<? extends Block> blockObj, String modelPath) {
        allVariants(blockObj, models().getExistingFile(modLoc(modelPath)));
    }

    public void allVariants(RegistryObject<? extends Block> blockObj, ModelFile model) {
        getVariantBuilder(blockObj.get()).forAllStates((state)->
                ConfiguredModel.builder()
                        .modelFile(model)
                        .build());
    }

    public void parentedItem(RegistryObject<? extends ItemLike> blockObj, String path) {
        itemModels().withExistingParent(blockObj.getId().getPath(), modLoc(path));
    }

    public void singleSlidingDoor(String size, RegistryObject<? extends Block> blockObj) {
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

    public void doubleSlidingDoor(String size, RegistryObject<? extends Block> blockObj) {
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

    public void sizedBlockWithItem(String size, BiConsumer<String, RegistryObject<? extends Block>> blockModelGenerator, RegistryObject<? extends Block> blockObj) {
        blockModelGenerator.accept(size, blockObj);
        simpleBlockItem(blockObj.get(), models().getExistingFile(modLoc("block/" + blockObj.getId().getPath())));
    }
}
