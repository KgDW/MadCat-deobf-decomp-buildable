package me.madcat.event.events;

import me.madcat.event.EventStage;
import net.minecraft.entity.player.EntityPlayer;

public class TotemPopEvent
extends EventStage {
    private final EntityPlayer entity;

    public TotemPopEvent(EntityPlayer entityPlayer) {
        this.entity = entityPlayer;
    }

    public EntityPlayer getEntity() {
        return this.entity;
    }
}

