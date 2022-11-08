package dev.evangelion.client.modules.combat;

import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.world.World;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.network.play.client.CPacketUseEntity;
import dev.evangelion.client.events.EventPacketSend;
import dev.evangelion.client.values.impl.ValueEnum;
import dev.evangelion.api.manager.module.RegisterModule;
import dev.evangelion.api.manager.module.Module;

@RegisterModule(name = "Criticals", description = "Makes every one of your hits a critical.", category = Module.Category.COMBAT)
public class ModuleCriticals extends Module
{
    public static ValueEnum mode;
    
    @SubscribeEvent
    public void onPacketSend(final EventPacketSend event) {
        if (event.getPacket() instanceof CPacketUseEntity) {
            final CPacketUseEntity packet = (CPacketUseEntity)event.getPacket();
            if (packet.getAction() == CPacketUseEntity.Action.ATTACK && !(packet.getEntityFromWorld((World)ModuleCriticals.mc.world) instanceof EntityEnderCrystal)) {
                doCritical(ModuleCriticals.mode.getValue().toString());
            }
        }
    }
    
    public static void doCritical(final String mode) {
        if (!mode.equals("None")) {
            if (!ModuleCriticals.mc.player.onGround) {
                return;
            }
            if (ModuleCriticals.mc.player.isInWater() || ModuleCriticals.mc.player.isInLava()) {
                return;
            }
            if (mode.equals("Packet")) {
                ModuleCriticals.mc.player.connection.sendPacket((Packet)new CPacketPlayer.Position(ModuleCriticals.mc.player.posX, ModuleCriticals.mc.player.posY + 0.0625, ModuleCriticals.mc.player.posZ, true));
                ModuleCriticals.mc.player.connection.sendPacket((Packet)new CPacketPlayer.Position(ModuleCriticals.mc.player.posX, ModuleCriticals.mc.player.posY, ModuleCriticals.mc.player.posZ, false));
                ModuleCriticals.mc.player.connection.sendPacket((Packet)new CPacketPlayer.Position(ModuleCriticals.mc.player.posX, ModuleCriticals.mc.player.posY + 1.1E-5, ModuleCriticals.mc.player.posZ, false));
                ModuleCriticals.mc.player.connection.sendPacket((Packet)new CPacketPlayer.Position(ModuleCriticals.mc.player.posX, ModuleCriticals.mc.player.posY, ModuleCriticals.mc.player.posZ, false));
            }
            else if (mode.equals("Bypass")) {
                ModuleCriticals.mc.player.motionY = 0.10000000149011612;
                ModuleCriticals.mc.player.fallDistance = 0.1f;
                ModuleCriticals.mc.player.onGround = false;
            }
            else {
                ModuleCriticals.mc.player.jump();
                if (mode.equals("MiniJump")) {
                    final EntityPlayerSP player = ModuleCriticals.mc.player;
                    player.motionY /= 2.0;
                }
            }
        }
    }
    
    static {
        ModuleCriticals.mode = new ValueEnum("Mode", "Mode", "The mode for the Criticals.", Modes.Packet);
    }
    
    public enum Modes
    {
        Packet, 
        Jump, 
        MiniJump, 
        FakeJump;
    }
}
