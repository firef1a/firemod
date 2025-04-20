package dev.fire.screens;

import dev.fire.Mod;
import dev.fire.config.Config;
import dev.fire.event.KeyInputHandler;
import dev.fire.features.FeatureHudObjects;
import dev.fire.render.*;
import dev.fire.render.impl.ColorRect;
import dev.fire.render.impl.ColorRectFeature;
import dev.fire.render.impl.RenderObject;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

public class PTPScreen extends Screen {
    public final Screen parentScreen;

    public PTPScreen(Text title, Screen parentScreen) {
        super(title);
        this.parentScreen = parentScreen;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        //super.render(context, mouseX, mouseY, delta);
        ColorRect rect = new ColorRect(new Scaler(0.45, 0.4), new Scaler(0.1, 0.2), new ARGB(0.5, 0x000000), 0, Alignment.NONE, Alignment.NONE, true);
        rect.render(context);

    }

    @Override
    public void renderBackground(DrawContext context, int mouseX, int mouseY, float delta) {
        super.renderBackground(context, mouseX, mouseY, delta);
    }

    @Override
    protected void init() {
        super.init();
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        Point2i mouse = new Point2i((int) mouseX, (int) mouseY);

        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean shouldPause() {
        return false;
    }
    @Override
    public boolean shouldCloseOnEsc() {
        return true;
    }
    @Override
    public void close() {
        Mod.setCurrentScreen(parentScreen);
    }

    public static boolean isOpen() { return Mod.getCurrentScreen() instanceof PTPScreen; }

}
