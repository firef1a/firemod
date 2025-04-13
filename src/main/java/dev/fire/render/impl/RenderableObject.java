package dev.fire.render.impl;

import dev.fire.render.Point2i;
import dev.fire.render.Scaler;
import net.minecraft.client.gui.DrawContext;

public interface RenderableObject {
    void addSibling(RenderObject object);
    void internalRender(DrawContext context);
    void render(DrawContext context);

    Point2i getSize();
    int getX1();
    int getY1();
    int getX2();
    int getY2();
    int getWidth();
    int getHeight();
}
