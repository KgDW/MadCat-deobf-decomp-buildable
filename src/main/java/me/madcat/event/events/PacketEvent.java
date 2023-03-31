package me.madcat.event.events;

import me.madcat.event.EventStage;
import net.minecraft.network.Packet;
import net.minecraftforge.fml.common.eventhandler.Cancelable;

public class PacketEvent
extends EventStage {
    public final Packet<?> packet;

    public PacketEvent(Packet<?> packet) {
        this.packet = packet;
    }

    public <T extends Packet<?>> T getPacket() {
        return (T)this.packet;
    }

    @Cancelable
    public static class Send
    extends PacketEvent {
        public Send(Packet<?> packet) {
            super(packet);
        }
    }

    @Cancelable
    public static class Receive
    extends PacketEvent {
        public Receive(Packet<?> packet) {
            super(packet);
        }
    }
}

