package dev.evangelion.client.gui.particles;

import dev.evangelion.client.modules.client.ModuleParticles;
import org.lwjgl.opengl.GL11;
import dev.evangelion.api.utilities.RenderUtils;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.List;

public class ParticleSystem
{
    public static float SPEED;
    private List<Particle> particleList;
    public int dist;
    
    public ParticleSystem(final int initAmount, final int dist) {
        this.particleList = new ArrayList<Particle>();
        this.addParticles(initAmount);
        this.dist = dist;
    }
    
    public void addParticles(final int amount) {
        for (int i = 0; i < amount; ++i) {
            this.particleList.add(Particle.generateParticle());
        }
    }
    
    public void changeParticles(final int amount) {
        this.particleList.clear();
        for (int i = 0; i < amount; ++i) {
            this.particleList.add(Particle.generateParticle());
        }
    }
    
    public void tick(final int delta) {
        for (final Particle particle : this.particleList) {
            particle.tick(delta, ParticleSystem.SPEED);
        }
    }
    
    public void render() {
        for (final Particle particle : this.particleList) {
            for (final Particle particle2 : this.particleList) {
                final float distance = particle.getDistanceTo(particle2);
                if (particle.getDistanceTo(particle2) < this.dist) {
                    final float alpha = Math.min(1.0f, Math.min(1.0f, 1.0f - distance / this.dist));
                    RenderUtils.prepare();
                    GL11.glEnable(2848);
                    GL11.glDisable(3553);
                    GL11.glLineWidth(ModuleParticles.INSTANCE.lineWidth.getValue().floatValue());
                    GL11.glBegin(1);
                    GL11.glColor4f(ModuleParticles.INSTANCE.color.getValue().getRed() / 255.0f, ModuleParticles.INSTANCE.color.getValue().getGreen() / 255.0f, ModuleParticles.INSTANCE.color.getValue().getBlue() / 255.0f, alpha);
                    GL11.glVertex2d((double)particle.getX(), (double)particle.getY());
                    GL11.glVertex2d((double)particle2.getX(), (double)particle2.getY());
                    GL11.glEnd();
                    GL11.glEnable(3553);
                    GL11.glDisable(2848);
                    RenderUtils.release();
                }
            }
            RenderUtils.drawCircle(particle.getX(), particle.getY(), ModuleParticles.INSTANCE.size.getValue().floatValue(), ModuleParticles.INSTANCE.color.getValue());
        }
    }
    
    static {
        ParticleSystem.SPEED = 0.1f;
    }
}
