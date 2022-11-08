/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.util.math.Vec3d
 */
package dev.evangelion.api.utilities;

import java.math.BigDecimal;
import java.math.RoundingMode;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.Vec3d;

public class MathUtils {
    static Minecraft mc = Minecraft.getMinecraft();

    public static double square(double input) {
        return input * input;
    }

    public static float square(float input) {
        return input * input;
    }

    public static double roundToPlaces(double number, int places) {
        BigDecimal decimal = new BigDecimal(number);
        decimal = decimal.setScale(places, RoundingMode.HALF_UP);
        return decimal.doubleValue();
    }

    public static Vec3d roundVector(Vec3d vec3d, int places) {
        return new Vec3d(MathUtils.roundToPlaces(vec3d.x, places), MathUtils.roundToPlaces(vec3d.y, places), MathUtils.roundToPlaces(vec3d.z, places));
    }

    public static double randomNumber(double max, double min) {
        return Math.random() * (max - min) + min;
    }

    public static double distance(float x, float y, float x1, float y1) {
        return Math.sqrt((x - x1) * (x - x1) + (y - y1) * (y - y1));
    }

    public static float normalize(float value, float min, float max) {
        return (value - min) / (max - min);
    }
}

