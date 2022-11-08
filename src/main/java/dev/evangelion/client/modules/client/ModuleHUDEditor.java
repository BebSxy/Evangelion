package dev.evangelion.client.modules.client;

import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.client.gui.GuiScreen;
import dev.evangelion.Evangelion;
import dev.evangelion.api.manager.module.RegisterModule;
import dev.evangelion.api.manager.module.Module;

@RegisterModule(name = "HUDEditor", tag = "HUD Editor", description = "The client's HUD Editor.", category = Module.Category.CLIENT)
public class ModuleHUDEditor extends Module
{
    public static ModuleHUDEditor INSTANCE;
    
    public ModuleHUDEditor() {
        ModuleHUDEditor.INSTANCE = this;
    }
    
    @Override
    public void onEnable() {
        super.onEnable();
        if (ModuleHUDEditor.mc.player == null || ModuleHUDEditor.mc.world == null) {
            this.disable(false);
            return;
        }
        ModuleHUDEditor.mc.displayGuiScreen((GuiScreen)Evangelion.HUD_EDITOR);
    }
    
    public ChatFormatting getSecondColor() {
        return ChatFormatting.WHITE;
    }
    
    public enum secondColors
    {
        Normal, 
        Gray, 
        DarkGray, 
        White;
    }
}
