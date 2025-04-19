package dev.fire.render.impl;

import dev.fire.render.Alignment;
import dev.fire.render.Point2i;
import dev.fire.render.Scaler;
import net.minecraft.client.gui.DrawContext;

import java.util.ArrayList;

public class RenderObject implements RenderableObject{
    public Scaler position;
    protected Scaler size;
    public ArrayList<RenderObject> siblings;
    public double zIndex;
    public RenderObject parent = null;

    protected boolean enabled = true;
    protected Alignment parentAlignment;
    protected Alignment alignment;


    public RenderObject() {
        this(new Scaler(0,0), new Scaler(0,0), 0, Alignment.NONE, Alignment.NONE, true);
    }

    public RenderObject(Scaler position, Scaler size, double zIndex, Alignment alignment, Alignment parentAlignment, boolean enabled) {
        this(position, size, alignment, parentAlignment, zIndex);
        this.enabled = enabled;
    }

    public RenderObject(Scaler position, Scaler size, Alignment alignment, Alignment parentAlignment, double zIndex) {
        this.position = position;
        this.size = size;
        this.zIndex = zIndex;
        this.alignment = alignment;
        this.parentAlignment = parentAlignment;
        this.siblings = new ArrayList<>();
    }

    public void update() { }

    public void internalRender(DrawContext context) { }

    public void addSibling(RenderObject object) {
        object.parent = this;
        int index = 0;
        for (int i = 0; i < siblings.size(); i++) {
            if (siblings.get(i).zIndex > object.zIndex) {
                break;
            }
            index = i;
        }
        siblings.add(index, object);
    }

    public void render(DrawContext context) {
        if (isEnabled()) internalRender(context);
        for (RenderObject object : siblings) {
            object.render(context);
        }
    }

    public Scaler getPosition() {
        if (parent == null) {
            return position;
        } else {
            Scaler pos = parent.getPosition().add(position);
            if (parentAlignment.equals(Alignment.RIGHT)) {
                pos = pos.add(parent.getScalerSize().sx, 0);
            }
            if (parentAlignment.equals(Alignment.LEFT)) {
                pos = pos.add(-parent.getScalerSize().sx, 0);
            }
            if (parentAlignment.equals(Alignment.TOP)) {
                pos = pos.add(0, parent.getScalerSize().sy);
            }
            if (parentAlignment.equals(Alignment.BOTTOM)) {
                pos = pos.add(0, -parent.getScalerSize().sy);
            }

            if (alignment.equals(Alignment.RIGHT)) {
                pos = pos.add(-this.getScalerSize().sx, 0);
            }
            if (alignment.equals(Alignment.LEFT)) {
                pos = pos.add(this.getScalerSize().sx, 0);
            }
            if (alignment.equals(Alignment.TOP)) {
                pos = pos.add(0, -this.getScalerSize().sy);
            }
            if (alignment.equals(Alignment.BOTTOM)) {
                pos = pos.add(0, this.getScalerSize().sy);
            }
            return pos;
        }
    }

    public Point2i getScreenPosition() {
        return getPosition().getScreenPosition();
    }

    public int getX1() { return getScreenPosition().x; }
    public int getY1() { return getScreenPosition().y; }

    public int getX2() { return (int) (getX1() + getWidth()); }
    public int getY2() { return (int) (getY1() + getHeight()); }

    public Scaler getScalerSize() { return size; }
    public Point2i getSize() { return getScalerSize().getScreenPosition(); }
    public int getWidth() { return getSize().x; }
    public int getHeight() { return getSize().y; }


    public void setEnabled(boolean e) { enabled = e; }
    public boolean isEnabled() { return enabled; }
    public Alignment getAlignment() { return alignment; }

    public boolean containsPoint(Point2i point) {
        return point.x >= getX1() && point.x <= getX2() && point.y >= getY1() && point.y <= getY2();
    }

}
