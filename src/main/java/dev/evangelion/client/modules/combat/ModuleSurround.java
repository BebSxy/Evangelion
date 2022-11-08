package dev.evangelion.client.modules.combat;

import net.minecraft.util.EnumFacing;
import java.util.Collection;
import net.minecraft.world.IBlockAccess;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.util.EnumHand;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import java.util.Iterator;
import dev.evangelion.api.utilities.BlockUtils;
import dev.evangelion.api.utilities.ChatUtils;
import dev.evangelion.Evangelion;
import dev.evangelion.api.utilities.MathUtils;
import dev.evangelion.client.events.EventMotion;
import dev.evangelion.api.utilities.InventoryUtils;
import net.minecraft.util.math.BlockPos;
import dev.evangelion.client.values.impl.ValueBoolean;
import dev.evangelion.client.values.impl.ValueNumber;
import dev.evangelion.client.values.impl.ValueEnum;
import dev.evangelion.api.manager.module.RegisterModule;
import dev.evangelion.api.manager.module.Module;

@RegisterModule(name = "Surround", description = "Places blocks around your feet to protect you from crystals.", category = Module.Category.COMBAT)
public class ModuleSurround extends Module
{
    private final ValueEnum mode;
    private final ValueEnum autoSwitch;
    private final ValueEnum itemSwitch;
    private final ValueNumber blocks;
    private final ValueEnum supports;
    private final ValueBoolean dynamic;
    private final ValueBoolean ignoreCrystals;
    private final ValueBoolean stepDisable;
    private final ValueBoolean jumpDisable;
    private int placements;
    private BlockPos startPosition;
    
    public ModuleSurround() {
        this.mode = new ValueEnum("Mode", "Mode", "The mode for the Surround.", Modes.Normal);
        this.autoSwitch = new ValueEnum("Switch", "Switch", "The mode for Switching.", InventoryUtils.SwitchModes.Normal);
        this.itemSwitch = new ValueEnum("Item", "Item", "The item to place the blocks with.", InventoryUtils.ItemModes.Obsidian);
        this.blocks = new ValueNumber("Blocks", "Blocks", "The amount of blocks that can be placed per tick.", 8, 1, 40);
        this.supports = new ValueEnum("Supports", "Supports", "The support blocks for the Surround.", Supports.Dynamic);
        this.dynamic = new ValueBoolean("Dynamic", "Dynamic", "Makes the surround place dynamically.", true);
        this.ignoreCrystals = new ValueBoolean("IgnoreCrystals", "Ignore Crystals", "Ignores crystals when checking if there are any entities in the block that needs to be placed.", false);
        this.stepDisable = new ValueBoolean("StepDisable", "Step Disable", "Disable if step enabled.", true);
        this.jumpDisable = new ValueBoolean("JumpDisable", "Jump Disable", "Disable if player jumps.", true);
    }
    
    @SubscribeEvent
    public void onUpdate(final EventMotion event) {
        super.onUpdate();
        if (this.startPosition.getY() != MathUtils.roundToPlaces(ModuleSurround.mc.player.posY, 0) && this.mode.getValue().equals(Modes.Normal)) {
            this.disable(true);
            return;
        }
        if ((this.jumpDisable.getValue() && ModuleSurround.mc.gameSettings.keyBindJump.isKeyDown()) || (this.stepDisable.getValue() && Evangelion.MODULE_MANAGER.isModuleEnabled("Step"))) {
            this.disable(true);
            return;
        }
        final int slot = InventoryUtils.getTargetSlot(this.itemSwitch.getValue().toString());
        final int lastSlot = ModuleSurround.mc.player.inventory.currentItem;
        if (slot == -1) {
            ChatUtils.sendMessage("No blocks could be found.", "Surround");
            this.disable(true);
            return;
        }
        if (!this.getUnsafeBlocks().isEmpty()) {
            InventoryUtils.switchSlot(slot, this.autoSwitch.getValue().equals(InventoryUtils.SwitchModes.Silent));
            for (final BlockPos position : this.getUnsafeBlocks()) {
                if (!this.supports.getValue().equals(Supports.None) && (BlockUtils.getPlaceableSide(position) == null || this.supports.getValue().equals(Supports.Static))) {
                    this.placeBlock(event, position.down());
                }
                this.placeBlock(event, position);
            }
            if (!this.autoSwitch.getValue().equals(InventoryUtils.SwitchModes.Strict)) {
                InventoryUtils.switchSlot(lastSlot, this.autoSwitch.getValue().equals(InventoryUtils.SwitchModes.Silent));
            }
        }
        this.placements = 0;
        if (this.getUnsafeBlocks().isEmpty() && this.mode.getValue().equals(Modes.Toggle)) {
            this.disable(true);
        }
    }
    
    @Override
    public void onEnable() {
        super.onEnable();
        if (ModuleSurround.mc.player == null || ModuleSurround.mc.world == null) {
            this.disable(false);
            return;
        }
        this.startPosition = new BlockPos(MathUtils.roundVector(ModuleSurround.mc.player.getPositionVector(), 0));
    }
    
    public void placeBlock(final EventMotion event, final BlockPos position) {
        if (BlockUtils.isPositionPlaceable(position, true, true, this.ignoreCrystals.getValue()) && this.placements < this.blocks.getValue().intValue()) {
            BlockUtils.placeBlock(event, position, EnumHand.MAIN_HAND);
            ++this.placements;
        }
    }
    
    public List<BlockPos> getUnsafeBlocks() {
        final List<BlockPos> positions = new ArrayList<BlockPos>();
        for (final BlockPos position : this.getOffsets()) {
            if (ModuleSurround.mc.world.getBlockState(position).getBlock().isReplaceable((IBlockAccess)ModuleSurround.mc.world, position)) {
                positions.add(position);
            }
        }
        return positions;
    }
    
    private List<BlockPos> getOffsets() {
        final List<BlockPos> offsets = new ArrayList<BlockPos>();
        if (this.dynamic.getValue()) {
            final double decimalX = Math.abs(ModuleSurround.mc.player.posX) - Math.floor(Math.abs(ModuleSurround.mc.player.posX));
            final double decimalZ = Math.abs(ModuleSurround.mc.player.posZ) - Math.floor(Math.abs(ModuleSurround.mc.player.posZ));
            final int lengthX = this.calculateLength(decimalX, false);
            final int negativeLengthX = this.calculateLength(decimalX, true);
            final int lengthZ = this.calculateLength(decimalZ, false);
            final int negativeLengthZ = this.calculateLength(decimalZ, true);
            final List<BlockPos> tempOffsets = new ArrayList<BlockPos>();
            offsets.addAll(this.getOverlapPositions());
            for (int x = 1; x < lengthX + 1; ++x) {
                tempOffsets.add(this.addToPosition(this.getPlayerPosition(), x, 1 + lengthZ));
                tempOffsets.add(this.addToPosition(this.getPlayerPosition(), x, -(1 + negativeLengthZ)));
            }
            for (int x = 0; x <= negativeLengthX; ++x) {
                tempOffsets.add(this.addToPosition(this.getPlayerPosition(), -x, 1 + lengthZ));
                tempOffsets.add(this.addToPosition(this.getPlayerPosition(), -x, -(1 + negativeLengthZ)));
            }
            for (int z = 1; z < lengthZ + 1; ++z) {
                tempOffsets.add(this.addToPosition(this.getPlayerPosition(), 1 + lengthX, z));
                tempOffsets.add(this.addToPosition(this.getPlayerPosition(), -(1 + negativeLengthX), z));
            }
            for (int z = 0; z <= negativeLengthZ; ++z) {
                tempOffsets.add(this.addToPosition(this.getPlayerPosition(), 1 + lengthX, -z));
                tempOffsets.add(this.addToPosition(this.getPlayerPosition(), -(1 + negativeLengthX), -z));
            }
            offsets.addAll(tempOffsets);
        }
        else {
            for (final EnumFacing side : EnumFacing.HORIZONTALS) {
                offsets.add(this.getPlayerPosition().add(side.getXOffset(), 0, side.getZOffset()));
            }
        }
        return offsets;
    }
    
    private BlockPos getPlayerPosition() {
        return new BlockPos(ModuleSurround.mc.player.posX, (ModuleSurround.mc.player.posY - Math.floor(ModuleSurround.mc.player.posY) > 0.8) ? (Math.floor(ModuleSurround.mc.player.posY) + 1.0) : Math.floor(ModuleSurround.mc.player.posY), ModuleSurround.mc.player.posZ);
    }
    
    private List<BlockPos> getOverlapPositions() {
        final List<BlockPos> positions = new ArrayList<BlockPos>();
        final int offsetX = this.calculateOffset(ModuleSurround.mc.player.posX - Math.floor(ModuleSurround.mc.player.posX));
        final int offsetZ = this.calculateOffset(ModuleSurround.mc.player.posZ - Math.floor(ModuleSurround.mc.player.posZ));
        positions.add(this.getPlayerPosition());
        for (int x = 0; x <= Math.abs(offsetX); ++x) {
            for (int z = 0; z <= Math.abs(offsetZ); ++z) {
                final int properX = x * offsetX;
                final int properZ = z * offsetZ;
                positions.add(this.getPlayerPosition().add(properX, -1, properZ));
            }
        }
        return positions;
    }
    
    private BlockPos addToPosition(final BlockPos position, double x, double z) {
        if (position.getX() < 0) {
            x = -x;
        }
        if (position.getZ() < 0) {
            z = -z;
        }
        return position.add(x, 0.0, z);
    }
    
    private int calculateOffset(final double dec) {
        return (dec >= 0.7) ? 1 : ((dec <= 0.3) ? -1 : 0);
    }
    
    private int calculateLength(final double decimal, final boolean negative) {
        if (negative) {
            return (decimal <= 0.3) ? 1 : 0;
        }
        return (decimal >= 0.7) ? 1 : 0;
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
    
    public enum Modes
    {
        Normal, 
        Persistent, 
        Toggle, 
        Shift;
    }
    
    public enum Supports
    {
        None, 
        Dynamic, 
        Static;
    }
}
