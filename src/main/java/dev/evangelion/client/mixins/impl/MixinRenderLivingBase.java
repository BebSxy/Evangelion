package dev.evangelion.client.mixins.impl;

import org.spongepowered.asm.mixin.Overwrite;
import dev.evangelion.client.modules.visuals.chams.ModuleChams;
import dev.evangelion.client.modules.visuals.esp.ModuleESP;
import dev.evangelion.Evangelion;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderManager;
import org.spongepowered.asm.mixin.Shadow;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import org.spongepowered.asm.mixin.Mixin;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.EntityLivingBase;

@Mixin({ RenderLivingBase.class })
public abstract class MixinRenderLivingBase<T extends EntityLivingBase> extends Render<T>
{
    @Shadow
    protected ModelBase mainModel;
    
    @Shadow
    protected boolean isVisible(final T p_193115_1_) {
        return !p_193115_1_.isInvisible() || this.renderOutlines;
    }
    
    public MixinRenderLivingBase(final RenderManager renderManagerIn, final ModelBase modelBaseIn, final float shadowSizeIn) {
        super(renderManagerIn);
    }

    @Overwrite
    protected void renderModel(T entitylivingbaseIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor) {
        boolean flag1;
        boolean flag = this.isVisible(entitylivingbaseIn);
        boolean bl = flag1 = !flag && !entitylivingbaseIn.isInvisibleToPlayer((EntityPlayer)Minecraft.getMinecraft().player);
        if (flag || flag1) {
            if (!this.bindEntityTexture((T) entitylivingbaseIn)) {
                return;
            }
            if (flag1) {
                GlStateManager.enableBlendProfile((GlStateManager.Profile)GlStateManager.Profile.TRANSPARENT_MODEL);
            }
            if (Evangelion.MODULE_MANAGER.isModuleEnabled("ESP")) {
                ModuleESP.INSTANCE.entityESP.renderEntities(this.mainModel, (Entity)entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor);
            }
            if (Evangelion.MODULE_MANAGER.isModuleEnabled("Chams")) {
                ModuleChams.INSTANCE.entityChams.renderModel(this.mainModel, (Entity)entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor);
            } else {
                this.mainModel.render(entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor);
            }
            if (flag1) {
                GlStateManager.disableBlendProfile((GlStateManager.Profile)GlStateManager.Profile.TRANSPARENT_MODEL);
            }
        }
    }
}
