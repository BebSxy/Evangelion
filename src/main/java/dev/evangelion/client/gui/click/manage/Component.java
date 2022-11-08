package dev.evangelion.client.gui.click.manage;

import dev.evangelion.api.utilities.IMinecraft;

public class Component implements IMinecraft
{
    private boolean visible;
    private int offset;
    private final int width;
    private final int height;
    private final Frame parent;
    
    public Component(final int offset, final Frame parent) {
        this.offset = offset;
        this.parent = parent;
        this.width = parent.getWidth();
        this.height = 14;
        this.visible = true;
    }
    
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
    }
    
    public void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) {
    }
    
    public void mouseReleased(final int mouseX, final int mouseY, final int state) {
    }
    
    public void keyTyped(final char typedChar, final int keyCode) {
    }
    
    public void onGuiClosed() {
    }
    
    public void update(final int mouseX, final int mouseY, final float partialTicks) {
    }
    
    public boolean isHovering(final int mouseX, final int mouseY) {
        return mouseX >= this.getX() && mouseX <= this.getX() + this.getWidth() && mouseY >= this.getY() && mouseY <= this.getY() + this.getHeight();
    }
    
    public boolean isVisible() {
        return this.visible;
    }
    
    public void setVisible(final boolean visible) {
        this.visible = visible;
    }
    
    public int getOffset() {
        return this.offset;
    }
    
    public void setOffset(final int offset) {
        this.offset = offset;
    }
    
    public int getX() {
        return this.parent.getX();
    }
    
    public int getY() {
        return this.parent.getY() + this.offset;
    }
    
    public int getWidth() {
        return this.width;
    }
    
    public int getHeight() {
        return this.height;
    }
    
    public Frame getParent() {
        return this.parent;
    }
}
