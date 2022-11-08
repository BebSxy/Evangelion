package dev.evangelion.client.events;

import net.minecraft.network.Packet;
import dev.evangelion.api.manager.event.Event;

public class EventPacketReceive extends Event
{
    private final Packet<?> packet;
    
    public EventPacketReceive(final Packet<?> packet) {
        this.packet = packet;
    }
    
    public Packet<?> getPacket() {
        return this.packet;
    }
}
