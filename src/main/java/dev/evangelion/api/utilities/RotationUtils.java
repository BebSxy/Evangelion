/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.entity.EntityPlayerSP
 *  net.minecraft.entity.Entity
 *  net.minecraft.util.math.MathHelper
 */
package dev.evangelion.api.utilities;

import dev.evangelion.api.utilities.IMinecraft;
import dev.evangelion.client.events.EventMotion;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;

public class RotationUtils
implements IMinecraft {
    private static float c;
    private static float b;

    public static float[] getRotations(double posX, double posY, double posZ) {
        EntityPlayerSP player = RotationUtils.mc.player;
        double x = posX - player.posX;
        double y = posY - (player.posY + (double)player.getEyeHeight());
        double z = posZ - player.posZ;
        double dist = MathHelper.sqrt((double)(x * x + z * z));
        float yaw = (float)(Math.atan2(z, x) * 180.0 / Math.PI) - 90.0f;
        float pitch = (float)(-(Math.atan2(y, dist) * 180.0 / Math.PI));
        return new float[]{yaw, pitch};
    }

    public static float[] getRotationsEntity(Entity entity) {
        return RotationUtils.getRotations(entity.posX, entity.posY + (double)entity.getEyeHeight() - 0.4, entity.posZ);
    }

    public static void rotate(EventMotion event, float[] angle) {
        event.setRotationYaw(angle[0]);
        event.setRotationPitch(angle[1]);
    }

    public static float[] getSmoothRotations(float[] angles, int smooth) {
        float var2 = MathHelper.clamp((float)(1.0f - (float)smooth / 100.0f), (float)0.1f, (float)1.0f);
        c += (angles[0] - c) * var2;
        b += (angles[1] - b) * var2;
        return new float[]{MathHelper.wrapDegrees((float)c), b};
    }

    public static float getDistanceBetweenAngles(float angle1, float angle2) {
        float angle = Math.abs(angle1 - angle2) % 360.0f;
        if (angle > 180.0f) {
            angle = 360.0f - angle;
        }
        return angle;
    }
}

