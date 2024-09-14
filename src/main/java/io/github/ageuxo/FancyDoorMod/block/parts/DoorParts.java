package io.github.ageuxo.FancyDoorMod.block.parts;

import io.github.ageuxo.FancyDoorMod.adastra.SlidingDoorPartProperty;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.block.state.properties.EnumProperty;

public final class DoorParts<E extends Enum<E> & DoorPart & StringRepresentable> {

    public static final DoorParts<DoorPart2x3> PARTS_2X3 = new DoorParts<>(DoorPart2x3.BOTTOM_RIGHT);
    public static final DoorParts<SlidingDoorPartProperty> PARTS_3X3 = new DoorParts<>(SlidingDoorPartProperty.BOTTOM);

    private final E mainPart;
    private final EnumProperty<E> property;

    public DoorParts(E mainPart) {
        this.mainPart = mainPart;
        Class<E> partClass = (Class<E>) mainPart.getClass();
        this.property = EnumProperty.create(partClass.getName(), partClass);
    }

    public E mainPart() {
        return mainPart;
    }

    public EnumProperty<E> property() {
        return property;
    }


}
