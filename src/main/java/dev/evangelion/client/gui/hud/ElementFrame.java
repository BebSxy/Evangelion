package dev.evangelion.client.gui.hud;

import dev.evangelion.client.events.EventRender2D;
import dev.evangelion.api.utilities.RenderUtils;
import java.awt.Color;
import net.minecraft.client.gui.ScaledResolution;
import dev.evangelion.Evangelion;
import dev.evangelion.api.manager.element.Element;
import dev.evangelion.api.utilities.IMinecraft;

public class ElementFrame implements IMinecraft
{
    private final Element element;
    private float x;
    private float y;
    private float width;
    private float height;
    private float dragX;
    private float dragY;
    private boolean dragging;
    private boolean visible;
    private HudEditorScreen parent;
    
    public ElementFrame(final Element element, final float x, final float y, final float width, final float height, final HudEditorScreen parent) {
        this.element = element;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.parent = parent;
        this.dragging = false;
        this.visible = true;
    }
    
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        if (this.element != null && Evangelion.ELEMENT_MANAGER.isElementEnabled(this.element.getName())) {
            if (this.dragging) {
                this.x = this.dragX + mouseX;
                this.y = this.dragY + mouseY;
                final ScaledResolution resolution = new ScaledResolution(ElementFrame.mc);
                if (this.x < 0.0) {
                    this.x = 0.0f;
                }
                if (this.y < 0.0) {
                    this.y = 0.0f;
                }
                if (this.x > resolution.getScaledWidth() - this.width) {
                    this.x = resolution.getScaledWidth() - this.width;
                }
                if (this.y > resolution.getScaledHeight() - this.height) {
                    this.y = resolution.getScaledHeight() - this.height;
                }
            }
            if (this.dragging) {
                RenderUtils.drawRect(this.x, this.y, this.x + this.width, this.y + this.height, new Color(Color.DARK_GRAY.getRed(), Color.DARK_GRAY.getGreen(), Color.DARK_GRAY.getBlue(), 100));
            }
            else {
                RenderUtils.drawRect(this.x, this.y, this.x + this.width, this.y + this.height, new Color(Color.BLACK.getRed(), Color.BLACK.getGreen(), Color.BLACK.getBlue(), 100));
            }
            this.element.onRender2D(new EventRender2D(partialTicks));
        }
    }
    
    public void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) {
        if (mouseButton == 0 && this.isHovering(mouseX, mouseY)) {
            this.dragX = this.x - mouseX;
            this.dragY = this.y - mouseY;
            this.dragging = true;
        }
    }
    
    public void mouseReleased(final int mouseX, final int mouseY, final int state) {
        this.dragging = false;
    }
    
    public boolean isHovering(final int mouseX, final int mouseY) {
        return mouseX >= this.x && mouseX <= this.x + this.width && mouseY >= this.y && mouseY <= this.y + this.height;
    }
    
    public Element getElement() {
        return this.element;
    }
    
    public HudEditorScreen getParent() {
        return this.parent;
    }
    
    public float getX() {
        return this.x;
    }
    
    public void setX(final float x) {
        this.x = x;
    }
    
    public float getY() {
        return this.y;
    }
    
    public void setY(final float y) {
        this.y = y;
    }
    
    public float getWidth() {
        return this.width;
    }
    
    public void setWidth(final float width) {
        this.width = width;
    }
    
    public float getHeight() {
        return this.height;
    }
    
    public void setHeight(final float height) {
        this.height = height;
    }
    
    public boolean isDragging() {
        return this.dragging;
    }
    
    public void setDragging(final boolean dragging) {
        this.dragging = dragging;
    }
    
    public boolean isVisible() {
        return this.visible;
    }
    
    public void setVisible(final boolean visible) {
        this.visible = visible;
    }
}
