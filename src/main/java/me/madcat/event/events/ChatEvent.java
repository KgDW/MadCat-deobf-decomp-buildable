package me.madcat.event.events;

import me.madcat.event.EventStage;
import net.minecraftforge.fml.common.eventhandler.Cancelable;

@Cancelable
public class ChatEvent
extends EventStage {
    private final String msg;

    public ChatEvent(String string) {
        this.msg = string;
    }

    public String getMsg() {
        return this.msg;
    }
}

