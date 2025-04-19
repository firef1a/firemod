package dev.fire.render;

import com.google.gson.JsonObject;
import dev.fire.Mod;

public class Scaler {
    public double sx;
    public double sy;

    public Scaler(double sx, double sy) {
        this.sx = sx;
        this.sy = sy;
    }

    public void setScaler(Scaler scaler) {
        this.sx = scaler.sx;
        this.sy = scaler.sy;
    }

    public Scaler add(Scaler scaler) { return new Scaler(sx + scaler.sx, sy + scaler.sy);}
    public Scaler add(double x, double y) { return new Scaler(sx + x, sy + y);}

    public Point2i getScreenPosition() {
        return new Point2i((int) (sx * Mod.getWindowWidth()), (int) (sy * Mod.getWindowHeight()));
    }
    public int getScreenX() { return getScreenPosition().x; }
    public int getScreenY() { return getScreenPosition().y; }

    public static Scaler fromPosition(int x, int y) {
        return new Scaler((double) x / Mod.getWindowWidth(), (double) y / Mod.getWindowHeight());
    }

    public static Scaler fromPosition(double x, double y) {
        return new Scaler(x / Mod.getWindowWidth(), y / Mod.getWindowHeight());
    }

    public static Scaler fromPosition(Point2i point) {
        return fromPosition(point.x, point.y);
    }

    public void saveConfig(String namespace, JsonObject jsonObject) {
        jsonObject.addProperty(namespace + ".sx", sx);
        jsonObject.addProperty(namespace + ".sy", sy);
    }

    public static Scaler fromJsonOrDefault(String namespace, JsonObject jsonObject, Scaler def) {
        return jsonObject.has(namespace + ".sx") && jsonObject.has(namespace + ".sy") ?
                new Scaler(jsonObject.get(namespace + ".sx").getAsDouble(), jsonObject.get(namespace + ".sy").getAsDouble()) :
                def;
    }

    /*
    public void toJson(JsonObject jsonObject, String namespace, String fieldName) { this.toJson(jsonObject,namespace + "." + fieldName); }
    public static Scaler getFromJson(JsonObject jsonObject, String namespace, String fieldName) { return getFromJson(jsonObject,namespace + "." + fieldName); }

    public void toJson(JsonObject jsonObject, String namespace) {
        JsonUtils.addProperty(jsonObject, namespace, "sx", this.sx);
        JsonUtils.addProperty(jsonObject, namespace, "sy", this.sy);
    }

    public static Scaler getFromJson(JsonObject jsonObject, String namespace) {
        double sx = JsonUtils.getElement(jsonObject, namespace, "sx").getAsDouble();
        double sy = JsonUtils.getElement(jsonObject, namespace, "sy").getAsDouble();
        return new Scaler(sx, sy);
    }

     */
    public String toString() {
        return "Scaler(" + sx + ", " + sy + ")";
    }
}