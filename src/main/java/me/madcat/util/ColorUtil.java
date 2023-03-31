package me.madcat.util;

import java.awt.Color;
import me.madcat.features.modules.client.ClickGui;

public class ColorUtil {
    public static int toRGBA(float f, float f2, float f3, float f4) {
        return ColorUtil.toRGBA((int)(f * 255.0f), (int)(f2 * 255.0f), (int)(f3 * 255.0f), (int)(f4 * 255.0f));
    }

    public static Color injectAlpha(Color color, int n) {
        return new Color(color.getRed(), color.getGreen(), color.getBlue(), n);
    }

    public static int toRGBA(double[] dArray) {
        if (dArray.length != 4) {
            throw new IllegalArgumentException("colors[] must have a length of 4!");
        }
        return ColorUtil.toRGBA((float)dArray[0], (float)dArray[1], (float)dArray[2], (float)dArray[3]);
    }

    public static int toRGBA(float[] fArray) {
        if (fArray.length != 4) {
            throw new IllegalArgumentException("colors[] must have a length of 4!");
        }
        return ColorUtil.toRGBA(fArray[0], fArray[1], fArray[2], fArray[3]);
    }

    public static Color interpolate(float f, Color color, Color color2) {
        float f2 = (float)color.getRed() / 255.0f;
        float f3 = (float)color.getGreen() / 255.0f;
        float f4 = (float)color.getBlue() / 255.0f;
        float f5 = (float)color.getAlpha() / 255.0f;
        float f6 = (float)color2.getRed() / 255.0f;
        float f7 = (float)color2.getGreen() / 255.0f;
        float f8 = (float)color2.getBlue() / 255.0f;
        float f9 = (float)color2.getAlpha() / 255.0f;
        float f10 = f2 * f + f6 * (1.0f - f);
        float f11 = f3 * f + f7 * (1.0f - f);
        float f12 = f4 * f + f8 * (1.0f - f);
        float f13 = f5 * f + f9 * (1.0f - f);
        return new Color(f10, f11, f12, f13);
    }

    public static int toRGBA(Color color) {
        return ColorUtil.toRGBA(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
    }

    public static Color rainbow(int n) {
        double d = Math.ceil((double)(System.currentTimeMillis() + (long)n) / 20.0);
        return Color.getHSBColor((float)(d % 360.0 / 360.0), ClickGui.INSTANCE().rainbowSaturation.getValue() / 255.0f, ClickGui.INSTANCE().rainbowBrightness.getValue() / 255.0f);
    }

    public static int toRGBA(int n, int n2, int n3) {
        return ColorUtil.toRGBA(n, n2, n3, 255);
    }

    public static Color pulseColor(Color color, int n, int n2) {
        float[] fArray = new float[3];
        Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), fArray);
        float f = Math.abs(((float)(System.currentTimeMillis() % 2000L) / Float.intBitsToFloat(Float.floatToIntBits(0.0013786979f) ^ 0x7ECEB56D) + (float)n / (float)n2 * Float.intBitsToFloat(Float.floatToIntBits(0.09192204f) ^ 0x7DBC419F)) % Float.intBitsToFloat(Float.floatToIntBits(0.7858098f) ^ 0x7F492AD5) - Float.intBitsToFloat(Float.floatToIntBits(6.46708f) ^ 0x7F4EF252));
        f = Float.intBitsToFloat(Float.floatToIntBits(18.996923f) ^ 0x7E97F9B3) + Float.intBitsToFloat(Float.floatToIntBits(2.7958195f) ^ 0x7F32EEB5) * f;
        fArray[2] = f % Float.intBitsToFloat(Float.floatToIntBits(0.8992331f) ^ 0x7F663424);
        return new Color(Color.HSBtoRGB(fArray[0], fArray[1], fArray[2]));
    }

    public static int toRGBA(int n, int n2, int n3, int n4) {
        return (n << 16) + (n2 << 8) + n3 + (n4 << 24);
    }

    public static int toARGB(int n, int n2, int n3, int n4) {
        return new Color(n, n2, n3, n4).getRGB();
    }

    public static Color getGradientOffset(Color color, Color color2, double d) {
        int n;
        double d2;
        if (d > 1.0) {
            d2 = d % 1.0;
            n = (int)d;
            d = n % 2 == 0 ? d2 : 1.0 - d2;
        }
        d2 = 1.0 - d;
        n = (int)((double)color.getRed() * d2 + (double)color2.getRed() * d);
        int n2 = (int)((double)color.getGreen() * d2 + (double)color2.getGreen() * d);
        int n3 = (int)((double)color.getBlue() * d2 + (double)color2.getBlue() * d);
        return new Color(n, n2, n3);
    }

    public static class Colors {
        public static final int ORANGE;
        public static final int WHITE;
        public static final int YELLOW;
        public static final int BLACK;
        public static final int RED;
        public static final int GREEN;
        public static final int BLUE;
        public static final int RAINBOW;
        public static final int DARK_RED;
        public static final int GRAY;
        public static final int PURPLE;

        static {
            RAINBOW = Integer.MIN_VALUE;
            WHITE = ColorUtil.toRGBA(255, 255, 255, 255);
            BLACK = ColorUtil.toRGBA(0, 0, 0, 255);
            RED = ColorUtil.toRGBA(255, 0, 0, 255);
            GREEN = ColorUtil.toRGBA(0, 255, 0, 255);
            BLUE = ColorUtil.toRGBA(0, 0, 255, 255);
            ORANGE = ColorUtil.toRGBA(255, 128, 0, 255);
            PURPLE = ColorUtil.toRGBA(163, 73, 163, 255);
            GRAY = ColorUtil.toRGBA(127, 127, 127, 255);
            DARK_RED = ColorUtil.toRGBA(64, 0, 0, 255);
            YELLOW = ColorUtil.toRGBA(255, 255, 0, 255);
        }
    }
}

