package me.madcat.features.modules.misc;

import me.madcat.features.modules.Module;
import me.madcat.features.setting.Setting;

public class ExtraTab
extends Module {
    private static ExtraTab INSTANCE = new ExtraTab();
    public final Setting<Integer> size = this.register(new Setting<>("Size", 250, 1, 1000));

    public ExtraTab() {
        super("ExtraTab", "Extends Tab", Module.Category.MISC);
        this.setInstance();
    }

    public static ExtraTab INSTANCE() {
        if (INSTANCE == null) {
            INSTANCE = new ExtraTab();
        }
        return INSTANCE;
    }

    private void setInstance() {
        INSTANCE = this;
    }
}

