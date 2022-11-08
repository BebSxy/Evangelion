package dev.evangelion.client.mixins.impl;

import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import dev.evangelion.client.modules.visuals.chams.ModuleChams;
import dev.evangelion.client.modules.visuals.esp.ModuleESP;
import dev.evangelion.Evangelion;
import net.minecraft.entity.Entity;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderEnderCrystal;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({ RenderEnderCrystal.class })
public class MixinRenderEnderCrystal
{
    @Redirect(method = { "doRender*" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/model/ModelBase;render(Lnet/minecraft/entity/Entity;FFFFFF)V"))
    public void doRender(final ModelBase model, final Entity entity, final float limbSwing, final float limbSwingAmount, final float ageInTicks, final float netHeadYaw, final float headPitch, final float scale) {
        if (Evangelion.MODULE_MANAGER.isModuleEnabled("ESP") && ModuleESP.INSTANCE.crystals.getValue()) {
            ModuleESP.INSTANCE.entityESP.renderCrystals(model, entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
        }
        if (Evangelion.MODULE_MANAGER.isModuleEnabled("Chams") && ModuleChams.INSTANCE.crystals.getValue()) {
            ModuleChams.INSTANCE.crystalChams.renderModel(model, entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
        }
        else {
            model.render(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
        }
    }
}
