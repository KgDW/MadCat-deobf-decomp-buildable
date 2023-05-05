package me.madcat.mixin.mixins;

import me.madcat.features.modules.render.GlintModify;
import me.madcat.features.modules.render.Model;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={RenderItem.class})
public class MixinRenderItem {
    final Minecraft mc = Minecraft.getMinecraft();
    private float angle;

    @ModifyArg(method={"renderEffect"}, at=@At(value="INVOKE", target="net/minecraft/client/renderer/RenderItem.renderModel(Lnet/minecraft/client/renderer/block/model/IBakedModel;I)V"), index=1)
    private int renderEffect(int oldValue) {
        return GlintModify.INSTANCE().isEnabled() ? GlintModify.getColor(1L, 1.0f).getRGB() : oldValue;
    }

    @Inject(method={"renderItemModel"}, at={@At(value="INVOKE", target="Lnet/minecraft/client/renderer/RenderItem;renderItem(Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/renderer/block/model/IBakedModel;)V", shift=At.Shift.BEFORE)})
    private void renderCustom(ItemStack stack, IBakedModel bakedmodel, ItemCameraTransforms.TransformType transform, boolean leftHanded, CallbackInfo ci) {
        Model mod = Model.INSTANCE();
        float scale = 1.0f;
        float xOffset = 0.0f;
        float yOffset = 0.0f;
        if (mod.isOn()) {
            GlStateManager.scale(scale, scale, scale);
            if (this.mc.player.getActiveItemStack() != stack) {
                GlStateManager.translate(xOffset, yOffset, 0.0f);
            }
        }
    }

    @Inject(method={"renderItemModel"}, at={@At(value="HEAD")})
    public void renderItem(ItemStack stack, IBakedModel bakedmodel, ItemCameraTransforms.TransformType transform, boolean leftHanded, CallbackInfo ci) {
        Model mod = Model.INSTANCE();
        if (mod.isOn() && (transform == ItemCameraTransforms.TransformType.FIRST_PERSON_LEFT_HAND || transform == ItemCameraTransforms.TransformType.FIRST_PERSON_RIGHT_HAND)) {
            if (transform == ItemCameraTransforms.TransformType.FIRST_PERSON_LEFT_HAND && this.mc.player.getActiveHand() == EnumHand.OFF_HAND && this.mc.player.isHandActive()) {
                return;
            }
            if (mod.isOn() && (((Boolean)mod.spinX.getValue()).booleanValue() || ((Boolean)mod.spinY.getValue()).booleanValue())) {
                GlStateManager.rotate(this.angle, (Boolean)mod.spinX.getValue() != false ? this.angle : 0.0f, (Boolean)mod.spinY.getValue() != false ? this.angle : 0.0f, 0.0f);
                this.angle += 1.0f;
            }
        } else {
            if (this.mc.player.getActiveHand() == EnumHand.MAIN_HAND && this.mc.player.isHandActive()) {
                return;
            }
            if (mod.isOn() && (((Boolean)mod.spinX.getValue()).booleanValue() || ((Boolean)mod.spinY.getValue()).booleanValue())) {
                GlStateManager.rotate(this.angle, (Boolean)mod.spinX.getValue() != false ? this.angle : 0.0f, (Boolean)mod.spinY.getValue() != false ? this.angle : 0.0f, 0.0f);
                this.angle += 1.0f;
            }
        }
    }
}
