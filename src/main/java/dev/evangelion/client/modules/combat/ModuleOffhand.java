package dev.evangelion.client.modules.combat;

import org.lwjgl.input.Mouse;
import net.minecraft.item.ItemSword;
import dev.evangelion.api.utilities.InventoryUtils;
import net.minecraft.init.Items;
import net.minecraft.client.gui.inventory.GuiContainer;
import dev.evangelion.client.values.impl.ValueBoolean;
import dev.evangelion.client.values.impl.ValueNumber;
import dev.evangelion.client.values.impl.ValueEnum;
import dev.evangelion.api.manager.module.RegisterModule;
import dev.evangelion.api.manager.module.Module;

@RegisterModule(name = "Offhand", description = "Automatically switch items to your offhand.", category = Module.Category.COMBAT)
public class ModuleOffhand extends Module
{
    ValueEnum mode;
    ValueNumber hp;
    ValueNumber fall;
    ValueBoolean swordGap;
    
    public ModuleOffhand() {
        this.mode = new ValueEnum("Mode", "Mode", "Mode for offhand.", Modes.Totem);
        this.hp = new ValueNumber("Health", "Health", "Health of player", 12.0f, 1.0f, 20.0f);
        this.fall = new ValueNumber("Fall", "Fall", "Fall distance.", 10, 5, 30);
        this.swordGap = new ValueBoolean("SwordGap", "Sword Gap", "Automatically switch to gap when sword.", false);
    }
    
    @Override
    public void onUpdate() {
        super.onUpdate();
        if (super.nullCheck() || ModuleOffhand.mc.currentScreen instanceof GuiContainer) {
            return;
        }
        if (ModuleOffhand.mc.player.getHealth() + ModuleOffhand.mc.player.getAbsorptionAmount() <= this.hp.getValue().floatValue() || (ModuleOffhand.mc.player.fallDistance >= this.fall.getValue().intValue() && !ModuleOffhand.mc.player.isElytraFlying())) {
            if (ModuleOffhand.mc.player.getHeldItemOffhand().getItem() != Items.TOTEM_OF_UNDYING) {
                InventoryUtils.offhandItem(Items.TOTEM_OF_UNDYING);
            }
        }
        else if (ModuleOffhand.mc.player.getHeldItemMainhand().getItem() instanceof ItemSword && this.swordGap.getValue() && Mouse.isButtonDown(1)) {
            if (ModuleOffhand.mc.player.getHeldItemOffhand().getItem() != Items.GOLDEN_APPLE) {
                InventoryUtils.offhandItem(Items.GOLDEN_APPLE);
            }
        }
        else if (this.mode.getValue().equals(Modes.Totem) && ModuleOffhand.mc.player.getHeldItemOffhand().getItem() != Items.TOTEM_OF_UNDYING) {
            InventoryUtils.offhandItem(Items.TOTEM_OF_UNDYING);
        }
        else if (this.mode.getValue().equals(Modes.Crystal) && ModuleOffhand.mc.player.getHeldItemOffhand().getItem() != Items.END_CRYSTAL) {
            InventoryUtils.offhandItem(Items.END_CRYSTAL);
        }
        else if (this.mode.getValue().equals(Modes.Gapple) && ModuleOffhand.mc.player.getHeldItemOffhand().getItem() != Items.GOLDEN_APPLE) {
            InventoryUtils.offhandItem(Items.GOLDEN_APPLE);
        }
    }
    
    @Override
    public String getHudInfo() {
        return this.mode.getValue().name();
    }
    
    public enum Modes
    {
        Totem, 
        Crystal, 
        Gapple;
    }
}
