package me.madcat.mixin.mixins;

import net.minecraft.util.EnumHand;
import org.spongepowered.asm.mixin.injection.Inject;
import net.minecraft.client.renderer.GlStateManager;
import me.madcat.features.modules.render.Model;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import me.madcat.features.modules.render.GlintModify;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderItem;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({ RenderItem.class })
public class MixinRenderItem
{
    final Minecraft mc;
    private float angle;

    public MixinRenderItem() {
        this.mc = Minecraft.getMinecraft();
    }

    @ModifyArg(method = { "renderEffect" }, at = @At(value = "INVOKE", target = "net/minecraft/client/renderer/RenderItem.renderModel(Lnet/minecraft/client/renderer/block/model/IBakedModel;I)V"), index = 1)
    private int renderEffect(final int oldValue) {
        return GlintModify.INSTANCE().isEnabled() ? GlintModify.getColor(1L, 1.0f).getRGB() : oldValue;
    }

    @Inject(method = { "renderItemModel" }, at = { @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/RenderItem;renderItem(Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/renderer/block/model/IBakedModel;)V", shift = At.Shift.BEFORE) })
    private void renderCustom(final ItemStack stack, final IBakedModel bakedmodel, final ItemCameraTransforms.TransformType transform, final boolean leftHanded, final CallbackInfo ci) {
        final Model mod = Model.INSTANCE();
        final float scale = 1.0f;
        final float xOffset = 0.0f;
        final float yOffset = 0.0f;
        if (mod.isOn()) {
            GlStateManager.scale(scale, scale, scale);
            if (this.mc.player.getActiveItemStack() != stack) {
                GlStateManager.translate(xOffset, yOffset, 0.0f);
            }
        }
    }

    @Inject(method = { "renderItemModel" }, at = { @At("HEAD") })
    public void renderItem(final ItemStack stack, final IBakedModel bakedmodel, final ItemCameraTransforms.TransformType transform, final boolean leftHanded, final CallbackInfo ci) {
        final Model mod = Model.INSTANCE();
        if (mod.isOn() && (transform == ItemCameraTransforms.TransformType.FIRST_PERSON_LEFT_HAND || transform == ItemCameraTransforms.TransformType.FIRST_PERSON_RIGHT_HAND)) {
            if (transform == ItemCameraTransforms.TransformType.FIRST_PERSON_LEFT_HAND && this.mc.player.getActiveHand() == EnumHand.OFF_HAND && this.mc.player.isHandActive()) {
                return;
            }
            if (mod.isOn() && (mod.spinX.getValue() || mod.spinY.getValue())) {
                GlStateManager.rotate(this.angle, ((boolean)mod.spinX.getValue()) ? this.angle : 0.0f, ((boolean)mod.spinY.getValue()) ? this.angle : 0.0f, 0.0f);
                ++this.angle;
            }
        }
        else {
            if (this.mc.player.getActiveHand() == EnumHand.MAIN_HAND && this.mc.player.isHandActive()) {
                return;
            }
            if (mod.isOn() && (mod.spinX.getValue() || mod.spinY.getValue())) {
                GlStateManager.rotate(this.angle, ((boolean)mod.spinX.getValue()) ? this.angle : 0.0f, ((boolean)mod.spinY.getValue()) ? this.angle : 0.0f, 0.0f);
                ++this.angle;
            }
        }
    }
}
