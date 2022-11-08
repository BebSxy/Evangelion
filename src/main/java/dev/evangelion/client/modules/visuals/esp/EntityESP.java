package dev.evangelion.client.modules.visuals.esp;

import java.awt.Color;
import dev.evangelion.api.utilities.OutlineUtils;
import dev.evangelion.client.modules.visuals.chams.ModuleChams;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.client.model.ModelBase;
import dev.evangelion.api.utilities.IMinecraft;

public class EntityESP implements IMinecraft
{
    public void renderEntities(final ModelBase model, final Entity entity, final float limbSwing, final float limbSwingAmount, final float ageInTicks, final float netHeadYaw, final float headPitch, final float scale) {
        GlStateManager.depthMask(true);
        if (((entity instanceof EntityPlayer && ModuleESP.INSTANCE.players.getValue()) || (entity instanceof EntityMob && ModuleESP.INSTANCE.mobs.getValue()) || (entity instanceof EntityAnimal && ModuleESP.INSTANCE.animals.getValue())) && entity != ModuleChams.INSTANCE.player && entity != EntityESP.mc.player) {
            OutlineUtils.renderOne(ModuleESP.INSTANCE.E_width.getValue().floatValue());
            model.render(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
            OutlineUtils.renderTwo();
            model.render(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
            OutlineUtils.renderThree();
            OutlineUtils.renderFour(ModuleESP.INSTANCE.E_color.getValue());
            model.render(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
            OutlineUtils.renderFive();
            OutlineUtils.setColor(Color.WHITE);
        }
    }
    
    public void renderCrystals(final ModelBase model, final Entity entity, final float limbSwing, final float limbSwingAmount, final float ageInTicks, final float netHeadYaw, final float headPitch, final float scale) {
        GlStateManager.depthMask(true);
        OutlineUtils.renderOne(ModuleESP.INSTANCE.E_width.getValue().floatValue());
        model.render(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
        OutlineUtils.renderTwo();
        model.render(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
        OutlineUtils.renderThree();
        OutlineUtils.renderFour(ModuleESP.INSTANCE.E_color.getValue());
        model.render(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
        OutlineUtils.renderFive();
        OutlineUtils.setColor(Color.WHITE);
    }
}
