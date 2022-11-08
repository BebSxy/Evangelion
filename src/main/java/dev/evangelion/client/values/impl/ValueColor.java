package dev.evangelion.client.values.impl;

import dev.evangelion.api.utilities.ColorUtils;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.common.MinecraftForge;
import dev.evangelion.client.events.EventClient;
import dev.evangelion.client.modules.client.ModuleColor;
import java.awt.Color;
import dev.evangelion.client.values.Value;

public class ValueColor extends Value
{
    private final Color defaultValue;
    private Color value;
    private boolean rainbow;
    private boolean sync;
    private final ValueCategory parent;
    
    public ValueColor(final String name, final String tag, final String description, final Color value) {
        super(name, tag, description);
        this.defaultValue = value;
        this.value = value;
        this.parent = null;
        this.rainbow = false;
        this.sync = false;
    }
    
    public ValueColor(final String name, final String tag, final String description, final ValueCategory parent, final Color value) {
        super(name, tag, description);
        this.defaultValue = value;
        this.value = value;
        this.parent = parent;
        this.rainbow = false;
        this.sync = false;
    }
    
    public ValueCategory getParent() {
        return this.parent;
    }
    
    public Color getDefaultValue() {
        return this.defaultValue;
    }
    
    public Color getActualValue() {
        return this.value;
    }
    
    public Color getValue() {
        if (this.sync && this != ModuleColor.INSTANCE.color) {
            return new Color(ModuleColor.getColor().getRed(), ModuleColor.getColor().getGreen(), ModuleColor.getColor().getBlue(), this.value.getAlpha());
        }
        this.doRainbow();
        return this.value;
    }
    
    public void setValue(final Color value) {
        this.value = value;
        final EventClient event = new EventClient(this);
        MinecraftForge.EVENT_BUS.post((Event)event);
    }
    
    public boolean isRainbow() {
        return this.rainbow;
    }
    
    public void setRainbow(final boolean rainbow) {
        this.rainbow = rainbow;
    }
    
    public boolean isSync() {
        return this.sync;
    }
    
    public void setSync(final boolean sync) {
        this.sync = sync;
    }
    
    private void doRainbow() {
        if (this.rainbow) {
            final Color rainbowColor = ColorUtils.rainbow(1);
            this.setValue(new Color(rainbowColor.getRed(), rainbowColor.getGreen(), rainbowColor.getBlue(), this.value.getAlpha()));
        }
    }
}
