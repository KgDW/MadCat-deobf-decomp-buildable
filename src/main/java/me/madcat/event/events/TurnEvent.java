package me.madcat.event.events;

import me.madcat.event.EventStage;
import net.minecraftforge.fml.common.eventhandler.Cancelable;

@Cancelable
public class TurnEvent
extends EventStage {
    private final float yaw;
    private final float pitch;

    public TurnEvent(float f, float f2) {
        this.yaw = f;
        this.pitch = f2;
    }

    public float getYaw() {
        return this.yaw;
    }

    public float getPitch() {
        return this.pitch;
    }
}

