package dev.evangelion.client.modules.visuals.esp;

import java.util.Iterator;
import dev.evangelion.api.utilities.RenderUtils;
import net.minecraft.util.math.AxisAlignedBB;
import org.lwjgl.opengl.GL11;
import net.minecraft.init.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import dev.evangelion.client.events.EventRender3D;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import dev.evangelion.client.events.EventPacketReceive;
import java.util.concurrent.ConcurrentHashMap;
import java.util.ArrayList;
import java.awt.Color;
import net.minecraft.util.math.BlockPos;
import java.util.Map;
import java.util.List;
import dev.evangelion.client.values.impl.ValueEnum;
import dev.evangelion.client.values.impl.ValueNumber;
import dev.evangelion.client.values.impl.ValueColor;
import dev.evangelion.client.values.impl.ValueCategory;
import dev.evangelion.client.values.impl.ValueBoolean;
import dev.evangelion.api.manager.module.RegisterModule;
import dev.evangelion.api.manager.module.Module;

@RegisterModule(name = "ESP", description = "Render entities.", category = Module.Category.VISUALS)
public class ModuleESP extends Module
{
    public static ModuleESP INSTANCE;
    public HoleESP holeESP;
    public EntityESP entityESP;
    public ItemESP itemESP;
    public SoundESP soundESP;
    public ValueBoolean players;
    public ValueBoolean mobs;
    public ValueBoolean animals;
    public ValueBoolean crystals;
    public ValueBoolean items;
    public ValueBoolean holes;
    public ValueBoolean sounds;
    public ValueCategory entitiesCategory;
    public ValueColor E_color;
    public ValueNumber E_width;
    public ValueCategory itemsCategory;
    public ValueColor I_color;
    public ValueBoolean I_outline;
    public ValueNumber I_width;
    public ValueCategory soundsCategory;
    public ValueNumber soundsScale;
    public ValueBoolean chorusOnly;
    public ValueNumber soundsFadeSpeed;
    public ValueCategory holesCategory;
    public ValueEnum H_mode;
    public ValueBoolean burrowESP;
    public ValueColor burrowColor;
    public ValueNumber H_height;
    public ValueNumber H_outlineWidth;
    public ValueBoolean H_doubles;
    public ValueNumber H_range;
    public ValueColor H_obsidian;
    public ValueColor H_obsidianOL;
    public ValueColor H_bedrock;
    public ValueColor H_bedrockOL;
    public ValueColor H_doubleColor;
    public ValueColor H_doubleOL;
    public List<HoleESP.HoleInfo> hole;
    public Map<BlockPos, Sound> soundPositions;
    
    public ModuleESP() {
        this.players = new ValueBoolean("Players", "Players", "", true);
        this.mobs = new ValueBoolean("Mobs", "Mobs", "", true);
        this.animals = new ValueBoolean("Animals", "Animals", "", true);
        this.crystals = new ValueBoolean("Crystals", "Crystals", "", true);
        this.items = new ValueBoolean("Items", "Items", "", true);
        this.holes = new ValueBoolean("Holes", "Holes", "", true);
        this.sounds = new ValueBoolean("Sounds", "Sounds", "", false);
        this.entitiesCategory = new ValueCategory("Entities", "Entities category.");
        this.E_color = new ValueColor("EntitiesColor", "Color", "Color of ESP for entiies.", this.entitiesCategory, new Color(0, 255, 0));
        this.E_width = new ValueNumber("EntitiesWidth", "Width", "Outline width of ESP.", this.entitiesCategory, 3.0, 0.5, 5.0);
        this.itemsCategory = new ValueCategory("Items", "Items category.");
        this.I_color = new ValueColor("ItemsColor", "Color", "Color of ESP for entiies.", this.itemsCategory, new Color(255, 162, 0));
        this.I_outline = new ValueBoolean("ItemsOutline", "Outline", "Render outline of ESP", this.itemsCategory, true);
        this.I_width = new ValueNumber("ItemsWidth", "Width", "Outline width of ESP.", this.itemsCategory, 3.0, 0.5, 5.0);
        this.soundsCategory = new ValueCategory("Sounds", "Sounds category.");
        this.soundsScale = new ValueNumber("SoundsScale", "Scale", "The scale of the sound nametags.", this.soundsCategory, 30, 10, 100);
        this.chorusOnly = new ValueBoolean("ChorusOnly", "Chorus Only", "Only render chorus sound.", this.soundsCategory, false);
        this.soundsFadeSpeed = new ValueNumber("SoundsFadeSpeed", "Fade Speed", "", this.soundsCategory, 200, 10, 500);
        this.holesCategory = new ValueCategory("Holes", "Holes category.");
        this.H_mode = new ValueEnum("Mode", "Mode", "", this.holesCategory, H_modes.Normal);
        this.burrowESP = new ValueBoolean("BurrowESP", "Burrow ESP", "", this.holesCategory, false);
        this.burrowColor = new ValueColor("BurrowColor", "Burrow Color", "", this.holesCategory, new Color(255, 255, 255, 100));
        this.H_height = new ValueNumber("MainHeight", "MainHeight", "", this.holesCategory, 1.0, -1.0, 3.0);
        this.H_outlineWidth = new ValueNumber("OutlineWidth", "OutlineWidth", "", this.holesCategory, 2.0, 0.5, 5.0);
        this.H_doubles = new ValueBoolean("Doubles", "Doubles", "", this.holesCategory, true);
        this.H_range = new ValueNumber("Range", "Range", "", this.holesCategory, 8, 0, 10);
        this.H_obsidian = new ValueColor("ObiColor", "ObiColor", "", this.holesCategory, new Color(255, 0, 0, 120));
        this.H_obsidianOL = new ValueColor("ObiOutlineColor", "ObiOutlineColor", "", this.holesCategory, new Color(255, 0, 0, 255));
        this.H_bedrock = new ValueColor("BrockColor", "BrockColor", "", this.holesCategory, new Color(0, 255, 0, 120));
        this.H_bedrockOL = new ValueColor("BrockOutlineColor", "BrockOutlineColor", "", this.holesCategory, new Color(0, 255, 0, 255));
        this.H_doubleColor = new ValueColor("DoubleColor", "DoubleColor", "", this.holesCategory, new Color(100, 0, 255, 120));
        this.H_doubleOL = new ValueColor("DoubleOutlineColor", "DoubleOutlineColor", "", this.holesCategory, new Color(100, 0, 255, 255));
        this.hole = new ArrayList<HoleESP.HoleInfo>();
        this.soundPositions = new ConcurrentHashMap<BlockPos, Sound>();
        ModuleESP.INSTANCE = this;
    }
    
    @Override
    public void onUpdate() {
        if (ModuleESP.mc.player == null || ModuleESP.mc.world == null) {
            return;
        }
        this.holeESP = new HoleESP();
        this.itemESP = new ItemESP();
        this.entityESP = new EntityESP();
        this.soundESP = new SoundESP();
        if (this.holeESP != null && this.holes.getValue()) {
            this.holeESP.updateHoles();
        }
        this.soundPositions.entrySet().removeIf(e -> System.currentTimeMillis() - e.getValue().starTime > ModuleESP.INSTANCE.soundsFadeSpeed.getValue().intValue() * 10);
    }
    
    @SubscribeEvent
    public void onReceive(final EventPacketReceive event) {
        if (this.soundESP != null && this.sounds.getValue()) {
            this.soundESP.receivePackets(event);
        }
    }
    
    @Override
    public void onRender3D(final EventRender3D event) {
        if (ModuleESP.mc.player == null || ModuleESP.mc.world == null) {
            return;
        }
        if (this.holeESP != null && this.holes.getValue()) {
            this.holeESP.renderHoles();
        }
        if (this.itemESP != null && this.items.getValue()) {
            this.itemESP.renderItems();
        }
        if (this.soundESP != null && this.sounds.getValue()) {
            this.soundESP.renderSounds();
        }
        if (this.burrowESP.getValue()) {
            for (final EntityPlayer player : ModuleESP.mc.world.playerEntities) {
                if (player != null && player != ModuleESP.mc.player && ModuleESP.mc.player.getDistance((Entity)player) <= 10.0f) {
                    final BlockPos playerPos = new BlockPos(Math.floor(player.posX), Math.floor(player.posY), Math.floor(player.posZ));
                    if (ModuleESP.mc.world.getBlockState(playerPos).getBlock() != Blocks.OBSIDIAN && ModuleESP.mc.world.getBlockState(playerPos).getBlock() != Blocks.ENDER_CHEST && ModuleESP.mc.world.getBlockState(playerPos).getBlock() != Blocks.BEDROCK) {
                        continue;
                    }
                    GL11.glPushMatrix();
                    final double x = player.prevPosX + (player.posX - player.prevPosX) * ModuleESP.mc.getRenderPartialTicks() - ModuleESP.mc.renderManager.renderPosX;
                    final double y = player.prevPosY + (player.posY - player.prevPosY) * ModuleESP.mc.getRenderPartialTicks() - ModuleESP.mc.renderManager.renderPosY;
                    final double z = player.prevPosZ + (player.posZ - player.prevPosZ) * ModuleESP.mc.getRenderPartialTicks() - ModuleESP.mc.renderManager.renderPosZ;
                    final AxisAlignedBB bb1 = player.getEntityBoundingBox();
                    final AxisAlignedBB bb2 = new AxisAlignedBB(bb1.minX - player.posX, bb1.minY - player.posY, bb1.minZ - player.posZ, bb1.maxX - player.posX, (bb1.maxY - player.posY) / 2.0, bb1.maxZ - player.posZ);
                    GL11.glTranslated(x, y, z);
                    RenderUtils.drawBlock(bb2, this.burrowColor.getValue());
                    GL11.glPopMatrix();
                }
            }
        }
    }
    
    public enum H_modes
    {
        Normal, 
        Glow;
    }
    
    public static class Sound
    {
        public String soundName;
        public BlockPos pos;
        public long starTime;
        
        public Sound(final BlockPos pos, final String soundName) {
            this.pos = pos;
            this.soundName = soundName;
            this.starTime = 0L;
        }
    }
}
