package dev.evangelion.client.events;

import net.minecraft.entity.player.EntityPlayer;
import dev.evangelion.api.manager.event.Event;

public class EventDeath extends Event
{
    private final EntityPlayer entity;
    
    public EventDeath(final EntityPlayer entity) {
        this.entity = entity;
    }
    
    public EntityPlayer getEntity() {
        return this.entity;
    }
}
