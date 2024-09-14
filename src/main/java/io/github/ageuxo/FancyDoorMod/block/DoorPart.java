package io.github.ageuxo.FancyDoorMod.block;

public interface DoorPart<T> {
    int xOffset();
    int yOffset();
    T[] values();
}
