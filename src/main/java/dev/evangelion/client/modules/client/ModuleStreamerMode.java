package dev.evangelion.client.modules.client;

import net.minecraft.scoreboard.Team;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.client.network.NetworkPlayerInfo;
import java.util.Iterator;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import dev.evangelion.api.utilities.ChatUtils;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.util.text.ChatType;
import net.minecraft.network.play.server.SPacketChat;
import dev.evangelion.client.events.EventPacketReceive;
import dev.evangelion.client.values.impl.ValueString;
import dev.evangelion.client.values.impl.ValueBoolean;
import dev.evangelion.api.manager.module.RegisterModule;
import dev.evangelion.api.manager.module.Module;

@RegisterModule(name = "StreamerMode", tag = "Streamer Mode", description = "Hide your name and other information.", category = Module.Category.CLIENT)
public class ModuleStreamerMode extends Module
{
    public static ValueBoolean hideYou;
    public static ValueString yourName;
    public static ValueBoolean hideF3;
    
    @SubscribeEvent
    public void onReceive(final EventPacketReceive event) {
        if (event.getPacket() instanceof SPacketChat) {
            final SPacketChat packet = (SPacketChat)event.getPacket();
            if (packet.getType() != ChatType.GAME_INFO && this.getChatNames(packet.getChatComponent().getFormattedText(), packet.getChatComponent().getUnformattedText())) {
                event.setCancelled(true);
            }
        }
    }
    
    private boolean getChatNames(final String message, final String unformatted) {
        String out = message;
        if (ModuleStreamerMode.hideYou.getValue()) {
            if (ModuleStreamerMode.mc.player == null) {
                return false;
            }
            out = out.replace(ModuleStreamerMode.mc.player.getName(), ModuleStreamerMode.yourName.getValue());
        }
        ChatUtils.sendRawMessage(out);
        return true;
    }
    
    @SubscribeEvent
    public void renderOverlayEvent(final RenderGameOverlayEvent.Text event) {
        if (FMLClientHandler.instance().getClient().player.capabilities.isCreativeMode) {
            return;
        }
        if (!ModuleStreamerMode.hideF3.getValue()) {
            return;
        }
        final Iterator<String> it = (Iterator<String>)event.getLeft().listIterator();
        while (it.hasNext()) {
            final String value = it.next();
            if ((value != null && value.startsWith("XYZ:")) || value.startsWith("Looking at:") || value.startsWith("Block:") || value.startsWith("Facing:")) {
                it.remove();
            }
        }
    }
    
    public static String getPlayerName(final NetworkPlayerInfo networkPlayerInfoIn) {
        String dname = (networkPlayerInfoIn.getDisplayName() != null) ? networkPlayerInfoIn.getDisplayName().getFormattedText() : ScorePlayerTeam.formatPlayerName((Team)networkPlayerInfoIn.getPlayerTeam(), networkPlayerInfoIn.getGameProfile().getName());
        dname = dname.replace(ModuleStreamerMode.mc.player.getName(), ModuleStreamerMode.yourName.getValue());
        return dname;
    }
    
    static {
        ModuleStreamerMode.hideYou = new ValueBoolean("HideIGN", "Hide IGN", "Hide your in game name.", false);
        ModuleStreamerMode.yourName = new ValueString("YourName", "Your Name", "The name that will be shown instead of your ign.", "You");
        ModuleStreamerMode.hideF3 = new ValueBoolean("HideF3", "HideF3", "", false);
    }
}
