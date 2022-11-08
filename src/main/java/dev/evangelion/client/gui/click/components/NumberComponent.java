package dev.evangelion.client.gui.click.components;

import net.minecraft.util.ChatAllowedCharacters;
import java.awt.datatransfer.DataFlavor;
import java.awt.Toolkit;
import org.lwjgl.input.Keyboard;
import dev.evangelion.api.utilities.MathUtils;
import com.mojang.realmsclient.gui.ChatFormatting;
import java.awt.Color;
import dev.evangelion.api.utilities.RenderUtils;
import dev.evangelion.Evangelion;
import dev.evangelion.client.gui.click.manage.Frame;
import dev.evangelion.api.utilities.TimerUtils;
import dev.evangelion.client.values.impl.ValueNumber;
import dev.evangelion.client.gui.click.manage.Component;

public class NumberComponent extends Component
{
    private final ValueNumber value;
    private float sliderWidth;
    private boolean dragging;
    private boolean listening;
    private String currentString;
    private final TimerUtils timer;
    private final TimerUtils backTimer;
    private boolean selecting;
    private boolean line;
    
    public NumberComponent(final ValueNumber value, final int offset, final Frame parent) {
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
        RenderUtils.drawRect((float)(this.getX() + 1), (float)this.getY(), this.getX() + 1 + this.sliderWidth, (float)(this.getY() + 14), Evangelion.CLICK_GUI.getColor());
        if (this.selecting) {
            RenderUtils.drawRect(this.getX() + 3 + Evangelion.FONT_MANAGER.getStringWidth(this.value.getTag() + " "), (float)(this.getY() + 3), this.getX() + 3 + Evangelion.FONT_MANAGER.getStringWidth(this.value.getTag() + " ") + Evangelion.FONT_MANAGER.getStringWidth(this.currentString), this.getY() + Evangelion.FONT_MANAGER.getHeight() + 3.0f, new Color(Color.LIGHT_GRAY.getRed(), Color.LIGHT_GRAY.getGreen(), Color.LIGHT_GRAY.getBlue(), 100));
        }
        if (this.listening) {
            Evangelion.FONT_MANAGER.drawStringWithShadow(this.value.getTag() + " " + ChatFormatting.GRAY + this.currentString + (this.selecting ? "" : (this.line ? (Evangelion.MODULE_MANAGER.isModuleEnabled("Font") ? "|" : "\u23d0") : "")), (float)(this.getX() + 3), (float)(this.getY() + 3), Color.WHITE);
        }
        else {
            Evangelion.FONT_MANAGER.drawStringWithShadow(this.value.getTag() + " " + ChatFormatting.GRAY + this.value.getValue() + ((this.value.getType() == 1) ? ".0" : ""), (float)(this.getX() + 3), (float)(this.getY() + 3), Color.WHITE);
        }
    }
    
    @Override
    public void update(final int mouseX, final int mouseY, final float partialTicks) {
        super.update(mouseX, mouseY, partialTicks);
        if (this.value.getParent() != null) {
            this.setVisible(this.value.getParent().isOpen());
        }
        final double difference = Math.min(98, Math.max(0, mouseX - this.getX()));
        if (this.value.getType() == 1) {
            this.sliderWidth = 98.0f * (this.value.getValue().intValue() - this.value.getMinimum().intValue()) / (this.value.getMaximum().intValue() - this.value.getMinimum().intValue());
            if (this.dragging) {
                if (difference == 0.0) {
                    this.value.setValue(this.value.getMinimum());
                }
                else {
                    final int value = (int)MathUtils.roundToPlaces(difference / 98.0 * (this.value.getMaximum().intValue() - this.value.getMinimum().intValue()) + this.value.getMinimum().intValue(), 0);
                    this.value.setValue(value);
                }
            }
        }
        else if (this.value.getType() == 2) {
            this.sliderWidth = (float)(98.0 * (this.value.getValue().doubleValue() - this.value.getMinimum().doubleValue()) / (this.value.getMaximum().doubleValue() - this.value.getMinimum().doubleValue()));
            if (this.dragging) {
                if (difference == 0.0) {
                    this.value.setValue(this.value.getMinimum());
                }
                else {
                    final double value2 = MathUtils.roundToPlaces(difference / 98.0 * (this.value.getMaximum().doubleValue() - this.value.getMinimum().doubleValue()) + this.value.getMinimum().doubleValue(), 2);
                    this.value.setValue(value2);
                }
            }
        }
        else if (this.value.getType() == 3) {
            this.sliderWidth = 98.0f * (this.value.getValue().floatValue() - this.value.getMinimum().floatValue()) / (this.value.getMaximum().floatValue() - this.value.getMinimum().floatValue());
            if (this.dragging) {
                if (difference == 0.0) {
                    this.value.setValue(this.value.getMinimum());
                }
                else {
                    final float value3 = (float)MathUtils.roundToPlaces(difference / 98.0 * (this.value.getMaximum().floatValue() - this.value.getMinimum().floatValue()) + this.value.getMinimum().floatValue(), 2);
                    this.value.setValue(value3);
                }
            }
        }
    }
    
    @Override
    public void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) {
        if (this.isHovering(mouseX, mouseY) && mouseButton == 0) {
            this.dragging = true;
        }
        else if (mouseButton == 1 && this.isHovering(mouseX, mouseY)) {
            this.listening = !this.listening;
            this.currentString = this.value.getValue().toString();
        }
    }
    
    @Override
    public void mouseReleased(final int mouseX, final int mouseY, final int state) {
        super.mouseReleased(mouseX, mouseY, state);
        this.dragging = false;
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
            if (this.value.getType() == 1) {
                try {
                    if (Integer.parseInt(this.currentString) <= this.value.getMaximum().intValue() && Integer.parseInt(this.currentString) >= this.value.getMinimum().intValue()) {
                        this.value.setValue(Integer.parseInt(this.currentString));
                    }
                    else {
                        this.value.setValue(this.value.getValue());
                    }
                }
                catch (NumberFormatException e) {
                    this.value.setValue(this.value.getValue());
                }
            }
            else if (this.value.getType() == 3) {
                try {
                    if (Float.parseFloat(this.currentString) <= this.value.getMaximum().floatValue() && Float.parseFloat(this.currentString) >= this.value.getMinimum().floatValue()) {
                        this.value.setValue(Float.parseFloat(this.currentString));
                    }
                    else {
                        this.value.setValue(this.value.getValue());
                    }
                }
                catch (NumberFormatException e) {
                    this.value.setValue(this.value.getValue());
                }
            }
            else if (this.value.getType() == 2) {
                try {
                    if (Double.parseDouble(this.currentString) <= this.value.getMaximum().doubleValue() && Double.parseDouble(this.currentString) >= this.value.getMinimum().doubleValue()) {
                        this.value.setValue(Double.parseDouble(this.currentString));
                    }
                    else {
                        this.value.setValue(this.value.getValue());
                    }
                }
                catch (NumberFormatException e) {
                    this.value.setValue(this.value.getValue());
                }
            }
        }
        this.currentString = "";
    }
    
    private String removeLastCharacter(final String input) {
        if (input.length() > 0) {
            return input.substring(0, input.length() - 1);
        }
        return input;
    }
    
    public ValueNumber getValue() {
        return this.value;
    }
}
