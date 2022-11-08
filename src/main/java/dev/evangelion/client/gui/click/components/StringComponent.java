package dev.evangelion.client.gui.click.components;

import net.minecraft.util.ChatAllowedCharacters;
import java.awt.datatransfer.DataFlavor;
import java.awt.Toolkit;
import org.lwjgl.input.Keyboard;
import java.awt.Color;
import dev.evangelion.api.utilities.RenderUtils;
import dev.evangelion.Evangelion;
import dev.evangelion.client.gui.click.manage.Frame;
import dev.evangelion.api.utilities.TimerUtils;
import dev.evangelion.client.values.impl.ValueString;
import dev.evangelion.client.gui.click.manage.Component;

public class StringComponent extends Component
{
    private final ValueString value;
    private boolean listening;
    private String currentString;
    private final TimerUtils timer;
    private final TimerUtils backTimer;
    private boolean selecting;
    private boolean line;
    
    public StringComponent(final ValueString value, final int offset, final Frame parent) {
        super(offset, parent);
        this.currentString = "";
        this.timer = new TimerUtils();
        this.backTimer = new TimerUtils();
        this.selecting = false;
        this.line = false;
        this.value = value;
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        if (this.timer.hasTimeElapsed(400L)) {
            this.line = !this.line;
            this.timer.reset();
        }
        RenderUtils.drawRect((float)(this.getX() + 1), (float)this.getY(), (float)(this.getX() + this.getWidth() - 1), (float)(this.getY() + 14), Evangelion.CLICK_GUI.getColor());
        if (this.selecting) {
            RenderUtils.drawRect((float)(this.getX() + 3), (float)(this.getY() + 3), this.getX() + 3 + Evangelion.FONT_MANAGER.getStringWidth(this.currentString), this.getY() + Evangelion.FONT_MANAGER.getHeight() + 3.0f, new Color(Color.LIGHT_GRAY.getRed(), Color.LIGHT_GRAY.getGreen(), Color.LIGHT_GRAY.getBlue(), 100));
        }
        if (this.listening) {
            Evangelion.FONT_MANAGER.drawStringWithShadow(this.currentString + (this.selecting ? "" : (this.line ? (Evangelion.MODULE_MANAGER.isModuleEnabled("Font") ? "|" : "\u23d0") : "")), (float)(this.getX() + 3), (float)(this.getY() + 3), Color.LIGHT_GRAY);
        }
        else {
            Evangelion.FONT_MANAGER.drawStringWithShadow(this.value.getValue(), (float)(this.getX() + 3), (float)(this.getY() + 3), Color.LIGHT_GRAY);
        }
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
        if (mouseButton == 0 && this.isHovering(mouseX, mouseY)) {
            this.listening = !this.listening;
            this.currentString = this.value.getValue();
        }
    }
    
    @Override
    public void keyTyped(final char typedChar, final int keyCode) {
        super.keyTyped(typedChar, keyCode);
        this.backTimer.reset();
        if (this.listening) {
            if (keyCode == 1) {
                this.selecting = false;
                return;
            }
            Label_0228: {
                if (keyCode == 28) {
                    this.updateString();
                    this.selecting = false;
                    this.listening = false;
                }
                else if (keyCode == 14) {
                    this.currentString = (this.selecting ? "" : this.removeLastCharacter(this.currentString));
                    this.selecting = false;
                }
                else {
                    Label_0162: {
                        if (keyCode == 47) {
                            if (!Keyboard.isKeyDown(157)) {
                                if (!Keyboard.isKeyDown(29)) {
                                    break Label_0162;
                                }
                            }
                            try {
                                this.currentString += Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor);
                            }
                            catch (Exception exception) {
                                exception.printStackTrace();
                            }
                            break Label_0228;
                        }
                    }
                    if (ChatAllowedCharacters.isAllowedCharacter(typedChar)) {
                        this.currentString = (this.selecting ? ("" + typedChar) : (this.currentString + typedChar));
                        this.selecting = false;
                    }
                }
            }
            if (keyCode == 30 && Keyboard.isKeyDown(29)) {
                this.selecting = true;
            }
        }
    }
    
    private void updateString() {
        if (this.currentString.length() > 0) {
            this.value.setValue(this.currentString);
        }
        this.currentString = "";
    }
    
    private String removeLastCharacter(final String input) {
        if (input.length() > 0) {
            return input.substring(0, input.length() - 1);
        }
        return input;
    }
    
    public ValueString getValue() {
        return this.value;
    }
}
