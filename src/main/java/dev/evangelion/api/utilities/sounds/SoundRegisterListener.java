/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.util.SoundEvent
 *  net.minecraftforge.event.RegistryEvent$Register
 *  net.minecraftforge.fml.common.eventhandler.EventPriority
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 *  net.minecraftforge.registries.IForgeRegistryEntry
 */
package dev.evangelion.api.utilities.sounds;


import net.minecraft.util.SoundEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistryEntry;

public class SoundRegisterListener {
    @SubscribeEvent(priority= EventPriority.NORMAL, receiveCanceled=true)
    public void registerSoundEvents(RegistryEvent.Register<SoundEvent> event) {
        event.getRegistry().registerAll(SoundRegistrator.DOUBLE_KILL,
                SoundRegistrator.TRIPLE_KILL,
                SoundRegistrator.OVER_KILL,
                SoundRegistrator.KILL_TACULAR,
                SoundRegistrator.KILLAMONJARO,
                SoundRegistrator.ARENA_SWITCH,
                SoundRegistrator.COD_HITSOUND,
                SoundRegistrator.METAL_IMPACT);
    }
}

