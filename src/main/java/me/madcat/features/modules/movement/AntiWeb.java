package me.madcat.features.modules.movement;

import me.madcat.features.modules.Module;
import me.madcat.features.setting.Setting;
import me.madcat.util.MathUtil;

public class AntiWeb
extends Module {
    private final Setting<Float> speed = this.register(new Setting<>("Factor", 10.0f, 1.0f, 10.0f));

    public AntiWeb() {
        super("AntiWeb", "Stops you being slowed down by webs", Module.Category.MOVEMENT);
    }

    @Override
    public void onUpdate() {
        if (AntiWeb.mc.player.isDead) {
            double[] dArray = MathUtil.directionSpeed((double) this.speed.getValue() / 10.0);
            AntiWeb.mc.player.motionX = dArray[0];
            AntiWeb.mc.player.motionZ = dArray[1];
            if (AntiWeb.mc.gameSettings.keyBindSneak.isKeyDown()) {
                AntiWeb.mc.player.motionY -= this.speed.getValue() / 10.0f;
            }
        }
    }
}

