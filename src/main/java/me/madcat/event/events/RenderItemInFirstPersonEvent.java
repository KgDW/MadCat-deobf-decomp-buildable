package me.madcat.event.events;

import me.madcat.event.EventStage;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.eventhandler.Cancelable;

@Cancelable
public class RenderItemInFirstPersonEvent
extends EventStage {
    public final EntityLivingBase entity;
    public ItemStack stack;
    public final ItemCameraTransforms.TransformType transformType;
    public final boolean leftHanded;

    public RenderItemInFirstPersonEvent(EntityLivingBase entityLivingBase, ItemStack itemStack, ItemCameraTransforms.TransformType transformType, boolean bl, int n) {
        super(n);
        this.entity = entityLivingBase;
        this.stack = itemStack;
        this.transformType = transformType;
        this.leftHanded = bl;
    }

    public ItemCameraTransforms.TransformType getTransformType() {
        return this.transformType;
    }

    public ItemStack getStack() {
        return this.stack;
    }

    public void setStack(ItemStack itemStack) {
        this.stack = itemStack;
    }

    public EntityLivingBase getEntity() {
        return this.entity;
    }
}

