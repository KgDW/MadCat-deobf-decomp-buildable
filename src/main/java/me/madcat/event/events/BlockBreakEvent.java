package me.madcat.event.events;

import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

@Cancelable
public class BlockBreakEvent
extends Event {
    private final int breakerId;
    private final BlockPos position;
    private int progress;

    public BlockBreakEvent(int n, BlockPos blockPos, int n2) {
        this.breakerId = n;
        this.position = blockPos;
        this.progress = n2;
    }

    public int getBreakerId() {
        return this.breakerId;
    }

    public BlockPos getPosition() {
        return this.position;
    }

    public int getProgress() {
        return this.progress;
    }

    public void setProgress(int n) {
        this.progress = n;
    }
}

