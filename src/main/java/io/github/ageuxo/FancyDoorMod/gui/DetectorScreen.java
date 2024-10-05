package io.github.ageuxo.FancyDoorMod.gui;

import io.github.ageuxo.FancyDoorMod.FancyDoorsMod;
import io.github.ageuxo.FancyDoorMod.gui.widget.IntValueWidget;
import io.github.ageuxo.FancyDoorMod.network.NetRegistry;
import io.github.ageuxo.FancyDoorMod.network.packet.C2SDetectorPacket;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import org.joml.Vector2i;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class DetectorScreen extends Screen {
    private static final ResourceLocation TEXTURE = FancyDoorsMod.modRL("textures/gui/screen/detector.png");
    private final BlockPos pos;
    private IntValueWidget xWidget;
    private IntValueWidget yWidget;
    private IntValueWidget zWidget;
    private Vector2i plateMax;
    private Vector2i plateMin;
    private int plateWidth;
    private int plateHeight;

    private final int initX;
    private final int minX;
    private final int maxX;
    private final int initY;
    private final int minY;
    private final int maxY;
    private final int initZ;
    private final int minZ;
    private final int maxZ;

    public DetectorScreen(BlockPos pos, int initX, int minX, int maxX, int initY, int minY, int maxY, int initZ, int minZ, int maxZ) {
        super(FancyDoorsMod.DETECTOR_SCREEN_TITLE);
        this.pos = pos;
        this.initX = initX;
        this.minX = minX;
        this.maxX = maxX;
        this.initY = initY;
        this.minY = minY;
        this.maxY = maxY;
        this.initZ = initZ;
        this.minZ = minZ;
        this.maxZ = maxZ;
    }

    @Override
    protected void init() {
        this.plateWidth = this.width / 2;
        this.plateHeight = this.height / 2;
        this.plateMin = new Vector2i(width / 4, height / 4);
        this.plateMax = new Vector2i(plateMin).add(plateWidth, plateHeight);

        createWidgets();
    }

    private void sendNewValues() {
        if (xWidget.value() != this.initX || yWidget.value() != this.initY || zWidget.value() != this.initZ) {
            NetRegistry.INSTANCE.sendToServer(new C2SDetectorPacket(this.pos, xWidget.value(), yWidget.value(), zWidget.value()));
        }
    }

    private void createWidgets() {
        // Submit btn
        int btnW = 35;
        int btnH = 12;
        int btnX = plateMax.x - (btnW + 5);
        int btnY = plateMax.y - (btnH + 5);
        Button button = Button.builder(FancyDoorsMod.DETECTOR_SCREEN_BTN_SUBMIT, (btn) -> sendNewValues())
                .pos(btnX, btnY)
                .size(btnW, btnH)
                .createNarration(narr -> MutableComponent.create(FancyDoorsMod.DETECTOR_SCREEN_BTN_SUBMIT.getContents()))
                .build();

        int wWidth = 50;
        int wHeight = 20;
        int heightStep = wHeight + 5;
        int wX = plateMax.x - (wWidth + 10);
        int wY = btnY - heightStep;
        this.zWidget = new IntValueWidget("Z", wX, wY, wWidth, wHeight,
                this.initZ, this.minZ, this.maxZ);
        wY -= heightStep;
        this.yWidget = new IntValueWidget("Y", wX, wY, wWidth, wHeight,
                this.initY, this.minY, this.maxY);
        wY -= heightStep;
        this.xWidget = new IntValueWidget("X", wX, wY, wWidth, wHeight,
                this.initX, this.minX, this.maxX);

        this.addRenderableWidgets(xWidget, yWidget, zWidget, button);
    }

    @SafeVarargs
    private <W extends GuiEventListener & Renderable & NarratableEntry> void addRenderableWidgets(W... widgets) {
        for (W widget : widgets) {
            this.addRenderableWidget(widget);
        }
    }

    @Override
    public void render(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        this.renderBackground(pGuiGraphics);
        pGuiGraphics.blitNineSlicedSized(TEXTURE, this.plateMin.x, this.plateMin.y, this.plateWidth, this.plateHeight,
                7, 27, 27, 0, 0, 27, 27);

        super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
    }

}
