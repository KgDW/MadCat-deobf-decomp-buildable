package me.madcat.event.events;

import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

@Cancelable
public class Packet
extends Event {
    private Object packet;
    private Type type;

    public Packet(Object object, Type type) {
        this.packet = object;
        this.type = type;
    }

    public Object getPacket() {
        return this.packet;
    }

    public void setPacket(Object object) {
        this.packet = object;
    }

    public Type getType() {
        return this.type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public enum Type {
        INCOMING,
        OUTGOING

    }
}

