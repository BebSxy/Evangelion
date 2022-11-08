package dev.evangelion.client.modules.movement;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.network.play.server.SPacketPlayerPosLook;
import dev.evangelion.client.events.EventPacketReceive;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayer;
import dev.evangelion.api.utilities.TimerUtils;
import dev.evangelion.client.values.impl.ValueNumber;
import dev.evangelion.client.values.impl.ValueEnum;
import dev.evangelion.api.manager.module.RegisterModule;
import dev.evangelion.api.manager.module.Module;

@RegisterModule(name = "Boost", description = "Makes you go very fast for a short period of time.", category = Module.Category.MOVEMENT)
public class ModuleBoost extends Module
{
    private final ValueEnum finisher;
    private final ValueNumber toggleDelay;
    private final ValueNumber minimumSpeed;
    private final ValueNumber maximumSpeed;
    private final TimerUtils timer;
    private float timerSpeed;
    
    public ModuleBoost() {
        this.finisher = new ValueEnum("Finisher", "Finisher", "The finisher for the Boost.", Finishers.Punch);
        this.toggleDelay = new ValueNumber("ToggleDelay", "Toggle Delay", "The delay that the blink should automatically toggle off when reached.", 5.0f, 1.0f, 20.0f);
        this.minimumSpeed = new ValueNumber("MinimumSpeeed", "Minimum Speed", "The minimum speed for the player.", 1.0f, 1.0f, 10.0f);
        this.maximumSpeed = new ValueNumber("MaximumSpeed", "Maximum Speed", "The maximum speed for the player.", 1.2f, 1.0f, 10.0f);
        this.timer = new TimerUtils();
    }
    
    @Override
    public void onTick() {
        super.onTick();
        if (this.timer.hasTimeElapsed(this.toggleDelay.getValue().longValue() * 100L)) {
            if (this.finisher.getValue().equals(Finishers.Ground)) {
                ModuleBoost.mc.player.connection.sendPacket((Packet)new CPacketPlayer(true));
            }
            else if (this.finisher.getValue().equals(Finishers.Punch)) {
                ModuleBoost.mc.player.connection.sendPacket((Packet)new CPacketPlayerDigging(CPacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
                ModuleBoost.mc.player.connection.sendPacket((Packet)new CPacketPlayerTryUseItemOnBlock(new BlockPos(-1, -1, -1), EnumFacing.DOWN, EnumHand.MAIN_HAND, 0.0f, 0.0f, 0.0f));
                ModuleBoost.mc.player.connection.sendPacket((Packet)new CPacketPlayerDigging(CPacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
            }
            this.disable(true);
        }
    }
    
    @Override
    public void onUpdate() {
        super.onUpdate();
        this.timerSpeed = Math.min(this.timerSpeed + 0.006f, this.maximumSpeed.getValue().floatValue());
        ModuleBoost.mc.timer.tickLength = 50.0f / this.timerSpeed;
    }
    
    @Override
    public void onEnable() {
        super.onEnable();
        if (ModuleBoost.mc.player == null || ModuleBoost.mc.world == null) {
            this.disable(false);
        }
        this.timerSpeed = this.minimumSpeed.getValue().floatValue();
        this.timer.reset();
    }
    
    @Override
    public void onDisable() {
        super.onDisable();
        if (ModuleBoost.mc.player == null || ModuleBoost.mc.world == null) {
            return;
        }
        ModuleBoost.mc.timer.tickLength = 50.0f;
    }
    
    @SubscribeEvent
    public void onPacketReceive(final EventPacketReceive event) {
        if (event.getPacket() instanceof SPacketPlayerPosLook) {
            this.disable(true);
        }
    }
    
    @Override
    public void onLogin() {
        super.onLogin();
        this.disable(false);
    }
    
    @Override
    public void onLogout() {
        super.onLogout();
        this.disable(false);
    }
    
    @Override
    public void onDeath() {
        super.onDeath();
        this.disable(true);
    }
    
    public enum Finishers
    {
        None, 
        Punch, 
        Ground;
    }
}
