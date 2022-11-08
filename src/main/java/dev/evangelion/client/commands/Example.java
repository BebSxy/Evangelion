package dev.evangelion.client.commands;

import dev.evangelion.api.manager.command.RegisterCommand;
import dev.evangelion.api.manager.command.Command;

@RegisterCommand(name = "", description = "", syntax = "", aliases = {})
public class Example extends Command
{
    @Override
    public void onCommand(final String[] args) {
    }
}
