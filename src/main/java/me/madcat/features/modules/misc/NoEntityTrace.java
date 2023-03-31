package me.madcat.features.modules.misc;

import me.madcat.features.Feature;
import me.madcat.features.modules.Module;

public class NoEntityTrace
extends Module {
    private static NoEntityTrace INSTANCE = new NoEntityTrace();
    public boolean noTrace;

    public NoEntityTrace() {
        super("NoEntityTrace", "Mine through entities", Module.Category.MISC);
        this.setInstance();
    }

    public static NoEntityTrace INSTANCE() {
        if (INSTANCE == null) {
            INSTANCE = new NoEntityTrace();
        }
        return INSTANCE;
    }

    private void setInstance() {
        INSTANCE = this;
    }

    @Override
    public void onUpdate() {
        if (Feature.fullNullCheck()) {
            return;
        }
        this.noTrace = true;
    }
}

