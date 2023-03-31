package me.madcat.mixin.mixins;

import net.minecraft.util.math.AxisAlignedBB;
import org.spongepowered.asm.mixin.injection.Inject;
import me.madcat.event.events.TurnEvent;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.common.MinecraftForge;
import me.madcat.event.events.PushEvent;
import org.spongepowered.asm.mixin.Shadow;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(value = { Entity.class }, priority = 998)
public abstract class MixinEntity
{
    @Shadow
    public double posX;
    @Shadow
    public double posY;
    @Shadow
    public double posZ;
    @Shadow
    public float rotationPitch;
    @Shadow
    public float rotationYaw;
    @Shadow
    public Entity ridingEntity;
    @Shadow
    public double motionX;
    @Shadow
    public double motionY;
    @Shadow
    public double motionZ;
    @Shadow
    public boolean onGround;
    @Shadow
    public float width;

    @Redirect(method = { "applyEntityCollision" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;addVelocity(DDD)V"))
    public void addVelocityHook(final Entity entity, final double x, final double y, final double z) {
        final PushEvent event = new PushEvent(entity, x, y, z, true);
        MinecraftForge.EVENT_BUS.post((Event)event);
        if (event.isCanceled()) {
            return;
        }
        entity.motionX += event.x;
        entity.motionY += event.y;
        entity.motionZ += event.z;
        entity.isAirBorne = event.airbone;
    }

    @Inject(method = { "turn" }, at = { @At("HEAD") }, cancellable = true)
    public void onTurnHook(final float yaw, final float pitch, final CallbackInfo info) {
        final TurnEvent event = new TurnEvent(yaw, pitch);
        MinecraftForge.EVENT_BUS.post((Event)event);
        if (event.isCanceled()) {
            info.cancel();
        }
    }

    @Shadow
    public abstract boolean isSprinting();

    @Shadow
    public abstract AxisAlignedBB getEntityBoundingBox();

    @Shadow
    public abstract boolean isSneaking();
}
 