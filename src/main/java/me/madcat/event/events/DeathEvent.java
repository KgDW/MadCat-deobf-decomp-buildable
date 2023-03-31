package me.madcat.event.events;

import me.madcat.event.EventStage;
import net.minecraft.entity.player.EntityPlayer;

public class DeathEvent
extends EventStage {
    public final EntityPlayer player;

    public DeathEvent(EntityPlayer entityPlayer) {
        this.player = entityPlayer;
    }
}

