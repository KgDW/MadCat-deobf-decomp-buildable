package me.madcat.event.events;

import me.madcat.event.EventStage;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.eventhandler.Cancelable;

@Cancelable
public class UpdateWalkingPlayerEventTwo
extends EventStage {
    public static UpdateWalkingPlayerEventTwo INSTANCE;
    protected float yaw;
    protected float pitch;
    protected double x;
    protected double y;
    protected double z;
    protected boolean onGround;

    public UpdateWalkingPlayerEventTwo(int n, double d, double d2, double d3, float f, float f2, boolean bl) {
        super(n);
        INSTANCE = this;
        this.x = d;
        this.y = d2;
        this.z = d3;
        this.yaw = f;
        this.pitch = f2;
        this.onGround = bl;
    }

    public UpdateWalkingPlayerEventTwo(int n, UpdateWalkingPlayerEventTwo updateWalkingPlayerEventTwo) {
        this(n, updateWalkingPlayerEventTwo.x, updateWalkingPlayerEventTwo.y, updateWalkingPlayerEventTwo.z, updateWalkingPlayerEventTwo.yaw, updateWalkingPlayerEventTwo.pitch, updateWalkingPlayerEventTwo.onGround);
    }

    public void setRotation(float f, float f2) {
        if (Minecraft.getMinecraft().player != null) {
            Minecraft.getMinecraft().player.rotationYawHead = f;
            Minecraft.getMinecraft().player.renderYawOffset = f;
        }
        this.setYaw(f);
        this.setPitch(f2);
    }

    public void setPostion(double d, double d2, double d3, boolean bl) {
        this.setX(d);
        this.setY(d2);
        this.setZ(d3);
        this.setOnGround(bl);
    }

    public void setPostion(double d, double d2, double d3, float f, float f2, boolean bl) {
        this.setX(d);
        this.setY(d2);
        this.setZ(d3);
        this.setYaw(f);
        this.setPitch(f2);
        this.setOnGround(bl);
    }

    public float getYaw() {
        return this.yaw;
    }

    public void setYaw(float f) {
        this.yaw = f;
    }

    public void setYaw(double d) {
        this.yaw = (float)d;
    }

    public float getPitch() {
        return this.pitch;
    }

    public void setPitch(float f) {
        this.pitch = f;
    }

    public void setPitch(double d) {
        this.pitch = (float)d;
    }

    public double getX() {
        return this.x;
    }

    public void setX(double d) {
        this.x = d;
    }

    public double getY() {
        return this.y;
    }

    public void setY(double d) {
        this.y = d;
    }

    public double getZ() {
        return this.z;
    }

    public void setZ(double d) {
        this.z = d;
    }

    public boolean isOnGround() {
        return this.onGround;
    }

    public void setOnGround(boolean bl) {
        this.onGround = bl;
    }
}

