package dev.evangelion.client.events;

import dev.evangelion.api.manager.event.Event;

public class EventRender3D extends Event
{
    private final float partialTicks;
    
    public EventRender3D(final float partialTicks) {
        this.partialTicks = partialTicks;
    }
    
    public float getPartialTicks() {
        return this.partialTicks;
    }
}
