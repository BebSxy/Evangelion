package dev.evangelion.client.commands;

import java.util.Iterator;
import dev.evangelion.api.utilities.ChatUtils;
import com.mojang.realmsclient.gui.ChatFormatting;
import dev.evangelion.client.modules.client.ModuleCommands;
import dev.evangelion.api.manager.module.Module;
import dev.evangelion.Evangelion;
import dev.evangelion.api.manager.command.RegisterCommand;
import dev.evangelion.api.manager.command.Command;

@RegisterCommand(name = "toggle", description = "Let's you toggle a module by name.", syntax = "toggle <module>", aliases = { "t" })
public class CommandToggle extends Command
{
    @Override
    public void onCommand(final String[] args) {
        if (args.length == 1) {
            boolean found = false;
            for (final Module module : Evangelion.MODULE_MANAGER.getModules()) {
                if (module.getName().equalsIgnoreCase(args[0])) {
                    module.toggle(false);
                    ChatUtils.sendMessage(ModuleCommands.getSecondColor() + "" + ChatFormatting.BOLD + module.getTag() + ModuleCommands.getFirstColor() + " has been toggled " + (module.isToggled() ? (ChatFormatting.GREEN + "on") : (ChatFormatting.RED + "off")) + ModuleCommands.getFirstColor() + "!", "Toggle");
                    found = true;
                    break;
                }
            }
            if (!found) {
                ChatUtils.sendMessage("Could not find module.", "Toggle");
            }
        }
        else {
            this.sendSyntax();
        }
    }
}
