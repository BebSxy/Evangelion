package dev.evangelion.client.mixins.impl;

import dev.evangelion.client.modules.combat.ModuleReach;
import dev.evangelion.client.modules.miscellaneous.ambience.Weather;
import dev.evangelion.client.modules.miscellaneous.ambience.ModuleAmbience;
import org.spongepowered.asm.mixin.injection.Redirect;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.init.Items;
import org.spongepowered.asm.mixin.injection.Inject;
import dev.evangelion.client.modules.visuals.ModuleNoRender;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import dev.evangelion.client.modules.visuals.ModuleViewClip;
import dev.evangelion.Evangelion;
import org.spongepowered.asm.mixin.Shadow;
import net.minecraft.item.ItemStack;
import net.minecraft.client.renderer.EntityRenderer;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(value = { EntityRenderer.class }, priority = Integer.MAX_VALUE)
public class MixinEntityRenderer
{
    @Shadow
    private ItemStack itemActivationItem;
    
    @ModifyVariable(method = { "orientCamera" }, ordinal = 3, at = @At(value = "STORE", ordinal = 0), require = 1)
    public double orientCameraDistance(final double range) {
        return (Evangelion.MODULE_MANAGER.isModuleEnabled("ViewClip") && ModuleViewClip.INSTANCE.extend.getValue()) ? ModuleViewClip.INSTANCE.distance.getValue().doubleValue() : range;
    }
    
    @ModifyVariable(method = { "orientCamera" }, ordinal = 7, at = @At(value = "STORE", ordinal = 0), require = 1)
    public double orientCamera(final double range) {
        return (Evangelion.MODULE_MANAGER.isModuleEnabled("ViewClip") && ModuleViewClip.INSTANCE.extend.getValue()) ? ModuleViewClip.INSTANCE.distance.getValue().doubleValue() : ((Evangelion.MODULE_MANAGER.isModuleEnabled("ViewClip") && !ModuleViewClip.INSTANCE.extend.getValue()) ? 4.0 : range);
    }
    
    @Inject(method = { "hurtCameraEffect" }, at = { @At("HEAD") }, cancellable = true)
    public void hurtCameraEffect(final float ticks, final CallbackInfo info) {
        if (Evangelion.MODULE_MANAGER.isModuleEnabled("NoRender") && ModuleNoRender.INSTANCE.hurtCamera.getValue()) {
            info.cancel();
        }
    }
    
    @Inject(method = { "renderItemActivation" }, at = { @At("HEAD") }, cancellable = true)
    public void renderItemActivation(final CallbackInfo info) {
        if (this.itemActivationItem != null && Evangelion.MODULE_MANAGER.isModuleEnabled("NoRender") && ModuleNoRender.INSTANCE.totem.getValue() && this.itemActivationItem.getItem() == Items.TOTEM_OF_UNDYING) {
            info.cancel();
        }
    }
    
    @Redirect(method = { "setupCameraTransform" }, at = @At(value = "FIELD", target = "Lnet/minecraft/client/entity/EntityPlayerSP;prevTimeInPortal:F"))
    public float setupCameraTransform(final EntityPlayerSP player) {
        if (Evangelion.MODULE_MANAGER.isModuleEnabled("NoRender") && ModuleNoRender.INSTANCE.nausea.getValue()) {
            return -3.4028235E38f;
        }
        return player.prevTimeInPortal;
    }
    
    @Inject(method = { "renderWorldPass" }, at = { @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/EntityRenderer;renderRainSnow(F)V") })
    public void weatherHook(final int pass, final float partialTicks, final long finishTimeNano, final CallbackInfo ci) {
        if (Evangelion.MODULE_MANAGER.isModuleEnabled("Ambience") && !ModuleAmbience.INSTANCE.weatherMode.getValue().equals(ModuleAmbience.WeatherModes.None)) {
            Weather.render(partialTicks);
        }
    }
    
    @Inject(method = { "getMouseOver" }, at = { @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;getPositionEyes(F)Lnet/minecraft/util/math/Vec3d;", shift = At.Shift.BEFORE) }, cancellable = true)
    public void onGetEntitiesInAABBExcluding(final float partialTicks, final CallbackInfo ci) {
        if (ModuleReach.INSTANCE.shouldIgnoreHitBox()) {
            ci.cancel();
            final ModuleReach instance = ModuleReach.INSTANCE;
            ModuleReach.mc.profiler.endSection();
        }
    }
}
