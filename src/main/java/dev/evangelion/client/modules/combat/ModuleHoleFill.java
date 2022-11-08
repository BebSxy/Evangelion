package dev.evangelion.client.modules.combat;

import dev.evangelion.api.manager.module.Module;
import dev.evangelion.api.manager.module.RegisterModule;
import dev.evangelion.api.utilities.BlockUtils;
import dev.evangelion.api.utilities.ChatUtils;
import dev.evangelion.api.utilities.HoleUtils;
import dev.evangelion.api.utilities.InventoryUtils;
import dev.evangelion.api.utilities.MathUtils;
import dev.evangelion.api.utilities.PlayerUtils;
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
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@RegisterModule(name="HoleFill", tag="Hole Fill", description="Fills holes so enemies cant go in.", category=Module.Category.COMBAT)
public class ModuleHoleFill
        extends Module {
    ValueEnum mode = new ValueEnum("Mode", "Mode", "Mode for the holefill.", Modes.Smart);
    ValueEnum autoSwitch = new ValueEnum("Switch", "Switch", "The mode for Switching.", InventoryUtils.SwitchModes.Normal);
    ValueEnum itemSwitch = new ValueEnum("Item", "Item", "The item to place the blocks with.", InventoryUtils.ItemModes.Obsidian);
    ValueNumber range = new ValueNumber("Range", "Range", "Range to place blocks.", Float.valueOf(8.0f), Float.valueOf(3.0f), Float.valueOf(8.0f));
    ValueNumber targetRange = new ValueNumber("TargetRange", "TargetRange", "The range for the target.", Float.valueOf(6.0f), Float.valueOf(0.0f), Float.valueOf(10.0f));
    ValueBoolean noTargetDisable = new ValueBoolean("NoTargetDisable", "No Target Check", "Disable the module if there is no target.", false);
    ValueBoolean noHolesDisable = new ValueBoolean("NoHolesDisable", "No Holes Disable", "Disable the module if there are no more holes to fill.", false);
    ValueBoolean nearIgnore = new ValueBoolean("NearIgnore", "Near Ignore", "Ignores the holes that are near you.", false);
    ValueNumber nearRange = new ValueNumber("NearIgnoreRange", "Near Range", "Range to ignore the holes near you.", Float.valueOf(3.0f), Float.valueOf(1.0f), Float.valueOf(5.0f));
    ValueBoolean onlyIfSafe = new ValueBoolean("OnlyIfSafe", "Only If Safe", "Only holefills when you are safe.", false);
    ValueBoolean doubles = new ValueBoolean("DoubleHoles", "Double Holes", "Also fills double holes.", true);
    ValueNumber delay = new ValueNumber("Delay", "Delay", "Delay to fill holes.", 100, 0, 200);
    ValueCategory renderCategory = new ValueCategory("Render", "Render category.");
    ValueBoolean render = new ValueBoolean("FillRender", "Render", "Render the holes you are filling.", this.renderCategory, true);
    ValueNumber width = new ValueNumber("OutlineWidth", "Outline Width", "Outline width of the render.", this.renderCategory, Float.valueOf(1.0f), Float.valueOf(0.5f), Float.valueOf(5.0f));
    ValueColor color = new ValueColor("Color", "Color", "", this.renderCategory, new Color(0, 170, 255, 120));
    private List<BlockPos> placeableHoles = Collections.synchronizedList(new ArrayList());
    private EntityPlayer target = null;
    private TimerUtils cooldown = new TimerUtils();
    private BlockPos currentHole = null;

    @SubscribeEvent
    public void onMotion(EventMotion event) {
        if (this.nullCheck()) {
            return;
        }
        this.placeableHoles = this.getHoles();
        this.target = TargetUtils.getTarget(this.targetRange.getValue().floatValue());
        int slot = InventoryUtils.getTargetSlot(this.itemSwitch.getValue().toString());
        int lastSlot = ModuleHoleFill.mc.player.inventory.currentItem;
        if (this.placeableHoles.isEmpty()) {
            this.currentHole = null;
        }
        if (slot == -1) {
            ChatUtils.sendMessage("No blocks could be found.", "HoleFill");
            this.disable(true);
            return;
        }
        if (this.noTargetDisable.getValue() && this.target == null) {
            this.disable(true);
            return;
        }
        if (this.mode.getValue().equals((Object)Modes.Smart) && this.target == null || this.onlyIfSafe.getValue() && !HoleUtils.isInHole((EntityPlayer)ModuleHoleFill.mc.player)) {
            return;
        }
        if (!this.placeableHoles.isEmpty()) {
            for (BlockPos pos : this.placeableHoles.stream().sorted(Comparator.comparing(p -> ModuleHoleFill.mc.player.getDistanceSq(p))).collect(Collectors.toList())) {
                if (!this.cooldown.hasTimeElapsed(this.delay.getValue().intValue())) continue;
                this.currentHole = pos;
                InventoryUtils.switchSlot(slot, this.autoSwitch.getValue().equals((Object)InventoryUtils.SwitchModes.Silent));
                BlockUtils.placeBlock(event, pos, EnumHand.MAIN_HAND);
                if (!this.autoSwitch.getValue().equals((Object)InventoryUtils.SwitchModes.Strict)) {
                    InventoryUtils.switchSlot(lastSlot, this.autoSwitch.getValue().equals((Object)InventoryUtils.SwitchModes.Silent));
                }
                this.cooldown.reset();
            }
        }
        if (this.noHolesDisable.getValue() && this.placeableHoles.isEmpty()) {
            this.disable(true);
        }
    }

    @Override
    public void onRender3D(EventRender3D event) {
        super.onRender3D(event);
        if (this.nullCheck()) {
            return;
        }
        if (this.currentHole != null && this.render.getValue()) {
            RenderUtils.drawBlock(RenderUtils.getRenderBB((Object)this.currentHole), this.color.getValue());
            RenderUtils.drawBlockOutline(RenderUtils.getRenderBB((Object)this.currentHole), this.color.getValue(), this.width.getValue().floatValue());
        }
    }

    public List<BlockPos> getHoles() {
        List<BlockPos> holes = Collections.synchronizedList(new ArrayList());
        for (BlockPos position : PlayerUtils.getSphere(this.range.getValue().floatValue(), true, false)) {
            if (HoleUtils.isHole(position)) {
                if (this.nearIgnore.getValue() && ModuleHoleFill.mc.player.getDistanceSq(position) <= (double)MathUtils.square(this.nearRange.getValue().floatValue()) || !BlockUtils.isPositionPlaceable(position, true, true, false)) continue;
                holes.add(position);
                continue;
            }
            if (!HoleUtils.isDoubleHole(position) || !this.doubles.getValue() || ModuleHoleFill.mc.world.getBlockState(position).getBlock() != Blocks.AIR || ModuleHoleFill.mc.world.getBlockState(position.up()).getBlock() != Blocks.AIR || ModuleHoleFill.mc.world.getBlockState(position.up().up()).getBlock() != Blocks.AIR || this.nearIgnore.getValue() && ModuleHoleFill.mc.player.getDistanceSq(position) <= (double)MathUtils.square(this.nearRange.getValue().floatValue()) || !BlockUtils.isPositionPlaceable(position, true, true, false)) continue;
            holes.add(position);
        }
        return holes;
    }

    public static enum Modes {
        Normal,
        Smart;

    }
}

