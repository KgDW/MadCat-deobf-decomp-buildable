package me.madcat.features.modules.movement;

import me.madcat.event.events.PacketEvent;
import me.madcat.features.modules.Module;
import me.madcat.features.setting.Setting;
import me.madcat.util.MathUtil;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.event.InputUpdateEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class NoSlow
extends Module {
    public final Setting<Boolean> itemSlow = this.register(new Setting<>("Item", true));
    public final Setting<Boolean> shift = this.register(new Setting<>("Sneak", false));
    public final Setting<Boolean> web = this.register(new Setting<>("AntiWeb", false));
    private final Setting<Float> speed = this.register(new Setting<>("Speed", 10.0f, 1.0f, 10.0f, this::new0));
    private final Setting<Float> speed2 = this.register(new Setting<>("MotionY", 0.5f, 0.1f, 1.0f, this::new1));
    public final Setting<Boolean> strict = this.register(new Setting<>("Bypass", Boolean.FALSE, this::new2));

    public NoSlow() {
        super("NoSlow", "No item use slow down", Module.Category.MOVEMENT);
    }

    @SubscribeEvent
    public void Slow(InputUpdateEvent inputUpdateEvent) {
        if (!NoSlow.mc.player.isSneaking() && NoSlow.mc.player.isHandActive() && this.itemSlow.getValue() && !NoSlow.mc.player.isRiding()) {
            inputUpdateEvent.getMovementInput().moveStrafe *= 5.0f;
            inputUpdateEvent.getMovementInput().moveForward *= 5.0f;
        } else if (NoSlow.mc.player.isSneaking() && this.shift.getValue() && !NoSlow.mc.player.isRiding()) {
            inputUpdateEvent.getMovementInput().moveStrafe *= 5.0f;
            inputUpdateEvent.getMovementInput().moveForward *= 5.0f;
        }
    }

    @Override
    public void onUpdate() {
        if (this.web.getValue() && NoSlow.mc.player.isDead) {
            double[] dArray = MathUtil.directionSpeed((double) this.speed.getValue() / 10.0);
            NoSlow.mc.player.motionX = dArray[0];
            NoSlow.mc.player.motionZ = dArray[1];
            NoSlow.mc.player.motionY -= this.speed2.getValue();
        }
    }

    @SubscribeEvent
    public void onPacket(PacketEvent.Send send) {
        if (NoSlow.fullNullCheck()) {
            return;
        }
        if (send.getPacket() instanceof CPacketPlayer && this.strict.getValue() && this.itemSlow.getValue() && NoSlow.mc.player.isHandActive() && !NoSlow.mc.player.isRiding()) {
            NoSlow.mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.ABORT_DESTROY_BLOCK, new BlockPos(Math.floor(NoSlow.mc.player.posX), Math.floor(NoSlow.mc.player.posY), Math.floor(NoSlow.mc.player.posZ)), EnumFacing.DOWN));
        }
    }

    private boolean new2(Boolean bl) {
        return this.itemSlow.getValue();
    }

    private boolean new1(Float f) {
        return this.web.getValue();
    }

    private boolean new0(Float f) {
        return this.web.getValue();
    }
}

