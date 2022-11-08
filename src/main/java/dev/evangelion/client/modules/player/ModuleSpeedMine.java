package dev.evangelion.client.modules.player;

import net.minecraft.block.BlockShulkerBox;
import dev.evangelion.api.utilities.PlayerUtils;
import java.util.Iterator;
import net.minecraft.entity.Entity;
import java.util.Collection;
import java.util.ArrayList;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.item.ItemStack;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.init.Enchantments;
import net.minecraft.world.World;
import net.minecraft.util.EnumHand;
import dev.evangelion.client.events.EventClickBlock;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import dev.evangelion.api.utilities.RenderUtils;
import dev.evangelion.client.events.EventRender3D;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import dev.evangelion.client.modules.client.ModuleRotations;
import dev.evangelion.api.utilities.RotationUtils;
import dev.evangelion.Evangelion;
import dev.evangelion.api.utilities.InventoryUtils;
import net.minecraft.init.Items;
import dev.evangelion.api.utilities.MathUtils;
import net.minecraft.init.Blocks;
import dev.evangelion.client.events.EventMotion;
import java.awt.Color;
import dev.evangelion.api.utilities.TimerUtils;
import net.minecraft.util.EnumFacing;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import dev.evangelion.client.values.impl.ValueColor;
import dev.evangelion.client.values.impl.ValueBoolean;
import dev.evangelion.client.values.impl.ValueNumber;
import dev.evangelion.client.values.impl.ValueCategory;
import dev.evangelion.client.values.impl.ValueEnum;
import dev.evangelion.api.manager.module.RegisterModule;
import dev.evangelion.api.manager.module.Module;

@RegisterModule(name = "SpeedMine", tag = "Speed Mine", description = "Makes mining faster and easier.", category = Module.Category.PLAYER)
public class ModuleSpeedMine extends Module
{
    private final ValueEnum mode;
    private final ValueCategory packetCategory;
    private final ValueEnum finishMode;
    private final ValueEnum switchMode;
    private final ValueNumber resetRange;
    private final ValueCategory autoMineCategory;
    private final ValueBoolean burrow;
    private final ValueNumber targetRange;
    private final ValueBoolean enderChests;
    private final ValueNumber EChestRange;
    private final ValueBoolean shulkers;
    private final ValueNumber shulkerRange;
    private final ValueBoolean autoTrap;
    private final ValueCategory renderCategory;
    private final ValueEnum renderMode;
    private final ValueEnum colorMode;
    private final ValueNumber width;
    private final ValueColor renderColor;
    private BlockPos autoMinePos;
    private EntityPlayer target;
    private BlockPos EChestPos;
    private BlockPos shulkerPos;
    private BlockPos currentPosition;
    private EnumFacing currentSide;
    private final TimerUtils timer;
    private boolean switched;
    
    public ModuleSpeedMine() {
        this.mode = new ValueEnum("Mode", "Mode", "The mode for the Speed Mine.", Modes.Packet);
        this.packetCategory = new ValueCategory("Packet", "The category for the mining mode Packet.");
        this.finishMode = new ValueEnum("Finish", "Finish", "The mode for finishing.", this.packetCategory, FinishModes.Instant);
        this.switchMode = new ValueEnum("PacketSwitch", "Switch", "The mode for switching.", this.packetCategory, SwitchModes.None);
        this.resetRange = new ValueNumber("PacketResetRange", "Reset Range", "The range for resetting your current position.", this.packetCategory, 10.0f, 0.0f, 50.0f);
        this.autoMineCategory = new ValueCategory("AutoMine", "Auto mine category.");
        this.burrow = new ValueBoolean("Burrow", "Burrow", "Mine burrow blocks.", this.autoMineCategory, true);
        this.targetRange = new ValueNumber("TargetRange", "Target Range", "Range to target entities for auto mine.", this.autoMineCategory, 5.0f, 3.0f, 8.0f);
        this.enderChests = new ValueBoolean("EnderChests", "Ender Chests", "Automatically mine ender chests.", this.autoMineCategory, true);
        this.EChestRange = new ValueNumber("EChestRange", "EChest Range", "Range to select ender chests to target.", this.autoMineCategory, 5.0f, 3.0f, 6.0f);
        this.shulkers = new ValueBoolean("Shulkers", "Shulkers", "Automatically mine shulkers.", this.autoMineCategory, true);
        this.shulkerRange = new ValueNumber("ShulkerRange", "Shulker Range", "Range to select shulkers to target.", this.autoMineCategory, 5.0f, 3.0f, 6.0f);
        this.autoTrap = new ValueBoolean("AutoTrap", "Auto Trap", "Mine auto trap blocks.", this.autoMineCategory, true);
        this.renderCategory = new ValueCategory("Render", "The category for rendering.");
        this.renderMode = new ValueEnum("RenderMode", "Render Mode", "Mode to render the block.", this.renderCategory, RenderModes.Normal);
        this.colorMode = new ValueEnum("ColorMode", "Color Mode", "Mode for the color of the render.", this.renderCategory, ColorModes.Status);
        this.width = new ValueNumber("Width", "Width", "The width for the Outline.", this.renderCategory, 1.0f, 0.1f, 5.0f);
        this.renderColor = new ValueColor("RenderColor", "Color", "The color for the rendering.", this.renderCategory, new Color(0, 0, 255, 100));
        this.autoMinePos = null;
        this.target = null;
        this.EChestPos = null;
        this.shulkerPos = null;
        this.currentPosition = null;
        this.currentSide = null;
        this.timer = new TimerUtils();
        this.switched = false;
    }
    
    @SubscribeEvent
    public void onUpdate(final EventMotion event) {
        this.target = this.target();
        this.EChestPos = this.EChestPos();
        this.shulkerPos = this.shulkerPos();
        if (this.autoMinePos != null && (ModuleSpeedMine.mc.world.getBlockState(this.autoMinePos).getBlock() == Blocks.AIR || ModuleSpeedMine.mc.player.getDistanceSq(this.autoMinePos) > MathUtils.square(this.resetRange.getValue().floatValue()))) {
            this.autoMinePos = null;
        }
        if (this.target != null) {
            final BlockPos pos = new BlockPos(this.target.posX, this.target.posY, this.target.posZ);
            if (ModuleSpeedMine.mc.world.getBlockState(pos).getBlock() != Blocks.AIR && this.burrow.getValue()) {
                this.hitPos(pos);
            }
        }
        if (this.autoTrap.getValue()) {
            BlockPos pos = new BlockPos(ModuleSpeedMine.mc.player.posX, ModuleSpeedMine.mc.player.posY, ModuleSpeedMine.mc.player.posZ);
            pos = pos.up().up();
            if (ModuleSpeedMine.mc.world.getBlockState(pos).getBlock() != Blocks.AIR) {
                this.hitPos(pos);
            }
        }
        if (this.shulkerPos != null && this.shulkers.getValue()) {
            this.hitPos(this.shulkerPos);
        }
        if (this.EChestPos != null && this.enderChests.getValue()) {
            this.hitPos(this.EChestPos);
        }
        if (this.currentPosition != null && this.currentSide != null) {
            if (ModuleSpeedMine.mc.world.getBlockState(this.currentPosition).getBlock() == Blocks.AIR || ModuleSpeedMine.mc.player.getDistanceSq(this.currentPosition) > MathUtils.square(this.resetRange.getValue().floatValue())) {
                this.currentPosition = null;
                this.currentSide = null;
            }
            if (this.finishMode.getValue().equals(FinishModes.Instant) && this.currentPosition != null && this.currentSide != null && !this.switched && this.timer.hasTimeElapsed(this.calculateTime(this.currentPosition)) && !this.switchMode.getValue().equals(SwitchModes.None)) {
                final int slot = InventoryUtils.findItem(Items.DIAMOND_PICKAXE, 0, 9);
                final int lastSlot = ModuleSpeedMine.mc.player.inventory.currentItem;
                if (slot != -1 && ModuleSpeedMine.mc.player.inventory.currentItem != slot) {
                    if (Evangelion.MODULE_MANAGER.isModuleEnabled("Rotations")) {
                        final float[] rot = RotationUtils.getSmoothRotations(RotationUtils.getRotations(this.currentPosition.getX(), this.currentPosition.getY(), this.currentPosition.getZ()), ModuleRotations.INSTANCE.smoothness.getValue().intValue());
                        RotationUtils.rotate(event, rot);
                    }
                    InventoryUtils.switchSlot(slot, this.switchMode.getValue().equals(SwitchModes.Silent));
                    ModuleSpeedMine.mc.player.connection.sendPacket((Packet)new CPacketPlayerDigging(CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK, this.currentPosition, this.currentSide));
                    if (this.switchMode.getValue().equals(SwitchModes.Silent)) {
                        InventoryUtils.switchSlot(lastSlot, this.switchMode.getValue().equals(SwitchModes.Silent));
                    }
                    this.switched = true;
                }
            }
        }
    }
    
    @Override
    public void onRender3D(final EventRender3D event) {
        super.onRender3D(event);
        if (this.currentPosition != null && this.currentSide != null) {
            final long breakTime = this.calculateTime(this.currentPosition);
            final AxisAlignedBB bb = RenderUtils.getRenderBB(this.currentPosition);
            float expand = MathUtils.normalize((float)this.timer.timeElapsed(), 0.0f, (float)breakTime);
            expand = MathHelper.clamp(expand, 0.0f, 1.0f);
            final Color color = this.colorMode.getValue().equals(ColorModes.Status) ? new Color(this.timer.hasTimeElapsed(breakTime) ? 0 : 255, this.timer.hasTimeElapsed(breakTime) ? 255 : 0, 0, this.renderColor.getValue().getAlpha()) : (this.colorMode.getValue().equals(ColorModes.Smooth) ? new Color(255 - (int)(expand * 255.0f), (int)(expand * 255.0f), 0, this.renderColor.getValue().getAlpha()) : this.renderColor.getValue());
            if (this.renderMode.getValue().equals(RenderModes.Normal)) {
                RenderUtils.drawBlock(this.currentPosition, color);
                RenderUtils.drawBlockOutline(this.currentPosition, color, this.width.getValue().floatValue());
            }
            else if (this.renderMode.getValue().equals(RenderModes.Expand)) {
                RenderUtils.drawBlock(RenderUtils.scale(bb, expand), color);
                RenderUtils.drawBlockOutline(RenderUtils.scale(bb, expand), color, this.width.getValue().floatValue());
            }
        }
    }
    
    @SubscribeEvent
    public void onClickBlock(final EventClickBlock event) {
        if (this.currentPosition != null && this.currentSide != null) {
            event.setCancelled(true);
        }
        if (this.finishMode.getValue().equals(FinishModes.Click) && this.currentPosition != null && this.currentSide != null && this.timer.hasTimeElapsed(this.calculateTime(this.currentPosition)) && !this.switchMode.getValue().equals(SwitchModes.None)) {
            final int slot = InventoryUtils.findItem(Items.DIAMOND_PICKAXE, 0, 9);
            final int lastSlot = ModuleSpeedMine.mc.player.inventory.currentItem;
            if (slot != -1 && ModuleSpeedMine.mc.player.inventory.currentItem != slot) {
                InventoryUtils.switchSlot(slot, this.switchMode.getValue().equals(SwitchModes.Silent));
                ModuleSpeedMine.mc.player.connection.sendPacket((Packet)new CPacketPlayerDigging(CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK, this.currentPosition, this.currentSide));
                if (this.switchMode.getValue().equals(SwitchModes.Silent)) {
                    InventoryUtils.switchSlot(lastSlot, this.switchMode.getValue().equals(SwitchModes.Silent));
                }
            }
        }
        if (ModuleSpeedMine.mc.world.getBlockState(event.getPosition()).getBlock().blockHardness != -1.0f) {
            this.timer.reset();
            this.switched = false;
            if (event.isPre() && ModuleSpeedMine.mc.playerController.curBlockDamageMP > 0.1f) {
                ModuleSpeedMine.mc.playerController.isHittingBlock = true;
            }
            if (event.isPost()) {
                ModuleSpeedMine.mc.playerController.isHittingBlock = false;
                ModuleSpeedMine.mc.player.swingArm(EnumHand.MAIN_HAND);
                if (event.getPosition() != this.currentPosition && this.currentPosition != null) {
                    ModuleSpeedMine.mc.player.connection.sendPacket((Packet)new CPacketPlayerDigging(CPacketPlayerDigging.Action.ABORT_DESTROY_BLOCK, this.currentPosition, this.currentSide));
                    ModuleSpeedMine.mc.playerController.isHittingBlock = false;
                    ModuleSpeedMine.mc.playerController.curBlockDamageMP = 0.0f;
                }
                this.currentPosition = event.getPosition();
                this.currentSide = event.getSide();
                ModuleSpeedMine.mc.player.connection.sendPacket((Packet)new CPacketPlayerDigging(CPacketPlayerDigging.Action.START_DESTROY_BLOCK, event.getPosition(), event.getSide()));
                ModuleSpeedMine.mc.player.connection.sendPacket((Packet)new CPacketPlayerDigging(CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK, event.getPosition(), event.getSide()));
                event.setCancelled(true);
            }
        }
    }
    
    private long calculateTime(final BlockPos position) {
        if (this.getBreakSpeed(position) == -1.0f) {
            return -1L;
        }
        return (int)Math.ceil(1.0f / (this.getBreakSpeed(position) / ModuleSpeedMine.mc.world.getBlockState(position).getBlockHardness((World)ModuleSpeedMine.mc.world, position) / 30.0f)) * 50L;
    }
    
    private float getBreakSpeed(final BlockPos position) {
        final IBlockState state = ModuleSpeedMine.mc.world.getBlockState(position);
        float maxSpeed = 1.0f;
        for (int i = 0; i <= 9; ++i) {
            final ItemStack stack = ModuleSpeedMine.mc.player.inventory.getStackInSlot(i);
            if (!stack.isEmpty()) {
                float speed = stack.getDestroySpeed(state);
                if (speed > 1.0f) {
                    final int efficiency = EnchantmentHelper.getEnchantmentLevel(Enchantments.EFFICIENCY, stack);
                    if (efficiency > 0) {
                        speed += MathUtils.square((float)efficiency) + 1.0f;
                    }
                    if (speed > maxSpeed) {
                        maxSpeed = speed;
                    }
                }
            }
        }
        return maxSpeed;
    }
    
    public void hitPos(final BlockPos pos) {
        if (pos.equals((Object)this.autoMinePos) || (this.currentPosition != null && this.currentSide != null)) {
            return;
        }
        if (ModuleSpeedMine.mc.world.getBlockState(pos).getBlock().blockHardness != -1.0f) {
            this.autoMinePos = pos;
            ModuleSpeedMine.mc.playerController.onPlayerDamageBlock(pos, getFacing(pos, true));
            ModuleSpeedMine.mc.player.swingArm(EnumHand.MAIN_HAND);
        }
    }
    
    public static EnumFacing getFacing(final BlockPos pos, final boolean verticals) {
        for (final EnumFacing facing : EnumFacing.values()) {
            final RayTraceResult result = ModuleSpeedMine.mc.world.rayTraceBlocks(new Vec3d(ModuleSpeedMine.mc.player.posX, (double)ModuleSpeedMine.mc.player.eyeHeight, ModuleSpeedMine.mc.player.posZ), new Vec3d(pos.getX() + 0.5 + facing.getDirectionVec().getX() * 1.0 / 2.0, pos.getY() + 0.5 + facing.getDirectionVec().getY() * 1.0 / 2.0, pos.getZ() + 0.5 + facing.getDirectionVec().getZ() * 1.0 / 2.0), false, true, false);
            if (result != null && result.typeOfHit == RayTraceResult.Type.BLOCK && result.getBlockPos().equals((Object)pos)) {
                return facing;
            }
        }
        if (!verticals) {
            return null;
        }
        if (pos.getY() > ModuleSpeedMine.mc.player.posY + ModuleSpeedMine.mc.player.getEyeHeight()) {
            return EnumFacing.DOWN;
        }
        return EnumFacing.UP;
    }
    
    public EntityPlayer target() {
        EntityPlayer target = null;
        for (final EntityPlayer player : new ArrayList<EntityPlayer>(ModuleSpeedMine.mc.world.playerEntities)) {
            if (player == null) {
                continue;
            }
            if (ModuleSpeedMine.mc.player.getDistance((Entity)player) > this.targetRange.getValue().floatValue()) {
                continue;
            }
            if (player == ModuleSpeedMine.mc.player) {
                continue;
            }
            if (Evangelion.FRIEND_MANAGER.isFriend(player.getName())) {
                continue;
            }
            if (target == null) {
                target = player;
            }
            else {
                if (ModuleSpeedMine.mc.player.getDistanceSq((Entity)player) >= ModuleSpeedMine.mc.player.getDistanceSq((Entity)target)) {
                    continue;
                }
                target = player;
            }
        }
        return target;
    }
    
    public BlockPos EChestPos() {
        BlockPos bestEChest = null;
        for (final BlockPos pos : PlayerUtils.getSphere(this.EChestRange.getValue().floatValue(), true, false)) {
            if (ModuleSpeedMine.mc.world.getBlockState(pos).getBlock() == Blocks.ENDER_CHEST) {
                if (bestEChest == null) {
                    bestEChest = pos;
                }
                else {
                    if (ModuleSpeedMine.mc.player.getDistanceSq(pos) >= ModuleSpeedMine.mc.player.getDistanceSq(bestEChest)) {
                        continue;
                    }
                    bestEChest = pos;
                }
            }
        }
        return bestEChest;
    }
    
    public BlockPos shulkerPos() {
        BlockPos bestShulker = null;
        for (final BlockPos pos : PlayerUtils.getSphere(this.shulkerRange.getValue().floatValue(), true, false)) {
            if (ModuleSpeedMine.mc.world.getBlockState(pos).getBlock() instanceof BlockShulkerBox) {
                if (bestShulker == null) {
                    bestShulker = pos;
                }
                else {
                    if (ModuleSpeedMine.mc.player.getDistanceSq(pos) >= ModuleSpeedMine.mc.player.getDistanceSq(bestShulker)) {
                        continue;
                    }
                    bestShulker = pos;
                }
            }
        }
        return bestShulker;
    }
    
    public enum Modes
    {
        Packet;
    }
    
    public enum FinishModes
    {
        Instant, 
        Click;
    }
    
    public enum SwitchModes
    {
        None, 
        Normal, 
        Silent;
    }
    
    public enum RenderModes
    {
        Normal, 
        Expand;
    }
    
    public enum ColorModes
    {
        Status, 
        Smooth, 
        Custom;
    }
}
