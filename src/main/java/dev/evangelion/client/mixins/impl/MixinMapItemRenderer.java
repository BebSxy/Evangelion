package dev.evangelion.client.mixins.impl;

import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import dev.evangelion.client.modules.visuals.ModuleNoRender;
import dev.evangelion.Evangelion;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.world.storage.MapData;
import net.minecraft.client.gui.MapItemRenderer;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(value = { MapItemRenderer.class }, priority = Integer.MAX_VALUE)
public class MixinMapItemRenderer
{
    @Inject(method = { "renderMap" }, at = { @At("HEAD") }, cancellable = true)
    public void renderMap(final MapData mapData, final boolean noOverlayRendering, final CallbackInfo info) {
        if (Evangelion.MODULE_MANAGER.isModuleEnabled("NoRender") && ModuleNoRender.INSTANCE.maps.getValue()) {
            info.cancel();
        }
    }
}
