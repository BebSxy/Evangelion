package dev.evangelion.client.modules.miscellaneous;

import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.network.play.client.CPacketSpectate;
import net.minecraft.network.play.client.CPacketAnimation;
import net.minecraft.network.play.client.CPacketUpdateSign;
import net.minecraft.network.play.client.CPacketCreativeInventoryAction;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.network.play.client.CPacketSeenAdvancements;
import net.minecraft.network.play.client.CPacketResourcePackStatus;
import net.minecraft.network.play.client.CPacketRecipeInfo;
import net.minecraft.network.play.client.CPacketInput;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.network.play.client.CPacketPlayerAbilities;
import net.minecraft.network.play.client.CPacketPlaceRecipe;
import net.minecraft.network.play.client.CPacketSteerBoat;
import net.minecraft.network.play.client.CPacketVehicleMove;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketKeepAlive;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.network.play.client.CPacketCustomPayload;
import net.minecraft.network.play.client.CPacketCloseWindow;
import net.minecraft.network.play.client.CPacketClickWindow;
import net.minecraft.network.play.client.CPacketEnchantItem;
import net.minecraft.network.play.client.CPacketConfirmTransaction;
import net.minecraft.network.play.client.CPacketClientSettings;
import net.minecraft.network.play.client.CPacketClientStatus;
import net.minecraft.network.play.client.CPacketChatMessage;
import net.minecraft.network.play.client.CPacketTabComplete;
import net.minecraft.network.play.client.CPacketConfirmTeleport;
import dev.evangelion.client.events.EventPacketSend;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import dev.evangelion.client.events.EventPacketReceive;
import dev.evangelion.client.values.impl.ValueBoolean;
import dev.evangelion.api.manager.module.RegisterModule;
import dev.evangelion.api.manager.module.Module;

@RegisterModule(name = "PacketCanceler", tag = "Packet Canceler", description = "Cancels packets.", category = Module.Category.MISCELLANEOUS)
public class ModulePacketCanceler extends Module
{
    ValueBoolean sPacketSpawnObject;
    ValueBoolean sPacketSpawnExperienceOrb;
    ValueBoolean sPacketSpawnGlobalEntity;
    ValueBoolean sPacketSpawnMob;
    ValueBoolean sPacketSpawnPainting;
    ValueBoolean sPacketSpawnPlayer;
    ValueBoolean sPacketAnimation;
    ValueBoolean sPacketStatistics;
    ValueBoolean sPacketBlockBreakAnim;
    ValueBoolean sPacketUpdateTileEntity;
    ValueBoolean sPacketBlockAction;
    ValueBoolean sPacketBlockChange;
    ValueBoolean sPacketUpdateBossInfo;
    ValueBoolean sPacketServerDifficulty;
    ValueBoolean sPacketTabComplete;
    ValueBoolean sPacketChat;
    ValueBoolean sPacketMultiBlockChange;
    ValueBoolean sPacketConfirmTransaction;
    ValueBoolean sPacketCloseWindow;
    ValueBoolean sPacketOpenWindow;
    ValueBoolean sPacketWindowItems;
    ValueBoolean sPacketWindowProperty;
    ValueBoolean sPacketSetSlot;
    ValueBoolean sPacketCooldown;
    ValueBoolean sPacketCustomPayload;
    ValueBoolean sPacketCustomSound;
    ValueBoolean sPacketDisconnect;
    ValueBoolean sPacketEntityStatus;
    ValueBoolean sPacketExplosion;
    ValueBoolean sPacketUnloadChunk;
    ValueBoolean sPacketChangeGameState;
    ValueBoolean sPacketKeepAlive;
    ValueBoolean sPacketChunkData;
    ValueBoolean sPacketEffect;
    ValueBoolean sPacketParticles;
    ValueBoolean sPacketJoinGame;
    ValueBoolean sPacketMaps;
    ValueBoolean sPacketEntity;
    ValueBoolean sPacketMoveVehicle;
    ValueBoolean sPacketSignEditorOpen;
    ValueBoolean sPacketPlaceGhostRecipe;
    ValueBoolean sPacketPlayerAbilities;
    ValueBoolean sPacketCombatEvent;
    ValueBoolean sPacketPlayerListItem;
    ValueBoolean sPacketPlayerPosLook;
    ValueBoolean sPacketUseBed;
    ValueBoolean sPacketRecipeBook;
    ValueBoolean sPacketDestroyEntities;
    ValueBoolean sPacketRemoveEntityEffect;
    ValueBoolean sPacketResourcePackSend;
    ValueBoolean sPacketRespawn;
    ValueBoolean sPacketEntityHeadLook;
    ValueBoolean sPacketSelectAdvancementsTab;
    ValueBoolean sPacketWorldBorder;
    ValueBoolean sPacketCamera;
    ValueBoolean sPacketHeldItemChange;
    ValueBoolean sPacketDisplayObjective;
    ValueBoolean sPacketEntityMetadata;
    ValueBoolean sPacketEntityAttach;
    ValueBoolean sPacketEntityVelocity;
    ValueBoolean sPacketEntityEquipment;
    ValueBoolean sPacketSetExperience;
    ValueBoolean sPacketUpdateHealth;
    ValueBoolean sPacketScoreboardObjective;
    ValueBoolean sPacketSetPassengers;
    ValueBoolean sPacketTeams;
    ValueBoolean sPacketUpdateScore;
    ValueBoolean sPacketSpawnPosition;
    ValueBoolean sPacketTimeUpdate;
    ValueBoolean sPacketTitle;
    ValueBoolean sPacketSoundEffect;
    ValueBoolean sPacketPlayerListHeaderFooter;
    ValueBoolean sPacketCollectItem;
    ValueBoolean sPacketEntityTeleport;
    ValueBoolean sPacketAdvancementInfo;
    ValueBoolean sPacketEntityProperties;
    ValueBoolean sPacketEntityEffect;
    ValueBoolean cPacketConfirmTeleport;
    ValueBoolean cPacketTabComplete;
    ValueBoolean cPacketChatMessage;
    ValueBoolean cPacketClientStatus;
    ValueBoolean cPacketClientSettings;
    ValueBoolean cPacketConfirmTransaction;
    ValueBoolean cPacketEnchantItem;
    ValueBoolean cPacketClickWindow;
    ValueBoolean cPacketCloseWindow;
    ValueBoolean cPacketCustomPayload;
    ValueBoolean cPacketUseEntity;
    ValueBoolean cPacketKeepAlive;
    ValueBoolean cPacketPlayer;
    ValueBoolean cPacketVehicleMove;
    ValueBoolean cPacketSteerBoat;
    ValueBoolean cPacketPlaceRecipe;
    ValueBoolean cPacketPlayerAbilities;
    ValueBoolean cPacketPlayerDigging;
    ValueBoolean cPacketEntityAction;
    ValueBoolean cPacketInput;
    ValueBoolean cPacketRecipeInfo;
    ValueBoolean cPacketResourcePackStatus;
    ValueBoolean cPacketSeenAdvancements;
    ValueBoolean CPacketHeldItemChange;
    ValueBoolean cPacketCreativeInventoryAction;
    ValueBoolean cPacketUpdateSign;
    ValueBoolean cPacketAnimation;
    ValueBoolean cPacketSpectate;
    ValueBoolean cPacketPlayerTryUseItemOnBlock;
    ValueBoolean cPacketPlayerTryUseItem;
    
    public ModulePacketCanceler() {
        this.sPacketSpawnObject = new ValueBoolean("SPacketSpawnObject", "SPacketSpawnObject", "", false);
        this.sPacketSpawnExperienceOrb = new ValueBoolean("SPacketSpawnExperienceOrb", "SPacketSpawnExperienceOrb", "", false);
        this.sPacketSpawnGlobalEntity = new ValueBoolean("SPacketSpawnGlobalEntity", "SPacketSpawnGlobalEntity", "", false);
        this.sPacketSpawnMob = new ValueBoolean("SPacketSpawnMob", "SPacketSpawnMob", "", false);
        this.sPacketSpawnPainting = new ValueBoolean("SPacketSpawnPainting", "SPacketSpawnPainting", "", false);
        this.sPacketSpawnPlayer = new ValueBoolean("SPacketSpawnPlayer", "SPacketSpawnPlayer", "", false);
        this.sPacketAnimation = new ValueBoolean("SPacketAnimation", "SPacketAnimation", "", false);
        this.sPacketStatistics = new ValueBoolean("SPacketStatistics", "SPacketStatistics", "", false);
        this.sPacketBlockBreakAnim = new ValueBoolean("SPacketBlockBreakAnim", "SPacketBlockBreakAnim", "", false);
        this.sPacketUpdateTileEntity = new ValueBoolean("SPacketUpdateTileEntity", "SPacketUpdateTileEntity", "", false);
        this.sPacketBlockAction = new ValueBoolean("SPacketBlockAction", "SPacketBlockAction", "", false);
        this.sPacketBlockChange = new ValueBoolean("SPacketBlockChange", "SPacketBlockChange", "", false);
        this.sPacketUpdateBossInfo = new ValueBoolean("SPacketUpdateBossInfo", "SPacketUpdateBossInfo", "", false);
        this.sPacketServerDifficulty = new ValueBoolean("SPacketServerDifficulty", "SPacketServerDifficulty", "", false);
        this.sPacketTabComplete = new ValueBoolean("SPacketTabComplete", "SPacketTabComplete", "", false);
        this.sPacketChat = new ValueBoolean("SPacketChat", "SPacketChat", "", false);
        this.sPacketMultiBlockChange = new ValueBoolean("SPacketMultiBlockChange", "SPacketMultiBlockChange", "", false);
        this.sPacketConfirmTransaction = new ValueBoolean("SPacketConfirmTransaction", "SPacketConfirmTransaction", "", false);
        this.sPacketCloseWindow = new ValueBoolean("SPacketCloseWindow", "SPacketCloseWindow", "", false);
        this.sPacketOpenWindow = new ValueBoolean("SPacketOpenWindow", "SPacketOpenWindow", "", false);
        this.sPacketWindowItems = new ValueBoolean("SPacketWindowItems", "SPacketWindowItems", "", false);
        this.sPacketWindowProperty = new ValueBoolean("SPacketWindowProperty", "SPacketWindowProperty", "", false);
        this.sPacketSetSlot = new ValueBoolean("SPacketSetSlot", "SPacketSetSlot", "", false);
        this.sPacketCooldown = new ValueBoolean("SPacketCooldown", "SPacketCooldown", "", false);
        this.sPacketCustomPayload = new ValueBoolean("SPacketCustomPayload", "SPacketCustomPayload", "", false);
        this.sPacketCustomSound = new ValueBoolean("SPacketCustomSound", "SPacketCustomSound", "", false);
        this.sPacketDisconnect = new ValueBoolean("SPacketDisconnect", "SPacketDisconnect", "", false);
        this.sPacketEntityStatus = new ValueBoolean("SPacketEntityStatus", "SPacketEntityStatus", "", false);
        this.sPacketExplosion = new ValueBoolean("SPacketExplosion", "SPacketExplosion", "", false);
        this.sPacketUnloadChunk = new ValueBoolean("SPacketUnloadChunk", "SPacketUnloadChunk", "", false);
        this.sPacketChangeGameState = new ValueBoolean("SPacketChangeGameState", "SPacketChangeGameState", "", false);
        this.sPacketKeepAlive = new ValueBoolean("SPacketKeepAlive", "SPacketKeepAlive", "", false);
        this.sPacketChunkData = new ValueBoolean("SPacketChunkData", "SPacketChunkData", "", false);
        this.sPacketEffect = new ValueBoolean("SPacketEffect", "SPacketEffect", "", false);
        this.sPacketParticles = new ValueBoolean("SPacketParticles", "SPacketParticles", "", false);
        this.sPacketJoinGame = new ValueBoolean("SPacketJoinGame", "SPacketJoinGame", "", false);
        this.sPacketMaps = new ValueBoolean("SPacketMaps", "SPacketMaps", "", false);
        this.sPacketEntity = new ValueBoolean("SPacketEntity", "SPacketEntity", "", false);
        this.sPacketMoveVehicle = new ValueBoolean("SPacketMoveVehicle", "SPacketMoveVehicle", "", false);
        this.sPacketSignEditorOpen = new ValueBoolean("SPacketSignEditorOpen", "SPacketSignEditorOpen", "", false);
        this.sPacketPlaceGhostRecipe = new ValueBoolean("SPacketPlaceGhostRecipe", "SPacketPlaceGhostRecipe", "", false);
        this.sPacketPlayerAbilities = new ValueBoolean("SPacketPlayerAbilities", "SPacketPlayerAbilities", "", false);
        this.sPacketCombatEvent = new ValueBoolean("SPacketCombatEvent", "SPacketCombatEvent", "", false);
        this.sPacketPlayerListItem = new ValueBoolean("SPacketPlayerListItem", "SPacketPlayerListItem", "", false);
        this.sPacketPlayerPosLook = new ValueBoolean("SPacketPlayerPosLook", "SPacketPlayerPosLook", "", false);
        this.sPacketUseBed = new ValueBoolean("SPacketUseBed", "SPacketUseBed", "", false);
        this.sPacketRecipeBook = new ValueBoolean("SPacketRecipeBook", "SPacketRecipeBook", "", false);
        this.sPacketDestroyEntities = new ValueBoolean("SPacketDestroyEntities", "SPacketDestroyEntities", "", false);
        this.sPacketRemoveEntityEffect = new ValueBoolean("SPacketRemoveEntityEffect", "SPacketRemoveEntityEffect", "", false);
        this.sPacketResourcePackSend = new ValueBoolean("SPacketResourcePackSend", "SPacketResourcePackSend", "", false);
        this.sPacketRespawn = new ValueBoolean("SPacketRespawn", "SPacketRespawn", "", false);
        this.sPacketEntityHeadLook = new ValueBoolean("SPacketEntityHeadLook", "SPacketEntityHeadLook", "", false);
        this.sPacketSelectAdvancementsTab = new ValueBoolean("SPacketSelectAdvancementsTab", "SPacketSelectAdvancementsTab", "", false);
        this.sPacketWorldBorder = new ValueBoolean("SPacketWorldBorder", "SPacketWorldBorder", "", false);
        this.sPacketCamera = new ValueBoolean("SPacketCamera", "SPacketCamera", "", false);
        this.sPacketHeldItemChange = new ValueBoolean("SPacketHeldItemChange", "SPacketHeldItemChange", "", false);
        this.sPacketDisplayObjective = new ValueBoolean("SPacketDisplayObjective", "SPacketDisplayObjective", "", false);
        this.sPacketEntityMetadata = new ValueBoolean("SPacketEntityMetadata", "SPacketEntityMetadata", "", false);
        this.sPacketEntityAttach = new ValueBoolean("SPacketEntityAttach", "SPacketEntityAttach", "", false);
        this.sPacketEntityVelocity = new ValueBoolean("SPacketEntityVelocity", "SPacketEntityVelocity", "", false);
        this.sPacketEntityEquipment = new ValueBoolean("SPacketEntityEquipment", "SPacketEntityEquipment", "", false);
        this.sPacketSetExperience = new ValueBoolean("SPacketSetExperience", "SPacketSetExperience", "", false);
        this.sPacketUpdateHealth = new ValueBoolean("SPacketUpdateHealth", "SPacketUpdateHealth", "", false);
        this.sPacketScoreboardObjective = new ValueBoolean("SPacketScoreboardObjective", "SPacketScoreboardObjective", "", false);
        this.sPacketSetPassengers = new ValueBoolean("SPacketSetPassengers", "SPacketSetPassengers", "", false);
        this.sPacketTeams = new ValueBoolean("SPacketTeams", "SPacketTeams", "", false);
        this.sPacketUpdateScore = new ValueBoolean("SPacketUpdateScore", "SPacketUpdateScore", "", false);
        this.sPacketSpawnPosition = new ValueBoolean("SPacketSpawnPosition", "SPacketSpawnPosition", "", false);
        this.sPacketTimeUpdate = new ValueBoolean("SPacketTimeUpdate", "SPacketTimeUpdate", "", false);
        this.sPacketTitle = new ValueBoolean("SPacketTitle", "SPacketTitle", "", false);
        this.sPacketSoundEffect = new ValueBoolean("SPacketSoundEffect", "SPacketSoundEffect", "", false);
        this.sPacketPlayerListHeaderFooter = new ValueBoolean("SPacketPlayerListHeaderFooter", "SPacketPlayerListHeaderFooter", "", false);
        this.sPacketCollectItem = new ValueBoolean("SPacketCollectItem", "SPacketCollectItem", "", false);
        this.sPacketEntityTeleport = new ValueBoolean("SPacketEntityTeleport", "SPacketEntityTeleport", "", false);
        this.sPacketAdvancementInfo = new ValueBoolean("SPacketAdvancementInfo", "SPacketAdvancementInfo", "", false);
        this.sPacketEntityProperties = new ValueBoolean("SPacketEntityProperties", "SPacketEntityProperties", "", false);
        this.sPacketEntityEffect = new ValueBoolean("SPacketEntityEffect", "SPacketEntityEffect", "", false);
        this.cPacketConfirmTeleport = new ValueBoolean("CPacketConfirmTeleport", "CPacketConfirmTeleport", "", false);
        this.cPacketTabComplete = new ValueBoolean("CPacketTabComplete", "CPacketTabComplete", "", false);
        this.cPacketChatMessage = new ValueBoolean("CPacketChatMessage", "CPacketChatMessage", "", false);
        this.cPacketClientStatus = new ValueBoolean("CPacketClientStatus", "CPacketClientStatus", "", false);
        this.cPacketClientSettings = new ValueBoolean("CPacketClientSettings", "CPacketClientSettings", "", false);
        this.cPacketConfirmTransaction = new ValueBoolean("CPacketConfirmTransaction", "CPacketConfirmTransaction", "", false);
        this.cPacketEnchantItem = new ValueBoolean("CPacketEnchantItem", "CPacketEnchantItem", "", false);
        this.cPacketClickWindow = new ValueBoolean("CPacketClickWindow", "CPacketClickWindow", "", false);
        this.cPacketCloseWindow = new ValueBoolean("CPacketCloseWindow", "CPacketCloseWindow", "", false);
        this.cPacketCustomPayload = new ValueBoolean("CPacketCustomPayload", "CPacketCustomPayload", "", false);
        this.cPacketUseEntity = new ValueBoolean("CPacketUseEntity", "CPacketUseEntity", "", false);
        this.cPacketKeepAlive = new ValueBoolean("CPacketKeepAlive", "CPacketKeepAlive", "", false);
        this.cPacketPlayer = new ValueBoolean("CPacketPlayer", "CPacketPlayer", "", false);
        this.cPacketVehicleMove = new ValueBoolean("CPacketVehicleMove", "CPacketVehicleMove", "", false);
        this.cPacketSteerBoat = new ValueBoolean("CPacketSteerBoat", "CPacketSteerBoat", "", false);
        this.cPacketPlaceRecipe = new ValueBoolean("CPacketPlaceRecipe", "CPacketPlaceRecipe", "", false);
        this.cPacketPlayerAbilities = new ValueBoolean("CPacketPlayerAbilities", "CPacketPlayerAbilities", "", false);
        this.cPacketPlayerDigging = new ValueBoolean("CPacketPlayerDigging", "CPacketPlayerDigging", "", false);
        this.cPacketEntityAction = new ValueBoolean("CPacketEntityAction", "CPacketEntityAction", "", false);
        this.cPacketInput = new ValueBoolean("CPacketInput", "CPacketInput", "", false);
        this.cPacketRecipeInfo = new ValueBoolean("CPacketRecipeInfo", "CPacketRecipeInfo", "", false);
        this.cPacketResourcePackStatus = new ValueBoolean("CPacketResourcePackStatus", "CPacketResourcePackStatus", "", false);
        this.cPacketSeenAdvancements = new ValueBoolean("CPacketSeenAdvancements", "CPacketSeenAdvancements", "", false);
        this.CPacketHeldItemChange = new ValueBoolean("CPacketHeldItemChange", "CPacketHeldItemChange", "", false);
        this.cPacketCreativeInventoryAction = new ValueBoolean("CPacketCreativeInventoryAction", "CPacketCreativeInventoryAction", "", false);
        this.cPacketUpdateSign = new ValueBoolean("CPacketUpdateSign", "CPacketUpdateSign", "", false);
        this.cPacketAnimation = new ValueBoolean("CPacketAnimation", "CPacketAnimation", "", false);
        this.cPacketSpectate = new ValueBoolean("CPacketSpectate", "CPacketSpectate", "", false);
        this.cPacketPlayerTryUseItemOnBlock = new ValueBoolean("CPacketPlayerTryUseItemOnBlock", "CPacketPlayerTryUseItemOnBlock", "", false);
        this.cPacketPlayerTryUseItem = new ValueBoolean("CPacketPlayerTryUseItem", "CPacketPlayerTryUseItem", "", false);
    }
    
    @SubscribeEvent
    public void onReceive(final EventPacketReceive event) {
    }
    
    @SubscribeEvent
    public void onSend(final EventPacketSend event) {
        if (event.getPacket() instanceof CPacketConfirmTeleport && this.cPacketConfirmTeleport.getValue()) {
            event.setCancelled(true);
        }
        if (event.getPacket() instanceof CPacketTabComplete && this.cPacketTabComplete.getValue()) {
            event.setCancelled(true);
        }
        if (event.getPacket() instanceof CPacketChatMessage && this.cPacketChatMessage.getValue()) {
            event.setCancelled(true);
        }
        if (event.getPacket() instanceof CPacketClientStatus && this.cPacketClientStatus.getValue()) {
            event.setCancelled(true);
        }
        if (event.getPacket() instanceof CPacketClientSettings && this.cPacketClientSettings.getValue()) {
            event.setCancelled(true);
        }
        if (event.getPacket() instanceof CPacketConfirmTransaction && this.cPacketConfirmTransaction.getValue()) {
            event.setCancelled(true);
        }
        if (event.getPacket() instanceof CPacketEnchantItem && this.cPacketEnchantItem.getValue()) {
            event.setCancelled(true);
        }
        if (event.getPacket() instanceof CPacketClickWindow && this.cPacketClickWindow.getValue()) {
            event.setCancelled(true);
        }
        if (event.getPacket() instanceof CPacketCloseWindow && this.cPacketCloseWindow.getValue()) {
            event.setCancelled(true);
        }
        if (event.getPacket() instanceof CPacketCustomPayload && this.cPacketCustomPayload.getValue()) {
            event.setCancelled(true);
        }
        if (event.getPacket() instanceof CPacketUseEntity && this.cPacketUseEntity.getValue()) {
            event.setCancelled(true);
        }
        if (event.getPacket() instanceof CPacketKeepAlive && this.cPacketKeepAlive.getValue()) {
            event.setCancelled(true);
        }
        if (event.getPacket() instanceof CPacketPlayer && this.cPacketPlayer.getValue()) {
            event.setCancelled(true);
        }
        if (event.getPacket() instanceof CPacketVehicleMove && this.cPacketVehicleMove.getValue()) {
            event.setCancelled(true);
        }
        if (event.getPacket() instanceof CPacketSteerBoat && this.cPacketSteerBoat.getValue()) {
            event.setCancelled(true);
        }
        if (event.getPacket() instanceof CPacketPlaceRecipe && this.cPacketPlaceRecipe.getValue()) {
            event.setCancelled(true);
        }
        if (event.getPacket() instanceof CPacketPlayerAbilities && this.cPacketPlayerAbilities.getValue()) {
            event.setCancelled(true);
        }
        if (event.getPacket() instanceof CPacketPlayerDigging && this.cPacketPlayerDigging.getValue()) {
            event.setCancelled(true);
        }
        if (event.getPacket() instanceof CPacketEntityAction && this.cPacketEntityAction.getValue()) {
            event.setCancelled(true);
        }
        if (event.getPacket() instanceof CPacketInput && this.cPacketInput.getValue()) {
            event.setCancelled(true);
        }
        if (event.getPacket() instanceof CPacketRecipeInfo && this.cPacketRecipeInfo.getValue()) {
            event.setCancelled(true);
        }
        if (event.getPacket() instanceof CPacketResourcePackStatus && this.cPacketResourcePackStatus.getValue()) {
            event.setCancelled(true);
        }
        if (event.getPacket() instanceof CPacketSeenAdvancements && this.cPacketSeenAdvancements.getValue()) {
            event.setCancelled(true);
        }
        if (event.getPacket() instanceof CPacketHeldItemChange && this.CPacketHeldItemChange.getValue()) {
            event.setCancelled(true);
        }
        if (event.getPacket() instanceof CPacketCreativeInventoryAction && this.cPacketCreativeInventoryAction.getValue()) {
            event.setCancelled(true);
        }
        if (event.getPacket() instanceof CPacketUpdateSign && this.cPacketUpdateSign.getValue()) {
            event.setCancelled(true);
        }
        if (event.getPacket() instanceof CPacketAnimation && this.cPacketAnimation.getValue()) {
            event.setCancelled(true);
        }
        if (event.getPacket() instanceof CPacketSpectate && this.cPacketSpectate.getValue()) {
            event.setCancelled(true);
        }
        if (event.getPacket() instanceof CPacketPlayerTryUseItemOnBlock && this.cPacketPlayerTryUseItemOnBlock.getValue()) {
            event.setCancelled(true);
        }
        if (event.getPacket() instanceof CPacketPlayerTryUseItem && this.cPacketPlayerTryUseItem.getValue()) {
            event.setCancelled(true);
        }
    }
}
