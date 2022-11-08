package dev.evangelion.client.gui.click.components;

import java.awt.Color;
import dev.evangelion.Evangelion;
import dev.evangelion.client.gui.click.manage.Frame;
import dev.evangelion.client.values.impl.ValueCategory;
import dev.evangelion.client.gui.click.manage.Component;

public class CategoryComponent extends Component
{
    private final ValueCategory value;
    
    public CategoryComponent(final ValueCategory value, final int offset, final Frame parent) {
        super(offset, parent);
        this.value = value;
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        Evangelion.FONT_MANAGER.drawStringWithShadow(this.value.getName(), (float)(this.getX() + 3), (float)(this.getY() + 3), Color.WHITE);
        Evangelion.FONT_MANAGER.drawStringWithShadow(this.value.isOpen() ? "-" : "+", this.getX() + this.getWidth() - 3 - Evangelion.FONT_MANAGER.getStringWidth(this.value.isOpen() ? "+" : "-"), (float)(this.getY() + 3), Color.WHITE);
    }
    
    @Override
    public void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) {
        if (this.isHovering(mouseX, mouseY) && mouseButton == 1) {
            this.value.setOpen(!this.value.isOpen());
            this.getParent().refresh();
        }
    }
}
