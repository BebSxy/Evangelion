/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.init.MobEffects
 *  net.minecraft.util.math.AxisAlignedBB
 *  net.minecraft.util.math.BlockPos
 */
package dev.evangelion.api.utilities;

import dev.evangelion.api.utilities.IMinecraft;
import dev.evangelion.client.events.EventPlayerMove;
import java.util.Objects;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;

public class MovementUtils
implements IMinecraft {
    public static boolean isMoving(EntityPlayer player) {
        return player.moveStrafing != 0.0f || player.moveForward != 0.0f;
    }

    public static AxisAlignedBB predictMovement(EntityPlayer player, float seconds) {
        AxisAlignedBB bb = player.boundingBox;
        double x = (player.posX - player.prevPosX) / (double)(MovementUtils.mc.timer.tickLength / 100.0f);
        double y = (player.posY - player.prevPosY) / (double)(MovementUtils.mc.timer.tickLength / 100.0f);
        double z = (player.posZ - player.prevPosZ) / (double)(MovementUtils.mc.timer.tickLength / 100.0f);
        if (!MovementUtils.isMoving(player)) {
            return player.boundingBox;
        }
        if (MovementUtils.mc.world.getCollisionBoxes((Entity)player, bb.expand(x, 0.0, 0.0)).size() > 0) {
            x = 0.0;
        }
        if (MovementUtils.mc.world.getCollisionBoxes((Entity)player, bb.expand(0.0, y, 0.0)).size() > 0) {
            y = 0.0;
        }
        if (MovementUtils.mc.world.getCollisionBoxes((Entity)player, bb.expand(0.0, 0.0, z)).size() > 0) {
            z = 0.0;
        }
        AxisAlignedBB predictedBB = new AxisAlignedBB(bb.minX + x * (double)seconds, bb.minY + y * (double)seconds, bb.minZ + z * (double)seconds, bb.maxX + x * (double)seconds, bb.maxY + y * (double)seconds, bb.maxZ + z * (double)seconds);
        return predictedBB;
    }

    public static double getBaseMoveSpeed(double speed) {
        double baseSpeed = speed;
        if (MovementUtils.mc.player != null && MovementUtils.mc.player.isPotionActive(MobEffects.SPEED)) {
            int amplifier = Objects.requireNonNull(MovementUtils.mc.player.getActivePotionEffect(MobEffects.SPEED)).getAmplifier();
            baseSpeed *= 1.0 + 0.2 * (double)(amplifier + 1);
        }
        return baseSpeed;
    }

    public static double getJumpBoost() {
        double defaultSpeed = 0.0;
        if (MovementUtils.mc.player.isPotionActive(MobEffects.JUMP_BOOST)) {
            int amplifier = MovementUtils.mc.player.getActivePotionEffect(MobEffects.JUMP_BOOST).getAmplifier();
            defaultSpeed += (double)(amplifier + 1) * 0.1;
        }
        return defaultSpeed;
    }

    public static void strafe(EventPlayerMove event, double speed) {
        float moveForward = MovementUtils.mc.player.movementInput.moveForward;
        float moveStrafe = MovementUtils.mc.player.movementInput.moveStrafe;
        float rotationYaw = MovementUtils.mc.player.rotationYaw;
        if (moveForward == 0.0f && moveStrafe == 0.0f) {
            event.setX(0.0);
            event.setZ(0.0);
        } else if (moveForward != 0.0f) {
            if (moveStrafe >= 1.0f) {
                rotationYaw += moveForward > 0.0f ? -45.0f : 45.0f;
                moveStrafe = 0.0f;
            } else if (moveStrafe <= -1.0f) {
                rotationYaw += moveForward > 0.0f ? 45.0f : -45.0f;
                moveStrafe = 0.0f;
            }
            if (moveForward > 0.0f) {
                moveForward = 1.0f;
            } else if (moveForward < 0.0f) {
                moveForward = -1.0f;
            }
        }
        double motionX = Math.cos(Math.toRadians(rotationYaw + 90.0f));
        double motionZ = Math.sin(Math.toRadians(rotationYaw + 90.0f));
        event.setX((double)moveForward * speed * motionX + (double)moveStrafe * speed * motionZ);
        event.setZ((double)moveForward * speed * motionZ - (double)moveStrafe * speed * motionX);
        if (moveForward == 0.0f && moveStrafe == 0.0f) {
            event.setX(0.0);
            event.setZ(0.0);
        }
    }

    public static boolean isMovingTowards(EntityPlayer player, double posX, double posY, double posZ) {
        double x = (player.posX - player.prevPosX) / (double)(MovementUtils.mc.timer.tickLength / 100.0f);
        double y = (player.posY - player.prevPosY) / (double)(MovementUtils.mc.timer.tickLength / 100.0f);
        double z = (player.posZ - player.prevPosZ) / (double)(MovementUtils.mc.timer.tickLength / 100.0f);
        BlockPos playerPos = new BlockPos(MovementUtils.mc.player.posX, MovementUtils.mc.player.posY, MovementUtils.mc.player.posZ);
        BlockPos motionPos = new BlockPos(MovementUtils.mc.player.posX + x * 4.0, MovementUtils.mc.player.posY + x * 4.0, MovementUtils.mc.player.posZ + x * 4.0);
        return motionPos.distanceSq(posX, posY, posZ) < playerPos.distanceSq(posX, posY, posZ);
    }
}

