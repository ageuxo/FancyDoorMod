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
        shapedDoor(RecipeCategory.DECORATIONS, FancyDoorsMod.IRON_DOUBLE_2X3_SLIDING_DOOR)
                .define('D', Items.IRON_DOOR)
                .define('I', Items.IRON_INGOT)
                .pattern("II")
                .pattern("DD");

        // 3x3
        shapedDoor(RecipeCategory.DECORATIONS, FancyDoorsMod.IRON_DOUBLE_3X3_SLIDING_DOOR)
                .define('D', Items.IRON_DOOR)
                .define('I', Items.IRON_INGOT)
                .pattern("III")
                .pattern("DID")
                .pattern("III");

        shapedDoor(RecipeCategory.DECORATIONS, FancyDoorsMod.DOUBLE_3X3_SLIDING_DOOR)
                .define('D', Items.IRON_DOOR)
                .define('I', Items.IRON_INGOT)
                .pattern("III")
                .pattern("DID");

        shapedDoor(RecipeCategory.DECORATIONS, FancyDoorsMod.DOUBLE_3X3_CAUTION_SLIDING_DOOR)
                .define('D', Items.IRON_DOOR)
                .define('I', Items.IRON_INGOT)
                .define('Y', Items.YELLOW_DYE)
                .define('B', Items.BLACK_DYE)
                .pattern("III")
                .pattern("DID")
                .pattern("YBY");

        shapedDoor(RecipeCategory.DECORATIONS, FancyDoorsMod.IRON_SINGLE_3X3_SLIDING_DOOR)
                .define('D', Items.IRON_DOOR)
                .define('I', Items.IRON_INGOT)
                .pattern("III")
                .pattern("IDI")
                .pattern("III");


        this.builders.forEach((builder -> builder.save(pWriter)));
    }

    private @NotNull ShapedRecipeBuilder shapedDoor(RecipeCategory category, RegistryObject<? extends Block> block) {
        ShapedRecipeBuilder recipe = ShapedRecipeBuilder.shaped(category, block.get())
                .unlockedBy("has_iron_door", InventoryChangeTrigger.TriggerInstance.hasItems(Items.IRON_DOOR));
        builders.add(recipe);
        return recipe;
    }
}
