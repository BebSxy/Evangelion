package dev.evangelion.client.values.impl;

import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.common.MinecraftForge;
import dev.evangelion.client.events.EventClient;
import dev.evangelion.client.values.Value;

public class ValueBoolean extends Value
{
    private final boolean defaultValue;
    private boolean value;
    private final ValueCategory parent;
    
    public ValueBoolean(final String name, final String tag, final String description, final boolean value) {
        super(name, tag, description);
        this.defaultValue = value;
        this.value = value;
        this.parent = null;
    }
    
    public ValueBoolean(final String name, final String tag, final String description, final ValueCategory parent, final boolean value) {
        super(name, tag, description);
        this.defaultValue = value;
        this.value = value;
        this.parent = parent;
    }
    
    public ValueCategory getParent() {
        return this.parent;
    }
    
    public boolean getDefaultValue() {
        return this.defaultValue;
    }
    
    public boolean getValue() {
        return this.value;
    }
    
    public void setValue(final boolean value) {
        this.value = value;
        final EventClient event = new EventClient(this);
        MinecraftForge.EVENT_BUS.post((Event)event);
    }
}
