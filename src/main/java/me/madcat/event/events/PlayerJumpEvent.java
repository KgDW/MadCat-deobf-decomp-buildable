package me.madcat.event.events;

import me.madcat.event.EventStage;

public class PlayerJumpEvent
extends EventStage {
    public final double motionX;
    public final double motionY;

    public PlayerJumpEvent(double d, double d2) {
        this.motionX = d;
        this.motionY = d2;
    }
}

