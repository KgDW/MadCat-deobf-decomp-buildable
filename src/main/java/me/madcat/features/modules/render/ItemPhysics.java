package me.madcat.features.modules.render;

import me.madcat.features.modules.Module;
import me.madcat.features.setting.Setting;

public class ItemPhysics
extends Module {
    public static ItemPhysics INSTANCE = new ItemPhysics();
    public final Setting<Float> Scaling = this.register(new Setting<>("Scale", 0.5f, 0.1f, 1.0f));
    public final Setting<Float> rotateSpeed = this.register(new Setting<>("RotateSpeed", 0.5f, 0.0f, 1.0f));
    public final Setting<Float> shulkerBox = this.register(new Setting<>("ShulkerBoxScale", 0.5f, 0.0f, 4.0f));

    public ItemPhysics() {
        super("ItemPhysics", "Apply physics to items", Module.Category.RENDER);
        this.setInstance();
    }

    public static ItemPhysics INSTANCE() {
        if (INSTANCE == null) {
            INSTANCE = new ItemPhysics();
        }
        return INSTANCE;
    }

    private void setInstance() {
        INSTANCE = this;
    }
}

