package dev.evangelion.client.modules.client;

import java.awt.GraphicsEnvironment;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import dev.evangelion.client.values.Value;
import dev.evangelion.api.utilities.ChatUtils;
import dev.evangelion.api.manager.event.Event;
import dev.evangelion.client.events.EventClient;
import dev.evangelion.api.manager.miscellaneous.FontManager;
import dev.evangelion.client.gui.font.FontRenderer;
import java.awt.Font;
import dev.evangelion.client.values.impl.ValueBoolean;
import dev.evangelion.client.values.impl.ValueNumber;
import dev.evangelion.client.values.impl.ValueEnum;
import dev.evangelion.client.values.impl.ValueString;
import dev.evangelion.api.manager.module.RegisterModule;
import dev.evangelion.api.manager.module.Module;

@RegisterModule(name = "Font", description = "Allows you to customize the client's font.", category = Module.Category.CLIENT)
public class ModuleFont extends Module
{
    public final ValueString name;
    public final ValueEnum style;
    public final ValueNumber size;
    public final ValueBoolean shadow;
    public static ModuleFont INSTANCE;
    boolean reloadFont;
    
    public ModuleFont() {
        this.name = new ValueString("FontName", "Name", "The name for the Font.", "Arial");
        this.style = new ValueEnum("Style", "Style", "Style of the font.", Styles.Plain);
        this.size = new ValueNumber("Size", "Size", "The size for the Font.", 18, 10, 50);
        this.shadow = new ValueBoolean("Shadow", "Shadow", "The shadow for the Font.", true);
        this.reloadFont = false;
        ModuleFont.INSTANCE = this;
    }
    
    public int getStyle() {
        if (this.style.getValue().equals(Styles.Plain)) {
            return 0;
        }
        if (this.style.getValue().equals(Styles.Bold)) {
            return 1;
        }
        if (this.style.getValue().equals(Styles.Italic)) {
            return 2;
        }
        return 3;
    }
    
    @Override
    public void onEnable() {
        super.onEnable();
        FontManager.FONT_RENDERER = new FontRenderer(new Font(this.name.getValue(), this.getStyle(), this.size.getValue().intValue()));
    }
    
    @SubscribeEvent
    public void onSettingChange(final EventClient event) {
        final Value value = event.getValue();
        if (event.getStage() == Event.Stage.POST && value != null && this.getValues().contains(value)) {
            if (value instanceof ValueString) {
                final ValueString valueString = (ValueString)value;
                if (valueString.getName().equals("FontName") && !checkFont(valueString.getValue())) {
                    ChatUtils.sendMessage("That font doesn't exist.", "Font");
                    return;
                }
            }
            this.reloadFont = true;
        }
    }
    
    @Override
    public void onUpdate() {
        super.onUpdate();
        if (this.reloadFont) {
            FontManager.FONT_RENDERER = new FontRenderer(new Font(this.name.getValue(), this.getStyle(), this.size.getValue().intValue()));
            this.reloadFont = false;
        }
    }
    
    public static boolean checkFont(final String font) {
        final String[] availableFontFamilyNames;
        final String[] fonts = availableFontFamilyNames = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
        for (final String s : availableFontFamilyNames) {
            if (s.equals(font)) {
                return true;
            }
        }
        return false;
    }
    
    public enum Styles
    {
        Plain, 
        Italic, 
        Bold, 
        Both;
    }
}
