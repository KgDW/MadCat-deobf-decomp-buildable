package me.madcat.features.modules.movement;

import me.madcat.features.modules.Module;
import me.madcat.features.modules.exploit.Clip;
import me.madcat.util.EntityUtil;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.MovementInput;
import net.minecraft.util.MovementInputFromOptions;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.event.InputUpdateEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class AutoCenter
extends Module {
    BlockPos centerPos;

    public AutoCenter() {
        super("AutoCenter", "Moves player to the center of a block", Module.Category.MOVEMENT);
    }

    @Override
    public void onEnable() {
        this.centerPos = EntityUtil.getRoundedBlockPos(AutoCenter.mc.player);
    }

    private double getSpeed(Entity entity) {
        return Math.hypot(entity.motionX, entity.motionZ);
    }

    private boolean isFlying(EntityPlayer entityPlayer) {
        return entityPlayer.isElytraFlying() || entityPlayer.capabilities.isFlying;
    }

    @SubscribeEvent
    public void onInput(InputUpdateEvent inputUpdateEvent) {
        if (inputUpdateEvent.getMovementInput() instanceof MovementInputFromOptions && this.centerPos != null) {
            MovementInput movementInput = inputUpdateEvent.getMovementInput();
            this.resetMove(movementInput);
        }
    }

    private void resetMove(MovementInput movementInput) {
        movementInput.moveForward = 0.0f;
        movementInput.moveStrafe = 0.0f;
        movementInput.forwardKeyDown = false;
        movementInput.backKeyDown = false;
        movementInput.leftKeyDown = false;
        movementInput.rightKeyDown = false;
    }

    @Override
    public void onDisable() {
        AutoCenter.mc.player.motionX = 0.0;
        AutoCenter.mc.player.motionZ = 0.0;
    }

    @Override
    public void onTick() {
        if (Clip.INSTANCE.isEnabled()) {
            this.disable();
            return;
        }
        if (AutoCenter.mc.player.isEntityAlive()) {
            if (!this.isFlying(AutoCenter.mc.player)) {
                EntityPlayerSP entityPlayerSP = AutoCenter.mc.player;
                double d = this.getSpeed(entityPlayerSP);
                if (this.centerPos != null) {
                    if (AutoCenter.mc.player.posX == (double)this.centerPos.getX() && AutoCenter.mc.player.posZ == (double)this.centerPos.getZ()) {
                        this.disable();
                        return;
                    }
                    if (AutoCenter.mc.player.posX - (double)this.centerPos.getX() <= 0.7 && AutoCenter.mc.player.posX - (double)this.centerPos.getX() >= 0.3 && AutoCenter.mc.player.posZ - (double)this.centerPos.getZ() <= 0.7 && AutoCenter.mc.player.posZ - (double)this.centerPos.getZ() >= 0.3) {
                        this.disable();
                        return;
                    }
                    if (this.centerPos.getDistance((int)AutoCenter.mc.player.posX, (int)AutoCenter.mc.player.posY, (int)AutoCenter.mc.player.posZ) > 1.5) {
                        this.disable();
                        return;
                    }
                    Vec3d vec3d = AutoCenter.mc.player.getPositionVector();
                    Vec3d vec3d2 = new Vec3d((double)this.centerPos.getX() + 0.5, AutoCenter.mc.player.posY, (double)this.centerPos.getZ() + 0.5);
                    float f = this.getRotationTo(vec3d, vec3d2).x;
                    float f2 = f / 180.0f * (float)Math.PI;
                    double d2 = vec3d.distanceTo(vec3d2);
                    EntityPlayerSP entityPlayerSP2 = AutoCenter.mc.player;
                    double d3 = this.applySpeedPotionEffects(entityPlayerSP2);
                    double d4 = AutoCenter.mc.player.onGround ? d3 : Math.max(d + 0.02, d3);
                    double d5 = Math.min(d4, d2);
                    AutoCenter.mc.player.motionX = (double)(-((float)Math.sin(f2))) * d5;
                    AutoCenter.mc.player.motionZ = (double)((float)Math.cos(f2)) * d5;
                } else {
                    this.disable();
                }
            } else {
                this.disable();
            }
        } else {
            this.disable();
        }
    }

    private Vec2f getRotationTo(Vec3d vec3d, Vec3d vec3d2) {
        Vec3d vec3d3 = vec3d2.subtract(vec3d);
        return this.getRotationFromVec(vec3d3);
    }

    private double applySpeedPotionEffects(EntityLivingBase entityLivingBase) {
        PotionEffect potionEffect = entityLivingBase.getActivePotionEffect(MobEffects.SPEED);
        return potionEffect == null ? 0.2873 : 0.2873 * this.getSpeedEffectMultiplier(entityLivingBase);
    }

    private double getSpeedEffectMultiplier(EntityLivingBase entityLivingBase) {
        PotionEffect potionEffect = entityLivingBase.getActivePotionEffect(MobEffects.SPEED);
        return potionEffect == null ? 1.0 : 1.0 + ((double)potionEffect.getAmplifier() + 1.0) * 0.2;
    }

    private double normalizeAngle(double d) {
        double d2 = d;
        if ((d2 %= 360.0) >= 180.0) {
            d2 -= 360.0;
        }
        if (d2 < -180.0) {
            d2 += 360.0;
        }
        return d2;
    }

    private Vec2f getRotationFromVec(Vec3d vec3d) {
        double d = vec3d.x;
        double d2 = vec3d.z;
        double d3 = Math.hypot(d, d2);
        d2 = vec3d.z;
        double d4 = vec3d.x;
        double d5 = this.normalizeAngle(Math.toDegrees(Math.atan2(d2, d4)) - 90.0);
        double d6 = this.normalizeAngle(Math.toDegrees(-Math.atan2(vec3d.y, d3)));
        return new Vec2f((float)d5, (float)d6);
    }
}

