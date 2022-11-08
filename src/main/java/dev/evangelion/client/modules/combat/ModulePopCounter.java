package dev.evangelion.client.modules.combat;

import java.util.Iterator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.entity.Entity;
import dev.evangelion.api.utilities.ChatUtils;
import dev.evangelion.client.modules.client.ModuleCommands;
import dev.evangelion.Evangelion;
import net.minecraft.world.World;
import net.minecraft.network.play.server.SPacketEntityStatus;
import dev.evangelion.client.events.EventPacketReceive;
import java.util.HashMap;
import dev.evangelion.api.manager.module.RegisterModule;
import dev.evangelion.api.manager.module.Module;

@RegisterModule(name = "PopCounter", tag = "Pop Counter", description = "Keeps count of how any totems a player pops.", category = Module.Category.COMBAT)
public class ModulePopCounter extends Module
{
    public static final HashMap<String, Integer> popCount;
    
    @SubscribeEvent
    public void onReceive(final EventPacketReceive event) {
        if (this.nullCheck()) {
            return;
        }
        if (event.getPacket() instanceof SPacketEntityStatus) {
            final SPacketEntityStatus packet = (SPacketEntityStatus)event.getPacket();
            if (packet.getOpCode() == 35) {
                final Entity entity = packet.getEntity((World)ModulePopCounter.mc.world);
                if (entity == null) {
                    return;
                }
                int count = 1;
                if (ModulePopCounter.popCount.containsKey(entity.getName())) {
                    count = ModulePopCounter.popCount.get(entity.getName());
                    ModulePopCounter.popCount.put(entity.getName(), ++count);
                }
                else {
                    ModulePopCounter.popCount.put(entity.getName(), count);
                }
                if (entity == ModulePopCounter.mc.player) {
                    return;
                }
                if (Evangelion.FRIEND_MANAGER.isFriend(entity.getName())) {
                    ChatUtils.sendMessage(ModuleCommands.getFirstColor() + entity.getName() + " popped " + ModuleCommands.getSecondColor() + count + ModuleCommands.getFirstColor() + " totems! you should go help them", -6969420);
                }
                else {
                    ChatUtils.sendMessage(ModuleCommands.getFirstColor() + entity.getName() + " popped " + ModuleCommands.getSecondColor() + count + ModuleCommands.getFirstColor() + " totems!", -6969420);
                }
            }
        }
    }
    
    @Override
    public void onUpdate() {
        super.onUpdate();
        if (this.nullCheck()) {
            return;
        }
        for (final EntityPlayer player : ModulePopCounter.mc.world.playerEntities) {
            if (!ModulePopCounter.popCount.containsKey(player.getName())) {
                continue;
            }
            if (!player.isDead && player.getHealth() > 0.0f) {
                continue;
            }
            final int count = ModulePopCounter.popCount.get(player.getName());
            ModulePopCounter.popCount.remove(player.getName());
            if (player == ModulePopCounter.mc.player) {
                continue;
            }
            ChatUtils.sendMessage(ModuleCommands.getFirstColor() + player.getName() + " died after popping " + ModuleCommands.getSecondColor() + count + ModuleCommands.getFirstColor() + " totems!", -6969420);
        }
    }
    
    static {
        popCount = new HashMap<String, Integer>();
    }
}
