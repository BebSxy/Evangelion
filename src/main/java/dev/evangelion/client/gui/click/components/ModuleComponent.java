package dev.evangelion.client.gui.click.components;

import dev.evangelion.client.modules.client.ModuleColor;
import net.minecraft.util.math.MathHelper;
import com.mojang.realmsclient.gui.ChatFormatting;
import dev.evangelion.api.utilities.RenderUtils;
import dev.evangelion.Evangelion;
import dev.evangelion.api.utilities.ColorUtils;
import dev.evangelion.client.modules.client.ModuleGUI;
import net.minecraft.client.gui.ScaledResolution;
import java.util.Iterator;
import dev.evangelion.client.values.impl.ValueCategory;
import dev.evangelion.client.values.impl.ValueColor;
import dev.evangelion.client.values.impl.ValueBind;
import dev.evangelion.client.values.impl.ValueString;
import dev.evangelion.client.values.impl.ValueEnum;
import dev.evangelion.client.values.impl.ValueNumber;
import dev.evangelion.client.values.impl.ValueBoolean;
import dev.evangelion.client.values.Value;
import java.util.HashMap;
import dev.evangelion.client.gui.click.manage.Frame;
import java.awt.Color;
import java.util.Map;
import dev.evangelion.api.manager.module.Module;
import java.util.ArrayList;
import dev.evangelion.client.gui.click.manage.Component;

public class ModuleComponent extends Component
{
    private final ArrayList<Component> components;
    private final Module module;
    private boolean open;
    public Map<Integer, Color> colorMap;
    
    public ModuleComponent(final Module module, final int offset, final Frame parent) {
        super(offset, parent);
        this.open = false;
        this.colorMap = new HashMap<Integer, Color>();
        this.module = module;
        this.components = new ArrayList<Component>();
        int valueOffset = offset;
        if (!module.getValues().isEmpty()) {
            for (final Value value : module.getValues()) {
                if (value instanceof ValueBoolean) {
                    this.components.add(new BooleanComponent((ValueBoolean)value, valueOffset, parent));
                    valueOffset += 14;
                }
                if (value instanceof ValueNumber) {
                    this.components.add(new NumberComponent((ValueNumber)value, valueOffset, parent));
                    valueOffset += 14;
                }
                if (value instanceof ValueEnum) {
                    this.components.add(new EnumComponent((ValueEnum)value, valueOffset, parent));
                    valueOffset += 14;
                }
                if (value instanceof ValueString) {
                    this.components.add(new StringComponent((ValueString)value, valueOffset, parent));
                    valueOffset += 14;
                }
                if (value instanceof ValueBind) {
                    this.components.add(new BindComponent((ValueBind)value, valueOffset, parent));
                    valueOffset += 14;
                }
                if (value instanceof ValueColor) {
                    this.components.add(new ColorComponentTest((ValueColor)value, valueOffset, parent));
                    valueOffset += 14;
                }
                if (value instanceof ValueCategory) {
                    this.components.add(new CategoryComponent((ValueCategory)value, valueOffset, parent));
                    valueOffset += 14;
                }
            }
        }
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        final int height = new ScaledResolution(ModuleComponent.mc).getScaledHeight();
        for (int i = 0; i <= height; ++i) {
            this.colorMap.put(i, ColorUtils.wave(Color.WHITE, ModuleGUI.INSTANCE.fadeOffset.getValue().intValue(), i * 2 + 10));
        }
        if (this.module.isToggled() && ModuleGUI.INSTANCE.rectEnabled.getValue()) {
            RenderUtils.drawRect(this.getX() + 0.4f, this.getY() - 0.8f, this.getX() + this.getWidth() - 0.4f, this.getY() + 14.1f, Evangelion.CLICK_GUI.getColor());
        }
        Evangelion.FONT_MANAGER.drawStringWithShadow((this.module.isToggled() ? "" : ChatFormatting.GRAY) + this.module.getTag(), (float)(this.getX() + 3), (float)(this.getY() + 3), ModuleGUI.INSTANCE.fadeText.getValue() ? this.colorMap.get(MathHelper.clamp(this.getY() + 3, 0, height)) : Color.WHITE);
        for (final Component component : this.components) {
            component.update(mouseX, mouseY, partialTicks);
        }
        if (this.isOpen()) {
            for (final Component component : this.components) {
                if (component.isVisible()) {
                    component.drawScreen(mouseX, mouseY, partialTicks);
                    if (component instanceof BooleanComponent) {
                        final BooleanComponent c = (BooleanComponent)component;
                        if (!component.isHovering(mouseX, mouseY) || c.getValue().getDescription().isEmpty()) {
                            continue;
                        }
                        RenderUtils.drawRect((float)(mouseX + 5), (float)(mouseY - 2), mouseX + Evangelion.FONT_MANAGER.getStringWidth(c.getValue().getDescription()) + 7.0f, (float)(mouseY + 11), new Color(40, 40, 40));
                        RenderUtils.drawOutline((float)(mouseX + 5), (float)(mouseY - 2), mouseX + Evangelion.FONT_MANAGER.getStringWidth(c.getValue().getDescription()) + 7.0f, (float)(mouseY + 11), 1.0f, ModuleColor.getColor());
                        Evangelion.FONT_MANAGER.drawStringWithShadow(c.getValue().getDescription(), (float)(mouseX + 7), (float)mouseY, Color.WHITE);
                    }
                    else if (component instanceof NumberComponent) {
                        final NumberComponent c2 = (NumberComponent)component;
                        if (!component.isHovering(mouseX, mouseY) || c2.getValue().getDescription().isEmpty()) {
                            continue;
                        }
                        RenderUtils.drawRect((float)(mouseX + 5), (float)(mouseY - 2), mouseX + Evangelion.FONT_MANAGER.getStringWidth(c2.getValue().getDescription()) + 7.0f, (float)(mouseY + 11), new Color(40, 40, 40));
                        RenderUtils.drawOutline((float)(mouseX + 5), (float)(mouseY - 2), mouseX + Evangelion.FONT_MANAGER.getStringWidth(c2.getValue().getDescription()) + 7.0f, (float)(mouseY + 11), 1.0f, ModuleColor.getColor());
                        Evangelion.FONT_MANAGER.drawStringWithShadow(c2.getValue().getDescription(), (float)(mouseX + 7), (float)mouseY, Color.WHITE);
                    }
                    else if (component instanceof EnumComponent) {
                        final EnumComponent c3 = (EnumComponent)component;
                        if (!component.isHovering(mouseX, mouseY) || c3.getValue().getDescription().isEmpty()) {
                            continue;
                        }
                        RenderUtils.drawRect((float)(mouseX + 5), (float)(mouseY - 2), mouseX + Evangelion.FONT_MANAGER.getStringWidth(c3.getValue().getDescription()) + 7.0f, (float)(mouseY + 11), new Color(40, 40, 40));
                        RenderUtils.drawOutline((float)(mouseX + 5), (float)(mouseY - 2), mouseX + Evangelion.FONT_MANAGER.getStringWidth(c3.getValue().getDescription()) + 7.0f, (float)(mouseY + 11), 1.0f, ModuleColor.getColor());
                        Evangelion.FONT_MANAGER.drawStringWithShadow(c3.getValue().getDescription(), (float)(mouseX + 7), (float)mouseY, Color.WHITE);
                    }
                    else {
                        if (!(component instanceof StringComponent)) {
                            continue;
                        }
                        final StringComponent c4 = (StringComponent)component;
                        if (!component.isHovering(mouseX, mouseY) || c4.getValue().getDescription().isEmpty()) {
                            continue;
                        }
                        RenderUtils.drawRect((float)(mouseX + 5), (float)(mouseY - 2), mouseX + Evangelion.FONT_MANAGER.getStringWidth(c4.getValue().getDescription()) + 7.0f, (float)(mouseY + 11), new Color(40, 40, 40));
                        RenderUtils.drawOutline((float)(mouseX + 5), (float)(mouseY - 2), mouseX + Evangelion.FONT_MANAGER.getStringWidth(c4.getValue().getDescription()) + 7.0f, (float)(mouseY + 11), 1.0f, ModuleColor.getColor());
                        Evangelion.FONT_MANAGER.drawStringWithShadow(c4.getValue().getDescription(), (float)(mouseX + 7), (float)mouseY, Color.WHITE);
                    }
                }
            }
        }
    }
    
    @Override
    public void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        if (this.isHovering(mouseX, mouseY)) {
            if (mouseButton == 0) {
                this.module.toggle(true);
            }
            if (mouseButton == 1) {
                this.setOpen(!this.open);
                this.getParent().refresh();
            }
        }
        if (this.isOpen()) {
            for (final Component component : this.components) {
                if (component.isVisible()) {
                    component.mouseClicked(mouseX, mouseY, mouseButton);
                }
            }
        }
    }
    
    @Override
    public void mouseReleased(final int mouseX, final int mouseY, final int state) {
        super.mouseReleased(mouseX, mouseY, state);
        for (final Component component : this.components) {
            component.mouseReleased(mouseX, mouseY, state);
        }
    }
    
    @Override
    public void keyTyped(final char typedChar, final int keyCode) {
        super.keyTyped(typedChar, keyCode);
        if (this.isOpen()) {
            for (final Component component : this.components) {
                if (component.isVisible()) {
                    component.keyTyped(typedChar, keyCode);
                }
            }
        }
    }
    
    @Override
    public void onGuiClosed() {
        super.onGuiClosed();
        if (this.isOpen()) {
            for (final Component component : this.components) {
                component.onGuiClosed();
            }
        }
    }
    
    public ArrayList<Component> getComponents() {
        return this.components;
    }
    
    public Module getModule() {
        return this.module;
    }
    
    public boolean isOpen() {
        return this.open;
    }
    
    public void setOpen(final boolean open) {
        this.open = open;
    }
}
