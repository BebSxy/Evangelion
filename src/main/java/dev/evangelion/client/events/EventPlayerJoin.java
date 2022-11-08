package dev.evangelion.client.events;

import java.util.UUID;
import dev.evangelion.api.manager.event.Event;

public class EventPlayerJoin extends Event
{
    private final String name;
    private final UUID id;
    
    public EventPlayerJoin(final String name, final UUID id) {
        this.name = name;
        this.id = id;
    }
    
    public String getName() {
        return this.name;
    }
    
    public UUID getID() {
        return this.id;
    }
}
