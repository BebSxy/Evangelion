package dev.evangelion.client.values;

public class Value
{
    private final String name;
    private final String tag;
    private final String description;
    
    public Value(final String name, final String tag, final String description) {
        this.name = name;
        this.tag = tag;
        this.description = description;
    }
    
    public String getName() {
        return this.name;
    }
    
    public String getTag() {
        return this.tag;
    }
    
    public String getDescription() {
        return this.description;
    }
}
