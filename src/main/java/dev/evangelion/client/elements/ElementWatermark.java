package dev.evangelion.client.elements;

import dev.evangelion.client.modules.client.ModuleColor;
import dev.evangelion.Evangelion;
import dev.evangelion.client.events.EventRender2D;
import dev.evangelion.client.values.impl.ValueString;
import dev.evangelion.client.values.impl.ValueEnum;
import dev.evangelion.client.values.impl.ValueCategory;
import dev.evangelion.api.manager.element.RegisterElement;
import dev.evangelion.api.manager.element.Element;

@RegisterElement(name = "Watermark", description = "The watermark for the client.")
public class ElementWatermark extends Element
{
    private final ValueCategory watermarkCategory;
    private final ValueEnum mode;
    private final ValueString customValue;
    private final ValueCategory versionCategory;
    private final ValueEnum version;
    
    public ElementWatermark() {
        this.watermarkCategory = new ValueCategory("Watermark", "The category for the watermark.");
        this.mode = new ValueEnum("Mode", "Mode", "The mode for the watermark.", this.watermarkCategory, Modes.Normal);
        this.customValue = new ValueString("WatermarkValue", "Value", "The value for the Custom Watermark.", this.watermarkCategory, "Evangelion");
        this.versionCategory = new ValueCategory("Version", "The category for the version.");
        this.version = new ValueEnum("Version", "Version", "Renders the Version on the watermark.", this.versionCategory, Versions.Normal);
    }
    
    @Override
    public void onRender2D(final EventRender2D event) {
        super.onRender2D(event);
        this.frame.setWidth(Evangelion.FONT_MANAGER.getStringWidth(this.getText()));
        this.frame.setHeight(Evangelion.FONT_MANAGER.getHeight());
        Evangelion.FONT_MANAGER.drawStringWithShadow(this.getText(), this.frame.getX(), this.frame.getY(), ModuleColor.getColor());
    }
    
    private String getText() {
        return (this.mode.getValue().equals(Modes.Custom) ? this.customValue.getValue() : "Evangelion") + (this.version.getValue().equals(Versions.None) ? "" : (" " + (this.version.getValue().equals(Versions.Normal) ? "v" : "") + "190622"));
    }
    
    public enum Modes
    {
        Normal, 
        Custom;
    }
    
    public enum Versions
    {
        None, 
        Simple, 
        Normal;
    }
}
