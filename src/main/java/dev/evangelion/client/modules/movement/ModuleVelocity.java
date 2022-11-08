package dev.evangelion.client.modules.movement;

import com.mojang.realmsclient.gui.ChatFormatting;
import dev.evangelion.client.events.EventPush;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.network.play.server.SPacketExplosion;
import net.minecraft.network.play.server.SPacketEntityVelocity;
import dev.evangelion.client.events.EventPacketReceive;
import dev.evangelion.client.values.impl.ValueNumber;
import dev.evangelion.client.values.impl.ValueBoolean;
import dev.evangelion.api.manager.module.RegisterModule;
import dev.evangelion.api.manager.module.Module;

@RegisterModule(name = "Velocity", description = "Remove the knockback of the player.", category = Module.Category.MOVEMENT)
public class ModuleVelocity extends Module
{
    public static ValueBoolean noPush;
    public static ValueNumber horizontal;
    public static ValueNumber vertical;
    
    @SubscribeEvent
    public void onReceive(final EventPacketReceive event) {
        if (ModuleVelocity.mc.player == null || ModuleVelocity.mc.world == null) {
            return;
        }
        if (event.getPacket() instanceof SPacketEntityVelocity) {
            final SPacketEntityVelocity sPacketEntityVelocity = (SPacketEntityVelocity)event.getPacket();
            if (sPacketEntityVelocity.getEntityID() == ModuleVelocity.mc.player.entityId) {
                if (ModuleVelocity.horizontal.getValue().floatValue() == 0.0f && ModuleVelocity.vertical.getValue().floatValue() == 0.0f) {
                    event.setCancelled(true);
                }
                else {
                    final SPacketEntityVelocity sPacketEntityVelocity2 = sPacketEntityVelocity;
                    sPacketEntityVelocity2.motionX *= (int)ModuleVelocity.horizontal.getValue().floatValue();
                    final SPacketEntityVelocity sPacketEntityVelocity3 = sPacketEntityVelocity;
                    sPacketEntityVelocity3.motionY *= (int)ModuleVelocity.vertical.getValue().floatValue();
                    final SPacketEntityVelocity sPacketEntityVelocity4 = sPacketEntityVelocity;
                    sPacketEntityVelocity4.motionZ *= (int)ModuleVelocity.horizontal.getValue().floatValue();
                }
            }
        }
        if (event.getPacket() instanceof SPacketExplosion) {
            final SPacketExplosion sPacketExplosion = (SPacketExplosion)event.getPacket();
            if (ModuleVelocity.horizontal.getValue().floatValue() == 0.0f && ModuleVelocity.vertical.getValue().floatValue() == 0.0f) {
                event.setCancelled(true);
            }
            else {
                final SPacketExplosion sPacketExplosion2 = sPacketExplosion;
                sPacketExplosion2.motionX *= ModuleVelocity.horizontal.getValue().floatValue();
                final SPacketExplosion sPacketExplosion3 = sPacketExplosion;
                sPacketExplosion3.motionY *= ModuleVelocity.vertical.getValue().floatValue();
                final SPacketExplosion sPacketExplosion4 = sPacketExplosion;
                sPacketExplosion4.motionZ *= ModuleVelocity.horizontal.getValue().floatValue();
            }
        }
    }
    
    @SubscribeEvent
    public void onPush(final EventPush event) {
        if (ModuleVelocity.noPush.getValue()) {
            event.setCancelled(true);
        }
    }
    
    @Override
    public String getHudInfo() {
        return "H" + ModuleVelocity.horizontal.getValue().floatValue() + "%" + ChatFormatting.GRAY + "," + ChatFormatting.WHITE + "V" + ModuleVelocity.vertical.getValue().floatValue() + "%";
    }
    
    static {
        ModuleVelocity.noPush = new ValueBoolean("NoPush", "NoPush", "", false);
        ModuleVelocity.horizontal = new ValueNumber("Horizontal", "Horizontal", "", 0.0f, 0.0f, 100.0f);
        ModuleVelocity.vertical = new ValueNumber("Vertical", "Vertical", "", 0.0f, 0.0f, 100.0f);
    }
}
