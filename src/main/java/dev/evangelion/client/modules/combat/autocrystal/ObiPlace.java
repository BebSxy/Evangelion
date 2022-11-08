package dev.evangelion.client.modules.combat.autocrystal;

import net.minecraft.block.Block;
import net.minecraft.block.BlockObsidian;
import net.minecraft.block.BlockSnow;
import net.minecraft.block.BlockDeadBush;
import net.minecraft.block.BlockFire;
import net.minecraft.block.BlockTallGrass;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.BlockAir;
import java.util.Iterator;
import dev.evangelion.api.utilities.PlayerUtils;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.init.Blocks;
import dev.evangelion.api.utilities.MathUtils;
import net.minecraft.entity.Entity;
import dev.evangelion.api.utilities.BlockUtils;
import net.minecraft.util.EnumHand;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.util.math.BlockPos;
import dev.evangelion.api.utilities.InventoryUtils;
import net.minecraft.entity.player.EntityPlayer;
import dev.evangelion.client.events.EventMotion;
import dev.evangelion.api.utilities.IMinecraft;

public class ObiPlace implements IMinecraft
{
    public void doObiPlace(final EventMotion event) {
        final ModuleAutoCrystal instance = ModuleAutoCrystal.INSTANCE;
        ModuleAutoCrystal.oldSlot = ObiPlace.mc.player.inventory.currentItem;
        final ModuleAutoCrystal instance2 = ModuleAutoCrystal.INSTANCE;
        ModuleAutoCrystal.obiTarget = (EntityPlayer)this.getClosest();
        final int obiSlot = InventoryUtils.getTargetSlot("Obsidian");
        final ModuleAutoCrystal instance3 = ModuleAutoCrystal.INSTANCE;
        if (ModuleAutoCrystal.obiTarget != null) {
            final ModuleAutoCrystal instance4 = ModuleAutoCrystal.INSTANCE;
            final double floor = Math.floor(ModuleAutoCrystal.obiTarget.posX);
            final ModuleAutoCrystal instance5 = ModuleAutoCrystal.INSTANCE;
            final double posY = ModuleAutoCrystal.obiTarget.posY;
            final ModuleAutoCrystal instance6 = ModuleAutoCrystal.INSTANCE;
            final BlockPos playerPos = new BlockPos(floor, posY, Math.floor(ModuleAutoCrystal.obiTarget.posZ));
            final ModuleAutoCrystal instance7 = ModuleAutoCrystal.INSTANCE;
            if (!ModuleAutoCrystal.placed) {
                if (ModuleAutoCrystal.renderPosition != null) {
                    return;
                }
                final ModuleAutoCrystal instance8 = ModuleAutoCrystal.INSTANCE;
                final BlockPos pos = this.getPos(ModuleAutoCrystal.obiTarget);
                if (pos != null) {
                    if (obiSlot != -1) {
                        ObiPlace.mc.player.connection.sendPacket((Packet)new CPacketHeldItemChange(obiSlot));
                    }
                    BlockUtils.placeBlock(event, pos, EnumHand.MAIN_HAND);
                    final NetHandlerPlayClient connection = ObiPlace.mc.player.connection;
                    final ModuleAutoCrystal instance9 = ModuleAutoCrystal.INSTANCE;
                    connection.sendPacket((Packet)new CPacketHeldItemChange(ModuleAutoCrystal.oldSlot));
                    final ModuleAutoCrystal instance10 = ModuleAutoCrystal.INSTANCE;
                    ModuleAutoCrystal.placed = true;
                    final ModuleAutoCrystal instance11 = ModuleAutoCrystal.INSTANCE;
                    ModuleAutoCrystal.currentPos = pos;
                }
            }
            final ModuleAutoCrystal instance12 = ModuleAutoCrystal.INSTANCE;
            if (ModuleAutoCrystal.currentPos != null) {
                final ModuleAutoCrystal instance13 = ModuleAutoCrystal.INSTANCE;
                if (ModuleAutoCrystal.placed) {
                    final EntityPlayerSP player = ObiPlace.mc.player;
                    final ModuleAutoCrystal instance14 = ModuleAutoCrystal.INSTANCE;
                    if (player.getDistance((Entity)ModuleAutoCrystal.obiTarget) <= ModuleAutoCrystal.INSTANCE.targetRange.getValue().doubleValue()) {
                        final EntityPlayerSP player2 = ObiPlace.mc.player;
                        final ModuleAutoCrystal instance15 = ModuleAutoCrystal.INSTANCE;
                        if (player2.getDistanceSq(ModuleAutoCrystal.currentPos) <= MathUtils.square(ModuleAutoCrystal.INSTANCE.blockDistance.getValue().doubleValue())) {
                            final int getY = playerPos.getY();
                            final ModuleAutoCrystal instance16 = ModuleAutoCrystal.INSTANCE;
                            if (getY > ModuleAutoCrystal.currentPos.getY()) {
                                final ModuleAutoCrystal instance17 = ModuleAutoCrystal.INSTANCE;
                                if (!BlockUtils.isIntercepted(ModuleAutoCrystal.currentPos.up())) {
                                    final WorldClient world = ObiPlace.mc.world;
                                    final ModuleAutoCrystal instance18 = ModuleAutoCrystal.INSTANCE;
                                    if (world.getBlockState(ModuleAutoCrystal.currentPos.up()).getBlock() == Blocks.AIR) {
                                        return;
                                    }
                                }
                            }
                        }
                    }
                    final ModuleAutoCrystal instance19 = ModuleAutoCrystal.INSTANCE;
                    ModuleAutoCrystal.placed = false;
                }
            }
        }
    }
    
    public BlockPos getPos(final EntityPlayer target) {
        BlockPos placePos = null;
        final BlockPos playerPos = new BlockPos(Math.floor(target.posX), target.posY, Math.floor(target.posZ));
        double dist = MathUtils.square(ModuleAutoCrystal.INSTANCE.palceReach.getValue().doubleValue());
        for (final BlockPos pos : PlayerUtils.getSphere(ModuleAutoCrystal.INSTANCE.palceReach.getValue().floatValue(), true, false)) {
            if (pos.getY() >= playerPos.getY()) {
                continue;
            }
            if (pos == playerPos) {
                continue;
            }
            if (!this.canPlace(pos, true, true)) {
                continue;
            }
            if (ObiPlace.mc.world.getBlockState(pos.up()).getBlock() != Blocks.AIR) {
                continue;
            }
            if (BlockUtils.isIntercepted(pos.up())) {
                continue;
            }
            if (BlockUtils.isIntercepted(pos.up())) {
                continue;
            }
            final double pDist = target.getDistanceSq(pos);
            if (pDist >= dist) {
                continue;
            }
            dist = pDist;
            placePos = pos;
        }
        return placePos;
    }
    
    public Entity getClosest() {
        Entity returnEntity = null;
        double dist = ModuleAutoCrystal.INSTANCE.targetRange.getValue().doubleValue();
        for (final Entity entity : ObiPlace.mc.world.loadedEntityList) {
            if (entity instanceof EntityPlayer && entity != null) {
                if (ObiPlace.mc.player.getDistance(entity) > dist) {
                    continue;
                }
                if (entity == ObiPlace.mc.player) {
                    continue;
                }
                final double pDist = ObiPlace.mc.player.getDistance(entity);
                if (pDist >= dist) {
                    continue;
                }
                dist = pDist;
                returnEntity = entity;
            }
        }
        return returnEntity;
    }
    
    public boolean canPlace(final BlockPos pos, final boolean obsidian, final boolean bedrock) {
        final Block block = BlockUtils.mc.world.getBlockState(pos).getBlock();
        return block instanceof BlockAir || block instanceof BlockLiquid || block instanceof BlockTallGrass || block instanceof BlockFire || block instanceof BlockDeadBush || block instanceof BlockSnow || (block instanceof BlockObsidian && obsidian) || (block == Blocks.BEDROCK && bedrock);
    }
}
