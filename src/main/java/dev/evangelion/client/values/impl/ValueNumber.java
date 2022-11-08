package dev.evangelion.client.values.impl;

import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.common.MinecraftForge;
import dev.evangelion.client.events.EventClient;
import dev.evangelion.client.values.Value;

public class ValueNumber extends Value
{
    public static final int INTEGER = 1;
    public static final int DOUBLE = 2;
    public static final int FLOAT = 3;
    private final Number defaultValue;
    private Number value;
    private final Number minimum;
    private final Number maximum;
    private final ValueCategory parent;
    
    public ValueNumber(final String name, final String tag, final String description, final Number value, final Number minimum, final Number maximum) {
        super(name, tag, description);
        this.defaultValue = value;
        this.value = value;
        this.minimum = minimum;
        this.maximum = maximum;
        this.parent = null;
    }
    
    public ValueNumber(final String name, final String tag, final String description, final ValueCategory parent, final Number value, final Number minimum, final Number maximum) {
        super(name, tag, description);
        this.defaultValue = value;
        this.value = value;
        this.minimum = minimum;
        this.maximum = maximum;
        this.parent = parent;
    }
    
    public ValueCategory getParent() {
        return this.parent;
    }
    
    public Number getDefaultValue() {
        return this.defaultValue;
    }
    
    public Number getValue() {
        return this.value;
    }
    
    public void setValue(final Number value) {
        this.value = value;
        final EventClient event = new EventClient(this);
        MinecraftForge.EVENT_BUS.post((Event)event);
    }
    
    public Number getMaximum() {
        return this.maximum;
    }
    
    public Number getMinimum() {
        return this.minimum;
    }
    
    public int getType() {
        if (this.value.getClass() == Integer.class) {
            return 1;
        }
        if (this.value.getClass() == Double.class) {
            return 2;
        }
        if (this.value.getClass() == Float.class) {
            return 3;
        }
        return -1;
    }
}
