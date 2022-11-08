package dev.evangelion.client.modules.visuals;

import java.awt.Color;
import dev.evangelion.client.values.impl.ValueColor;
import dev.evangelion.api.manager.module.RegisterModule;
import dev.evangelion.api.manager.module.Module;

@RegisterModule(name = "GlintModify", tag = "Glint Modify", description = "Let's you change your enchanting glint color.", category = Module.Category.VISUALS)
public class ModuleGlintModify extends Module
{
    public final ValueColor color;
    public static ModuleGlintModify INSTANCE;
    
    public ModuleGlintModify() {
        this.color = new ValueColor("Color", "Color", "The color of the Glint.", new Color(0, 0, 255));
        ModuleGlintModify.INSTANCE = this;
    }
}
