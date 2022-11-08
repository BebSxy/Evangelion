package dev.evangelion.client.mixins.impl;

import org.spongepowered.asm.mixin.Overwrite;
import dev.evangelion.client.modules.combat.ModuleReach;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import net.minecraftforge.common.MinecraftForge;
import dev.evangelion.client.events.EventClickBlock;
import dev.evangelion.api.manager.event.Event;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(value = { PlayerControllerMP.class }, priority = Integer.MAX_VALUE)
public class MixinPlayerControllerMP
{
    @Inject(method = { "onPlayerDamageBlock" }, at = { @At("HEAD") }, cancellable = true)
    public void onPlayerDamageBlock(final BlockPos position, final EnumFacing side, final CallbackInfoReturnable<Boolean> info) {
        final EventClickBlock event = new EventClickBlock(Event.Stage.PRE, position, side);
        MinecraftForge.EVENT_BUS.post((net.minecraftforge.fml.common.eventhandler.Event)event);
        if (event.isCancelled()) {
            info.cancel();
        }
    }
    
    @Inject(method = { "clickBlock" }, at = { @At("HEAD") }, cancellable = true)
    public void clickBlock(final BlockPos position, final EnumFacing side, final CallbackInfoReturnable<Boolean> info) {
        final EventClickBlock event = new EventClickBlock(Event.Stage.POST, position, side);
        MinecraftForge.EVENT_BUS.post((net.minecraftforge.fml.common.eventhandler.Event)event);
        if (event.isCancelled()) {
            info.cancel();
        }
    }
    
    @Overwrite
    public float getBlockReachDistance() {
        final float attrib = (float)Minecraft.getMinecraft().player.getEntityAttribute(EntityPlayer.REACH_DISTANCE).getAttributeValue();
        if (ModuleReach.INSTANCE.isToggled()) {
            return attrib + ModuleReach.INSTANCE.reachAdd.getValue().floatValue();
        }
        return Minecraft.getMinecraft().player.isCreative() ? attrib : (attrib - 0.5f);
    }
}
