package dev.evangelion.client.modules.miscellaneous;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.util.EnumFacing;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import dev.evangelion.client.events.EventPacketSend;
import dev.evangelion.api.manager.module.RegisterModule;
import dev.evangelion.api.manager.module.Module;

@RegisterModule(name = "BuildHeight", tag = "Build Height", description = "Makes placing at build height easier.", category = Module.Category.MISCELLANEOUS)
public class ModuleBuildHeight extends Module
{
    @SubscribeEvent
    public void onPacketSend(final EventPacketSend event) {
        if (event.getPacket() instanceof CPacketPlayerTryUseItemOnBlock) {
            final CPacketPlayerTryUseItemOnBlock packet = (CPacketPlayerTryUseItemOnBlock)event.getPacket();
            if (packet.getPos().getY() >= 255 && packet.getDirection() == EnumFacing.UP) {
                packet.placedBlockDirection = EnumFacing.DOWN;
            }
        }
    }
}
