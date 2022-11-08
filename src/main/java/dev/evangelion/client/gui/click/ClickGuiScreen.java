package dev.evangelion.client.gui.click;

import dev.evangelion.client.modules.client.ModuleGUI;
import java.io.IOException;
import java.util.Iterator;
import dev.evangelion.client.modules.client.ModuleColor;
import dev.evangelion.api.utilities.RenderUtils;
import java.awt.Color;
import dev.evangelion.Evangelion;
import dev.evangelion.client.gui.click.components.ModuleComponent;
import dev.evangelion.client.gui.click.manage.Component;
import dev.evangelion.api.manager.module.Module;
import dev.evangelion.client.gui.click.manage.Frame;
import java.util.ArrayList;
import net.minecraft.client.gui.GuiScreen;

public class ClickGuiScreen extends GuiScreen
{
    private final ArrayList<Frame> frames;
    
    public ClickGuiScreen() {
        this.frames = new ArrayList<Frame>();
        int offset = 30;
        for (final Module.Category category : Module.Category.values()) {
            if (category != Module.Category.HUD) {
                this.frames.add(new Frame(category, offset, 20));
                offset += 110;
            }
        }
    }
    
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        for (final Frame frame : this.frames) {
            frame.drawScreen(mouseX, mouseY, partialTicks);
        }
        for (final Frame frame : this.frames) {
            for (final Component c : frame.getComponents()) {
                if (c instanceof ModuleComponent) {
                    final ModuleComponent component = (ModuleComponent)c;
                    if (!component.isHovering(mouseX, mouseY) || !frame.isOpen() || component.getModule().getDescription().isEmpty()) {
                        continue;
                    }
                    RenderUtils.drawRect((float)(mouseX + 5), (float)(mouseY - 2), mouseX + Evangelion.FONT_MANAGER.getStringWidth(component.getModule().getDescription()) + 7.0f, (float)(mouseY + 11), new Color(40, 40, 40));
                    RenderUtils.drawOutline((float)(mouseX + 5), (float)(mouseY - 2), mouseX + Evangelion.FONT_MANAGER.getStringWidth(component.getModule().getDescription()) + 7.0f, (float)(mouseY + 11), 1.0f, ModuleColor.getColor());
                    Evangelion.FONT_MANAGER.drawStringWithShadow(component.getModule().getDescription(), (float)(mouseX + 7), (float)mouseY, Color.WHITE);
                }
            }
        }
    }
    
    public void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        for (final Frame frame : this.frames) {
            frame.mouseClicked(mouseX, mouseY, mouseButton);
        }
    }
    
    public void mouseReleased(final int mouseX, final int mouseY, final int state) {
        super.mouseReleased(mouseX, mouseY, state);
        for (final Frame frame : this.frames) {
            frame.mouseReleased(mouseX, mouseY, state);
        }
    }
    
    public void keyTyped(final char typedChar, final int keyCode) throws IOException {
        super.keyTyped(typedChar, keyCode);
        for (final Frame frame : this.frames) {
            frame.keyTyped(typedChar, keyCode);
        }
    }
    
    public void onGuiClosed() {
        super.onGuiClosed();
        ModuleGUI.INSTANCE.disable(true);
        for (final Frame frame : this.frames) {
            frame.onGuiClosed();
        }
    }
    
    public boolean doesGuiPauseGame() {
        return false;
    }
    
    public void drawGradientRect(final double left, final double top, final double right, final double bottom, final int startColor, final int endColor) {
        this.drawGradientRect((int)left, (int)top, (int)right, (int)bottom, startColor, endColor);
    }
    
    public Color getColor() {
        return new Color(ModuleColor.getColor().getRed(), ModuleColor.getColor().getGreen(), ModuleColor.getColor().getBlue(), 160);
    }
}
