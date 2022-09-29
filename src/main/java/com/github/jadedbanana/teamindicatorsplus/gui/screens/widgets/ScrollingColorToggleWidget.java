package com.github.jadedbanana.teamindicatorsplus.gui.screens.widgets;

import com.github.jadedbanana.teamindicatorsplus.TeamIndicatorsPlus;
import com.github.jadedbanana.teamindicatorsplus.TeamIndicatorsUtil;
import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.ElementListWidget;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ScrollingColorToggleWidget extends ElementListWidget<ScrollingColorToggleWidget.ColorToggleEntry> {

    public static MinecraftClient client;
    private final int rowWidth;


    /*
    Constructor.
     */
    public ScrollingColorToggleWidget(MinecraftClient client, Screen parent, int width, int height, ColorToggleEntryType type) {
        super(client, width, height, 20, height - 32, 39);

        // Copy over attributes.
        ScrollingColorToggleWidget.client = client;
        this.rowWidth = 110 + type.width;

        // Create entries.
        ArrayList<Formatting> formatList = TeamIndicatorsUtil.getColorFormats();
        for (int i = 0; i < formatList.size(); i++)
            this.addEntry(new ColorToggleEntry(parent, type, formatList.get(i), i));
    }


    /*
    Variable overrides.
     */
    protected int getScrollbarPositionX() {
        return super.getScrollbarPositionX() + 42;
    }
    public int getRowWidth() {
        return this.rowWidth;
    }

    @Override
    public void mouseMoved(double mouseX, double mouseY) {
        super.mouseMoved(mouseX, mouseY);
    }

    @Override
    public boolean keyReleased(int keyCode, int scanCode, int modifiers) { return super.keyReleased(keyCode, scanCode, modifiers); }
    @Override
    public boolean charTyped(char chr, int modifiers) { return super.charTyped(chr, modifiers); }
    @Override
    public void setInitialFocus(@Nullable Element element) { super.setInitialFocus(element); }
    @Override
    public void focusOn(@Nullable Element element) { super.focusOn(element); }
    @Override
    public boolean changeFocus(boolean lookForwards) { return super.changeFocus(lookForwards); }


    /*
    Narration stuff, I don't know what to do with these so I'll do it later I guess
     */
    @Override
    public boolean isNarratable() { return false; }
    @Override
    public void appendNarrations(NarrationMessageBuilder builder) {}

    public static class ColorToggleEntry extends ElementListWidget.Entry<ColorToggleEntry> {

        // Config index, used to grab config info.
        private final int configIndex;
        private final boolean[] configList;
        private boolean enabled;

        // Text stuff. Used to draw the text for the config options.
        private final Text titleText;

        // Image stuff.
        private final Identifier coloredImage;
        private final Identifier defaultImage;
        private int offsetX, offsetY, imageW, imageH;

        // Enable / disable button.
        private final ButtonWidget enableDisableButton;

        // Parent screen.
        public Screen parentScreen;


        /*
        Constructor.
        Creates everything the entry uses.
         */
        public ColorToggleEntry(Screen parentScreen, ColorToggleEntryType type, Formatting formatting, int configIndex) {
            // Set parentScreen.
            this.parentScreen = parentScreen;

            // Copy config stuff.
            this.configIndex = configIndex;
            configList = type.configList;
            enabled = configList[this.configIndex];

            // Copy images + image data.
            coloredImage = type.colorImages[this.configIndex];
            defaultImage = type.defaultImage;
            offsetX = type.offsetX;
            offsetY = type.offsetY;
            imageW = type.width;
            imageH = type.height;

            // Create text objects based on color name
            titleText = Text.translatable("teamindicatorsplus.options.color." + formatting.getName().toLowerCase());

            // Create button.
            this.enableDisableButton = new ButtonWidget(0, 0, 100, 20, Text.literal("#0bacff"), (button) -> {
                this.toggleSet(!this.configList[this.configIndex]);
            });
        }


        /*
        Toggle set.
        Toggles the config option to the provided value.
         */
        public void toggleSet(boolean changeVal) {
            this.configList[this.configIndex] = changeVal;
            this.enabled = changeVal;
            TeamIndicatorsPlus.saveConfig();
        }


        /*
        Render.
        Renders the entry in the list.
         */
        @Override
        public void render(MatrixStack matrices, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta) {
            // Draw text.
            drawCenteredText(matrices, ScrollingColorToggleWidget.client.textRenderer, this.titleText, x + entryWidth / 2, y + 4, 16777215);

            // Draw image.
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            RenderSystem.setShader(GameRenderer::getPositionTexShader);
            RenderSystem.setShaderTexture(0, enabled ? coloredImage : defaultImage);
            this.parentScreen.drawTexture(matrices, x, y + 15, offsetX, offsetY, imageW, imageH);

            // Draw button.
            this.enableDisableButton.x = x + entryWidth - 100;
            this.enableDisableButton.y = y + 15;
            this.enableDisableButton.render(matrices, mouseX, mouseY, tickDelta);
        }


        /*
        Methods that need to exist for the buttons to work.
         */
        public List<? extends Element> children() {
            return ImmutableList.of(this.enableDisableButton);
        }
        public List<? extends Selectable> selectableChildren() { return ImmutableList.of(this.enableDisableButton); }
        public boolean mouseClicked(double mouseX, double mouseY, int button) { return this.enableDisableButton.mouseClicked(mouseX, mouseY, button); }
        public boolean mouseReleased(double mouseX, double mouseY, int button) { return this.enableDisableButton.mouseReleased(mouseX, mouseY, button); }
    }
}
