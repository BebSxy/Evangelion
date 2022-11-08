package dev.evangelion.api.manager.element;

import dev.evangelion.client.gui.hud.ElementFrame;
import dev.evangelion.client.values.Value;
import java.util.ArrayList;
import net.minecraft.client.Minecraft;
import dev.evangelion.api.manager.module.Module;

public abstract class Element extends Module
{
    protected static final Minecraft mc;
    private final ArrayList<Value> values;
    public ElementFrame frame;
    
    public Element() {
        final RegisterElement annotation = this.getClass().getAnnotation(RegisterElement.class);
        this.name = annotation.name();
        this.tag.setValue(annotation.tag().equals("4GquuoBHl7gkSDaNeMb5") ? annotation.name() : annotation.tag());
        this.description = annotation.description();
        this.values = new ArrayList<Value>();
    }
    
    public void setFrame(final ElementFrame frame) {
        this.frame = frame;
    }
    
    @Override
    public ArrayList<Value> getValues() {
        return this.values;
    }
    
    static {
        mc = Minecraft.getMinecraft();
    }
}
