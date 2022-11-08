package dev.evangelion.client.modules.visuals;

import dev.evangelion.api.utilities.RenderUtils;
import net.minecraft.client.gui.ScaledResolution;
import dev.evangelion.client.events.EventRender2D;
import net.minecraftforge.client.GuiIngameForge;
import java.awt.Color;
import dev.evangelion.client.values.impl.ValueColor;
import dev.evangelion.client.values.impl.ValueNumber;
import dev.evangelion.client.values.impl.ValueBoolean;
import dev.evangelion.client.values.impl.ValueCategory;
import dev.evangelion.api.manager.module.RegisterModule;
import dev.evangelion.api.manager.module.Module;

@RegisterModule(name = "Crosshair", tag = "Crosshair", description = "Change how the default minecraft crosshair looks.", category = Module.Category.VISUALS)
public class ModuleCrosshair extends Module
{
    private final ValueCategory crosshairCategory;
    private final ValueBoolean dynamic;
    private final ValueBoolean attackIndicator;
    private final ValueNumber length;
    private final ValueNumber partWidth;
    private final ValueNumber gap;
    private final ValueCategory fillCategory;
    private final ValueBoolean fill;
    private final ValueColor fillColor;
    private final ValueCategory outlineCategory;
    private final ValueBoolean outline;
    private final ValueNumber outlineWidth;
    private final ValueColor outlineColor;
    
    public ModuleCrosshair() {
        this.crosshairCategory = new ValueCategory("Crosshair", "The category for the configuration of the crosshair.");
        this.dynamic = new ValueBoolean("Dynamic", "Dynamic", "Makes it so that the crosshair expands when holding down keys.", this.crosshairCategory, true);
        this.attackIndicator = new ValueBoolean("AttackIndicator", "Attack Indicator", "Renders an attack indicator below the crosshair to show you your combat delay.", this.crosshairCategory, true);
        this.length = new ValueNumber("Length", "Length", "The length of the parts of the crosshair.", this.crosshairCategory, 10.0f, 0.0f, 25.0f);
        this.partWidth = new ValueNumber("Width", "Width", "The width of the parts of the crosshair.", this.crosshairCategory, 2.5f, 0.0f, 25.0f);
        this.gap = new ValueNumber("Gap", "Gap", "The gap space on every part of the crosshair.", this.crosshairCategory, 6.1f, 0.0f, 25.0f);
        this.fillCategory = new ValueCategory("Fill", "The category for the filling of the crosshair parts.");
        this.fill = new ValueBoolean("Fill", "Fill", "Fills every crosshair part.", this.fillCategory, true);
        this.fillColor = new ValueColor("FillColor", "Color", "The color for the filling.", this.fillCategory, new Color(0, 0, 255));
        this.outlineCategory = new ValueCategory("Outline", "The category for outlining the crosshair parts.");
        this.outline = new ValueBoolean("Outline", "Outline", "Outlines every crosshair part.", this.outlineCategory, true);
        this.outlineWidth = new ValueNumber("OutlineWidth", "Width", "The width for the outlining.", this.outlineCategory, 1.0f, 0.0f, 5.0f);
        this.outlineColor = new ValueColor("OutlineColor", "Color", "The color for the outlining.", this.outlineCategory, new Color(0, 0, 0));
    }
    
    @Override
    public void onEnable() {
        GuiIngameForge.renderCrosshairs = false;
    }
    
    @Override
    public void onDisable() {
        GuiIngameForge.renderCrosshairs = true;
    }
    
    @Override
    public void onRender2D(final EventRender2D event) {
        final ScaledResolution resolution = new ScaledResolution(ModuleCrosshair.mc);
        final float width = resolution.getScaledWidth() / 2.0f;
        final float height = resolution.getScaledHeight() / 2.0f;
        if (this.fill.getValue()) {
            RenderUtils.drawRect(width - this.gap.getValue().floatValue() - this.length.getValue().floatValue() - (this.isMoving() ? 2.0f : 0.0f), height - this.partWidth.getValue().floatValue() / 2.0f, width - this.gap.getValue().floatValue() - this.length.getValue().floatValue() - (this.isMoving() ? 2.0f : 0.0f) + this.length.getValue().floatValue(), height - this.partWidth.getValue().floatValue() / 2.0f + this.partWidth.getValue().floatValue(), this.fillColor.getValue());
            RenderUtils.drawRect(width + this.gap.getValue().floatValue() + (this.isMoving() ? 2 : 0), height - this.partWidth.getValue().floatValue() / 2.0f, width + this.gap.getValue().floatValue() + (this.isMoving() ? 2 : 0) + this.length.getValue().floatValue(), height - this.partWidth.getValue().floatValue() / 2.0f + this.partWidth.getValue().floatValue(), this.fillColor.getValue());
            RenderUtils.drawRect(width - this.partWidth.getValue().floatValue() / 2.0f, height - this.gap.getValue().floatValue() - this.length.getValue().floatValue() - (this.isMoving() ? 2 : 0), width - this.partWidth.getValue().floatValue() / 2.0f + this.partWidth.getValue().floatValue(), height - this.gap.getValue().floatValue() - this.length.getValue().floatValue() - (this.isMoving() ? 2 : 0) + this.length.getValue().floatValue(), this.fillColor.getValue());
            RenderUtils.drawRect(width - this.partWidth.getValue().floatValue() / 2.0f, height + this.gap.getValue().floatValue() + (this.isMoving() ? 2 : 0), width - this.partWidth.getValue().floatValue() / 2.0f + this.partWidth.getValue().floatValue(), height + this.gap.getValue().floatValue() + (this.isMoving() ? 2 : 0) + this.length.getValue().floatValue(), this.fillColor.getValue());
            if (this.attackIndicator.getValue() && ModuleCrosshair.mc.player.getCooledAttackStrength(0.0f) < 1.0f) {
                RenderUtils.drawRect(width - 10.0f, height + this.gap.getValue().floatValue() + this.length.getValue().floatValue() + (this.isMoving() ? 2 : 0) + 2.0f, width - 10.0f + ModuleCrosshair.mc.player.getCooledAttackStrength(0.0f) * 20.0f, height + this.gap.getValue().floatValue() + this.length.getValue().floatValue() + (this.isMoving() ? 2 : 0) + 2.0f + 2.0f, this.fillColor.getValue());
            }
        }
        if (this.outline.getValue()) {
            RenderUtils.drawOutline(width - this.gap.getValue().floatValue() - this.length.getValue().floatValue() - (this.isMoving() ? 2.0f : 0.0f), height - this.partWidth.getValue().floatValue() / 2.0f, width - this.gap.getValue().floatValue() - this.length.getValue().floatValue() - (this.isMoving() ? 2.0f : 0.0f) + this.length.getValue().floatValue(), height - this.partWidth.getValue().floatValue() / 2.0f + this.partWidth.getValue().floatValue(), this.outlineWidth.getValue().floatValue(), this.outlineColor.getValue());
            RenderUtils.drawOutline(width + this.gap.getValue().floatValue() + (this.isMoving() ? 2 : 0), height - this.partWidth.getValue().floatValue() / 2.0f, width + this.gap.getValue().floatValue() + (this.isMoving() ? 2 : 0) + this.length.getValue().floatValue(), height - this.partWidth.getValue().floatValue() / 2.0f + this.partWidth.getValue().floatValue(), this.outlineWidth.getValue().floatValue(), this.outlineColor.getValue());
            RenderUtils.drawOutline(width - this.partWidth.getValue().floatValue() / 2.0f, height - this.gap.getValue().floatValue() - this.length.getValue().floatValue() - (this.isMoving() ? 2 : 0), width - this.partWidth.getValue().floatValue() / 2.0f + this.partWidth.getValue().floatValue(), height - this.gap.getValue().floatValue() - this.length.getValue().floatValue() - (this.isMoving() ? 2 : 0) + this.length.getValue().floatValue(), this.outlineWidth.getValue().floatValue(), this.outlineColor.getValue());
            RenderUtils.drawOutline(width - this.partWidth.getValue().floatValue() / 2.0f, height + this.gap.getValue().floatValue() + (this.isMoving() ? 2 : 0), width - this.partWidth.getValue().floatValue() / 2.0f + this.partWidth.getValue().floatValue(), height + this.gap.getValue().floatValue() + (this.isMoving() ? 2 : 0) + this.length.getValue().floatValue(), this.outlineWidth.getValue().floatValue(), this.outlineColor.getValue());
            if (this.attackIndicator.getValue() && ModuleCrosshair.mc.player.getCooledAttackStrength(0.0f) < 1.0f) {
                RenderUtils.drawOutline(width - 10.0f, height + this.gap.getValue().floatValue() + this.length.getValue().floatValue() + (this.isMoving() ? 2 : 0) + 2.0f, width - 10.0f + ModuleCrosshair.mc.player.getCooledAttackStrength(0.0f) * 20.0f, height + this.gap.getValue().floatValue() + this.length.getValue().floatValue() + (this.isMoving() ? 2 : 0) + 2.0f + 2.0f, this.outlineWidth.getValue().floatValue(), this.outlineColor.getValue());
            }
        }
    }
    
    public boolean isMoving() {
        return (ModuleCrosshair.mc.player.isSneaking() || ModuleCrosshair.mc.player.moveStrafing != 0.0f || ModuleCrosshair.mc.player.moveForward != 0.0f || !ModuleCrosshair.mc.player.onGround) && this.dynamic.getValue();
    }
}
