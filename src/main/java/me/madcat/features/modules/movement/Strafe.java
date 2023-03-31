package me.madcat.features.modules.movement;

import java.util.Objects;
import me.madcat.MadCat;
import me.madcat.event.events.ClientEvent;
import me.madcat.event.events.MoveEvent;
import me.madcat.event.events.UpdateWalkingPlayerEvent;
import me.madcat.features.modules.Module;
import me.madcat.features.setting.Setting;
import me.madcat.util.EntityUtil;
import me.madcat.util.Timer;
import net.minecraft.init.MobEffects;
import net.minecraft.util.MovementInput;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class Strafe
extends Module {
    private static Strafe INSTANCE;
    private final Setting<Mode> mode = this.register(new Setting<>("Mode", Mode.NCP));
    private final Setting<Boolean> limiter = this.register(new Setting<>("SetGround", Boolean.TRUE, this::new0));
    private final Setting<Float> speed = this.register(new Setting<>("Speed", 2.0f, 1.0f, 5.0f, this::new1));
    private final Timer timer = new Timer();
    public final Setting<Boolean> strafeJump = this.register(new Setting<>("Jump", Boolean.FALSE));
    private int stage = 1;
    private double moveSpeed;
    private double lastDist;

    public Strafe() {
        super("Strafe", "AirControl etc", Module.Category.MOVEMENT);
        INSTANCE = this;
    }

    public static Strafe INSTANCE() {
        if (INSTANCE == null) {
            INSTANCE = new Strafe();
        }
        return INSTANCE;
    }

    public static double getBaseMoveSpeed() {
        double d = 0.272;
        if (Strafe.mc.player.isPotionActive(MobEffects.SPEED)) {
            int n = Objects.requireNonNull(Strafe.mc.player.getActivePotionEffect(MobEffects.SPEED)).getAmplifier();
            d *= 1.0 + 0.2 * (double)n;
        }
        return d;
    }

    @Override
    public void onEnable() {
        this.timer.reset();
        this.moveSpeed = Strafe.getBaseMoveSpeed();
    }

    @Override
    public void onDisable() {
        this.moveSpeed = 0.0;
    }

    @SubscribeEvent
    public void onUpdateWalkingPlayer(UpdateWalkingPlayerEvent updateWalkingPlayerEvent) {
        if (updateWalkingPlayerEvent.getStage() == 0) {
            this.lastDist = Math.sqrt((Strafe.mc.player.posX - Strafe.mc.player.prevPosX) * (Strafe.mc.player.posX - Strafe.mc.player.prevPosX) + (Strafe.mc.player.posZ - Strafe.mc.player.prevPosZ) * (Strafe.mc.player.posZ - Strafe.mc.player.prevPosZ));
        }
    }

    @SubscribeEvent
    public void onMove(MoveEvent moveEvent) {
        if (this.mode.getValue() == Mode.NCP) {
            this.doNCP(moveEvent);
        }
    }

    private void doNCP(MoveEvent moveEvent) {
        double d;
        if (this.shouldReturn()) {
            return;
        }
        if (!this.limiter.getValue() && Strafe.mc.player.onGround) {
            this.stage = 2;
        }
        switch (this.stage) {
            case 0: {
                ++this.stage;
                this.lastDist = 0.0;
                break;
            }
            case 2: {
                d = 0.40123128;
                if (Strafe.mc.player.moveForward == 0.0f && Strafe.mc.player.moveStrafing == 0.0f || !Strafe.mc.player.onGround) break;
                if (Strafe.mc.player.isPotionActive(MobEffects.JUMP_BOOST)) {
                    d += (float)(Objects.requireNonNull(Strafe.mc.player.getActivePotionEffect(MobEffects.JUMP_BOOST)).getAmplifier() + 1) * 0.1f;
                }
                if (this.strafeJump.getValue()) {
                    Strafe.mc.player.motionY = d;
                }
                if (this.strafeJump.getValue()) {
                    moveEvent.setY(Strafe.mc.player.motionY);
                }
                this.moveSpeed *= this.speed.getValue();
                break;
            }
            case 3: {
                this.moveSpeed = this.lastDist - 0.76 * (this.lastDist - Strafe.getBaseMoveSpeed());
                break;
            }
            default: {
                if (Strafe.mc.world.getCollisionBoxes(Strafe.mc.player, Strafe.mc.player.getEntityBoundingBox().offset(0.0, Strafe.mc.player.motionY, 0.0)).size() > 0 || Strafe.mc.player.collidedVertically && this.stage > 0) {
                    this.stage = Strafe.mc.player.moveForward != 0.0f || Strafe.mc.player.moveStrafing != 0.0f ? 1 : 0;
                }
                this.moveSpeed = this.lastDist - this.lastDist / 159.0;
            }
        }
        this.moveSpeed = Math.max(this.moveSpeed, Strafe.getBaseMoveSpeed());
        d = Strafe.mc.player.movementInput.moveForward;
        double d2 = Strafe.mc.player.movementInput.moveStrafe;
        double d3 = Strafe.mc.player.rotationYaw;
        if (d == 0.0 && d2 == 0.0) {
            moveEvent.setX(0.0);
            moveEvent.setZ(0.0);
        } else if (d != 0.0 && d2 != 0.0) {
            d *= Math.sin(0.7853981633974483);
            d2 *= Math.cos(0.7853981633974483);
        }
        moveEvent.setX((d * this.moveSpeed * -Math.sin(Math.toRadians(d3)) + d2 * this.moveSpeed * Math.cos(Math.toRadians(d3))) * 0.99);
        moveEvent.setZ((d * this.moveSpeed * Math.cos(Math.toRadians(d3)) - d2 * this.moveSpeed * -Math.sin(Math.toRadians(d3))) * 0.99);
        ++this.stage;
    }

    @SubscribeEvent
    public void onMode(MoveEvent moveEvent) {
        if (!(this.shouldReturn() || moveEvent.getStage() != 0 || this.mode.getValue() != Mode.INSTANT || Strafe.nullCheck() || Strafe.mc.player.isSneaking() || Strafe.mc.player.isInWater() || Strafe.mc.player.isInLava() || Strafe.mc.player.movementInput.moveForward == 0.0f && Strafe.mc.player.movementInput.moveStrafe == 0.0f)) {
            if (Strafe.mc.player.onGround && this.strafeJump.getValue()) {
                Strafe.mc.player.motionY = 0.4;
                moveEvent.setY(0.4);
            }
            MovementInput movementInput = Strafe.mc.player.movementInput;
            float f = movementInput.moveForward;
            float f2 = movementInput.moveStrafe;
            float f3 = Strafe.mc.player.rotationYaw;
            if ((double)f == 0.0 && (double)f2 == 0.0) {
                moveEvent.setX(0.0);
                moveEvent.setZ(0.0);
            } else {
                if ((double)f != 0.0) {
                    if ((double)f2 > 0.0) {
                        f3 += (float)((double)f > 0.0 ? -45 : 45);
                    } else if ((double)f2 < 0.0) {
                        f3 += (float)((double)f > 0.0 ? 45 : -45);
                    }
                    f2 = 0.0f;
                }
                f2 = f2 == 0.0f ? f2 : ((double)f2 > 0.0 ? 1.0f : -1.0f);
                moveEvent.setX((double)f * EntityUtil.getMaxSpeed() * Math.cos(Math.toRadians(f3 + 90.0f)) + (double)f2 * EntityUtil.getMaxSpeed() * Math.sin(Math.toRadians(f3 + 90.0f)));
                moveEvent.setZ((double)f * EntityUtil.getMaxSpeed() * Math.sin(Math.toRadians(f3 + 90.0f)) - (double)f2 * EntityUtil.getMaxSpeed() * Math.cos(Math.toRadians(f3 + 90.0f)));
            }
        }
    }

    @SubscribeEvent
    public void onSettingChange(ClientEvent clientEvent) {
        if (clientEvent.getStage() == 2 && clientEvent.getSetting().equals(this.mode) && this.mode.getPlannedValue() == Mode.INSTANT) {
            Strafe.mc.player.motionY = -0.1;
        }
    }

    private boolean shouldReturn() {
        return MadCat.moduleManager.isModuleEnabled("Speed") || MadCat.moduleManager.isModuleEnabled("PacketFly") || MadCat.moduleManager.isModuleEnabled("Phase") || MadCat.moduleManager.isModuleEnabled("Flight");
    }

    private boolean new1(Float f) {
        return this.mode.getValue() == Mode.NCP;
    }

    private boolean new0(Boolean bl) {
        return this.mode.getValue() == Mode.NCP;
    }

    public enum Mode {
        NCP,
        INSTANT

    }
}

