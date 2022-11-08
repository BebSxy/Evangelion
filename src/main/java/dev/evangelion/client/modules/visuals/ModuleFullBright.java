package dev.evangelion.client.modules.visuals;

import dev.evangelion.api.manager.module.RegisterModule;
import dev.evangelion.api.manager.module.Module;

@RegisterModule(name = "FullBright", tag = "Full Bright", description = "Makes it so that the world is always at maximum brightness.", category = Module.Category.VISUALS)
public class ModuleFullBright extends Module
{
    private float oldBrightness;
    
    public ModuleFullBright() {
        this.oldBrightness = 1.0f;
    }
    
    @Override
    public void onUpdate() {
        super.onUpdate();
        ModuleFullBright.mc.gameSettings.gammaSetting = 100.0f;
    }
    
    @Override
    public void onEnable() {
        super.onEnable();
        if (ModuleFullBright.mc.gameSettings != null) {
            this.oldBrightness = ModuleFullBright.mc.gameSettings.gammaSetting;
        }
    }
    
    @Override
    public void onDisable() {
        super.onDisable();
        ModuleFullBright.mc.gameSettings.gammaSetting = this.oldBrightness;
        this.oldBrightness = -1.0f;
    }
}
