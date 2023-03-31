package me.madcat.features.modules.movement;

import java.util.List;
import java.util.Objects;
import me.madcat.event.events.MoveEvent;
import me.madcat.event.events.PacketEvent;
import me.madcat.event.events.UpdateWalkingPlayerEventTwo;
import me.madcat.features.modules.Module;
import me.madcat.features.setting.Setting;
import me.madcat.util.MathUtil;
import me.madcat.util.Wrapper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.network.play.server.SPacketPlayerPosLook;
import net.minecraft.util.MovementInput;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class LongJump
extends Module {
    public final Setting<Double> boost = this.register(new Setting<>("Boost", 4.5, 0.1, 20.0));
    public final Setting<Boolean> noKick = this.register(new Setting<>("AntiKick", true));
    public int stage;
    public int airTicks;
    public int groundTicks;
    public double speed;
    public double distance;

    public LongJump() {
        super("LongJump", "ear", Module.Category.MOVEMENT);
    }

    @SubscribeEvent
    public void PacketEvent(PacketEvent.Receive receive) {
        if (receive.getPacket() instanceof SPacketPlayerPosLook) {
            if (this.noKick.getValue()) {
                this.disable();
            }
            this.speed = 0.0;
            this.stage = 0;
            this.airTicks = 0;
            this.groundTicks = 0;
        }
    }

    @Override
    public void onEnable() {
        if (LongJump.mc.player != null) {
            this.distance = MovementUtil.getDistance2D();
            this.speed = MovementUtil.getSpeed();
        }
        this.stage = 0;
        this.airTicks = 0;
        this.groundTicks = 0;
    }

    @SubscribeEvent
    public void onUpdate(UpdateWalkingPlayerEventTwo updateWalkingPlayerEventTwo) {
        this.distance = MovementUtil.getDistance2D();
    }

    @SubscribeEvent
    public void MoveEvent(MoveEvent moveEvent) {
        if (LongJump.mc.player.moveStrafing <= 0.0f && LongJump.mc.player.moveForward <= 0.0f) {
            this.stage = 1;
        }
        if (MathUtil.round(LongJump.mc.player.posY - (double)((int)LongJump.mc.player.posY), 3) == MathUtil.round(0.943, 3)) {
            LongJump.mc.player.motionY -= 0.03;
            moveEvent.setY(moveEvent.getY() - 0.03);
        }
        if (this.stage == 1 && MovementUtil.isMoving()) {
            this.stage = 2;
            this.speed = this.boost.getValue() * MovementUtil.getSpeed() - 0.01;
        } else if (this.stage == 2) {
            this.stage = 3;
            LongJump.mc.player.motionY = 0.424;
            moveEvent.setY(0.424);
            this.speed *= 2.149802;
        } else if (this.stage == 3) {
            this.stage = 4;
            double d = 0.66 * (this.distance - MovementUtil.getSpeed());
            this.speed = this.distance - d;
        } else {
            if (LongJump.mc.world.getCollisionBoxes(LongJump.mc.player, LongJump.mc.player.getEntityBoundingBox().offset(0.0, LongJump.mc.player.motionY, 0.0)).size() > 0 || LongJump.mc.player.collidedVertically) {
                this.stage = 1;
            }
            this.speed = this.distance - this.distance / 159.0;
        }
        this.speed = Math.max(this.speed, MovementUtil.getSpeed());
        MovementUtil.strafe(moveEvent, this.speed);
        float f = LongJump.mc.player.movementInput.moveForward;
        float f2 = LongJump.mc.player.movementInput.moveStrafe;
        float f3 = LongJump.mc.player.rotationYaw;
        if (f == 0.0f && f2 == 0.0f) {
            moveEvent.setX(0.0);
            moveEvent.setZ(0.0);
        } else if (f != 0.0f) {
            if (f2 >= 1.0f) {
                f3 += (float)(f > 0.0f ? -45 : 45);
                f2 = 0.0f;
            } else if (f2 <= -1.0f) {
                f3 += (float)(f > 0.0f ? 45 : -45);
                f2 = 0.0f;
            }
            if (f > 0.0f) {
                f = 1.0f;
            } else if (f < 0.0f) {
                f = -1.0f;
            }
        }
        double d = Math.cos(Math.toRadians(f3 + 90.0f));
        double d2 = Math.sin(Math.toRadians(f3 + 90.0f));
        moveEvent.setX((double)f * this.speed * d + (double)f2 * this.speed * d2);
        moveEvent.setZ((double)f * this.speed * d2 - (double)f2 * this.speed * d);
    }

    public double getDistance(EntityPlayer entityPlayer, double d) {
        List list = entityPlayer.world.getCollisionBoxes(entityPlayer, entityPlayer.getEntityBoundingBox().offset(0.0, -d, 0.0));
        if (list.isEmpty()) {
            return 0.0;
        }
        double d2 = 0.0;
        return entityPlayer.posY - d2;
    }

    public static class MovementUtil {
        public static boolean isMoving() {
            return (double)Wrapper.mc.player.moveForward != 0.0 || (double)Wrapper.mc.player.moveStrafing != 0.0;
        }

        public static void strafe(MoveEvent moveEvent, double d) {
            if (MovementUtil.isMoving()) {
                double[] dArray = MovementUtil.strafe(d);
                moveEvent.setX(dArray[0]);
                moveEvent.setZ(dArray[1]);
            } else {
                moveEvent.setX(0.0);
                moveEvent.setZ(0.0);
            }
        }

        public static double[] strafe(double d) {
            return MovementUtil.strafe(Wrapper.mc.player, d);
        }

        public static double[] strafe(Entity entity, double d) {
            return MovementUtil.strafe(entity, Wrapper.mc.player.movementInput, d);
        }

        public static double[] strafe(Entity entity, MovementInput movementInput, double d) {
            float f = movementInput.moveForward;
            float f2 = movementInput.moveStrafe;
            float f3 = entity.prevRotationYaw + (entity.rotationYaw - entity.prevRotationYaw) * Wrapper.mc.getRenderPartialTicks();
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
            double d2 = (double)f * d * -Math.sin(Math.toRadians(f3)) + (double)f2 * d * Math.cos(Math.toRadians(f3));
            double d3 = (double)f * d * Math.cos(Math.toRadians(f3)) - (double)f2 * d * -Math.sin(Math.toRadians(f3));
            return new double[]{d2, d3};
        }

        public static double getDistance2D() {
            double d = Wrapper.mc.player.posX - Wrapper.mc.player.prevPosX;
            double d2 = Wrapper.mc.player.posZ - Wrapper.mc.player.prevPosZ;
            return Math.sqrt(d * d + d2 * d2);
        }

        public static double getSpeed() {
            return MovementUtil.getSpeed(false);
        }

        public static double getSpeed(boolean bl) {
            int n;
            double d = 0.2873;
            if (Wrapper.mc.player.isPotionActive(MobEffects.SPEED)) {
                n = Objects.requireNonNull(Wrapper.mc.player.getActivePotionEffect(MobEffects.SPEED)).getAmplifier();
                d *= 1.0 + 0.2 * (double)(n + 1);
            }
            if (bl && Wrapper.mc.player.isPotionActive(MobEffects.SLOWNESS)) {
                n = Objects.requireNonNull(Wrapper.mc.player.getActivePotionEffect(MobEffects.SLOWNESS)).getAmplifier();
                d /= 1.0 + 0.2 * (double)(n + 1);
            }
            return d;
        }
    }
}

