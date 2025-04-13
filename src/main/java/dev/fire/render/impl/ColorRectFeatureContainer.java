package dev.fire.render.impl;

import dev.fire.Mod;
import dev.fire.features.Feature;
import dev.fire.render.ARGB;
import dev.fire.render.Alignment;
import dev.fire.render.Point2i;
import dev.fire.render.Scaler;
import net.minecraft.client.gui.DrawContext;

public class ColorRectFeatureContainer extends ColorRectFeature{
    private int margin;
    public ColorRectFeatureContainer(Scaler position, int margin, ARGB color, double zIndex, Alignment alignment, Feature feature) {
        super(position, new Scaler(0,0), color, zIndex, alignment, feature);
        this.margin = margin;
    }

    @Override
    public Point2i getScreenPosition() {
        return super.getScreenPosition().add(-margin, -margin);
    }

    @Override
    public Scaler getScalerSize() {
        double maxWidth = Mod.MC.textRenderer.getWidth(" ".repeat(10));
        double maxHeight = Mod.MC.textRenderer.fontHeight;
        for (RenderObject object : siblings) {
            Point2i size = object.getScalerSize().getScreenPosition();
            if (size.x > maxWidth) { maxWidth = size.x; }
            if (size.y > maxHeight) { maxHeight = size.y; }
        }
        return Scaler.fromPosition(maxWidth+margin*2, maxHeight+margin*2);
    }
}
