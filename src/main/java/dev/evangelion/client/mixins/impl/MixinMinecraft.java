package dev.evangelion.client.mixins.impl;

import net.minecraft.client.multiplayer.PlayerControllerMP;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import dev.evangelion.Evangelion;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({ Minecraft.class })
public class MixinMinecraft
{
    @Redirect(method = { "sendClickBlockToController" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/entity/EntityPlayerSP;isHandActive()Z"))
    private boolean isHandActiveHook(final EntityPlayerSP playerSP) {
        return !Evangelion.MODULE_MANAGER.isModuleEnabled("MultiTask") && playerSP.isHandActive();
    }
    
    @Redirect(method = { "rightClickMouse" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/PlayerControllerMP;getIsHittingBlock()Z", ordinal = 0), require = 1)
    private boolean isHittingBlockHook(final PlayerControllerMP playerControllerMP) {
        return !Evangelion.MODULE_MANAGER.isModuleEnabled("MultiTask") && playerControllerMP.getIsHittingBlock();
    }
}
