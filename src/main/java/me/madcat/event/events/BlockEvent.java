package me.madcat.event.events;

import me.madcat.event.EventStage;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.eventhandler.Cancelable;

@Cancelable
public class BlockEvent
extends EventStage {
    public final BlockPos pos;
    public final EnumFacing facing;

    public BlockEvent(int n, BlockPos blockPos, EnumFacing enumFacing) {
        super(n);
        this.pos = blockPos;
        this.facing = enumFacing;
    }
}

