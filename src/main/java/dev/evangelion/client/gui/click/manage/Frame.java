package dev.evangelion.client.gui.click.manage;

import dev.evangelion.client.gui.click.components.ColorComponentTest;
import dev.evangelion.client.modules.client.ModuleColor;
import java.awt.Color;
import dev.evangelion.api.utilities.RenderUtils;
import dev.evangelion.client.modules.client.ModuleGUI;
import org.lwjgl.input.Mouse;
import dev.evangelion.api.manager.element.Element;
import java.util.Iterator;
import dev.evangelion.client.gui.click.components.ModuleComponent;
import dev.evangelion.Evangelion;
import dev.evangelion.api.manager.module.Module;
import java.util.ArrayList;
import dev.evangelion.api.utilities.IMinecraft;

public class Frame implements IMinecraft
{
    private final ArrayList<Component> components;
    private final String tab;
    private int x;
    private int y;
    private int height;
    private final int width;
    private boolean open;
    private boolean dragging;
    private int dragX;
    private int dragY;
    
    public Frame(final Module.Category category, final int x, final int y) {
        this.open = true;
        this.tab = category.getName();
        this.x = x;
        this.y = y;
        this.width = 100;
        this.dragging = false;
        this.dragX = 0;
        this.dragY = 0;
        this.components = new ArrayList<Component>();
        int offset = 16;
        for (final Module module : Evangelion.MODULE_MANAGER.getModules(category)) {
            this.components.add(new ModuleComponent(module, offset, this));
            offset += 16;
        }
        this.height = offset;
        this.refresh();
    }
    
    public Frame(final int x, final int y) {
        this.open = true;
        this.tab = "HUD";
        this.x = x;
        this.y = y;
        this.width = 100;
        this.dragging = false;
        this.dragX = 0;
        this.dragY = 0;
        this.components = new ArrayList<Component>();
        int offset = 16;
        for (final Element element : Evangelion.ELEMENT_MANAGER.getElements()) {
            this.components.add(new ModuleComponent(element, offset, this));
            offset += 16;
        }
        this.height = offset;
        this.refresh();
    }
    
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        this.refresh();
        if (this.isDragging()) {
            this.setX(mouseX - this.dragX);
            this.setY(mouseY - this.dragY);
        }
        if (mouseX >= this.getX() && mouseX <= this.getX() + this.getWidth()) {
            final int scroll = Mouse.getDWheel();
            if (scroll < 0) {
                this.setY(this.getY() - ModuleGUI.INSTANCE.scrollSpeed.getValue().intValue());
            }
            else if (scroll > 0) {
                this.setY(this.getY() + ModuleGUI.INSTANCE.scrollSpeed.getValue().intValue());
            }
        }
        RenderUtils.drawRect((float)(this.getX() - 4), (float)(this.getY() - 3), (float)(this.getX() + this.getWidth() + 4), (float)(this.getY() + 13), Evangelion.CLICK_GUI.getColor());
        if (this.isOpen()) {
            RenderUtils.drawRect((float)(this.getX() - 2), (float)(this.getY() + 13), (float)(this.getX() + this.getWidth() + 2), (float)(this.getY() + this.getHeight()), new Color(0, 0, 0, 160));
            RenderUtils.drawOutline((float)(this.getX() - 2), (float)(this.getY() + 13), (float)(this.getX() + this.getWidth() + 2), (float)(this.getY() + this.getHeight()), 0.5f, ModuleColor.getColor());
        }
        Evangelion.FONT_MANAGER.drawStringWithShadow(this.tab, (float)(this.x + 3), (float)(this.y + 1), Color.WHITE);
        if (this.isOpen()) {
            for (final Component component : this.components) {
                if (component.isVisible()) {
                    component.drawScreen(mouseX, mouseY, partialTicks);
                }
            }
        }
    }
    
    public void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) {
        if (mouseX >= this.getX() - 4 && mouseX <= this.getX() + this.getWidth() + 4 && mouseY >= this.getY() - 3 && mouseY <= this.getY() + 13) {
            if (mouseButton == 0) {
                this.setDragging(true);
                this.dragX = mouseX - this.getX();
                this.dragY = mouseY - this.getY();
            }
            if (mouseButton == 1) {
                this.open = !this.open;
            }
        }
        if (this.isOpen()) {
            for (final Component component : this.components) {
                component.mouseClicked(mouseX, mouseY, mouseButton);
            }
        }
    }
    
    public void mouseReleased(final int mouseX, final int mouseY, final int state) {
        this.setDragging(false);
        for (final Component component : this.components) {
            component.mouseReleased(mouseX, mouseY, state);
        }
    }
    
    public void keyTyped(final char typedChar, final int keyCode) {
        if (this.isOpen()) {
            for (final Component component : this.components) {
                if (component.isVisible()) {
                    component.keyTyped(typedChar, keyCode);
                }
            }
        }
    }
    
    public void refresh() {
        int offset = 16;
        for (final Component component : this.components) {
            if (component.isVisible()) {
                component.setOffset(offset);
                offset += 16;
                if (!(component instanceof ModuleComponent)) {
                    continue;
                }
                final ModuleComponent moduleComponent = (ModuleComponent)component;
                if (moduleComponent.getModule().getValues().isEmpty() || !moduleComponent.isOpen()) {
                    continue;
                }
                for (final Component valueComponent : moduleComponent.getComponents()) {
                    if (valueComponent.isVisible()) {
                        valueComponent.setOffset(offset);
                        offset += ((valueComponent instanceof ColorComponentTest && ((ColorComponentTest)valueComponent).isOpen()) ? 190 : 14);
                    }
                }
            }
        }
        this.setHeight(offset);
    }
    
    public void onGuiClosed() {
        for (final Component component : this.components) {
            component.onGuiClosed();
        }
    }
    
    public boolean isDragging() {
        return this.dragging;
    }
    
    public void setDragging(final boolean dragging) {
        this.dragging = dragging;
    }
    
    public boolean isOpen() {
        return this.open;
    }
    
    public int getX() {
        return this.x;
    }
    
    public void setX(final int x) {
        this.x = x;
    }
    
    public int getY() {
        return this.y;
    }
    
    public void setY(final int y) {
        this.y = y;
    }
    
    public int getWidth() {
        return this.width;
    }
    
    public int getHeight() {
        return this.height;
    }
    
    public void setHeight(final int height) {
        this.height = height;
    }
    
    public ArrayList<Component> getComponents() {
        return this.components;
    }
}
