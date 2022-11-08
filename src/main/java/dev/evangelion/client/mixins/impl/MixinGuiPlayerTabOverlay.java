package dev.evangelion.client.mixins.impl;

import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import dev.evangelion.client.modules.client.ModuleStreamerMode;
import dev.evangelion.Evangelion;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.client.gui.GuiPlayerTabOverlay;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({ GuiPlayerTabOverlay.class })
public class MixinGuiPlayerTabOverlay
{
    @Inject(method = { "getPlayerName" }, at = { @At("HEAD") }, cancellable = true)
    public void getPlayerName(final NetworkPlayerInfo networkPlayerInfoIn, final CallbackInfoReturnable returnable) {
        if (Evangelion.MODULE_MANAGER.isModuleEnabled("StreamerMode") && ModuleStreamerMode.hideYou.getValue()) {
            returnable.cancel();
            returnable.setReturnValue(ModuleStreamerMode.getPlayerName(networkPlayerInfoIn));
        }
    }
}
