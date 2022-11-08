package dev.evangelion.client.modules.visuals;

import dev.evangelion.api.utilities.sounds.SoundInitializer;
import net.minecraft.client.gui.Gui;
import dev.evangelion.api.utilities.RenderUtils;
import org.lwjgl.opengl.GL11;
import net.minecraft.client.gui.ScaledResolution;
import dev.evangelion.client.events.EventRender2D;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import dev.evangelion.api.utilities.sounds.SoundUtils;
import java.awt.Color;
import dev.evangelion.api.utilities.TimerUtils;
import net.minecraft.util.ResourceLocation;
import java.io.File;
import dev.evangelion.client.values.impl.ValueString;
import dev.evangelion.client.values.impl.ValueEnum;
import dev.evangelion.client.values.impl.ValueColor;
import dev.evangelion.client.values.impl.ValueNumber;
import dev.evangelion.client.values.impl.ValueBoolean;
import dev.evangelion.api.manager.module.RegisterModule;
import dev.evangelion.api.manager.module.Module;

@RegisterModule(name = "Hitmarkers", description = "Render hitmarkers when hitting entities.", category = Module.Category.VISUALS)
public class ModuleHitmarkers extends Module
{
    ValueBoolean render;
    ValueNumber timeout;
    ValueBoolean ignoreCrystals;
    ValueColor color;
    ValueBoolean sound;
    ValueEnum soundMode;
    ValueString customSound;
    ValueNumber volume;
    private File hitSound;
    private ResourceLocation hitmarkerResource;
    TimerUtils timer;
    boolean renderMark;
    boolean playSound;
    
    public ModuleHitmarkers() {
        this.render = new ValueBoolean("Render", "Render", "Render a hitmarker when you hit people.", true);
        this.timeout = new ValueNumber("Timeout", "Timeout", "Timeout of how long hitmarkers last.", 1000, 0, 3000);
        this.ignoreCrystals = new ValueBoolean("IgnoreCrystals", "Ignore Crystals", "Dont do hitmarkers if you attack a crystal.", false);
        this.color = new ValueColor("Color", "Color", "", new Color(255, 255, 255, 255));
        this.sound = new ValueBoolean("Sound", "Sound", "Play a sound when you hit an entity;", true);
        this.soundMode = new ValueEnum("SoundMode", "Sound", "Mode of sound.", soundModes.ArenaSwitch);
        this.customSound = new ValueString("CustomSound", "Custom Sound", "Name of custom sound file to use for hitmarker.", "hitmarker.wav");
        this.volume = new ValueNumber("Volume", "Volume", "Volume of the sounds.", 50.0f, 0.0f, 50.0f);
        this.hitmarkerResource = new ResourceLocation("evangelion:hitmarker.png");
        this.timer = new TimerUtils();
        this.renderMark = false;
        this.playSound = false;
        this.hitSound = new File(ModuleHitmarkers.mc.gameDir + File.separator + "Evangelion" + File.separator + "Sounds" + File.separator + this.customSound.getValue());
    }
    
    @Override
    public void onUpdate() {
        this.hitSound = new File(ModuleHitmarkers.mc.gameDir + File.separator + "Evangelion" + File.separator + "Sounds" + File.separator + this.customSound.getValue());
        if (this.timer.hasTimeElapsed(this.timeout.getValue().intValue()) && this.renderMark) {
            this.timer.reset();
            this.renderMark = false;
        }
        if (this.playSound && this.sound.getValue()) {
            if (this.soundMode.getValue().equals(soundModes.Custom)) {
                SoundUtils.playSound(this.hitSound, -50.0f + this.volume.getValue().floatValue());
            }
            else {
                SoundUtils.playSound(this.hitmarkerSound(), -50.0f + this.volume.getValue().floatValue());
            }
            this.playSound = false;
        }
    }
    
    @SubscribeEvent
    public void onAttack(final AttackEntityEvent event) {
        if (!event.getEntity().equals((Object)ModuleHitmarkers.mc.player)) {
            return;
        }
        if (event.getTarget() instanceof EntityEnderCrystal && this.ignoreCrystals.getValue()) {
            return;
        }
        this.renderMark = true;
        this.playSound = true;
    }
    
    @Override
    public void onRender2D(final EventRender2D event) {
        if (this.renderMark && !this.timer.hasTimeElapsed(this.timeout.getValue().intValue()) && this.render.getValue()) {
            final ScaledResolution res = new ScaledResolution(ModuleHitmarkers.mc);
            this.renderMark(res.getScaledWidth() / 2 - 9, res.getScaledHeight() / 2 - 9);
        }
    }
    
    public void renderMark(final int x, final int y) {
        ModuleHitmarkers.mc.getTextureManager().bindTexture(this.hitmarkerResource);
        GL11.glPushMatrix();
        RenderUtils.glColor4f(this.color.getValue());
        Gui.drawScaledCustomSizeModalRect(x, y, 0.0f, 0.0f, 420, 420, 18, 18, 420.0f, 420.0f);
        GL11.glPopMatrix();
    }
    
    public File hitmarkerSound() {
        if (this.soundMode.getValue().equals(soundModes.ArenaSwitch)) {
            return SoundInitializer.arena_switch;
        }
        if (this.soundMode.getValue().equals(soundModes.COD)) {
            return SoundInitializer.cod_hitsound;
        }
        return SoundInitializer.metal_impact;
    }
    
    public enum soundModes
    {
        ArenaSwitch, 
        COD, 
        MetalImpact, 
        Custom;
    }
}
