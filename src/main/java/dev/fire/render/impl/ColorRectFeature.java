package dev.fire.render.impl;

import dev.fire.features.Feature;
import dev.fire.render.ARGB;
import dev.fire.render.Alignment;
import dev.fire.render.Scaler;

public class ColorRectFeature extends ColorRect {
    public Feature feature;
    public ColorRectFeature(Scaler position, Scaler size, ARGB color, double zIndex, Alignment alignment, Alignment parentAlignment, Feature feature) {
        super(position, size, color, zIndex, alignment, parentAlignment, true);
        this.feature = feature;
    }

    @Override
    public boolean isEnabled() { return feature.isEnabled(); }

    @Override
    public void setEnabled(boolean e) { feature.setIsEnabled(e); }
}
