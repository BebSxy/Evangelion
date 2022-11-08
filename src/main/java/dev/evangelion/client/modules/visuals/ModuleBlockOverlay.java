package dev.evangelion.client.modules.visuals;

import dev.evangelion.api.utilities.RenderUtils;
import net.minecraft.util.math.RayTraceResult;
import dev.evangelion.client.events.EventRender3D;
import java.awt.Color;
import dev.evangelion.client.values.impl.ValueColor;
import dev.evangelion.client.values.impl.ValueNumber;
import dev.evangelion.client.values.impl.ValueEnum;
import dev.evangelion.api.manager.module.RegisterModule;
import dev.evangelion.api.manager.module.Module;

@RegisterModule(name = "BlockOverlay", tag = "Block Overlay", description = "Renders an overlay on top of the block you are currently aiming at.", category = Module.Category.VISUALS)
public class ModuleBlockOverlay extends Module
{
    private final ValueEnum fill;
    private final ValueEnum outline;
    private final ValueNumber width;
    private final ValueColor fillColor;
    private final ValueColor outlineColor;
    
    public ModuleBlockOverlay() {
        this.fill = new ValueEnum("Fill", "Fill", "The mode for filling in the position.", Renders.Normal);
        this.outline = new ValueEnum("Outline", "Outline", "The mode for outlining the position.", Renders.Normal);
        this.width = new ValueNumber("Width", "Width", "The width for the Outline.", 1.0f, 0.1f, 5.0f);
        this.fillColor = new ValueColor("FillColor", "Fill Color", "The color for the Filling.", new Color(0, 0, 255, 100));
        this.outlineColor = new ValueColor("OutlineColor", "Outline Color", "The color for the Outlining.", new Color(0, 0, 255));
    }
    
    @Override
    public void onRender3D(final EventRender3D event) {
        super.onRender3D(event);
        if (ModuleBlockOverlay.mc.objectMouseOver != null && ModuleBlockOverlay.mc.objectMouseOver.typeOfHit == RayTraceResult.Type.BLOCK) {
            if (this.fill.getValue().equals(Renders.Normal)) {
                RenderUtils.drawBlock(ModuleBlockOverlay.mc.objectMouseOver.getBlockPos(), this.fillColor.getValue());
            }
            if (this.outline.getValue().equals(Renders.Normal)) {
                RenderUtils.drawBlockOutline(ModuleBlockOverlay.mc.objectMouseOver.getBlockPos(), this.outlineColor.getValue(), this.width.getValue().floatValue());
            }
        }
    }
    
    public enum Renders
    {
        None, 
        Normal;
    }
}
