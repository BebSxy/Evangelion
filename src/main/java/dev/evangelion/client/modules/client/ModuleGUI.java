package dev.evangelion.client.modules.client;

import net.minecraft.client.gui.GuiScreen;
import dev.evangelion.Evangelion;
import dev.evangelion.client.values.impl.ValueBoolean;
import dev.evangelion.client.values.impl.ValueNumber;
import dev.evangelion.api.manager.module.RegisterModule;
import dev.evangelion.api.manager.module.Module;

@RegisterModule(name = "GUI", description = "The client's GUI interface for interacting with modules and settings.", category = Module.Category.CLIENT, bind = 54)
public class ModuleGUI extends Module
{
    public static ModuleGUI INSTANCE;
    public ValueNumber scrollSpeed;
    public ValueBoolean rectEnabled;
    public ValueBoolean fadeText;
    public ValueNumber fadeOffset;
    
    public ModuleGUI() {
        this.scrollSpeed = new ValueNumber("ScrollSpeed", "Scroll Speed", "The speed for scrolling through the GUI.", 10, 1, 50);
        this.rectEnabled = new ValueBoolean("RectEnabled", "Rect Enabled", "Render a rectangle behind enabled modules.", true);
        this.fadeText = new ValueBoolean("FadeText", "Fade Text", "Add cool animation to the text of the GUI.", false);
        this.fadeOffset = new ValueNumber("FadeOffset", "Fade Offset", "Offset for the text animation of the GUI.", 100, 0, 255);
        ModuleGUI.INSTANCE = this;
    }
    
    @Override
    public void onEnable() {
        if (ModuleGUI.mc.player == null || ModuleGUI.mc.world == null || Evangelion.CLICK_GUI == null) {
            this.disable(false);
            return;
        }
        ModuleGUI.mc.displayGuiScreen((GuiScreen)Evangelion.CLICK_GUI);
    }
}
