package dev.evangelion.client.events;

import net.minecraft.entity.player.EntityPlayer;
import java.util.UUID;
import dev.evangelion.api.manager.event.Event;

public class EventPlayerLeave extends Event
{
    private final String name;
    private final UUID id;
    private final EntityPlayer entity;
    
    public EventPlayerLeave(final String name, final UUID id, final EntityPlayer entity) {
        this.name = name;
        this.id = id;
        this.entity = entity;
    }
    
    public String getName() {
        return this.name;
    }
    
    public UUID getID() {
        return this.id;
    }
    
    public EntityPlayer getEntity() {
        return this.entity;
    }
}
