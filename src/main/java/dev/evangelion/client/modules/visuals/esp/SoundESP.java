package dev.evangelion.client.modules.visuals.esp;

import java.util.Iterator;
import dev.evangelion.api.utilities.RenderUtils;
import java.awt.Color;
import java.util.Map;
import net.minecraft.client.audio.SoundEventAccessor;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.init.SoundEvents;
import net.minecraft.network.play.server.SPacketSoundEffect;
import dev.evangelion.client.events.EventPacketReceive;
import dev.evangelion.api.utilities.IMinecraft;

public class SoundESP implements IMinecraft
{
    public void receivePackets(final EventPacketReceive event) {
        if (event.getPacket() instanceof SPacketSoundEffect) {
            final SPacketSoundEffect packet = (SPacketSoundEffect)event.getPacket();
            if (packet.getSound() != SoundEvents.ITEM_CHORUS_FRUIT_TELEPORT && packet.getSound() != SoundEvents.ENTITY_ENDERMEN_TELEPORT && ModuleESP.INSTANCE.chorusOnly.getValue()) {
                return;
            }
            final BlockPos pos = new BlockPos(packet.getX(), packet.getY(), packet.getZ());
            Object soundName = packet.getSound().getSoundName();
            soundName = SoundESP.mc.getSoundHandler().getAccessor((ResourceLocation)soundName);
            if (soundName != null && ((SoundEventAccessor)soundName).getSubtitle() != null) {
                ModuleESP.INSTANCE.soundPositions.put(pos, new ModuleESP.Sound(pos, ((SoundEventAccessor)soundName).getSubtitle().getUnformattedText()));
                ModuleESP.INSTANCE.soundPositions.get(pos).starTime = System.currentTimeMillis();
            }
        }
    }
    
    public void renderSounds() {
        for (final Map.Entry<BlockPos, ModuleESP.Sound> entry : ModuleESP.INSTANCE.soundPositions.entrySet()) {
            if (System.currentTimeMillis() - entry.getValue().starTime < ModuleESP.INSTANCE.soundsFadeSpeed.getValue().intValue() * 10) {
                final double x = entry.getValue().pos.getX() - SoundESP.mc.getRenderManager().renderPosX;
                final double y = entry.getValue().pos.getY() - 1.2f - SoundESP.mc.getRenderManager().renderPosY;
                final double z = entry.getValue().pos.getZ() - SoundESP.mc.getRenderManager().renderPosZ;
                RenderUtils.drawNametag(entry.getValue().soundName, x, y, z, ModuleESP.INSTANCE.soundsScale.getValue().floatValue() / 10000.0f, Color.WHITE);
            }
        }
    }
}
