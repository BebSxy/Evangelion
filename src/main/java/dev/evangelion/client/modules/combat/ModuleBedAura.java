package dev.evangelion.client.modules.combat;

import dev.evangelion.client.values.impl.ValueCategory;
import dev.evangelion.api.manager.module.RegisterModule;
import dev.evangelion.api.manager.module.Module;

@RegisterModule(name = "BedAura", tag = "Bed Aura", description = "Automatically explode beds when you are in the nether.", category = Module.Category.COMBAT)
public class ModuleBedAura extends Module
{
    private final ValueCategory breakCategory;
    private final ValueCategory placeCategory;
    
    public ModuleBedAura() {
        this.breakCategory = new ValueCategory("Break", "The category for bed breaking.");
        this.placeCategory = new ValueCategory("Place", "The category for bed placing.");
    }
}
