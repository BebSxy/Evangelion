package dev.evangelion.client.modules.client;

import com.mojang.realmsclient.gui.ChatFormatting;
import dev.evangelion.client.values.impl.ValueString;
import dev.evangelion.client.values.impl.ValueEnum;
import dev.evangelion.client.values.impl.ValueCategory;
import dev.evangelion.api.manager.module.RegisterModule;
import dev.evangelion.api.manager.module.Module;

@RegisterModule(name = "Commands", description = "Let's you customize commands and sending text.", category = Module.Category.CLIENT, persistent = true)
public class ModuleCommands extends Module
{
    public final ValueCategory watermarkCategory;
    public final ValueEnum watermarkMode;
    public final ValueString watermarkText;
    public final ValueString firstSymbol;
    public final ValueString secondSymbol;
    public final ValueCategory firstWatermarkColorCategory;
    public final ValueEnum firstWatermarkColor;
    public final ValueEnum firstWatermarkBrightness;
    public final ValueCategory secondWatermarkColorCategory;
    public final ValueEnum secondWatermarkColor;
    public final ValueEnum secondWatermarkBrightness;
    public final ValueCategory firstColorCategory;
    public final ValueEnum firstColorMode;
    public final ValueEnum firstColorBrightness;
    public final ValueCategory secondColorCategory;
    public final ValueEnum secondColorMode;
    public final ValueEnum secondColorBrightness;
    public static ModuleCommands INSTANCE;
    
    public ModuleCommands() {
        this.watermarkCategory = new ValueCategory("Watermark", "The category for the Watermark.");
        this.watermarkMode = new ValueEnum("WatermarkMode", "Mode", "The mode for the watermark.", this.watermarkCategory, WatermarkModes.Normal);
        this.watermarkText = new ValueString("WatermarkText", "Text", "The watermark text.", this.watermarkCategory, "Evangelion");
        this.firstSymbol = new ValueString("WatermarkFirstSymbol", "First Symbol", "The first symbol on the watermark.", this.watermarkCategory, "[");
        this.secondSymbol = new ValueString("WatermarkSecondSymbol", "Second Symbol", "The second symbol on the watermark.", this.watermarkCategory, "]");
        this.firstWatermarkColorCategory = new ValueCategory("First Mark Color", "The colors for the first color on the Watermark.");
        this.firstWatermarkColor = new ValueEnum("FirstWatermarkColor", "Color", "The color of the first watermark color.", this.firstWatermarkColorCategory, ColorModes.Purple);
        this.firstWatermarkBrightness = new ValueEnum("FirstWatermarkBrightness", "Brightness", "The brightness of the second watermark color.", this.firstWatermarkColorCategory, LightModes.Light);
        this.secondWatermarkColorCategory = new ValueCategory("Second Mark Color", "The colors for the second color on the Watermark.");
        this.secondWatermarkColor = new ValueEnum("SecondWatermarkColor", "Color", "The color of the second watermark color.", this.secondWatermarkColorCategory, ColorModes.Purple);
        this.secondWatermarkBrightness = new ValueEnum("SecondWatermarkBrightness", "Brightness", "The brightness of the second watermark color.", this.secondWatermarkColorCategory, LightModes.Dark);
        this.firstColorCategory = new ValueCategory("First Color", "The first color in the chat sending.");
        this.firstColorMode = new ValueEnum("FirstColorMode", "Color", "The color for the First Color.", this.firstColorCategory, ColorModes.Purple);
        this.firstColorBrightness = new ValueEnum("FirstColorBrightness", "Brightness", "The brightness for the First Color.", this.firstColorCategory, LightModes.Light);
        this.secondColorCategory = new ValueCategory("Second Color", "The second color in the chat sending.");
        this.secondColorMode = new ValueEnum("SecondColorMode", "Color", "The color for the Second Color.", this.secondColorCategory, ColorModes.Purple);
        this.secondColorBrightness = new ValueEnum("SecondColorBrightness", "Brightness", "The brightness for the Second Color.", this.secondColorCategory, LightModes.Dark);
        ModuleCommands.INSTANCE = this;
    }
    
    public static ChatFormatting getFirstColor() {
        switch ((ColorModes)ModuleCommands.INSTANCE.firstColorMode.getValue()) {
            case Black: {
                if (ModuleCommands.INSTANCE.firstColorBrightness.getValue().equals(LightModes.Light)) {
                    return ChatFormatting.DARK_GRAY;
                }
                return ChatFormatting.BLACK;
            }
            case Gray: {
                if (ModuleCommands.INSTANCE.firstColorBrightness.getValue().equals(LightModes.Light)) {
                    return ChatFormatting.GRAY;
                }
                return ChatFormatting.DARK_GRAY;
            }
            case Blue: {
                if (ModuleCommands.INSTANCE.firstColorBrightness.getValue().equals(LightModes.Light)) {
                    return ChatFormatting.BLUE;
                }
                return ChatFormatting.DARK_BLUE;
            }
            case Green: {
                if (ModuleCommands.INSTANCE.firstColorBrightness.getValue().equals(LightModes.Light)) {
                    return ChatFormatting.GREEN;
                }
                return ChatFormatting.DARK_GREEN;
            }
            case Aqua: {
                if (ModuleCommands.INSTANCE.firstColorBrightness.getValue().equals(LightModes.Light)) {
                    return ChatFormatting.AQUA;
                }
                return ChatFormatting.DARK_AQUA;
            }
            case Red: {
                if (ModuleCommands.INSTANCE.firstColorBrightness.getValue().equals(LightModes.Light)) {
                    return ChatFormatting.RED;
                }
                return ChatFormatting.DARK_RED;
            }
            case Yellow: {
                if (ModuleCommands.INSTANCE.firstColorBrightness.getValue().equals(LightModes.Light)) {
                    return ChatFormatting.YELLOW;
                }
                return ChatFormatting.GOLD;
            }
            case Purple: {
                if (ModuleCommands.INSTANCE.firstColorBrightness.getValue().equals(LightModes.Light)) {
                    return ChatFormatting.LIGHT_PURPLE;
                }
                return ChatFormatting.DARK_PURPLE;
            }
            default: {
                if (ModuleCommands.INSTANCE.firstColorBrightness.getValue().equals(LightModes.Light)) {
                    return ChatFormatting.WHITE;
                }
                return ChatFormatting.GRAY;
            }
        }
    }
    
    public static ChatFormatting getSecondColor() {
        switch ((ColorModes)ModuleCommands.INSTANCE.secondColorMode.getValue()) {
            case Black: {
                if (ModuleCommands.INSTANCE.secondColorBrightness.getValue().equals(LightModes.Light)) {
                    return ChatFormatting.DARK_GRAY;
                }
                return ChatFormatting.BLACK;
            }
            case Gray: {
                if (ModuleCommands.INSTANCE.secondColorBrightness.getValue().equals(LightModes.Light)) {
                    return ChatFormatting.GRAY;
                }
                return ChatFormatting.DARK_GRAY;
            }
            case Blue: {
                if (ModuleCommands.INSTANCE.secondColorBrightness.getValue().equals(LightModes.Light)) {
                    return ChatFormatting.BLUE;
                }
                return ChatFormatting.DARK_BLUE;
            }
            case Green: {
                if (ModuleCommands.INSTANCE.secondColorBrightness.getValue().equals(LightModes.Light)) {
                    return ChatFormatting.GREEN;
                }
                return ChatFormatting.DARK_GREEN;
            }
            case Aqua: {
                if (ModuleCommands.INSTANCE.secondColorBrightness.getValue().equals(LightModes.Light)) {
                    return ChatFormatting.AQUA;
                }
                return ChatFormatting.DARK_AQUA;
            }
            case Red: {
                if (ModuleCommands.INSTANCE.secondColorBrightness.getValue().equals(LightModes.Light)) {
                    return ChatFormatting.RED;
                }
                return ChatFormatting.DARK_RED;
            }
            case Yellow: {
                if (ModuleCommands.INSTANCE.secondColorBrightness.getValue().equals(LightModes.Light)) {
                    return ChatFormatting.YELLOW;
                }
                return ChatFormatting.GOLD;
            }
            case Purple: {
                if (ModuleCommands.INSTANCE.secondColorBrightness.getValue().equals(LightModes.Light)) {
                    return ChatFormatting.LIGHT_PURPLE;
                }
                return ChatFormatting.DARK_PURPLE;
            }
            default: {
                if (ModuleCommands.INSTANCE.secondColorBrightness.getValue().equals(LightModes.Light)) {
                    return ChatFormatting.WHITE;
                }
                return ChatFormatting.GRAY;
            }
        }
    }
    
    public static ChatFormatting getFirstWatermarkColor() {
        switch ((ColorModes)ModuleCommands.INSTANCE.firstWatermarkColor.getValue()) {
            case Black: {
                if (ModuleCommands.INSTANCE.firstWatermarkBrightness.getValue().equals(LightModes.Light)) {
                    return ChatFormatting.DARK_GRAY;
                }
                return ChatFormatting.BLACK;
            }
            case Gray: {
                if (ModuleCommands.INSTANCE.firstWatermarkBrightness.getValue().equals(LightModes.Light)) {
                    return ChatFormatting.GRAY;
                }
                return ChatFormatting.DARK_GRAY;
            }
            case Blue: {
                if (ModuleCommands.INSTANCE.firstWatermarkBrightness.getValue().equals(LightModes.Light)) {
                    return ChatFormatting.BLUE;
                }
                return ChatFormatting.DARK_BLUE;
            }
            case Green: {
                if (ModuleCommands.INSTANCE.firstWatermarkBrightness.getValue().equals(LightModes.Light)) {
                    return ChatFormatting.GREEN;
                }
                return ChatFormatting.DARK_GREEN;
            }
            case Aqua: {
                if (ModuleCommands.INSTANCE.firstWatermarkBrightness.getValue().equals(LightModes.Light)) {
                    return ChatFormatting.AQUA;
                }
                return ChatFormatting.DARK_AQUA;
            }
            case Red: {
                if (ModuleCommands.INSTANCE.firstWatermarkBrightness.getValue().equals(LightModes.Light)) {
                    return ChatFormatting.RED;
                }
                return ChatFormatting.DARK_RED;
            }
            case Yellow: {
                if (ModuleCommands.INSTANCE.firstWatermarkBrightness.getValue().equals(LightModes.Light)) {
                    return ChatFormatting.YELLOW;
                }
                return ChatFormatting.GOLD;
            }
            case Purple: {
                if (ModuleCommands.INSTANCE.firstWatermarkBrightness.getValue().equals(LightModes.Light)) {
                    return ChatFormatting.LIGHT_PURPLE;
                }
                return ChatFormatting.DARK_PURPLE;
            }
            default: {
                if (ModuleCommands.INSTANCE.firstWatermarkBrightness.getValue().equals(LightModes.Light)) {
                    return ChatFormatting.WHITE;
                }
                return ChatFormatting.GRAY;
            }
        }
    }
    
    public static ChatFormatting getSecondWatermarkColor() {
        switch ((ColorModes)ModuleCommands.INSTANCE.secondWatermarkColor.getValue()) {
            case Black: {
                if (ModuleCommands.INSTANCE.secondWatermarkBrightness.getValue().equals(LightModes.Light)) {
                    return ChatFormatting.DARK_GRAY;
                }
                return ChatFormatting.BLACK;
            }
            case Gray: {
                if (ModuleCommands.INSTANCE.secondWatermarkBrightness.getValue().equals(LightModes.Light)) {
                    return ChatFormatting.GRAY;
                }
                return ChatFormatting.DARK_GRAY;
            }
            case Blue: {
                if (ModuleCommands.INSTANCE.secondWatermarkBrightness.getValue().equals(LightModes.Light)) {
                    return ChatFormatting.BLUE;
                }
                return ChatFormatting.DARK_BLUE;
            }
            case Green: {
                if (ModuleCommands.INSTANCE.secondWatermarkBrightness.getValue().equals(LightModes.Light)) {
                    return ChatFormatting.GREEN;
                }
                return ChatFormatting.DARK_GREEN;
            }
            case Aqua: {
                if (ModuleCommands.INSTANCE.secondWatermarkBrightness.getValue().equals(LightModes.Light)) {
                    return ChatFormatting.AQUA;
                }
                return ChatFormatting.DARK_AQUA;
            }
            case Red: {
                if (ModuleCommands.INSTANCE.secondWatermarkBrightness.getValue().equals(LightModes.Light)) {
                    return ChatFormatting.RED;
                }
                return ChatFormatting.DARK_RED;
            }
            case Yellow: {
                if (ModuleCommands.INSTANCE.secondWatermarkBrightness.getValue().equals(LightModes.Light)) {
                    return ChatFormatting.YELLOW;
                }
                return ChatFormatting.GOLD;
            }
            case Purple: {
                if (ModuleCommands.INSTANCE.secondWatermarkBrightness.getValue().equals(LightModes.Light)) {
                    return ChatFormatting.LIGHT_PURPLE;
                }
                return ChatFormatting.DARK_PURPLE;
            }
            default: {
                if (ModuleCommands.INSTANCE.secondWatermarkBrightness.getValue().equals(LightModes.Light)) {
                    return ChatFormatting.WHITE;
                }
                return ChatFormatting.GRAY;
            }
        }
    }
    
    public enum WatermarkModes
    {
        Normal, 
        Japanese, 
        Custom, 
        None;
    }
    
    public enum ColorModes
    {
        Gold, 
        Black, 
        Gray, 
        Blue, 
        Green, 
        Aqua, 
        Red, 
        Yellow, 
        White, 
        Purple;
    }
    
    public enum LightModes
    {
        Light, 
        Dark;
    }
}
