package dev.evangelion.client.commands;

import java.util.Iterator;
import dev.evangelion.api.utilities.ChatUtils;
import com.mojang.realmsclient.gui.ChatFormatting;
import dev.evangelion.client.modules.client.ModuleCommands;
import dev.evangelion.api.manager.module.Module;
import dev.evangelion.Evangelion;
import dev.evangelion.api.manager.command.RegisterCommand;
import dev.evangelion.api.manager.command.Command;

@RegisterCommand(name = "notify", description = "Let's you disable or enable module toggle messages.", syntax = "notify <module> <value>", aliases = { "chatnotify", "togglemsg", "togglemsgs", "togglemessages" })
public class CommandNotify extends Command
{
    @Override
    public void onCommand(final String[] args) {
        if (args.length == 2) {
            boolean found = false;
            for (final Module module : Evangelion.MODULE_MANAGER.getModules()) {
                if (module.getName().equalsIgnoreCase(args[0])) {
                    found = true;
                    module.setChatNotify(Boolean.parseBoolean(args[1]));
                    ChatUtils.sendMessage(ModuleCommands.getSecondColor() + module.getName() + ModuleCommands.getFirstColor() + " now has Toggle Messages " + (module.isChatNotify() ? (ChatFormatting.GREEN + "enabled") : (ChatFormatting.RED + "disabled")) + ModuleCommands.getFirstColor() + ".", "Notify");
                }
            }
            if (!found) {
                ChatUtils.sendMessage("Could not find module.", "Notify");
            }
        }
        else {
            this.sendSyntax();
        }
    }
}
