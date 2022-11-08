package dev.evangelion.client.gui.particles;

import dev.evangelion.api.utilities.MathUtils;
import dev.evangelion.client.modules.client.ModuleParticles;
import org.lwjgl.opengl.Display;
import javax.vecmath.Vector2f;
import java.util.Random;

public class Particle
{
    private static final Random random;
    private Vector2f velocity;
    private Vector2f pos;
    private float size;
    private float alpha;
    
    public Particle(final Vector2f velocity, final float x, final float y, final float size) {
        this.velocity = velocity;
        this.pos = new Vector2f(x, y);
        this.size = size;
    }
    
    public static Particle generateParticle() {
        final Vector2f velocity = new Vector2f((float)(Math.random() * 2.0 - 1.0), (float)(Math.random() * 2.0 - 1.0));
        final float x = (float)Particle.random.nextInt(Display.getWidth());
        final float y = (float)Particle.random.nextInt(Display.getHeight());
        return new Particle(velocity, x, y, ModuleParticles.INSTANCE.size.getValue().floatValue());
    }
    
    public float getAlpha() {
        return this.alpha;
    }
    
    public Vector2f getVelocity() {
        return this.velocity;
    }
    
    public void setVelocity(final Vector2f velocity) {
        this.velocity = velocity;
    }
    
    public float getX() {
        return this.pos.getX();
    }
    
    public void setX(final float x) {
        this.pos.setX(x);
    }
    
    public float getY() {
        return this.pos.getY();
    }
    
    public void setY(final float y) {
        this.pos.setY(y);
    }
    
    public float getSize() {
        return this.size;
    }
    
    public void setSize(final float size) {
        this.size = size;
    }
    
    public void tick(final int delta, final float speed) {
        final Vector2f pos = this.pos;
        pos.x += this.velocity.getX() * delta * speed;
        final Vector2f pos2 = this.pos;
        pos2.y += this.velocity.getY() * delta * speed;
        if (this.alpha < 255.0f) {
            this.alpha += 0.05f * delta;
        }
        if (this.pos.getX() > Display.getWidth()) {
            this.pos.setX(0.0f);
        }
        if (this.pos.getX() < 0.0f) {
            this.pos.setX((float)Display.getWidth());
        }
        if (this.pos.getY() > Display.getHeight()) {
            this.pos.setY(0.0f);
        }
        if (this.pos.getY() < 0.0f) {
            this.pos.setY((float)Display.getHeight());
        }
    }
    
    public float getDistanceTo(final Particle particle1) {
        return this.getDistanceTo(particle1.getX(), particle1.getY());
    }
    
    public float getDistanceTo(final float x, final float y) {
        return (float)MathUtils.distance(this.getX(), this.getY(), x, y);
    }
    
    static {
        random = new Random();
    }
}
