package io.github.ageuxo.FancyDoorMod;

import io.github.ageuxo.FancyDoorMod.block.ModBlocks;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ModCreativeModeTabs {
    public static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(BuiltInRegistries.CREATIVE_MODE_TAB.key(), FancyDoorsMod.MOD_ID);

    public static final RegistryObject<CreativeModeTab> MAIN = TABS.register("main",
            ()-> CreativeModeTab.builder()
                    .title(FancyDoorsMod.CREATIVE_TAB)
                    .icon( ()-> ModBlocks.IRON_DOUBLE_3X3_SLIDING_DOOR.get().asItem().getDefaultInstance() )
                    .displayItems( (params, output) -> FancyDoorsMod.ITEMS.getEntries().forEach(entry -> output.accept(entry.get())) )
                    .build());
}
