package dev.fire.features;

import dev.fire.render.ARGB;
import dev.fire.render.Alignment;
import dev.fire.render.Scaler;
import dev.fire.render.impl.RectScreenObject;
import dev.fire.render.impl.RenderObject;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;

public class FeatureHudObjects extends Feature {
    private static RectScreenObject featureHUDObjects = new RectScreenObject();
    public FeatureHudObjects() {
        init("featurehudobjects", "Feature Hud Objects");
    }

    public static void registerObject(RenderObject object) { featureHUDObjects.addSibling(object); }

    @Override
    public void renderHUD(DrawContext context, RenderTickCounter tickCounter) {
        featureHUDObjects.render(context);
    }
}
