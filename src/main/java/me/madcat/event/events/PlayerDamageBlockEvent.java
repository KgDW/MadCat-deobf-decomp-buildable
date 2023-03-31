package me.madcat.event.events;

import me.madcat.event.EventStage;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.eventhandler.Cancelable;

@Cancelable
public class PlayerDamageBlockEvent
extends EventStage {
    public final BlockPos pos;
    public final EnumFacing facing;

    public PlayerDamageBlockEvent(int n, BlockPos blockPos, EnumFacing enumFacing) {
        super(n);
        this.pos = blockPos;
        this.facing = enumFacing;
    }

    public final BlockPos getPos() {
        return this.pos;
    }
}

