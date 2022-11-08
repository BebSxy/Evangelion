package dev.evangelion.client.modules.visuals.esp;

import net.minecraft.util.math.Vec3d;
import java.util.Iterator;
import java.awt.Color;
import org.lwjgl.opengl.GL11;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.math.AxisAlignedBB;
import dev.evangelion.api.utilities.RenderUtils;
import net.minecraft.entity.item.EntityEnderPearl;
import net.minecraft.entity.item.EntityExpBottle;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.Entity;
import dev.evangelion.api.utilities.IMinecraft;

public class ItemESP implements IMinecraft
{
    public void renderItems() {
        for (final Entity entity : ItemESP.mc.world.loadedEntityList) {
            if (entity instanceof EntityItem || entity instanceof EntityExpBottle || (entity instanceof EntityEnderPearl && ItemESP.mc.player.getDistanceSq(entity) < 2500.0)) {
                int i = 0;
                final Vec3d interp = RenderUtils.getInterpolatedRenderPos(entity, ItemESP.mc.getRenderPartialTicks());
                final AxisAlignedBB bb = new AxisAlignedBB(entity.getEntityBoundingBox().minX - 0.05 - entity.posX + interp.x, entity.getEntityBoundingBox().minY - 0.0 - entity.posY + interp.y, entity.getEntityBoundingBox().minZ - 0.05 - entity.posZ + interp.z, entity.getEntityBoundingBox().maxX + 0.05 - entity.posX + interp.x, entity.getEntityBoundingBox().maxY + 0.1 - entity.posY + interp.y, entity.getEntityBoundingBox().maxZ + 0.05 - entity.posZ + interp.z);
                GlStateManager.pushMatrix();
                GlStateManager.enableBlend();
                GlStateManager.disableDepth();
                GlStateManager.tryBlendFuncSeparate(770, 771, 0, 1);
                GlStateManager.disableTexture2D();
                GL11.glEnable(2848);
                GlStateManager.depthMask(false);
                GL11.glEnable(2848);
                GL11.glHint(3154, 4354);
                GL11.glLineWidth(1.0f);
                RenderUtils.drawBlock(bb, ModuleESP.INSTANCE.I_color.getValue());
                if (ModuleESP.INSTANCE.I_outline.getValue()) {
                    RenderUtils.drawBlockOutline(bb, new Color(ModuleESP.INSTANCE.I_color.getValue().getRed(), ModuleESP.INSTANCE.I_color.getValue().getGreen(), ModuleESP.INSTANCE.I_color.getValue().getBlue(), 255), ModuleESP.INSTANCE.I_width.getValue().floatValue());
                }
                GL11.glDisable(2848);
                GlStateManager.depthMask(true);
                GL11.glDisable(2848);
                GlStateManager.enableDepth();
                GlStateManager.enableTexture2D();
                GlStateManager.disableBlend();
                GlStateManager.popMatrix();
                if (++i >= 50) {
                    break;
                }
                continue;
            }
        }
    }
}
