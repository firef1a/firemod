package dev.fire.render.screenElements;

import dev.fire.Mod;
import dev.fire.render.ARGB;
import dev.fire.render.Point2d;
import dev.fire.render.Point2i;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;

import java.awt.*;
import java.awt.image.renderable.RenderContext;
import java.util.ArrayList;
import java.util.function.Consumer;

public class Rect implements RendRect {
    public Point2i position, size;
    public ARGB rectColor;
    public ARGB borderColor, borderHighlightColor;
    protected Text text;
    public Consumer<Point2i> clickEffect;
    protected ArrayList<RendRect> siblings;


    public Rect() { }
    public Rect(Point2i position, Point2i size, ARGB rectColor, ARGB borderColor, ARGB borderHighlightColor, Consumer<Point2i> clickEffect) {
        this.position = position;
        this.size = size;
        this.rectColor = rectColor;
        this.borderColor = borderColor;
        this.borderHighlightColor = borderHighlightColor;
        this.clickEffect = clickEffect;
    }

    public Rect(Point2i position, Point2i size, ARGB rectColor, ARGB borderColor, ARGB borderHighlightColor) {
        this(position, size, rectColor, borderColor, borderHighlightColor, null);
    }
    public Rect(Point2i position, Point2i size, ARGB rectColor , ARGB borderColor) {
        this(position, size, rectColor, borderColor, null, null);
    }

    public Rect(Point2i position, Point2i size, ARGB rectColor) {
        this(position, size, rectColor, null, null, null);
    }

    public void setClickEffect(Consumer<Point2i> clickEffect) { this.clickEffect = clickEffect; }
    public void setText(Text text) { this.text = text; }

    public void render(DrawContext context, Point2i mouse) {
        context.fill(getX1(), getY1(), getX2(), getY2(), rectColor.getColor());
        if (borderColor != null) {
            int color = borderColor.getColor();
            if (borderHighlightColor != null) {
                if (containsPoint(mouse)) {
                    color = borderHighlightColor.getColor();
                }
            }
            context.drawBorder(getX1(), getY1(), size.x, size.y, color);
        }
        if (text != null) context.drawText(Mod.MC.textRenderer, text, getCenter().x-(Mod.MC.textRenderer.getWidth(text)/2), getCenter().y-(Mod.MC.textRenderer.fontHeight/2), 0xFFFFFF, true);
    }

    public void onClick(Point2i mouse) { if (clickEffect != null) clickEffect.accept(mouse); }

    public Point2i getCenter() { return position.add(size.multiply(new Point2d(0.5, 0.5))); }

    public int getX1() { return position.x; }
    public int getY1() { return position.y; }

    public int getX2() { return getX1() + size.x; }
    public int getY2() { return getY1() + size.y; }

    public boolean containsPoint(Point2i point) {
        return point.x >= getX1() && point.x <= getX2() && point.y >= getY1() && point.y <= getY2();
    }
}
