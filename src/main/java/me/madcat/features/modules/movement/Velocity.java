package me.madcat.features.modules.movement;

import me.madcat.event.events.PacketEvent;
import me.madcat.event.events.PushEvent;
import me.madcat.features.modules.Module;
import me.madcat.features.setting.Setting;
import net.minecraft.network.play.server.SPacketEntityVelocity;
import net.minecraft.network.play.server.SPacketExplosion;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class Velocity
extends Module {
    public final Setting<Boolean> antiKnockBack = this.register(new Setting<>("KnockBack", true));
    public final Setting<Boolean> noEntityPush = this.register(new Setting<>("No PlayerPush", true));
    public final Setting<Boolean> noBlockPush = this.register(new Setting<>("No BlockPush", true));
    public final Setting<Boolean> noWaterPush = this.register(new Setting<>("No LiquidPush", true));

    public Velocity() {
        super("Velocity", "Anti push and knock back", Module.Category.MOVEMENT);
    }

    @SubscribeEvent
    public void onPacketReceived(PacketEvent.Receive receive) {
        if (this.antiKnockBack.getValue()) {
            if (receive.getPacket() instanceof SPacketEntityVelocity && ((SPacketEntityVelocity)receive.getPacket()).getEntityID() == Velocity.mc.player.getEntityId()) {
                receive.setCanceled(true);
            }
            if (receive.getPacket() instanceof SPacketExplosion) {
                receive.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public void onPush(PushEvent pushEvent) {
        if (pushEvent.getStage() == 0 && this.noEntityPush.getValue() && pushEvent.entity.equals(Velocity.mc.player)) {
            pushEvent.x = -pushEvent.x * 0.0;
            pushEvent.y = -pushEvent.y * 0.0;
            pushEvent.z = -pushEvent.z * 0.0;
        } else if (pushEvent.getStage() == 1 && this.noBlockPush.getValue()) {
            pushEvent.setCanceled(true);
        } else if (pushEvent.getStage() == 2 && this.noWaterPush.getValue() && Velocity.mc.player != null && Velocity.mc.player.equals(pushEvent.entity)) {
            pushEvent.setCanceled(true);
        }
    }
}

