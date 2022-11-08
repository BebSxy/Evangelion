/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.mojang.realmsclient.gui.ChatFormatting
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.CPacketChatMessage
 *  net.minecraft.util.text.ITextComponent
 *  net.minecraft.util.text.TextComponentString
 */
package dev.evangelion.api.utilities;

import com.mojang.realmsclient.gui.ChatFormatting;
import dev.evangelion.api.utilities.IMinecraft;
import dev.evangelion.client.modules.client.ModuleCommands;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketChatMessage;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;

public class ChatUtils
implements IMinecraft {
    public static void sendMessage(String message) {
        if (ChatUtils.mc.player == null || ChatUtils.mc.world == null || ChatUtils.mc.ingameGUI == null) {
            return;
        }
        TextComponentString component = new TextComponentString(ChatUtils.getWatermark() + (!ModuleCommands.INSTANCE.watermarkMode.getValue().equals((Object)ModuleCommands.WatermarkModes.None) ? " " : "") + (Object)ModuleCommands.getFirstColor() + message);
        ChatUtils.mc.ingameGUI.getChatGUI().printChatMessage((ITextComponent)component);
    }

    public static void sendMessage(String message, int id) {
        if (ChatUtils.mc.player == null || ChatUtils.mc.world == null || ChatUtils.mc.ingameGUI == null) {
            return;
        }
        TextComponentString component = new TextComponentString(ChatUtils.getWatermark() + (!ModuleCommands.INSTANCE.watermarkMode.getValue().equals((Object)ModuleCommands.WatermarkModes.None) ? " " : "") + (Object)ModuleCommands.getFirstColor() + message);
        ChatUtils.mc.ingameGUI.getChatGUI().printChatMessageWithOptionalDeletion((ITextComponent)component, id);
    }

    public static void sendMessage(String message, String name) {
        if (ChatUtils.mc.player == null || ChatUtils.mc.world == null || ChatUtils.mc.ingameGUI == null) {
            return;
        }
        TextComponentString component = new TextComponentString(ChatUtils.getWatermark() + (!ModuleCommands.INSTANCE.watermarkMode.getValue().equals((Object)ModuleCommands.WatermarkModes.None) ? " " : "") + (Object)ChatFormatting.AQUA + "[" + name + "]: " + (Object)ModuleCommands.getFirstColor() + message);
        ChatUtils.mc.ingameGUI.getChatGUI().printChatMessage((ITextComponent)component);
    }

    public static void sendMessage(String message, String name, int id) {
        if (ChatUtils.mc.player == null || ChatUtils.mc.world == null || ChatUtils.mc.ingameGUI == null) {
            return;
        }
        TextComponentString component = new TextComponentString(ChatUtils.getWatermark() + (!ModuleCommands.INSTANCE.watermarkMode.getValue().equals((Object)ModuleCommands.WatermarkModes.None) ? " " : "") + (Object)ChatFormatting.AQUA + "[" + name + "]: " + (Object)ModuleCommands.getFirstColor() + message);
        ChatUtils.mc.ingameGUI.getChatGUI().printChatMessageWithOptionalDeletion((ITextComponent)component, id);
    }

    public static void sendRawMessage(String message) {
        if (ChatUtils.mc.player == null || ChatUtils.mc.world == null || ChatUtils.mc.ingameGUI == null) {
            return;
        }
        TextComponentString component = new TextComponentString(message);
        ChatUtils.mc.player.sendMessage((ITextComponent)component);
    }

    public static void sendPlayerMessage(String message) {
        if (ChatUtils.mc.player == null || ChatUtils.mc.world == null) {
            return;
        }
        ChatUtils.mc.player.connection.sendPacket((Packet)new CPacketChatMessage(message));
    }

    public static String getWatermark() {
        if (!ModuleCommands.INSTANCE.watermarkMode.getValue().equals((Object)ModuleCommands.WatermarkModes.None)) {
            return (Object)ModuleCommands.getSecondWatermarkColor() + ModuleCommands.INSTANCE.firstSymbol.getValue() + (Object)ModuleCommands.getFirstWatermarkColor() + (ModuleCommands.INSTANCE.watermarkMode.getValue().equals((Object)ModuleCommands.WatermarkModes.Custom) ? ModuleCommands.INSTANCE.watermarkText.getValue() : (ModuleCommands.INSTANCE.watermarkMode.getValue().equals((Object)ModuleCommands.WatermarkModes.Japanese) ? "\u30a8\u30f4\u30a1" : "Evangelion")) + (Object)ModuleCommands.getSecondWatermarkColor() + ModuleCommands.INSTANCE.secondSymbol.getValue();
        }
        return "";
    }
}

