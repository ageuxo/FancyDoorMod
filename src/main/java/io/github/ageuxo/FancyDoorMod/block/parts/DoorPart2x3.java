package io.github.ageuxo.FancyDoorMod.block.parts;

import net.minecraft.util.StringRepresentable;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;

public enum DoorPart2x3 implements StringRepresentable, DoorPart {
    TOP_LEFT(-1, 2),
    TOP_RIGHT(0, 2),
    MIDDLE_LEFT(-1, 1),
    MIDDLE_RIGHT(0, 1),
    BOTTOM_LEFT(-1, 0),
    BOTTOM_RIGHT(0, 0);

    private final int xOffset;
    private final int yOffset;

    DoorPart2x3(int xOffset, int yOffset) {
        this.xOffset = xOffset;
        this.yOffset = yOffset;
    }

    public int xOffset() {
        return xOffset;
    }

    public int yOffset() {
        return yOffset;
    }

    public boolean isController() {
        return this == BOTTOM_RIGHT;
    }

    @Override
    public @NotNull String getSerializedName() {
        return this.name().toLowerCase(Locale.ROOT);
    }

}
