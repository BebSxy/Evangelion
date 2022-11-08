package dev.evangelion.api.manager.miscellaneous;

import dev.evangelion.client.modules.client.ModuleFont;
import dev.evangelion.Evangelion;
import java.awt.Color;
import dev.evangelion.client.gui.font.FontRenderer;
import dev.evangelion.api.utilities.IMinecraft;

public class FontManager implements IMinecraft
{
    public static FontRenderer FONT_RENDERER;
    
    public void drawStringWithShadow(final String text, final float x, final float y, final Color color) {
        if (Evangelion.MODULE_MANAGER.isModuleEnabled("Font")) {
            FontManager.FONT_RENDERER.drawString(text, x, y, color.getRGB(), ModuleFont.INSTANCE.shadow.getValue());
        }
        else {
            FontManager.mc.fontRenderer.drawString(text, x, y, color.getRGB(), ModuleFont.INSTANCE.shadow.getValue());
        }
    }
    
    public float getStringWidth(final String text) {
        if (Evangelion.MODULE_MANAGER.isModuleEnabled("Font")) {
            return (float)FontManager.FONT_RENDERER.getStringWidth(text);
        }
        return (float)FontManager.mc.fontRenderer.getStringWidth(text);
    }
    
    public float getHeight() {
        if (Evangelion.MODULE_MANAGER.isModuleEnabled("Font")) {
            return (float)FontManager.FONT_RENDERER.getHeight();
        }
        return (float)FontManager.mc.fontRenderer.FONT_HEIGHT;
    }
}
