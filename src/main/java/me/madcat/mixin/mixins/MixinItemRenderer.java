package me.madcat.mixin.mixins;

import me.madcat.event.events.RenderItemEvent;
import me.madcat.event.events.RenderItemInFirstPersonEvent;
import me.madcat.features.modules.render.Model;
import me.madcat.features.modules.render.NoRender;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={ItemRenderer.class})
public abstract class MixinItemRenderer {
    @Inject(method={"renderSuffocationOverlay"}, at={@At(value="HEAD")}, cancellable=true)
    public void renderSuffocationOverlay(CallbackInfo ci) {
        if (NoRender.INSTANCE().isOn() && ((Boolean)NoRender.INSTANCE().blocks.getValue()).booleanValue()) {
            ci.cancel();
        }
    }

    @Redirect(method={"renderItemInFirstPerson(Lnet/minecraft/client/entity/AbstractClientPlayer;FFLnet/minecraft/util/EnumHand;FLnet/minecraft/item/ItemStack;F)V"}, at=@At(value="INVOKE", target="Lnet/minecraft/client/renderer/ItemRenderer;renderItemSide(Lnet/minecraft/entity/EntityLivingBase;Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/renderer/block/model/ItemCameraTransforms$TransformType;Z)V"))
    public void renderItemInFirstPerson(ItemRenderer itemRenderer, EntityLivingBase entitylivingbaseIn, ItemStack heldStack, ItemCameraTransforms.TransformType transform, boolean leftHanded) {
        RenderItemInFirstPersonEvent eventPre = new RenderItemInFirstPersonEvent(entitylivingbaseIn, heldStack, transform, leftHanded, 0);
        MinecraftForge.EVENT_BUS.post(eventPre);
        if (!eventPre.isCanceled()) {
            itemRenderer.renderItemSide(entitylivingbaseIn, eventPre.getStack(), eventPre.getTransformType(), leftHanded);
        }
        RenderItemInFirstPersonEvent eventPost = new RenderItemInFirstPersonEvent(entitylivingbaseIn, heldStack, transform, leftHanded, 1);
        MinecraftForge.EVENT_BUS.post(eventPost);
    }

    @Inject(method={"rotateArm"}, at={@At(value="HEAD")}, cancellable=true)
    public void rotateArmHook(float partialTicks, CallbackInfo info) {
        Model mod = Model.INSTANCE();
        if (mod.isOn() && ((Boolean)mod.noSway.getValue()).booleanValue()) {
            info.cancel();
        }
    }

    @Inject(method={"transformSideFirstPerson"}, at={@At(value="HEAD")}, cancellable=true)
    public void transformSideFirstPerson(EnumHandSide hand, float p_187459_2_, CallbackInfo cancel) {
        RenderItemEvent event = new RenderItemEvent(0.0, 0.0, 0.0, 0.0, 0.0, 0.0);
        MinecraftForge.EVENT_BUS.post(event);
        if (Model.INSTANCE().isEnabled()) {
            boolean bob = Model.INSTANCE().isDisabled() || (Boolean)Model.INSTANCE().doBob.getValue() != false;
            int i = hand == EnumHandSide.RIGHT ? 1 : -1;
            GlStateManager.translate((float)i * 0.56f, -0.52f + (bob ? p_187459_2_ : 0.0f) * -0.6f, -0.72f);
            if (hand == EnumHandSide.RIGHT) {
                GlStateManager.translate(event.getMainX(), event.getMainY(), event.getMainZ());
            } else {
                GlStateManager.translate(event.getOffX(), event.getOffY(), event.getOffZ());
            }
            cancel.cancel();
        }
    }

    @Inject(method={"transformEatFirstPerson"}, at={@At(value="HEAD")}, cancellable=true)
    private void transformEatFirstPerson(float p_187454_1_, EnumHandSide hand, ItemStack stack, CallbackInfo cancel) {
        if (Model.INSTANCE().isEnabled()) {
            if (!((Boolean)Model.INSTANCE().noEatAnimation.getValue()).booleanValue()) {
                float f3;
                float f = (float)Minecraft.getMinecraft().player.getItemInUseCount() - p_187454_1_ + 1.0f;
                float f2 = f / (float)stack.getMaxItemUseDuration();
                if (f2 < 0.8f) {
                    f3 = MathHelper.abs(MathHelper.cos(f / 4.0f * (float)Math.PI) * 0.1f);
                    GlStateManager.translate(0.0f, f3, 0.0f);
                }
                f3 = 1.0f - (float)Math.pow(f2, 27.0);
                int i = hand == EnumHandSide.RIGHT ? 1 : -1;
                GlStateManager.translate((double)(f3 * 0.6f * (float)i) * (Double)Model.INSTANCE().eatX.getValue(), (double)(f3 * 0.5f) * -((Double)Model.INSTANCE().eatY.getValue()).doubleValue(), 0.0);
                GlStateManager.rotate((float)i * f3 * 90.0f, 0.0f, 1.0f, 0.0f);
                GlStateManager.rotate(f3 * 10.0f, 1.0f, 0.0f, 0.0f);
                GlStateManager.rotate((float)i * f3 * 30.0f, 0.0f, 0.0f, 1.0f);
            }
            cancel.cancel();
        }
    }
}
 