package me.madcat.features.modules.movement;

import net.minecraft.init.Blocks;
import java.text.DecimalFormat;

import net.minecraft.network.play.client.CPacketPlayer;
import me.madcat.features.modules.exploit.Clip;
import net.minecraft.block.Block;
import me.madcat.features.modules.exploit.Phase;
import me.madcat.features.setting.Setting;
import me.madcat.features.modules.Module;

public class Step extends Module
{
    private static Step instance;
    final Setting<Double> height;
    final Setting<Mode> mode;
    final Setting<Boolean> Burrow;
    final Setting<Boolean> Sneak;

    public Step() {
        super("Step", "step", Category.MOVEMENT);
        this.height = this.register(new Setting("Height", 2.5, 0.5, 3.5));
        this.mode = this.register(new Setting("Mode", Mode.Vanilla));
        this.Burrow = this.register(new Setting("PauseBurrow", true));
        this.Sneak = this.register(new Setting("PauseSneak", true));
        Step.instance = this;
    }

    public static Step INSTANCE() {
        if (Step.instance == null) {
            Step.instance = new Step();
        }
        return Step.instance;
    }

    public static double[] forward(final double n) {
        float moveForward = Step.mc.player.movementInput.moveForward;
        float moveStrafe = Step.mc.player.movementInput.moveStrafe;
        float n2 = Step.mc.player.prevRotationYaw + (Step.mc.player.rotationYaw - Step.mc.player.prevRotationYaw) * Step.mc.getRenderPartialTicks();
        if (moveForward != 0.0f) {
            if (moveStrafe > 0.0f) {
                n2 += ((moveForward > 0.0f) ? -45 : 45);
            }
            else if (moveStrafe < 0.0f) {
                n2 += ((moveForward > 0.0f) ? 45 : -45);
            }
            moveStrafe = 0.0f;
            if (moveForward > 0.0f) {
                moveForward = 1.0f;
            }
            else if (moveForward < 0.0f) {
                moveForward = -1.0f;
            }
        }
        final double sin = Math.sin(Math.toRadians(n2 + 90.0f));
        final double cos = Math.cos(Math.toRadians(n2 + 90.0f));
        return new double[] { moveForward * n * cos + moveStrafe * n * sin, moveForward * n * sin - moveStrafe * n * cos };
    }

    @Override
    public void onToggle() {
        Step.mc.player.stepHeight = 0.6f;
    }

    @Override
    public void onUpdate() {
        if (fullNullCheck()) {
            return;
        }
        if ((Phase.collideBlockIntersects(Step.mc.player.getEntityBoundingBox(), Step::onUpdate) || Phase.collideBlockIntersects(Step.mc.player.getEntityBoundingBox(), Step::onUpdate1)) && this.Burrow.getValue()) {
            Step.mc.player.stepHeight = 0.5f;
            return;
        }
        if (Step.mc.player.isSneaking() && this.Sneak.getValue()) {
            Step.mc.player.stepHeight = 0.5f;
            return;
        }
        if (Clip.INSTANCE.isEnabled()) {
            Step.mc.player.stepHeight = 0.5f;
            return;
        }
        if (Step.mc.player.isInWater() || Step.mc.player.isInLava() || Step.mc.player.isOnLadder() || Step.mc.gameSettings.keyBindJump.isKeyDown()) {
            Step.mc.player.stepHeight = 0.5f;
            return;
        }
        if (this.mode.getValue() == Mode.Normal) {
            Step.mc.player.stepHeight = 0.5f;
            final double[] forward = forward(0.1);
            boolean b = false;
            boolean b2 = false;
            boolean b3 = false;
            boolean b4 = false;
            if (Step.mc.world.getCollisionBoxes(Step.mc.player, Step.mc.player.getEntityBoundingBox().offset(forward[0], 2.6, forward[1])).isEmpty() && !Step.mc.world.getCollisionBoxes(Step.mc.player, Step.mc.player.getEntityBoundingBox().offset(forward[0], 2.4, forward[1])).isEmpty()) {
                b = true;
            }
            if (Step.mc.world.getCollisionBoxes(Step.mc.player, Step.mc.player.getEntityBoundingBox().offset(forward[0], 2.1, forward[1])).isEmpty() && !Step.mc.world.getCollisionBoxes(Step.mc.player, Step.mc.player.getEntityBoundingBox().offset(forward[0], 1.9, forward[1])).isEmpty()) {
                b2 = true;
            }
            if (Step.mc.world.getCollisionBoxes(Step.mc.player, Step.mc.player.getEntityBoundingBox().offset(forward[0], 1.6, forward[1])).isEmpty() && !Step.mc.world.getCollisionBoxes(Step.mc.player, Step.mc.player.getEntityBoundingBox().offset(forward[0], 1.4, forward[1])).isEmpty()) {
                b3 = true;
            }
            if (Step.mc.world.getCollisionBoxes(Step.mc.player, Step.mc.player.getEntityBoundingBox().offset(forward[0], 1.0, forward[1])).isEmpty() && !Step.mc.world.getCollisionBoxes(Step.mc.player, Step.mc.player.getEntityBoundingBox().offset(forward[0], 0.6, forward[1])).isEmpty()) {
                b4 = true;
            }
            if (Step.mc.player.collidedHorizontally && (Step.mc.player.moveForward != 0.0f || Step.mc.player.moveStrafing != 0.0f) && Step.mc.player.onGround) {
                if (b4 && this.height.getValue() >= 1.0) {
                    final double[] array = { 0.42, 0.753 };
                    for (double v : array) {
                        Step.mc.player.connection.sendPacket(new CPacketPlayer.Position(Step.mc.player.posX, Step.mc.player.posY + v, Step.mc.player.posZ, Step.mc.player.onGround));
                    }
                    Step.mc.player.setPosition(Step.mc.player.posX, Step.mc.player.posY + 1.0, Step.mc.player.posZ);
                }
                if (b3 && this.height.getValue() >= 1.5) {
                    final double[] array2 = { 0.42, 0.75, 1.0, 1.16, 1.23, 1.2 };
                    for (double v : array2) {
                        Step.mc.player.connection.sendPacket(new CPacketPlayer.Position(Step.mc.player.posX, Step.mc.player.posY + v, Step.mc.player.posZ, Step.mc.player.onGround));
                    }
                    Step.mc.player.setPosition(Step.mc.player.posX, Step.mc.player.posY + 1.5, Step.mc.player.posZ);
                }
                if (b2 && this.height.getValue() >= 2.0) {
                    final double[] array3 = { 0.42, 0.78, 0.63, 0.51, 0.9, 1.21, 1.45, 1.43 };
                    for (double v : array3) {
                        Step.mc.player.connection.sendPacket(new CPacketPlayer.Position(Step.mc.player.posX, Step.mc.player.posY + v, Step.mc.player.posZ, Step.mc.player.onGround));
                    }
                    Step.mc.player.setPosition(Step.mc.player.posX, Step.mc.player.posY + 2.0, Step.mc.player.posZ);
                }
                if (b && this.height.getValue() >= 2.5) {
                    final double[] array4 = { 0.425, 0.821, 0.699, 0.599, 1.022, 1.372, 1.652, 1.869, 2.019, 1.907 };
                    for (double v : array4) {
                        Step.mc.player.connection.sendPacket(new CPacketPlayer.Position(Step.mc.player.posX, Step.mc.player.posY + v, Step.mc.player.posZ, Step.mc.player.onGround));
                    }
                    Step.mc.player.setPosition(Step.mc.player.posX, Step.mc.player.posY + 2.5, Step.mc.player.posZ);
                }
            }
        }
        if (this.mode.getValue() == Mode.Vanilla) {
            Step.mc.player.stepHeight = Float.parseFloat(new DecimalFormat("#").format(this.height.getValue()));
        }
    }

    @Override
    public String getDisplayInfo() {
        return this.mode.currentEnumName();
    }

    @Override
    public void onDisable() {
        Step.mc.player.stepHeight = 0.5f;
    }

    private static Boolean onUpdate1(final Block block) {
        return block == Blocks.ENDER_CHEST;
    }

    private static Boolean onUpdate(final Block block) {
        return block == Blocks.OBSIDIAN;
    }

    public enum Mode
    {
        Vanilla,
        Normal;

        private static final Mode[] $VALUES;

        static {
            $VALUES = new Mode[] { Mode.Vanilla, Mode.Normal };
        }
    }
}
 