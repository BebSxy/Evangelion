package dev.evangelion.client.elements;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.time.temporal.TemporalAdjusters;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import dev.evangelion.client.modules.client.ModuleHUDEditor;
import com.mojang.realmsclient.gui.ChatFormatting;
import dev.evangelion.client.modules.client.ModuleColor;
import net.minecraft.client.gui.ScaledResolution;
import dev.evangelion.Evangelion;
import dev.evangelion.client.events.EventRender2D;
import dev.evangelion.client.values.impl.ValueCategory;
import dev.evangelion.client.values.impl.ValueString;
import dev.evangelion.client.values.impl.ValueBoolean;
import dev.evangelion.client.values.impl.ValueEnum;
import dev.evangelion.api.manager.element.RegisterElement;
import dev.evangelion.api.manager.element.Element;

@RegisterElement(name = "Welcomer", description = "Renders a nice greeting message.")
public class ElementWelcomer extends Element
{
    private final ValueEnum mode;
    private final ValueBoolean center;
    private final ValueString customValue;
    private final ValueBoolean nameColor;
    private final ValueCategory emojiCategory;
    private final ValueBoolean emoji;
    private final ValueString emojiValue;
    
    public ElementWelcomer() {
        this.mode = new ValueEnum("Mode", "Mode", "The mode for the Welcomer.", Modes.Shorter);
        this.center = new ValueBoolean("Center", "Center", "Makes the Welcomer be positioned to the center.", true);
        this.customValue = new ValueString("CustomValue", "Custom Value", "The value for the Custom mode.", "Hello, <player>!");
        this.nameColor = new ValueBoolean("NameColor", "Name Color", "The color for the player's name.", true);
        this.emojiCategory = new ValueCategory("Emoji", "The emoji that is put at the end of the welcomer message.");
        this.emoji = new ValueBoolean("Emoji", "Emoji", "Renders a nice face after the welcomer text.", this.emojiCategory, true);
        this.emojiValue = new ValueString("EmojiValue", "Value", "The value for the Emoji.", this.emojiCategory, ">:)");
    }
    
    @Override
    public void onRender2D(final EventRender2D event) {
        this.frame.setWidth(Evangelion.FONT_MANAGER.getStringWidth(this.getText()));
        this.frame.setHeight(Evangelion.FONT_MANAGER.getHeight());
        final ScaledResolution resolution = new ScaledResolution(ElementWelcomer.mc);
        Evangelion.FONT_MANAGER.drawStringWithShadow(this.getText(), this.center.getValue() ? (resolution.getScaledWidth() / 2.0f - Evangelion.FONT_MANAGER.getStringWidth(this.getText()) / 2.0f) : this.frame.getX(), this.frame.getY(), ModuleColor.getColor());
    }
    
    public String getText() {
        return this.getWelcomeMessage() + (this.emoji.getValue() ? (" " + this.emojiValue.getValue()) : "");
    }
    
    public String getWelcomeMessage() {
        switch ((Modes)this.mode.getValue()) {
            case Short: {
                return "Greetings, " + this.getNameColor() + this.getPlayerName() + ChatFormatting.RESET + "!";
            }
            case Time: {
                return this.getTimeOfDay() + ", " + this.getNameColor() + this.getPlayerName() + ChatFormatting.RESET + "!";
            }
            case Holiday: {
                return this.getHoliday() + ", " + this.getNameColor() + this.getPlayerName() + ChatFormatting.RESET + "!";
            }
            case Hebrew: {
                return "Shalom, " + this.getNameColor() + this.getPlayerName() + ChatFormatting.RESET + "!";
            }
            case Long: {
                return "Welcome to Europa, " + this.getNameColor() + this.getPlayerName() + ChatFormatting.RESET + "!";
            }
            case Custom: {
                return this.customValue.getValue().replaceAll("<player>", this.getNameColor() + this.getPlayerName() + ChatFormatting.RESET);
            }
            default: {
                return "Hello, " + this.getNameColor() + this.getPlayerName() + ChatFormatting.RESET + "!";
            }
        }
    }
    
    private ChatFormatting getNameColor() {
        if (this.nameColor.getValue()) {
            return ChatFormatting.RESET;
        }
        return ModuleHUDEditor.INSTANCE.getSecondColor();
    }
    
    public String getPlayerName() {
        return ElementWelcomer.mc.player.getName();
    }
    
    public String getTimeOfDay() {
        final Calendar calendar = Calendar.getInstance();
        final int timeOfDay = calendar.get(11);
        if (timeOfDay < 12) {
            return "Good morning";
        }
        if (timeOfDay < 16) {
            return "Good afternoon";
        }
        if (timeOfDay < 21) {
            return "Good evening";
        }
        return "Good night";
    }
    
    public String getHoliday() {
        final int month = Integer.parseInt(new SimpleDateFormat("MM").format(new Date()));
        final int day = Integer.parseInt(new SimpleDateFormat("dd").format(new Date()));
        switch (month) {
            case 1: {
                if (day == 1) {
                    return "Happy New Years";
                }
            }
            case 2: {
                if (day == 14) {
                    return "Happy Valentines Day";
                }
                break;
            }
            case 10: {
                if (day == 31) {
                    return "Happy Halloween";
                }
                break;
            }
            case 11: {
                final LocalDate thanksGiving = Year.of(Integer.parseInt(new SimpleDateFormat("yyyy").format(new Date()))).atMonth(Month.NOVEMBER).atDay(1).with(TemporalAdjusters.lastInMonth(DayOfWeek.WEDNESDAY));
                if (thanksGiving.getDayOfMonth() == day) {
                    return "Happy Thanksgiving";
                }
            }
            case 12: {
                if (day == 25) {
                    return "Merry Christmas";
                }
                break;
            }
        }
        return "No holiday is currently going on";
    }
    
    public enum Modes
    {
        Shorter, 
        Short, 
        Holiday, 
        Time, 
        Hebrew, 
        Long, 
        Custom;
    }
}
