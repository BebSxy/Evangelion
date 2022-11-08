package dev.evangelion.client.modules.miscellaneous.spammer;

import java.nio.file.attribute.FileAttribute;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Paths;
import java.io.File;
import dev.evangelion.api.utilities.IMinecraft;

public class SpammerInitializer implements IMinecraft
{
    public void initialize() {
        try {
            if (!Files.exists(Paths.get(SpammerInitializer.mc.gameDir + File.separator + "Evangelion/Spammer/", new String[0]), new LinkOption[0])) {
                Files.createDirectories(Paths.get(SpammerInitializer.mc.gameDir + File.separator + "Evangelion/Spammer/", new String[0]), (FileAttribute<?>[])new FileAttribute[0]);
            }
        }
        catch (Exception ex) {}
    }
}
