package me.madcat.features.modules.render;

import me.madcat.features.modules.Module;
import me.madcat.features.setting.Setting;

public class CameraClip
extends Module {
    private static CameraClip INSTANCE = new CameraClip();
    public final Setting<Double> distance = this.register(new Setting<>("Distance", 4.0, -10.0, 20.0));

    public CameraClip() {
        super("CameraClip", "CameraClip", Module.Category.RENDER);
        this.setInstance();
    }

    public static CameraClip Instance() {
        if (INSTANCE == null) {
            INSTANCE = new CameraClip();
        }
        return INSTANCE;
    }

    private void setInstance() {
        INSTANCE = this;
    }
}

