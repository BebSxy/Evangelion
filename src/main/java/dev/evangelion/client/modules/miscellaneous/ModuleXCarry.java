package dev.evangelion.client.modules.miscellaneous;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.network.play.client.CPacketCloseWindow;
import dev.evangelion.client.events.EventPacketSend;
import dev.evangelion.api.manager.module.RegisterModule;
import dev.evangelion.api.manager.module.Module;

@RegisterModule(name = "XCarry", tag = "X Carry", description = "Use the crafting slots as inventory space.", category = Module.Category.MISCELLANEOUS)
public class ModuleXCarry extends Module
{
    @SubscribeEvent
    public void onSend(final EventPacketSend event) {
        if (event.getPacket() instanceof CPacketCloseWindow && ((CPacketCloseWindow)event.getPacket()).windowId == ModuleXCarry.mc.player.inventoryContainer.windowId) {
            event.setCancelled(true);
        }
    }
}
