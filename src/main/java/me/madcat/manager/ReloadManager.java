package me.madcat.manager;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.madcat.MadCat;
import me.madcat.event.events.PacketEvent;
import me.madcat.features.Feature;
import me.madcat.features.command.Command;
import net.minecraft.network.play.client.CPacketChatMessage;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ReloadManager
extends Feature {
    public String prefix;

    public void unload() {
        MinecraftForge.EVENT_BUS.unregister(this);
    }

    @SubscribeEvent
    public void onPacketSend(PacketEvent.Send send) {
        CPacketChatMessage cPacketChatMessage;
        if (ReloadManager.fullNullCheck()) {
            return;
        }
        if (send.getPacket() instanceof CPacketChatMessage && (cPacketChatMessage = send.getPacket()).getMessage().startsWith(this.prefix) && cPacketChatMessage.getMessage().contains("reload")) {
            MadCat.load();
            send.setCanceled(true);
        }
    }

    public void init(String string) {
        this.prefix = string;
        MinecraftForge.EVENT_BUS.register(this);
        if (!ReloadManager.fullNullCheck()) {
            Command.sendMessage(ChatFormatting.RED + "unloaded. Type " + string + "reload to reload.");
        }
    }
}

