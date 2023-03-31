package me.madcat.event.events;

import java.util.UUID;
import me.madcat.event.EventStage;
import net.minecraft.entity.player.EntityPlayer;

public class ConnectionEvent
extends EventStage {
    private final UUID uuid;
    private final EntityPlayer entity;
    private final String name;

    public ConnectionEvent(int n, UUID uUID, String string) {
        super(n);
        this.uuid = uUID;
        this.name = string;
        this.entity = null;
    }

    public ConnectionEvent(int n, EntityPlayer entityPlayer, UUID uUID, String string) {
        super(n);
        this.entity = entityPlayer;
        this.uuid = uUID;
        this.name = string;
    }

    public String getName() {
        return this.name;
    }

    public UUID getUuid() {
        return this.uuid;
    }

    public EntityPlayer getEntity() {
        return this.entity;
    }
}

