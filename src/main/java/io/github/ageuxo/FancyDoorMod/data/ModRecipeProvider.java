package io.github.ageuxo.FancyDoorMod.data;

import io.github.ageuxo.FancyDoorMod.FancyDoorsMod;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class ModRecipeProvider extends RecipeProvider {
    private final List<RecipeBuilder> builders = new ArrayList<>();

    public ModRecipeProvider(PackOutput output) {
        super(output);
    }

    @Override
    protected void buildRecipes(Consumer<FinishedRecipe> pWriter) {

        // 2x3
        shapedDoor(FancyDoorsMod.IRON_DOUBLE_2X3_SLIDING_DOOR)
                .define('D', Items.IRON_DOOR)
                .define('I', Items.IRON_INGOT)
                .pattern("II")
                .pattern("DD");

        // 3x3
        shapedDoor(FancyDoorsMod.IRON_DOUBLE_3X3_SLIDING_DOOR)
                .define('D', Items.IRON_DOOR)
                .define('I', Items.IRON_INGOT)
                .pattern("III")
                .pattern("DID")
                .pattern("III");

        shapedDoor(FancyDoorsMod.DOUBLE_3X3_SLIDING_DOOR)
                .define('D', Items.IRON_DOOR)
                .define('I', Items.IRON_INGOT)
                .pattern("III")
                .pattern("DID");

        shapedDoor(FancyDoorsMod.DOUBLE_3X3_CAUTION_SLIDING_DOOR)
                .define('D', Items.IRON_DOOR)
                .define('I', Items.IRON_INGOT)
                .define('Y', Items.YELLOW_DYE)
                .define('B', Items.BLACK_DYE)
                .pattern("III")
                .pattern("DID")
                .pattern("YBY");

        shapedDoor(FancyDoorsMod.IRON_SINGLE_3X3_SLIDING_DOOR)
                .define('D', Items.IRON_DOOR)
                .define('I', Items.IRON_INGOT)
                .pattern("III")
                .pattern("IDI")
                .pattern("III");

        shapedPortcullis(FancyDoorsMod.PORTCULLIS_FLAT_BLOCK)
                .define('P', Items.PISTON)
                .define('T', Items.IRON_TRAPDOOR)
                .define('B', Items.IRON_BARS)
                .pattern("PTP")
                .pattern("BBB")
                .pattern("BBB");

        shapedPortcullis(FancyDoorsMod.PORTCULLIS_FULL_BLOCK)
                .define('P', Items.PISTON)
                .define('T', Items.IRON_TRAPDOOR)
                .define('I', Items.IRON_INGOT)
                .pattern("PTP")
                .pattern("III")
                .pattern("III");


        this.builders.forEach((builder -> builder.save(pWriter)));
    }

    private @NotNull ShapedRecipeBuilder shapedDoor(RegistryObject<? extends Block> block) {
        ShapedRecipeBuilder recipe = ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, block.get())
                .unlockedBy("has_iron_door", InventoryChangeTrigger.TriggerInstance.hasItems(Items.IRON_DOOR));
        builders.add(recipe);
        return recipe;
    }

    private @NotNull ShapedRecipeBuilder shapedPortcullis(RegistryObject<? extends Block> block) {
        ShapedRecipeBuilder recipe = ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, block.get())
                .unlockedBy("has_piston", InventoryChangeTrigger.TriggerInstance.hasItems(Items.PISTON));
        builders.add(recipe);
        return recipe;
    }
}
