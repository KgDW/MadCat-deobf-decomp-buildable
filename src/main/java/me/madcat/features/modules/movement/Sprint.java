package me.madcat.features.modules.movement;

import me.madcat.features.modules.Module;
import me.madcat.features.setting.Setting;

public class Sprint
extends Module {
    public final Setting<Boolean> shift = this.register(new Setting<>("StairJump", false));

    public Sprint() {
        super("Sprint", "Force sprint", Module.Category.MOVEMENT);
    }

    @Override
    public void onTick() {
        if (!(Sprint.mc.player.moveForward == 0.0f && Sprint.mc.player.moveStrafing == 0.0f || Sprint.mc.player.isSprinting())) {
            Sprint.mc.player.setSprinting(true);
        }
        if (this.shift.getValue() && Sprint.mc.player.onGround && Sprint.mc.player.posY - Math.floor(Sprint.mc.player.posY) > 0.0 && Sprint.mc.player.moveForward != 0.0f) {
            Sprint.mc.player.jump();
        }
    }
}

