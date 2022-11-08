package dev.evangelion.client.modules.combat;

import dev.evangelion.api.utilities.BlockUtils;
import net.minecraft.util.EnumHand;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import dev.evangelion.Evangelion;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.MathHelper;
import dev.evangelion.api.utilities.ChatUtils;
import net.minecraft.util.math.BlockPos;
import dev.evangelion.client.events.EventMotion;
import dev.evangelion.api.utilities.InventoryUtils;
import dev.evangelion.client.values.impl.ValueEnum;
import dev.evangelion.api.manager.module.RegisterModule;
import dev.evangelion.api.manager.module.Module;

@RegisterModule(name = "SelfFill", tag = "Self Fill", description = "Automatically fills your position with a block and either rubberbands you into it or jumps on it.", category = Module.Category.COMBAT)
public class ModuleSelfFill extends Module
{
    private final ValueEnum mode;
    private final ValueEnum autoSwitch;
    private final ValueEnum itemSwitch;
    
    public ModuleSelfFill() {
        this.mode = new ValueEnum("Mode", "Mode", "Mode for the burrow.", Modes.Normal);
        this.autoSwitch = new ValueEnum("Switch", "Switch", "The mode for Switching.", InventoryUtils.SwitchModes.Normal);
        this.itemSwitch = new ValueEnum("Item", "Item", "The item to place the blocks with.", InventoryUtils.ItemModes.Obsidian);
    }
    
    @SubscribeEvent
    public void onUpdate(final EventMotion event) {
        super.onUpdate();
        final BlockPos lastPosition = new BlockPos(ModuleSelfFill.mc.player.posX, Math.ceil(ModuleSelfFill.mc.player.posY), ModuleSelfFill.mc.player.posZ);
        final int slot = InventoryUtils.getTargetSlot(this.itemSwitch.getValue().toString());
        final int lastSlot = ModuleSelfFill.mc.player.inventory.currentItem;
        if (slot == -1) {
            ChatUtils.sendMessage("No blocks could be found.", "SelfFill");
            this.disable(true);
            return;
        }
        if (this.mode.getValue().equals(Modes.Normal)) {
            this.doBurrow(event, slot, lastSlot, lastPosition);
            this.disable(true);
        }
        else if (this.mode.getValue().equals(Modes.Persistent)) {
            final BlockPos playerPos = new BlockPos((double)MathHelper.floor(ModuleSelfFill.mc.player.posX), Math.ceil(ModuleSelfFill.mc.player.posY), (double)MathHelper.floor(ModuleSelfFill.mc.player.posZ));
            if (ModuleSelfFill.mc.world.getBlockState(playerPos).getBlock() == Blocks.AIR) {
                this.doBurrow(event, slot, lastSlot, lastPosition);
            }
            if (ModuleSelfFill.mc.gameSettings.keyBindJump.isKeyDown() || Evangelion.MODULE_MANAGER.isModuleEnabled("Step")) {
                this.disable(true);
            }
        }
    }
    
    public void doBurrow(final EventMotion event, final int slot, final int lastSlot, final BlockPos lastPosition) {
        InventoryUtils.switchSlot(slot, this.autoSwitch.getValue().equals(InventoryUtils.SwitchModes.Silent));
        ModuleSelfFill.mc.player.connection.sendPacket((Packet)new CPacketPlayer.Position(ModuleSelfFill.mc.player.posX, ModuleSelfFill.mc.player.posY + 0.419, ModuleSelfFill.mc.player.posZ, true));
        ModuleSelfFill.mc.player.connection.sendPacket((Packet)new CPacketPlayer.Position(ModuleSelfFill.mc.player.posX, ModuleSelfFill.mc.player.posY + 0.75319998, ModuleSelfFill.mc.player.posZ, true));
        ModuleSelfFill.mc.player.connection.sendPacket((Packet)new CPacketPlayer.Position(ModuleSelfFill.mc.player.posX, ModuleSelfFill.mc.player.posY + 1.00013597, ModuleSelfFill.mc.player.posZ, true));
        ModuleSelfFill.mc.player.connection.sendPacket((Packet)new CPacketPlayer.Position(ModuleSelfFill.mc.player.posX, ModuleSelfFill.mc.player.posY + 1.16610926, ModuleSelfFill.mc.player.posZ, true));
        BlockUtils.placeBlock(event, lastPosition, EnumHand.MAIN_HAND);
        ModuleSelfFill.mc.player.connection.sendPacket((Packet)new CPacketPlayer.Position(ModuleSelfFill.mc.player.posX, ModuleSelfFill.mc.player.posY + 1.0, ModuleSelfFill.mc.player.posZ, false));
        if (!this.autoSwitch.getValue().equals(InventoryUtils.SwitchModes.Strict)) {
            InventoryUtils.switchSlot(lastSlot, this.autoSwitch.getValue().equals(InventoryUtils.SwitchModes.Silent));
        }
    }
    
    @Override
    public void onEnable() {
        super.onEnable();
        if (ModuleSelfFill.mc.player == null || ModuleSelfFill.mc.world == null) {
            this.disable(false);
        }
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
        Persistent;
    }
}
