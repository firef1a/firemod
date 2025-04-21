package dev.fire.render.screenElements;

import dev.fire.render.Point2i;
import net.minecraft.client.gui.DrawContext;

public interface RendRect {
    void render(DrawContext drawContext, Point2i mouse);
}
