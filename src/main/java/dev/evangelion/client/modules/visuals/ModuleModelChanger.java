package dev.evangelion.client.modules.visuals;

import dev.evangelion.client.values.impl.ValueNumber;
import dev.evangelion.client.values.impl.ValueCategory;
import dev.evangelion.api.manager.module.RegisterModule;
import dev.evangelion.api.manager.module.Module;

@RegisterModule(name = "ModelChanger", tag = "Model Changer", description = "Let's you change your viewmodel.", category = Module.Category.VISUALS)
public class ModuleModelChanger extends Module
{
    public final ValueCategory translateCategory;
    public final ValueNumber translateX;
    public final ValueNumber translateY;
    public final ValueNumber translateZ;
    public final ValueCategory rotateCategory;
    public final ValueNumber rotateX;
    public final ValueNumber rotateY;
    public final ValueNumber rotateZ;
    public final ValueCategory scaleCategory;
    public final ValueNumber scaleX;
    public final ValueNumber scaleY;
    public final ValueNumber scaleZ;
    public static ModuleModelChanger INSTANCE;
    
    public ModuleModelChanger() {
        this.translateCategory = new ValueCategory("Translate", "The category for the Translation.");
        this.translateX = new ValueNumber("TranslateX", "X", "The X for the Translation.", this.translateCategory, 0, -360, 360);
        this.translateY = new ValueNumber("TranslateY", "Y", "The Y for the Translation.", this.translateCategory, 0, -360, 360);
        this.translateZ = new ValueNumber("TranslateZ", "Z", "The Z for the Translation.", this.translateCategory, 0, -360, 360);
        this.rotateCategory = new ValueCategory("Rotate", "The category for the Rotation.");
        this.rotateX = new ValueNumber("RotateX", "X", "The X for the Rotation.", this.rotateCategory, 0, -360, 360);
        this.rotateY = new ValueNumber("RotateY", "Y", "The Y for the Rotation.", this.rotateCategory, 0, -360, 360);
        this.rotateZ = new ValueNumber("RotateZ", "Z", "The Z for the Rotation.", this.rotateCategory, 0, -360, 360);
        this.scaleCategory = new ValueCategory("Scale", "The category for the Scaling.");
        this.scaleX = new ValueNumber("ScaleX", "X", "The X for the Scaling.", this.scaleCategory, 1.0f, 0.0f, 5.0f);
        this.scaleY = new ValueNumber("ScaleY", "Y", "The Y for the Scaling.", this.scaleCategory, 1.0f, 0.0f, 5.0f);
        this.scaleZ = new ValueNumber("ScaleZ", "Z", "The Z for the Scaling.", this.scaleCategory, 1.0f, 0.0f, 5.0f);
        ModuleModelChanger.INSTANCE = this;
    }
}
