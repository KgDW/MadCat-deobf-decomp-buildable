package me.madcat.event.events;

import me.madcat.event.EventStage;
import net.minecraft.client.model.ModelBase;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.common.eventhandler.Cancelable;

@Cancelable
public class RenderEntityModelEvent
extends EventStage {
    public final ModelBase modelBase;
    public final Entity entity;
    public final float limbSwing;
    public final float limbSwingAmount;
    public final float age;
    public final float headYaw;
    public final float headPitch;
    public final float scale;

    public RenderEntityModelEvent(int n, ModelBase modelBase, Entity entity, float f, float f2, float f3, float f4, float f5, float f6) {
        super(n);
        this.modelBase = modelBase;
        this.entity = entity;
        this.limbSwing = f;
        this.limbSwingAmount = f2;
        this.age = f3;
        this.headYaw = f4;
        this.headPitch = f5;
        this.scale = f6;
    }
}

