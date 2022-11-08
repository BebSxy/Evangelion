/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.util.ResourceLocation
 *  net.minecraft.util.SoundEvent
 */
package dev.evangelion.api.utilities.sounds;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;

public class SoundRegistrator {
    public static final SoundEvent DOUBLE_KILL = SoundRegistrator.addSoundsToRegistry("double_kill");
    public static final SoundEvent TRIPLE_KILL = SoundRegistrator.addSoundsToRegistry("triple_kill");
    public static final SoundEvent OVER_KILL = SoundRegistrator.addSoundsToRegistry("over_kill");
    public static final SoundEvent KILL_TACULAR = SoundRegistrator.addSoundsToRegistry("kill_tacular");
    public static final SoundEvent KILLAMONJARO = SoundRegistrator.addSoundsToRegistry("killamonjaro");
    public static final SoundEvent ARENA_SWITCH = SoundRegistrator.addSoundsToRegistry("arena_switch");
    public static final SoundEvent COD_HITSOUND = SoundRegistrator.addSoundsToRegistry("cod_hitsound");
    public static final SoundEvent METAL_IMPACT = SoundRegistrator.addSoundsToRegistry("metal_impact");

    private static SoundEvent addSoundsToRegistry(String soundId) {
        ResourceLocation shotSoundLocation = new ResourceLocation("evangelion", soundId);
        SoundEvent soundEvent = new SoundEvent(shotSoundLocation);
        soundEvent.setRegistryName(shotSoundLocation);
        return soundEvent;
    }
}

