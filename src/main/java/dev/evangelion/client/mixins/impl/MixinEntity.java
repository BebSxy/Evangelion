package dev.evangelion.client.mixins.impl;

import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.entity.MoverType;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.common.MinecraftForge;
import dev.evangelion.client.events.EventPush;
import dev.evangelion.client.events.EventStep;
import org.spongepowered.asm.mixin.Shadow;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({ Entity.class })
public class MixinEntity
{
    @Shadow
    public float stepHeight;
    private float cachedStepHeight;
    private EventStep stepEvent;
    
    @Redirect(method = { "applyEntityCollision" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;addVelocity(DDD)V"))
    public void addVelocityHook(final Entity entity, final double x, final double y, final double z) {
        final EventPush event = new EventPush(entity, x, y, z, true);
        MinecraftForge.EVENT_BUS.post((Event)event);
        if (!event.isCancelled()) {
            entity.motionX += event.x;
            entity.motionY += event.y;
            entity.motionZ += event.z;
            entity.isAirBorne = event.airbone;
        }
    }
    
    @Inject(method = { "move" }, at = { @At(value = "FIELD", target = "Lnet/minecraft/entity/Entity;stepHeight:F", shift = At.Shift.BEFORE, ordinal = 3) })
    private void onInjectStepPre(final MoverType type, final double x, final double y, final double z, final CallbackInfo ci) {
        this.cachedStepHeight = this.stepHeight;
        this.stepEvent = new EventStep(dev.evangelion.api.manager.event.Event.Stage.PRE, this.stepHeight);
        MinecraftForge.EVENT_BUS.post((Event)this.stepEvent);
    }
    
    @Inject(method = { "move" }, at = { @At(value = "FIELD", target = "Lnet/minecraft/entity/Entity;stepHeight:F", ordinal = 4, shift = At.Shift.BEFORE) }, require = 1)
    private void onInjectStepPre(final CallbackInfo ci) {
        this.stepHeight = this.stepEvent.getHeight();
    }
    
    @Inject(method = { "move" }, at = { @At(value = "INVOKE", target = "Lnet/minecraft/profiler/Profiler;endSection()V", shift = At.Shift.BEFORE, ordinal = 0) })
    private void onInjectStepEventPost(final CallbackInfo ci) {
        this.stepHeight = this.cachedStepHeight;
        MinecraftForge.EVENT_BUS.post((Event)new EventStep(dev.evangelion.api.manager.event.Event.Stage.POST, this.stepHeight));
    }
}
