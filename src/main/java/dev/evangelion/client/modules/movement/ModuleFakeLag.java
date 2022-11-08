package dev.evangelion.client.modules.movement;

import dev.evangelion.api.utilities.RenderUtils;
import net.minecraft.util.math.MathHelper;
import dev.evangelion.api.utilities.MathUtils;
import net.minecraft.client.gui.ScaledResolution;
import dev.evangelion.client.events.EventRender2D;
import net.minecraft.network.Packet;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import dev.evangelion.client.events.EventPacketSend;
import java.util.LinkedList;
import java.awt.Color;
import dev.evangelion.api.utilities.TimerUtils;
import net.minecraft.network.play.client.CPacketPlayer;
import java.util.Queue;
import dev.evangelion.client.values.impl.ValueColor;
import dev.evangelion.client.values.impl.ValueBoolean;
import dev.evangelion.client.values.impl.ValueNumber;
import dev.evangelion.api.manager.module.RegisterModule;
import dev.evangelion.api.manager.module.Module;

@RegisterModule(name = "FakeLag", tag = "Fake Lag", description = "Let's you move without actually moving, and then teleport you to the spot that you have moved to.", category = Module.Category.MOVEMENT)
public class ModuleFakeLag extends Module
{
    ValueNumber delay;
    ValueBoolean indicator;
    ValueColor color;
    private final Queue<CPacketPlayer> packets;
    private final TimerUtils timer;
    private boolean sending;
    
    public ModuleFakeLag() {
        this.delay = new ValueNumber("Delay", "Delay", "Delay for the fakelag.", 155, 10, 350);
        this.indicator = new ValueBoolean("Indicator", "Indicator", "Render an indicator that shows when the fakelag is executing", false);
        this.color = new ValueColor("Color", "Color", "", new Color(255, 255, 255));
        this.packets = new LinkedList<CPacketPlayer>();
        this.timer = new TimerUtils();
        this.sending = false;
    }
    
    @SubscribeEvent
    public void onPacket(final EventPacketSend event) {
        if (this.sending) {
            return;
        }
        if (this.isToggled() && event.getPacket() instanceof CPacketPlayer) {
            event.setCancelled(true);
            this.packets.add((CPacketPlayer)event.getPacket());
        }
    }
    
    @Override
    public void onUpdate() {
        if (this.timer.hasTimeElapsed(this.delay.getValue().intValue())) {
            while (!this.packets.isEmpty()) {
                this.sending = true;
                ModuleFakeLag.mc.player.connection.sendPacket((Packet)this.packets.poll());
            }
            this.sending = false;
            this.timer.reset();
        }
    }
    
    @Override
    public void onRender2D(final EventRender2D event) {
        if (this.nullCheck()) {
            return;
        }
        final ScaledResolution resolution = new ScaledResolution(ModuleFakeLag.mc);
        float expand = MathUtils.normalize((float)this.timer.timeElapsed(), 0.0f, this.delay.getValue().floatValue());
        expand = MathHelper.clamp(expand, 0.0f, 1.0f);
        final float width = resolution.getScaledWidth() / 2.0f;
        final float height = resolution.getScaledHeight() / 2.0f;
        if (this.indicator.getValue()) {
            RenderUtils.drawRect(width - 15.0f, height + 20.0f, width - 15.0f + ((this.packets.size() > 1) ? (expand * 30.0f) : 0.0f), height + 25.0f, this.color.getValue());
            RenderUtils.drawOutline(width - 15.0f, height + 20.0f, width + 15.0f, height + 25.0f, 0.5f, Color.BLACK);
        }
    }
    
    @Override
    public void onEnable() {
        if (ModuleFakeLag.mc.player != null) {
            this.timer.reset();
        }
    }
    
    @Override
    public void onDisable() {
        while (!this.packets.isEmpty()) {
            ModuleFakeLag.mc.player.connection.sendPacket((Packet)this.packets.poll());
        }
    }
    
    @Override
    public String getHudInfo() {
        return this.packets.size() + "";
    }
}
