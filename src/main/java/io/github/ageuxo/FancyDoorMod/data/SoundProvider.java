package io.github.ageuxo.FancyDoorMod.data;

import io.github.ageuxo.FancyDoorMod.FancyDoorsMod;
import net.minecraft.data.PackOutput;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.SoundDefinitionsProvider;
import net.minecraftforge.registries.RegistryObject;

public class SoundProvider extends SoundDefinitionsProvider {

    public SoundProvider(PackOutput output, ExistingFileHelper helper) {
        super(output, FancyDoorsMod.MOD_ID, helper);
    }

    @Override
    public void registerSounds() {

        addSimpleSound(FancyDoorsMod.WRENCH_SOUND, "wrenched");
        addSimpleSound(FancyDoorsMod.SLIDING_DOOR_OPEN, "door.sliding.open");

    }

    protected void addSimpleSound(RegistryObject<SoundEvent> soundEvent, String subtitle) {
        add(soundEvent.get(), definition().subtitle("subtitle."+ FancyDoorsMod.MOD_ID +"."+ subtitle)
                .with(sound(soundEvent.getId())));
    }


}
