package dev.evangelion.client.modules.combat;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiMainMenu;
import dev.evangelion.client.values.impl.ValueBoolean;
import dev.evangelion.client.values.impl.ValueNumber;
import dev.evangelion.api.manager.module.RegisterModule;
import dev.evangelion.api.manager.module.Module;

@RegisterModule(name = "Autolog", description = "Automatically log out when you reach a certain hp level", category = Module.Category.COMBAT)
public class ModuleAutoLog extends Module
{
    ValueNumber health;
    ValueBoolean logToggle;
    
    public ModuleAutoLog() {
        this.health = new ValueNumber("Health", "Health", "The health level to log out with.", 12, 0, 24);
        this.logToggle = new ValueBoolean("LogToggle", "Log Toggle", "Whether or not to toggle the module when logging.", false);
    }
    
    @Override
    public void onUpdate() {
        super.onUpdate();
        if (ModuleAutoLog.mc.player.getHealth() <= this.health.getValue().floatValue()) {
            ModuleAutoLog.mc.world.sendQuittingDisconnectingPacket();
            ModuleAutoLog.mc.displayGuiScreen((GuiScreen)new GuiMainMenu());
        }
    }
    
    @Override
    public void onLogin() {
        super.onLogin();
        if (this.logToggle.getValue()) {
            this.disable(false);
        }
    }
    
    @Override
    public void onLogout() {
        super.onLogout();
        if (this.logToggle.getValue()) {
            this.disable(false);
        }
    }
}
