package dev.evangelion.client.modules.player;

import java.util.Iterator;
import net.minecraft.init.Items;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.client.gui.inventory.GuiContainer;
import java.util.HashMap;
import net.minecraft.item.ItemStack;
import java.util.Map;
import dev.evangelion.client.values.impl.ValueNumber;
import dev.evangelion.api.manager.module.RegisterModule;
import dev.evangelion.api.manager.module.Module;

@RegisterModule(name = "Replenish", description = "Automatically refills your hotbar slots when they are low on items.", category = Module.Category.PLAYER)
public class ModuleReplenish extends Module
{
    private final ValueNumber delay;
    private final ValueNumber threshold;
    private int delayStep;
    
    public ModuleReplenish() {
        this.delay = new ValueNumber("Delay", "Delay", "The delay for replenishing in ticks.", 4, 0, 20);
        this.threshold = new ValueNumber("Threshold", "Threshold", "The threshold to replenish the slot at.", 10, 0, 63);
        this.delayStep = 0;
    }
    
    private static Map<Integer, ItemStack> getInventorySlots(int current, final int last) {
        final Map<Integer, ItemStack> fullInventorySlots = new HashMap<Integer, ItemStack>();
        while (current <= last) {
            fullInventorySlots.put(current, (ItemStack)ModuleReplenish.mc.player.inventoryContainer.getInventory().get(current));
            ++current;
        }
        return fullInventorySlots;
    }
    
    @Override
    public void onUpdate() {
        if (ModuleReplenish.mc.player == null) {
            return;
        }
        if (ModuleReplenish.mc.currentScreen instanceof GuiContainer) {
            return;
        }
        if (this.delayStep < this.delay.getValue().intValue()) {
            ++this.delayStep;
            return;
        }
        this.delayStep = 0;
        final Pair<Integer, Integer> slots = this.findReplenishableHotbarSlot();
        if (slots == null) {
            return;
        }
        final int inventorySlot = slots.getKey();
        final int hotbarSlot = slots.getValue();
        ModuleReplenish.mc.playerController.windowClick(0, inventorySlot, 0, ClickType.PICKUP, (EntityPlayer)ModuleReplenish.mc.player);
        ModuleReplenish.mc.playerController.windowClick(0, hotbarSlot, 0, ClickType.PICKUP, (EntityPlayer)ModuleReplenish.mc.player);
        ModuleReplenish.mc.playerController.windowClick(0, inventorySlot, 0, ClickType.PICKUP, (EntityPlayer)ModuleReplenish.mc.player);
    }
    
    private Pair<Integer, Integer> findReplenishableHotbarSlot() {
        Pair<Integer, Integer> returnPair = null;
        for (final Map.Entry<Integer, ItemStack> hotbarSlot : getInventorySlots(36, 44).entrySet()) {
            final ItemStack stack = hotbarSlot.getValue();
            if (!stack.isEmpty()) {
                if (stack.getItem() == Items.AIR) {
                    continue;
                }
                if (!stack.isStackable()) {
                    continue;
                }
                if (stack.getCount() >= stack.getMaxStackSize()) {
                    continue;
                }
                if (stack.getCount() > this.threshold.getValue().doubleValue()) {
                    continue;
                }
                final int inventorySlot = this.findCompatibleInventorySlot(stack);
                if (inventorySlot == -1) {
                    continue;
                }
                returnPair = new Pair<Integer, Integer>(inventorySlot, hotbarSlot.getKey());
            }
        }
        return returnPair;
    }
    
    private int findCompatibleInventorySlot(final ItemStack hotbarStack) {
        int inventorySlot = -1;
        int smallestStackSize = 999;
        for (final Map.Entry<Integer, ItemStack> entry : getInventorySlots(9, 35).entrySet()) {
            final ItemStack inventoryStack = entry.getValue();
            if (!inventoryStack.isEmpty()) {
                if (inventoryStack.getItem() == Items.AIR) {
                    continue;
                }
                if (!this.isCompatibleStacks(hotbarStack, inventoryStack)) {
                    continue;
                }
                final int currentStackSize = ((ItemStack)ModuleReplenish.mc.player.inventoryContainer.getInventory().get((int)entry.getKey())).getCount();
                if (smallestStackSize <= currentStackSize) {
                    continue;
                }
                smallestStackSize = currentStackSize;
                inventorySlot = entry.getKey();
            }
        }
        return inventorySlot;
    }
    
    private boolean isCompatibleStacks(final ItemStack stack1, final ItemStack stack2) {
        return stack1.getItem().equals(stack2.getItem()) && stack1.getDisplayName().equals(stack2.getDisplayName()) && stack1.getItemDamage() == stack2.getItemDamage();
    }
    
    public static class Pair<T, S>
    {
        T key;
        S value;
        
        public Pair(final T key, final S value) {
            this.key = key;
            this.value = value;
        }
        
        public T getKey() {
            return this.key;
        }
        
        public S getValue() {
            return this.value;
        }
        
        public void setValue(final S value) {
            this.value = value;
        }
    }
}
