package dev.evangelion.client.commands;

import java.io.IOException;
import dev.evangelion.api.utilities.ChatUtils;
import java.io.File;
import java.awt.Desktop;
import dev.evangelion.api.manager.command.RegisterCommand;
import dev.evangelion.api.manager.command.Command;

@RegisterCommand(name = "folder", description = "Opens the Configuration folder.", syntax = "folder", aliases = { "openfolder", "cfgfldr", "cfgfolder", "configfolder" })
public class CommandFolder extends Command
{
    @Override
    public void onCommand(final String[] args) {
        try {
            Desktop.getDesktop().open(new File("Evangelion"));
            ChatUtils.sendMessage("Successfully opened configuration folder.", "Folder");
        }
        catch (IOException exception) {
            ChatUtils.sendMessage("Could not open configuration folder.", "Folder");
            exception.printStackTrace();
        }
    }
}
