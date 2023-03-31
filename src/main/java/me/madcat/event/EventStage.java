package me.madcat.event;

import net.minecraftforge.fml.common.eventhandler.Event;

public class EventStage
extends Event {
    private final boolean cancelled = true;
    private int stage;

    public EventStage() {
    }

    public EventStage(int n) {
        this.stage = n;
    }

    public int getStage() {
        return this.stage;
    }

    public void setStage(int n) {
        this.stage = n;
    }

    public final void isCancelled() {
    }
}

