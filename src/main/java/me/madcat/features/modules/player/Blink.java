package me.madcat.features.modules.player;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import me.madcat.event.events.PacketEvent;
import me.madcat.features.Feature;
import me.madcat.features.modules.Module;
import me.madcat.features.setting.Setting;
import me.madcat.util.MathUtil;
import me.madcat.util.Timer;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketChatMessage;
import net.minecraft.network.play.client.CPacketClientStatus;
import net.minecraft.network.play.client.CPacketConfirmTeleport;
import net.minecraft.network.play.client.CPacketKeepAlive;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketTabComplete;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class Blink
extends Module {
    private static Blink INSTANCE = new Blink();
    private final Setting<Boolean> cPacketPlayer = this.register(new Setting<>("CPacketPlayer", true));
    private final Setting<Mode> autoOff = this.register(new Setting<>("AutoOff", Mode.MANUAL));
    private final Setting<Integer> timeLimit = this.register(new Setting<Object>("Time", 20, 1, 500, this::new0));
    private final Setting<Integer> packetLimit = this.register(new Setting<Object>("Packets", 20, 1, 500, this::new1));
    private final Setting<Float> distance = this.register(new Setting<Object>("Distance", 10.0f, 1.0f, 100.0f, this::new2));
    private final Timer timer = new Timer();
    private final Queue<Packet<?>> packets = new ConcurrentLinkedQueue();
    private EntityOtherPlayerMP entity;
    private int packetsCanceled = 0;
    private BlockPos startPos = null;

    public Blink() {
        super("Blink", "Fake lag", Module.Category.PLAYER);
        this.setInstance();
    }

    public static Blink INSTANCE() {
        if (INSTANCE == null) {
            INSTANCE = new Blink();
        }
        return INSTANCE;
    }

    private void setInstance() {
        INSTANCE = this;
    }

    @Override
    public void onEnable() {
        this.entity = new EntityOtherPlayerMP(Blink.mc.world, Blink.mc.getSession().getProfile());
        this.entity.copyLocationAndAnglesFrom(Blink.mc.player);
        this.entity.rotationYaw = Blink.mc.player.rotationYaw;
        this.entity.rotationYawHead = Blink.mc.player.rotationYawHead;
        this.entity.inventory.copyInventory(Blink.mc.player.inventory);
        Blink.mc.world.addEntityToWorld(6942069, this.entity);
        this.startPos = Blink.mc.player.getPosition();
        this.packetsCanceled = 0;
        this.timer.reset();
    }

    @Override
    public void onUpdate() {
        if (Feature.fullNullCheck()) {
            return;
        }
        if (this.autoOff.getValue() == Mode.TIME && this.timer.passedS(this.timeLimit.getValue()) || this.autoOff.getValue() == Mode.DISTANCE && this.startPos != null && Blink.mc.player.getDistanceSq(this.startPos) >= MathUtil.square(this.distance.getValue()) || this.autoOff.getValue() == Mode.PACKETS && this.packetsCanceled >= this.packetLimit.getValue()) {
            this.disable();
        }
    }

    @Override
    public void onLogout() {
        if (this.isOn()) {
            this.disable();
        }
    }

    @SubscribeEvent
    public void onSendPacket(PacketEvent.Send send) {
        if (Feature.fullNullCheck()) {
            return;
        }
        if (Blink.mc.world != null && !mc.isSingleplayer()) {
            Packet<?> t = send.getPacket();
            if (this.cPacketPlayer.getValue() && t instanceof CPacketPlayer) {
                send.setCanceled(true);
                this.packets.add(t);
                ++this.packetsCanceled;
            }
            if (!this.cPacketPlayer.getValue()) {
                if (t instanceof CPacketChatMessage || t instanceof CPacketConfirmTeleport || t instanceof CPacketKeepAlive || t instanceof CPacketTabComplete || t instanceof CPacketClientStatus) {
                    return;
                }
                this.packets.add(t);
                send.setCanceled(true);
                ++this.packetsCanceled;
            }
        }
    }

    @Override
    public void onDisable() {
        Blink.mc.world.removeEntity(this.entity);
        while (!this.packets.isEmpty()) {
            Blink.mc.player.connection.sendPacket(this.packets.poll());
        }
        this.startPos = null;
    }

    @Override
    public String getDisplayInfo() {
        if (this.packets != null) {
            return this.packets.size() + "";
        }
        return null;
    }

    private boolean new2(Object object) {
        return this.autoOff.getValue() == Mode.DISTANCE;
    }

    private boolean new1(Object object) {
        return this.autoOff.getValue() == Mode.PACKETS;
    }

    private boolean new0(Object object) {
        return this.autoOff.getValue() == Mode.TIME;
    }

    public enum Mode {
        MANUAL,
        TIME,
        DISTANCE,
        PACKETS

    }
}

