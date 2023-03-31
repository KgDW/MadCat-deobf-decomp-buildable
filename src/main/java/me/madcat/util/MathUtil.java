package me.madcat.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class MathUtil
implements Wrapper {
    public static double normalize(double d, double d2, double d3) {
        return (d - d2) / (d3 - d2);
    }

    public static double[] directionSpeed(double d) {
        float f = MathUtil.mc.player.movementInput.moveForward;
        float f2 = MathUtil.mc.player.movementInput.moveStrafe;
        float f3 = MathUtil.mc.player.prevRotationYaw + (MathUtil.mc.player.rotationYaw - MathUtil.mc.player.prevRotationYaw) * mc.getRenderPartialTicks();
        if (f != 0.0f) {
            if (f2 > 0.0f) {
                f3 += (float)(f > 0.0f ? -45 : 45);
            } else if (f2 < 0.0f) {
                f3 += (float)(f > 0.0f ? 45 : -45);
            }
            f2 = 0.0f;
            if (f > 0.0f) {
                f = 1.0f;
            } else if (f < 0.0f) {
                f = -1.0f;
            }
        }
        double d2 = Math.sin(Math.toRadians(f3 + 90.0f));
        double d3 = Math.cos(Math.toRadians(f3 + 90.0f));
        double d4 = (double)f * d * d3 + (double)f2 * d * d2;
        double d5 = (double)f * d * d2 - (double)f2 * d * d3;
        return new double[]{d4, d5};
    }

    public static Vec3d calculateLine(Vec3d vec3d, Vec3d vec3d2, double d) {
        double d2 = Math.sqrt(MathUtil.multiply(vec3d2.x - vec3d.x) + MathUtil.multiply(vec3d2.y - vec3d.y) + MathUtil.multiply(vec3d2.z - vec3d.z));
        double d3 = (vec3d2.x - vec3d.x) / d2;
        double d4 = (vec3d2.y - vec3d.y) / d2;
        double d5 = (vec3d2.z - vec3d.z) / d2;
        double d6 = vec3d.x + d3 * d;
        double d7 = vec3d.y + d4 * d;
        double d8 = vec3d.z + d5 * d;
        return new Vec3d(d6, d7, d8);
    }

    public static <K, V extends Comparable<? super V>> LinkedHashMap sortByValue(Map<K, V> map, boolean bl) {
        LinkedList<Map.Entry<K, V>> linkedList = new LinkedList<>(map.entrySet());
        if (bl) {
            linkedList.sort(Map.Entry.comparingByValue(Comparator.reverseOrder()));
        } else {
            linkedList.sort(Map.Entry.comparingByValue());
        }
        LinkedHashMap linkedHashMap = new LinkedHashMap();
        for (Map.Entry entry : linkedList) {
            linkedHashMap.put(entry.getKey(), entry.getValue());
        }
        return linkedHashMap;
    }

    public static Vec3d direction(float f) {
        return new Vec3d(Math.cos(MathUtil.degToRad(f + 90.0f)), 0.0, Math.sin(MathUtil.degToRad(f + 90.0f)));
    }

    public static int clamp(int n, int n2, int n3) {
        return n < n2 ? n2 : Math.min(n, n3);
    }

    public static Integer increaseNumber(int n, int n2, int n3) {
        if (n < n2) {
            return n + n3;
        }
        return n2;
    }

    public static float[] calcAngle(Vec3d vec3d, Vec3d vec3d2) {
        double d = vec3d2.x - vec3d.x;
        double d2 = (vec3d2.y - vec3d.y) * -1.0;
        double d3 = vec3d2.z - vec3d.z;
        double d4 = MathHelper.sqrt(d * d + d3 * d3);
        return new float[]{(float)MathHelper.wrapDegrees(Math.toDegrees(Math.atan2(d3, d)) - 90.0), (float)MathHelper.wrapDegrees(Math.toDegrees(Math.atan2(d2, d4)))};
    }

    public static double multiply(double d) {
        return d * d;
    }

    public static String getTimeOfDay() {
        Calendar calendar = Calendar.getInstance();
        int n = calendar.get(Calendar.HOUR_OF_DAY);
        if (n < 12) {
            return "Good Morning ";
        }
        if (n < 16) {
            return "Good Afternoon ";
        }
        if (n < 21) {
            return "Good Evening ";
        }
        return "Good Night ";
    }

    public static List<Vec3d> getBlockBlocks(Entity entity) {
        ArrayList<Vec3d> arrayList = new ArrayList<>();
        AxisAlignedBB axisAlignedBB = entity.getEntityBoundingBox();
        double d = entity.posY;
        double d2 = MathUtil.round(axisAlignedBB.minX, 0);
        double d3 = MathUtil.round(axisAlignedBB.minZ, 0);
        double d4 = MathUtil.round(axisAlignedBB.maxX, 0);
        double d5 = MathUtil.round(axisAlignedBB.maxZ, 0);
        if (d2 != d4) {
            arrayList.add(new Vec3d(d2, d, d3));
            arrayList.add(new Vec3d(d4, d, d3));
            if (d3 != d5) {
                arrayList.add(new Vec3d(d2, d, d5));
                arrayList.add(new Vec3d(d4, d, d5));
                return arrayList;
            }
        } else if (d3 != d5) {
            arrayList.add(new Vec3d(d2, d, d3));
            arrayList.add(new Vec3d(d2, d, d5));
            return arrayList;
        }
        arrayList.add(entity.getPositionVector());
        return arrayList;
    }

    public static Float decreaseNumber(float f, float f2, float f3) {
        if (f > f2) {
            return f - f3;
        }
        return f2;
    }

    public static Float increaseNumber(float f, float f2, float f3) {
        if (f < f2) {
            return f + f3;
        }
        return f2;
    }

    public static float[] calcAngleNoY(Vec3d vec3d, Vec3d vec3d2) {
        double d = vec3d2.x - vec3d.x;
        double d2 = vec3d2.z - vec3d.z;
        return new float[]{(float)MathHelper.wrapDegrees(Math.toDegrees(Math.atan2(d2, d)) - 90.0)};
    }

    public static Vec3d interpolateEntity(Entity entity, float f) {
        return new Vec3d(entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * (double)f, entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * (double)f, entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * (double)f);
    }

    public static double animate(double d, double d2, double d3) {
        boolean bl = false;
        boolean bl2 = d > d2 || (bl = false);
        if (d3 < 0.0) {
            d3 = 0.0;
        } else if (d3 > 1.0) {
            d3 = 1.0;
        }
        double d4 = Math.max(d, d2) - Math.min(d, d2);
        double d5 = d4 * d3;
        if (d5 < 0.1) {
            d5 = 0.1;
        }
        d2 = bl ? d2 + d5 : d2 - d5;
        return d2;
    }

    public static Vec3d roundVec(Vec3d vec3d, int n) {
        return new Vec3d(MathUtil.round(vec3d.x, n), MathUtil.round(vec3d.y, n), MathUtil.round(vec3d.z, n));
    }

    public static float wrap(float f) {
        float f2 = f % 360.0f;
        if (f2 >= 180.0f) {
            f2 -= 360.0f;
        }
        if (f2 < -180.0f) {
            f2 += 360.0f;
        }
        return f2;
    }

    public static double round(double d, int n) {
        if (n < 0) {
            throw new IllegalArgumentException();
        }
        BigDecimal bigDecimal = BigDecimal.valueOf(d);
        bigDecimal = bigDecimal.setScale(n, RoundingMode.FLOOR);
        return bigDecimal.doubleValue();
    }

    public static Vec3d[] convertVectors(Vec3d vec3d, Vec3d[] vec3dArray) {
        Vec3d[] vec3dArray2 = new Vec3d[vec3dArray.length];
        for (int i = 0; i < vec3dArray.length; ++i) {
            vec3dArray2[i] = vec3d.add(vec3dArray[i]);
        }
        return vec3dArray2;
    }

    public static double square(double d) {
        return d * d;
    }

    public static float animate(float f, float f2, float f3) {
        float f4 = (f2 - f) / Math.max((float)Minecraft.getDebugFPS(), 5.0f) * 15.0f;
        if (f4 > 0.0f) {
            f4 = Math.max(f3, f4);
            f4 = Math.min(f2 - f, f4);
        } else if (f4 < 0.0f) {
            f4 = Math.min(-f3, f4);
            f4 = Math.max(f2 - f, f4);
        }
        return f + f4;
    }

    public static float getInterpolatedFloat(float f, float f2, float f3) {
        return f + (f2 - f) * f3;
    }

    public static float round(float f, int n) {
        if (n < 0) {
            throw new IllegalArgumentException();
        }
        BigDecimal bigDecimal = BigDecimal.valueOf(f);
        bigDecimal = bigDecimal.setScale(n, RoundingMode.FLOOR);
        return bigDecimal.floatValue();
    }

    public static float clamp(float f, float f2, float f3) {
        return f < f2 ? f2 : Math.min(f, f3);
    }

    public static Vec3d extrapolatePlayerPosition(EntityPlayer entityPlayer, int n) {
        Vec3d vec3d = new Vec3d(entityPlayer.lastTickPosX, entityPlayer.lastTickPosY, entityPlayer.lastTickPosZ);
        Vec3d vec3d2 = new Vec3d(entityPlayer.posX, entityPlayer.posY, entityPlayer.posZ);
        double d = MathUtil.multiply(entityPlayer.motionX) + MathUtil.multiply(entityPlayer.motionY) + MathUtil.multiply(entityPlayer.motionZ);
        Vec3d vec3d3 = MathUtil.calculateLine(vec3d, vec3d2, d * (double)n);
        return new Vec3d(vec3d3.x, entityPlayer.posY, vec3d3.z);
    }

    public static Integer decreaseNumber(int n, int n2, int n3) {
        if (n > n2) {
            return n - n3;
        }
        return n2;
    }

    public static double degToRad(double d) {
        return d * 0.01745329238474369;
    }
}

