package me.madcat.features.modules.client;

import com.mojang.realmsclient.gui.ChatFormatting;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;
import me.madcat.event.events.PacketEvent;
import me.madcat.features.modules.Module;
import me.madcat.features.setting.Setting;
import net.minecraft.network.play.client.CPacketChatMessage;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class BetterChat
extends Module {
    public static BetterChat INSTANCE;
    public final Setting<Boolean> rect = this.register(new Setting<>("Rect", true));
    public final Setting<Boolean> colorRect = this.register(new Setting<>("ColorRect", Boolean.FALSE, this::new0));
    public final Setting<Boolean> infinite = this.register(new Setting<>("InfiniteChat", false));
    public final Setting<Boolean> suffix = this.register(new Setting<>("Suffix", false));
    public final Setting<String> clientName = this.register(new Setting<>("ClientName", "MadCat 3.0", this::new1));
    public final Setting<Boolean> time = this.register(new Setting<>("TimeStamps", false));
    public final Setting<Bracket> bracket = this.register(new Setting<>("Bracket", Bracket.TRIANGLE, this::new2));

    public BetterChat() {
        super("BetterChat", "Modifies your chat", Module.Category.CLIENT);
        INSTANCE = this;
    }

    @SubscribeEvent
    public void onPacketSend(PacketEvent.Send send) {
        if (BetterChat.fullNullCheck()) {
            return;
        }
        if (this.suffix.getValue() && send.getPacket() instanceof CPacketChatMessage) {
            CPacketChatMessage cPacketChatMessage = send.getPacket();
            String message = cPacketChatMessage.getMessage();
            if (message.startsWith("/") || message.endsWith(this.clientName.getValue()) || message.startsWith("!")) {
                return;
            }
            String newMessage = message + " " + this.clientName.getValue();
            if (newMessage.length() >= 256) {
                newMessage = newMessage.substring(0, 256);
            }
            try {
                Field packetField = PacketEvent.class.getDeclaredField("packet");
                packetField.setAccessible(true);
                packetField.set(send, new CPacketChatMessage(newMessage));
            } catch (NoSuchFieldException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }



    @SubscribeEvent
    public void onClientChatReceived(ClientChatReceivedEvent clientChatReceivedEvent) {
        if (this.time.getValue()) {
            Date date = new Date();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
            String string = simpleDateFormat.format(date);
            String string2 = this.bracket.getValue() == Bracket.TRIANGLE ? "<" : "[";
            String string3 = this.bracket.getValue() == Bracket.TRIANGLE ? ">" : "]";
            TextComponentString textComponentString = new TextComponentString(ChatFormatting.GRAY + string2 + ChatFormatting.WHITE + string + ChatFormatting.GRAY + string3 + ChatFormatting.RESET + " ");
            clientChatReceivedEvent.setMessage(textComponentString.appendSibling(clientChatReceivedEvent.getMessage()));
        }
    }

    private boolean new2(Bracket bracket) {
        return this.time.getValue();
    }

    private boolean new1(String string) {
        return this.suffix.getValue();
    }

    private boolean new0(Boolean bl) {
        return this.rect.getValue();
    }

    private enum Bracket {
        SQUARE,
        TRIANGLE

    }
}

