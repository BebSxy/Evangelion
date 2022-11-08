package dev.evangelion.client.gui.click.components;

import dev.evangelion.Evangelion;
import dev.evangelion.client.modules.client.ModuleColor;
import dev.evangelion.api.utilities.RenderUtils;
import java.awt.Color;
import dev.evangelion.client.gui.click.manage.Frame;
import dev.evangelion.client.values.impl.ValueColor;
import dev.evangelion.client.gui.click.manage.Component;

public class ColorComponent extends Component
{
    private final ValueColor value;
    public boolean open;
    boolean hueDragging;
    float hueWidth;
    boolean saturationDragging;
    float saturationWidth;
    boolean brightnessDragging;
    float brightnessWidth;
    boolean alphaDragging;
    float alphaWidth;
    
    public ColorComponent(final ValueColor value, final int offset, final Frame parent) {
        super(offset, parent);
        this.open = false;
        this.value = value;
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        final float[] hsb = Color.RGBtoHSB(this.value.getValue().getRed(), this.value.getValue().getGreen(), this.value.getValue().getBlue(), null);
        final Color color = Color.getHSBColor(hsb[0], 1.0f, 1.0f);
        RenderUtils.drawRect((float)(this.getX() + this.getWidth() - 12), (float)(this.getY() + 2), (float)(this.getX() + this.getWidth() - 2), (float)(this.getY() + 12), this.value.getValue());
        RenderUtils.drawOutline((float)(this.getX() + this.getWidth() - 12), (float)(this.getY() + 2), (float)(this.getX() + this.getWidth() - 2), (float)(this.getY() + 12), 0.5f, new Color(20, 20, 20));
        if (this.isOpen()) {
            for (float i = 0.0f; i + 1.0f < 96.0f; i += 0.45f) {
                RenderUtils.drawRect(this.getX() + 2 + i, (float)(this.getY() + 16), this.getX() + 2 + i + 1.0f, (float)(this.getY() + 27), Color.getHSBColor(i / 96.0f, 1.0f, 1.0f));
            }
            RenderUtils.drawOutline((float)(this.getX() + 2), (float)(this.getY() + 16), (float)(this.getX() + this.getWidth() - 2), (float)(this.getY() + 27), 0.5f, Color.BLACK);
            RenderUtils.drawRect(this.getX() + 2 + this.hueWidth, (float)(this.getY() + 16), this.getX() + 2 + this.hueWidth + 1.0f, (float)(this.getY() + 27), Color.WHITE);
            RenderUtils.drawSidewaysGradient((float)(this.getX() + 2), (float)(this.getY() + 29), (float)(this.getX() + this.getWidth() - 2), (float)(this.getY() + 40), new Color(255, 255, 255), color);
            RenderUtils.drawOutline((float)(this.getX() + 2), (float)(this.getY() + 29), (float)(this.getX() + this.getWidth() - 2), (float)(this.getY() + 40), 0.5f, Color.BLACK);
            RenderUtils.drawRect(this.getX() + 2 + this.saturationWidth, (float)(this.getY() + 29), this.getX() + 2 + this.saturationWidth + 1.0f, (float)(this.getY() + 40), Color.WHITE);
            RenderUtils.drawSidewaysGradient((float)(this.getX() + 2), (float)(this.getY() + 42), (float)(this.getX() + this.getWidth() - 2), (float)(this.getY() + 53), new Color(0, 0, 0), color);
            RenderUtils.drawOutline((float)(this.getX() + 2), (float)(this.getY() + 42), (float)(this.getX() + this.getWidth() - 2), (float)(this.getY() + 53), 0.5f, Color.BLACK);
            RenderUtils.drawRect(this.getX() + 2 + this.brightnessWidth, (float)(this.getY() + 42), this.getX() + 2 + this.brightnessWidth + 1.0f, (float)(this.getY() + 53), Color.WHITE);
            RenderUtils.drawSidewaysGradient((float)(this.getX() + 2), (float)(this.getY() + 55), (float)(this.getX() + this.getWidth() - 2), (float)(this.getY() + 66), new Color(color.getRed(), color.getGreen(), color.getBlue(), 0), color);
            RenderUtils.drawOutline((float)(this.getX() + 2), (float)(this.getY() + 55), (float)(this.getX() + this.getWidth() - 2), (float)(this.getY() + 66), 0.5f, Color.BLACK);
            RenderUtils.drawRect(this.getX() + 2 + this.alphaWidth, (float)(this.getY() + 55), this.getX() + 2 + this.alphaWidth + 1.0f, (float)(this.getY() + 66), Color.WHITE);
            RenderUtils.drawRect((float)(this.getX() + 2), (float)(this.getY() + 68), (float)(this.getX() + 54), this.getY() + 79.5f, ModuleColor.getColor());
            RenderUtils.drawOutline((float)(this.getX() + 2), (float)(this.getY() + 68), (float)(this.getX() + 54), this.getY() + 79.5f, 0.5f, Color.BLACK);
            Evangelion.FONT_MANAGER.drawStringWithShadow("Copy", this.getX() + 27 - Evangelion.FONT_MANAGER.getStringWidth("Copy") / 2.0f, this.getY() + 69.5f, Color.WHITE);
            RenderUtils.drawRect((float)(this.getX() + 56), (float)(this.getY() + 68), (float)(this.getX() + this.getWidth() - 2), this.getY() + 79.5f, ModuleColor.getColor());
            RenderUtils.drawOutline((float)(this.getX() + 56), (float)(this.getY() + 68), (float)(this.getX() + this.getWidth() - 2), this.getY() + 79.5f, 0.5f, Color.BLACK);
            Evangelion.FONT_MANAGER.drawStringWithShadow("Paste", this.getX() + 82 - Evangelion.FONT_MANAGER.getStringWidth("Paste") / 2.0f, this.getY() + 69.5f, Color.WHITE);
            if (this.value.isRainbow()) {
                RenderUtils.drawRect((float)(this.getX() + 2), this.getY() + 81.5f, (float)(this.getX() + this.getWidth() - 2), this.getY() + 95.5f, Evangelion.CLICK_GUI.getColor());
            }
            Evangelion.FONT_MANAGER.drawStringWithShadow("Rainbow", (float)(this.getX() + 3), this.getY() + 84.5f, Color.WHITE);
            if (this.value.isSync()) {
                RenderUtils.drawRect((float)(this.getX() + 2), this.getY() + 97.5f, (float)(this.getX() + this.getWidth() - 2), this.getY() + 111.5f, Evangelion.CLICK_GUI.getColor());
            }
            Evangelion.FONT_MANAGER.drawStringWithShadow("Sync", (float)(this.getX() + 3), this.getY() + 100.5f, Color.WHITE);
        }
        Evangelion.FONT_MANAGER.drawStringWithShadow(this.value.getTag(), (float)(this.getX() + 3), (float)(this.getY() + 3), Color.WHITE);
    }
    
    @Override
    public void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        if (mouseButton == 0) {
            if (this.isOpen()) {
                if (this.isHoveringHue(mouseX, mouseY)) {
                    this.hueDragging = true;
                }
                if (this.isHoveringSaturation(mouseX, mouseY)) {
                    this.saturationDragging = true;
                }
                if (this.isHoveringBrightness(mouseX, mouseY)) {
                    this.brightnessDragging = true;
                }
                if (this.isHoveringAlpha(mouseX, mouseY)) {
                    this.alphaDragging = true;
                }
                if (this.isHoveringCopy(mouseX, mouseY)) {
                    Evangelion.COLOR_CLIPBOARD = this.value.getActualValue();
                }
                if (this.isHoveringPaste(mouseX, mouseY) && Evangelion.COLOR_CLIPBOARD != null) {
                    this.value.setValue(Evangelion.COLOR_CLIPBOARD);
                }
                if (this.isHoveringRainbow(mouseX, mouseY)) {
                    this.value.setRainbow(!this.value.isRainbow());
                }
                if (this.isHoveringSync(mouseX, mouseY)) {
                    this.value.setSync(!this.value.isSync());
                }
            }
        }
        else if (mouseButton == 1 && this.isHovering(mouseX, mouseY)) {
            this.open = !this.open;
        }
    }
    
    @Override
    public void mouseReleased(final int mouseX, final int mouseY, final int state) {
        this.hueDragging = false;
        this.saturationDragging = false;
        this.brightnessDragging = false;
        this.alphaDragging = false;
    }
    
    @Override
    public void update(final int mouseX, final int mouseY, final float partialTicks) {
        super.update(mouseX, mouseY, partialTicks);
        if (this.value.getParent() != null) {
            this.setVisible(this.value.getParent().isOpen());
        }
        final float[] hsb = Color.RGBtoHSB(this.value.getValue().getRed(), this.value.getValue().getGreen(), this.value.getValue().getBlue(), null);
        final double difference = Math.min(95, Math.max(0, mouseX - this.getX()));
        this.hueWidth = 95.5f * (hsb[0] * 360.0f / 360.0f);
        this.saturationWidth = 94.5f * (hsb[1] * 360.0f / 360.0f);
        this.brightnessWidth = 94.5f * (hsb[2] * 360.0f / 360.0f);
        this.alphaWidth = 94.5f * (this.value.getValue().getAlpha() / 255.0f);
        this.changeColor(difference, new Color(Color.HSBtoRGB((float)(difference / 95.0 * 360.0 / 360.0), hsb[1], hsb[2])), new Color(Color.HSBtoRGB(0.0f, hsb[1], hsb[2])), this.hueDragging);
        this.changeColor(difference, new Color(Color.HSBtoRGB(hsb[0], (float)(difference / 95.0 * 360.0 / 360.0), hsb[2])), new Color(Color.HSBtoRGB(hsb[0], 0.0f, hsb[2])), this.saturationDragging);
        this.changeColor(difference, new Color(Color.HSBtoRGB(hsb[0], hsb[1], (float)(difference / 95.0 * 360.0 / 360.0))), new Color(Color.HSBtoRGB(hsb[0], hsb[1], 0.0f)), this.brightnessDragging);
        this.changeAlpha(difference, (float)(difference / 95.0 * 255.0 / 255.0), this.alphaDragging);
    }
    
    private void changeColor(final double difference, final Color color, final Color zeroColor, final boolean dragging) {
        if (dragging) {
            if (difference == 0.0) {
                this.value.setValue(new Color(zeroColor.getRed(), zeroColor.getGreen(), zeroColor.getBlue(), this.value.getValue().getAlpha()));
            }
            else {
                this.value.setValue(new Color(color.getRed(), color.getGreen(), color.getBlue(), this.value.getValue().getAlpha()));
            }
        }
    }
    
    private void changeAlpha(final double difference, final float alpha, final boolean dragging) {
        if (dragging) {
            if (difference == 0.0) {
                this.value.setValue(new Color(this.value.getValue().getRed(), this.value.getValue().getGreen(), this.value.getValue().getBlue(), 0));
            }
            else {
                this.value.setValue(new Color(this.value.getValue().getRed(), this.value.getValue().getGreen(), this.value.getValue().getBlue(), (int)(alpha * 255.0f)));
            }
        }
    }
    
    public boolean isOpen() {
        return this.open;
    }
    
    public boolean isHoveringHue(final int mouseX, final int mouseY) {
        return mouseX > this.getX() + 2 && mouseX < this.getX() + 2 + this.getWidth() - 4 && mouseY > this.getY() + 16 && mouseY < this.getY() + 27;
    }
    
    public boolean isHoveringSaturation(final int mouseX, final int mouseY) {
        return mouseX > this.getX() + 2 && mouseX < this.getX() + 2 + this.getWidth() - 4 && mouseY > this.getY() + 29 && mouseY < this.getY() + 40;
    }
    
    public boolean isHoveringBrightness(final int mouseX, final int mouseY) {
        return mouseX > this.getX() + 2 && mouseX < this.getX() + 2 + this.getWidth() - 4 && mouseY > this.getY() + 42 && mouseY < this.getY() + 53;
    }
    
    public boolean isHoveringAlpha(final int mouseX, final int mouseY) {
        return mouseX > this.getX() + 2 && mouseX < this.getX() + 2 + this.getWidth() - 4 && mouseY > this.getY() + 55 && mouseY < this.getY() + 66;
    }
    
    public boolean isHoveringCopy(final int mouseX, final int mouseY) {
        return mouseX > this.getX() + 2 && mouseX < this.getX() + 54 && mouseY > this.getY() + 68 && mouseY < this.getY() + 79.5f;
    }
    
    public boolean isHoveringPaste(final int mouseX, final int mouseY) {
        return mouseX > this.getX() + 56 && mouseX < this.getX() + this.getWidth() - 2 && mouseY > this.getY() + 68 && mouseY < this.getY() + 79.5f;
    }
    
    public boolean isHoveringRainbow(final int mouseX, final int mouseY) {
        return mouseX > this.getX() + 2 && mouseX < this.getX() + this.getWidth() - 2 && mouseY > this.getY() + 81.5f && mouseY < this.getY() + 95.5f;
    }
    
    public boolean isHoveringSync(final int mouseX, final int mouseY) {
        return mouseX > this.getX() + 2 && mouseX < this.getX() + this.getWidth() - 2 && mouseY > this.getY() + 97.5f && mouseY < this.getY() + 111.5f;
    }
}
