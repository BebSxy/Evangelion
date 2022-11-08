package dev.evangelion.client.modules.miscellaneous;

import dev.evangelion.api.utilities.ChatUtils;
import dev.evangelion.client.modules.client.ModuleCommands;
import java.util.Random;
import java.util.Iterator;
import java.util.Collection;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetworkPlayerInfo;
import java.util.ArrayList;
import dev.evangelion.client.values.impl.ValueBoolean;
import dev.evangelion.api.manager.module.RegisterModule;
import dev.evangelion.api.manager.module.Module;

@RegisterModule(name = "Welcomer", tag = "Welcomer", description = "Sends a message when a player joins or leaves the server.", category = Module.Category.MISCELLANEOUS)
public class ModuleWelcomer extends Module
{
    ValueBoolean privateMsg;
    static ArrayList<NetworkPlayerInfo> playerMap;
    static int cachePlayerCount;
    boolean isOnServer;
    public static ArrayList<String> greets;
    public static ArrayList<String> goodbyes;
    public static ArrayList<String> joinMessages;
    public static ArrayList<String> leaveMessages;
    
    public ModuleWelcomer() {
        this.privateMsg = new ValueBoolean("Private", "Private", "Send the message as a client notification.", false);
        ModuleWelcomer.joinMessages.add("Hello, ");
        ModuleWelcomer.joinMessages.add("Welcome to the server, ");
        ModuleWelcomer.joinMessages.add("Nice to see you, ");
        ModuleWelcomer.joinMessages.add("Hey how are you, ");
        ModuleWelcomer.leaveMessages.add("Goodbye, ");
        ModuleWelcomer.leaveMessages.add("See you later, ");
        ModuleWelcomer.leaveMessages.add("Bye bye, ");
        ModuleWelcomer.leaveMessages.add("I hope you had a good time, ");
    }
    
    @Override
    public void onUpdate() {
        if (ModuleWelcomer.mc.player != null) {
            if (ModuleWelcomer.mc.player.ticksExisted % 10 == 0) {
                this.checkPlayers();
            }
            else if (ModuleWelcomer.mc.isSingleplayer()) {
                this.toggle(true);
            }
        }
    }
    
    @Override
    public void onEnable() {
        if (ModuleWelcomer.mc.player == null || ModuleWelcomer.mc.world == null) {
            return;
        }
        this.onJoinServer();
    }
    
    private void checkPlayers() {
        final ArrayList<NetworkPlayerInfo> infoMap = new ArrayList<NetworkPlayerInfo>(Minecraft.getMinecraft().getConnection().getPlayerInfoMap());
        final int currentPlayerCount = infoMap.size();
        if (currentPlayerCount != ModuleWelcomer.cachePlayerCount) {
            final ArrayList<NetworkPlayerInfo> currentInfoMap = (ArrayList<NetworkPlayerInfo>)infoMap.clone();
            currentInfoMap.removeAll(ModuleWelcomer.playerMap);
            if (currentInfoMap.size() > 5) {
                ModuleWelcomer.cachePlayerCount = ModuleWelcomer.playerMap.size();
                this.onJoinServer();
                return;
            }
            final ArrayList<NetworkPlayerInfo> playerMapClone = (ArrayList<NetworkPlayerInfo>)ModuleWelcomer.playerMap.clone();
            playerMapClone.removeAll(infoMap);
            for (final NetworkPlayerInfo npi : currentInfoMap) {
                this.playerJoined(npi);
            }
            for (final NetworkPlayerInfo npi : playerMapClone) {
                this.playerLeft(npi);
            }
            ModuleWelcomer.cachePlayerCount = ModuleWelcomer.playerMap.size();
            this.onJoinServer();
        }
    }
    
    private void onJoinServer() {
        ModuleWelcomer.playerMap = new ArrayList<NetworkPlayerInfo>(Minecraft.getMinecraft().getConnection().getPlayerInfoMap());
        ModuleWelcomer.cachePlayerCount = ModuleWelcomer.playerMap.size();
        this.isOnServer = true;
    }
    
    protected void playerJoined(final NetworkPlayerInfo playerInfo) {
        final Random random = new Random();
        if (!ModuleWelcomer.joinMessages.isEmpty()) {
            if (this.privateMsg.getValue()) {
                ChatUtils.sendMessage(ModuleCommands.getSecondColor() + ModuleWelcomer.joinMessages.get(random.nextInt(ModuleWelcomer.joinMessages.size())) + ModuleCommands.getFirstColor() + playerInfo.getGameProfile().getName());
            }
            else {
                ModuleWelcomer.mc.player.sendChatMessage(ModuleWelcomer.joinMessages.get(random.nextInt(ModuleWelcomer.joinMessages.size())) + playerInfo.getGameProfile().getName());
            }
        }
    }
    
    protected void playerLeft(final NetworkPlayerInfo playerInfo) {
        final Random random = new Random();
        if (!ModuleWelcomer.leaveMessages.isEmpty()) {
            if (this.privateMsg.getValue()) {
                ChatUtils.sendMessage(ModuleCommands.getSecondColor() + ModuleWelcomer.leaveMessages.get(random.nextInt(ModuleWelcomer.leaveMessages.size())) + ModuleCommands.getFirstColor() + playerInfo.getGameProfile().getName());
            }
            else {
                ModuleWelcomer.mc.player.sendChatMessage(ModuleWelcomer.leaveMessages.get(random.nextInt(ModuleWelcomer.leaveMessages.size())) + playerInfo.getGameProfile().getName());
            }
        }
    }
    
    static {
        ModuleWelcomer.joinMessages = new ArrayList<String>();
        ModuleWelcomer.leaveMessages = new ArrayList<String>();
        ModuleWelcomer.playerMap = new ArrayList<NetworkPlayerInfo>();
        ModuleWelcomer.greets = new ArrayList<String>();
        ModuleWelcomer.goodbyes = new ArrayList<String>();
    }
}
