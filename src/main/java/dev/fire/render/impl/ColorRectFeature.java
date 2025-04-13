package dev.fire.render.impl;

import dev.fire.features.Feature;
import dev.fire.render.ARGB;
import dev.fire.render.Alignment;
import dev.fire.render.Scaler;

public class ColorRectFeature extends ColorRect {
    public Feature feature;
    public ColorRectFeature(Scaler position, Scaler size, ARGB color, double zIndex, Alignment alignment, Feature feature) {
        super(position, size, color, zIndex, alignment, feature.isEnabled());
    }

    @Override
    public boolean isEnabled() { return feature.isEnabled(); }
    @Override
    public void setEnabled(boolean e) { feature.setIsEnabled(e); }
}
