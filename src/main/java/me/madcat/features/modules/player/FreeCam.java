package me.madcat.features.modules.player;

import me.madcat.event.events.PushEvent;
import net.minecraft.network.play.client.CPacketConfirmTeleport;
import net.minecraft.network.play.server.SPacketPlayerPosLook;
import net.minecraft.network.play.server.SPacketSetPassengers;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.network.play.client.CPacketKeepAlive;
import net.minecraft.network.play.client.CPacketChatMessage;
import net.minecraft.network.play.client.CPacketVehicleMove;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.network.play.client.CPacketPlayer;
import me.madcat.event.events.PacketEvent;
import net.minecraft.client.entity.EntityPlayerSP;
import me.madcat.util.MathUtil;
import me.madcat.features.Feature;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.util.math.AxisAlignedBB;
import me.madcat.features.setting.Setting;
import me.madcat.features.modules.Module;

public class FreeCam extends Module
{
    private static FreeCam INSTANCE;
    public final Setting<Double> speed;
    public final Setting<Boolean> view;
    public final Setting<Boolean> packet;
    public final Setting<Boolean> disable;
    public final Setting<Boolean> legit;
    private AxisAlignedBB oldBoundingBox;
    private EntityOtherPlayerMP entity;
    private Vec3d position;
    private Entity riding;
    private float yaw;
    private float pitch;

    public FreeCam() {
        super("FreeCam", "Look around freely", Category.PLAYER);
        this.speed = this.register(new Setting("speed", 0.5, 0.1, 5.0));
        this.view = this.register(new Setting("3d", false));
        this.packet = this.register(new Setting("packet", true));
        this.disable = this.register(new Setting("logout/off", true));
        this.legit = this.register(new Setting("legit", false));
        this.setInstance();
    }

    public static FreeCam INSTANCE() {
        if (FreeCam.INSTANCE == null) {
            FreeCam.INSTANCE = new FreeCam();
        }
        return FreeCam.INSTANCE;
    }

    private void setInstance() {
        FreeCam.INSTANCE = this;
    }

    @Override
    public void onEnable() {
        if (!Feature.fullNullCheck()) {
            this.oldBoundingBox = FreeCam.mc.player.getEntityBoundingBox();
            FreeCam.mc.player.setEntityBoundingBox(new AxisAlignedBB(FreeCam.mc.player.posX, FreeCam.mc.player.posY, FreeCam.mc.player.posZ, FreeCam.mc.player.posX, FreeCam.mc.player.posY, FreeCam.mc.player.posZ));
            if (FreeCam.mc.player.getRidingEntity() != null) {
                this.riding = FreeCam.mc.player.getRidingEntity();
                FreeCam.mc.player.dismountRidingEntity();
            }
            (this.entity = new EntityOtherPlayerMP(FreeCam.mc.world, FreeCam.mc.getSession().getProfile())).copyLocationAndAnglesFrom(FreeCam.mc.player);
            this.entity.rotationYaw = FreeCam.mc.player.rotationYaw;
            this.entity.rotationYawHead = FreeCam.mc.player.rotationYawHead;
            this.entity.inventory.copyInventory(FreeCam.mc.player.inventory);
            FreeCam.mc.world.addEntityToWorld(69420, this.entity);
            this.position = FreeCam.mc.player.getPositionVector();
            this.yaw = FreeCam.mc.player.rotationYaw;
            this.pitch = FreeCam.mc.player.rotationPitch;
            FreeCam.mc.player.noClip = true;
        }
    }

    @Override
    public void onDisable() {
        if (!Feature.fullNullCheck()) {
            FreeCam.mc.player.setEntityBoundingBox(this.oldBoundingBox);
            if (this.riding != null) {
                FreeCam.mc.player.startRiding(this.riding, true);
            }
            if (this.entity != null) {
                FreeCam.mc.world.removeEntity(this.entity);
            }
            if (this.position != null) {
                FreeCam.mc.player.setPosition(this.position.x, this.position.y, this.position.z);
            }
            FreeCam.mc.player.rotationYaw = this.yaw;
            FreeCam.mc.player.rotationPitch = this.pitch;
            FreeCam.mc.player.noClip = false;
        }
    }

    @Override
    public void onUpdate() {
        FreeCam.mc.player.noClip = true;
        FreeCam.mc.player.setVelocity(0.0, 0.0, 0.0);
        FreeCam.mc.player.jumpMovementFactor = this.speed.getValue().floatValue();
        final double[] directionSpeed = MathUtil.directionSpeed(this.speed.getValue());
        if (FreeCam.mc.player.movementInput.moveStrafe != 0.0f || FreeCam.mc.player.movementInput.moveForward != 0.0f) {
            FreeCam.mc.player.motionX = directionSpeed[0];
            FreeCam.mc.player.motionZ = directionSpeed[1];
        }
        else {
            FreeCam.mc.player.motionX = 0.0;
            FreeCam.mc.player.motionZ = 0.0;
        }
        FreeCam.mc.player.setSprinting(false);
        if (this.view.getValue() && !FreeCam.mc.gameSettings.keyBindSneak.isKeyDown() && !FreeCam.mc.gameSettings.keyBindJump.isKeyDown()) {
            FreeCam.mc.player.motionY = this.speed.getValue() * -MathUtil.degToRad(FreeCam.mc.player.rotationPitch) * FreeCam.mc.player.movementInput.moveForward;
        }
        if (FreeCam.mc.gameSettings.keyBindJump.isKeyDown()) {
            final EntityPlayerSP player = FreeCam.mc.player;
            player.motionY += this.speed.getValue();
        }
        if (FreeCam.mc.gameSettings.keyBindSneak.isKeyDown()) {
            final EntityPlayerSP player2 = FreeCam.mc.player;
            player2.motionY -= this.speed.getValue();
        }
    }

    @Override
    public void onLogout() {
        if (this.disable.getValue() && this.isEnabled()) {
            this.disable();
        }
    }

    @SubscribeEvent
    public void onPacketSend(final PacketEvent.Send send) {
        if (Feature.fullNullCheck()) {
            return;
        }
        if (this.legit.getValue() && this.entity != null && send.getPacket() instanceof CPacketPlayer) {
            final CPacketPlayer cPacketPlayer = send.getPacket();
            return;
        }
        if (this.packet.getValue()) {
            if (send.getPacket() instanceof CPacketPlayer) {
                send.setCanceled(true);
            }
        }
        else if (!(send.getPacket() instanceof CPacketUseEntity) && !(send.getPacket() instanceof CPacketPlayerTryUseItem) && !(send.getPacket() instanceof CPacketPlayerTryUseItemOnBlock) && !(send.getPacket() instanceof CPacketPlayer) && !(send.getPacket() instanceof CPacketVehicleMove) && !(send.getPacket() instanceof CPacketChatMessage) && !(send.getPacket() instanceof CPacketKeepAlive)) {
            send.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void onPacketReceive(final PacketEvent.Receive receive) {
        if (Feature.fullNullCheck()) {
            return;
        }
        if (receive.getPacket() instanceof SPacketSetPassengers) {
            final Entity entityByID = FreeCam.mc.world.getEntityByID(((SPacketSetPassengers) receive.getPacket()).getEntityId());
            if (entityByID != null && entityByID == this.riding) {
                this.riding = null;
            }
        }
        if (receive.getPacket() instanceof SPacketPlayerPosLook) {
            final SPacketPlayerPosLook sPacketPlayerPosLook = receive.getPacket();
            if (this.packet.getValue()) {
                if (this.entity != null) {
                    this.entity.setPositionAndRotation(sPacketPlayerPosLook.getX(), sPacketPlayerPosLook.getY(), sPacketPlayerPosLook.getZ(), sPacketPlayerPosLook.getYaw(), sPacketPlayerPosLook.getPitch());
                }
                this.position = new Vec3d(sPacketPlayerPosLook.getX(), sPacketPlayerPosLook.getY(), sPacketPlayerPosLook.getZ());
                FreeCam.mc.player.connection.sendPacket(new CPacketConfirmTeleport(sPacketPlayerPosLook.getTeleportId()));
                receive.setCanceled(true);
            }
            else {
                receive.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public void onPush(final PushEvent pushEvent) {
        if (pushEvent.getStage() == 1) {
            pushEvent.setCanceled(true);
        }
    }

    static {
        FreeCam.INSTANCE = new FreeCam();
    }
}
 