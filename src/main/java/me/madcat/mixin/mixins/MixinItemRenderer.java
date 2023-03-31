package me.madcat.mixin.mixins;

import net.minecraft.util.math.MathHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import me.madcat.event.events.RenderItemEvent;
import net.minecraft.util.EnumHandSide;
import me.madcat.features.modules.render.Model;
import org.spongepowered.asm.mixin.injection.Redirect;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.common.MinecraftForge;
import me.madcat.event.events.RenderItemInFirstPersonEvent;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.item.ItemStack;
import net.minecraft.entity.EntityLivingBase;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import me.madcat.features.modules.render.NoRender;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.client.renderer.ItemRenderer;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({ ItemRenderer.class })
public abstract class MixinItemRenderer
{
    @Inject(method = { "renderSuffocationOverlay" }, at = { @At("HEAD") }, cancellable = true)
    public void renderSuffocationOverlay(final CallbackInfo ci) {
        if (NoRender.INSTANCE().isOn() && NoRender.INSTANCE().blocks.getValue()) {
            ci.cancel();
        }
    }

    @Redirect(method = { "renderItemInFirstPerson(Lnet/minecraft/client/entity/AbstractClientPlayer;FFLnet/minecraft/util/EnumHand;FLnet/minecraft/item/ItemStack;F)V" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/ItemRenderer;renderItemSide(Lnet/minecraft/entity/EntityLivingBase;Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/renderer/block/model/ItemCameraTransforms$TransformType;Z)V"))
    public void renderItemInFirstPerson(final ItemRenderer itemRenderer, final EntityLivingBase entitylivingbaseIn, final ItemStack heldStack, final ItemCameraTransforms.TransformType transform, final boolean leftHanded) {
        final RenderItemInFirstPersonEvent eventPre = new RenderItemInFirstPersonEvent(entitylivingbaseIn, heldStack, transform, leftHanded, 0);
        MinecraftForge.EVENT_BUS.post((Event)eventPre);
        if (!eventPre.isCanceled()) {
            itemRenderer.renderItemSide(entitylivingbaseIn, eventPre.getStack(), eventPre.getTransformType(), leftHanded);
        }
        final RenderItemInFirstPersonEvent eventPost = new RenderItemInFirstPersonEvent(entitylivingbaseIn, heldStack, transform, leftHanded, 1);
        MinecraftForge.EVENT_BUS.post((Event)eventPost);
    }

    @Inject(method = { "rotateArm" }, at = { @At("HEAD") }, cancellable = true)
    public void rotateArmHook(final float partialTicks, final CallbackInfo info) {
        final Model mod = Model.INSTANCE();
        if (mod.isOn() && mod.noSway.getValue()) {
            info.cancel();
        }
    }

    @Inject(method = { "transformSideFirstPerson" }, at = { @At("HEAD") }, cancellable = true)
    public void transformSideFirstPerson(final EnumHandSide hand, final float p_187459_2_, final CallbackInfo cancel) {
        final RenderItemEvent event = new RenderItemEvent(0.0, 0.0, 0.0, 0.0, 0.0, 0.0);
        MinecraftForge.EVENT_BUS.post((Event)event);
        if (Model.INSTANCE().isEnabled()) {
            final boolean bob = Model.INSTANCE().isDisabled() || Model.INSTANCE().doBob.getValue();
            final int i = (hand == EnumHandSide.RIGHT) ? 1 : -1;
            GlStateManager.translate(i * 0.56f, -0.52f + (bob ? p_187459_2_ : 0.0f) * -0.6f, -0.72f);
            if (hand == EnumHandSide.RIGHT) {
                GlStateManager.translate(event.getMainX(), event.getMainY(), event.getMainZ());
            }
            else {
                GlStateManager.translate(event.getOffX(), event.getOffY(), event.getOffZ());
            }
            cancel.cancel();
        }
    }

    @Inject(method = { "transformEatFirstPerson" }, at = { @At("HEAD") }, cancellable = true)
    private void transformEatFirstPerson(final float p_187454_1_, final EnumHandSide hand, final ItemStack stack, final CallbackInfo cancel) {
        if (Model.INSTANCE().isEnabled()) {
            if (!Model.INSTANCE().noEatAnimation.getValue()) {
                final float f = Minecraft.getMinecraft().player.getItemInUseCount() - p_187454_1_ + 1.0f;
                final float f2 = f / stack.getMaxItemUseDuration();
                if (f2 < 0.8f) {
                    final float f3 = MathHelper.abs(MathHelper.cos(f / 4.0f * 3.1415927f) * 0.1f);
                    GlStateManager.translate(0.0f, f3, 0.0f);
                }
                final float f3 = 1.0f - (float)Math.pow(f2, 27.0);
                final int i = (hand == EnumHandSide.RIGHT) ? 1 : -1;
                GlStateManager.translate(f3 * 0.6f * i * Model.INSTANCE().eatX.getValue(), f3 * 0.5f * -Model.INSTANCE().eatY.getValue(), 0.0);
                GlStateManager.rotate(i * f3 * 90.0f, 0.0f, 1.0f, 0.0f);
                GlStateManager.rotate(f3 * 10.0f, 1.0f, 0.0f, 0.0f);
                GlStateManager.rotate(i * f3 * 30.0f, 0.0f, 0.0f, 1.0f);
            }
            cancel.cancel();
        }
    }
}
 