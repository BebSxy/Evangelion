package dev.evangelion.client.events;

import dev.evangelion.client.values.Value;
import dev.evangelion.api.manager.event.Event;

public class EventClient extends Event
{
    private final Value value;
    
    public EventClient(final Value value) {
        super(Stage.POST);
        this.value = value;
    }
    
    public Value getValue() {
        return this.value;
    }
}
