package dev.evangelion.client.gui.click.components;

import java.awt.Color;
import com.mojang.realmsclient.gui.ChatFormatting;
import dev.evangelion.api.utilities.RenderUtils;
import dev.evangelion.Evangelion;
import dev.evangelion.client.gui.click.manage.Frame;
import dev.evangelion.client.values.impl.ValueEnum;
import dev.evangelion.client.gui.click.manage.Component;

public class EnumComponent extends Component
{
    private final ValueEnum value;
    private int enumSize;
    
    public EnumComponent(final ValueEnum value, final int offset, final Frame parent) {
        super(offset, parent);
        this.value = value;
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        RenderUtils.drawRect((float)(this.getX() + 1), (float)this.getY(), (float)(this.getX() + this.getWidth() - 1), (float)(this.getY() + 14), Evangelion.CLICK_GUI.getColor());
        Evangelion.FONT_MANAGER.drawStringWithShadow(this.value.getTag() + " " + ChatFormatting.GRAY + this.value.getValue().toString(), (float)(this.getX() + 3), (float)(this.getY() + 3), Color.WHITE);
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
        if (mouseX >= this.getX() && mouseX <= this.getX() + this.getWidth() && mouseY >= this.getY() && mouseY <= this.getY() + this.getHeight()) {
            if (mouseButton == 0) {
                final int maxIndex = this.value.getEnums().size() - 1;
                ++this.enumSize;
                if (this.enumSize > maxIndex) {
                    this.enumSize = 0;
                }
                this.value.setValue(this.value.getEnums().get(this.enumSize));
            }
            else if (mouseButton == 1) {
                final int maxIndex = this.value.getEnums().size() - 1;
                --this.enumSize;
                if (this.enumSize < 0) {
                    this.enumSize = maxIndex;
                }
                this.value.setValue(this.value.getEnums().get(this.enumSize));
            }
        }
    }
    
    public ValueEnum getValue() {
        return this.value;
    }
}
