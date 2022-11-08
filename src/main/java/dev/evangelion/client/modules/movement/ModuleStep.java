package dev.evangelion.client.modules.movement;

import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.block.material.Material;
import dev.evangelion.client.events.EventStep;
import dev.evangelion.client.values.impl.ValueNumber;
import dev.evangelion.client.values.impl.ValueEnum;
import dev.evangelion.api.manager.module.RegisterModule;
import dev.evangelion.api.manager.module.Module;

@RegisterModule(name = "Step", description = "Makes you be able to walk up one or more blocks instantly.", category = Module.Category.MOVEMENT)
public class ModuleStep extends Module
{
    public static ModuleStep INSTANCE;
    private float oldHeight;
    private final ValueEnum mode;
    private final ValueNumber height;
    
    public ModuleStep() {
        this.oldHeight = -1.0f;
        this.mode = new ValueEnum("Mode", "Mode", "Mode for the step.", Modes.Vanilla);
        this.height = new ValueNumber("Height", "Height", "The height for the Step.", 2.0f, 0.5f, 5.0f);
        ModuleStep.INSTANCE = this;
    }
    
    @SubscribeEvent
    public void onStep(final EventStep event) {
        if (ModuleStep.mc.player.onGround && !ModuleStep.mc.player.isInsideOfMaterial(Material.WATER) && !ModuleStep.mc.player.isInsideOfMaterial(Material.LAVA) && ModuleStep.mc.player.collidedVertically && ModuleStep.mc.player.fallDistance == 0.0f && !ModuleStep.mc.gameSettings.keyBindJump.isKeyDown() && !ModuleStep.mc.player.isOnLadder()) {
            event.setHeight(this.height.getValue().floatValue());
            final double rheight = ModuleStep.mc.player.getEntityBoundingBox().minY - ModuleStep.mc.player.posY;
            if (rheight >= 0.625 && this.mode.getValue().equals(Modes.Normal)) {
                this.ncpStep(rheight);
            }
        }
        else {
            event.setHeight(0.6f);
        }
    }
    
    @Override
    public void onUpdate() {
        super.onUpdate();
        if (this.nullCheck()) {
            return;
        }
        if (this.mode.getValue().equals(Modes.Vanilla)) {
            ModuleStep.mc.player.stepHeight = this.height.getValue().floatValue();
        }
    }
    
    @Override
    public void onEnable() {
        super.onEnable();
        if (ModuleStep.mc.player == null || ModuleStep.mc.world == null) {
            return;
        }
        this.oldHeight = ModuleStep.mc.player.stepHeight;
    }
    
    @Override
    public void onDisable() {
        super.onDisable();
        ModuleStep.mc.player.stepHeight = this.oldHeight;
        this.oldHeight = -1.0f;
    }
    
    @Override
    public void onLogin() {
        super.onLogin();
        this.disable(false);
    }
    
    @Override
    public void onLogout() {
        super.onLogout();
        this.disable(false);
    }
    
    @Override
    public void onDeath() {
        super.onDeath();
        this.disable(true);
    }
    
    private void ncpStep(final double height) {
        final double posX = ModuleStep.mc.player.posX;
        final double posZ = ModuleStep.mc.player.posZ;
        double y = ModuleStep.mc.player.posY;
        if (height >= 1.1) {
            if (height < 1.6) {
                final double[] array;
                final double[] offset = array = new double[] { 0.42, 0.33, 0.24, 0.083, -0.078 };
                for (final double off : array) {
                    ModuleStep.mc.player.connection.sendPacket((Packet)new CPacketPlayer.Position(posX, y += off, posZ, false));
                }
            }
            else if (height < 2.1) {
                final double[] array2;
                final double[] heights = array2 = new double[] { 0.425, 0.821, 0.699, 0.599, 1.022, 1.372, 1.652, 1.869 };
                for (final double off : array2) {
                    ModuleStep.mc.player.connection.sendPacket((Packet)new CPacketPlayer.Position(posX, y + off, posZ, false));
                }
            }
            else {
                final double[] array3;
                final double[] heights = array3 = new double[] { 0.425, 0.821, 0.699, 0.599, 1.022, 1.372, 1.652, 1.869, 2.019, 1.907 };
                for (final double off : array3) {
                    ModuleStep.mc.player.connection.sendPacket((Packet)new CPacketPlayer.Position(posX, y + off, posZ, false));
                }
            }
        }
        else {
            double first = 0.42;
            double second = 0.75;
            if (height != 1.0) {
                first *= height;
                second *= height;
                if (first > 0.425) {
                    first = 0.425;
                }
                if (second > 0.78) {
                    second = 0.78;
                }
                if (second < 0.49) {
                    second = 0.49;
                }
            }
            ModuleStep.mc.player.connection.sendPacket((Packet)new CPacketPlayer.Position(posX, y + first, posZ, false));
            if (y + second < y + height) {
                ModuleStep.mc.player.connection.sendPacket((Packet)new CPacketPlayer.Position(posX, y + second, posZ, false));
            }
        }
    }
    
    @Override
    public String getHudInfo() {
        return this.mode.getValue().name();
    }
    
    public enum Modes
    {
        Vanilla, 
        Normal;
    }
}
