package me.madcat.event.events;

import me.madcat.event.EventStage;

public class Render3DEvent
extends EventStage {
    private final float partialTicks;

    public Render3DEvent(float f) {
        this.partialTicks = f;
    }

    public float getPartialTicks() {
        return this.partialTicks;
    }
}

