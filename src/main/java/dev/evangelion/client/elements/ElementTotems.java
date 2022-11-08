package dev.evangelion.client.elements;

import dev.evangelion.client.modules.client.ModuleColor;
import dev.evangelion.client.modules.client.ModuleHUDEditor;
import dev.evangelion.Evangelion;
import java.util.function.ToIntFunction;
import net.minecraft.client.renderer.GlStateManager;
import dev.evangelion.client.events.EventRender2D;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import dev.evangelion.api.manager.element.RegisterElement;
import dev.evangelion.api.manager.element.Element;

@RegisterElement(name = "Totems", description = "Show how many totems you have left.")
public class ElementTotems extends Element
{
    private final ItemStack stack;
    
    public ElementTotems() {
        this.stack = new ItemStack(Items.TOTEM_OF_UNDYING);
    }
    
    @Override
    public void onRender2D(final EventRender2D event) {
        super.onRender2D(event);
        GlStateManager.enableTexture2D();
        this.frame.setWidth(15.0f);
        this.frame.setHeight(15.0f);
        int totems = ElementTotems.mc.player.inventory.mainInventory.stream().filter(itemStack -> itemStack.getItem() == Items.TOTEM_OF_UNDYING).mapToInt(ItemStack::getCount).sum();
        if (ElementTotems.mc.player.getHeldItemOffhand().getItem() == Items.TOTEM_OF_UNDYING) {
            totems += ElementTotems.mc.player.getHeldItemOffhand().getCount();
        }
        if (totems > 0) {
            GlStateManager.enableDepth();
            ElementTotems.mc.getRenderItem().zLevel = 200.0f;
            ElementTotems.mc.getRenderItem().renderItemAndEffectIntoGUI(this.stack, (int)this.frame.getX(), (int)this.frame.getY());
            ElementTotems.mc.getRenderItem().renderItemOverlayIntoGUI(ElementTotems.mc.fontRenderer, this.stack, (int)this.frame.getX(), (int)this.frame.getY(), "");
            ElementTotems.mc.getRenderItem().zLevel = 0.0f;
            GlStateManager.enableTexture2D();
            GlStateManager.disableLighting();
            GlStateManager.disableDepth();
            Evangelion.FONT_MANAGER.drawStringWithShadow("" + ModuleHUDEditor.INSTANCE.getSecondColor() + totems + "", this.frame.getX() + 10.0f, this.frame.getY() + 12.0f, ModuleColor.getColor());
        }
        GlStateManager.enableDepth();
        GlStateManager.disableLighting();
    }
}
