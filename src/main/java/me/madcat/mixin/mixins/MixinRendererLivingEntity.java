package me.madcat.mixin.mixins;

import org.spongepowered.asm.mixin.injection.Inject;
import org.lwjgl.opengl.GL11;
import me.madcat.features.modules.render.Chams;
import net.minecraft.entity.player.EntityPlayer;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import me.madcat.event.events.RenderEntityModelEvent;
import me.madcat.features.modules.legacy.LegacyGlow;
import net.minecraft.entity.Entity;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import org.spongepowered.asm.mixin.Mixin;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.EntityLivingBase;

@Mixin({ RenderLivingBase.class })
public abstract class MixinRendererLivingEntity<T extends EntityLivingBase> extends Render<T>
{
    protected MixinRendererLivingEntity() {
        super((RenderManager)null);
    }

    @Redirect(method = { "renderModel" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/model/ModelBase;render(Lnet/minecraft/entity/Entity;FFFFFF)V"))
    private void renderModelHook(final ModelBase modelBase, final Entity entityIn, final float limbSwing, final float limbSwingAmount, final float ageInTicks, final float netHeadYaw, final float headPitch, final float scale) {
        boolean cancel = false;
        if (LegacyGlow.INSTANCE().isEnabled()) {
            final RenderEntityModelEvent event = new RenderEntityModelEvent(0, modelBase, entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
            if (LegacyGlow.INSTANCE().isEnabled()) {
                LegacyGlow.INSTANCE().onRenderModel(event);
                if (event.isCanceled()) {
                    cancel = true;
                }
            }
        }
        if (!cancel) {
            modelBase.render(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
        }
    }

    @Inject(method = { "doRender" }, at = { @At("HEAD") })
    public void doRenderPre(final T entity, final double x, final double y, final double z, final float entityYaw, final float partialTicks, final CallbackInfo info) {
        if (entity instanceof EntityPlayer && Chams.INSTANCE().isEnabled() && Chams.INSTANCE().behind.getValue()) {
            GL11.glEnable(32823);
            GL11.glPolygonOffset(1.0f, -1100000.0f);
        }
    }

    @Inject(method = { "doRender" }, at = { @At("RETURN") })
    public void doRenderPost(final T entity, final double x, final double y, final double z, final float entityYaw, final float partialTicks, final CallbackInfo info) {
        if (Chams.INSTANCE().isEnabled() && entity != null && Chams.INSTANCE().behind.getValue()) {
            GL11.glPolygonOffset(1.0f, 1000000.0f);
            GL11.glDisable(32823);
        }
    }
}
 