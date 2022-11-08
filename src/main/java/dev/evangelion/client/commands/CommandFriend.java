package dev.evangelion.client.commands;

import com.mojang.realmsclient.gui.ChatFormatting;
import dev.evangelion.client.modules.client.ModuleCommands;
import dev.evangelion.api.utilities.ChatUtils;
import dev.evangelion.Evangelion;
import dev.evangelion.api.manager.command.RegisterCommand;
import dev.evangelion.api.manager.command.Command;

@RegisterCommand(name = "Friend", description = "Let's you add friends.", syntax = "friend <add/del> <name>", aliases = { "friend", "f" })
public class CommandFriend extends Command
{
    @Override
    public void onCommand(final String[] args) {
        if (args.length == 1) {
            ChatUtils.sendMessage("You have " + (Evangelion.FRIEND_MANAGER.getFriends().size() + 1) + " friends");
            return;
        }
        if (args.length == 2) {
            if (args[0].equalsIgnoreCase("add")) {
                if (Evangelion.FRIEND_MANAGER.isFriend(args[1])) {
                    ChatUtils.sendMessage(ModuleCommands.getSecondColor() + args[1] + ModuleCommands.getFirstColor() + " is already a friend!");
                    return;
                }
                if (!Evangelion.FRIEND_MANAGER.isFriend(args[1])) {
                    Evangelion.FRIEND_MANAGER.addFriend(args[1]);
                    ChatUtils.sendMessage(ChatFormatting.GREEN + "Added " + ModuleCommands.getSecondColor() + args[1] + ModuleCommands.getFirstColor() + " to friends list");
                }
            }
            if (args[0].equalsIgnoreCase("del") || args[0].equalsIgnoreCase("remove")) {
                if (!Evangelion.FRIEND_MANAGER.isFriend(args[1])) {
                    ChatUtils.sendMessage(ModuleCommands.getSecondColor() + args[1] + ModuleCommands.getFirstColor() + " is not a friend!");
                    return;
                }
                if (Evangelion.FRIEND_MANAGER.isFriend(args[1])) {
                    Evangelion.FRIEND_MANAGER.removeFriend(args[1]);
                    ChatUtils.sendMessage(ChatFormatting.RED + "Removed " + ModuleCommands.getSecondColor() + args[1] + ModuleCommands.getFirstColor() + " from friends list");
                }
            }
        }
        else {
            this.sendSyntax();
        }
    }
}
