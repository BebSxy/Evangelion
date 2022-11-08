package dev.evangelion.client.modules.visuals.waypoints;

import net.minecraft.entity.player.EntityPlayer;
import java.util.UUID;

public class Waypoint
{
    private final String name;
    private final UUID id;
    private final EntityPlayer player;
    private final int dimension;
    
    public Waypoint(final String name, final UUID id, final EntityPlayer player, final int dimension) {
        this.name = name;
        this.id = id;
        this.player = player;
        this.dimension = dimension;
    }
    
    public String getName() {
        return this.name;
    }
    
    public EntityPlayer getPlayer() {
        return this.player;
    }
    
    public int getDimension() {
        return this.dimension;
    }
}
