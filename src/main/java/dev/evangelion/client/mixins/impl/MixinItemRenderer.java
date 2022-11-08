package dev.evangelion.client.mixins.impl;

import dev.evangelion.client.modules.visuals.ModuleNoRender;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import net.minecraft.client.renderer.GlStateManager;
import dev.evangelion.client.modules.visuals.ModuleModelChanger;
import dev.evangelion.Evangelion;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.item.ItemStack;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.client.renderer.ItemRenderer;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(value = { ItemRenderer.class }, priority = Integer.MAX_VALUE)
public class MixinItemRenderer
{
    @Inject(method = { "renderItemSide" }, at = { @At("HEAD") })
    public void renderItemSide(final EntityLivingBase entityLivingBase, final ItemStack stack, final ItemCameraTransforms.TransformType transform, final boolean leftHanded, final CallbackInfo info) {
        if (Evangelion.MODULE_MANAGER != null && Evangelion.MODULE_MANAGER.isModuleEnabled("ModelChanger") && ModuleModelChanger.INSTANCE != null) {
            GlStateManager.scale(ModuleModelChanger.INSTANCE.scaleX.getValue().floatValue(), ModuleModelChanger.INSTANCE.scaleY.getValue().floatValue(), ModuleModelChanger.INSTANCE.scaleZ.getValue().floatValue());
            if (transform == ItemCameraTransforms.TransformType.FIRST_PERSON_RIGHT_HAND) {
                GlStateManager.translate(ModuleModelChanger.INSTANCE.translateX.getValue().intValue() / 360.0f, ModuleModelChanger.INSTANCE.translateY.getValue().intValue() / 360.0f, ModuleModelChanger.INSTANCE.translateZ.getValue().intValue() / 360.0f);
                GlStateManager.rotate((float)ModuleModelChanger.INSTANCE.rotateX.getValue().intValue(), 1.0f, 0.0f, 0.0f);
                GlStateManager.rotate((float)ModuleModelChanger.INSTANCE.rotateY.getValue().intValue(), 0.0f, 1.0f, 0.0f);
                GlStateManager.rotate((float)ModuleModelChanger.INSTANCE.rotateZ.getValue().intValue(), 0.0f, 0.0f, 1.0f);
            }
            else if (transform == ItemCameraTransforms.TransformType.FIRST_PERSON_LEFT_HAND) {
                GlStateManager.translate(-ModuleModelChanger.INSTANCE.translateX.getValue().intValue() / 360.0f, ModuleModelChanger.INSTANCE.translateY.getValue().intValue() / 360.0f, ModuleModelChanger.INSTANCE.translateZ.getValue().intValue() / 360.0f);
                GlStateManager.rotate((float)(-ModuleModelChanger.INSTANCE.rotateX.getValue().intValue()), 1.0f, 0.0f, 0.0f);
                GlStateManager.rotate((float)ModuleModelChanger.INSTANCE.rotateY.getValue().intValue(), 0.0f, 1.0f, 0.0f);
                GlStateManager.rotate((float)ModuleModelChanger.INSTANCE.rotateZ.getValue().intValue(), 0.0f, 0.0f, 1.0f);
            }
        }
    }
    
    @Inject(method = { "renderFireInFirstPerson" }, at = { @At("HEAD") }, cancellable = true)
    public void renderFire(final CallbackInfo info) {
        if (Evangelion.MODULE_MANAGER.isModuleEnabled("NoRender") && ModuleNoRender.INSTANCE.fire.getValue()) {
            info.cancel();
        }
    }
    
    @Inject(method = { "renderOverlays" }, at = { @At("HEAD") }, cancellable = true)
    public void renderOverlays(final float partialTicks, final CallbackInfo info) {
        if (Evangelion.MODULE_MANAGER.isModuleEnabled("NoRender") && ModuleNoRender.INSTANCE.suffocation.getValue()) {
            info.cancel();
        }
    }
}
