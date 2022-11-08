/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.item.EntityEnderCrystal
 *  net.minecraft.entity.item.EntityItem
 *  net.minecraft.entity.item.EntityXPOrb
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.init.Blocks
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.CPacketAnimation
 *  net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock
 *  net.minecraft.util.EnumFacing
 *  net.minecraft.util.EnumHand
 *  net.minecraft.util.math.AxisAlignedBB
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.util.math.Vec3i
 *  net.minecraft.world.IBlockAccess
 *  net.minecraft.world.World
 */
package dev.evangelion.api.utilities;

import dev.evangelion.Evangelion;
import dev.evangelion.api.utilities.IMinecraft;
import dev.evangelion.api.utilities.MathUtils;
import dev.evangelion.api.utilities.RotationUtils;
import dev.evangelion.client.events.EventMotion;
import dev.evangelion.client.modules.client.ModuleRotations;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketAnimation;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockUtils
implements IMinecraft {
    public static void placeBlock(EventMotion event, BlockPos position, EnumHand hand) {
        if (!BlockUtils.mc.world.getBlockState(position).getBlock().isReplaceable((IBlockAccess)BlockUtils.mc.world, position)) {
            return;
        }
        if (BlockUtils.getPlaceableSide(position) == null) {
            return;
        }
        if (Evangelion.MODULE_MANAGER.isModuleEnabled("Rotations")) {
            float[] rot = RotationUtils.getSmoothRotations(RotationUtils.getRotations(position.getX(), position.getY(), position.getZ()), ModuleRotations.INSTANCE.smoothness.getValue().intValue());
            RotationUtils.rotate(event, rot);
        }
        BlockUtils.mc.player.connection.sendPacket((Packet)new CPacketPlayerTryUseItemOnBlock(position.offset(Objects.requireNonNull(BlockUtils.getPlaceableSide(position))), Objects.requireNonNull(BlockUtils.getPlaceableSide(position)).getOpposite(), hand, 0.5f, 0.5f, 0.5f));
        BlockUtils.mc.player.connection.sendPacket((Packet)new CPacketAnimation(hand));
    }

    public static boolean isPositionPlaceable(BlockPos position, boolean entityCheck, boolean sideCheck) {
        if (!BlockUtils.mc.world.getBlockState(position).getBlock().isReplaceable((IBlockAccess)BlockUtils.mc.world, position)) {
            return false;
        }
        if (entityCheck) {
            for (Entity entity : BlockUtils.mc.world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(position))) {
                if (entity instanceof EntityItem || entity instanceof EntityXPOrb) continue;
                return false;
            }
        }
        if (sideCheck) {
            return BlockUtils.getPlaceableSide(position) != null;
        }
        return true;
    }

    public static boolean isPositionPlaceable(BlockPos position, boolean entityCheck, boolean sideCheck, boolean ignoreCrystals) {
        if (!BlockUtils.mc.world.getBlockState(position).getBlock().isReplaceable((IBlockAccess)BlockUtils.mc.world, position)) {
            return false;
        }
        if (entityCheck) {
            for (Entity entity : BlockUtils.mc.world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(position))) {
                if (entity instanceof EntityItem || entity instanceof EntityXPOrb || entity instanceof EntityEnderCrystal && ignoreCrystals) continue;
                return false;
            }
        }
        if (sideCheck) {
            return BlockUtils.getPlaceableSide(position) != null;
        }
        return true;
    }

    public static EnumFacing getPlaceableSide(BlockPos position) {
        for (EnumFacing side : EnumFacing.values()) {
            if (!BlockUtils.mc.world.getBlockState(position.offset(side)).getBlock().canCollideCheck(BlockUtils.mc.world.getBlockState(position.offset(side)), false) || BlockUtils.mc.world.getBlockState(position.offset(side)).getMaterial().isReplaceable()) continue;
            return side;
        }
        return null;
    }

    public static List<BlockPos> getNearbyBlocks(EntityPlayer player, double blockRange, boolean motion) {
        ArrayList<BlockPos> nearbyBlocks = new ArrayList<BlockPos>();
        int range = (int)MathUtils.roundToPlaces(blockRange, 0);
        if (motion) {
            player.getPosition().add(new Vec3i(player.motionX, player.motionY, player.motionZ));
        }
        for (int x = -range; x <= range; ++x) {
            for (int y = -range; y <= range - range / 2; ++y) {
                for (int z = -range; z <= range; ++z) {
                    nearbyBlocks.add(player.getPosition().add(x, y, z));
                }
            }
        }
        return nearbyBlocks;
    }

    public static BlockResistance getBlockResistance(BlockPos block) {
        if (BlockUtils.mc.world.isAirBlock(block)) {
            return BlockResistance.Blank;
        }
        if (!(BlockUtils.mc.world.getBlockState(block).getBlock().getBlockHardness(BlockUtils.mc.world.getBlockState(block), (World)BlockUtils.mc.world, block) == -1.0f || BlockUtils.mc.world.getBlockState(block).getBlock().equals((Object)Blocks.OBSIDIAN) || BlockUtils.mc.world.getBlockState(block).getBlock().equals((Object)Blocks.ANVIL) || BlockUtils.mc.world.getBlockState(block).getBlock().equals((Object)Blocks.ENCHANTING_TABLE) || BlockUtils.mc.world.getBlockState(block).getBlock().equals((Object)Blocks.ENDER_CHEST))) {
            return BlockResistance.Breakable;
        }
        if (BlockUtils.mc.world.getBlockState(block).getBlock().equals((Object)Blocks.OBSIDIAN) || BlockUtils.mc.world.getBlockState(block).getBlock().equals((Object)Blocks.ANVIL) || BlockUtils.mc.world.getBlockState(block).getBlock().equals((Object)Blocks.ENCHANTING_TABLE) || BlockUtils.mc.world.getBlockState(block).getBlock().equals((Object)Blocks.ENDER_CHEST)) {
            return BlockResistance.Resistant;
        }
        if (BlockUtils.mc.world.getBlockState(block).getBlock().equals((Object)Blocks.BEDROCK)) {
            return BlockResistance.Unbreakable;
        }
        return null;
    }

    public static boolean isIntercepted(BlockPos blockPos) {
        for (Entity entity : BlockUtils.mc.world.loadedEntityList) {
            if (entity instanceof EntityItem || entity instanceof EntityEnderCrystal || !new AxisAlignedBB(blockPos).intersects(entity.getEntityBoundingBox())) continue;
            return true;
        }
        return false;
    }

    public static enum BlockResistance {
        Blank,
        Breakable,
        Resistant,
        Unbreakable;

    }
}

