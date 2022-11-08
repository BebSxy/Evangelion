package dev.evangelion.client.modules.visuals.waypoints;

import net.minecraft.util.math.AxisAlignedBB;
import java.util.Iterator;
import dev.evangelion.api.utilities.MathUtils;
import net.minecraft.entity.Entity;
import dev.evangelion.api.utilities.RenderUtils;
import dev.evangelion.client.events.EventRender3D;
import dev.evangelion.client.events.EventPlayerLeave;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.entity.player.EntityPlayer;
import dev.evangelion.api.utilities.ChatUtils;
import dev.evangelion.client.modules.client.ModuleCommands;
import dev.evangelion.client.events.EventPlayerJoin;
import java.util.Collections;
import java.util.ArrayList;
import java.awt.Color;
import java.util.List;
import dev.evangelion.client.values.impl.ValueColor;
import dev.evangelion.client.values.impl.ValueBoolean;
import dev.evangelion.client.values.impl.ValueEnum;
import dev.evangelion.client.values.impl.ValueNumber;
import dev.evangelion.api.manager.module.RegisterModule;
import dev.evangelion.api.manager.module.Module;

@RegisterModule(name = "Waypoints", description = "Render waypoints to certain locations.", category = Module.Category.VISUALS)
public class ModuleWaypoints extends Module
{
    public static ModuleWaypoints INSTANCE;
    ValueNumber range;
    ValueNumber scale;
    ValueEnum mode;
    ValueBoolean logoutSpots;
    ValueBoolean deathSpot;
    ValueBoolean message;
    ValueColor color;
    List<Waypoint> spots;
    Waypoint deathPoint;
    
    public ModuleWaypoints() {
        this.range = new ValueNumber("Range", "Range", "Max distance to rener the waypoints.", 200.0f, 10.0f, 500.0f);
        this.scale = new ValueNumber("Scale", "Scale", "Scale of the nametags", 30, 10, 100);
        this.mode = new ValueEnum("Mode", "Mode", "Mode for the waypoints.", Modes.Coords);
        this.logoutSpots = new ValueBoolean("LogoutSpots", "Logout Spots", "Render logout spots.", true);
        this.deathSpot = new ValueBoolean("DeathSpot", "Death Spot", "Render your death spot.", true);
        this.message = new ValueBoolean("Message", "Message", "Send a client side message to notify when someone connects.", false);
        this.color = new ValueColor("Color", "Color", "", new Color(0, 255, 0));
        this.spots = Collections.synchronizedList(new ArrayList<Waypoint>());
        ModuleWaypoints.INSTANCE = this;
    }
    
    @SubscribeEvent
    public void onPlayerJoin(final EventPlayerJoin event) {
        final EntityPlayer entity = ModuleWaypoints.mc.world.getPlayerEntityByUUID(event.getID());
        if (entity != null && this.message.getValue() && this.logoutSpots.getValue()) {
            ChatUtils.sendMessage(ModuleCommands.getSecondColor() + entity.getName() + ModuleCommands.getFirstColor() + " logged in" + (this.mode.getValue().equals(Modes.Coords) ? (" at [" + (int)entity.posX + ", " + (int)entity.posY + ", " + (int)entity.posZ + "]!") : "!"));
        }
        this.spots.removeIf(pos -> pos.getName().equalsIgnoreCase(event.getName()));
    }
    
    @SubscribeEvent
    public void onPlayerLeave(final EventPlayerLeave event) {
        final EntityPlayer entity = event.getEntity();
        if (this.message.getValue() && this.logoutSpots.getValue()) {
            ChatUtils.sendMessage(ModuleCommands.getSecondColor() + event.getName() + ModuleCommands.getFirstColor() + " logged out" + (this.mode.getValue().equals(Modes.Coords) ? (" at [" + (int)entity.posX + ", " + (int)entity.posY + ", " + (int)entity.posZ + "]!") : "!"));
        }
        if (event.getName() != null && entity != null && event.getID() != null) {
            this.spots.add(new Waypoint(event.getName(), event.getID(), entity, ModuleWaypoints.mc.player.dimension));
        }
    }
    
    @Override
    public void onDeath() {
        super.onDeath();
        this.deathPoint = new Waypoint(ModuleWaypoints.mc.player.getName(), ModuleWaypoints.mc.player.getUniqueID(), (EntityPlayer)ModuleWaypoints.mc.player, ModuleWaypoints.mc.player.dimension);
    }
    
    @Override
    public void onRender3D(final EventRender3D event) {
        super.onRender3D(event);
        if (this.logoutSpots.getValue() && !this.spots.isEmpty() && this.spots != null) {
            for (final Waypoint waypoint : this.spots) {
                if (waypoint.getPlayer() != null) {
                    final AxisAlignedBB bb = RenderUtils.getRenderBB(waypoint.getPlayer().boundingBox);
                    if (ModuleWaypoints.mc.player.getDistanceSq((Entity)waypoint.getPlayer()) > MathUtils.square(this.range.getValue().floatValue()) || ModuleWaypoints.mc.player.dimension != waypoint.getDimension()) {
                        continue;
                    }
                    RenderUtils.drawBlockOutline(bb, this.color.getValue(), 1.0f);
                    RenderUtils.drawNametag(waypoint.getName() + (this.mode.getValue().equals(Modes.Coords) ? (" XYZ " + (int)waypoint.getPlayer().posX + " " + (int)waypoint.getPlayer().posY + " " + (int)waypoint.getPlayer().posZ) : (" " + (int)ModuleWaypoints.mc.player.getDistance((Entity)waypoint.getPlayer()) + "m")), bb, this.scale.getValue().intValue() / 10000.0f, this.color.getValue());
                }
            }
        }
        if (this.deathSpot.getValue() && this.deathPoint != null && ModuleWaypoints.mc.player.dimension == this.deathPoint.getDimension()) {
            final AxisAlignedBB bb2 = RenderUtils.getRenderBB(this.deathPoint.getPlayer().boundingBox);
            if (ModuleWaypoints.mc.player.getDistanceSq((Entity)this.deathPoint.getPlayer()) <= MathUtils.square(this.range.getValue().floatValue())) {
                RenderUtils.drawBlockOutline(bb2, this.color.getValue(), 1.0f);
                RenderUtils.drawNametag("Death" + (this.mode.getValue().equals(Modes.Coords) ? (" XYZ " + (int)this.deathPoint.getPlayer().posX + " " + (int)this.deathPoint.getPlayer().posY + " " + (int)this.deathPoint.getPlayer().posZ) : (" " + (int)ModuleWaypoints.mc.player.getDistance((Entity)this.deathPoint.getPlayer()) + "m")), bb2, this.scale.getValue().intValue() / 10000.0f, this.color.getValue());
            }
        }
    }
    
    @Override
    public void onLogin() {
        super.onLogin();
        this.spots.clear();
    }
    
    @Override
    public void onLogout() {
        super.onLogout();
        this.spots.clear();
    }
    
    public enum Modes
    {
        Coords, 
        Distance;
    }
}
