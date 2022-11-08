/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.play.server.SPacketTimeUpdate
 *  net.minecraft.util.math.MathHelper
 *  net.minecraftforge.common.MinecraftForge
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 */
package dev.evangelion.api.utilities;

import dev.evangelion.client.events.EventPacketReceive;
import java.util.Arrays;
import net.minecraft.network.play.server.SPacketTimeUpdate;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class TPSUtils {
    private static final float[] tickRates = new float[20];
    private int nextIndex = 0;
    private long timeLastTimeUpdate = -1L;

    public TPSUtils() {
        Arrays.fill(tickRates, 0.0f);
        MinecraftForge.EVENT_BUS.register((Object)this);
    }

    public static float getTickRate() {
        float numTicks = 0.0f;
        float sumTickRates = 0.0f;
        for (float tickRate : tickRates) {
            if (!(tickRate > 0.0f)) continue;
            sumTickRates += tickRate;
            numTicks += 1.0f;
        }
        return MathHelper.clamp((float)(sumTickRates / numTicks), (float)0.0f, (float)20.0f);
    }

    public static float getTpsFactor() {
        float TPS = TPSUtils.getTickRate();
        return 20.0f / TPS;
    }

    private void onTimeUpdate() {
        if (this.timeLastTimeUpdate != -1L) {
            float timeElapsed = (float)(System.currentTimeMillis() - this.timeLastTimeUpdate) / 1000.0f;
            TPSUtils.tickRates[this.nextIndex % TPSUtils.tickRates.length] = MathHelper.clamp((float)(20.0f / timeElapsed), (float)0.0f, (float)20.0f);
            ++this.nextIndex;
        }
        this.timeLastTimeUpdate = System.currentTimeMillis();
    }

    @SubscribeEvent
    public void onUpdate(EventPacketReceive event) {
        if (event.getPacket() instanceof SPacketTimeUpdate) {
            this.onTimeUpdate();
        }
    }
}

