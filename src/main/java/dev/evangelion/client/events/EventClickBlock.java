package dev.evangelion.client.events;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import dev.evangelion.api.manager.event.Event;

public class EventClickBlock extends Event
{
    private final BlockPos position;
    private final EnumFacing side;
    
    public EventClickBlock(final Stage stage, final BlockPos position, final EnumFacing side) {
        super(stage);
        this.position = position;
        this.side = side;
    }
    
    public BlockPos getPosition() {
        return this.position;
    }
    
    public EnumFacing getSide() {
        return this.side;
    }
}
