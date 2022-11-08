package dev.evangelion.client.mixins.impl;

import dev.evangelion.client.events.EventPacketReceive;
import io.netty.channel.ChannelHandlerContext;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.common.MinecraftForge;
import dev.evangelion.client.events.EventPacketSend;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.network.Packet;
import net.minecraft.network.NetworkManager;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(value = { NetworkManager.class }, priority = Integer.MAX_VALUE)
public class MixinNetworkManager
{
    @Inject(method = { "sendPacket(Lnet/minecraft/network/Packet;)V" }, at = { @At("HEAD") }, cancellable = true)
    private void preSendPacket(final Packet<?> packet, final CallbackInfo info) {
        final EventPacketSend event = new EventPacketSend(packet);
        MinecraftForge.EVENT_BUS.post((Event)event);
        if (event.isCancelled()) {
            info.cancel();
        }
    }
    
    @Inject(method = { "channelRead0*" }, at = { @At("HEAD") }, cancellable = true)
    private void preReceivePacket(final ChannelHandlerContext context, final Packet<?> packet, final CallbackInfo info) {
        final EventPacketReceive event = new EventPacketReceive(packet);
        MinecraftForge.EVENT_BUS.post((Event)event);
        if (event.isCancelled()) {
            info.cancel();
        }
    }
}
