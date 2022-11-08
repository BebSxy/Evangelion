package dev.evangelion.client.modules.player;

import dev.evangelion.client.values.impl.ValueBoolean;
import dev.evangelion.client.values.impl.ValueEnum;
import dev.evangelion.api.manager.module.RegisterModule;
import dev.evangelion.api.manager.module.Module;

@RegisterModule(name = "Swing", description = "Changes the hand you swing with.", category = Module.Category.PLAYER)
public class ModuleSwing extends Module
{
    public final ValueEnum hand;
    public final ValueBoolean silent;
    public static ModuleSwing INSTANCE;
    
    public ModuleSwing() {
        this.hand = new ValueEnum("Hand", "Hand", "The hand to swing with.", Hands.Mainhand);
        this.silent = new ValueBoolean("Silent", "Silent", "Makes your swinging only serverside and not clientside.", false);
        ModuleSwing.INSTANCE = this;
    }
    
    public enum Hands
    {
        None, 
        Mainhand, 
        Offhand;
    }
}
