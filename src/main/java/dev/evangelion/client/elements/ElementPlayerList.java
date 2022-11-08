package dev.evangelion.client.elements;

import java.util.Iterator;
import dev.evangelion.client.modules.client.ModuleColor;
import net.minecraft.entity.Entity;
import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.entity.EntityLivingBase;
import dev.evangelion.Evangelion;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.Comparator;
import java.util.List;
import net.minecraft.entity.player.EntityPlayer;
import java.util.ArrayList;
import dev.evangelion.client.events.EventRender2D;
import dev.evangelion.client.values.impl.ValueNumber;
import dev.evangelion.api.manager.element.RegisterElement;
import dev.evangelion.api.manager.element.Element;

@RegisterElement(name = "PlayerList", tag = "Player List", description = "Shows the players that are near")
public class ElementPlayerList extends Element
{
    ValueNumber maxPlayers;

    public ElementPlayerList() {
        this.maxPlayers = new ValueNumber("MaxPlayers", "Max Players", "Max players that can be shown in the player list.", 8, 3, 20);
    }

    @Override
    public void onRender2D(final EventRender2D event) {
        super.onRender2D(event);
        if (this.nullCheck()) {
            return;
        }
        final ArrayList<EntityPlayer> players = new ArrayList<EntityPlayer>();
        for (final EntityPlayer player : ElementPlayerList.mc.world.playerEntities) {
            if (player == ElementPlayerList.mc.player) {
                continue;
            }
            if (player.isDead) {
                continue;
            }
            if (ElementPlayerList.mc.player.getHealth() <= 0.0f) {
                continue;
            }
            players.add(player);
        }
        int i = 0;
        for (final EntityPlayer player2 : players.stream().sorted(Comparator.comparing(p -> ElementPlayerList.mc.player.getDistance((Entity) p))).collect(Collectors.toList())) {
            if (i + 1 <= this.maxPlayers.getValue().intValue()) {
                Evangelion.FONT_MANAGER.drawStringWithShadow("" + this.getHealthColor((EntityLivingBase)player2) + (int)(player2.getHealth() + player2.getAbsorptionAmount()) + " " + (Evangelion.FRIEND_MANAGER.isFriend(player2.getName()) ? ChatFormatting.AQUA : ChatFormatting.RESET) + player2.getName() + " " + this.getDistanceColor((EntityLivingBase)player2) + (int)ElementPlayerList.mc.player.getDistance((Entity)player2), this.frame.getX(), this.frame.getY() + 10 * i, ModuleColor.getColor());
                ++i;
            }
        }
        float longestName = 0.0f;
        for (final EntityPlayer player3 : players) {
            final String text = "" + this.getHealthColor((EntityLivingBase)player3) + (int)(player3.getHealth() + player3.getAbsorptionAmount()) + " " + (Evangelion.FRIEND_MANAGER.isFriend(player3.getName()) ? ChatFormatting.AQUA : ChatFormatting.RESET) + player3.getName() + " " + this.getDistanceColor((EntityLivingBase)player3) + (int)ElementPlayerList.mc.player.getDistance((Entity)player3);
            if (Evangelion.FONT_MANAGER.getStringWidth(text) > longestName) {
                longestName = Evangelion.FONT_MANAGER.getStringWidth(text);
            }
        }
        this.frame.setWidth(longestName);
        this.frame.setHeight((float)(10 * i));
    }

    public ChatFormatting getHealthColor(final EntityLivingBase player) {
        if (player.getHealth() + player.getAbsorptionAmount() <= 5.0f) {
            return ChatFormatting.RED;
        }
        if (player.getHealth() + player.getAbsorptionAmount() > 5.0f && player.getHealth() + player.getAbsorptionAmount() < 15.0f) {
            return ChatFormatting.YELLOW;
        }
        if (player.getHealth() + player.getAbsorptionAmount() >= 15.0f) {
            return ChatFormatting.GREEN;
        }
        return ChatFormatting.WHITE;
    }

    public ChatFormatting getDistanceColor(final EntityLivingBase player) {
        if (ElementPlayerList.mc.player.getDistance((Entity)player) < 20.0f) {
            return ChatFormatting.RED;
        }
        if (ElementPlayerList.mc.player.getDistance((Entity)player) >= 20.0f && ElementPlayerList.mc.player.getDistance((Entity)player) < 50.0f) {
            return ChatFormatting.YELLOW;
        }
        if (ElementPlayerList.mc.player.getDistance((Entity)player) >= 50.0f) {
            return ChatFormatting.GREEN;
        }
        return ChatFormatting.WHITE;
    }
}
