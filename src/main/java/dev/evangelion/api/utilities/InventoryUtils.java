/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.Block
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.init.Blocks
 *  net.minecraft.inventory.ClickType
 *  net.minecraft.item.Item
 *  net.minecraft.item.ItemBlock
 *  net.minecraft.item.ItemStack
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.CPacketHeldItemChange
 */
package dev.evangelion.api.utilities;

import dev.evangelion.Evangelion;
import dev.evangelion.api.utilities.IMinecraft;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketHeldItemChange;

public class InventoryUtils
implements IMinecraft {
    public static int getTargetSlot(String input) {
        int obsidianSlot = InventoryUtils.findBlock(Blocks.OBSIDIAN, 0, 9);
        int chestSlot = InventoryUtils.findBlock(Blocks.ENDER_CHEST, 0, 9);
        if (obsidianSlot == -1 && chestSlot == -1) {
            return -1;
        }
        if (obsidianSlot != -1 && chestSlot == -1) {
            return obsidianSlot;
        }
        if (obsidianSlot == -1) {
            return chestSlot;
        }
        if (input.equals("Obsidian")) {
            return obsidianSlot;
        }
        return chestSlot;
    }

    public static void switchSlot(int slot, boolean silent) {
        Evangelion.PLAYER_MANAGER.setSwitching(true);
        InventoryUtils.mc.player.connection.sendPacket((Packet)new CPacketHeldItemChange(slot));
        if (!silent) {
            InventoryUtils.mc.player.inventory.currentItem = slot;
        }
        Evangelion.PLAYER_MANAGER.setSwitching(false);
    }

    public static int findItem(Item item, int minimum, int maximum) {
        for (int i = minimum; i <= maximum; ++i) {
            ItemStack stack = InventoryUtils.mc.player.inventory.getStackInSlot(i);
            if (stack.getItem() != item) continue;
            return i;
        }
        return -1;
    }

    public static ItemStack get(int slot) {
        if (slot == -2) {
            return InventoryUtils.mc.player.inventory.getItemStack();
        }
        return (ItemStack)InventoryUtils.mc.player.inventoryContainer.getInventory().get(slot);
    }

    public static int findItem(Item item) {
        for (int i = 9; i < 45; ++i) {
            if (InventoryUtils.get(i).getItem() != item) continue;
            return i;
        }
        return -1;
    }

    public static void offhandItem(Item item) {
        int slot = InventoryUtils.findItem(item);
        if (slot != -1) {
            Evangelion.PLAYER_MANAGER.setSwitching(true);
            InventoryUtils.mc.playerController.windowClick(InventoryUtils.mc.player.inventoryContainer.windowId, slot, 0, ClickType.PICKUP, (EntityPlayer)InventoryUtils.mc.player);
            InventoryUtils.mc.playerController.windowClick(InventoryUtils.mc.player.inventoryContainer.windowId, 45, 0, ClickType.PICKUP, (EntityPlayer)InventoryUtils.mc.player);
            InventoryUtils.mc.playerController.windowClick(InventoryUtils.mc.player.inventoryContainer.windowId, slot, 0, ClickType.PICKUP, (EntityPlayer)InventoryUtils.mc.player);
            InventoryUtils.mc.playerController.updateController();
            Evangelion.PLAYER_MANAGER.setSwitching(false);
        }
    }

    public static int findBlock(Block block, int minimum, int maximum) {
        for (int i = minimum; i <= maximum; ++i) {
            ItemBlock item;
            ItemStack stack = InventoryUtils.mc.player.inventory.getStackInSlot(i);
            if (!(stack.getItem() instanceof ItemBlock) || (item = (ItemBlock)stack.getItem()).getBlock() != block) continue;
            return i;
        }
        return -1;
    }

    public static enum ItemModes {
        Obsidian,
        Chest;

    }

    public static enum SwitchModes {
        Normal,
        Silent,
        Strict;

    }
}

