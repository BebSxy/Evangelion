package dev.evangelion.client.modules.movement;

import net.minecraft.util.math.BlockPos;
import net.minecraft.client.entity.EntityPlayerSP;
import dev.evangelion.api.utilities.HoleUtils;
import net.minecraft.util.math.Vec3d;
import dev.evangelion.client.values.impl.ValueBoolean;
import dev.evangelion.client.values.impl.ValueNumber;
import dev.evangelion.api.manager.module.RegisterModule;
import dev.evangelion.api.manager.module.Module;

@RegisterModule(name = "Anchor", description = "Drag you into holes.", category = Module.Category.MOVEMENT)
public class ModuleAnchor extends Module
{
    ValueNumber height;
    ValueBoolean doubles;
    ValueBoolean drop;
    ValueNumber speed;
    ValueBoolean pitchD;
    ValueNumber pitch;
    private Vec3d Center;
    
    public ModuleAnchor() {
        this.height = new ValueNumber("Height", "Height", "", 3, 1, 5);
        this.doubles = new ValueBoolean("Doubles", "Doubles", "", false);
        this.drop = new ValueBoolean("Drop", "Drop", "", false);
        this.speed = new ValueNumber("DropSpeed", "DropSpeed", "", 1.0, 0.1, 5.0);
        this.pitchD = new ValueBoolean("PitchDepend", "PitchDepend", "", false);
        this.pitch = new ValueNumber("Pitch", "Pitch", "", 60, 0, 90);
        this.Center = Vec3d.ZERO;
    }
    
    @Override
    public void onUpdate() {
        super.onUpdate();
        if (ModuleAnchor.mc.world == null || ModuleAnchor.mc.player == null || ModuleAnchor.mc.player.isInWater() || ModuleAnchor.mc.player.isInLava() || ModuleAnchor.mc.player.isOnLadder()) {
            return;
        }
        for (int i = 0; i < this.height.getValue().intValue(); ++i) {
            if (this.pitchD.getValue() && ModuleAnchor.mc.player.rotationPitch < this.pitch.getValue().intValue()) {
                return;
            }
            if (HoleUtils.isHole(this.getPlayerPos().down(i)) || (HoleUtils.isDoubleHole(this.getPlayerPos().down(i)) && this.doubles.getValue())) {
                this.Center = this.getCenter(ModuleAnchor.mc.player.posX, ModuleAnchor.mc.player.posY, ModuleAnchor.mc.player.posZ);
                final double XDiff = Math.abs(this.Center.x - ModuleAnchor.mc.player.posX);
                final double ZDiff = Math.abs(this.Center.z - ModuleAnchor.mc.player.posZ);
                if (XDiff <= 0.1 && ZDiff <= 0.1) {
                    this.Center = Vec3d.ZERO;
                }
                else {
                    final double MotionX = this.Center.x - ModuleAnchor.mc.player.posX;
                    final double MotionZ = this.Center.z - ModuleAnchor.mc.player.posZ;
                    ModuleAnchor.mc.player.motionX = MotionX / 2.0;
                    ModuleAnchor.mc.player.motionZ = MotionZ / 2.0;
                    if (this.drop.getValue()) {
                        final EntityPlayerSP player = ModuleAnchor.mc.player;
                        player.motionY -= this.speed.getValue().floatValue();
                    }
                }
            }
        }
    }
    
    public Vec3d getCenter(final double posX, final double posY, final double posZ) {
        final double x = Math.floor(posX) + 0.5;
        final double y = Math.floor(posY);
        final double z = Math.floor(posZ) + 0.5;
        return new Vec3d(x, y, z);
    }
    
    public BlockPos getPlayerPos() {
        return new BlockPos(Math.floor(ModuleAnchor.mc.player.posX), Math.floor(ModuleAnchor.mc.player.posY), Math.floor(ModuleAnchor.mc.player.posZ));
    }
}
