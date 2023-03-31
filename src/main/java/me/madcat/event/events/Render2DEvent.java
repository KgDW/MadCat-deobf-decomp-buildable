package me.madcat.event.events;

import me.madcat.event.EventStage;
import net.minecraft.client.gui.ScaledResolution;

public class Render2DEvent
extends EventStage {
    public float partialTicks;
    public ScaledResolution scaledResolution;

    public Render2DEvent(float f, ScaledResolution scaledResolution) {
        this.partialTicks = f;
        this.scaledResolution = scaledResolution;
    }

    public void setPartialTicks(float f) {
        this.partialTicks = f;
    }

    public void setScaledResolution(ScaledResolution scaledResolution) {
        this.scaledResolution = scaledResolution;
    }

    public double getScreenWidth() {
        return this.scaledResolution.getScaledWidth_double();
    }

    public double getScreenHeight() {
        return this.scaledResolution.getScaledHeight_double();
    }
}

