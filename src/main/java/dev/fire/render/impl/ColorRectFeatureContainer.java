package dev.fire.render.impl;

import dev.fire.features.Feature;
import dev.fire.render.ARGB;
import dev.fire.render.Alignment;
import dev.fire.render.Point2i;
import dev.fire.render.Scaler;

public class ColorRectFeatureContainer extends ColorRectFeature {
    private int margin;

    public ColorRectFeatureContainer(Scaler position, int margin, ARGB color, double zIndex, Alignment alignment, Alignment parentAlignment, Feature feature) {
        super(position, new Scaler(0,0), color, zIndex, alignment, parentAlignment, feature);
        this.margin = margin;
    }

    @Override
    public Point2i getScreenPosition() {
        return super.getScreenPosition().add(-margin, -margin);
    }

    @Override
    public Scaler getScalerSize() {
        double maxWidth = 0;
        double maxHeight = 0;
        for (RenderObject object : siblings) {
            Point2i size = object.getScalerSize().getScreenPosition();
            if (size.x > maxWidth) { maxWidth = size.x; }
            if (size.y > maxHeight) { maxHeight = size.y; }
        }
        return Scaler.fromPosition(maxWidth+margin*2, maxHeight+margin*2);
    }
}
