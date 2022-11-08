package dev.evangelion.client.modules.visuals;

import com.google.common.collect.Maps;
import dev.evangelion.api.manager.module.Module;
import dev.evangelion.api.manager.module.RegisterModule;
import dev.evangelion.api.utilities.IMinecraft;
import dev.evangelion.api.utilities.RenderUtils;
import dev.evangelion.api.utilities.TimerUtils;
import dev.evangelion.client.events.EventRender2D;
import dev.evangelion.client.events.EventRender3D;
import dev.evangelion.client.values.impl.ValueBoolean;
import dev.evangelion.client.values.impl.ValueCategory;
import dev.evangelion.client.values.impl.ValueColor;
import dev.evangelion.client.values.impl.ValueNumber;
import java.awt.Color;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.Map;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

@RegisterModule(name="Arrows", description="Show where entities are when off-screen.", category=Module.Category.VISUALS)
public class ModuleArrows
        extends Module {
    ValueBoolean onScreen = new ValueBoolean("OnScreen", "On Screen", "", false);
    ValueNumber size = new ValueNumber("Size", "Size", "", 10.0, 5.0, 20.0);
    ValueNumber radius = new ValueNumber("Radius", "Radius", "", 45, 10, 200);
    ValueBoolean outline = new ValueBoolean("Outline", "Outline", "", true);
    ValueColor color = new ValueColor("Color", "Color", "", new Color(255, 0, 0, 170));
    ValueCategory pulses = new ValueCategory("Pulse", "");
    ValueBoolean pulse = new ValueBoolean("Pulse", "Pulse", "", this.pulses, false);
    ValueNumber pulseMax = new ValueNumber("PulseMax", "Pulse Max", "", this.pulses, 255, 0, 255);
    ValueNumber pulseMin = new ValueNumber("PulseMin", "Pulse Min", "", this.pulses, 100, 0, 255);
    private EntityListener entityListener = new EntityListener();
    private int alpha;
    private int pulseAlpha = 1;
    private boolean isReversing = false;
    private TimerUtils timer = new TimerUtils();
    private int maxvalue;
    private int minvalue;

    @Override
    public void onRender3D(EventRender3D event) {
        super.onRender3D(event);
        this.entityListener.render3d();
    }

    @Override
    public void onRender2D(EventRender2D event) {
        super.onRender2D(event);
        this.maxvalue = this.pulseMax.getValue().intValue();
        this.minvalue = this.pulseMin.getValue().intValue();
        if (this.pulseAlpha < this.minvalue) {
            this.pulseAlpha = this.minvalue;
        }
        if (this.timer.hasTimeElapsed(1L)) {
            this.pulseAlpha += this.isReversing ? -1 : 1;
            if (this.isReversing && this.pulseAlpha <= this.minvalue) {
                this.isReversing = false;
            } else if (!this.isReversing && this.pulseAlpha >= this.maxvalue) {
                this.isReversing = true;
            }
            this.timer.reset();
        }
        this.alpha = this.pulse.getValue() ? this.pulseAlpha : this.color.getValue().getAlpha();
        ModuleArrows.mc.world.loadedEntityList.forEach(o -> {
            if (o instanceof EntityLivingBase && this.isValid((EntityLivingBase)o)) {
                EntityLivingBase entity = (EntityLivingBase)o;
                Vec3d pos = this.entityListener.getEntityLowerBounds().get((Object)entity);
                if (pos != null && (!this.isOnScreen(pos) || this.onScreen.getValue())) {
                    int x = Display.getWidth() / 2 / (ModuleArrows.mc.gameSettings.guiScale == 0 ? 1 : ModuleArrows.mc.gameSettings.guiScale);
                    int y = Display.getHeight() / 2 / (ModuleArrows.mc.gameSettings.guiScale == 0 ? 1 : ModuleArrows.mc.gameSettings.guiScale);
                    float yaw = this.getRotations(entity) - ModuleArrows.mc.player.rotationYaw;
                    GL11.glTranslatef((float)x, (float)y, (float)0.0f);
                    GL11.glRotatef((float)yaw, (float)0.0f, (float)0.0f, (float)1.0f);
                    GL11.glTranslatef((float)(-x), (float)(-y), (float)0.0f);
                    RenderUtils.drawTracerPointer(x, y - this.radius.getValue().intValue(), this.size.getValue().floatValue(), 2.0f, 1.0f, new Color(this.color.getValue().getRed(), this.color.getValue().getGreen(), this.color.getValue().getBlue(), this.alpha), this.outline.getValue());
                    GL11.glTranslatef((float)x, (float)y, (float)0.0f);
                    GL11.glRotatef((float)(-yaw), (float)0.0f, (float)0.0f, (float)1.0f);
                    GL11.glTranslatef((float)(-x), (float)(-y), (float)0.0f);
                }
            }
        });
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    private boolean isOnScreen(Vec3d pos) {
        if (!(pos.x > -1.0)) return false;
        if (!(pos.z < 1.0)) return false;
        int n = ModuleArrows.mc.gameSettings.guiScale == 0 ? 1 : ModuleArrows.mc.gameSettings.guiScale;
        if (!(pos.x / (double)n >= 0.0)) return false;
        int n2 = ModuleArrows.mc.gameSettings.guiScale == 0 ? 1 : ModuleArrows.mc.gameSettings.guiScale;
        if (!(pos.x / (double)n2 <= (double)Display.getWidth())) return false;
        int n3 = ModuleArrows.mc.gameSettings.guiScale == 0 ? 1 : ModuleArrows.mc.gameSettings.guiScale;
        if (!(pos.y / (double)n3 >= 0.0)) return false;
        int n4 = ModuleArrows.mc.gameSettings.guiScale == 0 ? 1 : ModuleArrows.mc.gameSettings.guiScale;
        if (!(pos.y / (double)n4 <= (double)Display.getHeight())) return false;
        return true;
    }

    private boolean isValid(EntityLivingBase entity) {
        return entity != ModuleArrows.mc.player && this.isValidType(entity) && entity.getEntityId() != -1488 && entity.isEntityAlive();
    }

    private boolean isValidType(EntityLivingBase entity) {
        return entity instanceof EntityPlayer;
    }

    private float getRotations(EntityLivingBase ent) {
        double x = ent.posX - ModuleArrows.mc.player.posX;
        double z = ent.posZ - ModuleArrows.mc.player.posZ;
        float yaw = (float)(-(Math.atan2(x, z) * 57.29577951308232));
        return yaw;
    }

    public static Vec3d to2D(double x, double y, double z) {
        FloatBuffer screenCoords = BufferUtils.createFloatBuffer((int)3);
        IntBuffer viewport = BufferUtils.createIntBuffer((int)16);
        FloatBuffer modelView = BufferUtils.createFloatBuffer((int)16);
        FloatBuffer projection = BufferUtils.createFloatBuffer((int)16);
        GL11.glGetFloat((int)2982, (FloatBuffer)modelView);
        GL11.glGetFloat((int)2983, (FloatBuffer)projection);
        GL11.glGetInteger((int)2978, (IntBuffer)viewport);
        boolean result = GLU.gluProject((float)((float)x), (float)((float)y), (float)((float)z), (FloatBuffer)modelView, (FloatBuffer)projection, (IntBuffer)viewport, (FloatBuffer)screenCoords);
        if (result) {
            return new Vec3d((double)screenCoords.get(0), (double)((float)Display.getHeight() - screenCoords.get(1)), (double)screenCoords.get(2));
        }
        return null;
    }

    public class EntityListener {
        private Map<Entity, Vec3d> entityUpperBounds = Maps.newHashMap();
        private Map<Entity, Vec3d> entityLowerBounds = Maps.newHashMap();

        private void render3d() {
            if (!this.entityUpperBounds.isEmpty()) {
                this.entityUpperBounds.clear();
            }
            if (!this.entityLowerBounds.isEmpty()) {
                this.entityLowerBounds.clear();
            }
            for (Entity e : IMinecraft.mc.world.loadedEntityList) {
                Vec3d bound = this.getEntityRenderPosition(e);
                bound.add(new Vec3d(0.0, (double)e.height + 0.2, 0.0));
                Vec3d upperBounds = ModuleArrows.to2D(bound.x, bound.y, bound.z);
                Vec3d lowerBounds = ModuleArrows.to2D(bound.x, bound.y - 2.0, bound.z);
                if (upperBounds == null || lowerBounds == null) continue;
                this.entityUpperBounds.put(e, upperBounds);
                this.entityLowerBounds.put(e, lowerBounds);
            }
        }

        private Vec3d getEntityRenderPosition(Entity entity) {
            double partial = IMinecraft.mc.timer.renderPartialTicks;
            double x = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * partial - IMinecraft.mc.getRenderManager().viewerPosX;
            double y = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * partial - IMinecraft.mc.getRenderManager().viewerPosY;
            double z = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * partial - IMinecraft.mc.getRenderManager().viewerPosZ;
            return new Vec3d(x, y, z);
        }

        public Map<Entity, Vec3d> getEntityLowerBounds() {
            return this.entityLowerBounds;
        }
    }
}

