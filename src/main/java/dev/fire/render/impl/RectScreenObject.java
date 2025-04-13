package dev.fire.render.impl;

import dev.fire.Mod;
import dev.fire.render.ARGB;
import dev.fire.render.Alignment;
import dev.fire.render.Scaler;

public class RectScreenObject extends RenderObject {
    public RectScreenObject() {
        super();
    }

    @Override
    public Scaler getScalerSize() {
        return Scaler.fromPosition(Mod.getWindowWidth(), Mod.getWindowHeight());
    }
}
