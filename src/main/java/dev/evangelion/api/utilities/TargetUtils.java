/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraft.entity.player.EntityPlayer
 */
package dev.evangelion.api.utilities;

import dev.evangelion.Evangelion;
import dev.evangelion.api.utilities.IMinecraft;
import dev.evangelion.api.utilities.MathUtils;
import java.util.ArrayList;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;

public class TargetUtils
implements IMinecraft {
    public static EntityLivingBase getTarget(final float range, final float wallRange, final boolean visible, final TargetMode targetMode) {
        EntityLivingBase targetEntity = null;
        for (final Entity e : new ArrayList<Entity>(TargetUtils.mc.world.loadedEntityList)) {
            if (!(e instanceof EntityLivingBase)) {
                continue;
            }
            final EntityLivingBase entity = (EntityLivingBase)e;
            if (TargetUtils.mc.player.canEntityBeSeen((Entity)entity)) {
                if (TargetUtils.mc.player.getDistance(entity.posX, entity.posY, entity.posZ) > range) {
                    continue;
                }
            }
            else if (TargetUtils.mc.player.getDistance(entity.posX, entity.posY, entity.posZ) > wallRange) {
                continue;
            }
            if (Evangelion.FRIEND_MANAGER.isFriend(e.getName())) {
                continue;
            }
            if (entity == TargetUtils.mc.player && entity.getName().equals(TargetUtils.mc.player.getName())) {
                continue;
            }
            if (!(entity instanceof EntityPlayer)) {
                continue;
            }
            if ((entity.isDead || entity.getHealth() <= 0.0f) && !TargetUtils.mc.player.canEntityBeSeen((Entity)entity) && visible) {
                continue;
            }
            if (((EntityPlayer)entity).capabilities.isCreativeMode) {
                continue;
            }
            if (targetEntity == null) {
                targetEntity = entity;
            }
            else if (targetMode == TargetMode.Range) {
                if (TargetUtils.mc.player.getDistance(entity.posX, entity.posY, entity.posZ) >= TargetUtils.mc.player.getDistance(targetEntity.posX, targetEntity.posY, targetEntity.posZ)) {
                    continue;
                }
                targetEntity = entity;
            }
            else {
                if (entity.getHealth() + entity.getAbsorptionAmount() >= targetEntity.getHealth() + targetEntity.getAbsorptionAmount()) {
                    continue;
                }
                targetEntity = entity;
            }
        }
        return targetEntity;
    }

    public static EntityPlayer getTarget(final float range) {
        EntityPlayer targetPlayer = null;
        for (final EntityPlayer player : new ArrayList<EntityPlayer>(TargetUtils.mc.world.playerEntities)) {
            if (TargetUtils.mc.player.getDistanceSq((Entity)player) > MathUtils.square(range)) {
                continue;
            }
            if (player == TargetUtils.mc.player) {
                continue;
            }
            if (Evangelion.FRIEND_MANAGER.isFriend(player.getName())) {
                continue;
            }
            if (player.isDead) {
                continue;
            }
            if (player.getHealth() <= 0.0f) {
                continue;
            }
            if (targetPlayer == null) {
                targetPlayer = player;
            }
            else {
                if (TargetUtils.mc.player.getDistanceSq((Entity)player) >= TargetUtils.mc.player.getDistanceSq((Entity)targetPlayer)) {
                    continue;
                }
                targetPlayer = player;
            }
        }
        return targetPlayer;
    }

    public static enum TargetMode {
        Range,
        Health;

    }
}

