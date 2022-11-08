package dev.evangelion.client.modules.visuals;

import dev.evangelion.client.values.impl.ValueNumber;
import dev.evangelion.api.manager.module.RegisterModule;
import dev.evangelion.api.manager.module.Module;

@RegisterModule(name = "GameSettings", tag = "Game Settings", description = "Let's you change game settings more accurately.", category = Module.Category.VISUALS)
public class ModuleGameSettings extends Module
{
    ValueNumber fov;
    float oldFov;
    
    public ModuleGameSettings() {
        this.fov = new ValueNumber("FOV", "FOV", "The FOV of the game.", 130, 1, 180);
    }
    
    @Override
    public void onUpdate() {
        super.onUpdate();
        ModuleGameSettings.mc.gameSettings.fovSetting = (float)this.fov.getValue().intValue();
    }
    
    @Override
    public void onEnable() {
        super.onEnable();
        this.oldFov = ModuleGameSettings.mc.gameSettings.fovSetting;
    }
    
    @Override
    public void onDisable() {
        super.onDisable();
        ModuleGameSettings.mc.gameSettings.fovSetting = this.oldFov;
    }
}
