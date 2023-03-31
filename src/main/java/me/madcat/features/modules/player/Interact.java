package me.madcat.features.modules.player;

import me.madcat.event.events.PacketEvent;
import me.madcat.features.Feature;
import me.madcat.features.modules.Module;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class Interact
extends Module {
    private static Interact INSTANCE = new Interact();

    public Interact() {
        super("Interact", "Allows you to place at build height", Module.Category.PLAYER);
        this.setInstance();
    }

    public static Interact INSTANCE() {
        if (INSTANCE == null) {
            INSTANCE = new Interact();
        }
        return INSTANCE;
    }

    private void setInstance() {
        INSTANCE = this;
    }

    @SubscribeEvent
    public void onPacketSend(PacketEvent.Send send) {
        CPacketPlayerTryUseItemOnBlock cPacketPlayerTryUseItemOnBlock;
        if (Feature.fullNullCheck()) {
            return;
        }
        if (send.getStage() == 0 && send.getPacket() instanceof CPacketPlayerTryUseItemOnBlock && (cPacketPlayerTryUseItemOnBlock = send.getPacket()).getPos().getY() >= 255) {
            cPacketPlayerTryUseItemOnBlock.getDirection();
        }
    }
}

