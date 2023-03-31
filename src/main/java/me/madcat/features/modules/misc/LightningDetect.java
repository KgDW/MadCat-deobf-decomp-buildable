package me.madcat.features.modules.misc;

import me.madcat.event.events.PacketEvent;
import me.madcat.features.Feature;
import me.madcat.features.command.Command;
import me.madcat.features.modules.Module;
import net.minecraft.network.play.server.SPacketSpawnGlobalEntity;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class LightningDetect
extends Module {
    public LightningDetect() {
        super("LightningDetect", "EZ", Module.Category.MISC);
    }

    @SubscribeEvent
    public void PacketEvent(PacketEvent.Receive receive) {
        if (Feature.fullNullCheck()) {
            return;
        }
        if (receive.packet instanceof SPacketSpawnGlobalEntity) {
            String string = "Lightning Detected! X:" + (int)((SPacketSpawnGlobalEntity)receive.packet).getX() + " Y:" + (int)((SPacketSpawnGlobalEntity)receive.packet).getY() + " Z:" + (int)((SPacketSpawnGlobalEntity)receive.packet).getZ();
            Command.sendMessage(string);
        }
    }
}

