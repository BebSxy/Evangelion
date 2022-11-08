/*
 * Decompiled with CFR 0.150.
 */
package dev.evangelion.api.utilities;

import dev.evangelion.api.utilities.IMinecraft;
import dev.evangelion.client.modules.client.ModuleColor;
import java.awt.Color;

public class ColorUtils
implements IMinecraft {
    public static Color wave(Color color, int index, int count) {
        float[] hsb = new float[3];
        Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), hsb);
        float brightness = Math.abs(((float)(System.currentTimeMillis() % 2000L) / 1000.0f + (float)index / (float)count * 2.0f) % 2.0f - 1.0f);
        brightness = 0.5f + 0.5f * brightness;
        hsb[2] = brightness % 2.0f;
        return new Color(Color.HSBtoRGB(hsb[0], hsb[1], hsb[2]));
    }

    public static Color rainbow(int delay) {
        double rainbowState = Math.ceil((double)(System.currentTimeMillis() + (long)(delay * ModuleColor.INSTANCE.rainbowOffset.getValue().intValue())) / 20.0);
        return Color.getHSBColor((float)((rainbowState %= 360.0) / 360.0), (float)ModuleColor.INSTANCE.rainbowSat.getValue().intValue() / 255.0f, (float)ModuleColor.INSTANCE.rainbowBri.getValue().intValue() / 255.0f);
    }

    public static Color gradient(Color color1, Color color2, int index, int count) {
        double offset = Math.abs(((float)(System.currentTimeMillis() % 2000L) / 1000.0f + (float)index / (float)(count + 1) * 2.0f) % 2.0f - 1.0f);
        if (offset > 1.0) {
            double left = offset % 1.0;
            int off = (int)offset;
            offset = off % 2 == 0 ? left : 1.0 - left;
        }
        double inverse_percent = 1.0 - offset;
        int redPart = (int)((double)color1.getRed() * inverse_percent + (double)color2.getRed() * offset);
        int greenPart = (int)((double)color1.getGreen() * inverse_percent + (double)color2.getGreen() * offset);
        int bluePart = (int)((double)color1.getBlue() * inverse_percent + (double)color2.getBlue() * offset);
        return new Color(redPart, greenPart, bluePart);
    }

    public static Color setAlpha(Color color, int alpha) {
        return new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha);
    }
}

