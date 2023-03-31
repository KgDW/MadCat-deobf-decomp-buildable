package me.madcat.manager;

import java.util.HashMap;
import me.madcat.features.Feature;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.MathHelper;

public class SpeedManager
extends Feature {
    public boolean didJumpLastTick = false;
    public final HashMap<EntityPlayer, Double> playerSpeeds = new HashMap();
    public static final double LAST_JUMP_INFO_DURATION_DEFAULT = 3.0;
    public double firstJumpSpeed = 0.0;
    public double lastJumpSpeed = 0.0;
    public long jumpInfoStartTime = 0L;
    public static boolean isJumping;
    public static boolean didJumpThisTick;
    public boolean wasFirstJump = true;
    public double jumpSpeedChanged = 0.0;
    public double speedometerCurrentSpeed = 0.0;
    public double percentJumpSpeedChanged = 0.0;

    static {
        didJumpThisTick = false;
        isJumping = false;
    }

    public float lastJumpInfoTimeRemaining() {
        return (float)(Minecraft.getSystemTime() - this.jumpInfoStartTime) / 1000.0f;
    }

    public void updateValues() {
        double d = SpeedManager.mc.player.posX - SpeedManager.mc.player.prevPosX;
        double d2 = SpeedManager.mc.player.posZ - SpeedManager.mc.player.prevPosZ;
        this.speedometerCurrentSpeed = d * d + d2 * d2;
        if (didJumpThisTick && (!SpeedManager.mc.player.onGround || isJumping)) {
            if (didJumpThisTick && !this.didJumpLastTick) {
                boolean bl = this.lastJumpSpeed == 0.0 || (this.wasFirstJump = false);
                this.percentJumpSpeedChanged = this.speedometerCurrentSpeed != 0.0 ? this.speedometerCurrentSpeed / this.lastJumpSpeed - 1.0 : -1.0;
                this.jumpSpeedChanged = this.speedometerCurrentSpeed - this.lastJumpSpeed;
                this.jumpInfoStartTime = Minecraft.getSystemTime();
                this.lastJumpSpeed = this.speedometerCurrentSpeed;
                this.firstJumpSpeed = this.wasFirstJump ? this.lastJumpSpeed : 0.0;
            }
            this.didJumpLastTick = didJumpThisTick;
        } else {
            this.didJumpLastTick = false;
            this.lastJumpSpeed = 0.0;
        }
        this.updatePlayers();
    }

    public void updatePlayers() {
        for (EntityPlayer entityPlayer : SpeedManager.mc.world.playerEntities) {
            int n = 20;
            if (!(SpeedManager.mc.player.getDistanceSq(entityPlayer) < (double)(n * n))) {
                continue;
            }
            double d = entityPlayer.posX - entityPlayer.prevPosX;
            double d2 = entityPlayer.posZ - entityPlayer.prevPosZ;
            double d3 = d * d + d2 * d2;
            this.playerSpeeds.put(entityPlayer, d3);
        }
    }

    public double getSpeedMpS() {
        double d = this.turnIntoKpH(this.speedometerCurrentSpeed) / 3.6;
        d = (double)Math.round(10.0 * d) / 10.0;
        return d;
    }

    public static void setIsJumping(boolean bl) {
        isJumping = bl;
    }

    public double getSpeedKpH() {
        double d = this.turnIntoKpH(this.speedometerCurrentSpeed);
        d = (double)Math.round(10.0 * d) / 10.0;
        return d;
    }

    public double getPlayerSpeed(EntityPlayer entityPlayer) {
        if (this.playerSpeeds.get(entityPlayer) == null) {
            return 0.0;
        }
        return this.turnIntoKpH(this.playerSpeeds.get(entityPlayer));
    }

    public double turnIntoKpH(double d) {
        return (double)MathHelper.sqrt(d) * 71.2729367892;
    }

    public static void setDidJumpThisTick(boolean bl) {
        didJumpThisTick = bl;
    }
}

