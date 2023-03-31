package me.madcat.event.events;

import me.madcat.event.EventStage;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.common.eventhandler.Cancelable;

@Cancelable
public class PushEvent
extends EventStage {
    public Entity entity;
    public double x;
    public double y;
    public double z;
    public boolean airbone;

    public PushEvent(Entity entity, double d, double d2, double d3, boolean bl) {
        super(0);
        this.entity = entity;
        this.x = d;
        this.y = d2;
        this.z = d3;
        this.airbone = bl;
    }

    public PushEvent(int n) {
        super(n);
    }

    public PushEvent(int n, Entity entity) {
        super(n);
        this.entity = entity;
    }
}

