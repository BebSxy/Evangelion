package dev.evangelion.client.values.impl;

import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.common.MinecraftForge;
import dev.evangelion.client.events.EventClient;
import dev.evangelion.client.values.Value;

public class ValueString extends Value
{
    private final String defaultValue;
    private String value;
    private final ValueCategory parent;
    
    public ValueString(final String name, final String tag, final String description, final String value) {
        super(name, tag, description);
        this.defaultValue = value;
        this.value = value;
        this.parent = null;
    }
    
    public ValueString(final String name, final String tag, final String description, final ValueCategory parent, final String value) {
        super(name, tag, description);
        this.defaultValue = value;
        this.value = value;
        this.parent = parent;
    }
    
    public ValueCategory getParent() {
        return this.parent;
    }
    
    public String getDefaultValue() {
        return this.defaultValue;
    }
    
    public String getValue() {
        return this.value;
    }
    
    public void setValue(final String value) {
        this.value = value;
        final EventClient event = new EventClient(this);
        MinecraftForge.EVENT_BUS.post((Event)event);
    }
}
