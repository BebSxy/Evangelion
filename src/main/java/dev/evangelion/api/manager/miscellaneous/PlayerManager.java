package dev.evangelion.api.manager.miscellaneous;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.network.play.client.CPacketEntityAction;
import dev.evangelion.client.events.EventPacketSend;
import net.minecraftforge.common.MinecraftForge;
import dev.evangelion.api.utilities.IMinecraft;

public class PlayerManager implements IMinecraft
{
    private boolean switching;
    private boolean sneaking;
    private int slot;
    private int sentPackets;
    private int receivedPackets;
    
    public PlayerManager() {
        MinecraftForge.EVENT_BUS.register((Object)this);
    }
    
    @SubscribeEvent
    public void onPacketSend(final EventPacketSend event) {
        if (event.getPacket() instanceof CPacketEntityAction) {
            final CPacketEntityAction packet = (CPacketEntityAction)event.getPacket();
            if (packet.getAction() == CPacketEntityAction.Action.START_SNEAKING) {
                this.sneaking = true;
            }
            else if (packet.getAction() == CPacketEntityAction.Action.STOP_SNEAKING) {
                this.sneaking = false;
            }
        }
        if (event.getPacket() instanceof CPacketHeldItemChange) {
            final CPacketHeldItemChange packet2 = (CPacketHeldItemChange)event.getPacket();
            this.slot = packet2.getSlotId();
        }
    }
    
    public boolean isSwitching() {
        return this.switching;
    }
    
    public void setSwitching(final boolean switching) {
        this.switching = switching;
    }
    
    public boolean isSneaking() {
        return this.sneaking;
    }
    
    public int getSlot() {
        return this.slot;
    }
    
    public int getSentPackets() {
        return this.sentPackets;
    }
    
    public int getReceivedPackets() {
        return this.receivedPackets;
    }
}
