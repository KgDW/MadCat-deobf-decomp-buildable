package me.madcat.manager;

import me.madcat.features.Feature;
import me.madcat.mixin.mixins.IEntityPlayerSP;
import me.madcat.util.MathUtil;
import me.madcat.util.RotationUtil;
import net.minecraft.entity.Entity;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class RotationManager
extends Feature {
    private float yaw;
    private float pitch;

    public String getDirection4D(boolean bl) {
        return RotationUtil.getDirection4D(bl);
    }

    public int getDirection4D() {
        return RotationUtil.getDirection4D();
    }

    public float[] getAngle(Vec3d vec3d) {
        Vec3d vec3d2 = new Vec3d(RotationManager.mc.player.posX, RotationManager.mc.player.posY + (double)RotationManager.mc.player.getEyeHeight(), RotationManager.mc.player.posZ);
        double d = vec3d.x - vec3d2.x;
        double d2 = vec3d.y - vec3d2.y;
        double d3 = vec3d.z - vec3d2.z;
        double d4 = Math.sqrt(d * d + d3 * d3);
        float f = (float)Math.toDegrees(Math.atan2(d3, d)) - 90.0f;
        float f2 = (float)(-Math.toDegrees(Math.atan2(d2, d4)));
        return new float[]{RotationManager.mc.player.rotationYaw + MathHelper.wrapDegrees(f - RotationManager.mc.player.rotationYaw), RotationManager.mc.player.rotationPitch + MathHelper.wrapDegrees(f2 - RotationManager.mc.player.rotationPitch)};
    }

    public void lookAtVec3d(Vec3d vec3d) {
        float[] fArray = MathUtil.calcAngle(RotationManager.mc.player.getPositionEyes(mc.getRenderPartialTicks()), new Vec3d(vec3d.x, vec3d.y, vec3d.z));
        this.setPlayerRotations(fArray[0], fArray[1]);
    }

    public void lookAtVec3dPacket(Vec3d vec3d, boolean bl, boolean bl2) {
        float[] fArray = this.getAngle(vec3d);
        RotationManager.mc.player.connection.sendPacket(new CPacketPlayer.Rotation(fArray[0], bl ? (float)MathHelper.normalizeAngle((int)fArray[1], 360) : fArray[1], RotationManager.mc.player.onGround));
        if (bl2) {
            ((IEntityPlayerSP)RotationManager.mc.player).setLastReportedYaw(fArray[0]);
            ((IEntityPlayerSP)RotationManager.mc.player).setLastReportedPitch(fArray[1]);
        }
    }

    public void setPlayerPitch(float f) {
        RotationManager.mc.player.rotationPitch = f;
    }

    public void setPlayerYaw(float f) {
        RotationManager.mc.player.rotationYaw = f;
        RotationManager.mc.player.rotationYawHead = f;
    }

    public void setPlayerRotations(float f, float f2) {
        RotationManager.mc.player.rotationYaw = f;
        RotationManager.mc.player.rotationYawHead = f;
        RotationManager.mc.player.rotationPitch = f2;
    }

    public void setYaw(float f) {
        this.yaw = f;
    }

    public void lookAtEntity(Entity entity) {
        float[] fArray = MathUtil.calcAngle(RotationManager.mc.player.getPositionEyes(mc.getRenderPartialTicks()), entity.getPositionEyes(mc.getRenderPartialTicks()));
        this.setPlayerRotations(fArray[0], fArray[1]);
    }

    public void setPitch(float f) {
        this.pitch = f;
    }

    public void lookAtVec3d(double d, double d2, double d3) {
        Vec3d vec3d = new Vec3d(d, d2, d3);
        this.lookAtVec3d(vec3d);
    }

    public boolean isInFov(BlockPos blockPos) {
        int n = this.getYaw4D();
        if (n == 0 && (double)blockPos.getZ() - RotationManager.mc.player.getPositionVector().z < 0.0) {
            return false;
        }
        if (n == 1 && (double)blockPos.getX() - RotationManager.mc.player.getPositionVector().x > 0.0) {
            return false;
        }
        if (n == 2 && (double)blockPos.getZ() - RotationManager.mc.player.getPositionVector().z > 0.0) {
            return false;
        }
        return n != 3 || (double)blockPos.getX() - RotationManager.mc.player.getPositionVector().x >= 0.0;
    }

    public void restoreRotations() {
        RotationManager.mc.player.rotationYaw = this.yaw;
        RotationManager.mc.player.rotationYawHead = this.yaw;
        RotationManager.mc.player.rotationPitch = this.pitch;
    }

    public float getYaw() {
        return this.yaw;
    }

    public int getYaw4D() {
        return MathHelper.floor((double)(RotationManager.mc.player.rotationYaw * 4.0f / 360.0f) + 0.5) & 3;
    }

    public void lookAtVec3dPacket(Vec3d vec3d, boolean bl) {
        float[] fArray = this.getAngle(vec3d);
        RotationManager.mc.player.connection.sendPacket(new CPacketPlayer.Rotation(fArray[0], bl ? (float)MathHelper.normalizeAngle((int)fArray[1], 360) : fArray[1], RotationManager.mc.player.onGround));
    }

    public void lookAtPos(BlockPos blockPos) {
        float[] fArray = MathUtil.calcAngle(RotationManager.mc.player.getPositionEyes(mc.getRenderPartialTicks()), new Vec3d((float)blockPos.getX() + 0.5f, (float)blockPos.getY() + 0.5f, (float)blockPos.getZ() + 0.5f));
        this.setPlayerRotations(fArray[0], fArray[1]);
    }

    public float getPitch() {
        return this.pitch;
    }

    public void updateRotations() {
        this.yaw = RotationManager.mc.player.rotationYaw;
        this.pitch = RotationManager.mc.player.rotationPitch;
    }
}

