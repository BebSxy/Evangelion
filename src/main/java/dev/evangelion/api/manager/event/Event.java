package dev.evangelion.api.manager.event;

public class Event extends net.minecraftforge.fml.common.eventhandler.Event
{
    private boolean cancelled;
    private final Stage stage;
    
    public Event() {
        this.stage = Stage.PRE;
    }
    
    public Event(final Stage stage) {
        this.stage = stage;
    }
    
    public Stage getStage() {
        return this.stage;
    }
    
    public boolean isCancelled() {
        return this.cancelled;
    }
    
    public void setCancelled(final boolean cancelled) {
        this.cancelled = cancelled;
    }
    
    public boolean isPre() {
        return this.stage == Stage.PRE;
    }
    
    public boolean isPost() {
        return this.stage == Stage.POST;
    }
    
    public enum Stage
    {
        PRE, 
        POST;
    }
}
