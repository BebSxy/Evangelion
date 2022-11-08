package dev.evangelion.client.modules.client;

import net.minecraft.entity.Entity;
import dev.evangelion.api.utilities.ChatUtils;
import com.mojang.realmsclient.gui.ChatFormatting;
import dev.evangelion.Evangelion;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.util.EnumHand;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import org.lwjgl.input.Mouse;
import dev.evangelion.api.utilities.InventoryUtils;
import net.minecraft.init.Items;
import dev.evangelion.client.values.impl.ValueEnum;
import dev.evangelion.api.manager.module.RegisterModule;
import dev.evangelion.api.manager.module.Module;

@RegisterModule(name = "MiddleClick", tag = "Middle Click", description = "Add actions to middle click.", category = Module.Category.CLIENT)
public class ModuleMiddleClick extends Module
{
    public static ModuleMiddleClick INSTANCE;
    ValueEnum mode;
    int oldSlot;
    private int delay;
    public boolean xping;
    
    public ModuleMiddleClick() {
        this.mode = new ValueEnum("Mode", "Mode", "", modes.XP);
        this.oldSlot = -1;
        this.delay = 0;
        this.xping = false;
        ModuleMiddleClick.INSTANCE = this;
    }
    
    @Override
    public void onUpdate() {
        this.oldSlot = ModuleMiddleClick.mc.player.inventory.currentItem;
        final int pearlSlot = InventoryUtils.findItem(Items.ENDER_PEARL, 0, 9);
        if (this.mode.getValue().equals(modes.XP)) {
            if (Mouse.isButtonDown(2)) {
                if (this.hotbarXP() != -1) {
                    ModuleMiddleClick.mc.player.connection.sendPacket((Packet)new CPacketHeldItemChange(this.hotbarXP()));
                    ModuleMiddleClick.mc.player.connection.sendPacket((Packet)new CPacketPlayerTryUseItem(EnumHand.MAIN_HAND));
                    ModuleMiddleClick.mc.player.connection.sendPacket((Packet)new CPacketHeldItemChange(this.oldSlot));
                    this.xping = true;
                }
            }
            else {
                this.xping = false;
            }
        }
        else if (this.mode.getValue().equals(modes.Pearl) && Mouse.isButtonDown(2) && pearlSlot != -1) {
            ModuleMiddleClick.mc.player.connection.sendPacket((Packet)new CPacketHeldItemChange(pearlSlot));
            ModuleMiddleClick.mc.player.connection.sendPacket((Packet)new CPacketPlayerTryUseItem(EnumHand.MAIN_HAND));
            ModuleMiddleClick.mc.player.connection.sendPacket((Packet)new CPacketHeldItemChange(this.oldSlot));
        }
        if (this.mode.getValue().equals(modes.MCF)) {
            ++this.delay;
            final RayTraceResult object = ModuleMiddleClick.mc.objectMouseOver;
            if (object == null) {
                return;
            }
            if (object.typeOfHit == RayTraceResult.Type.ENTITY) {
                final Entity entity = object.entityHit;
                if (entity instanceof EntityPlayer && !(entity instanceof EntityArmorStand) && !ModuleMiddleClick.mc.player.isDead && ModuleMiddleClick.mc.player.canEntityBeSeen(entity)) {
                    final String ID = entity.getName();
                    if (Mouse.isButtonDown(2) && ModuleMiddleClick.mc.currentScreen == null && !Evangelion.FRIEND_MANAGER.isFriend(ID) && this.delay > 10) {
                        Evangelion.FRIEND_MANAGER.addFriend(ID);
                        ChatUtils.sendMessage("" + ChatFormatting.GREEN + "Added " + ModuleCommands.getSecondColor() + ID + ModuleCommands.getFirstColor() + " as friend");
                        this.delay = 0;
                    }
                    if (Mouse.isButtonDown(2) && ModuleMiddleClick.mc.currentScreen == null && Evangelion.FRIEND_MANAGER.isFriend(ID) && this.delay > 10) {
                        Evangelion.FRIEND_MANAGER.removeFriend(ID);
                        ChatUtils.sendMessage("" + ChatFormatting.RED + "Removed " + ModuleCommands.getSecondColor() + ID + ModuleCommands.getFirstColor() + " as friend");
                        this.delay = 0;
                    }
                }
            }
        }
    }
    
    private int hotbarXP() {
        for (int i = 0; i < 9; ++i) {
            if (ModuleMiddleClick.mc.player.inventory.getStackInSlot(i).getItem() == Items.EXPERIENCE_BOTTLE) {
                return i;
            }
        }
        return -1;
    }
    
    public enum modes
    {
        MCF, 
        XP, 
        Pearl;
    }
}
