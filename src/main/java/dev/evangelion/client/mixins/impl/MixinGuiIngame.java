package dev.evangelion.client.mixins.impl;

import dev.evangelion.client.modules.visuals.ModuleNoRender;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import dev.evangelion.client.modules.client.ModuleHUD;
import dev.evangelion.Evangelion;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.GuiIngame;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(value = { GuiIngame.class }, priority = Integer.MAX_VALUE)
public class MixinGuiIngame
{
    @Inject(method = { "renderPotionEffects" }, at = { @At("HEAD") }, cancellable = true)
    protected void renderPotionEffects(final ScaledResolution p_renderPotionEffects_1_, final CallbackInfo info) {
        if (Evangelion.MODULE_MANAGER.isModuleEnabled("HUD") && ModuleHUD.INSTANCE.effectHud.getValue().equals(ModuleHUD.effectHuds.Hide)) {
            info.cancel();
        }
    }
    
    @Inject(method = { "renderPortal" }, at = { @At("HEAD") }, cancellable = true)
    public void renderPortal(final float n, final ScaledResolution scaledResolution, final CallbackInfo info) {
        if (Evangelion.MODULE_MANAGER.isModuleEnabled("NoRender") && ModuleNoRender.INSTANCE.portal.getValue()) {
            info.cancel();
        }
    }
    
    @Inject(method = { "renderPumpkinOverlay" }, at = { @At("HEAD") }, cancellable = true)
    public void renderPumpkinOverlay(final ScaledResolution scaledResolution, final CallbackInfo info) {
        if (Evangelion.MODULE_MANAGER.isModuleEnabled("NoRender") && ModuleNoRender.INSTANCE.pumpkin.getValue()) {
            info.cancel();
        }
    }
}
