package dev.evangelion.client.modules.visuals.chams;

import net.minecraft.util.ResourceLocation;
import dev.evangelion.api.utilities.ColorUtils;
import org.lwjgl.opengl.GL11;
import net.minecraft.client.renderer.GlStateManager;
import dev.evangelion.api.utilities.RenderUtils;
import net.minecraft.entity.Entity;
import net.minecraft.client.model.ModelBase;
import dev.evangelion.api.utilities.IMinecraft;

public class CrystalChams implements IMinecraft
{
    public void renderModel(final ModelBase model, final Entity entity, final float limbSwing, final float limbSwingAmount, final float ageInTicks, final float netHeadYaw, final float headPitch, final float scale) {
        RenderUtils.beginChams(ModuleChams.INSTANCE.C_width.getValue().floatValue());
        if (!ModuleChams.INSTANCE.C_changeScale.getValue().equals(ModuleChams.C_ScaleModes.None)) {
            if (ModuleChams.INSTANCE.C_changeScale.getValue().equals(ModuleChams.C_ScaleModes.Custom)) {
                GlStateManager.scale(ModuleChams.INSTANCE.C_scaleX.getValue().floatValue(), ModuleChams.INSTANCE.C_scaleY.getValue().floatValue(), ModuleChams.INSTANCE.C_scaleZ.getValue().floatValue());
            }
            else {
                GlStateManager.scale(ModuleChams.INSTANCE.C_scaleGlobal.getValue().floatValue(), ModuleChams.INSTANCE.C_scaleGlobal.getValue().floatValue(), ModuleChams.INSTANCE.C_scaleGlobal.getValue().floatValue());
            }
        }
        if (ModuleChams.INSTANCE.C_mode.getValue().equals(ModuleChams.C_Modes.Lines)) {
            GL11.glPolygonMode(1032, 6913);
        }
        if (ModuleChams.INSTANCE.C_texture.getValue()) {
            GlStateManager.enableTexture2D();
        }
        else {
            GlStateManager.disableTexture2D();
        }
        GlStateManager.disableDepth();
        GL11.glDepthMask(false);
        RenderUtils.glColor4f(ModuleChams.INSTANCE.C_hiddenColor.getValue());
        if (ModuleChams.INSTANCE.C_walls.getValue()) {
            model.render(entity, limbSwing, ModuleChams.INSTANCE.C_changeSpeed.getValue() ? (limbSwingAmount * ModuleChams.INSTANCE.C_speed.getValue().floatValue()) : limbSwingAmount, ModuleChams.INSTANCE.C_changeBounce.getValue() ? (ageInTicks * ModuleChams.INSTANCE.C_bounce.getValue().floatValue()) : ageInTicks, netHeadYaw, headPitch, scale);
            this.renderShine(model, entity, limbSwing, ModuleChams.INSTANCE.C_changeSpeed.getValue() ? (limbSwingAmount * ModuleChams.INSTANCE.C_speed.getValue().floatValue()) : limbSwingAmount, ModuleChams.INSTANCE.C_changeBounce.getValue() ? (ageInTicks * ModuleChams.INSTANCE.C_bounce.getValue().floatValue()) : ageInTicks, netHeadYaw, headPitch, scale);
            if (ModuleChams.INSTANCE.C_mode.getValue().equals(ModuleChams.C_Modes.Both)) {
                RenderUtils.glColor4f(ColorUtils.setAlpha(ModuleChams.INSTANCE.C_hiddenColor.getValue(), 255));
                GL11.glPolygonMode(1032, 6913);
                model.render(entity, limbSwing, ModuleChams.INSTANCE.C_changeSpeed.getValue() ? (limbSwingAmount * ModuleChams.INSTANCE.C_speed.getValue().floatValue()) : limbSwingAmount, ModuleChams.INSTANCE.C_changeBounce.getValue() ? (ageInTicks * ModuleChams.INSTANCE.C_bounce.getValue().floatValue()) : ageInTicks, netHeadYaw, headPitch, scale);
                GL11.glPolygonMode(1032, 6914);
            }
        }
        GlStateManager.enableDepth();
        GL11.glDepthMask(true);
        RenderUtils.glColor4f(ModuleChams.INSTANCE.C_visibleColor.getValue());
        model.render(entity, limbSwing, ModuleChams.INSTANCE.C_changeSpeed.getValue() ? (limbSwingAmount * ModuleChams.INSTANCE.C_speed.getValue().floatValue()) : limbSwingAmount, ModuleChams.INSTANCE.C_changeBounce.getValue() ? (ageInTicks * ModuleChams.INSTANCE.C_bounce.getValue().floatValue()) : ageInTicks, netHeadYaw, headPitch, scale);
        this.renderShine(model, entity, limbSwing, ModuleChams.INSTANCE.C_changeSpeed.getValue() ? (limbSwingAmount * ModuleChams.INSTANCE.C_speed.getValue().floatValue()) : limbSwingAmount, ModuleChams.INSTANCE.C_changeBounce.getValue() ? (ageInTicks * ModuleChams.INSTANCE.C_bounce.getValue().floatValue()) : ageInTicks, netHeadYaw, headPitch, scale);
        if (ModuleChams.INSTANCE.C_mode.getValue().equals(ModuleChams.C_Modes.Both)) {
            RenderUtils.glColor4f(ColorUtils.setAlpha(ModuleChams.INSTANCE.C_visibleColor.getValue(), 255));
            GL11.glPolygonMode(1032, 6913);
            model.render(entity, limbSwing, ModuleChams.INSTANCE.C_changeSpeed.getValue() ? (limbSwingAmount * ModuleChams.INSTANCE.C_speed.getValue().floatValue()) : limbSwingAmount, ModuleChams.INSTANCE.C_changeBounce.getValue() ? (ageInTicks * ModuleChams.INSTANCE.C_bounce.getValue().floatValue()) : ageInTicks, netHeadYaw, headPitch, scale);
            GL11.glPolygonMode(1032, 6914);
        }
        if (ModuleChams.INSTANCE.C_texture.getValue()) {
            GlStateManager.disableTexture2D();
        }
        else {
            GlStateManager.enableTexture2D();
        }
        if (ModuleChams.INSTANCE.C_mode.getValue().equals(ModuleChams.C_Modes.Lines)) {
            GL11.glPolygonMode(1032, 6914);
        }
        RenderUtils.finalizeChams();
    }
    
    private void renderShine(final ModelBase model, final Entity entity, final float limbSwing, final float limbSwingAmount, final float ageInTicks, final float netHeadYaw, final float headPitch, final float scale) {
        if (ModuleChams.INSTANCE.C_shine.getValue()) {
            CrystalChams.mc.getTextureManager().bindTexture(new ResourceLocation("textures/misc/enchanted_item_glint.png"));
            GL11.glEnable(3553);
            GL11.glBlendFunc(768, 771);
            model.render(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
            GL11.glBlendFunc(770, 771);
            GL11.glDisable(3553);
        }
    }
}
