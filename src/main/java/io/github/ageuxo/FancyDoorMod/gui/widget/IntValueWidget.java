package io.github.ageuxo.FancyDoorMod.gui.widget;

import io.github.ageuxo.FancyDoorMod.FancyDoorsMod;
import net.minecraft.ChatFormatting;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.narration.NarratedElementType;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.joml.Vector2i;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class IntValueWidget extends AbstractWidget {
    private static final ResourceLocation TEXTURE = new ResourceLocation("minecraft", "textures/gui/widgets.png");

    private final String label;
    private final int min;
    private final int max;
    private int value;

    private final Button incrementBtn;
    private final Button decrementBtn;
    private final Vector2i midPoint;

    public IntValueWidget(String label, int x, int y, int width, int height, int startValue, int minValue, int maxValue) {
        super(x, y, width, height, Component.empty());
        if (minValue >= maxValue) {
            throw new IllegalArgumentException("minValue has to be less than maxValue!");
        }
        this.label = label;
        this.value = startValue;
        this.min = minValue;
        this.max = maxValue;
        boundValue();

        int buttonMarginWidth = width / 10;
        int buttonMarginHeight = height / 10;
        int buttonSize = ( (width / 3) + (height / 3) ) / 2;

        this.incrementBtn = new Increment(x + buttonMarginWidth, y + buttonMarginHeight, buttonSize, buttonSize);
        this.decrementBtn = new Decrement(x + this.width - buttonMarginWidth - buttonSize, y + buttonMarginHeight, buttonSize, buttonSize);
        this.midPoint = new Vector2i( (this.getX() + this.getX() + this.width ) / 2,
                (this.getY() + this.getY() + (this.height / 2)) / 2);
    }

    private void boundValue() {
        if (this.value > this.max) {
            this.value = this.max;
        } else if (this.value < this.min) {
            this.value = this.min;
        }
    }

    @Override
    public void onClick(double pMouseX, double pMouseY) {
        if (incrementBtn.isMouseOver(pMouseX, pMouseY)) {
            incrementBtn.onClick(pMouseX, pMouseY);
        } else if (decrementBtn.isMouseOver(pMouseX, pMouseY)) {
            decrementBtn.onClick(pMouseX, pMouseY);
        }
    }

    private void increment(Button button) {
        value++;
        boundValue();
    }

    private void decrement(Button button) {
        value--;
        boundValue();
    }

    public int value() {
        return value;
    }

    @Override
    protected void renderWidget(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        pGuiGraphics.blitNineSlicedSized(TEXTURE, this.getX(), this.getY(), this.width, this.height, 7, 200, 19,0, 67, 256, 256);
        this.incrementBtn.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
        this.decrementBtn.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
        //noinspection DataFlowIssue
        pGuiGraphics.drawCenteredString(Minecraft.getInstance().font, String.valueOf(this.value), midPoint.x, midPoint.y, ChatFormatting.DARK_AQUA.getColor());
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput pNarrationElementOutput) {
        pNarrationElementOutput.add(NarratedElementType.HINT, Component.translatable("fancydoors.detector.narrator.widget", this.label, this.value));
    }

    private class Increment extends Button {

        private Increment(int pX, int pY, int pWidth, int pHeight) {
            super(pX, pY, pWidth, pHeight,
                    Component.literal("+").withStyle(ChatFormatting.BOLD),
                    IntValueWidget.this::increment,
                    pMessageSupplier -> Component.empty().append(FancyDoorsMod.DETECTOR_SCREEN_BTN_INCR));
        }

    }

    private class Decrement extends Button{

        private Decrement(int pX, int pY, int pWidth, int pHeight) {
            super(pX, pY, pWidth, pHeight,
                    Component.literal("-").withStyle(ChatFormatting.BOLD),
                    IntValueWidget.this::decrement,
                    pMessageSupplier -> Component.empty().append(FancyDoorsMod.DETECTOR_SCREEN_BTN_DECR));
        }

    }
}
