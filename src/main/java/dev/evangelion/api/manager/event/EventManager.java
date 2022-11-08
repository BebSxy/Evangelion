package dev.evangelion.api.manager.event;

import org.json.simple.parser.ParseException;
import java.io.IOException;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.JSONArray;
import org.apache.commons.io.IOUtils;
import java.net.URL;
import dev.evangelion.client.events.EventDeath;
import java.util.Collection;
import java.util.ArrayList;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.entity.player.EntityPlayer;
import java.util.UUID;
import java.util.Iterator;
import dev.evangelion.client.events.EventPlayerLeave;
import net.minecraftforge.fml.common.eventhandler.Event;
import dev.evangelion.client.events.EventPlayerJoin;
import net.minecraft.network.play.server.SPacketPlayerListItem;
import dev.evangelion.client.events.EventPacketReceive;
import net.minecraftforge.common.MinecraftForge;
import com.google.common.collect.Maps;
import java.util.Map;
import dev.evangelion.api.utilities.IMinecraft;

public class EventManager implements IMinecraft
{
    private final Map<String, String> uuidNameCache = Maps.newConcurrentMap();

    public EventManager() {
        MinecraftForge.EVENT_BUS.register((Object)this);
    }

    @SubscribeEvent
    public void onReceive(EventPacketReceive event) {
        if (event.getPacket() instanceof SPacketPlayerListItem) {
            SPacketPlayerListItem packet = (SPacketPlayerListItem)event.getPacket();
            if (packet.getAction() == SPacketPlayerListItem.Action.ADD_PLAYER) {
                for (SPacketPlayerListItem.AddPlayerData playerData : packet.getEntries()) {
                    if (playerData.getProfile().getId() == EventManager.mc.session.getProfile().getId()) continue;
                    new Thread(() -> {
                        UUID id = playerData.getProfile().getId();
                        String name = this.resolveName(playerData.getProfile().getId().toString());
                        if (name != null && EventManager.mc.player != null && EventManager.mc.player.ticksExisted >= 1000) {
                            MinecraftForge.EVENT_BUS.post((Event)new EventPlayerJoin(name, id));
                        }
                    }).start();
                }
            }
            if (packet.getAction() == SPacketPlayerListItem.Action.REMOVE_PLAYER) {
                for (SPacketPlayerListItem.AddPlayerData playerData : packet.getEntries()) {
                    if (playerData.getProfile().getId() == EventManager.mc.session.getProfile().getId()) continue;
                    new Thread(() -> {
                        UUID id = playerData.getProfile().getId();
                        EntityPlayer entity = EventManager.mc.world.getPlayerEntityByUUID(id);
                        String name = this.resolveName(playerData.getProfile().getId().toString());
                        if (name != null && EventManager.mc.player != null && EventManager.mc.player.ticksExisted >= 1000) {
                            MinecraftForge.EVENT_BUS.post((Event)new EventPlayerLeave(name, id, entity));
                        }
                    }).start();
                }
            }
        }
    }

    @SubscribeEvent
    public void onTick(final TickEvent.ClientTickEvent event) {
        if (EventManager.mc.player == null || EventManager.mc.world == null) {
            return;
        }
        for (final EntityPlayer player : new ArrayList<EntityPlayer>(EventManager.mc.world.playerEntities)) {
            if (player.getHealth() <= 0.0f) {
                MinecraftForge.EVENT_BUS.post((Event)new EventDeath(player));
            }
        }
    }

    public String resolveName(String uuid) {
        if (this.uuidNameCache.containsKey(uuid = uuid.replace("-", ""))) {
            return this.uuidNameCache.get(uuid);
        }
        String url = "https://api.mojang.com/user/profiles/" + uuid + "/names";
        try {
            JSONObject latestName;
            JSONArray jsonArray;
            String nameJson = IOUtils.toString((URL)new URL(url));
            if (nameJson != null && nameJson.length() > 0 && (jsonArray = (JSONArray)JSONValue.parseWithException(nameJson)) != null && (latestName = (JSONObject)jsonArray.get(jsonArray.size() - 1)) != null) {
                return latestName.get("name").toString();
            }
        }
        catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
}
