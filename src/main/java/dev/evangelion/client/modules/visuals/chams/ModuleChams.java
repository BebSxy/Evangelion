package dev.evangelion.client.modules.visuals.chams;

import net.minecraft.entity.Entity;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;
import dev.evangelion.client.events.EventRender3D;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import com.mojang.authlib.GameProfile;
import net.minecraft.world.World;
import net.minecraft.network.play.server.SPacketEntityStatus;
import dev.evangelion.client.events.EventPacketReceive;
import java.awt.Color;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import dev.evangelion.client.values.impl.ValueColor;
import dev.evangelion.client.values.impl.ValueNumber;
import dev.evangelion.client.values.impl.ValueEnum;
import dev.evangelion.client.values.impl.ValueCategory;
import dev.evangelion.client.values.impl.ValueBoolean;
import dev.evangelion.api.manager.module.RegisterModule;
import dev.evangelion.api.manager.module.Module;

@RegisterModule(name = "Chams", tag = "Chams", description = "Render entities.", category = Module.Category.VISUALS)
public class ModuleChams extends Module
{
    public static ModuleChams INSTANCE;
    public CrystalChams crystalChams;
    public EntityChams entityChams;
    public ValueBoolean players;
    public ValueBoolean mobs;
    public ValueBoolean animals;
    public ValueBoolean crystals;
    public ValueBoolean pops;
    public ValueCategory entityCategory;
    public ValueEnum E_mode;
    public ValueNumber E_width;
    public ValueBoolean E_texture;
    ValueBoolean E_shine;
    public ValueBoolean E_walls;
    public ValueColor E_visibleColor;
    public ValueColor E_hiddenColor;
    public ValueCategory crystalCategory;
    public ValueEnum C_mode;
    public ValueNumber C_width;
    public ValueBoolean C_texture;
    public ValueBoolean C_shine;
    public ValueBoolean C_walls;
    public ValueEnum C_changeScale;
    public ValueNumber C_scaleGlobal;
    public ValueNumber C_scaleX;
    public ValueNumber C_scaleY;
    public ValueNumber C_scaleZ;
    public ValueBoolean C_changeSpeed;
    public ValueNumber C_speed;
    public ValueBoolean C_changeBounce;
    public ValueNumber C_bounce;
    public ValueColor C_visibleColor;
    public ValueColor C_hiddenColor;
    ValueCategory popCategory;
    public ValueEnum P_mode;
    public ValueNumber P_width;
    public ValueBoolean P_angel;
    public ValueNumber P_angelSpeed;
    public ValueNumber P_fadeSpeed;
    public ValueColor P_color;
    public EntityOtherPlayerMP player;
    public EntityPlayer entity;
    long startTime;
    public float opacity;
    public float noob;
    public long time;
    public long duration;
    public float startAlpha;
    public float outlineOpacity;
    public float startOutlineAlpha;
    
    public ModuleChams() {
        this.players = new ValueBoolean("Players", "Players", "", true);
        this.mobs = new ValueBoolean("Mobs", "Mobs", "", true);
        this.animals = new ValueBoolean("Animals", "Animals", "", true);
        this.crystals = new ValueBoolean("Crystals", "Crystals", "", true);
        this.pops = new ValueBoolean("TotemPops", "Totem Pops", "", false);
        this.entityCategory = new ValueCategory("Entites", "Entities category.");
        this.E_mode = new ValueEnum("EntityMode", "Mode", "Mode for chams.", this.entityCategory, E_Modes.Chams);
        this.E_width = new ValueNumber("EntityWidth", "Width", "Width of lines mode.", this.entityCategory, 3.0, 0.5, 5.0);
        this.E_texture = new ValueBoolean("EntityTexture", "Texture", "Doesn't remove the vanilla crystal texture from rendering.", this.entityCategory, false);
        this.E_shine = new ValueBoolean("EntityShine", "Shine", "Makes an enchanting glint render on the crystal.", this.entityCategory, false);
        this.E_walls = new ValueBoolean("EntityWalls", "Walls", "", this.entityCategory, true);
        this.E_visibleColor = new ValueColor("EntityVisibleColor", "Visible", "The color for the outside layer.", this.entityCategory, new Color(0, 0, 255));
        this.E_hiddenColor = new ValueColor("EntityHiddenColor", "Hidden", "The color for the inside layer.", this.entityCategory, new Color(0, 0, 255));
        this.crystalCategory = new ValueCategory("Crystals", "Crystals category.");
        this.C_mode = new ValueEnum("CrystalMode", "Mode", "Mode for chams.", this.crystalCategory, C_Modes.Chams);
        this.C_width = new ValueNumber("CrystalWidth", "Width", "Width of lines mode.", this.crystalCategory, 3.0, 0.5, 5.0);
        this.C_texture = new ValueBoolean("CrystalTexture", "Texture", "Doesn't remove the vanilla crystal texture from rendering.", this.crystalCategory, false);
        this.C_shine = new ValueBoolean("CrystalShine", "Shine", "Makes an enchanting glint render on the entity.", this.crystalCategory, false);
        this.C_walls = new ValueBoolean("CrystalWalls", "Walls", "Renders the crystal through walls.", this.crystalCategory, true);
        this.C_changeScale = new ValueEnum("CrystalChangeScale", "Custom Scale", "Changes the scale.", this.crystalCategory, C_ScaleModes.None);
        this.C_scaleGlobal = new ValueNumber("CrystalScaleGlobal", "Global", "The global scale of the Crystal.", this.crystalCategory, 1.0f, 0.0f, 5.0f);
        this.C_scaleX = new ValueNumber("CrystalScaleX", "X", "The X scale of the Crystal.", this.crystalCategory, 1.0f, 0.0f, 5.0f);
        this.C_scaleY = new ValueNumber("CrystalScaleY", "Y", "The Y scale of the Crystal.", this.crystalCategory, 1.0f, 0.0f, 5.0f);
        this.C_scaleZ = new ValueNumber("CrystalScaleZ", "Z", "The Z scale of the Crystal.", this.crystalCategory, 1.0f, 0.0f, 5.0f);
        this.C_changeSpeed = new ValueBoolean("CrystalChangeSpeed", "Speed", "Changes the speed of the rotation.", this.crystalCategory, false);
        this.C_speed = new ValueNumber("CrystalSpeed", "Speed", "The rotation speed of the crystal.", this.crystalCategory, 3.0f, 0.0f, 30.0f);
        this.C_changeBounce = new ValueBoolean("CrystalChangeBounce", "Bounce", "Changes the bounce speed of the crystal.", this.crystalCategory, false);
        this.C_bounce = new ValueNumber("CrystalBounce", "Bounce", "The bounce speed of the crystal.", this.crystalCategory, 0.2f, 0.0f, 15.0f);
        this.C_visibleColor = new ValueColor("CrystalVisibleColor", "Visible", "The color for the outside layer.", this.crystalCategory, new Color(0, 0, 255));
        this.C_hiddenColor = new ValueColor("CrystalHiddenColor", "Hidden", "The color for the inside layer.", this.crystalCategory, new Color(0, 0, 255));
        this.popCategory = new ValueCategory("PopChams", "Pop Chams category.");
        this.P_mode = new ValueEnum("PopMode", "Mode", "Mode for chams.", this.popCategory, P_Modes.Chams);
        this.P_width = new ValueNumber("PopWidth", "Width", "Width of lines mode.", this.popCategory, 3.0, 0.5, 5.0);
        this.P_angel = new ValueBoolean("PopAngel", "Angel", "", this.popCategory, false);
        this.P_angelSpeed = new ValueNumber("PopAngelSpeed", "AngelSpeed", "", this.popCategory, 150, 10, 500);
        this.P_fadeSpeed = new ValueNumber("PopFadeSpeed", "FadeSpeed", "", this.popCategory, 200, 10, 500);
        this.P_color = new ValueColor("PopColor", "Color", "The color for the chams.", this.popCategory, new Color(0, 0, 255));
        ModuleChams.INSTANCE = this;
    }
    
    @Override
    public void onUpdate() {
        super.onUpdate();
        this.crystalChams = new CrystalChams();
        this.entityChams = new EntityChams();
    }
    
    @SubscribeEvent
    public void onReceive(final EventPacketReceive event) {
        if (this.nullCheck()) {
            return;
        }
        if (this.pops.getValue() && event.getPacket() instanceof SPacketEntityStatus) {
            final SPacketEntityStatus packet = (SPacketEntityStatus)event.getPacket();
            if (packet.getEntity((World)ModuleChams.mc.world) instanceof EntityPlayer) {
                this.entity = (EntityPlayer)packet.getEntity((World)ModuleChams.mc.world);
                if (packet.getOpCode() == 35 && this.entity != null && this.entity != ModuleChams.mc.player) {
                    final GameProfile profile = new GameProfile(ModuleChams.mc.player.getUniqueID(), "");
                    (this.player = new EntityOtherPlayerMP((World)ModuleChams.mc.world, profile)).copyLocationAndAnglesFrom(packet.getEntity((World)ModuleChams.mc.world));
                    this.player.rotationYaw = this.entity.rotationYaw;
                    this.player.rotationYawHead = this.entity.rotationYawHead;
                    this.player.rotationPitch = this.entity.rotationPitch;
                    this.player.prevRotationPitch = this.entity.prevRotationPitch;
                    this.player.prevRotationYaw = this.entity.prevRotationYaw;
                    this.player.renderYawOffset = this.entity.renderYawOffset;
                    this.player.limbSwing = this.entity.limbSwing;
                    this.player.limbSwingAmount = this.entity.limbSwingAmount;
                    this.player.prevLimbSwingAmount = this.entity.prevLimbSwingAmount;
                    this.startTime = System.currentTimeMillis();
                }
            }
        }
    }
    
    @Override
    public void onRender3D(final EventRender3D event) {
        if (this.nullCheck()) {
            return;
        }
        if (this.pops.getValue()) {
            this.opacity = 0.0f;
            this.noob = 0.0f;
            this.time = System.currentTimeMillis();
            this.duration = this.time - this.startTime;
            this.startAlpha = ModuleChams.INSTANCE.P_color.getValue().getAlpha() / 255.0f;
            this.startOutlineAlpha = 1.0f;
            if (this.player != null && this.entity != null) {
                if (this.duration < ModuleChams.INSTANCE.P_fadeSpeed.getValue().intValue() * 10) {
                    this.opacity = this.startAlpha - this.duration / (float)(ModuleChams.INSTANCE.P_fadeSpeed.getValue().intValue() * 10);
                    this.outlineOpacity = this.startOutlineAlpha - this.duration / (float)(ModuleChams.INSTANCE.P_fadeSpeed.getValue().intValue() * 10);
                    this.noob = 0.5f + this.duration / (float)(ModuleChams.INSTANCE.P_fadeSpeed.getValue().intValue() * 10);
                }
                if (this.duration < ModuleChams.INSTANCE.P_fadeSpeed.getValue().intValue() * 10) {
                    GL11.glPushMatrix();
                    if (ModuleChams.INSTANCE.P_angel.getValue()) {
                        GlStateManager.translate(0.0f, this.duration / (float)(ModuleChams.INSTANCE.P_angelSpeed.getValue().intValue() * 10), 0.0f);
                    }
                    ModuleChams.mc.renderManager.renderEntityStatic((Entity)this.player, 1.0f, false);
                    GlStateManager.translate(0.0f, 0.0f, 0.0f);
                    GL11.glPopMatrix();
                }
            }
        }
    }
    
    public enum E_Modes
    {
        Chams, 
        Lines, 
        Both;
    }
    
    public enum C_Modes
    {
        Chams, 
        Lines, 
        Both;
    }
    
    public enum C_ScaleModes
    {
        None, 
        Global, 
        Custom;
    }
    
    public enum P_Modes
    {
        Chams, 
        Lines, 
        Both;
    }
}
