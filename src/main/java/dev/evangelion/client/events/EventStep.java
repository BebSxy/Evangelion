package dev.evangelion.client.events;

import dev.evangelion.api.manager.event.Event;

public class EventStep extends Event
{
    private float height;
    
    public EventStep(final Stage stage, final float height) {
        super(stage);
        this.height = height;
    }
    
    public float getHeight() {
        return this.height;
    }
    
    public void setHeight(final float height) {
        this.height = height;
    }
}
