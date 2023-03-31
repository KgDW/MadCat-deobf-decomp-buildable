package me.madcat.manager;

import me.madcat.features.Feature;
import net.minecraft.network.play.client.CPacketPlayer;

public class PositionManager
extends Feature {
    private boolean onground;
    private double x;
    private double y;
    private double z;

    public void restorePosition() {
        PositionManager.mc.player.posX = this.x;
        PositionManager.mc.player.posY = this.y;
        PositionManager.mc.player.posZ = this.z;
        PositionManager.mc.player.onGround = this.onground;
    }

    public double getY() {
        return this.y;
    }

    public double getX() {
        return this.x;
    }

    public void setPlayerPosition(double d, double d2, double d3) {
        PositionManager.mc.player.posX = d;
        PositionManager.mc.player.posY = d2;
        PositionManager.mc.player.posZ = d3;
    }

    public void setPlayerPosition(double d, double d2, double d3, boolean bl) {
        PositionManager.mc.player.posX = d;
        PositionManager.mc.player.posY = d2;
        PositionManager.mc.player.posZ = d3;
        PositionManager.mc.player.onGround = bl;
    }

    public double getZ() {
        return this.z;
    }

    public void setPositionPacket(double d, double d2, double d3, boolean bl, boolean bl2, boolean bl3) {
        PositionManager.mc.player.connection.sendPacket(new CPacketPlayer.Position(d, d2, d3, bl));
        if (bl2) {
            PositionManager.mc.player.setPosition(d, d2, d3);
            if (bl3) {
                this.updatePosition();
            }
        }
    }

    public void setX(double d) {
        this.x = d;
    }

    public void setY(double d) {
        this.y = d;
    }

    public void setZ(double d) {
        this.z = d;
    }

    public void updatePosition() {
        this.x = PositionManager.mc.player.posX;
        this.y = PositionManager.mc.player.posY;
        this.z = PositionManager.mc.player.posZ;
        this.onground = PositionManager.mc.player.onGround;
    }
}

