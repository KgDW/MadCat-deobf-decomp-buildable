package me.madcat.features.modules.movement;

import me.madcat.event.events.PacketEvent;
import me.madcat.features.modules.Module;
import me.madcat.features.setting.Setting;
import net.minecraft.network.play.client.CPacketConfirmTeleport;
import net.minecraft.network.play.client.CPacketVehicleMove;
import net.minecraft.network.play.server.SPacketMoveVehicle;
import net.minecraft.network.play.server.SPacketPlayerPosLook;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class BoatFly
extends Module {
    public final Setting<Double> speed = this.register(new Setting<>("Speed", 3.0, 1.0, 10.0));
    public final Setting<Double> verticalSpeed = this.register(new Setting<>("VerticalSpeed", 3.0, 1.0, 10.0));
    public final Setting<Boolean> noKick = this.register(new Setting<>("No-Kick", true));
    public final Setting<Boolean> packet = this.register(new Setting<>("Packet", true));
    public final Setting<Integer> packets = this.register(new Setting<Object>("Packets", 3, 1, 5, this::new0));
    private int teleportID;

    public BoatFly() {
        super("BoatFly", "/fly but boat", Module.Category.MOVEMENT);
    }

    @Override
    public void onUpdate() {
        if (BoatFly.fullNullCheck()) {
            return;
        }
        if (BoatFly.mc.player.getRidingEntity() == null) {
            return;
        }
        BoatFly.mc.player.getRidingEntity().setNoGravity(true);
        BoatFly.mc.player.getRidingEntity().motionY = 0.0;
        if (BoatFly.mc.gameSettings.keyBindJump.isKeyDown()) {
            BoatFly.mc.player.getRidingEntity().onGround = false;
            BoatFly.mc.player.getRidingEntity().motionY = this.verticalSpeed.getValue() / 10.0;
        }
        if (BoatFly.mc.gameSettings.keyBindSprint.isKeyDown()) {
            BoatFly.mc.player.getRidingEntity().onGround = false;
            BoatFly.mc.player.getRidingEntity().motionY = -(this.verticalSpeed.getValue() / 10.0);
        }
        double[] dArray = this.directionSpeed(this.speed.getValue() / 2.0);
        if (BoatFly.mc.player.movementInput.moveStrafe != 0.0f || BoatFly.mc.player.movementInput.moveForward != 0.0f) {
            BoatFly.mc.player.getRidingEntity().motionX = dArray[0];
            BoatFly.mc.player.getRidingEntity().motionZ = dArray[1];
        } else {
            BoatFly.mc.player.getRidingEntity().motionX = 0.0;
            BoatFly.mc.player.getRidingEntity().motionZ = 0.0;
        }
        if (this.noKick.getValue()) {
            if (BoatFly.mc.gameSettings.keyBindJump.isKeyDown()) {
                if (BoatFly.mc.player.ticksExisted % 8 < 2) {
                    BoatFly.mc.player.getRidingEntity().motionY = -0.04f;
                }
            } else if (BoatFly.mc.player.ticksExisted % 8 < 4) {
                BoatFly.mc.player.getRidingEntity().motionY = -0.08f;
            }
        }
        this.handlePackets(BoatFly.mc.player.getRidingEntity().motionX, BoatFly.mc.player.getRidingEntity().motionY, BoatFly.mc.player.getRidingEntity().motionZ);
    }

    public void handlePackets(double d, double d2, double d3) {
        if (this.packet.getValue()) {
            Vec3d vec3d = new Vec3d(d, d2, d3);
            if (BoatFly.mc.player.getRidingEntity() == null) {
                return;
            }
            Vec3d vec3d2 = BoatFly.mc.player.getRidingEntity().getPositionVector().add(vec3d);
            BoatFly.mc.player.getRidingEntity().setPosition(vec3d2.x, vec3d2.y, vec3d2.z);
            BoatFly.mc.player.connection.sendPacket(new CPacketVehicleMove(BoatFly.mc.player.getRidingEntity()));
            for (int i = 0; i < this.packets.getValue(); ++i) {
                BoatFly.mc.player.connection.sendPacket(new CPacketConfirmTeleport(this.teleportID++));
            }
        }
    }

    @SubscribeEvent
    public void onReceivePacket(PacketEvent.Receive receive) {
        if (BoatFly.fullNullCheck()) {
            return;
        }
        if (receive.getPacket() instanceof SPacketMoveVehicle && BoatFly.mc.player.isRiding()) {
            receive.setCanceled(true);
        }
        if (receive.getPacket() instanceof SPacketPlayerPosLook) {
            this.teleportID = ((SPacketPlayerPosLook) receive.getPacket()).getTeleportId();
        }
    }

    private double[] directionSpeed(double d) {
        float f = BoatFly.mc.player.movementInput.moveForward;
        float f2 = BoatFly.mc.player.movementInput.moveStrafe;
        float f3 = BoatFly.mc.player.prevRotationYaw + (BoatFly.mc.player.rotationYaw - BoatFly.mc.player.prevRotationYaw) * mc.getRenderPartialTicks();
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

    private boolean new0(Object object) {
        return this.packet.getValue();
    }
}

