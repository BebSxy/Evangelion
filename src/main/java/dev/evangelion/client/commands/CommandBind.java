package dev.evangelion.client.commands;

import java.util.Iterator;
import dev.evangelion.api.utilities.ChatUtils;
import dev.evangelion.client.modules.client.ModuleCommands;
import org.lwjgl.input.Keyboard;
import dev.evangelion.api.manager.module.Module;
import dev.evangelion.Evangelion;
import dev.evangelion.api.manager.command.RegisterCommand;
import dev.evangelion.api.manager.command.Command;

@RegisterCommand(name = "bind", description = "Let's you bind a module with commands.", syntax = "bind <name> <key> | clear", aliases = { "key", "keybind", "b" })
public class CommandBind extends Command
{
    @Override
    public void onCommand(final String[] args) {
        if (args.length == 2) {
            final String name = args[0];
            final String key = args[1];
            boolean found = false;
            for (final Module module : Evangelion.MODULE_MANAGER.getModules()) {
                if (module.getName().equalsIgnoreCase(name)) {
                    module.setBind(Keyboard.getKeyIndex(key.toUpperCase()));
                    ChatUtils.sendMessage("Bound " + ModuleCommands.getSecondColor() + module.getTag() + ModuleCommands.getFirstColor() + " to " + ModuleCommands.getSecondColor() + Keyboard.getKeyName(module.getBind()).toUpperCase() + ModuleCommands.getFirstColor() + ".", "Bind");
                    found = true;
                    break;
                }
            }
            if (!found) {
                ChatUtils.sendMessage("Could not find module.", "Bind");
            }
        }
        else if (args.length == 1) {
            if (args[0].equalsIgnoreCase("clear")) {
                for (final Module module2 : Evangelion.MODULE_MANAGER.getModules()) {
                    module2.setBind(0);
                }
                ChatUtils.sendMessage("Successfully cleared all binds.", "Bind");
            }
            else {
                this.sendSyntax();
            }
        }
        else {
            this.sendSyntax();
        }
    }
}
