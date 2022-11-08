package dev.evangelion.client.gui.click.components;

import dev.evangelion.Evangelion;
import dev.evangelion.api.utilities.RenderUtils;
import java.awt.Color;
import dev.evangelion.client.gui.click.manage.Frame;
import dev.evangelion.client.values.impl.ValueColor;
import dev.evangelion.client.gui.click.manage.Component;

public class ColorComponentTest extends Component
{
    private final ValueColor value;
    public boolean open;
    boolean hueDragging;
    float hueWidth;
    float saturationWidth;
    float brightnessHeight;
    boolean hsDragging;
    boolean alphaDragging;
    float alphaWidth;
    
    public ColorComponentTest(final ValueColor value, final int offset, final Frame parent) {
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
            RenderUtils.drawOutline(this.getX() + 2 + this.hueWidth, (float)(this.getY() + 16), this.getX() + 2 + this.hueWidth + 2.0f, (float)(this.getY() + 27), 0.5f, Color.WHITE);
            RenderUtils.drawSidewaysGradient((float)(this.getX() + 2), (float)(this.getY() + 29), (float)(this.getX() + this.getWidth() - 2), (float)(this.getY() + 25 + this.getWidth()), new Color(255, 255, 255), color);
            Evangelion.CLICK_GUI.drawGradientRect(this.getX() + 2, this.getY() + 29, this.getX() + this.getWidth() - 2, this.getY() + 25 + this.getWidth(), new Color(0, 0, 0, 0).getRGB(), new Color(0, 0, 0).getRGB());
            RenderUtils.drawOutline((float)(this.getX() + 2), (float)(this.getY() + 29), (float)(this.getX() + this.getWidth() - 2), (float)(this.getY() + 25 + this.getWidth()), 0.5f, Color.BLACK);
            RenderUtils.drawOutline(this.getX() + 2 + this.saturationWidth, this.getY() + 29 - this.brightnessHeight + 96.0f, this.getX() + 2 + this.saturationWidth + 2.0f, this.getY() + 29 - this.brightnessHeight + 2.0f + 96.0f, 0.8f, Color.BLACK);
            RenderUtils.drawOutline(this.getX() + 2 + this.saturationWidth, this.getY() + 29 - this.brightnessHeight + 96.0f, this.getX() + 2 + this.saturationWidth + 2.0f, this.getY() + 29 - this.brightnessHeight + 2.0f + 96.0f, 0.5f, Color.WHITE);
            RenderUtils.drawSidewaysGradient((float)(this.getX() + 2), (float)(this.getY() + 28 + this.getWidth()), (float)(this.getX() + this.getWidth() - 2), (float)(this.getY() + 39 + this.getWidth()), new Color(color.getRed(), color.getGreen(), color.getBlue(), 0), color);
            RenderUtils.drawOutline((float)(this.getX() + 2), (float)(this.getY() + 28 + this.getWidth()), (float)(this.getX() + this.getWidth() - 2), (float)(this.getY() + 39 + this.getWidth()), 0.5f, Color.BLACK);
            RenderUtils.drawOutline(this.getX() + 2 + this.alphaWidth, (float)(this.getY() + 28 + this.getWidth()), this.getX() + 2 + this.alphaWidth + 2.0f, (float)(this.getY() + 39 + this.getWidth()), 0.5f, Color.WHITE);
            RenderUtils.drawRect((float)(this.getX() + 2), (float)(this.getY() + 42 + this.getWidth()), (float)(this.getX() + 49), (float)(this.getY() + 53 + this.getWidth()), Evangelion.CLICK_GUI.getColor());
            Evangelion.FONT_MANAGER.drawStringWithShadow("Copy", this.getX() + 25 - Evangelion.FONT_MANAGER.getStringWidth("Copy") / 2.0f, this.getY() + 47.5f + this.getWidth() - Evangelion.FONT_MANAGER.getHeight() / 2.0f, Color.WHITE);
            RenderUtils.drawRect((float)(this.getX() + 51), (float)(this.getY() + 42 + this.getWidth()), (float)(this.getX() + 98), (float)(this.getY() + 53 + this.getWidth()), Evangelion.CLICK_GUI.getColor());
            Evangelion.FONT_MANAGER.drawStringWithShadow("Paste", this.getX() + 75 - Evangelion.FONT_MANAGER.getStringWidth("Paste") / 2.0f, this.getY() + 47.5f + this.getWidth() - Evangelion.FONT_MANAGER.getHeight() / 2.0f, Color.WHITE);
            if (this.value.isRainbow()) {
                RenderUtils.drawRect((float)(this.getX() + 2), (float)(this.getY() + 56 + this.getWidth()), (float)(this.getX() + this.getWidth() - 2), (float)(this.getY() + 70 + this.getWidth()), Evangelion.CLICK_GUI.getColor());
            }
            Evangelion.FONT_MANAGER.drawStringWithShadow("Rainbow", this.getX() + 48 - Evangelion.FONT_MANAGER.getStringWidth("Rainbow") / 2.0f, (float)(this.getY() + 59 + this.getWidth()), Color.WHITE);
            if (this.value.isSync()) {
                RenderUtils.drawRect((float)(this.getX() + 2), (float)(this.getY() + 73 + this.getWidth()), (float)(this.getX() + this.getWidth() - 2), (float)(this.getY() + 87 + this.getWidth()), Evangelion.CLICK_GUI.getColor());
            }
            Evangelion.FONT_MANAGER.drawStringWithShadow("Sync", this.getX() + 48 - Evangelion.FONT_MANAGER.getStringWidth("Sync") / 2.0f, (float)(this.getY() + 76 + this.getWidth()), Color.WHITE);
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
                if (this.isHoveringHS(mouseX, mouseY)) {
                    this.hsDragging = true;
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
        this.hsDragging = false;
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
        final double differenceY = Math.min(95, Math.max(0, mouseY - this.getY() - 29));
        this.hueWidth = 95.5f * (hsb[0] * 360.0f / 360.0f);
        this.saturationWidth = 94.5f * (hsb[1] * 360.0f / 360.0f);
        this.brightnessHeight = 94.5f * (hsb[2] * 360.0f / 360.0f);
        this.alphaWidth = 94.5f * (this.value.getValue().getAlpha() / 255.0f);
        this.changeColor(difference, new Color(Color.HSBtoRGB((float)(difference / 95.0 * 360.0 / 360.0), hsb[1], hsb[2])), new Color(Color.HSBtoRGB(0.0f, hsb[1], hsb[2])), this.hueDragging);
        this.changeHS(difference, differenceY, new Color(Color.HSBtoRGB(hsb[0], (float)(difference / 95.0 * 360.0 / 360.0), 1.0f - (float)(differenceY / 95.0 * 360.0 / 360.0))), new Color(Color.HSBtoRGB(hsb[0], 0.0f, 0.0f)), this.hsDragging);
        this.changeAlpha(difference, (float)(difference / 95.0 * 255.0 / 255.0), this.alphaDragging);
    }
    
    private void changeHS(final double difference, final double difference2, final Color color, final Color zeroColor, final boolean dragging) {
        if (dragging) {
            if (difference == 0.0 && difference2 == 0.0) {
                this.value.setValue(new Color(zeroColor.getRed(), zeroColor.getGreen(), zeroColor.getBlue(), this.value.getValue().getAlpha()));
            }
            else {
                this.value.setValue(new Color(color.getRed(), color.getGreen(), color.getBlue(), this.value.getValue().getAlpha()));
            }
        }
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
        return mouseX > this.getX() + 2 && mouseX < this.getX() + this.getWidth() - 2 && mouseY > this.getY() + 16 && mouseY < this.getY() + 27;
    }
    
    public boolean isHoveringHS(final int mouseX, final int mouseY) {
        return mouseX > this.getX() + 2 && mouseX < this.getX() + this.getWidth() - 2 && mouseY > this.getY() + 29 && mouseY < this.getY() + 25 + this.getWidth();
    }
    
    public boolean isHoveringAlpha(final int mouseX, final int mouseY) {
        return mouseX > this.getX() + 2 && mouseX < this.getX() + this.getWidth() - 2 && mouseY > this.getY() + 28 + this.getWidth() && mouseY < this.getY() + 39 + this.getWidth();
    }
    
    public boolean isHoveringCopy(final int mouseX, final int mouseY) {
        return mouseX > this.getX() + 2 && mouseX < this.getX() + 49 && mouseY > this.getY() + 42 + this.getWidth() && mouseY < this.getY() + 53 + this.getWidth();
    }
    
    public boolean isHoveringPaste(final int mouseX, final int mouseY) {
        return mouseX > this.getX() + 51 && mouseX < this.getX() + 98 && mouseY > this.getY() + 42 + this.getWidth() && mouseY < this.getY() + 53 + this.getWidth();
    }
    
    public boolean isHoveringRainbow(final int mouseX, final int mouseY) {
        return mouseX > this.getX() + 2 && mouseX < this.getX() + 98 && mouseY > this.getY() + 56 + this.getWidth() && mouseY < this.getY() + 70 + this.getWidth();
    }
    
    public boolean isHoveringSync(final int mouseX, final int mouseY) {
        return mouseX > this.getX() + 2 && mouseX < this.getX() + 98 && mouseY > this.getY() + 73 + this.getWidth() && mouseY < this.getY() + 87 + this.getWidth();
    }
}
