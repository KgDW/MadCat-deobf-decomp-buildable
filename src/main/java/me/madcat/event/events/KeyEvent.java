package me.madcat.event.events;

import me.madcat.event.EventStage;

public class KeyEvent
extends EventStage {
    private final int key;

    public KeyEvent(int n) {
        this.key = n;
    }

    public int getKey() {
        return this.key;
    }
}

