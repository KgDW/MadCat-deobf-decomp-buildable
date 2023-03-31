package me.madcat.event.events;

import me.madcat.event.EventStage;

public class PerspectiveEvent
extends EventStage {
    private float aspect;

    public PerspectiveEvent(float f) {
        this.aspect = f;
    }

    public float getAspect() {
        return this.aspect;
    }

    public void setAspect(float f) {
        this.aspect = f;
    }
}

