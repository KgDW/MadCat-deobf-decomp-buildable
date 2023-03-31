package me.madcat.features.modules.player;

import me.madcat.event.events.PacketEvent;
import me.madcat.features.Feature;
import me.madcat.features.modules.Module;
import net.minecraft.network.play.server.SPacketPlayerPosLook;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class NoRotate
extends Module {
    public NoRotate() {
        super("NoRotate", "Dangerous to use might desync you", Module.Category.PLAYER);
    }

    @SubscribeEvent
    public void onPacketReceive(PacketEvent.Receive receive) {
        if (Feature.fullNullCheck()) {
            return;
        }
        if (receive.getStage() == 0 && receive.getPacket() instanceof SPacketPlayerPosLook) {
            SPacketPlayerPosLook sPacketPlayerPosLook = receive.getPacket();
        }
    }
}

