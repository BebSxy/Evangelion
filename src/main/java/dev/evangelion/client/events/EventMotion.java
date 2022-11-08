package dev.evangelion.client.events;

import dev.evangelion.api.manager.event.Event;

public class EventMotion extends Event
{
    private float rotationYaw;
    private float rotationPitch;
    private float oldRotationYaw;
    private float oldRotationPitch;
    
    public EventMotion() {
        this(Stage.PRE, 0.0f, 0.0f);
    }
    
    public EventMotion(final Stage stage, final float rotationYaw, final float rotationPitch) {
        super(stage);
        this.rotationYaw = rotationYaw;
        this.rotationPitch = rotationPitch;
        this.oldRotationYaw = rotationYaw;
        this.oldRotationPitch = rotationPitch;
    }
    
    public final float getRotationYaw() {
        return this.rotationYaw;
    }
    
    public final float getRotationPitch() {
        return this.rotationPitch;
    }
    
    public final void setRotationYaw(final float i) {
        this.rotationYaw = i;
    }
    
    public final void setRotationPitch(final float i) {
        this.rotationPitch = i;
    }
    
    public final float getOldRotationYaw() {
        return this.oldRotationYaw;
    }
    
    public final float getOldRotationPitch() {
        return this.oldRotationPitch;
    }
}
