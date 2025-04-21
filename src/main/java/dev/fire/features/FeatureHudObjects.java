package dev.fire.features;

import dev.fire.Mod;
import dev.fire.render.hudElements.RectScreenObject;
import dev.fire.render.hudElements.RenderObject;
import dev.fire.screens.HudFeatureMoveScreen;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;

public class FeatureHudObjects extends Feature {
    public static RectScreenObject featureHUDObjects = new RectScreenObject();
    public FeatureHudObjects() {
        init("featurehudobjects", "Feature Hud Objects", "Internal Feature \"parent HUD element\", do not disable this.");
    }

    public static void registerObject(RenderObject object) { featureHUDObjects.addSibling(object); }

    @Override
    public void renderHUD(DrawContext context, RenderTickCounter tickCounter) {
        if (Mod.getCurrentScreen() instanceof HudFeatureMoveScreen) return;
        featureHUDObjects.render(context);
    }
}
