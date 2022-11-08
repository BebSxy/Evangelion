package dev.evangelion.client.mixins.impl;

import org.spongepowered.asm.mixin.gen.Accessor;
import net.minecraft.client.renderer.EntityRenderer;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({ EntityRenderer.class })
public interface IEntityRenderer
{
    @Accessor("rendererUpdateCount")
    int getRendererUpdateCount();
    
    @Accessor("rainXCoords")
    float[] getRainXCoords();
    
    @Accessor("rainYCoords")
    float[] getRainYCoords();
}
