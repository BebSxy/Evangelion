package dev.evangelion.client.modules.movement;

import net.minecraftforge.client.event.InputUpdateEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraft.item.Item;
import net.minecraft.util.MovementInput;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.entity.player.EntityPlayer;
import dev.evangelion.api.utilities.MovementUtils;
import net.minecraft.network.Packet;
import net.minecraft.entity.Entity;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemFood;
import net.minecraft.client.settings.KeyBinding;
import org.lwjgl.input.Keyboard;
import net.minecraft.client.gui.GuiChat;
import dev.evangelion.client.values.impl.ValueBoolean;
import dev.evangelion.api.manager.module.RegisterModule;
import dev.evangelion.api.manager.module.Module;

@RegisterModule(name = "NoSlow", tag = "No Slow", description = "Stops the player from slowing down in different situations.", category = Module.Category.MOVEMENT)
public class ModuleNoSlow extends Module
{
    public static ModuleNoSlow INSTANCE;
    public ValueBoolean strict;
    public ValueBoolean guiMove;
    public ValueBoolean soulSand;
    boolean sneaking;
    
    public ModuleNoSlow() {
        this.strict = new ValueBoolean("Strict", "Strict", "For strict servers.", true);
        this.guiMove = new ValueBoolean("GuiMove", "Gui Move", "Lets you move when you are in a gui screen.", true);
        this.soulSand = new ValueBoolean("SoulSand", "Soul Sand", "Stops soul sand from slowing down the player", true);
        ModuleNoSlow.INSTANCE = this;
    }
    
    @Override
    public void onUpdate() {
        if (ModuleNoSlow.mc.player == null || ModuleNoSlow.mc.world == null) {
            return;
        }
        if (ModuleNoSlow.mc.currentScreen != null && !(ModuleNoSlow.mc.currentScreen instanceof GuiChat) && this.guiMove.getValue()) {
            ModuleNoSlow.mc.player.movementInput.moveStrafe = 0.0f;
            ModuleNoSlow.mc.player.movementInput.moveForward = 0.0f;
            KeyBinding.setKeyBindState(ModuleNoSlow.mc.gameSettings.keyBindForward.getKeyCode(), Keyboard.isKeyDown(ModuleNoSlow.mc.gameSettings.keyBindForward.getKeyCode()));
            if (Keyboard.isKeyDown(ModuleNoSlow.mc.gameSettings.keyBindForward.getKeyCode())) {
                final MovementInput movementInput = ModuleNoSlow.mc.player.movementInput;
                ++movementInput.moveForward;
                ModuleNoSlow.mc.player.movementInput.forwardKeyDown = true;
            }
            else {
                ModuleNoSlow.mc.player.movementInput.forwardKeyDown = false;
            }
            KeyBinding.setKeyBindState(ModuleNoSlow.mc.gameSettings.keyBindBack.getKeyCode(), Keyboard.isKeyDown(ModuleNoSlow.mc.gameSettings.keyBindBack.getKeyCode()));
            if (Keyboard.isKeyDown(ModuleNoSlow.mc.gameSettings.keyBindBack.getKeyCode())) {
                final MovementInput movementInput2 = ModuleNoSlow.mc.player.movementInput;
                --movementInput2.moveForward;
                ModuleNoSlow.mc.player.movementInput.backKeyDown = true;
            }
            else {
                ModuleNoSlow.mc.player.movementInput.backKeyDown = false;
            }
            KeyBinding.setKeyBindState(ModuleNoSlow.mc.gameSettings.keyBindLeft.getKeyCode(), Keyboard.isKeyDown(ModuleNoSlow.mc.gameSettings.keyBindLeft.getKeyCode()));
            if (Keyboard.isKeyDown(ModuleNoSlow.mc.gameSettings.keyBindLeft.getKeyCode())) {
                final MovementInput movementInput3 = ModuleNoSlow.mc.player.movementInput;
                ++movementInput3.moveStrafe;
                ModuleNoSlow.mc.player.movementInput.leftKeyDown = true;
            }
            else {
                ModuleNoSlow.mc.player.movementInput.leftKeyDown = false;
            }
            KeyBinding.setKeyBindState(ModuleNoSlow.mc.gameSettings.keyBindRight.getKeyCode(), Keyboard.isKeyDown(ModuleNoSlow.mc.gameSettings.keyBindRight.getKeyCode()));
            if (Keyboard.isKeyDown(ModuleNoSlow.mc.gameSettings.keyBindRight.getKeyCode())) {
                final MovementInput movementInput4 = ModuleNoSlow.mc.player.movementInput;
                --movementInput4.moveStrafe;
                ModuleNoSlow.mc.player.movementInput.rightKeyDown = true;
            }
            else {
                ModuleNoSlow.mc.player.movementInput.rightKeyDown = false;
            }
            KeyBinding.setKeyBindState(ModuleNoSlow.mc.gameSettings.keyBindJump.getKeyCode(), Keyboard.isKeyDown(ModuleNoSlow.mc.gameSettings.keyBindJump.getKeyCode()));
            ModuleNoSlow.mc.player.movementInput.jump = Keyboard.isKeyDown(ModuleNoSlow.mc.gameSettings.keyBindJump.getKeyCode());
        }
        if (this.strict.getValue()) {
            final Item item = ModuleNoSlow.mc.player.getActiveItemStack().getItem();
            if (this.sneaking && ((!ModuleNoSlow.mc.player.isHandActive() && item instanceof ItemFood) || item instanceof ItemBow || item instanceof ItemPotion || !(item instanceof ItemFood) || !(item instanceof ItemBow) || !(item instanceof ItemPotion))) {
                ModuleNoSlow.mc.player.connection.sendPacket((Packet)new CPacketEntityAction((Entity)ModuleNoSlow.mc.player, CPacketEntityAction.Action.STOP_SNEAKING));
                this.sneaking = false;
            }
        }
        if (this.strict.getValue() && ModuleNoSlow.mc.player.isHandActive() && !ModuleNoSlow.mc.player.isRiding()) {
            final Item item = ModuleNoSlow.mc.player.getActiveItemStack().getItem();
            if ((MovementUtils.isMoving((EntityPlayer)ModuleNoSlow.mc.player) && item instanceof ItemFood) || item instanceof ItemBow || item instanceof ItemPotion) {
                ModuleNoSlow.mc.player.connection.sendPacket((Packet)new CPacketPlayerDigging(CPacketPlayerDigging.Action.ABORT_DESTROY_BLOCK, new BlockPos(Math.floor(ModuleNoSlow.mc.player.posX), Math.floor(ModuleNoSlow.mc.player.posY), Math.floor(ModuleNoSlow.mc.player.posZ)), EnumFacing.DOWN));
            }
        }
    }
    
    @SubscribeEvent
    public void onUseItem(final LivingEntityUseItemEvent event) {
        if (this.strict.getValue() && !this.sneaking) {
            ModuleNoSlow.mc.player.connection.sendPacket((Packet)new CPacketEntityAction((Entity)ModuleNoSlow.mc.player, CPacketEntityAction.Action.START_SNEAKING));
            this.sneaking = true;
        }
    }
    
    @SubscribeEvent
    public void onInputUpdate(final InputUpdateEvent event) {
        if (ModuleNoSlow.mc.player.isHandActive() && !ModuleNoSlow.mc.player.isRiding()) {
            final MovementInput movementInput = event.getMovementInput();
            movementInput.moveStrafe *= 5.0f;
            final MovementInput movementInput2 = event.getMovementInput();
            movementInput2.moveForward *= 5.0f;
        }
    }
}
