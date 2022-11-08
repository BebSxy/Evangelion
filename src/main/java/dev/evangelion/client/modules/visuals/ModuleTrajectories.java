package dev.evangelion.client.modules.visuals;

import java.util.ArrayList;
import java.util.Iterator;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.opengl.GL11;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import dev.evangelion.api.utilities.RenderUtils;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemLingeringPotion;
import net.minecraft.item.ItemSplashPotion;
import net.minecraft.item.ItemExpBottle;
import net.minecraft.item.ItemSnowball;
import net.minecraft.item.ItemEgg;
import net.minecraft.item.ItemEnderPearl;
import net.minecraft.item.ItemFishingRod;
import net.minecraft.util.EnumHand;
import net.minecraft.item.ItemBow;
import dev.evangelion.client.events.EventRender3D;
import java.awt.Color;
import dev.evangelion.client.values.impl.ValueColor;
import dev.evangelion.api.manager.module.RegisterModule;
import dev.evangelion.api.manager.module.Module;

@RegisterModule(name = "Trajectories", description = "Render where your throwable item is going.", category = Module.Category.VISUALS)
public class ModuleTrajectories extends Module
{
    ValueColor color;
    private static double velocity;
    private static double gravity;
    
    public ModuleTrajectories() {
        this.color = new ValueColor("Color", "Color", "", new Color(255, 0, 0, 120));
    }
    
    @Override
    public void onRender3D(final EventRender3D event) {
        super.onRender3D(event);
        if (this.nullCheck()) {
            return;
        }
        if (ModuleTrajectories.mc.gameSettings.thirdPersonView != 0 || (!(ModuleTrajectories.mc.player.getHeldItem(EnumHand.MAIN_HAND).getItem() instanceof ItemBow) && !(ModuleTrajectories.mc.player.getHeldItem(EnumHand.MAIN_HAND).getItem() instanceof ItemFishingRod) && !(ModuleTrajectories.mc.player.getHeldItem(EnumHand.MAIN_HAND).getItem() instanceof ItemEnderPearl) && !(ModuleTrajectories.mc.player.getHeldItem(EnumHand.MAIN_HAND).getItem() instanceof ItemEgg) && !(ModuleTrajectories.mc.player.getHeldItem(EnumHand.MAIN_HAND).getItem() instanceof ItemSnowball) && !(ModuleTrajectories.mc.player.getHeldItem(EnumHand.MAIN_HAND).getItem() instanceof ItemExpBottle) && !(ModuleTrajectories.mc.player.getHeldItem(EnumHand.MAIN_HAND).getItem() instanceof ItemSplashPotion))) {
            return;
        }
        boolean holdingBow = ModuleTrajectories.mc.player.getHeldItem(EnumHand.MAIN_HAND).getItem() instanceof ItemBow || ModuleTrajectories.mc.player.getHeldItem(EnumHand.OFF_HAND).getItem() instanceof ItemBow;
        boolean holdingThrowable = ModuleTrajectories.mc.player.getHeldItem(EnumHand.MAIN_HAND).getItem() instanceof ItemSplashPotion || ModuleTrajectories.mc.player.getHeldItem(EnumHand.OFF_HAND).getItem() instanceof ItemSplashPotion || ModuleTrajectories.mc.player.getHeldItem(EnumHand.MAIN_HAND).getItem() instanceof ItemLingeringPotion || ModuleTrajectories.mc.player.getHeldItem(EnumHand.OFF_HAND).getItem() instanceof ItemLingeringPotion || ModuleTrajectories.mc.player.getHeldItem(EnumHand.MAIN_HAND).getItem() instanceof ItemExpBottle || ModuleTrajectories.mc.player.getHeldItem(EnumHand.OFF_HAND).getItem() instanceof ItemExpBottle;
        if (ModuleTrajectories.mc.player.getHeldItem(EnumHand.MAIN_HAND).getItem() instanceof ItemBow || ModuleTrajectories.mc.player.getHeldItem(EnumHand.MAIN_HAND).getItem() instanceof ItemSnowball || ModuleTrajectories.mc.player.getHeldItem(EnumHand.MAIN_HAND).getItem() instanceof ItemEnderPearl || ModuleTrajectories.mc.player.getHeldItem(EnumHand.MAIN_HAND).getItem() instanceof ItemEgg) {
            holdingThrowable = false;
        }
        if (ModuleTrajectories.mc.player.getHeldItem(EnumHand.MAIN_HAND).getItem() instanceof ItemSplashPotion || ModuleTrajectories.mc.player.getHeldItem(EnumHand.MAIN_HAND).getItem() instanceof ItemLingeringPotion || ModuleTrajectories.mc.player.getHeldItem(EnumHand.MAIN_HAND).getItem() instanceof ItemSnowball || ModuleTrajectories.mc.player.getHeldItem(EnumHand.MAIN_HAND).getItem() instanceof ItemEnderPearl || ModuleTrajectories.mc.player.getHeldItem(EnumHand.MAIN_HAND).getItem() instanceof ItemEgg || ModuleTrajectories.mc.player.getHeldItem(EnumHand.MAIN_HAND).getItem() instanceof ItemExpBottle) {
            holdingBow = false;
        }
        if (holdingThrowable && (ModuleTrajectories.mc.player.getHeldItem(EnumHand.MAIN_HAND).getItem() instanceof ItemPotion || ModuleTrajectories.mc.player.getHeldItem(EnumHand.OFF_HAND).getItem() instanceof ItemPotion)) {
            ModuleTrajectories.velocity = 0.5;
            ModuleTrajectories.gravity = 0.05;
        }
        else if (holdingThrowable && (ModuleTrajectories.mc.player.getHeldItem(EnumHand.MAIN_HAND).getItem() instanceof ItemExpBottle || ModuleTrajectories.mc.player.getHeldItem(EnumHand.OFF_HAND).getItem() instanceof ItemExpBottle)) {
            ModuleTrajectories.velocity = 0.7;
            ModuleTrajectories.gravity = 0.07;
        }
        else if (holdingBow) {
            ModuleTrajectories.velocity = 1.5;
            ModuleTrajectories.gravity = 0.05;
        }
        else if (ModuleTrajectories.mc.player.getHeldItemMainhand().getItem() instanceof ItemFishingRod || ModuleTrajectories.mc.player.getHeldItemOffhand().getItem() instanceof ItemFishingRod) {
            ModuleTrajectories.velocity = 1.5;
            ModuleTrajectories.gravity = 0.15;
        }
        else {
            ModuleTrajectories.velocity = 1.5;
            ModuleTrajectories.gravity = 0.03;
        }
        final EntityPlayerSP player = ModuleTrajectories.mc.player;
        final float throwingYaw = player.rotationYaw;
        final float throwingPitch = player.rotationPitch;
        final Vec3d renderPos = RenderUtils.interpolateEntity((Entity)player, event.getPartialTicks());
        final float f = throwingYaw / 180.0f * 3.1415927f;
        final boolean bl2 = false;
        double posX = renderPos.x - (float)Math.cos(f) * 0.16f;
        double posY = renderPos.y + player.eyeHeight - 0.10000000149011612;
        final float f2 = throwingYaw / 180.0f * 3.1415927f;
        final boolean bl3 = false;
        double posZ = renderPos.z - (float)Math.sin(f2) * 0.16f;
        final double holdingBowMultiplier = holdingBow ? 1.0 : 0.4;
        float f3 = throwingYaw / 180.0f * 3.1415927f;
        boolean bl4 = false;
        final float f4 = -(float)Math.sin(f3);
        f3 = throwingPitch / 180.0f * 3.1415927f;
        bl4 = false;
        double motionX = f4 * (float)Math.cos(f3) * holdingBowMultiplier;
        final float f5 = (throwingPitch - (holdingThrowable ? 20 : 0)) / 180.0f * 3.1415927f;
        final boolean bl5 = false;
        double motionY = -(float)Math.sin(f5) * holdingBowMultiplier;
        float f6 = throwingYaw / 180.0f * 3.1415927f;
        boolean bl6 = false;
        final float f7 = (float)Math.cos(f6);
        f6 = throwingPitch / 180.0f * 3.1415927f;
        bl6 = false;
        double motionZ = f7 * (float)Math.cos(f6) * holdingBowMultiplier;
        if (!ModuleTrajectories.mc.player.onGround && !holdingBow) {
            motionY += ModuleTrajectories.mc.player.motionY;
        }
        final double d2;
        final double useDuration = d2 = (72000.0 - ModuleTrajectories.mc.player.getItemInUseCount()) / 20.0;
        final int n = 2;
        final boolean bl7 = false;
        double power = (Math.pow(d2, n) + useDuration * 2.0) / 3.0;
        if (power < 0.1000000014901161) {
            return;
        }
        if (power > 1.0) {
            power = 1.0;
        }
        double d3 = motionX;
        int n2 = 2;
        boolean bl8 = false;
        final double d4 = Math.pow(d3, n2);
        d3 = motionY;
        n2 = 2;
        bl8 = false;
        final double d5 = d4 + Math.pow(d3, n2);
        d3 = motionZ;
        n2 = 2;
        bl8 = false;
        d3 = d5 + Math.pow(d3, n2);
        n2 = 0;
        final double distance = Math.sqrt(d3);
        motionX /= distance;
        motionY /= distance;
        motionZ /= distance;
        motionX *= (holdingBow ? (power * 2.0) : 1.0) * ModuleTrajectories.velocity;
        motionY *= (holdingBow ? (power * 2.0) : 1.0) * ModuleTrajectories.velocity;
        motionZ *= (holdingBow ? (power * 2.0) : 1.0) * ModuleTrajectories.velocity;
        GlStateManager.pushMatrix();
        RenderUtils.prepare();
        GlStateManager.disableTexture2D();
        GlStateManager.enableAlpha();
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GL11.glLineWidth(1.5f);
        RenderUtils.glColor4f(this.color.getValue());
        GL11.glBegin(3);
        boolean hasLanded = false;
        final Object hitEntity = null;
        RayTraceResult landingPosition = null;
        while (!hasLanded && posY > 0.0) {
            final Vec3d present = new Vec3d(posX, posY, posZ);
            final Vec3d future = new Vec3d(posX + motionX, posY + motionY, posZ + motionZ);
            final RayTraceResult possibleLandingStrip = ModuleTrajectories.mc.world.rayTraceBlocks(present, future, false, true, false);
            if (possibleLandingStrip != null) {
                if (possibleLandingStrip.typeOfHit != RayTraceResult.Type.MISS) {
                    landingPosition = possibleLandingStrip;
                    hasLanded = true;
                }
            }
            else {
                final Entity entityHit = this.getEntityHit(present, future);
                if (entityHit != null) {
                    landingPosition = new RayTraceResult(entityHit);
                    hasLanded = true;
                }
            }
            final double motionAdjustmentHorizontal = 0.9900000095367432;
            motionY *= motionAdjustmentHorizontal;
            GL11.glVertex3d((posX += (motionX *= motionAdjustmentHorizontal)) - ModuleTrajectories.mc.getRenderManager().renderPosX, (posY += (motionY -= ModuleTrajectories.gravity)) - ModuleTrajectories.mc.getRenderManager().renderPosY, (posZ += (motionZ *= motionAdjustmentHorizontal)) - ModuleTrajectories.mc.getRenderManager().renderPosZ);
        }
        GL11.glEnd();
        GL11.glPushMatrix();
        GL11.glTranslated(posX - ModuleTrajectories.mc.getRenderManager().renderPosX, posY - ModuleTrajectories.mc.getRenderManager().renderPosY, posZ - ModuleTrajectories.mc.getRenderManager().renderPosZ);
        if (landingPosition != null && landingPosition.typeOfHit == RayTraceResult.Type.BLOCK) {
            final int present2 = landingPosition.sideHit.getIndex();
            switch (present2) {
                case 1: {
                    GL11.glRotatef(180.0f, 1.0f, 0.0f, 0.0f);
                    break;
                }
                case 2: {
                    GL11.glRotatef(90.0f, 1.0f, 0.0f, 0.0f);
                    break;
                }
                case 3: {
                    GL11.glRotatef(-90.0f, 1.0f, 0.0f, 0.0f);
                    break;
                }
                case 4: {
                    GL11.glRotatef(-90.0f, 0.0f, 0.0f, 1.0f);
                    break;
                }
                case 5: {
                    GL11.glRotatef(90.0f, 0.0f, 0.0f, 1.0f);
                    break;
                }
            }
            GL11.glRotatef(-90.0f, 1.0f, 0.0f, 0.0f);
            RenderUtils.drawRect(-0.6f, -0.6f, 0.6f, 0.6f, this.color.getValue());
        }
        GL11.glPopMatrix();
        if (landingPosition != null && landingPosition.typeOfHit == RayTraceResult.Type.ENTITY) {
            final Entity target = landingPosition.entityHit;
            GL11.glPushMatrix();
            final double x = target.prevPosX + (target.posX - target.prevPosX) * ModuleTrajectories.mc.getRenderPartialTicks() - ModuleTrajectories.mc.renderManager.renderPosX;
            final double y = target.prevPosY + (target.posY - target.prevPosY) * ModuleTrajectories.mc.getRenderPartialTicks() - ModuleTrajectories.mc.renderManager.renderPosY;
            final double z = target.prevPosZ + (target.posZ - target.prevPosZ) * ModuleTrajectories.mc.getRenderPartialTicks() - ModuleTrajectories.mc.renderManager.renderPosZ;
            final AxisAlignedBB bb1 = target.getEntityBoundingBox();
            final AxisAlignedBB bb2 = new AxisAlignedBB(bb1.minX - target.posX, bb1.minY - target.posY, bb1.minZ - target.posZ, bb1.maxX - target.posX, bb1.maxY - target.posY, bb1.maxZ - target.posZ);
            GL11.glTranslated(x, y, z);
            RenderUtils.drawBlock(bb2, this.color.getValue());
            GL11.glPopMatrix();
        }
        GlStateManager.disableAlpha();
        GlStateManager.disableBlend();
        RenderUtils.release();
        GlStateManager.popMatrix();
    }
    
    private final Entity getEntityHit(final Vec3d present, final Vec3d future) {
        for (final EntityLivingBase entity : this.getEntities()) {
            if (entity != ModuleTrajectories.mc.player) {
                if (!entity.canBeCollidedWith()) {
                    continue;
                }
                final double expander = 0.30000001192092896;
                if (entity.getEntityBoundingBox().expand(expander, expander, expander).calculateIntercept(present, future) == null) {
                    continue;
                }
                return (Entity)entity;
            }
        }
        return null;
    }
    
    private final ArrayList<EntityLivingBase> getEntities() {
        final ArrayList<EntityLivingBase> list = new ArrayList<EntityLivingBase>();
        for (final Entity entity : ModuleTrajectories.mc.world.loadedEntityList) {
            if (entity != ModuleTrajectories.mc.player) {
                if (!(entity instanceof EntityLivingBase)) {
                    continue;
                }
                list.add((EntityLivingBase)entity);
            }
        }
        return list;
    }
}
