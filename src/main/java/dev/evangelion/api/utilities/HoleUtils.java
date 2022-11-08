/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.state.IBlockState
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.init.Blocks
 *  net.minecraft.util.EnumFacing
 *  net.minecraft.util.math.BlockPos
 */
package dev.evangelion.api.utilities;

import dev.evangelion.api.utilities.BlockUtils;
import dev.evangelion.api.utilities.IMinecraft;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

public class HoleUtils
implements IMinecraft {
    public static boolean isBedrockHole(BlockPos pos) {
        boolean retVal = false;
        if (HoleUtils.mc.world.getBlockState(pos).getBlock().equals((Object)Blocks.AIR) && HoleUtils.mc.world.getBlockState(pos.up()).getBlock().equals((Object)Blocks.AIR) && HoleUtils.mc.world.getBlockState(pos.up().up()).getBlock().equals((Object)Blocks.AIR) && HoleUtils.mc.world.getBlockState(pos.down()).getBlock().equals((Object)Blocks.BEDROCK) && HoleUtils.mc.world.getBlockState(pos.east()).getBlock().equals((Object)Blocks.BEDROCK) && HoleUtils.mc.world.getBlockState(pos.west()).getBlock().equals((Object)Blocks.BEDROCK) && HoleUtils.mc.world.getBlockState(pos.south()).getBlock().equals((Object)Blocks.BEDROCK) && HoleUtils.mc.world.getBlockState(pos.north()).getBlock().equals((Object)Blocks.BEDROCK)) {
            retVal = true;
        }
        return retVal;
    }

    public static boolean isObiHole(BlockPos pos) {
        boolean retVal = false;
        int obiCount = 0;
        if (HoleUtils.mc.world.getBlockState(pos).getBlock().equals((Object)Blocks.AIR) && HoleUtils.mc.world.getBlockState(pos.up()).getBlock().equals((Object)Blocks.AIR) && HoleUtils.mc.world.getBlockState(pos.up().up()).getBlock().equals((Object)Blocks.AIR) && (HoleUtils.mc.world.getBlockState(pos.down()).getBlock().equals((Object)Blocks.BEDROCK) || HoleUtils.mc.world.getBlockState(pos.down()).getBlock().equals((Object)Blocks.OBSIDIAN))) {
            if (HoleUtils.mc.world.getBlockState(pos.down()).getBlock().equals((Object)Blocks.OBSIDIAN)) {
                ++obiCount;
            }
            if (HoleUtils.mc.world.getBlockState(pos.east()).getBlock().equals((Object)Blocks.BEDROCK) || HoleUtils.mc.world.getBlockState(pos.east()).getBlock().equals((Object)Blocks.OBSIDIAN)) {
                if (HoleUtils.mc.world.getBlockState(pos.east()).getBlock().equals((Object)Blocks.OBSIDIAN)) {
                    ++obiCount;
                }
                if (HoleUtils.mc.world.getBlockState(pos.west()).getBlock().equals((Object)Blocks.BEDROCK) || HoleUtils.mc.world.getBlockState(pos.west()).getBlock().equals((Object)Blocks.OBSIDIAN)) {
                    if (HoleUtils.mc.world.getBlockState(pos.west()).getBlock().equals((Object)Blocks.OBSIDIAN)) {
                        ++obiCount;
                    }
                    if (HoleUtils.mc.world.getBlockState(pos.south()).getBlock().equals((Object)Blocks.BEDROCK) || HoleUtils.mc.world.getBlockState(pos.south()).getBlock().equals((Object)Blocks.OBSIDIAN)) {
                        if (HoleUtils.mc.world.getBlockState(pos.south()).getBlock().equals((Object)Blocks.OBSIDIAN)) {
                            ++obiCount;
                        }
                        if (HoleUtils.mc.world.getBlockState(pos.north()).getBlock().equals((Object)Blocks.BEDROCK) || HoleUtils.mc.world.getBlockState(pos.north()).getBlock().equals((Object)Blocks.OBSIDIAN)) {
                            if (HoleUtils.mc.world.getBlockState(pos.north()).getBlock().equals((Object)Blocks.OBSIDIAN)) {
                                ++obiCount;
                            }
                            if (obiCount >= 1) {
                                retVal = true;
                            }
                        }
                    }
                }
            }
        }
        return retVal;
    }

    public static boolean isDoubleHole(BlockPos pos) {
        for (EnumFacing f : EnumFacing.HORIZONTALS) {
            int offZ;
            int offX = f.getXOffset();
            if (HoleUtils.mc.world.getBlockState(pos.add(offX, 0, offZ = f.getZOffset())).getBlock() != Blocks.OBSIDIAN && HoleUtils.mc.world.getBlockState(pos.add(offX, 0, offZ)).getBlock() != Blocks.BEDROCK || HoleUtils.mc.world.getBlockState(pos.add(offX * -2, 0, offZ * -2)).getBlock() != Blocks.OBSIDIAN && HoleUtils.mc.world.getBlockState(pos.add(offX * -2, 0, offZ * -2)).getBlock() != Blocks.BEDROCK || HoleUtils.mc.world.getBlockState(pos.add(offX * -1, 0, offZ * -1)).getBlock() != Blocks.AIR || !HoleUtils.isSafeBlock(pos.add(0, -1, 0)) || !HoleUtils.isSafeBlock(pos.add(offX * -1, -1, offZ * -1))) continue;
            if (offZ == 0 && HoleUtils.isSafeBlock(pos.add(0, 0, 1)) && HoleUtils.isSafeBlock(pos.add(0, 0, -1)) && HoleUtils.isSafeBlock(pos.add(offX * -1, 0, 1)) && HoleUtils.isSafeBlock(pos.add(offX * -1, 0, -1))) {
                return true;
            }
            if (offX != 0 || !HoleUtils.isSafeBlock(pos.add(1, 0, 0)) || !HoleUtils.isSafeBlock(pos.add(-1, 0, 0)) || !HoleUtils.isSafeBlock(pos.add(1, 0, offZ * -1)) || !HoleUtils.isSafeBlock(pos.add(-1, 0, offZ * -1))) continue;
            return true;
        }
        return false;
    }

    public static boolean isHole(BlockPos pos) {
        boolean retVal = false;
        if (HoleUtils.mc.world.getBlockState(pos).getBlock().equals((Object)Blocks.AIR) && HoleUtils.mc.world.getBlockState(pos.up()).getBlock().equals((Object)Blocks.AIR) && HoleUtils.mc.world.getBlockState(pos.up().up()).getBlock().equals((Object)Blocks.AIR) && (HoleUtils.mc.world.getBlockState(pos.down()).getBlock().equals((Object)Blocks.BEDROCK) || HoleUtils.mc.world.getBlockState(pos.down()).getBlock().equals((Object)Blocks.OBSIDIAN)) && (HoleUtils.mc.world.getBlockState(pos.east()).getBlock().equals((Object)Blocks.BEDROCK) || HoleUtils.mc.world.getBlockState(pos.east()).getBlock().equals((Object)Blocks.OBSIDIAN)) && (HoleUtils.mc.world.getBlockState(pos.west()).getBlock().equals((Object)Blocks.BEDROCK) || HoleUtils.mc.world.getBlockState(pos.west()).getBlock().equals((Object)Blocks.OBSIDIAN)) && (HoleUtils.mc.world.getBlockState(pos.south()).getBlock().equals((Object)Blocks.BEDROCK) || HoleUtils.mc.world.getBlockState(pos.south()).getBlock().equals((Object)Blocks.OBSIDIAN)) && (HoleUtils.mc.world.getBlockState(pos.north()).getBlock().equals((Object)Blocks.BEDROCK) || HoleUtils.mc.world.getBlockState(pos.north()).getBlock().equals((Object)Blocks.OBSIDIAN))) {
            retVal = true;
        }
        return retVal;
    }

    static boolean isSafeBlock(BlockPos pos) {
        return HoleUtils.mc.world.getBlockState(pos).getBlock() == Blocks.OBSIDIAN || HoleUtils.mc.world.getBlockState(pos).getBlock() == Blocks.BEDROCK;
    }

    public static boolean isDoubleBedrockHoleX(BlockPos blockPos) {
        if (!HoleUtils.mc.world.getBlockState(blockPos).getBlock().equals((Object)Blocks.AIR) || !HoleUtils.mc.world.getBlockState(blockPos.add(1, 0, 0)).getBlock().equals((Object)Blocks.AIR) || !HoleUtils.mc.world.getBlockState(blockPos.add(0, 1, 0)).getBlock().equals((Object)Blocks.AIR) && !HoleUtils.mc.world.getBlockState(blockPos.add(1, 1, 0)).getBlock().equals((Object)Blocks.AIR) || !HoleUtils.mc.world.getBlockState(blockPos.add(0, 2, 0)).getBlock().equals((Object)Blocks.AIR) && !HoleUtils.mc.world.getBlockState(blockPos.add(1, 2, 0)).getBlock().equals((Object)Blocks.AIR)) {
            return false;
        }
        for (BlockPos blockPos2 : new BlockPos[]{blockPos.add(2, 0, 0), blockPos.add(1, 0, 1), blockPos.add(1, 0, -1), blockPos.add(-1, 0, 0), blockPos.add(0, 0, 1), blockPos.add(0, 0, -1), blockPos.add(0, -1, 0), blockPos.add(1, -1, 0)}) {
            IBlockState iBlockState = HoleUtils.mc.world.getBlockState(blockPos2);
            if (iBlockState.getBlock() != Blocks.AIR && iBlockState.getBlock() == Blocks.BEDROCK) continue;
            return false;
        }
        return true;
    }

    public static boolean isDoubleBedrockHoleZ(BlockPos blockPos) {
        if (!HoleUtils.mc.world.getBlockState(blockPos).getBlock().equals((Object)Blocks.AIR) || !HoleUtils.mc.world.getBlockState(blockPos.add(0, 0, 1)).getBlock().equals((Object)Blocks.AIR) || !HoleUtils.mc.world.getBlockState(blockPos.add(0, 1, 0)).getBlock().equals((Object)Blocks.AIR) && !HoleUtils.mc.world.getBlockState(blockPos.add(0, 1, 1)).getBlock().equals((Object)Blocks.AIR) || !HoleUtils.mc.world.getBlockState(blockPos.add(0, 2, 0)).getBlock().equals((Object)Blocks.AIR) && !HoleUtils.mc.world.getBlockState(blockPos.add(0, 2, 1)).getBlock().equals((Object)Blocks.AIR)) {
            return false;
        }
        for (BlockPos blockPos2 : new BlockPos[]{blockPos.add(0, 0, 2), blockPos.add(1, 0, 1), blockPos.add(-1, 0, 1), blockPos.add(0, 0, -1), blockPos.add(1, 0, 0), blockPos.add(-1, 0, 0), blockPos.add(0, -1, 0), blockPos.add(0, -1, 1)}) {
            IBlockState iBlockState = HoleUtils.mc.world.getBlockState(blockPos2);
            if (iBlockState.getBlock() != Blocks.AIR && iBlockState.getBlock() == Blocks.BEDROCK) continue;
            return false;
        }
        return true;
    }

    public static boolean isDoubleObsidianHoleX(BlockPos blockPos) {
        if (!HoleUtils.mc.world.getBlockState(blockPos).getBlock().equals((Object)Blocks.AIR) || !HoleUtils.mc.world.getBlockState(blockPos.add(1, 0, 0)).getBlock().equals((Object)Blocks.AIR) || !HoleUtils.mc.world.getBlockState(blockPos.add(0, 1, 0)).getBlock().equals((Object)Blocks.AIR) && !HoleUtils.mc.world.getBlockState(blockPos.add(1, 1, 0)).getBlock().equals((Object)Blocks.AIR) || !HoleUtils.mc.world.getBlockState(blockPos.add(0, 2, 0)).getBlock().equals((Object)Blocks.AIR) && !HoleUtils.mc.world.getBlockState(blockPos.add(1, 2, 0)).getBlock().equals((Object)Blocks.AIR)) {
            return false;
        }
        for (BlockPos blockPos2 : new BlockPos[]{blockPos.add(2, 0, 0), blockPos.add(1, 0, 1), blockPos.add(1, 0, -1), blockPos.add(-1, 0, 0), blockPos.add(0, 0, 1), blockPos.add(0, 0, -1), blockPos.add(0, -1, 0), blockPos.add(1, -1, 0)}) {
            if (BlockUtils.getBlockResistance(blockPos2) == BlockUtils.BlockResistance.Resistant || BlockUtils.getBlockResistance(blockPos2) == BlockUtils.BlockResistance.Unbreakable) continue;
            return false;
        }
        return true;
    }

    public static boolean isDoubleObsidianHoleZ(BlockPos blockPos) {
        if (!HoleUtils.mc.world.getBlockState(blockPos).getBlock().equals((Object)Blocks.AIR) || !HoleUtils.mc.world.getBlockState(blockPos.add(0, 0, 1)).getBlock().equals((Object)Blocks.AIR) || !HoleUtils.mc.world.getBlockState(blockPos.add(0, 1, 0)).getBlock().equals((Object)Blocks.AIR) && !HoleUtils.mc.world.getBlockState(blockPos.add(0, 1, 1)).getBlock().equals((Object)Blocks.AIR) || !HoleUtils.mc.world.getBlockState(blockPos.add(0, 2, 0)).getBlock().equals((Object)Blocks.AIR) && !HoleUtils.mc.world.getBlockState(blockPos.add(0, 2, 1)).getBlock().equals((Object)Blocks.AIR)) {
            return false;
        }
        for (BlockPos blockPos2 : new BlockPos[]{blockPos.add(0, 0, 2), blockPos.add(1, 0, 1), blockPos.add(-1, 0, 1), blockPos.add(0, 0, -1), blockPos.add(1, 0, 0), blockPos.add(-1, 0, 0), blockPos.add(0, -1, 0), blockPos.add(0, -1, 1)}) {
            if (BlockUtils.getBlockResistance(blockPos2) == BlockUtils.BlockResistance.Resistant || BlockUtils.getBlockResistance(blockPos2) == BlockUtils.BlockResistance.Unbreakable) continue;
            return false;
        }
        return true;
    }

    public static boolean isInHole(EntityPlayer player) {
        boolean retVal = false;
        BlockPos pos = new BlockPos(Math.floor(player.posX), player.posY, Math.floor(player.posZ));
        if (HoleUtils.mc.world.getBlockState(pos).getBlock().equals((Object)Blocks.AIR) && HoleUtils.mc.world.getBlockState(pos.up()).getBlock().equals((Object)Blocks.AIR) && (HoleUtils.mc.world.getBlockState(pos.down()).getBlock().equals((Object)Blocks.BEDROCK) || HoleUtils.mc.world.getBlockState(pos.down()).getBlock().equals((Object)Blocks.OBSIDIAN)) && (HoleUtils.mc.world.getBlockState(pos.east()).getBlock().equals((Object)Blocks.BEDROCK) || HoleUtils.mc.world.getBlockState(pos.east()).getBlock().equals((Object)Blocks.OBSIDIAN)) && (HoleUtils.mc.world.getBlockState(pos.west()).getBlock().equals((Object)Blocks.BEDROCK) || HoleUtils.mc.world.getBlockState(pos.west()).getBlock().equals((Object)Blocks.OBSIDIAN)) && (HoleUtils.mc.world.getBlockState(pos.south()).getBlock().equals((Object)Blocks.BEDROCK) || HoleUtils.mc.world.getBlockState(pos.south()).getBlock().equals((Object)Blocks.OBSIDIAN)) && (HoleUtils.mc.world.getBlockState(pos.north()).getBlock().equals((Object)Blocks.BEDROCK) || HoleUtils.mc.world.getBlockState(pos.north()).getBlock().equals((Object)Blocks.OBSIDIAN))) {
            retVal = true;
        }
        return retVal;
    }
}

