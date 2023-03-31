package me.madcat.features.modules.player;

import me.madcat.event.events.PacketEvent;
import me.madcat.event.events.UpdateWalkingPlayerEventTwo;
import me.madcat.features.Feature;
import me.madcat.features.modules.Module;
import me.madcat.features.modules.player.PacketXP;
import me.madcat.features.setting.Setting;
import me.madcat.util.Timer;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class TimerModule
extends Module {
    public final Setting<Boolean> packetControl;
    private final Setting<Float> packetLimit;
    private float packet = 0.0f;
    private static TimerModule INSTANCE = new TimerModule();
    private float lastYaw;
    private int normalPos;
    private final Timer packetListReset;
    private final Setting<Float> tickNormal = this.register(new Setting<>("Speed", 1.2f, 0.1f, 10.0f));
    private int antiLagTimed;
    private float lastPitch;
    private int normalLookPos;
    private int rotationMode;
    private final Setting<Float> antiLagTime;

    @SubscribeEvent
    public final void onPacketSend(PacketEvent.Send send) {
        if (Feature.fullNullCheck()) {
            return;
        }
        if (send.getPacket() instanceof CPacketPlayer.Position && this.rotationMode == 1) {
            ++this.normalPos;
            if (this.normalPos > 20) {
                this.rotationMode = 2;
            }
        } else if (send.getPacket() instanceof CPacketPlayer.PositionRotation && this.rotationMode == 2) {
            ++this.normalLookPos;
            if (this.normalLookPos > 20) {
                this.rotationMode = 1;
            }
        } else if (!(send.getPacket() instanceof CPacketPlayer)) {
            this.packet += 1.0f;
        }
    }

    @SubscribeEvent
    public final void onUpdate(UpdateWalkingPlayerEventTwo updateWalkingPlayerEventTwo) {
        if (this.packetControl.getValue()) {
            switch (this.rotationMode) {
                case 1: {
                    updateWalkingPlayerEventTwo.setRotation(this.lastYaw, this.lastPitch);
                    break;
                }
                case 2: {
                    updateWalkingPlayerEventTwo.setRotation(this.lastYaw + TimerModule.nextFloat(1.0f, 3.0f), this.lastPitch + TimerModule.nextFloat(1.0f, 3.0f));
                }
            }
        }
    }

    private boolean new1(Float f) {
        return this.packetControl.getValue();
    }

    @Override
    public void onEnable() {
        this.packetListReset.reset();
        this.lastYaw = TimerModule.mc.player.rotationYaw;
        this.lastPitch = TimerModule.mc.player.rotationPitch;
    }

    public TimerModule() {
        super("Timer", "Timer", Module.Category.PLAYER);
        this.packetControl = this.register(new Setting<>("PacketControl", true));
        this.packetLimit = this.register(new Setting<>("PacketLimit", 200.0f, 10.0f, 1000.0f, this::new0));
        this.antiLagTime = this.register(new Setting<>("AntiLagTime", 30.0f, 10.0f, 100.0f, this::new1));
        Setting<Float> tickNormal2 = this.register(new Setting<>("AntiLagSpeed", 1.2f, 0.5f, 2.0f, this::new2));
        this.packetListReset = new Timer();
        this.setInstance();
    }

    public static TimerModule INSTANCE() {
        if (INSTANCE == null) {
            INSTANCE = new TimerModule();
        }
        return INSTANCE;
    }

    @Override
    public void onUpdate() {
        if (Feature.fullNullCheck()) {
            return;
        }
        if (this.packetListReset.passedMs(1000L)) {
            this.packet = 0.0f;
            this.normalPos = 0;
            this.normalLookPos = 0;
            this.rotationMode = 1;
            this.lastYaw = TimerModule.mc.player.rotationYaw;
            this.lastPitch = TimerModule.mc.player.rotationPitch;
            this.packetListReset.reset();
        }
        if (this.lastPitch > 85.0f) {
            this.lastPitch = 85.0f;
        }
        if (PacketXP.INSTANCE().isEnabled() && PacketXP.INSTANCE().bind.getValue().isDown() && PacketXP.INSTANCE().pitch.getValue()) {
            this.lastPitch = 85.0f;
        }
        boolean antiLag = false;
        if (this.packet > this.packetLimit.getValue()) {
            this.antiLagTimed = 0;
        }
        ++this.antiLagTimed;
        if ((float) this.antiLagTimed > this.antiLagTime.getValue()) {
        }
    }

    public static float nextFloat(float f, float f2) {
        return f == f2 || f2 - f <= 0.0f ? f : (float)((double)f + (double)(f2 - f) * Math.random());
    }

    @Override
    public void onDisable() {
        this.packetListReset.reset();
    }

    private boolean new2(Float f) {
        return this.packetControl.getValue();
    }

    @Override
    public String getDisplayInfo() {
        return TextFormatting.RED + "" + this.packet + "";
    }

    private boolean new0(Float f) {
        return this.packetControl.getValue();
    }

    private void setInstance() {
        INSTANCE = this;
    }
}

