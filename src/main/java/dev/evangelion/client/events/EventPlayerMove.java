package dev.evangelion.client.events;

import net.minecraft.entity.MoverType;
import dev.evangelion.api.manager.event.Event;

public class EventPlayerMove extends Event
{
    MoverType type;
    double x;
    double y;
    double z;
    
    public EventPlayerMove(final MoverType type, final double x, final double y, final double z) {
        this.type = type;
        this.x = x;
        this.y = y;
        this.z = z;
    }
    
    public void setType(final MoverType type) {
        this.type = type;
    }
    
    public void setX(final double x) {
        this.x = x;
    }
    
    public void setY(final double y) {
        this.y = y;
    }
    
    public void setZ(final double z) {
        this.z = z;
    }
    
    public MoverType getType() {
        return this.type;
    }
    
    public double getX() {
        return this.x;
    }
    
    public double getY() {
        return this.y;
    }
    
    public double getZ() {
        return this.z;
    }
}
