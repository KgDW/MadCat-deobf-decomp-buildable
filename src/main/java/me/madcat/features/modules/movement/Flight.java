package me.madcat.features.modules.movement;

import me.madcat.features.modules.Module;
import me.madcat.features.setting.Setting;

public final class Flight
extends Module {
    private final Setting<Float> speed = this.register(new Setting<>("Speed", 1.0f, 0.5f, 10.0f));
    private final Setting<Boolean> glide = this.register(new Setting<>("Glide", true));

    public Flight() {
        super("Flight", "Allows you to fly", Module.Category.MOVEMENT);
        Flight flight = this;
    }

    @Override
    public void onUpdate() {
        if (Flight.fullNullCheck()) {
            return;
        }
        if (Flight.mc.player == null || Flight.mc.world == null) {
            return;
        }
        Flight.mc.player.capabilities.isFlying = false;
        Flight.mc.player.motionX = 0.0;
        Flight.mc.player.motionY = 0.0;
        Flight.mc.player.motionZ = 0.0;
        Flight.mc.player.jumpMovementFactor = this.speed.getValue();
        if (this.glide.getValue() && !Flight.mc.player.onGround) {
            Flight.mc.player.motionY = -0.0315f;
            Flight.mc.player.jumpMovementFactor *= 1.21337f;
        }
        if (Flight.mc.gameSettings.keyBindJump.isKeyDown()) {
            Flight.mc.player.motionY += this.speed.getValue();
        }
        if (Flight.mc.gameSettings.keyBindSneak.isKeyDown()) {
            Flight.mc.player.motionY -= this.speed.getValue();
        }
    }
}

