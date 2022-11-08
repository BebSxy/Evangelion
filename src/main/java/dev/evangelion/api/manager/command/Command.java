package dev.evangelion.api.manager.command;

import dev.evangelion.api.utilities.ChatUtils;
import java.util.Arrays;
import java.util.List;
import dev.evangelion.api.utilities.IMinecraft;

public abstract class Command implements IMinecraft
{
    private final String name;
    private final String description;
    private final String syntax;
    private final List<String> aliases;
    
    public Command() {
        final RegisterCommand annotation = this.getClass().getAnnotation(RegisterCommand.class);
        this.name = annotation.name();
        this.description = annotation.description();
        this.syntax = annotation.syntax();
        this.aliases = Arrays.asList(annotation.aliases());
    }
    
    public abstract void onCommand(final String[] p0);
    
    public String getName() {
        return this.name;
    }
    
    public String getDescription() {
        return this.description;
    }
    
    public String getSyntax() {
        return this.syntax;
    }
    
    public List<String> getAliases() {
        return this.aliases;
    }
    
    public void sendSyntax() {
        ChatUtils.sendMessage(this.getSyntax());
    }
}
