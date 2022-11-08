package dev.evangelion.client.commands;

import java.util.Iterator;
import dev.evangelion.api.utilities.ChatUtils;
import com.mojang.realmsclient.gui.ChatFormatting;
import dev.evangelion.client.modules.client.ModuleCommands;
import dev.evangelion.api.manager.module.Module;
import dev.evangelion.Evangelion;
import dev.evangelion.api.manager.command.RegisterCommand;
import dev.evangelion.api.manager.command.Command;

@RegisterCommand(name = "drawn", description = "Let's you disable or enable module drawing on the module list.", syntax = "drawn <module> <value>", aliases = { "shown", "show", "draw" })
public class CommandDrawn extends Command
{
    public boolean allDrawn;
    
    public CommandDrawn() {
        this.allDrawn = false;
    }
    
    @Override
    public void onCommand(final String[] args) {
        if (args.length == 2) {
            boolean found = false;
            if (args[0].equalsIgnoreCase("all")) {
                for (final Module m : Evangelion.MODULE_MANAGER.getModules()) {
                    m.setDrawn(Boolean.parseBoolean(args[1]));
                }
                ChatUtils.sendMessage(ModuleCommands.getSecondColor() + "All modules" + ModuleCommands.getFirstColor() + " are now " + (Boolean.parseBoolean(args[1]) ? (ChatFormatting.GREEN + "shown") : (ChatFormatting.RED + "hidden")) + ModuleCommands.getFirstColor() + ".");
            }
            else {
                for (final Module module : Evangelion.MODULE_MANAGER.getModules()) {
                    if (module.getName().equalsIgnoreCase(args[0])) {
                        found = true;
                        module.setDrawn(Boolean.parseBoolean(args[1]));
                        ChatUtils.sendMessage(ModuleCommands.getSecondColor() + module.getName() + ModuleCommands.getFirstColor() + " is now " + (module.isDrawn() ? (ChatFormatting.GREEN + "shown") : (ChatFormatting.RED + "hidden")) + ModuleCommands.getFirstColor() + ".", "Drawn");
                    }
                }
            }
            if (!found) {
                ChatUtils.sendMessage("Could not find module.", "Drawn");
            }
        }
        else {
            this.sendSyntax();
        }
    }
}
