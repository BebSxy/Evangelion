package dev.evangelion.client.modules.visuals;

import dev.evangelion.client.values.impl.ValueNumber;
import dev.evangelion.client.values.impl.ValueBoolean;
import dev.evangelion.api.manager.module.RegisterModule;
import dev.evangelion.api.manager.module.Module;

@RegisterModule(name = "ViewClip", tag = "View Clip", description = "Let's you customize your third person.", category = Module.Category.VISUALS)
public class ModuleViewClip extends Module
{
    public final ValueBoolean extend;
    public final ValueNumber distance;
    public static ModuleViewClip INSTANCE;
    
    public ModuleViewClip() {
        this.extend = new ValueBoolean("Extend", "Extend", "Extends your distance.", false);
        this.distance = new ValueNumber("Distance", "Distance", "The distance that the viewclip should be extended from.", 3.0, 0.0, 50.0);
        ModuleViewClip.INSTANCE = this;
    }
}
