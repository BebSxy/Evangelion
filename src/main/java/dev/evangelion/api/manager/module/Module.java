package dev.evangelion.api.manager.module;

import dev.evangelion.api.utilities.ChatUtils;
import com.mojang.realmsclient.gui.ChatFormatting;
import dev.evangelion.client.modules.client.ModuleCommands;
import dev.evangelion.client.events.EventRender3D;
import dev.evangelion.client.events.EventRender2D;
import java.util.Random;
import net.minecraftforge.common.MinecraftForge;
import dev.evangelion.client.values.impl.ValueBind;
import dev.evangelion.client.values.impl.ValueBoolean;
import dev.evangelion.client.values.impl.ValueString;
import java.awt.Color;
import dev.evangelion.client.values.Value;
import java.util.ArrayList;
import dev.evangelion.api.utilities.IMinecraft;

public abstract class Module implements IMinecraft
{
    private ArrayList<Value> values;
    public String name;
    public String description;
    public Category category;
    private boolean toggled;
    private boolean persistent;
    private Color randomColor;
    public ValueString tag;
    public ValueBoolean chatNotify;
    public ValueBoolean drawn;
    public ValueBind bind;
    
    public Module() {
        this.values = null;
        this.persistent = false;
        this.tag = new ValueString("Tag", "Tag", "The module's display name.", "4GquuoBHl7gkSDaNeMb5");
        this.chatNotify = new ValueBoolean("ChatNotify", "Chat Notify", "Notifies you in chat when the module is toggled on or off.", true);
        this.drawn = new ValueBoolean("Drawn", "Drawn", "Makes the module appear on the array list.", true);
        this.bind = new ValueBind("Bind", "Bind", "The module's toggle bind.", 0);
        final RegisterModule annotation = this.getClass().getAnnotation(RegisterModule.class);
        if (annotation != null) {
            this.name = annotation.name();
            this.tag.setValue(annotation.tag().equals("4GquuoBHl7gkSDaNeMb5") ? annotation.name() : annotation.tag());
            this.description = annotation.description();
            this.category = annotation.category();
            this.persistent = annotation.persistent();
            this.bind.setValue(annotation.bind());
            this.values = new ArrayList<Value>();
            if (this.persistent) {
                this.setToggled(true);
                MinecraftForge.EVENT_BUS.register((Object)this);
            }
            this.randomColor = this.generateRandomColor();
        }
    }
    
    public Color generateRandomColor() {
        final Random random = new Random();
        final int randomRed = random.nextInt(255);
        final int randomGreen = random.nextInt(255);
        final int randomBlue = random.nextInt(255);
        return new Color(randomRed, randomGreen, randomBlue);
    }
    
    public void onTick() {
    }
    
    public void onUpdate() {
    }
    
    public void onRender2D(final EventRender2D event) {
    }
    
    public void onRender3D(final EventRender3D event) {
    }
    
    public void onEnable() {
    }
    
    public void onDisable() {
    }
    
    public void onLogin() {
    }
    
    public void onLogout() {
    }
    
    public void onDeath() {
    }
    
    public boolean nullCheck() {
        return Module.mc.player == null || Module.mc.world == null;
    }
    
    public String getHudInfo() {
        return "";
    }
    
    public String getName() {
        return this.name;
    }
    
    public String getDescription() {
        return this.description;
    }
    
    public Category getCategory() {
        return this.category;
    }
    
    public boolean isToggled() {
        return this.toggled;
    }
    
    public void setToggled(final boolean toggled) {
        this.toggled = toggled;
    }
    
    public boolean isPersistent() {
        return this.persistent;
    }
    
    public String getTag() {
        return this.tag.getValue();
    }
    
    public void setTag(final String tag) {
        this.tag.setValue(tag);
    }
    
    public boolean isChatNotify() {
        return this.chatNotify.getValue();
    }
    
    public void setChatNotify(final boolean chatNotify) {
        this.chatNotify.setValue(chatNotify);
    }
    
    public boolean isDrawn() {
        return this.drawn.getValue();
    }
    
    public void setDrawn(final boolean drawn) {
        this.drawn.setValue(drawn);
    }
    
    public int getBind() {
        return this.bind.getValue();
    }
    
    public void setBind(final int bind) {
        this.bind.setValue(bind);
    }
    
    public Color getRandomColor() {
        return this.randomColor;
    }
    
    public void toggle(final boolean message) {
        if (this.isToggled()) {
            this.disable(message);
        }
        else {
            this.enable(message);
        }
    }
    
    public void enable(final boolean message) {
        if (!this.persistent) {
            this.setToggled(true);
            MinecraftForge.EVENT_BUS.register((Object)this);
            if (message) {
                this.doToggleMessage();
            }
            this.onEnable();
        }
    }
    
    public void disable(final boolean message) {
        if (!this.persistent) {
            this.setToggled(false);
            MinecraftForge.EVENT_BUS.unregister((Object)this);
            if (message) {
                this.doToggleMessage();
            }
            this.onDisable();
        }
    }
    
    public void doToggleMessage() {
        if (this.isChatNotify()) {
            int number = 0;
            for (final char character : this.getTag().toCharArray()) {
                number += character;
                number *= 10;
            }
            ChatUtils.sendMessage(ModuleCommands.getSecondColor() + "" + ChatFormatting.BOLD + this.getTag() + " " + ModuleCommands.getFirstColor() + "has been toggled " + (this.isToggled() ? (ChatFormatting.GREEN + "on") : (ChatFormatting.RED + "off")) + ModuleCommands.getFirstColor() + "!", number);
        }
    }
    
    public ArrayList<Value> getValues() {
        return this.values;
    }
    
    public enum Category
    {
        COMBAT("Combat"), 
        PLAYER("Player"), 
        MISCELLANEOUS("Miscellaneous"), 
        MOVEMENT("Movement"), 
        VISUALS("Visuals"), 
        CLIENT("Client"), 
        HUD("HUD");
        
        private final String name;
        
        private Category(final String name) {
            this.name = name;
        }
        
        public String getName() {
            return this.name;
        }
    }
}
