package dev.evangelion.client.modules.client;

import net.minecraft.client.gui.inventory.GuiContainer;
import dev.evangelion.Evangelion;
import dev.evangelion.client.events.EventRender2D;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import dev.evangelion.client.values.Value;
import dev.evangelion.api.manager.event.Event;
import dev.evangelion.client.events.EventClient;
import java.awt.Color;
import dev.evangelion.client.gui.particles.ParticleSystem;
import dev.evangelion.client.values.impl.ValueNumber;
import dev.evangelion.client.values.impl.ValueColor;
import dev.evangelion.api.manager.module.RegisterModule;
import dev.evangelion.api.manager.module.Module;

@RegisterModule(name = "Particles", description = "Render particles in gui screens.", category = Module.Category.CLIENT)
public class ModuleParticles extends Module
{
    public static ModuleParticles INSTANCE;
    public ValueColor color;
    public ValueNumber size;
    public ValueNumber lineWidth;
    public ValueNumber amount;
    public ValueNumber radius;
    public ValueNumber speed;
    public ValueNumber delta;
    boolean updateParticles;
    private ParticleSystem ps;
    
    public ModuleParticles() {
        this.color = new ValueColor("ParticleColor", "Color", "", new Color(255, 255, 255));
        this.size = new ValueNumber("Size", "Size", "", 1.0f, 0.5f, 5.0f);
        this.lineWidth = new ValueNumber("LineWidth", "Line Width", "", 2.0, 1.0, 3.0);
        this.amount = new ValueNumber("Population", "Population", "", 100, 50, 400);
        this.radius = new ValueNumber("Radius", "Radius", "", 100, 50, 300);
        this.speed = new ValueNumber("Speed", "Speed", "", 0.1f, 0.1f, 10.0f);
        this.delta = new ValueNumber("Delta", "Delta", "", 1, 1, 10);
        this.updateParticles = false;
        ModuleParticles.INSTANCE = this;
    }
    
    @Override
    public void onEnable() {
        (this.ps = new ParticleSystem(this.amount.getValue().intValue(), this.radius.getValue().intValue())).tick(this.delta.getValue().intValue());
        this.ps.dist = this.radius.getValue().intValue();
        final ParticleSystem ps = this.ps;
        ParticleSystem.SPEED = (float)this.speed.getValue().doubleValue();
    }
    
    @SubscribeEvent
    public void onSettingChange(final EventClient event) {
        if (this.nullCheck()) {
            return;
        }
        final Value value = event.getValue();
        if (event.getStage() == Event.Stage.POST && value != null && value == this.amount) {
            this.updateParticles = true;
        }
    }
    
    @Override
    public void onUpdate() {
        super.onUpdate();
        if (this.nullCheck()) {
            return;
        }
        if (this.updateParticles) {
            this.ps.changeParticles(this.amount.getValue().intValue());
            this.updateParticles = false;
        }
        this.ps.tick(this.delta.getValue().intValue());
        this.ps.dist = this.radius.getValue().intValue();
        final ParticleSystem ps = this.ps;
        ParticleSystem.SPEED = (float)this.speed.getValue().doubleValue();
    }
    
    @Override
    public void onRender2D(final EventRender2D event) {
        if (ModuleParticles.mc.ingameGUI.getChatGUI().getChatOpen() || ModuleParticles.mc.currentScreen == Evangelion.CLICK_GUI || ModuleParticles.mc.currentScreen instanceof GuiContainer) {
            this.ps.render();
        }
    }
}
