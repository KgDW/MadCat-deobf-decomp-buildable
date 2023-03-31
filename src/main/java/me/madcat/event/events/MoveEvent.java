package me.madcat.event.events;

import me.madcat.event.EventStage;
import net.minecraft.entity.MoverType;
import net.minecraftforge.fml.common.eventhandler.Cancelable;

@Cancelable
public class MoveEvent
extends EventStage {
    private MoverType type;
    private double x;
    private double y;
    private double z;

    public MoveEvent(int n, MoverType moverType, double d, double d2, double d3) {
        super(n);
        this.type = moverType;
        this.x = d;
        this.y = d2;
        this.z = d3;
    }

    public MoverType getType() {
        return this.type;
    }

    public void setType(MoverType moverType) {
        this.type = moverType;
    }

    public double getX() {
        return this.x;
    }

    public void setX(double d) {
        this.x = d;
    }

    public double getY() {
        return this.y;
    }

    public void setY(double d) {
        this.y = d;
    }

    public double getZ() {
        return this.z;
    }

    public void setZ(double d) {
        this.z = d;
    }
}

