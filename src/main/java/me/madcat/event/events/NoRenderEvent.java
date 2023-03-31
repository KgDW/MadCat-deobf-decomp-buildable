package me.madcat.event.events;

import me.madcat.event.EventStage;
import net.minecraftforge.fml.common.eventhandler.Cancelable;

@Cancelable
public class NoRenderEvent
extends EventStage {
    public NoRenderEvent(int n) {
        super(n);
    }
}

