package dev.evangelion.client.gui.click.components;

import dev.evangelion.client.modules.client.ModuleColor;
import org.lwjgl.opengl.GL11;
import java.awt.Color;
import dev.evangelion.api.utilities.RenderUtils;
import dev.evangelion.Evangelion;
import dev.evangelion.client.gui.click.manage.Frame;
import dev.evangelion.client.values.impl.ValueBoolean;
import dev.evangelion.client.gui.click.manage.Component;

public class BooleanComponent extends Component
{
    private final ValueBoolean value;
    
    public BooleanComponent(final ValueBoolean value, final int offset, final Frame parent) {
        super(offset, parent);
        this.value = value;
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        if (this.value.getValue()) {
            RenderUtils.drawRect((float)(this.getX() + 1), (float)this.getY(), (float)(this.getX() + this.getWidth() - 1), (float)(this.getY() + 14), Evangelion.CLICK_GUI.getColor());
        }
        Evangelion.FONT_MANAGER.drawStringWithShadow(this.value.getTag(), (float)(this.getX() + 3), (float)(this.getY() + 3), Color.WHITE);
    }
    
    private void prepareLine() {
        GL11.glBegin(1);
        GL11.glColor3f(ModuleColor.getColor().getRed() / 255.0f, ModuleColor.getColor().getGreen() / 255.0f, ModuleColor.getColor().getBlue() / 255.0f);
        GL11.glVertex2d((double)(this.getX() + this.getWidth() - 8), (double)(this.getY() + 10));
        GL11.glColor3f(ModuleColor.getColor().getRed() / 255.0f, ModuleColor.getColor().getGreen() / 255.0f, ModuleColor.getColor().getBlue() / 255.0f);
    }
    
    @Override
    public void update(final int mouseX, final int mouseY, final float partialTicks) {
        super.update(mouseX, mouseY, partialTicks);
        if (this.value.getParent() != null) {
            this.setVisible(this.value.getParent().isOpen());
        }
    }
    
    @Override
    public void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) {
        if (this.isHovering(mouseX, mouseY) && mouseButton == 0) {
            this.value.setValue(!this.value.getValue());
        }
    }
    
    public ValueBoolean getValue() {
        return this.value;
    }
}
