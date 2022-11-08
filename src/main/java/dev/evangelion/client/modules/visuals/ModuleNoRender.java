package dev.evangelion.client.modules.visuals;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.network.play.server.SPacketExplosion;
import dev.evangelion.client.events.EventPacketReceive;
import dev.evangelion.client.values.impl.ValueBoolean;
import dev.evangelion.api.manager.module.RegisterModule;
import dev.evangelion.api.manager.module.Module;

@RegisterModule(name = "NoRender", tag = "No Render", description = "Prevents certain things from rendering.", category = Module.Category.VISUALS)
public class ModuleNoRender extends Module
{
    public final ValueBoolean items;
    public final ValueBoolean fire;
    public final ValueBoolean hurtCamera;
    public final ValueBoolean totem;
    public final ValueBoolean pumpkin;
    public final ValueBoolean nausea;
    public final ValueBoolean armor;
    public final ValueBoolean maps;
    public final ValueBoolean portal;
    public final ValueBoolean suffocation;
    public final ValueBoolean explosions;
    public static ModuleNoRender INSTANCE;
    
    public ModuleNoRender() {
        this.items = new ValueBoolean("Items", "Items", "Disables the rendering of Items.", false);
        this.fire = new ValueBoolean("Fire", "Fire", "Disables the rendering of Fire on your screen.", true);
        this.hurtCamera = new ValueBoolean("HurtCamera", "Hurt Camera", "Disables the rendering of a hurt camera.", true);
        this.totem = new ValueBoolean("Totems", "Totems", "Disables the rendering of the Totem pop animation.", false);
        this.pumpkin = new ValueBoolean("Pumpkin", "Pumpkin", "Disables the rendering of the pumpkin overlay.", true);
        this.nausea = new ValueBoolean("Nausea", "Nausea", "Disables the rendering of the effects of Nausea.", true);
        this.armor = new ValueBoolean("Armor", "Armor", "Disables the rendering of Armor.", false);
        this.maps = new ValueBoolean("Maps", "Maps", "Disables the rendering of Map contents.", false);
        this.portal = new ValueBoolean("Portal", "Portal", "Disables the rendering of the purple portal effects.", false);
        this.suffocation = new ValueBoolean("Suffocation", "Suffocation", "Disables the rendering of when you are inside of a block.", true);
        this.explosions = new ValueBoolean("Explosions", "Explosions", "Prevents explosions from rendering.", true);
        ModuleNoRender.INSTANCE = this;
    }
    
    @SubscribeEvent
    public void onPacketReceive(final EventPacketReceive event) {
        if (ModuleNoRender.mc.player == null || ModuleNoRender.mc.world == null) {
            return;
        }
        if (this.explosions.getValue() && event.getPacket() instanceof SPacketExplosion) {
            event.setCancelled(true);
        }
    }
}
