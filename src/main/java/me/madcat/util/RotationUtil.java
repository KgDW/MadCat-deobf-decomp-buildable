package me.madcat.util;

import me.madcat.util.BlockUtil;
import me.madcat.util.Wrapper;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class RotationUtil
implements Wrapper {

    public static void faceYawAndPitch(float f, float f2) {
        RotationUtil.mc.player.connection.sendPacket(new CPacketPlayer.Rotation(f, f2, RotationUtil.mc.player.onGround));
    }

    public static void facePos(BlockPos blockPos) {
        EnumFacing enumFacing = BlockUtil.getFirstFacing(blockPos);
        if (enumFacing == null) {
            return;
        }
        BlockPos blockPos2 = blockPos.offset(enumFacing);
        EnumFacing enumFacing2 = enumFacing.getOpposite();
        Vec3d vec3d = new Vec3d(blockPos2).add(0.5, 0.5, 0.5).add(new Vec3d(enumFacing2.getDirectionVec()).scale(0.5));
        RotationUtil.faceVector(vec3d, true);
    }

    public static int getDirection4D() {
        return MathHelper.floor((double)(RotationUtil.mc.player.rotationYaw * 4.0f / 360.0f) + 0.5) & 3;
    }

    public static String getDirection4D(boolean bl) {
        int n = RotationUtil.getDirection4D();
        if (n == 0) {
            return "South (+Z)";
        }
        if (n == 1) {
            return "West (-X)";
        }
        if (n == 2) {
            return (bl ? "\u00c2\u00a7\u0063" : "") + "North (-Z)";
        }
        if (n == 3) {
            return "East (+X)";
        }
        return "Loading...";
    }

    public static float[] getLegitRotations(Vec3d vec3d) {
        Vec3d vec3d2 = RotationUtil.getEyesPos();
        double d = vec3d.x - vec3d2.x;
        double d2 = vec3d.y - vec3d2.y;
        double d3 = vec3d.z - vec3d2.z;
        double d4 = Math.sqrt(d * d + d3 * d3);
        float f = (float)Math.toDegrees(Math.atan2(d3, d)) - 90.0f;
        float f2 = (float)(-Math.toDegrees(Math.atan2(d2, d4)));
        return new float[]{RotationUtil.mc.player.rotationYaw + MathHelper.wrapDegrees(f - RotationUtil.mc.player.rotationYaw), RotationUtil.mc.player.rotationPitch + MathHelper.wrapDegrees(f2 - RotationUtil.mc.player.rotationPitch)};
    }

    public static Vec3d getEyesPos() {
        return new Vec3d(RotationUtil.mc.player.posX, RotationUtil.mc.player.posY + (double)RotationUtil.mc.player.getEyeHeight(), RotationUtil.mc.player.posZ);
    }

    public static void faceVector(Vec3d vec3d, boolean bl) {
        float[] fArray = RotationUtil.getLegitRotations(vec3d);
        RotationUtil.mc.player.connection.sendPacket(new CPacketPlayer.Rotation(fArray[0], bl ? (float)MathHelper.normalizeAngle((int)fArray[1], 360) : fArray[1], RotationUtil.mc.player.onGround));
    }
}

