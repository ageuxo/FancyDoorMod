package io.github.ageuxo.FancyDoorMod.gui;

import io.github.ageuxo.FancyDoorMod.FancyDoorsMod;
import io.github.ageuxo.FancyDoorMod.block.entity.DetectorBlockEntity;
import io.github.ageuxo.FancyDoorMod.gui.widget.IntValueWidget;
import io.github.ageuxo.FancyDoorMod.network.CollectionDiff;
import io.github.ageuxo.FancyDoorMod.network.NetRegistry;
import io.github.ageuxo.FancyDoorMod.network.packet.C2SDetectorPacket;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import org.joml.Vector2i;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.List;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class DetectorScreen extends Screen {
    private static final ResourceLocation TEXTURE = FancyDoorsMod.modRL("textures/gui/screen/detector.png");
    private static final ResourceLocation RENDER_BTN = FancyDoorsMod.modRL("textures/gui/screen/detector.png");
    public static final int RENDER_BTN_IMG_SIZE = 20;

    private final BlockPos pos;
    private IntValueWidget xWidget;
    private IntValueWidget yWidget;
    private IntValueWidget zWidget;
    private Vector2i plateMax;
    private Vector2i plateMin;
    private int plateWidth;
    private int plateHeight;
    private boolean listModified;

    private int oldX;
    private final int minX;
    private final int maxX;
    private int oldY;
    private final int minY;
    private final int maxY;
    private int oldZ;
    private final int minZ;
    private final int maxZ;
    private final List<String> initFilters;
    private final List<String> filters;

    public DetectorScreen(BlockPos pos, int oldX, int minX, int maxX, int oldY, int minY, int maxY, int initZ, int minZ, int maxZ, List<String> filters) {
        super(FancyDoorsMod.DETECTOR_SCREEN_TITLE);
        this.pos = pos;
        this.oldX = oldX;
        this.minX = minX;
        this.maxX = maxX;
        this.oldY = oldY;
        this.minY = minY;
        this.maxY = maxY;
        this.oldZ = initZ;
        this.minZ = minZ;
        this.maxZ = maxZ;
        this.initFilters = filters;
        this.filters = new ArrayList<>(filters);
    }

    @Override
    protected void init() {
        this.plateWidth = this.width / 3;
        this.plateHeight = this.height / 3;
        this.plateMin = new Vector2i(width / 4, height / 4);
        this.plateMax = new Vector2i(plateMin).add(plateWidth, plateHeight);

        createWidgets();
    }

    private void sendNewValues() {
        if (xWidget.value() != this.oldX || yWidget.value() != this.oldY || zWidget.value() != this.oldZ || this.listModified) {
            NetRegistry.INSTANCE.sendToServer(new C2SDetectorPacket(this.pos, xWidget.value(), yWidget.value(), zWidget.value(), new CollectionDiff<>(this.initFilters, this.filters)));
            this.oldX = xWidget.value();
            this.oldY = yWidget.value();
            this.oldZ = zWidget.value();
        }
    }

    private void createWidgets() {
        int widgetWidth = 50;
        int widgetHeight = 20;

        // Submit btn
        int btnW = 35;
        int btnH = 12;

        int wSpace = 0;
        int hSpace = 0;

        // Calc taken space
        wSpace += (widgetWidth) ;
        wSpace += 10; // Margin

        hSpace += (widgetHeight * 3) + Math.max(RENDER_BTN_IMG_SIZE, btnH);
        hSpace += 10; // Margin

        this.plateWidth = wSpace;
        this.plateHeight = hSpace;

        plateMin.x = (this.width / 2) - (wSpace / 2);
        plateMin.y = (this.height / 2) - (hSpace / 2);

        plateMax.x = plateMin.x + wSpace;
        plateMax.y = plateMin.y + hSpace;

        int btnX = plateMax.x - (btnW + 5);
        int btnY = plateMax.y - (btnH + 5);
        Button submitBtn = Button.builder(FancyDoorsMod.DETECTOR_SCREEN_BTN_SUBMIT, (btn) -> sendNewValues())
                .pos(btnX, btnY)
                .size(btnW, btnH)
                .createNarration(narr -> MutableComponent.create(FancyDoorsMod.DETECTOR_SCREEN_BTN_SUBMIT.getContents()))
                .build();

        int chkX = plateMin.x + 5;
        int chkY = plateMax.y - (RENDER_BTN_IMG_SIZE + 5);
        ImageButton renderBtn = new ImageButton(chkX, chkY, RENDER_BTN_IMG_SIZE, RENDER_BTN_IMG_SIZE, 0, 0, RENDER_BTN_IMG_SIZE, RENDER_BTN, this::onPressRenderBtn);

        int heightStep = widgetHeight + 5;
        int wX = plateMax.x - (widgetWidth + 10);
        int wY = btnY - heightStep;
        this.zWidget = new IntValueWidget("Z", wX, wY, widgetWidth, widgetHeight,
                this.oldZ, this.minZ, this.maxZ, 2);
        wY -= heightStep;
        this.yWidget = new IntValueWidget("Y", wX, wY, widgetWidth, widgetHeight,
                this.oldY, this.minY, this.maxY, 2);
        wY -= heightStep;
        this.xWidget = new IntValueWidget("X", wX, wY, widgetWidth, widgetHeight,
                this.oldX, this.minX, this.maxX, 2);

        this.addRenderableWidgets(xWidget, yWidget, zWidget, submitBtn, renderBtn);
    }

    protected void onPressRenderBtn(Button button) {
        if (Minecraft.getInstance().level != null && Minecraft.getInstance().level.getBlockEntity(this.pos) instanceof DetectorBlockEntity be) {
            be.renderBox = !be.renderBox;
        }
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
