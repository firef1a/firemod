package dev.fire.render.impl;

import dev.fire.render.ARGB;
import dev.fire.render.Alignment;
import dev.fire.render.Scaler;
import net.minecraft.client.gui.DrawContext;

public class ColorRect extends RenderObject{
    public ARGB color;

    public ColorRect(Scaler position, Scaler size, ARGB color, double zIndex, Alignment alignment, Alignment parentAlignment, boolean enabled) {
        super(position, size, zIndex, alignment, parentAlignment, enabled);
        this.color = color;
    }

    @Override
    public void internalRender(DrawContext context) { context.fill(getX1(), getY1(), getX2(), getY2(), color.getColor()); }
}
