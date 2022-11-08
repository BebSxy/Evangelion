package dev.evangelion.client.elements;

import java.util.Iterator;
import dev.evangelion.client.modules.client.ModuleHUDEditor;
import dev.evangelion.client.modules.client.ModuleColor;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.function.Function;
import java.util.Comparator;
import net.minecraft.entity.player.EntityPlayer;
import dev.evangelion.Evangelion;
import java.util.ArrayList;
import dev.evangelion.client.events.EventRender2D;
import dev.evangelion.client.values.impl.ValueString;
import dev.evangelion.api.manager.element.RegisterElement;
import dev.evangelion.api.manager.element.Element;

@RegisterElement(name = "Friends", description = "Gives you a list of friends in your chunk distance.")
public class ElementFriends extends Element
{
    private final ValueString name;

    public ElementFriends() {
        this.name = new ValueString("Name", "Name", "The name for the group of friends.", "The Goons");
    }

    @Override
    public void onRender2D(final EventRender2D event) {
        super.onRender2D(event);
        final ArrayList<EntityPlayer> friends = ElementFriends.mc.world.playerEntities.stream().filter(p -> Evangelion.FRIEND_MANAGER.isFriend(p.getName())).sorted(Comparator.comparing(EntityPlayer::getName)).collect(Collectors.toCollection(ArrayList::new));
        this.frame.setWidth(friends.isEmpty() ? Evangelion.FONT_MANAGER.getStringWidth(this.name.getValue()) : Evangelion.FONT_MANAGER.getStringWidth(friends.get(0).getName()));
        this.frame.setHeight(Evangelion.FONT_MANAGER.getHeight() + (friends.isEmpty() ? 0.0f : (1.0f + (Evangelion.FONT_MANAGER.getHeight() + 1.0f) * (friends.size() + 1))));
        Evangelion.FONT_MANAGER.drawStringWithShadow(this.name.getValue(), this.frame.getX(), this.frame.getY(), ModuleColor.getColor());
        int index = 10;
        for (final EntityPlayer player : friends) {
            Evangelion.FONT_MANAGER.drawStringWithShadow(ModuleHUDEditor.INSTANCE.getSecondColor() + player.getName(), this.frame.getX(), this.frame.getY() + index, ModuleColor.getColor());
            index += 10;
        }
    }

    public enum Colors
    {
        Normal,
        White,
        Gray;
    }
}
