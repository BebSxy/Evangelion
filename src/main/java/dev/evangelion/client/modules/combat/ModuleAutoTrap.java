package dev.evangelion.client.modules.combat;

import dev.evangelion.api.manager.module.Module;
import dev.evangelion.api.manager.module.RegisterModule;
import dev.evangelion.api.utilities.BlockUtils;
import dev.evangelion.api.utilities.ChatUtils;
import dev.evangelion.api.utilities.InventoryUtils;
import dev.evangelion.api.utilities.MathUtils;
import dev.evangelion.api.utilities.RenderUtils;
import dev.evangelion.api.utilities.TargetUtils;
import dev.evangelion.api.utilities.TimerUtils;
import dev.evangelion.client.events.EventMotion;
import dev.evangelion.client.events.EventRender3D;
import dev.evangelion.client.values.impl.ValueBoolean;
import dev.evangelion.client.values.impl.ValueCategory;
import dev.evangelion.client.values.impl.ValueColor;
import dev.evangelion.client.values.impl.ValueEnum;
import dev.evangelion.client.values.impl.ValueNumber;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@RegisterModule(name="AutoTrap", tag="Auto Trap", description="Automatically traps targets.", category=Module.Category.COMBAT)
public class ModuleAutoTrap
        extends Module {
    ValueEnum autoSwitch = new ValueEnum("Switch", "Switch", "The mode for Switching.", InventoryUtils.SwitchModes.Normal);
    ValueEnum itemSwitch = new ValueEnum("Item", "Item", "The item to place the blocks with.", InventoryUtils.ItemModes.Obsidian);
    ValueNumber range = new ValueNumber("Range", "Range", "Range to place blocks.", Float.valueOf(8.0f), Float.valueOf(3.0f), Float.valueOf(8.0f));
    ValueNumber targetRange = new ValueNumber("TargetRange", "TargetRange", "The range for the target.", Float.valueOf(6.0f), Float.valueOf(0.0f), Float.valueOf(10.0f));
    ValueBoolean noTargetDisable = new ValueBoolean("NoTargetDisable", "No Target Check", "Disable the module if there is no target.", false);
    ValueBoolean noPositionsDisable = new ValueBoolean("NoPositionsDisable", "No Positions Disable", "Disable the module if there are no placeable positions.", false);
    ValueNumber delay = new ValueNumber("Delay", "Delay", "Delay to fill holes.", 100, 0, 200);
    ValueCategory renderCategory = new ValueCategory("Render", "Render category.");
    ValueBoolean render = new ValueBoolean("FillRender", "Render", "Render the holes you are filling.", this.renderCategory, true);
    ValueNumber width = new ValueNumber("OutlineWidth", "Outline Width", "Outline width of the render.", this.renderCategory, Float.valueOf(1.0f), Float.valueOf(0.5f), Float.valueOf(5.0f));
    ValueColor color = new ValueColor("Color", "Color", "", this.renderCategory, new Color(0, 170, 255, 120));
    private EntityPlayer target = null;
    private List<BlockPos> placeablePositions = Collections.synchronizedList(new ArrayList());
    private TimerUtils cooldown = new TimerUtils();
    private BlockPos currentPos = null;
    private final Vec3d[] offsets = new Vec3d[]{new Vec3d(-1.0, 0.0, 0.0), new Vec3d(1.0, 0.0, 0.0), new Vec3d(0.0, 0.0, -1.0), new Vec3d(0.0, 0.0, 1.0), new Vec3d(-1.0, 1.0, 0.0), new Vec3d(1.0, 1.0, 0.0), new Vec3d(0.0, 1.0, -1.0), new Vec3d(0.0, 1.0, 1.0), new Vec3d(0.0, 2.0, 1.0), new Vec3d(0.0, 2.0, 0.0)};

    @SubscribeEvent
    public void onMotion(EventMotion event) {
        if (this.nullCheck()) {
            return;
        }
        this.target = TargetUtils.getTarget(this.targetRange.getValue().floatValue());
        int slot = InventoryUtils.getTargetSlot(this.itemSwitch.getValue().toString());
        int lastSlot = ModuleAutoTrap.mc.player.inventory.currentItem;
        if (this.noTargetDisable.getValue() && this.target == null) {
            this.disable(true);
            return;
        }
        this.placeablePositions = this.getPlaceablePositions(this.target);
        if (this.placeablePositions.isEmpty()) {
            this.currentPos = null;
        }
        if (slot == -1) {
            ChatUtils.sendMessage("No blocks could be found.", "AutoTrap");
            this.disable(true);
            return;
        }
        if (!this.placeablePositions.isEmpty()) {
            for (BlockPos pos : this.placeablePositions.stream().sorted(Comparator.comparing(p -> p.getY())).sorted(Comparator.comparing(p -> ModuleAutoTrap.mc.player.getDistanceSq(p))).collect(Collectors.toList())) {
                if (!this.cooldown.hasTimeElapsed(this.delay.getValue().intValue())) continue;
                this.currentPos = pos;
                InventoryUtils.switchSlot(slot, this.autoSwitch.getValue().equals((Object)InventoryUtils.SwitchModes.Silent));
                BlockUtils.placeBlock(event, pos, EnumHand.MAIN_HAND);
                if (!this.autoSwitch.getValue().equals((Object)InventoryUtils.SwitchModes.Strict)) {
                    InventoryUtils.switchSlot(lastSlot, this.autoSwitch.getValue().equals((Object)InventoryUtils.SwitchModes.Silent));
                }
                this.cooldown.reset();
            }
        }
        if (this.noPositionsDisable.getValue() && this.placeablePositions.isEmpty()) {
            this.disable(true);
        }
    }

    @Override
    public void onRender3D(EventRender3D event) {
        super.onRender3D(event);
        if (this.nullCheck()) {
            return;
        }
        if (this.currentPos != null && this.render.getValue()) {
            RenderUtils.drawBlock(RenderUtils.getRenderBB((Object)this.currentPos), this.color.getValue());
            RenderUtils.drawBlockOutline(RenderUtils.getRenderBB((Object)this.currentPos), this.color.getValue(), this.width.getValue().floatValue());
        }
    }

    public List<BlockPos> getPlaceablePositions(EntityPlayer player) {
        List<BlockPos> positions = Collections.synchronizedList(new ArrayList());
        for (Vec3d vec3d : this.offsets) {
            BlockPos position = new BlockPos(vec3d.add(player.getPositionVector()));
            if (!BlockUtils.isPositionPlaceable(position, true, true, true) || !(ModuleAutoTrap.mc.player.getDistanceSq(position) <= (double)MathUtils.square(this.range.getValue().floatValue()))) continue;
            positions.add(position);
        }
        return positions;
    }
}

