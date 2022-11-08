package dev.evangelion.client.modules.miscellaneous.ambience;

import net.minecraft.world.biome.Biome;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.world.World;
import net.minecraft.entity.Entity;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.math.BlockPos;
import dev.evangelion.client.mixins.impl.IEntityRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.ResourceLocation;
import java.util.Random;
import dev.evangelion.api.utilities.IMinecraft;

public class Weather implements IMinecraft
{
    private static final Random RANDOM;
    private static final ResourceLocation RAIN_TEXTURES;
    private static final ResourceLocation SNOW_TEXTURES;
    
    public static void render(final float partialTicks) {
        final float f = ModuleAmbience.INSTANCE.strength.getValue().floatValue();
        final EntityRenderer renderer = Weather.mc.entityRenderer;
        renderer.enableLightmap();
        final Entity entity = Weather.mc.getRenderViewEntity();
        if (entity == null) {
            return;
        }
        final World world = (World)Weather.mc.world;
        final int i = MathHelper.floor(entity.posX);
        final int j = MathHelper.floor(entity.posY);
        final int k = MathHelper.floor(entity.posZ);
        final Tessellator tessellator = Tessellator.getInstance();
        final BufferBuilder bufferbuilder = tessellator.getBuffer();
        GlStateManager.disableCull();
        GlStateManager.glNormal3f(0.0f, 1.0f, 0.0f);
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.alphaFunc(516, 0.1f);
        final double d0 = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * partialTicks;
        final double d2 = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * partialTicks;
        final double d3 = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * partialTicks;
        final int l = MathHelper.floor(d2);
        int i2 = 5;
        if (Weather.mc.gameSettings.fancyGraphics) {
            i2 = 10;
        }
        int j2 = -1;
        final float f2 = ((IEntityRenderer)renderer).getRendererUpdateCount() + partialTicks;
        bufferbuilder.setTranslation(-d0, -d2, -d3);
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        final BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();
        for (int k2 = k - i2; k2 <= k + i2; ++k2) {
            for (int l2 = i - i2; l2 <= i + i2; ++l2) {
                final int i3 = (k2 - k + 16) * 32 + l2 - i + 16;
                final double d4 = ((IEntityRenderer)renderer).getRainXCoords()[i3] * 0.5;
                final double d5 = ((IEntityRenderer)renderer).getRainYCoords()[i3] * 0.5;
                blockpos$mutableblockpos.setPos(l2, 0, k2);
                final Biome biome = world.getBiome((BlockPos)blockpos$mutableblockpos);
                final int j3 = ModuleAmbience.INSTANCE.height.getValue().intValue();
                int k3 = j - i2;
                int l3 = j + i2;
                if (k3 < j3) {
                    k3 = j3;
                }
                if (l3 < j3) {
                    l3 = j3;
                }
                int i4;
                if ((i4 = j3) < l) {
                    i4 = l;
                }
                if (k3 != l3) {
                    Weather.RANDOM.setSeed(l2 * l2 * 3121 + l2 * 45238971 ^ k2 * k2 * 418711 + k2 * 13761);
                    blockpos$mutableblockpos.setPos(l2, k3, k2);
                    final float f3 = biome.getTemperature((BlockPos)blockpos$mutableblockpos);
                    if (ModuleAmbience.INSTANCE.weatherMode.getValue().equals(ModuleAmbience.WeatherModes.Rain)) {
                        if (j2 != 0) {
                            if (j2 >= 0) {
                                tessellator.draw();
                            }
                            j2 = 0;
                            Weather.mc.getTextureManager().bindTexture(Weather.RAIN_TEXTURES);
                            bufferbuilder.begin(7, DefaultVertexFormats.PARTICLE_POSITION_TEX_COLOR_LMAP);
                        }
                        final double d6 = -((((IEntityRenderer)renderer).getRendererUpdateCount() + l2 * l2 * 3121 + l2 * 45238971 + k2 * k2 * 418711 + k2 * 13761 & 0x1F) + (double)partialTicks) / 32.0 * (3.0 + Weather.RANDOM.nextDouble());
                        final double d7 = l2 + 0.5f - entity.posX;
                        final double d8 = k2 + 0.5f - entity.posZ;
                        final float f4 = MathHelper.sqrt(d7 * d7 + d8 * d8) / i2;
                        final float f5 = ((1.0f - f4 * f4) * 0.5f + 0.5f) * f;
                        blockpos$mutableblockpos.setPos(l2, i4, k2);
                        final int j4 = world.getCombinedLight((BlockPos)blockpos$mutableblockpos, 0);
                        final int k4 = j4 >> 16 & 0xFFFF;
                        final int l4 = j4 & 0xFFFF;
                        bufferbuilder.pos(l2 - d4 + 0.5, (double)l3, k2 - d5 + 0.5).tex(0.0, k3 * 0.25 + d6).color(1.0f, 1.0f, 1.0f, f5).lightmap(k4, l4).endVertex();
                        bufferbuilder.pos(l2 + d4 + 0.5, (double)l3, k2 + d5 + 0.5).tex(1.0, k3 * 0.25 + d6).color(1.0f, 1.0f, 1.0f, f5).lightmap(k4, l4).endVertex();
                        bufferbuilder.pos(l2 + d4 + 0.5, (double)k3, k2 + d5 + 0.5).tex(1.0, l3 * 0.25 + d6).color(1.0f, 1.0f, 1.0f, f5).lightmap(k4, l4).endVertex();
                        bufferbuilder.pos(l2 - d4 + 0.5, (double)k3, k2 - d5 + 0.5).tex(0.0, l3 * 0.25 + d6).color(1.0f, 1.0f, 1.0f, f5).lightmap(k4, l4).endVertex();
                    }
                    else if (ModuleAmbience.INSTANCE.weatherMode.getValue().equals(ModuleAmbience.WeatherModes.Snow)) {
                        if (j2 != 1) {
                            if (j2 >= 0) {
                                tessellator.draw();
                            }
                            j2 = 1;
                            Weather.mc.getTextureManager().bindTexture(Weather.SNOW_TEXTURES);
                            bufferbuilder.begin(7, DefaultVertexFormats.PARTICLE_POSITION_TEX_COLOR_LMAP);
                        }
                        final double d9 = -((((IEntityRenderer)renderer).getRendererUpdateCount() & 0x1FF) + partialTicks) / 512.0f;
                        final double d10 = Weather.RANDOM.nextDouble() + f2 * 0.01 * (float)Weather.RANDOM.nextGaussian();
                        final double d11 = Weather.RANDOM.nextDouble() + f2 * (float)Weather.RANDOM.nextGaussian() * 0.001;
                        final double d12 = l2 + 0.5f - entity.posX;
                        final double d13 = k2 + 0.5f - entity.posZ;
                        final float f6 = MathHelper.sqrt(d12 * d12 + d13 * d13) / i2;
                        final float f7 = ((1.0f - f6 * f6) * 0.3f + 0.5f) * f;
                        blockpos$mutableblockpos.setPos(l2, i4, k2);
                        final int i5 = (world.getCombinedLight((BlockPos)blockpos$mutableblockpos, 0) * 3 + 15728880) / 4;
                        final int j5 = i5 >> 16 & 0xFFFF;
                        final int k5 = i5 & 0xFFFF;
                        bufferbuilder.pos(l2 - d4 + 0.5, (double)l3, k2 - d5 + 0.5).tex(0.0 + d10, k3 * 0.25 + d9 + d11).color(1.0f, 1.0f, 1.0f, f7).lightmap(j5, k5).endVertex();
                        bufferbuilder.pos(l2 + d4 + 0.5, (double)l3, k2 + d5 + 0.5).tex(1.0 + d10, k3 * 0.25 + d9 + d11).color(1.0f, 1.0f, 1.0f, f7).lightmap(j5, k5).endVertex();
                        bufferbuilder.pos(l2 + d4 + 0.5, (double)k3, k2 + d5 + 0.5).tex(1.0 + d10, l3 * 0.25 + d9 + d11).color(1.0f, 1.0f, 1.0f, f7).lightmap(j5, k5).endVertex();
                        bufferbuilder.pos(l2 - d4 + 0.5, (double)k3, k2 - d5 + 0.5).tex(0.0 + d10, l3 * 0.25 + d9 + d11).color(1.0f, 1.0f, 1.0f, f7).lightmap(j5, k5).endVertex();
                    }
                }
            }
        }
        if (j2 >= 0) {
            tessellator.draw();
        }
        bufferbuilder.setTranslation(0.0, 0.0, 0.0);
        GlStateManager.enableCull();
        GlStateManager.disableBlend();
        GlStateManager.alphaFunc(516, 0.1f);
        renderer.disableLightmap();
    }
    
    static {
        RANDOM = new Random();
        RAIN_TEXTURES = new ResourceLocation("textures/environment/rain.png");
        SNOW_TEXTURES = new ResourceLocation("textures/environment/snow.png");
    }
}
