package dev.evangelion.client.values.impl;

import dev.evangelion.client.values.Value;

public class ValueCategory extends Value
{
    private boolean open;
    
    public ValueCategory(final String name, final String description) {
        super(name, name, description);
        this.open = false;
    }
    
    public boolean isOpen() {
        return this.open;
    }
    
    public void setOpen(final boolean open) {
        this.open = open;
    }
}
