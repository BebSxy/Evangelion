package dev.evangelion.client.values.impl;

import java.util.ArrayList;
import java.util.Iterator;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.common.MinecraftForge;
import dev.evangelion.client.events.EventClient;
import dev.evangelion.client.values.Value;

public class ValueEnum extends Value
{
    private final Enum defaultValue;
    private Enum value;
    private final ValueCategory parent;
    
    public ValueEnum(final String name, final String tag, final String description, final Enum value) {
        super(name, tag, description);
        this.defaultValue = value;
        this.value = value;
        this.parent = null;
    }
    
    public ValueEnum(final String name, final String tag, final String description, final ValueCategory parent, final Enum value) {
        super(name, tag, description);
        this.defaultValue = value;
        this.value = value;
        this.parent = parent;
    }
    
    public ValueCategory getParent() {
        return this.parent;
    }
    
    public Enum getDefaultValue() {
        return this.defaultValue;
    }
    
    public Enum getValue() {
        return this.value;
    }
    
    public void setValue(final Enum value) {
        this.value = value;
        final EventClient event = new EventClient(this);
        MinecraftForge.EVENT_BUS.post((Event)event);
    }
    
    public Enum getEnum(final String name) {
        for (final Enum value : this.getEnums()) {
            if (value.name().equals(name)) {
                return value;
            }
        }
        return null;
    }
    
    public ArrayList<Enum> getEnums() {
        final ArrayList<Enum> enums = new ArrayList<Enum>();
        for (final Enum value : (Enum[])this.value.getClass().getEnumConstants()) {
            enums.add(value);
        }
        return enums;
    }
}
