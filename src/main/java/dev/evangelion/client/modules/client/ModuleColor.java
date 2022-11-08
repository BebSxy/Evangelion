package dev.evangelion.client.modules.client;

import java.awt.Color;
import dev.evangelion.client.values.impl.ValueNumber;
import dev.evangelion.client.values.impl.ValueCategory;
import dev.evangelion.client.values.impl.ValueColor;
import dev.evangelion.api.manager.module.RegisterModule;
import dev.evangelion.api.manager.module.Module;

@RegisterModule(name = "Color", description = "Manages the client's global color.", category = Module.Category.CLIENT, persistent = true)
public class ModuleColor extends Module
{
    public static ModuleColor INSTANCE;
    public final ValueColor color;
    ValueCategory rainbowCategory;
    public ValueNumber rainbowOffset;
    public ValueNumber rainbowSat;
    public ValueNumber rainbowBri;
    
    public ModuleColor() {
        this.color = new ValueColor("Color", "Color", "The client's global color.", new Color(255, 0, 0));
        this.rainbowCategory = new ValueCategory("Rainbow", "Manage rainbow");
        this.rainbowOffset = new ValueNumber("RainbowOffset", "Offset", "", this.rainbowCategory, 255, 0, 1000);
        this.rainbowSat = new ValueNumber("RainbowSaturation", "Saturation", "", this.rainbowCategory, 255, 0, 255);
        this.rainbowBri = new ValueNumber("RainbowBrightness", "Brightness", "", this.rainbowCategory, 255, 0, 255);
        ModuleColor.INSTANCE = this;
    }
    
    public static Color getColor() {
        final Color color = (ModuleColor.INSTANCE == null) ? new Color(255, 255, 255) : ModuleColor.INSTANCE.color.getValue();
        return new Color(color.getRed(), color.getGreen(), color.getBlue(), 255);
    }
    
    public static Color getColor(final int alpha) {
        final Color color = (ModuleColor.INSTANCE == null) ? new Color(255, 255, 255) : ModuleColor.INSTANCE.color.getValue();
        return new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha);
    }
}
