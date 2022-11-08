package dev.evangelion.client.mixins.impl;

import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import dev.evangelion.Evangelion;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.RenderPlayer;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(value = { RenderPlayer.class }, priority = Integer.MAX_VALUE)
public class MixinRenderPlayer
{
    @Inject(method = { "renderEntityName*" }, at = { @At("HEAD") }, cancellable = true)
    public void renderEntityName(final AbstractClientPlayer entityIn, final double x, final double y, final double z, final String name, final double distanceSq, final CallbackInfo info) {
        if (Evangelion.MODULE_MANAGER.isModuleEnabled("NameTags")) {
            info.cancel();
        }
    }
}
