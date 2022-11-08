package dev.evangelion.client.modules.visuals.esp;

import java.awt.Color;
import net.minecraft.util.math.AxisAlignedBB;
import java.util.List;
import java.util.Objects;
import net.minecraft.entity.Entity;
import dev.evangelion.api.utilities.RenderUtils;
import java.util.Iterator;
import java.util.Collection;
import dev.evangelion.api.utilities.HoleUtils;
import net.minecraft.block.BlockAir;
import net.minecraft.util.math.BlockPos;
import net.minecraft.entity.player.EntityPlayer;
import dev.evangelion.api.utilities.BlockUtils;
import java.util.ArrayList;
import dev.evangelion.api.utilities.IMinecraft;

public class HoleESP implements IMinecraft
{
    public void updateHoles() {
        final ArrayList<HoleInfo> holes = new ArrayList<HoleInfo>();
        for (final BlockPos potentialHole : BlockUtils.getNearbyBlocks((EntityPlayer)HoleESP.mc.player, ModuleESP.INSTANCE.H_range.getValue().intValue(), false)) {
            if (!(HoleESP.mc.world.getBlockState(potentialHole).getBlock() instanceof BlockAir)) {
                continue;
            }
            if (HoleUtils.isBedrockHole(potentialHole)) {
                holes.add(new HoleInfo(potentialHole, Type.Bedrock, 0, 0));
            }
            else if (HoleUtils.isObiHole(potentialHole)) {
                holes.add(new HoleInfo(potentialHole, Type.Obsidian, 0, 0));
            }
            if (!ModuleESP.INSTANCE.H_doubles.getValue()) {
                continue;
            }
            if (HoleUtils.isDoubleBedrockHoleX(potentialHole.west()) || HoleUtils.isDoubleObsidianHoleX(potentialHole.west())) {
                holes.add(new HoleInfo(potentialHole.west(), Type.Double, 1, 0));
            }
            else {
                if (!HoleUtils.isDoubleBedrockHoleZ(potentialHole.north()) && !HoleUtils.isDoubleObsidianHoleZ(potentialHole.north())) {
                    continue;
                }
                holes.add(new HoleInfo(potentialHole.north(), Type.Double, 0, 1));
            }
        }
        ModuleESP.INSTANCE.hole.clear();
        ModuleESP.INSTANCE.hole.addAll(holes);
    }
    
    public void renderHoles() {
        RenderUtils.camera.setPosition(Objects.requireNonNull(HoleESP.mc.getRenderViewEntity()).posX, HoleESP.mc.getRenderViewEntity().posY, HoleESP.mc.getRenderViewEntity().posZ);
        final List<HoleInfo> currentHoles = new ArrayList<HoleInfo>();
        currentHoles.addAll(ModuleESP.INSTANCE.hole);
        currentHoles.forEach(holeInfo -> renderHole(holeInfo.pos, holeInfo.type, holeInfo.length, holeInfo.width));
    }
    
    public static void renderHole(final BlockPos hole, final Type type, final double length, final double width) {
        final AxisAlignedBB box = new AxisAlignedBB(hole.getX() - HoleESP.mc.getRenderManager().viewerPosX, hole.getY() - HoleESP.mc.getRenderManager().viewerPosY, hole.getZ() - HoleESP.mc.getRenderManager().viewerPosZ, hole.getX() + 1 - HoleESP.mc.getRenderManager().viewerPosX + length, hole.getY() + ModuleESP.INSTANCE.H_height.getValue().doubleValue() - HoleESP.mc.getRenderManager().viewerPosY, hole.getZ() + width + 1.0 - HoleESP.mc.getRenderManager().viewerPosZ);
        Color color = (type == Type.Bedrock) ? ModuleESP.INSTANCE.H_bedrockOL.getValue() : ((type == Type.Obsidian) ? ModuleESP.INSTANCE.H_obsidianOL.getValue() : ModuleESP.INSTANCE.H_doubleOL.getValue());
        RenderUtils.prepare();
        if (ModuleESP.INSTANCE.H_mode.getValue().equals(ModuleESP.H_modes.Normal)) {
            RenderUtils.drawBlockOutline(box, color, ModuleESP.INSTANCE.H_outlineWidth.getValue().floatValue());
            color = ((type == Type.Bedrock) ? ModuleESP.INSTANCE.H_bedrock.getValue() : ((type == Type.Obsidian) ? ModuleESP.INSTANCE.H_obsidian.getValue() : ModuleESP.INSTANCE.H_doubleColor.getValue()));
            RenderUtils.drawBlock(box.shrink(0.0020000000949949026), color);
        }
        if (ModuleESP.INSTANCE.H_mode.getValue().equals(ModuleESP.H_modes.Glow)) {
            RenderUtils.drawGradientBlockOutline(box, new Color(0, 0, 0, 0), color, ModuleESP.INSTANCE.H_outlineWidth.getValue().floatValue());
            color = ((type == Type.Bedrock) ? ModuleESP.INSTANCE.H_bedrock.getValue() : ((type == Type.Obsidian) ? ModuleESP.INSTANCE.H_obsidian.getValue() : ModuleESP.INSTANCE.H_doubleColor.getValue()));
            RenderUtils.drawOpenGradientBoxBB(box.shrink(0.004999999888241291), color, new Color(0, 0, 0, 0), false);
        }
        RenderUtils.release();
    }
    
    public enum Type
    {
        Obsidian, 
        Bedrock, 
        Double;
    }
    
    static class HoleInfo
    {
        BlockPos pos;
        Type type;
        int length;
        int width;
        
        public HoleInfo(final BlockPos pos, final Type type, final int length, final int width) {
            this.pos = pos;
            this.type = type;
            this.length = length;
            this.width = width;
        }
    }
}
