package dev.evangelion.api.manager.command;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import java.util.Iterator;
import dev.evangelion.api.utilities.ChatUtils;
import java.util.Arrays;
import net.minecraftforge.client.event.ClientChatEvent;
import dev.evangelion.client.commands.CommandFriend;
import dev.evangelion.client.commands.CommandToggle;
import dev.evangelion.client.commands.CommandTag;
import dev.evangelion.client.commands.CommandPrefix;
import dev.evangelion.client.commands.CommandNotify;
import dev.evangelion.client.commands.CommandFolder;
import dev.evangelion.client.commands.CommandDrawn;
import dev.evangelion.client.commands.CommandConfig;
import dev.evangelion.client.commands.CommandBind;
import net.minecraftforge.common.MinecraftForge;
import java.util.ArrayList;
import dev.evangelion.api.utilities.IMinecraft;

public class CommandManager implements IMinecraft
{
    private String prefix;
    private final ArrayList<Command> commands;
    
    public CommandManager() {
        this.prefix = ".";
        MinecraftForge.EVENT_BUS.register((Object)this);
        this.commands = new ArrayList<Command>();
        this.register(new CommandBind());
        this.register(new CommandConfig());
        this.register(new CommandDrawn());
        this.register(new CommandFolder());
        this.register(new CommandNotify());
        this.register(new CommandPrefix());
        this.register(new CommandTag());
        this.register(new CommandToggle());
        this.register(new CommandFriend());
    }
    
    public void register(final Command command) {
        this.commands.add(command);
    }
    
    @SubscribeEvent
    public void onChatSent(final ClientChatEvent event) {
        String message = event.getMessage();
        if (message.startsWith(this.getPrefix())) {
            event.setCanceled(true);
            message = message.substring(this.getPrefix().length());
            if (message.split(" ").length > 0) {
                final String name = message.split(" ")[0];
                boolean found = false;
                for (final Command command : this.getCommands()) {
                    if (command.getAliases().contains(name.toLowerCase()) || command.getName().equalsIgnoreCase(name)) {
                        CommandManager.mc.ingameGUI.getChatGUI().addToSentMessages(event.getMessage());
                        command.onCommand(Arrays.copyOfRange(message.split(" "), 1, message.split(" ").length));
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    ChatUtils.sendMessage("Command could not be found.");
                }
            }
        }
    }
    
    public ArrayList<Command> getCommands() {
        return this.commands;
    }
    
    public String getPrefix() {
        return this.prefix;
    }
    
    public void setPrefix(final String prefix) {
        this.prefix = prefix;
    }
}
