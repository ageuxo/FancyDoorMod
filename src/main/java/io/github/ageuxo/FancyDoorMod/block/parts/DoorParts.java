package io.github.ageuxo.FancyDoorMod.block.parts;

import io.github.ageuxo.FancyDoorMod.adastra.SlidingDoorBlockEntity;
import io.github.ageuxo.FancyDoorMod.adastra.SlidingDoorPartProperty;
import io.github.ageuxo.FancyDoorMod.block.entity.ModBEs;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraftforge.registries.RegistryObject;

public final class DoorParts<E extends Enum<E> & DoorPart & StringRepresentable> {

    public static final DoorParts<DoorPart2x3> DOUBLE_2X3 = new DoorParts<>(DoorPart2x3.BOTTOM_RIGHT, ModBEs.DOUBLE_2X3_SLIDING_DOOR_BE);
    public static final DoorParts<SlidingDoorPartProperty> DOUBLE_3X3 = new DoorParts<>(SlidingDoorPartProperty.BOTTOM, ModBEs.DOUBLE_SLIDING_DOOR_BE);
    public static final DoorParts<SlidingDoorPartProperty> SINGLE_3X3 = new DoorParts<>(SlidingDoorPartProperty.BOTTOM, ModBEs.SINGLE_SLIDING_DOOR_BE);
    public static final DoorParts<SlidingDoorPartProperty> PORTCULLIS_3X3 = new DoorParts<>(SlidingDoorPartProperty.BOTTOM, ModBEs.PORTCULLIS_BE);
    public static final DoorParts<SlidingDoorPartProperty> MULTIPART = new DoorParts<>(SlidingDoorPartProperty.BOTTOM, ModBEs.MULTIPART);

    private final E mainPart;
    private final RegistryObject<BlockEntityType<SlidingDoorBlockEntity>> beType;
    private final EnumProperty<E> property;

    public DoorParts(E mainPart, RegistryObject<BlockEntityType<SlidingDoorBlockEntity>> beType) {
        this.mainPart = mainPart;
        this.beType = beType;
        Class<E> partClass = (Class<E>) mainPart.getClass();
        this.property = EnumProperty.create("part", partClass);
    }

    public E mainPart() {
        return mainPart;
    }

    public EnumProperty<E> property() {
        return property;
    }


    public RegistryObject<BlockEntityType<SlidingDoorBlockEntity>> beType() {
        return beType;
    }
}
