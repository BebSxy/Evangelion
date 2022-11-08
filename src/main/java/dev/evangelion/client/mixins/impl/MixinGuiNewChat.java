package dev.evangelion.client.mixins.impl;

import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import net.minecraft.client.gui.Gui;
import dev.evangelion.client.modules.miscellaneous.ModuleChat;
import dev.evangelion.Evangelion;
import net.minecraft.client.gui.GuiNewChat;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({ GuiNewChat.class })
public abstract class MixinGuiNewChat
{
    @Redirect(method = { "drawChat" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiNewChat;drawRect(IIIII)V"))
    public void drawBackground(final int left, final int top, final int right, final int bottom, final int colour) {
        if (Evangelion.MODULE_MANAGER.isModuleEnabled("Chat") && ModuleChat.INSTANCE.noBackground.getValue()) {
            Gui.drawRect(left, top, right, bottom, 0);
        }
        else {
            Gui.drawRect(left, top, right, bottom, colour);
        }
    }
}
