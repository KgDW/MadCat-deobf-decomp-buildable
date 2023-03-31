package me.madcat.features.modules.player;

import me.madcat.features.modules.Module;

public class MultiTask
extends Module {
    private static MultiTask INSTANCE = new MultiTask();

    public MultiTask() {
        super("MultiTask", "Allows you to eat while mining and so on", Module.Category.PLAYER);
        this.setInstance();
    }

    public static MultiTask INSTANCE() {
        if (INSTANCE == null) {
            INSTANCE = new MultiTask();
        }
        return INSTANCE;
    }

    private void setInstance() {
        INSTANCE = this;
    }
}

