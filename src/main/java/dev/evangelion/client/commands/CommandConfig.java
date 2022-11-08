package dev.evangelion.client.commands;

import dev.evangelion.api.utilities.ChatUtils;
import dev.evangelion.Evangelion;
import dev.evangelion.api.manager.command.RegisterCommand;
import dev.evangelion.api.manager.command.Command;

@RegisterCommand(name = "config", description = "Let's you save or load your configuration without restarting the game.", syntax = "config <save|load>", aliases = { "configuration", "cfg" })
public class CommandConfig extends Command
{
    @Override
    public void onCommand(final String[] args) {
        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("load")) {
                Evangelion.CONFIG_MANAGER.load();
                ChatUtils.sendMessage("Successfully loaded configuration.", "Config");
            }
            else if (args[0].equalsIgnoreCase("save")) {
                Evangelion.CONFIG_MANAGER.save();
                ChatUtils.sendMessage("Successfully saved configuration.", "Config");
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
