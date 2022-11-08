package dev.evangelion.client.modules.miscellaneous;

import dev.evangelion.client.values.impl.ValueEnum;
import dev.evangelion.api.manager.module.RegisterModule;
import dev.evangelion.api.manager.module.Module;

@RegisterModule(name = "WeatherModifier", tag = "Weather Modifier", description = "Modify the worlds weather.", category = Module.Category.MISCELLANEOUS)
public class ModuleWeatherModifier extends Module
{
    ValueEnum mode;
    
    public ModuleWeatherModifier() {
        this.mode = new ValueEnum("Mode", "Mode", "Mode for the worlds weather.", Modes.None);
    }
    
    public enum Modes
    {
        Rain, 
        Snow, 
        None;
    }
}
