package dev.fire.render;

public class Point2d {
    public double x;
    public double y;

    public Point2d(double x, double y) {
        this.x = x;
        this.y = y;

    }
    public Point2i getPoint2i() { return new Point2i((int) x, (int) y); }
}
