package dev.evangelion.client.mixins.impl;

import dev.evangelion.client.events.EventPush;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketAnimation;
import net.minecraft.client.Minecraft;
import net.minecraft.util.EnumHand;
import dev.evangelion.client.modules.player.ModuleSwing;
import dev.evangelion.Evangelion;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.common.MinecraftForge;
import dev.evangelion.client.events.EventPlayerMove;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.entity.MoverType;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.world.World;
import dev.evangelion.client.events.EventMotion;
import net.minecraft.client.entity.EntityPlayerSP;
import org.spongepowered.asm.mixin.Mixin;
import net.minecraft.client.entity.AbstractClientPlayer;

@Mixin(value = { EntityPlayerSP.class }, priority = Integer.MAX_VALUE)
public abstract class MixinEntityPlayerSP extends AbstractClientPlayer
{
    private EventMotion eventMotion;
    
    public MixinEntityPlayerSP(final World world, final NetHandlerPlayClient client) {
        super(world, client.getGameProfile());
        this.eventMotion = new EventMotion();
    }
    
    @Inject(method = { "move" }, at = { @At("HEAD") }, cancellable = true)
    private void move(final MoverType type, final double x, final double y, final double z, final CallbackInfo info) {
        final EventPlayerMove event = new EventPlayerMove(type, x, y, z);
        MinecraftForge.EVENT_BUS.post((Event)event);
        if (event.isCancelled()) {
            super.move(type, event.getX(), event.getY(), event.getZ());
            info.cancel();
        }
    }
    
    @Inject(method = { "onUpdateWalkingPlayer" }, at = { @At("HEAD") }, cancellable = true)
    private void onUpdateWalkingPlayer(final CallbackInfo info) {
        if (this.eventMotion.isCanceled()) {
            info.cancel();
        }
    }
    
    @Inject(method = { "onUpdate" }, at = { @At(value = "INVOKE", target = "Lnet/minecraft/client/entity/EntityPlayerSP;onUpdateWalkingPlayer()V", shift = At.Shift.BEFORE) })
    private void onUpdateWalkingPlayerPre(final CallbackInfo info) {
        this.eventMotion = new EventMotion(dev.evangelion.api.manager.event.Event.Stage.PRE, this.rotationYaw, this.rotationPitch);
        MinecraftForge.EVENT_BUS.post((Event)this.eventMotion);
        this.rotationYaw = this.eventMotion.getRotationYaw();
        this.rotationPitch = this.eventMotion.getRotationPitch();
    }
    
    @Inject(method = { "onUpdate" }, at = { @At(value = "INVOKE", target = "Lnet/minecraft/client/entity/EntityPlayerSP;onUpdateWalkingPlayer()V", shift = At.Shift.AFTER) })
    private void onUpdateWalkingPlayerPost(final CallbackInfo ci) {
        if (this.rotationYaw == this.eventMotion.getRotationYaw()) {
            this.rotationYaw = this.eventMotion.getOldRotationYaw();
        }
        if (this.rotationPitch == this.eventMotion.getRotationPitch()) {
            this.rotationPitch = this.eventMotion.getOldRotationPitch();
        }
    }
    
    @Inject(method = { "swingArm" }, at = { @At("HEAD") }, cancellable = true)
    public void swingArm(final CallbackInfo info) {
        if (Evangelion.MODULE_MANAGER.isModuleEnabled("Swing")) {
            info.cancel();
            if (!ModuleSwing.INSTANCE.hand.getValue().equals(ModuleSwing.Hands.None)) {
                if (ModuleSwing.INSTANCE.hand.getValue().equals(ModuleSwing.Hands.Offhand)) {
                    if (!ModuleSwing.INSTANCE.silent.getValue()) {
                        super.swingArm(EnumHand.OFF_HAND);
                    }
                    Minecraft.getMinecraft().player.connection.sendPacket((Packet)new CPacketAnimation(EnumHand.OFF_HAND));
                }
                else {
                    if (!ModuleSwing.INSTANCE.silent.getValue()) {
                        super.swingArm(EnumHand.MAIN_HAND);
                    }
                    Minecraft.getMinecraft().player.connection.sendPacket((Packet)new CPacketAnimation(EnumHand.MAIN_HAND));
                }
            }
        }
    }
    
    @Inject(method = { "pushOutOfBlocks" }, at = { @At("HEAD") }, cancellable = true)
    private void pushOutOfBlocksHook(final double x, final double y, final double z, final CallbackInfoReturnable<Boolean> info) {
        final EventPush event = new EventPush();
        MinecraftForge.EVENT_BUS.post((Event)event);
        if (event.isCancelled()) {
            info.setReturnValue(false);
        }
    }
}
