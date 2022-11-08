package dev.evangelion.client.values.impl;

import dev.evangelion.client.values.Value;

public class ValueBind extends Value
{
    private final int defaultValue;
    private int value;
    private final ValueCategory parent;
    
    public ValueBind(final String name, final String tag, final String description, final int value) {
        super(name, tag, description);
        this.defaultValue = value;
        this.value = value;
        this.parent = null;
    }
    
    public ValueBind(final String name, final String tag, final String description, final ValueCategory parent, final int value) {
        super(name, tag, description);
        this.defaultValue = value;
        this.value = value;
        this.parent = parent;
    }
    
    public ValueCategory getParent() {
        return this.parent;
    }
    
    public int getDefaultValue() {
        return this.defaultValue;
    }
    
    public int getValue() {
        return this.value;
    }
    
    public void setValue(final int value) {
        this.value = value;
    }
}
