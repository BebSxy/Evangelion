package dev.evangelion.client.modules.combat;

import dev.evangelion.api.utilities.RenderUtils;
import net.minecraft.util.math.AxisAlignedBB;
import org.lwjgl.opengl.GL11;
import dev.evangelion.client.events.EventRender3D;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.util.EnumHand;
import net.minecraft.entity.player.EntityPlayer;
import dev.evangelion.client.modules.client.ModuleRotations;
import net.minecraft.entity.Entity;
import dev.evangelion.api.utilities.RotationUtils;
import dev.evangelion.Evangelion;
import net.minecraft.item.ItemSword;
import dev.evangelion.api.utilities.InventoryUtils;
import net.minecraft.init.Items;
import dev.evangelion.client.events.EventMotion;
import java.awt.Color;
import dev.evangelion.api.utilities.TargetUtils;
import net.minecraft.entity.EntityLivingBase;
import dev.evangelion.client.values.impl.ValueColor;
import dev.evangelion.client.values.impl.ValueNumber;
import dev.evangelion.client.values.impl.ValueBoolean;
import dev.evangelion.client.values.impl.ValueEnum;
import dev.evangelion.api.manager.module.RegisterModule;
import dev.evangelion.api.manager.module.Module;

@RegisterModule(name = "KillAura", tag = "Kill Aura", description = "Automatically attacks entities.", category = Module.Category.COMBAT)
public class ModuleKillAura extends Module
{
    ValueEnum mode;
    ValueEnum weaponMode;
    ValueBoolean hitDelay;
    ValueBoolean criticals;
    ValueNumber range;
    ValueBoolean visibleOnly;
    ValueNumber wallsRange;
    ValueBoolean render;
    ValueColor renderColor;
    private EntityLivingBase target;
    
    public ModuleKillAura() {
        this.mode = new ValueEnum("Mode", "Mode", "Mode of selecting the target.", TargetUtils.TargetMode.Range);
        this.weaponMode = new ValueEnum("Weapon", "Weapon", "Mode for weapon.", WeaponMode.Switch);
        this.hitDelay = new ValueBoolean("HitDelay", "Hit Delay", "Add a delay for 1.9 pvp.", true);
        this.criticals = new ValueBoolean("Criticals", "Criticals", "Do critical hits when attacking", true);
        this.range = new ValueNumber("Range", "Range", "The range for attacking the target.", 5.0f, 0.0f, 6.0f);
        this.visibleOnly = new ValueBoolean("VisibleOnly", "Visible Only", "", false);
        this.wallsRange = new ValueNumber("WallsRange", "Walls Range", "The range for attacking the target through walls.", 3.5f, 0.0f, 6.0f);
        this.render = new ValueBoolean("Render", "Render", "Render target.", false);
        this.renderColor = new ValueColor("RenderColor", "Render Color", "Color for render of target.", new Color(255, 0, 0));
        this.target = null;
    }
    
    @SubscribeEvent
    public void onMotion(final EventMotion event) {
        super.onUpdate();
        if (super.nullCheck()) {
            return;
        }
        this.target = TargetUtils.getTarget(this.range.getValue().floatValue(), this.wallsRange.getValue().floatValue(), this.visibleOnly.getValue(), (TargetUtils.TargetMode)this.mode.getValue());
        if (this.target != null) {
            if (this.weaponMode.getValue().equals(WeaponMode.Switch)) {
                final int swordSlot = InventoryUtils.findItem(Items.DIAMOND_SWORD, 0, 9);
                if (swordSlot != -1) {
                    InventoryUtils.switchSlot(swordSlot, false);
                }
            }
            else if (this.weaponMode.getValue().equals(WeaponMode.Require) && !(ModuleKillAura.mc.player.inventory.getCurrentItem().getItem() instanceof ItemSword)) {
                return;
            }
            if (Evangelion.MODULE_MANAGER.isModuleEnabled("Rotations")) {
                final float[] rot = RotationUtils.getSmoothRotations(RotationUtils.getRotationsEntity((Entity)this.target), ModuleRotations.INSTANCE.smoothness.getValue().intValue());
                RotationUtils.rotate(event, rot);
            }
            if (this.hitDelay.getValue()) {
                if (ModuleKillAura.mc.player.getCooledAttackStrength(0.0f) >= 1.0f) {
                    if (this.criticals.getValue()) {
                        ModuleCriticals.doCritical(ModuleCriticals.mode.getValue().toString());
                    }
                    ModuleKillAura.mc.playerController.attackEntity((EntityPlayer)ModuleKillAura.mc.player, (Entity)this.target);
                    ModuleKillAura.mc.player.swingArm(EnumHand.MAIN_HAND);
                }
            }
            else {
                if (this.criticals.getValue()) {
                    ModuleCriticals.doCritical(ModuleCriticals.mode.getValue().toString());
                }
                ModuleKillAura.mc.playerController.attackEntity((EntityPlayer)ModuleKillAura.mc.player, (Entity)this.target);
                ModuleKillAura.mc.player.swingArm(EnumHand.MAIN_HAND);
            }
        }
    }
    
    @Override
    public void onRender3D(final EventRender3D event) {
        super.onRender3D(event);
        if (this.render.getValue() && this.target != null) {
            if (!this.weaponMode.getValue().equals(WeaponMode.None) && !(ModuleKillAura.mc.player.inventory.getCurrentItem().getItem() instanceof ItemSword)) {
                return;
            }
            GL11.glPushMatrix();
            final double x = this.target.prevPosX + (this.target.posX - this.target.prevPosX) * ModuleKillAura.mc.getRenderPartialTicks() - ModuleKillAura.mc.renderManager.renderPosX;
            final double y = this.target.prevPosY + (this.target.posY - this.target.prevPosY) * ModuleKillAura.mc.getRenderPartialTicks() - ModuleKillAura.mc.renderManager.renderPosY;
            final double z = this.target.prevPosZ + (this.target.posZ - this.target.prevPosZ) * ModuleKillAura.mc.getRenderPartialTicks() - ModuleKillAura.mc.renderManager.renderPosZ;
            final AxisAlignedBB bb1 = this.target.getEntityBoundingBox();
            final AxisAlignedBB bb2 = new AxisAlignedBB(bb1.minX - this.target.posX, bb1.minY - this.target.posY, bb1.minZ - this.target.posZ, bb1.maxX - this.target.posX, bb1.maxY - this.target.posY, bb1.maxZ - this.target.posZ);
            GL11.glTranslated(x, y, z);
            RenderUtils.drawBlock(bb2, this.renderColor.getValue());
            GL11.glPopMatrix();
        }
    }
    
    @Override
    public String getHudInfo() {
        return this.mode.getValue().name();
    }
    
    public enum WeaponMode
    {
        Require, 
        Switch, 
        None;
    }
}
