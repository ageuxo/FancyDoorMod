package io.github.ageuxo.FancyDoorMod.gui.widget;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class ToggleButton extends Button {
    protected final ResourceLocation texture;
    protected final int uSize;
    protected final int vSize;
    protected final int uOffset;
    protected final int vOffset;
    protected final int textureWidth;
    protected final int textureHeight;

    protected boolean pressed;

    public ToggleButton(int x, int y, int width, int height, OnPress onPress, Component message, ResourceLocation texture, int uSize, int vSize, int uOffset, int vOffset, int textureWidth, int textureHeight, boolean startPressed) {
        super(x, y, width, height, message, onPress, DEFAULT_NARRATION);
        setTooltip(Tooltip.create(message));
        this.texture = texture;
        this.uSize = uSize;
        this.vSize = vSize;
        this.uOffset = uOffset;
        this.vOffset = vOffset;
        this.textureWidth = textureWidth;
        this.textureHeight = textureHeight;
        this.pressed = startPressed;
    }

    @Override
    public void onPress() {
        this.pressed = !this.pressed;
        super.onPress();
    }

    @Override
    protected void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        int wOffset = this.uOffset;
        if (this.pressed) {
            wOffset += 20;
        }
        int hOffset = this.vOffset;
        if (this.isHovered) {
            hOffset += 20;
        }

        RenderSystem.enableDepthTest();
        guiGraphics.blit(this.texture, this.getX(), this.getY(), wOffset, hOffset, this.width, this.height, this.textureWidth, this.textureHeight);
    }

    public boolean isPressed() {
        return this.pressed;
    }
}
