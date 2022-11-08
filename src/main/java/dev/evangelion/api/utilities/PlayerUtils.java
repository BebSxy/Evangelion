package dev.evangelion.api.utilities;

import net.minecraft.util.math.BlockPos;
import java.util.List;
import net.minecraft.client.model.ModelBiped;
import java.util.Iterator;
import dev.evangelion.Evangelion;
import net.minecraft.entity.Entity;
import java.util.Collection;
import java.util.ArrayList;
import net.minecraft.entity.player.EntityPlayer;

public class PlayerUtils implements IMinecraft
{
    public static EntityPlayer getTarget(final float range) {
        EntityPlayer optimalPlayer = null;
        for (final EntityPlayer player : new ArrayList<EntityPlayer>(PlayerUtils.mc.world.playerEntities)) {
            if (PlayerUtils.mc.player.getDistance((Entity)player) > range) {
                continue;
            }
            if (Evangelion.FRIEND_MANAGER.isFriend(player.getName())) {
                continue;
            }
            if (player.isDead) {
                continue;
            }
            if (player.getHealth() <= 0.0f && player == PlayerUtils.mc.player) {
                continue;
            }
            if (player.getName().equals(PlayerUtils.mc.player.getName())) {
                continue;
            }
            if (player.hurtTime != 0) {
                continue;
            }
            if (optimalPlayer == null) {
                optimalPlayer = player;
            }
            else {
                if (PlayerUtils.mc.player.getDistanceSq((Entity)player) >= PlayerUtils.mc.player.getDistanceSq((Entity)optimalPlayer)) {
                    continue;
                }
                optimalPlayer = player;
            }
        }
        return optimalPlayer;
    }

    public static float[][] getBipedRotations(final ModelBiped biped) {
        final float[][] rotations = new float[5][];
        final float[] headRotation = { biped.bipedHead.rotateAngleX, biped.bipedHead.rotateAngleY, biped.bipedHead.rotateAngleZ };
        rotations[0] = headRotation;
        final float[] rightArmRotation = { biped.bipedRightArm.rotateAngleX, biped.bipedRightArm.rotateAngleY, biped.bipedRightArm.rotateAngleZ };
        rotations[1] = rightArmRotation;
        final float[] leftArmRotation = { biped.bipedLeftArm.rotateAngleX, biped.bipedLeftArm.rotateAngleY, biped.bipedLeftArm.rotateAngleZ };
        rotations[2] = leftArmRotation;
        final float[] rightLegRotation = { biped.bipedRightLeg.rotateAngleX, biped.bipedRightLeg.rotateAngleY, biped.bipedRightLeg.rotateAngleZ };
        rotations[3] = rightLegRotation;
        final float[] leftLegRotation = { biped.bipedLeftLeg.rotateAngleX, biped.bipedLeftLeg.rotateAngleY, biped.bipedLeftLeg.rotateAngleZ };
        rotations[4] = leftLegRotation;
        return rotations;
    }

    public static List<BlockPos> getSphere(final float range, final boolean sphere, final boolean hollow) {
        final List<BlockPos> blocks = new ArrayList<BlockPos>();
        for (int x = PlayerUtils.mc.player.getPosition().getX() - (int)range; x <= PlayerUtils.mc.player.getPosition().getX() + range; ++x) {
            for (int z = PlayerUtils.mc.player.getPosition().getZ() - (int)range; z <= PlayerUtils.mc.player.getPosition().getZ() + range; ++z) {
                for (int y = sphere ? (PlayerUtils.mc.player.getPosition().getY() - (int)range) : PlayerUtils.mc.player.getPosition().getY(); y < PlayerUtils.mc.player.getPosition().getY() + range; ++y) {
                    final double distance = (PlayerUtils.mc.player.getPosition().getX() - x) * (PlayerUtils.mc.player.getPosition().getX() - x) + (PlayerUtils.mc.player.getPosition().getZ() - z) * (PlayerUtils.mc.player.getPosition().getZ() - z) + (sphere ? ((PlayerUtils.mc.player.getPosition().getY() - y) * (PlayerUtils.mc.player.getPosition().getY() - y)) : 0);
                    if (distance < range * range && (!hollow || distance >= (range - 1.0) * (range - 1.0))) {
                        blocks.add(new BlockPos(x, y, z));
                    }
                }
            }
        }
        return blocks;
    }
}
