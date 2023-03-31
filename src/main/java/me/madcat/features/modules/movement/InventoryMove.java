package me.madcat.features.modules.movement;

import me.madcat.features.modules.Module;
import me.madcat.features.setting.Setting;

public class InventoryMove
extends Module {
    private static InventoryMove INSTANCE = new InventoryMove();
    public final Setting<Boolean> shift = this.register(new Setting<>("Sneak", false));

    public InventoryMove() {
        super("InvMove", "Allow walking on the interface", Module.Category.MOVEMENT);
        this.setInstance();
    }

    public static InventoryMove INSTANCE() {
        if (INSTANCE == null) {
            INSTANCE = new InventoryMove();
        }
        return INSTANCE;
    }

    private void setInstance() {
        INSTANCE = this;
    }
}

