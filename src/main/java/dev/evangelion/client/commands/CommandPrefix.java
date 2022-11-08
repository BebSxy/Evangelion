package dev.evangelion.client.commands;

import dev.evangelion.client.modules.client.ModuleCommands;
import dev.evangelion.Evangelion;
import dev.evangelion.api.utilities.ChatUtils;
import dev.evangelion.api.manager.command.RegisterCommand;
import dev.evangelion.api.manager.command.Command;

@RegisterCommand(name = "Prefix", description = "Let's you change your command prefix.", syntax = "prefix <input>", aliases = { "commandprefix", "cmdprefix", "commandp", "cmdp" })
public class CommandPrefix extends Command
{
    @Override
    public void onCommand(final String[] args) {
        if (args.length == 1) {
            if (args[0].length() > 2) {
                ChatUtils.sendMessage("The prefix must not be longer than 2 characters.", "Prefix");
            }
            else {
                Evangelion.COMMAND_MANAGER.setPrefix(args[0]);
                ChatUtils.sendMessage("Prefix set to \"" + ModuleCommands.getSecondColor() + Evangelion.COMMAND_MANAGER.getPrefix() + ModuleCommands.getFirstColor() + "\"!", "Prefix");
            }
        }
        else {
            this.sendSyntax();
        }
    }
}
