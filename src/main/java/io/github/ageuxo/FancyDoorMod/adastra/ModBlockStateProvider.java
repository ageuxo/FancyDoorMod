package io.github.ageuxo.FancyDoorMod.adastra;

import io.github.ageuxo.FancyDoorMod.FancyDoorsMod;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
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
    }

    public void slidingDoor(RegistryObject<Block> blockObj) {
        var block = blockObj.get();
        String name = blockObj.getId().getPath();
        getVariantBuilder(block).forAllStates(state -> {
            ResourceLocation texture = modLoc("block/sliding_door/%s".formatted(name));

            return ConfiguredModel.builder()
                    .modelFile(models().getBuilder(name)
                            .texture("0", texture)
                            .texture("particle", texture)
                            .parent(models().getExistingFile(modLoc("block/sliding_door"))))
                    .build();
        });
    }

    public void slidingDoorWithItem(RegistryObject<Block> blockObj) {
        slidingDoor(blockObj);
        simpleBlockItem(blockObj.get(), models().withExistingParent(blockObj.getId().getPath(), blockObj.getId()));
    }
}
