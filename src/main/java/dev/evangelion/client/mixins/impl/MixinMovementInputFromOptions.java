package dev.evangelion.client.mixins.impl;

import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.lwjgl.input.Keyboard;
import net.minecraft.client.gui.GuiChat;
import dev.evangelion.client.modules.movement.ModuleNoSlow;
import dev.evangelion.Evangelion;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.MovementInputFromOptions;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({ MovementInputFromOptions.class })
public class MixinMovementInputFromOptions
{
    @Redirect(method = { "updatePlayerMoveState" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/settings/KeyBinding;isKeyDown()Z"))
    public boolean isKeyPressed(final KeyBinding keyBinding) {
        if (Evangelion.MODULE_MANAGER.isModuleEnabled("NoSlow") && ModuleNoSlow.INSTANCE.guiMove.getValue()) {
            final ModuleNoSlow instance = ModuleNoSlow.INSTANCE;
            if (ModuleNoSlow.mc.currentScreen != null) {
                final ModuleNoSlow instance2 = ModuleNoSlow.INSTANCE;
                if (!(ModuleNoSlow.mc.currentScreen instanceof GuiChat)) {
                    final ModuleNoSlow instance3 = ModuleNoSlow.INSTANCE;
                    if (ModuleNoSlow.mc.player != null) {
                        return Keyboard.isKeyDown(keyBinding.getKeyCode());
                    }
                }
            }
        }
        return keyBinding.isKeyDown();
    }
}
