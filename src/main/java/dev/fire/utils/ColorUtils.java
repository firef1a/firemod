package dev.fire.utils;

public class ColorUtils {
    public static int getARGB(int rgb, double alpha) {
        if (alpha < 0.0 || alpha > 1.0) {
            return 0;
        }

        int red = (rgb >> 16) & 0xFF;
        int green = (rgb >> 8) & 0xFF;
        int blue = rgb & 0xFF;
        int alphaInt = (int) Math.round(alpha * 255);

        return (alphaInt << 24) | (red << 16) | (green << 8) | blue;
    }
}
