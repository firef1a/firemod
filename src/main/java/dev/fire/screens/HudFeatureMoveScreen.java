package dev.fire.screens;

import dev.fire.Mod;
import dev.fire.features.FeatureHudObjects;
import dev.fire.render.Alignment;
import dev.fire.render.Point2d;
import dev.fire.render.Point2i;
import dev.fire.render.Scaler;
import dev.fire.render.impl.RenderObject;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

public class HudFeatureMoveScreen extends Screen {
    public final Screen parentScreen;
    public RenderObject selectedObject;
    public Point2i selectedObjectOffset;

    public HudFeatureMoveScreen(Text title, Screen parentScreen) {
        super(title);
        this.parentScreen = parentScreen;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        FeatureHudObjects.featureHUDObjects.render(context);
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

        for (RenderObject object : FeatureHudObjects.featureHUDObjects.siblings) {
            if (object.containsPoint(mouse)) {
                selectedObject = object;

                selectedObjectOffset = new Point2i(mouse.x - object.getX1(), mouse.y - object.getY1());
                if (object.getAlignment() == Alignment.RIGHT) selectedObjectOffset = selectedObjectOffset.add(object.getSize().multiply(new Point2d(-1,0)));

                break;
            }
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        Point2i mouse = new Point2i((int) mouseX, (int) mouseY);

        if (selectedObject != null) {
            selectedObject.position.setScaler(Scaler.fromPosition(mouse.add(-selectedObjectOffset.x, -selectedObjectOffset.y)));
        }

        this.selectedObject = null;
        this.selectedObjectOffset = null;
        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        Point2i mouse = new Point2i((int) mouseX, (int) mouseY);

        if (selectedObject != null) {
            selectedObject.position.setScaler(Scaler.fromPosition(mouse.add(-selectedObjectOffset.x, -selectedObjectOffset.y)));
        }

        return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
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
        // save config here !!!
        assert client != null;
        client.setScreen(parentScreen);
    }

    public static boolean isOpen() { return Mod.getCurrentScreen() instanceof HudFeatureMoveScreen; }

}
