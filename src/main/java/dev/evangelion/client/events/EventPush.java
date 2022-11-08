package dev.evangelion.client.events;

import net.minecraft.entity.Entity;
import dev.evangelion.api.manager.event.Event;

public class EventPush extends Event
{
    public Entity entity;
    public double x;
    public double y;
    public double z;
    public boolean airbone;
    
    public EventPush(final Entity entity, final double x, final double y, final double z, final boolean airbone) {
        this.entity = entity;
        this.x = x;
        this.y = y;
        this.z = z;
        this.airbone = airbone;
    }
    
    public EventPush() {
    }
    
    public EventPush(final Entity entity) {
        this.entity = entity;
    }
}
