package dev.evangelion.client.events;

import net.minecraft.network.Packet;
import dev.evangelion.api.manager.event.Event;

public class EventPacketSend extends Event
{
    private final Packet<?> packet;
    
    public EventPacketSend(final Packet<?> packet) {
        this.packet = packet;
    }
    
    public Packet<?> getPacket() {
        return this.packet;
    }
}
