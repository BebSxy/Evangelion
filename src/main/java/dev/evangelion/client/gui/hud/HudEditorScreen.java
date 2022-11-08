package dev.evangelion.client.gui.hud;

import dev.evangelion.client.modules.client.ModuleHUDEditor;
import java.io.IOException;
import dev.evangelion.client.modules.client.ModuleGUI;
import org.lwjgl.input.Mouse;
import java.util.Iterator;
import dev.evangelion.api.manager.element.Element;
import dev.evangelion.Evangelion;
import dev.evangelion.client.gui.click.manage.Frame;
import java.util.ArrayList;
import net.minecraft.client.gui.GuiScreen;

public class HudEditorScreen extends GuiScreen
{
    private final ArrayList<ElementFrame> elementFrames;
    private final Frame frame;
    
    public HudEditorScreen() {
        this.elementFrames = new ArrayList<ElementFrame>();
        this.frame = new Frame(20, 20);
        for (final Element element : Evangelion.ELEMENT_MANAGER.getElements()) {
            this.addElement(element);
            element.setFrame(this.getFrame(element));
        }
    }
    
    public void addElement(final Element element) {
        this.elementFrames.add(new ElementFrame(element, 10.0f, 10.0f, 80.0f, 15.0f, this));
    }
    
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        this.frame.drawScreen(mouseX, mouseY, partialTicks);
        final int scroll = Mouse.getDWheel();
        if (scroll < 0) {
            this.frame.setY(this.frame.getY() - ModuleGUI.INSTANCE.scrollSpeed.getValue().intValue());
        }
        else if (scroll > 0) {
            this.frame.setY(this.frame.getY() + ModuleGUI.INSTANCE.scrollSpeed.getValue().intValue());
        }
        for (final ElementFrame frame : this.elementFrames) {
            frame.drawScreen(mouseX, mouseY, partialTicks);
        }
    }
    
    public void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        this.frame.mouseClicked(mouseX, mouseY, mouseButton);
        for (final ElementFrame frame : this.elementFrames) {
            frame.mouseClicked(mouseX, mouseY, mouseButton);
        }
    }
    
    public void mouseReleased(final int mouseX, final int mouseY, final int state) {
        super.mouseReleased(mouseX, mouseY, state);
        this.frame.mouseReleased(mouseX, mouseY, state);
        for (final ElementFrame frame : this.elementFrames) {
            frame.mouseReleased(mouseX, mouseY, state);
        }
    }
    
    public void keyTyped(final char typedChar, final int keyCode) throws IOException {
        super.keyTyped(typedChar, keyCode);
        this.frame.keyTyped(typedChar, keyCode);
    }
    
    public void onGuiClosed() {
        super.onGuiClosed();
        if (ModuleHUDEditor.INSTANCE != null) {
            ModuleHUDEditor.INSTANCE.disable(true);
        }
    }
    
    public boolean doesGuiPauseGame() {
        return false;
    }
    
    public Frame getFrame() {
        return this.frame;
    }
    
    public ElementFrame getFrame(final Element element) {
        for (final ElementFrame frame : this.elementFrames) {
            if (!frame.getElement().equals(element)) {
                continue;
            }
            return frame;
        }
        return null;
    }
}
