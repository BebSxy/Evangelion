package dev.evangelion.client.mixins.impl;

import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import dev.evangelion.client.modules.visuals.ModuleAnimations;
import dev.evangelion.Evangelion;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import net.minecraft.entity.EntityLivingBase;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(value = { EntityLivingBase.class }, priority = Integer.MAX_VALUE)
public class MixinEntityLivingBase
{
    @Inject(method = { "getArmSwingAnimationEnd" }, at = { @At("HEAD") }, cancellable = true)
    private void getArmSwingAnimationEnd(final CallbackInfoReturnable<Integer> info) {
        if (Evangelion.MODULE_MANAGER.isModuleEnabled("Animations") && ModuleAnimations.INSTANCE.changeSwing.getValue()) {
            info.setReturnValue(ModuleAnimations.INSTANCE.swingSpeed.getValue().intValue());
        }
    }
}
