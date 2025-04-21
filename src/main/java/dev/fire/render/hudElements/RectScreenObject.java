package dev.fire.render.hudElements;

import dev.fire.Mod;
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
