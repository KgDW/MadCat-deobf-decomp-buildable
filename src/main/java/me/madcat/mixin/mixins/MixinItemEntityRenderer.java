package me.madcat.mixin.mixins;

import java.util.Random;
import me.madcat.features.modules.render.ItemPhysics;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderEntityItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemShulkerBox;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={RenderEntityItem.class})
public abstract class MixinItemEntityRenderer
        extends Render<EntityItem> {
    private static final float RAD_TO_DEG = 57.29578f;
    @Shadow
    @Final
    private Random random;
    @Shadow
    private RenderItem itemRenderer;

    protected MixinItemEntityRenderer(RenderManager p_i1487_1_) {
        super(p_i1487_1_);
    }

    @Shadow
    public abstract int getModelCount(ItemStack var1);

    @Inject(at={@At(value="HEAD")}, method={"doRender"}, cancellable=true)
    public void render(EntityItem itemEntity, double x, double y, double z, float yaw, float partialTicks, CallbackInfo callbackInfo) {
        ItemStack itemStack = itemEntity.getItem();
        if (!ItemPhysics.INSTANCE.isEnabled() || itemStack.getItem() == null) {
            return;
        }
        int renderCount = this.getModelCount(itemStack);
        Item item = itemStack.getItem();
        int seed = Item.getIdFromItem(item) + itemStack.getItemDamage();
        this.random.setSeed(seed);
        float rotation = (((float)itemEntity.getAge() + partialTicks) / 20.0f + itemEntity.height) / 20.0f * ((Float)ItemPhysics.INSTANCE.rotateSpeed.getValue()).floatValue();
        this.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
        this.getRenderManager().renderEngine.getTexture(TextureMap.LOCATION_BLOCKS_TEXTURE).setBlurMipmap(false, false);
        GlStateManager.enableRescaleNormal();
        GlStateManager.alphaFunc(516, 0.1f);
        GlStateManager.enableBlend();
        RenderHelper.enableStandardItemLighting();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.pushMatrix();
        GlStateManager.translate((float)x, (float)y, (float)z);
        if (itemEntity.getItem().getItem() instanceof ItemShulkerBox) {
            GlStateManager.scale(((Float)ItemPhysics.INSTANCE.Scaling.getValue()).floatValue() + 1.1f + ((Float)ItemPhysics.INSTANCE.shulkerBox.getValue()).floatValue(), ((Float)ItemPhysics.INSTANCE.Scaling.getValue()).floatValue() + 1.1f + ((Float)ItemPhysics.INSTANCE.shulkerBox.getValue()).floatValue(), ((Float)ItemPhysics.INSTANCE.Scaling.getValue()).floatValue() + 1.1f + ((Float)ItemPhysics.INSTANCE.shulkerBox.getValue()).floatValue());
        } else if (itemEntity.getItem().getItem() instanceof ItemBlock) {
            GlStateManager.scale(((Float)ItemPhysics.INSTANCE.Scaling.getValue()).floatValue() + 1.1f, ((Float)ItemPhysics.INSTANCE.Scaling.getValue()).floatValue() + 1.1f, ((Float)ItemPhysics.INSTANCE.Scaling.getValue()).floatValue() + 1.1f);
        } else {
            GlStateManager.scale(((Float)ItemPhysics.INSTANCE.Scaling.getValue()).floatValue() + 0.3f, ((Float)ItemPhysics.INSTANCE.Scaling.getValue()).floatValue() + 0.3f, ((Float)ItemPhysics.INSTANCE.Scaling.getValue()).floatValue() + 0.3f);
        }
        GlStateManager.rotate(90.0f, 1.0f, 0.0f, 0.0f);
        GlStateManager.rotate(itemEntity.rotationYaw * 57.29578f, 0.0f, 0.0f, 1.0f);
        Minecraft mc = Minecraft.getMinecraft();
        IBakedModel iBakedModel = mc.getRenderItem().getItemModelMesher().getItemModel(itemStack);
        this.rotateX(itemEntity, rotation);
        if (iBakedModel.isGui3d()) {
            GlStateManager.translate(0.0f, -0.2f, -0.08f);
        } else if (itemEntity.getEntityWorld().getBlockState(itemEntity.getPosition()).getBlock() == Blocks.SNOW || itemEntity.getEntityWorld().getBlockState(itemEntity.getPosition().down()).getBlock() == Blocks.SOUL_SAND) {
            GlStateManager.translate(0.0f, 0.0f, -0.14f);
        } else {
            GlStateManager.translate(0.0f, 0.0f, -0.04f);
        }
        float height = 0.2f;
        if (iBakedModel.isGui3d()) {
            GlStateManager.translate(0.0f, height, 0.0f);
        }
        GlStateManager.rotate(itemEntity.rotationPitch * 57.29578f, 0.0f, 1.0f, 0.0f);
        if (iBakedModel.isGui3d()) {
            GlStateManager.translate(0.0f, -height, 0.0f);
        }
        if (!iBakedModel.isGui3d()) {
            float xO = -0.0f * (float)(renderCount - 1) * 0.5f;
            float yO = -0.0f * (float)(renderCount - 1) * 0.5f;
            float zO = -0.09375f * (float)(renderCount - 1) * 0.5f;
            GlStateManager.translate(xO, yO, zO);
        }
        for (int k = 0; k < renderCount; ++k) {
            GlStateManager.pushMatrix();
            if (k > 0 && iBakedModel.isGui3d()) {
                float f11 = (this.random.nextFloat() * 2.0f - 1.0f) * 0.15f;
                float f13 = (this.random.nextFloat() * 2.0f - 1.0f) * 0.15f;
                float f10 = (this.random.nextFloat() * 2.0f - 1.0f) * 0.15f;
                GlStateManager.translate(f11, f13, f10);
            }
            iBakedModel.getItemCameraTransforms().applyTransform(ItemCameraTransforms.TransformType.GROUND);
            this.itemRenderer.renderItem(itemStack, iBakedModel);
            GlStateManager.popMatrix();
            if (iBakedModel.isGui3d()) continue;
            GlStateManager.translate(0.0f, 0.0f, 0.09375f);
        }
        GlStateManager.popMatrix();
        GlStateManager.disableRescaleNormal();
        GlStateManager.disableBlend();
        this.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
        this.getRenderManager().renderEngine.getTexture(TextureMap.LOCATION_BLOCKS_TEXTURE).restoreLastBlurMipmap();
        callbackInfo.cancel();
    }

    private void rotateX(EntityItem itemEntity, float rotation) {
        if (itemEntity.onGround) {
            return;
        }
        itemEntity.rotationPitch += rotation * 2.0f;
    }
}
