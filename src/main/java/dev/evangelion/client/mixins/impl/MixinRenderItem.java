package dev.evangelion.client.mixins.impl;

import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import dev.evangelion.client.modules.visuals.ModuleGlintModify;
import dev.evangelion.Evangelion;
import net.minecraft.client.renderer.RenderItem;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(value = { RenderItem.class }, priority = Integer.MAX_VALUE)
public class MixinRenderItem
{
    @ModifyArg(method = { "renderEffect" }, at = @At(value = "INVOKE", target = "net/minecraft/client/renderer/RenderItem.renderModel(Lnet/minecraft/client/renderer/block/model/IBakedModel;I)V"), index = 1)
    private int renderEffect(final int oldValue) {
        return Evangelion.MODULE_MANAGER.isModuleEnabled("GlintModify") ? ModuleGlintModify.INSTANCE.color.getValue().getRGB() : oldValue;
    }
}
