package dev.evangelion.client.modules.miscellaneous.spammer;

import dev.evangelion.api.utilities.MathUtils;
import java.util.stream.Stream;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketChatMessage;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import dev.evangelion.api.utilities.TimerUtils;
import java.io.File;
import dev.evangelion.client.values.impl.ValueBoolean;
import dev.evangelion.client.values.impl.ValueNumber;
import dev.evangelion.client.values.impl.ValueString;
import dev.evangelion.api.manager.module.RegisterModule;
import dev.evangelion.api.manager.module.Module;

@RegisterModule(name = "Spammer", description = "Spam txt files from your client folder.", category = Module.Category.MISCELLANEOUS)
public class ModuleSpammer extends Module
{
    ValueString fileName;
    ValueNumber delay;
    ValueBoolean greenText;
    ValueBoolean randomNumbers;
    private File spammerFile;
    TimerUtils timer;
    ArrayList<String> spamList;
    private int pos;
    
    public ModuleSpammer() {
        this.fileName = new ValueString("FileName", "File Name", "The name of the file to spam, it must be a txt.", "spam.txt");
        this.delay = new ValueNumber("Delay", "Delay", "Delay to wait before sending each message.", 1.0f, 0.0f, 10.0f);
        this.greenText = new ValueBoolean("GreenText", "Green Text", "Make the message green by adding a '>' before.", true);
        this.randomNumbers = new ValueBoolean("RandomNumbers", "Random Numbers", "Adds random numbers at the end of the message to prevent getting kicked.", false);
        this.timer = new TimerUtils();
        this.spamList = new ArrayList<String>();
        this.pos = 0;
        this.spammerFile = new File(ModuleSpammer.mc.gameDir + File.separator + "Evangelion" + File.separator + "Spammer" + File.separator + this.fileName.getValue());
    }
    
    @Override
    public void onUpdate() {
        super.onUpdate();
        if (this.nullCheck()) {
            return;
        }
        this.spammerFile = new File(ModuleSpammer.mc.gameDir + File.separator + "Evangelion" + File.separator + "Spammer" + File.separator + this.fileName.getValue());
        try (final Stream<String> lines = Files.lines(Paths.get(this.spammerFile.getPath(), new String[0]))) {
            lines.forEach(msg -> this.spamList.add(msg));
        }
        catch (Exception ex) {}
        if (this.timer.hasTimeElapsed((long)(this.delay.getValue().floatValue() * 1000.0f))) {
            if (this.pos == this.spamList.size()) {
                this.pos = 0;
            }
            final String message = this.spamList.get(this.pos);
            ModuleSpammer.mc.player.connection.sendPacket((Packet)new CPacketChatMessage((this.greenText.getValue() ? ">" : "") + message + (this.randomNumbers.getValue() ? (" " + this.randomNumbers()) : "")));
            ++this.pos;
            this.timer.reset();
        }
    }
    
    @Override
    public void onLogin() {
        super.onLogin();
        this.disable(false);
    }
    
    @Override
    public void onLogout() {
        super.onLogout();
        this.disable(false);
    }
    
    @Override
    public void onEnable() {
        super.onEnable();
        this.pos = 0;
        this.spamList.clear();
    }
    
    private String randomNumbers() {
        String finalString = "";
        for (int i = 0; i <= 6; ++i) {
            finalString += (int)MathUtils.randomNumber(9.0, 0.0);
        }
        return finalString;
    }
}
