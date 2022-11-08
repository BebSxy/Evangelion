package dev.evangelion.client.gui.click.components;

import java.awt.Color;
import org.lwjgl.input.Keyboard;
import com.mojang.realmsclient.gui.ChatFormatting;
import dev.evangelion.Evangelion;
import dev.evangelion.client.gui.click.manage.Frame;
import dev.evangelion.client.values.impl.ValueBind;
import dev.evangelion.client.gui.click.manage.Component;

public class BindComponent extends Component
{
    private final ValueBind value;
    private boolean binding;
    
    public BindComponent(final ValueBind value, final int offset, final Frame parent) {
        super(offset, parent);
        this.value = value;
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        Evangelion.FONT_MANAGER.drawStringWithShadow(this.value.getTag() + " " + ChatFormatting.GRAY + (this.binding ? "..." : Keyboard.getKeyName(this.value.getValue()).toUpperCase()), (float)(this.getX() + 3), (float)(this.getY() + 3), Color.WHITE);
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
        super.mouseClicked(mouseX, mouseY, mouseButton);
        if (mouseButton == 0 && mouseX >= this.getX() && mouseX <= this.getX() + this.getWidth() && mouseY >= this.getY() && mouseY <= this.getY() + this.getHeight()) {
            this.binding = !this.binding;
        }
    }
    
    @Override
    public void keyTyped(final char typedChar, final int keyCode) {
        super.keyTyped(typedChar, keyCode);
        if (this.binding) {
            if (keyCode == 211) {
                this.value.setValue(0);
            }
            else if (keyCode != 1) {
                this.value.setValue(keyCode);
            }
            this.binding = false;
        }
    }
}
