package dev.evangelion.client.mixins.impl;

import net.minecraft.inventory.EntityEquipmentSlot;
import org.spongepowered.asm.mixin.injection.Inject;
import dev.evangelion.client.modules.visuals.ModuleNoRender;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.entity.EntityLivingBase;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import net.minecraft.client.renderer.GlStateManager;
import dev.evangelion.client.modules.visuals.ModuleGlintModify;
import dev.evangelion.Evangelion;
import net.minecraft.client.renderer.entity.layers.LayerArmorBase;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(value = { LayerArmorBase.class }, priority = Integer.MAX_VALUE)
public class MixinLayerArmorBase
{
    @Redirect(method = { "renderEnchantedGlint" }, at = @At(value = "INVOKE", target = "net/minecraft/client/renderer/GlStateManager.color(FFFF)V", ordinal = 1))
    private static void renderEnchantedGlint(final float red, final float green, final float blue, final float alpha) {
        if (Evangelion.MODULE_MANAGER.isModuleEnabled("GlintModify")) {
            GlStateManager.color((float)ModuleGlintModify.INSTANCE.color.getValue().getRed(), (float)ModuleGlintModify.INSTANCE.color.getValue().getGreen(), (float)ModuleGlintModify.INSTANCE.color.getValue().getBlue(), (float)ModuleGlintModify.INSTANCE.color.getValue().getAlpha());
        }
        else {
            GlStateManager.color(red, green, blue, alpha);
        }
    }
    
    @Inject(method = { "doRenderLayer" }, at = { @At("HEAD") }, cancellable = true)
    public void doRenderLayer(final EntityLivingBase entitylivingbaseIn, final float limbSwing, final float limbSwingAmount, final float partialTicks, final float ageInTicks, final float netHeadYaw, final float headPitch, final float scale, final CallbackInfo info) {
        if (Evangelion.MODULE_MANAGER.isModuleEnabled("NoRender") && ModuleNoRender.INSTANCE.armor.getValue()) {
            info.cancel();
        }
    }
    
    @Inject(method = { "renderArmorLayer" }, at = { @At("HEAD") }, cancellable = true)
    public void renderArmorLayer(final EntityLivingBase entityLivingBaseIn, final float limbSwing, final float limbSwingAmount, final float partialTicks, final float ageInTicks, final float netHeadYaw, final float headPitch, final float scale, final EntityEquipmentSlot slotIn, final CallbackInfo info) {
        if (Evangelion.MODULE_MANAGER.isModuleEnabled("NoRender") && ModuleNoRender.INSTANCE.armor.getValue()) {
            info.cancel();
        }
    }
}
