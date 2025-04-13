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
    protected Alignment alignment;


    public RenderObject() {
        this(new Scaler(0,0), new Scaler(0,0), 0, Alignment.NONE, true);
    }

    public RenderObject(Scaler position, Scaler size, double zIndex, Alignment alignment, boolean enabled) {
        this(position, size, alignment, zIndex);
        this.enabled = enabled;
    }

    public RenderObject(Scaler position, Scaler size, Alignment alignment, double zIndex) {
        this.position = position;
        this.size = size;
        this.zIndex = zIndex;
        this.alignment = alignment;
        this.siblings = new ArrayList<>();
    }

    public void update() { };

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
        internalRender(context);
        for (RenderObject object : siblings) {
            object.render(context);
        }
    }

    public Point2i getScreenPosition() {
        if (parent == null) { return position.getScreenPosition(); }
        else {
            Point2i pos = parent.position.add(position).getScreenPosition();
            if (alignment.equals(Alignment.RIGHT)) {
                return pos.add(parent.getWidth(), 0);
            }
            if (alignment.equals(Alignment.LEFT)) {
                return pos.add(-parent.getWidth(), 0);
            }
            if (alignment.equals(Alignment.TOP)) {
                return pos.add(0, parent.getHeight());
            }
            if (alignment.equals(Alignment.BOTTOM)) {
                return pos.add(0, -parent.getHeight());
            }
            return pos;
        }
    }

    public int getX1() { return getScreenPosition().x; }
    public int getY1() { return getScreenPosition().y; }

    public int getX2() { return getX1() + getWidth(); }
    public int getY2() { return getY1() + getHeight(); }

    public Scaler getScalerSize() { return size; }
    public Point2i getSize() { return getScalerSize().getScreenPosition(); }
    public int getWidth() { return getSize().x; }
    public int getHeight() { return getSize().y; }


    public void setEnabled(boolean e) { enabled = e; }
    public boolean isEnabled() { return enabled; }
    public Alignment getAlignment() { return alignment; }

}
