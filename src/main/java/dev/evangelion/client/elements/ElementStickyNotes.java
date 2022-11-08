package dev.evangelion.client.elements;

import dev.evangelion.client.modules.client.ModuleColor;
import dev.evangelion.Evangelion;
import dev.evangelion.client.events.EventRender2D;
import dev.evangelion.client.values.impl.ValueString;
import dev.evangelion.client.values.impl.ValueCategory;
import dev.evangelion.client.values.impl.ValueEnum;
import dev.evangelion.api.manager.element.RegisterElement;
import dev.evangelion.api.manager.element.Element;

@RegisterElement(name = "StickyNotes", tag = "Sticky Notes", description = "Let's you write custom text on the screen.")
public class ElementStickyNotes extends Element
{
    private final ValueEnum lines;
    private final ValueCategory lineCategory;
    private final ValueString lineOne;
    private final ValueString lineTwo;
    private final ValueString lineThree;
    private final ValueString lineFour;
    
    public ElementStickyNotes() {
        this.lines = new ValueEnum("Lines", "Lines", "The amount of lines that should be rendered.", LinesAmount.One);
        this.lineCategory = new ValueCategory("Lines", "The lines for the text.");
        this.lineOne = new ValueString("LineOne", "LineOne", "The first line.", this.lineCategory, "Placeholder");
        this.lineTwo = new ValueString("LineTwo", "LineTwo", "The second line.", this.lineCategory, "Placeholder");
        this.lineThree = new ValueString("LineThree", "LineThree", "The third line.", this.lineCategory, "Placeholder");
        this.lineFour = new ValueString("LineFour", "LineFour", "The fourth line.", this.lineCategory, "Placeholder");
    }
    
    @Override
    public void onRender2D(final EventRender2D event) {
        super.onRender2D(event);
        this.frame.setWidth(Evangelion.FONT_MANAGER.getStringWidth(this.lineOne.getValue()));
        this.frame.setHeight(Evangelion.FONT_MANAGER.getHeight() * this.getMultiplier() + this.getMultiplier());
        for (int i = 0; i <= this.getMultiplier() - 1; ++i) {
            Evangelion.FONT_MANAGER.drawStringWithShadow((i == 1) ? this.lineTwo.getValue() : ((i == 2) ? this.lineThree.getValue() : ((i == 3) ? this.lineFour.getValue() : this.lineOne.getValue())), this.frame.getX(), this.frame.getY() + (Evangelion.FONT_MANAGER.getHeight() + 1.0f) * i, ModuleColor.getColor());
        }
    }
    
    public int getMultiplier() {
        switch ((LinesAmount)this.lines.getValue()) {
            case Two: {
                return 2;
            }
            case Three: {
                return 3;
            }
            case Four: {
                return 4;
            }
            default: {
                return 1;
            }
        }
    }
    
    public enum LinesAmount
    {
        One, 
        Two, 
        Three, 
        Four;
    }
}
