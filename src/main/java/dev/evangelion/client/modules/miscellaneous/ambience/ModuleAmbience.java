package dev.evangelion.client.modules.miscellaneous.ambience;

import net.minecraft.network.play.server.SPacketTimeUpdate;
import dev.evangelion.client.events.EventPacketReceive;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import java.awt.Color;
import dev.evangelion.client.values.impl.ValueEnum;
import dev.evangelion.client.values.impl.ValueColor;
import dev.evangelion.client.values.impl.ValueBoolean;
import dev.evangelion.client.values.impl.ValueCategory;
import dev.evangelion.client.values.impl.ValueNumber;
import dev.evangelion.api.manager.module.RegisterModule;
import dev.evangelion.api.manager.module.Module;

@RegisterModule(name = "Ambience", tag = "Ambience", description = "Change the time of the world.", category = Module.Category.MISCELLANEOUS)
public class ModuleAmbience extends Module
{
    public static ModuleAmbience INSTANCE;
    ValueNumber ambienceTime;
    ValueCategory skyCategory;
    ValueBoolean skyModifier;
    ValueColor skyColor;
    ValueCategory weatherCategory;
    public ValueEnum weatherMode;
    public ValueNumber height;
    public ValueNumber strength;
    private long oldTime;
    
    public ModuleAmbience() {
        this.ambienceTime = new ValueNumber("AmbienceTime", "Time", "What time of the day the world will be.", 24000, 0, 24000);
        this.skyCategory = new ValueCategory("SkyCategory", "Sky category.");
        this.skyModifier = new ValueBoolean("SkyModifier", "Sky Modifier", "Choose whether or not to modify the worlds sky.", this.skyCategory, false);
        this.skyColor = new ValueColor("SkyColor", "Color", "", this.skyCategory, new Color(57, 243, 178));
        this.weatherCategory = new ValueCategory("WeatherCategory", "Weather category.");
        this.weatherMode = new ValueEnum("WeatherMode", "Weather", "Mode for the worlds weather.", this.weatherCategory, WeatherModes.None);
        this.height = new ValueNumber("Height", "Height", "Height of the weather.", this.weatherCategory, 80, 0, 255);
        this.strength = new ValueNumber("Strength", "Strength", "Strength of the weather.", this.weatherCategory, 0.8f, 0.1f, 2.0f);
        ModuleAmbience.INSTANCE = this;
    }
    
    @SubscribeEvent
    public void onEntityRender(final EntityViewRenderEvent.FogColors event) {
        if (this.skyModifier.getValue()) {
            event.setRed(this.skyColor.getValue().getRed() / 255.0f);
            event.setGreen(this.skyColor.getValue().getGreen() / 255.0f);
            event.setBlue(this.skyColor.getValue().getBlue() / 255.0f);
        }
    }
    
    @SubscribeEvent
    public void onReceive(final EventPacketReceive event) {
        if (event.getPacket() instanceof SPacketTimeUpdate) {
            event.setCancelled(true);
        }
    }
    
    @Override
    public void onEnable() {
        if (ModuleAmbience.mc.world == null) {
            return;
        }
        this.oldTime = ModuleAmbience.mc.world.getWorldTime();
    }
    
    @Override
    public void onUpdate() {
        ModuleAmbience.mc.world.setWorldTime((long)this.ambienceTime.getValue().intValue());
    }
    
    @Override
    public void onDisable() {
        ModuleAmbience.mc.world.setWorldTime(this.oldTime);
    }
    
    @Override
    public String getHudInfo() {
        return this.ambienceTime.getValue().intValue() + "";
    }
    
    public enum WeatherModes
    {
        Rain, 
        Snow, 
        None;
    }
}
