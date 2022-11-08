package dev.evangelion.client.modules.player;

import net.minecraft.init.Blocks;
import dev.evangelion.api.utilities.MathUtils;
import net.minecraft.util.math.MathHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameType;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import com.mojang.authlib.GameProfile;
import java.util.UUID;
import dev.evangelion.api.utilities.TimerUtils;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import dev.evangelion.client.values.impl.ValueCategory;
import dev.evangelion.client.values.impl.ValueEnum;
import dev.evangelion.client.values.impl.ValueNumber;
import dev.evangelion.client.values.impl.ValueBoolean;
import dev.evangelion.client.values.impl.ValueString;
import dev.evangelion.api.manager.module.RegisterModule;
import dev.evangelion.api.manager.module.Module;

@RegisterModule(name = "FakePlayer", tag = "Fake Player", description = "Spawns a fake player for you to test features on.", category = Module.Category.PLAYER)
public class ModuleFakePlayer extends Module
{
    private final ValueString name;
    private final ValueBoolean copyInv;
    private final ValueNumber health;
    private final ValueNumber absorption;
    private final ValueEnum movementMode;
    private final ValueCategory movementCategory;
    private final ValueNumber velocity;
    private final ValueNumber directionTimeout;
    private EntityOtherPlayerMP player;
    private double[] direction;
    private boolean changeDirection;
    private TimerUtils timer;
    private TimerUtils stepTimer;
    private boolean stepping;
    
    public ModuleFakePlayer() {
        this.name = new ValueString("Name", "Name", "The name of the player.", "Dummy");
        this.copyInv = new ValueBoolean("CopyInventry", "Copy Inventory", "Copies the inventory of your player onto the fake player.", true);
        this.health = new ValueNumber("Health", "Health", "The health of the player.", 20, 0, 20);
        this.absorption = new ValueNumber("Absorption", "Absorption", "The absorption amount of the player.", 16, 0, 16);
        this.movementMode = new ValueEnum("MovementMode", "Mode", "The mode for the fake player movement.", MovingModes.None);
        this.movementCategory = new ValueCategory("MovementCategory", "Movement category.");
        this.velocity = new ValueNumber("Velocity", "Velocity", "The velocity to apply on the fake player.", this.movementCategory, 0.3f, 0.1f, 0.4f);
        this.directionTimeout = new ValueNumber("DirectionTimeout", "Direction Timeout", "The time to reach before changing direction.", this.movementCategory, 1.0, 0.1, 2.0);
        this.player = null;
        this.direction = this.generateDirection();
        this.changeDirection = false;
        this.timer = new TimerUtils();
        this.stepTimer = new TimerUtils();
        this.stepping = false;
    }
    
    @Override
    public void onEnable() {
        super.onEnable();
        if (ModuleFakePlayer.mc.world == null) {
            this.disable(false);
            return;
        }
        (this.player = new EntityOtherPlayerMP((World)ModuleFakePlayer.mc.world, new GameProfile(UUID.randomUUID(), this.name.getValue()))).copyLocationAndAnglesFrom((Entity)ModuleFakePlayer.mc.player);
        this.player.rotationYawHead = ModuleFakePlayer.mc.player.rotationYawHead;
        this.player.rotationYaw = ModuleFakePlayer.mc.player.rotationYaw;
        this.player.rotationPitch = ModuleFakePlayer.mc.player.rotationPitch;
        this.player.setGameType(GameType.SURVIVAL);
        this.player.setHealth((float)this.health.getValue().intValue());
        this.player.setAbsorptionAmount((float)this.absorption.getValue().intValue());
        ModuleFakePlayer.mc.world.addEntityToWorld(-3421341, (Entity)this.player);
        if (this.copyInv.getValue()) {
            this.player.inventory.copyInventory(ModuleFakePlayer.mc.player.inventory);
        }
        this.player.onLivingUpdate();
        this.timer.reset();
    }
    
    @Override
    public void onUpdate() {
        super.onUpdate();
        if (this.nullCheck()) {
            return;
        }
        if (this.player == null) {
            return;
        }
        if (this.movementMode.getValue().equals(MovingModes.Random)) {
            final BlockPos pos = new BlockPos(this.player.posX, this.player.posY, this.player.posZ);
            if (this.timer.hasTimeElapsed((long)(this.directionTimeout.getValue().doubleValue() * 1000.0))) {
                this.changeDirection = true;
                this.timer.reset();
            }
            if (this.hasObstruction(pos)) {
                for (final EnumFacing facing : EnumFacing.HORIZONTALS) {
                    final BlockPos offsetPos = pos.offset(facing);
                    if ((ModuleFakePlayer.mc.world.getBlockState(offsetPos).getMaterial().blocksMovement() || ModuleFakePlayer.mc.world.getBlockState(offsetPos.up()).getMaterial().blocksMovement()) && ModuleFakePlayer.mc.world.getBlockState(offsetPos.up().up()).getMaterial().blocksMovement() && this.player.getHorizontalFacing().equals((Object)facing)) {
                        this.changeDirection = true;
                        this.timer.reset();
                    }
                }
            }
            if (this.changeDirection) {
                double[] newDirection = this.generateDirection();
                if (this.direction == newDirection) {
                    newDirection = this.generateDirection();
                }
                this.direction = newDirection;
                this.changeDirection = false;
            }
            if (this.stepping && this.stepTimer.hasTimeElapsed(500L)) {
                this.stepping = false;
                this.stepTimer.reset();
            }
            this.player.jump();
            final float[] rotations = getRotations(this.player.posX + this.direction[0], this.player.posY + this.fixAxisY((EntityPlayer)this.player), this.player.posZ + this.direction[1], (EntityPlayer)this.player);
            this.player.rotationYaw = rotations[0];
            this.player.rotationYawHead = rotations[0];
            this.player.rotationPitch = 0.0f;
            this.player.setPosition(this.player.posX + this.direction[0], this.player.posY + this.fixAxisY((EntityPlayer)this.player), this.player.posZ + this.direction[1]);
            this.player.prevLimbSwingAmount = this.player.limbSwingAmount;
            final double d1 = this.player.posX - this.player.prevPosX;
            final double d2 = this.player.posZ - this.player.prevPosZ;
            float f2 = MathHelper.sqrt(d1 * d1 + d2 * d2) * 4.0f;
            if (f2 > 1.0f) {
                f2 = 1.0f;
            }
            final EntityOtherPlayerMP player = this.player;
            player.limbSwingAmount += (f2 - this.player.limbSwingAmount) * 0.4f;
            final EntityOtherPlayerMP player2 = this.player;
            player2.limbSwing += this.player.limbSwingAmount;
        }
    }
    
    @Override
    public void onDisable() {
        if (ModuleFakePlayer.mc.world != null) {
            ModuleFakePlayer.mc.world.removeEntityFromWorld(-3421341);
        }
    }
    
    public double[] generateDirection() {
        final double angle = MathUtils.randomNumber(6.283185307179586, 0.0);
        final double[] direction = { Math.cos(angle), Math.sin(angle) };
        final double x = direction[0] * this.velocity.getValue().floatValue();
        final double z = direction[1] * this.velocity.getValue().floatValue();
        return new double[] { x, z };
    }
    
    public float fixAxisY(final EntityPlayer player) {
        final BlockPos pos = new BlockPos(player.posX, player.posY, player.posZ);
        if (ModuleFakePlayer.mc.world.getBlockState(pos.down()).getBlock() == Blocks.AIR && !this.stepping) {
            return -1.0f;
        }
        if (this.hasObstruction(pos)) {
            for (final EnumFacing facing : EnumFacing.HORIZONTALS) {
                final BlockPos offsetPos = pos.offset(facing);
                if (ModuleFakePlayer.mc.world.getBlockState(offsetPos).getMaterial().blocksMovement() && !ModuleFakePlayer.mc.world.getBlockState(offsetPos.up()).getMaterial().blocksMovement() && player.getHorizontalFacing().equals((Object)facing) && !ModuleFakePlayer.mc.world.getBlockState(offsetPos.up().up()).getMaterial().blocksMovement()) {
                    this.stepping = true;
                    this.stepTimer.reset();
                    return 1.0f;
                }
                if (ModuleFakePlayer.mc.world.getBlockState(offsetPos.up()).getMaterial().blocksMovement() && player.getHorizontalFacing().equals((Object)facing) && !ModuleFakePlayer.mc.world.getBlockState(offsetPos.up().up()).getMaterial().blocksMovement()) {
                    this.stepping = true;
                    this.stepTimer.reset();
                    return 2.0f;
                }
            }
            return 0.0f;
        }
        return 0.0f;
    }
    
    public boolean hasObstruction(final BlockPos pos) {
        for (final EnumFacing facing : EnumFacing.HORIZONTALS) {
            final BlockPos offsetPos = pos.offset(facing);
            if (ModuleFakePlayer.mc.world.getBlockState(offsetPos).getMaterial().blocksMovement()) {
                return true;
            }
            if (ModuleFakePlayer.mc.world.getBlockState(offsetPos.up()).getMaterial().blocksMovement()) {
                return true;
            }
        }
        return false;
    }
    
    public static float[] getRotations(final double posX, final double posY, final double posZ, final EntityPlayer player) {
        final double x = posX - player.posX;
        final double y = posY - (player.posY + player.getEyeHeight());
        final double z = posZ - player.posZ;
        final double dist = MathHelper.sqrt(x * x + z * z);
        final float yaw = (float)(Math.atan2(z, x) * 180.0 / 3.141592653589793) - 90.0f;
        final float pitch = (float)(-(Math.atan2(y, dist) * 180.0 / 3.141592653589793));
        return new float[] { yaw, pitch };
    }
    
    public enum MovingModes
    {
        Random, 
        None;
    }
}
