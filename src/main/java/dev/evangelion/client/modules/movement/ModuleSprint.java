package dev.evangelion.client.modules.movement;

import net.minecraft.entity.player.EntityPlayer;
import dev.evangelion.api.utilities.MovementUtils;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import dev.evangelion.api.manager.event.Event;
import dev.evangelion.client.events.EventPlayerMove;
import dev.evangelion.client.values.impl.ValueEnum;
import dev.evangelion.api.manager.module.RegisterModule;
import dev.evangelion.api.manager.module.Module;

@RegisterModule(name = "Sprint", tag = "Sprint", description = "Always be sprinting.", category = Module.Category.MOVEMENT)
public class ModuleSprint extends Module
{
    ValueEnum sprintMode;
    
    public ModuleSprint() {
        this.sprintMode = new ValueEnum("SprintMode", "Sprint Mode", "Modes for sprint.", sprintModes.Rage);
    }
    
    @SubscribeEvent
    public void onPlayerMove(final EventPlayerMove event) {
        if (event.getStage() == Event.Stage.PRE && this.sprintMode.getValue().equals(sprintModes.Rage) && (ModuleSprint.mc.player.movementInput.moveForward != 0.0f || ModuleSprint.mc.player.movementInput.moveStrafe != 0.0f)) {
            event.setCancelled(true);
        }
    }
    
    @Override
    public void onUpdate() {
        super.onUpdate();
        if (this.nullCheck()) {
            return;
        }
        if (this.sprintMode.getValue().equals(sprintModes.Legit)) {
            if (ModuleSprint.mc.player.moveForward > 0.0f && !ModuleSprint.mc.player.collidedHorizontally) {
                ModuleSprint.mc.player.setSprinting(true);
            }
        }
        else if (MovementUtils.isMoving((EntityPlayer)ModuleSprint.mc.player)) {
            ModuleSprint.mc.player.setSprinting(true);
        }
    }
    
    @Override
    public String getHudInfo() {
        return this.sprintMode.getValue().name();
    }
    
    public enum sprintModes
    {
        Legit, 
        Rage;
    }
}
