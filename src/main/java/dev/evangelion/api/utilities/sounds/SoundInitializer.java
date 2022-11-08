/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.common.io.ByteStreams
 */
package dev.evangelion.api.utilities.sounds;

import com.google.common.io.ByteStreams;
import dev.evangelion.api.utilities.IMinecraft;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Paths;
import java.nio.file.attribute.FileAttribute;

public class SoundInitializer
implements IMinecraft {
    public static File arena_switch;
    public static File cod_hitsound;
    public static File metal_impact;
    public static File double_kill;
    public static File triple_kill;
    public static File over_kill;
    public static File kill_tacular;
    public static File killamonjaro;
    private String directory;

    public SoundInitializer() {
        this.directory = SoundInitializer.mc.gameDir + File.separator + "Evangelion" + File.separator + "Sounds" + File.separator + "Client" + File.separator;
        arena_switch = new File(this.directory + "arena_switch.wav");
        cod_hitsound = new File(this.directory + "cod_hitsound.wav");
        metal_impact = new File(this.directory + "metal_impact.wav");
        double_kill = new File(this.directory + "double_kill.wav");
        triple_kill = new File(this.directory + "triple_kill.wav");
        over_kill = new File(this.directory + "over_kill.wav");
        kill_tacular = new File(this.directory + "kill_tacular.wav");
        killamonjaro = new File(this.directory + "killamonjaro.wav");
    }

    public void initialize() {
        try {
            if (!Files.exists(Paths.get(SoundInitializer.mc.gameDir + File.separator + "Evangelion/Sounds/Client/", new String[0]), new LinkOption[0])) {
                Files.createDirectories(Paths.get(SoundInitializer.mc.gameDir + File.separator + "Evangelion/Sounds/Client/", new String[0]), new FileAttribute[0]);
            }
        }
        catch (Exception exception) {
            // empty catch block
        }
        this.create("arena_switch.wav", arena_switch);
        this.create("cod_hitsound.wav", cod_hitsound);
        this.create("metal_impact.wav", metal_impact);
        this.create("double_kill.wav", double_kill);
        this.create("triple_kill.wav", triple_kill);
        this.create("over_kill.wav", over_kill);
        this.create("kill_tacular.wav", kill_tacular);
        this.create("killamonjaro.wav", killamonjaro);
    }

    public void create(String name, File file) {
        try {
            if (!file.exists()) {
                InputStream stream = this.getClass().getClassLoader().getResourceAsStream("assets/evangelion/sounds/" + name);
                FileOutputStream outputStream = new FileOutputStream(file);
                ByteStreams.copy((InputStream)stream, (OutputStream)outputStream);
            }
        }
        catch (Exception exception) {
            // empty catch block
        }
    }
}

