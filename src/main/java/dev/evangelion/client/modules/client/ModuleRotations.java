package dev.evangelion.client.modules.client;

import dev.evangelion.client.values.impl.ValueNumber;
import dev.evangelion.api.manager.module.RegisterModule;
import dev.evangelion.api.manager.module.Module;

@RegisterModule(name = "Rotations", description = "Rotations of the client", category = Module.Category.CLIENT)
public class ModuleRotations extends Module
{
    public static ModuleRotations INSTANCE;
    public ValueNumber smoothness;
    
    public ModuleRotations() {
        this.smoothness = new ValueNumber("RotationSmoothness", "Smoothness", "", 60, 1, 100);
        ModuleRotations.INSTANCE = this;
    }
}
