package io.github.ageuxo.FancyDoorMod;

import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.BlockModelBuilder;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;

public class ModBlockStateProvider extends BlockStateProvider {

    public ModBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, FancyDoorsMod.MOD_ID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        slidingDoorWithItem(FancyDoorsMod.IRON_SLIDING_DOOR);
        slidingDoorWithItem(FancyDoorsMod.STEEL_SLIDING_DOOR);
        singleSlidingDoor(FancyDoorsMod.SINGLE_SLIDING_DOOR);
    }

    public void singleSlidingDoor(RegistryObject<Block> blockObj) {
        String path = blockObj.getId().getPath();
        ResourceLocation texture = modLoc("block/sliding_door/" + path);
        BlockModelBuilder baseModel = models()
                .withExistingParent(path, modLoc("block/single_sliding_door"))
                .texture("0", texture)
                .texture("particle", texture);

        getVariantBuilder(blockObj.get()).forAllStates((state)->
                ConfiguredModel.builder()
                        .modelFile(baseModel)
                        .build());
    }

    public void slidingDoor(RegistryObject<Block> blockObj) {
        String path = blockObj.getId().getPath();
        ResourceLocation texture = modLoc("block/sliding_door/" + path);
        BlockModelBuilder baseModel = models()
                .withExistingParent(path, modLoc("block/sliding_door"))
                        .texture("0", texture)
                        .texture("particle", texture);

        getVariantBuilder(blockObj.get()).forAllStates((state)->
                ConfiguredModel.builder()
                        .modelFile(baseModel)
                        .build());
    }

    public void slidingDoorWithItem(RegistryObject<Block> blockObj) {
        slidingDoor(blockObj);
        simpleBlockItem(blockObj.get(), models().getExistingFile(modLoc("block/" + blockObj.getId().getPath())));
    }
}
