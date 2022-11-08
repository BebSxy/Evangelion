package dev.evangelion.client.modules.combat;

import net.minecraft.item.ItemPickaxe;
import dev.evangelion.client.values.impl.ValueBoolean;
import dev.evangelion.client.values.impl.ValueNumber;
import dev.evangelion.api.manager.module.RegisterModule;
import dev.evangelion.api.manager.module.Module;

@RegisterModule(name = "Reach", description = "Add reach to the player.", category = Module.Category.COMBAT)
public class ModuleReach extends Module
{
    public static ModuleReach INSTANCE;
    public ValueNumber reachAdd;
    public ValueBoolean noHitBox;
    public ValueBoolean pickaxeOnly;
    
    public ModuleReach() {
        this.reachAdd = new ValueNumber("ReachAdd", "Reach Add", "Value to add to player reach.", 3.0f, 0.0f, 3.0f);
        this.noHitBox = new ValueBoolean("NoHitBox", "No HitBox", "Ignore entity hit boxes.", false);
        this.pickaxeOnly = new ValueBoolean("PickaxeOny", "Pickaxe Only", "Only ignore hit boxes when holding a pickaxe.", true);
        ModuleReach.INSTANCE = this;
    }
    
    public boolean shouldIgnoreHitBox() {
        return (ModuleReach.mc.player.getHeldItemMainhand().getItem() instanceof ItemPickaxe || !this.pickaxeOnly.getValue()) && this.noHitBox.getValue();
    }
    
    @Override
    public String getHudInfo() {
        return this.reachAdd.getValue().floatValue() + "";
    }
}
