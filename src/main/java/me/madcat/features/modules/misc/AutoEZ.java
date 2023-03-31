package me.madcat.features.modules.misc;

import me.madcat.features.modules.Module;
import me.madcat.features.setting.Setting;

public class AutoEZ
extends Module {
    public final Setting<String> SelfString;
    public final Setting<Boolean> whenSelf;
    public final Setting<String> EzString = this.register(new Setting<>("String", "Ez"));
    public final Setting<Boolean> poped = this.register(new Setting<>("PopCounter", true));
    public final Setting<Boolean> whenFriend = this.register(new Setting<>("whenFriend", false));
    private static AutoEZ INSTANCE = new AutoEZ();

    public static AutoEZ INSTANCE() {
        if (INSTANCE == null) {
            INSTANCE = new AutoEZ();
        }
        return INSTANCE;
    }

    public AutoEZ() {
        super("AutoEZ", "say ez for enemy dead", Module.Category.MISC);
        this.whenSelf = this.register(new Setting<>("whenSelf", false));
        this.SelfString = this.register(new Setting<>("SelfString", "potato server nice lag"));
        this.setInstance();
    }

    private void setInstance() {
        INSTANCE = this;
    }
}

