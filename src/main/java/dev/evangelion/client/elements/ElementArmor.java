package dev.evangelion.client.elements;

import java.util.Iterator;
import dev.evangelion.client.modules.client.ModuleColor;
import dev.evangelion.client.modules.client.ModuleHUDEditor;
import java.awt.Color;
import dev.evangelion.Evangelion;
import net.minecraft.item.ItemStack;
import net.minecraft.client.renderer.GlStateManager;
import dev.evangelion.client.events.EventRender2D;
import dev.evangelion.client.values.impl.ValueBoolean;
import dev.evangelion.api.manager.element.RegisterElement;
import dev.evangelion.api.manager.element.Element;

@RegisterElement(name = "Armor", description = "Renders the status of your armor on the screen.")
public class ElementArmor extends Element
{
    public static ValueBoolean percentage;
    public static ValueBoolean percentageColor;
    
    @Override
    public void onRender2D(final EventRender2D event) {
        super.onRender2D(event);
        GlStateManager.enableTexture2D();
        this.frame.setWidth(90.0f);
        this.frame.setHeight(15.0f);
        int index = 0;
        for (final ItemStack stack : ElementArmor.mc.player.inventory.armorInventory) {
            ++index;
            if (stack.isEmpty()) {
                continue;
            }
            GlStateManager.enableDepth();
            ElementArmor.mc.getRenderItem().zLevel = 200.0f;
            ElementArmor.mc.getRenderItem().renderItemAndEffectIntoGUI(stack, (int)(this.frame.getX() - 90.0f + (9 - index) * 20 + 2.0f), (int)this.frame.getY());
            ElementArmor.mc.getRenderItem().renderItemOverlayIntoGUI(ElementArmor.mc.fontRenderer, stack, (int)(this.frame.getX() - 90.0f + (9 - index) * 20 + 2.0f), (int)this.frame.getY(), "");
            ElementArmor.mc.getRenderItem().zLevel = 0.0f;
            GlStateManager.enableTexture2D();
            GlStateManager.disableLighting();
            GlStateManager.disableDepth();
            final String s = (stack.getCount() > 1) ? (stack.getCount() + "") : "";
            Evangelion.FONT_MANAGER.drawStringWithShadow(s, this.frame.getX() - 90.0f + (9 - index) * 20 + 2.0f + 19.0f - 2.0f - Evangelion.FONT_MANAGER.getStringWidth(s), this.frame.getY() + 9.0f, new Color(16777215));
            final float green = (stack.getMaxDamage() - (float)stack.getItemDamage()) / stack.getMaxDamage();
            final float red = 1.0f - green;
            final int dmg = 100 - (int)(red * 100.0f);
            if (!ElementArmor.percentage.getValue()) {
                continue;
            }
            Evangelion.FONT_MANAGER.drawStringWithShadow((ElementArmor.percentageColor.getValue() ? "" : ModuleHUDEditor.INSTANCE.getSecondColor()) + "" + dmg + "", this.frame.getX() - 90.0f + (9 - index) * 20 + 2.0f + 8.0f - Evangelion.FONT_MANAGER.getStringWidth(dmg + "") / 2.0f, this.frame.getY() - 11.0f, ElementArmor.percentageColor.getValue() ? new Color(red, green, 0.0f) : ModuleColor.getColor());
        }
        GlStateManager.enableDepth();
        GlStateManager.disableLighting();
    }
    
    static {
        ElementArmor.percentage = new ValueBoolean("Percentage", "Percentage", "Renders the percentage that the armor's durability is at.", true);
        ElementArmor.percentageColor = new ValueBoolean("PercentageColor", "Percentage Color", "The color for the percentage.", true);
    }
}
