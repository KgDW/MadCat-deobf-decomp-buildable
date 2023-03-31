package me.madcat.features.modules.combat.surround;

import net.minecraft.network.play.client.CPacketPlayer;

public class CustomCPacketPlayer extends CPacketPlayer {
    private float yaw;

    public CustomCPacketPlayer(float yaw) {
        this.yaw = yaw;
    }

    public void setYaw(float yaw) {
        this.yaw = yaw;
    }

    public float getYaw() {
        return this.yaw;
    }
}
