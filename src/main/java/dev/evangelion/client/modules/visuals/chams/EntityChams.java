package dev.evangelion.client.modules.visuals.chams;

import net.minecraft.util.ResourceLocation;
import dev.evangelion.api.utilities.ColorUtils;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;
import dev.evangelion.api.utilities.RenderUtils;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.Entity;
import net.minecraft.client.model.ModelBase;
import dev.evangelion.api.utilities.IMinecraft;

public class EntityChams implements IMinecraft
{
    public void renderModel(final ModelBase model, final Entity entity, final float limbSwing, final float limbSwingAmount, final float ageInTicks, final float netHeadYaw, final float headPitch, final float scale) {
        if (entity != ModuleChams.INSTANCE.player) {
            if ((entity instanceof EntityPlayer && ModuleChams.INSTANCE.players.getValue()) || (entity instanceof EntityMob && ModuleChams.INSTANCE.mobs.getValue()) || (entity instanceof EntityAnimal && ModuleChams.INSTANCE.animals.getValue())) {
                RenderUtils.beginChams(ModuleChams.INSTANCE.E_width.getValue().floatValue());
                if (ModuleChams.INSTANCE.E_mode.getValue().equals(ModuleChams.E_Modes.Lines)) {
                    GL11.glPolygonMode(1032, 6913);
                }
                if (ModuleChams.INSTANCE.E_texture.getValue()) {
                    GlStateManager.enableTexture2D();
                }
                else {
                    GlStateManager.disableTexture2D();
                }
                GlStateManager.disableDepth();
                GL11.glDepthMask(false);
                RenderUtils.glColor4f(ModuleChams.INSTANCE.E_hiddenColor.getValue());
                if (ModuleChams.INSTANCE.E_walls.getValue()) {
                    model.render(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
                    this.renderShine(model, entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
                }
                if (ModuleChams.INSTANCE.E_mode.getValue().equals(ModuleChams.E_Modes.Both)) {
                    RenderUtils.glColor4f(ColorUtils.setAlpha(ModuleChams.INSTANCE.E_hiddenColor.getValue(), 255));
                    GL11.glPolygonMode(1032, 6913);
                    model.render(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
                    GL11.glPolygonMode(1032, 6914);
                }
                GlStateManager.enableDepth();
                GL11.glDepthMask(true);
                RenderUtils.glColor4f(ModuleChams.INSTANCE.E_visibleColor.getValue());
                model.render(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
                this.renderShine(model, entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
                if (ModuleChams.INSTANCE.E_mode.getValue().equals(ModuleChams.E_Modes.Both)) {
                    RenderUtils.glColor4f(ColorUtils.setAlpha(ModuleChams.INSTANCE.E_visibleColor.getValue(), 255));
                    GL11.glPolygonMode(1032, 6913);
                    model.render(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
                    GL11.glPolygonMode(1032, 6914);
                }
                if (ModuleChams.INSTANCE.E_texture.getValue()) {
                    GlStateManager.disableTexture2D();
                }
                else {
                    GlStateManager.enableTexture2D();
                }
                if (ModuleChams.INSTANCE.E_mode.getValue().equals(ModuleChams.E_Modes.Lines)) {
                    GL11.glPolygonMode(1032, 6914);
                }
                RenderUtils.finalizeChams();
            }
            else {
                model.render(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
            }
        }
        else if (ModuleChams.INSTANCE.player != null) {
            RenderUtils.beginChams(ModuleChams.INSTANCE.P_width.getValue().floatValue());
            if (ModuleChams.INSTANCE.P_mode.getValue().equals(ModuleChams.P_Modes.Lines)) {
                GL11.glPolygonMode(1032, 6913);
            }
            GL11.glColor4f(ModuleChams.INSTANCE.P_color.getValue().getRed() / 255.0f, ModuleChams.INSTANCE.P_color.getValue().getGreen() / 255.0f, ModuleChams.INSTANCE.P_color.getValue().getBlue() / 255.0f, ModuleChams.INSTANCE.opacity);
            model.render(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
            if (ModuleChams.INSTANCE.P_mode.getValue().equals(ModuleChams.P_Modes.Lines)) {
                GL11.glPolygonMode(1032, 6914);
            }
            if (ModuleChams.INSTANCE.P_mode.getValue().equals(ModuleChams.P_Modes.Both)) {
                GL11.glColor4f(ModuleChams.INSTANCE.P_color.getValue().getRed() / 255.0f, ModuleChams.INSTANCE.P_color.getValue().getGreen() / 255.0f, ModuleChams.INSTANCE.P_color.getValue().getBlue() / 255.0f, ModuleChams.INSTANCE.outlineOpacity);
                GL11.glPolygonMode(1032, 6913);
                model.render(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
                GL11.glPolygonMode(1032, 6914);
            }
            RenderUtils.finalizeChams();
        }
    }
    
    private void renderShine(final ModelBase model, final Entity entity, final float limbSwing, final float limbSwingAmount, final float ageInTicks, final float netHeadYaw, final float headPitch, final float scale) {
        if (ModuleChams.INSTANCE.E_shine.getValue()) {
            EntityChams.mc.getTextureManager().bindTexture(new ResourceLocation("textures/misc/enchanted_item_glint.png"));
            GL11.glEnable(3553);
            GL11.glBlendFunc(768, 771);
            model.render(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
            GL11.glBlendFunc(770, 771);
            GL11.glDisable(3553);
        }
    }
}
