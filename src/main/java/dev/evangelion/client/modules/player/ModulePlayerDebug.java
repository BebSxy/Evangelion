package dev.evangelion.client.modules.player;

import dev.evangelion.Evangelion;
import dev.evangelion.client.events.EventRender2D;
import java.awt.Color;
import dev.evangelion.api.utilities.RenderUtils;
import net.minecraft.util.math.AxisAlignedBB;
import dev.evangelion.client.events.EventRender3D;
import java.text.DecimalFormat;
import dev.evangelion.client.values.impl.ValueBoolean;
import dev.evangelion.api.manager.module.RegisterModule;
import dev.evangelion.api.manager.module.Module;

@RegisterModule(name = "PlayerDebug", tag = "Player Debug", description = "Module for devs.", category = Module.Category.PLAYER)
public class ModulePlayerDebug extends Module
{
    ValueBoolean motionX;
    ValueBoolean motionY;
    ValueBoolean motionZ;
    ValueBoolean worldTime;
    ValueBoolean renderMotion;
    DecimalFormat format;
    
    public ModulePlayerDebug() {
        this.motionX = new ValueBoolean("MotionX", "Motion X", "", true);
        this.motionY = new ValueBoolean("MotionY", "Motion Y", "", true);
        this.motionZ = new ValueBoolean("MotionZ", "Motion Z", "", true);
        this.worldTime = new ValueBoolean("WorldTime", "World Time", "", false);
        this.renderMotion = new ValueBoolean("RenderMotion", "Render Motion", "", false);
        this.format = new DecimalFormat("#.#");
    }
    
    @Override
    public void onRender3D(final EventRender3D eventRender3D) {
        if (this.renderMotion.getValue()) {
            final AxisAlignedBB playerBB = ModulePlayerDebug.mc.player.getEntityBoundingBox();
            AxisAlignedBB bb = new AxisAlignedBB(playerBB.minX + ModulePlayerDebug.mc.player.motionX, playerBB.minY + ModulePlayerDebug.mc.player.motionY, playerBB.minZ + ModulePlayerDebug.mc.player.motionZ, playerBB.maxX + ModulePlayerDebug.mc.player.motionX, playerBB.maxY + ModulePlayerDebug.mc.player.motionY, playerBB.maxZ + ModulePlayerDebug.mc.player.motionZ);
            bb = RenderUtils.getRenderBB(bb);
            RenderUtils.drawBlockOutline(bb, Color.CYAN, 1.0f);
        }
    }
    
    @Override
    public void onRender2D(final EventRender2D event) {
        super.onRender2D(event);
        if (this.motionX.getValue()) {
            Evangelion.FONT_MANAGER.drawStringWithShadow("Motion X: " + this.format.format(ModulePlayerDebug.mc.player.motionX), 50.0f, 50.0f, Color.WHITE);
        }
        if (this.motionY.getValue()) {
            Evangelion.FONT_MANAGER.drawStringWithShadow("Motion Y: " + this.format.format(ModulePlayerDebug.mc.player.motionY), 50.0f, 60.0f, Color.WHITE);
        }
        if (this.motionZ.getValue()) {
            Evangelion.FONT_MANAGER.drawStringWithShadow("Motion Z: " + this.format.format(ModulePlayerDebug.mc.player.motionZ), 50.0f, 70.0f, Color.WHITE);
        }
        if (this.worldTime.getValue()) {
            Evangelion.FONT_MANAGER.drawStringWithShadow("World Time: " + this.format.format(ModulePlayerDebug.mc.timer.tickLength), 50.0f, 80.0f, Color.WHITE);
        }
    }
}
