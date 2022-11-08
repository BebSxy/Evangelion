/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.entity.EntityPlayerSP
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.util.SoundCategory
 *  net.minecraft.util.SoundEvent
 */
package dev.evangelion.api.utilities.sounds;

import dev.evangelion.api.utilities.IMinecraft;
import java.io.File;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;

public class SoundUtils
implements IMinecraft {
    public static void playSound(SoundEvent sound) {
        try {
            EntityPlayerSP player = SoundUtils.mc.player;
            SoundUtils.mc.world.playSound((EntityPlayer)player, player.getPosition(), sound, SoundCategory.PLAYERS, 1.0f, 1.0f);
        }
        catch (Exception exception) {
            // empty catch block
        }
    }

    public static void playSound(File file, Float volume) {
        if (!file.exists()) {
            return;
        }
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(file);
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            FloatControl gainControl = (FloatControl)clip.getControl(FloatControl.Type.MASTER_GAIN);
            gainControl.setValue(volume.floatValue());
            clip.start();
        }
        catch (Exception exception) {
            // empty catch block
        }
    }
}

