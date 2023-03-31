package me.madcat.mixin.mixins;

import org.spongepowered.asm.mixin.injection.Inject;
import me.madcat.util.ColorUtil;
import me.madcat.features.modules.client.ClickGui;
import net.minecraft.util.math.MathHelper;
import me.madcat.features.modules.legacy.LegacyGlow;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.item.EntityEnderCrystal;
import me.madcat.util.RenderUtil;
import java.awt.Color;
import me.madcat.MadCat;
import org.lwjgl.opengl.GL11;
import net.minecraft.client.renderer.GlStateManager;
import me.madcat.features.modules.render.CrystalChams;
import net.minecraft.entity.Entity;
import net.minecraft.client.model.ModelBase;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Final;
import net.minecraft.util.ResourceLocation;
import net.minecraft.client.renderer.entity.RenderEnderCrystal;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({ RenderEnderCrystal.class })
public class MixinRenderEnderCrystal
{
    @Final
    @Shadow
    private static ResourceLocation ENDER_CRYSTAL_TEXTURES;
    @Shadow
    public ModelBase modelEnderCrystal;
    @Shadow
    public ModelBase modelEnderCrystalNoBase;

    @Redirect(method = { "doRender(Lnet/minecraft/entity/item/EntityEnderCrystal;DDDFF)V" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/model/ModelBase;render(Lnet/minecraft/entity/Entity;FFFFFF)V"))
    public void renderModelBaseHook(final ModelBase model, final Entity entity, final float limbSwing, final float limbSwingAmount, final float ageInTicks, final float netHeadYaw, final float headPitch, final float scale) {
        final CrystalChams mod = CrystalChams.INSTANCE;
        final float newLimbSwingAmount = mod.changeSpeed.getValue() ? (limbSwingAmount * mod.spinSpeed.getValue()) : limbSwingAmount;
        final float newAgeInTicks = mod.changeSpeed.getValue() ? ((mod.floatFactor.getValue() == 0.0f) ? 0.15f : (ageInTicks * mod.floatFactor.getValue())) : ageInTicks;
        if (mod.isOn()) {
            GlStateManager.scale((float)mod.scale.getValue(), (float)mod.scale.getValue(), (float)mod.scale.getValue());
            if (mod.model.getValue() == CrystalChams.Model.VANILLA) {
                model.render(entity, limbSwing, newLimbSwingAmount, newAgeInTicks, netHeadYaw, headPitch, scale);
            }
            else if (mod.model.getValue() == CrystalChams.Model.XQZ) {
                GL11.glEnable(32823);
                GlStateManager.enablePolygonOffset();
                GL11.glPolygonOffset(1.0f, -1000000.0f);
                if (mod.modelColor.getValue()) {
                    final Color rainbow = MadCat.colorManager.getRainbow();
                    final Color color = mod.rainbow.getValue() ? new Color(rainbow.getRed(), rainbow.getGreen(), rainbow.getBlue(), mod.modelAlpha.getValue()) : new Color(mod.modelRed.getValue(), mod.modelGreen.getValue(), mod.modelBlue.getValue(), mod.modelAlpha.getValue());
                    RenderUtil.glColor(color);
                }
                model.render(entity, limbSwing, newLimbSwingAmount, newAgeInTicks, netHeadYaw, headPitch, scale);
                GL11.glDisable(32823);
                GlStateManager.disablePolygonOffset();
                GL11.glPolygonOffset(1.0f, 1000000.0f);
            }
            if (mod.wireframe.getValue()) {
                final Color rainbow = MadCat.colorManager.getRainbow();
                final Color color = mod.rainbow.getValue() ? new Color(rainbow.getRed(), rainbow.getGreen(), rainbow.getBlue(), mod.alpha.getValue()) : (mod.lineColor.getValue() ? new Color(mod.lineRed.getValue(), mod.lineGreen.getValue(), mod.lineBlue.getValue(), mod.lineAlpha.getValue()) : new Color(mod.red.getValue(), mod.green.getValue(), mod.blue.getValue(), mod.alpha.getValue()));
                GL11.glPushMatrix();
                GL11.glPushAttrib(1048575);
                GL11.glPolygonMode(1032, 6913);
                GL11.glDisable(3553);
                GL11.glDisable(2896);
                GL11.glDisable(2929);
                GL11.glEnable(2848);
                GL11.glEnable(3042);
                GlStateManager.blendFunc(770, 771);
                RenderUtil.glColor(color);
                GlStateManager.glLineWidth((float)mod.lineWidth.getValue());
                model.render(entity, limbSwing, newLimbSwingAmount, newAgeInTicks, netHeadYaw, headPitch, scale);
                GL11.glPopAttrib();
                GL11.glPopMatrix();
            }
            if (mod.fill.getValue()) {
                final Color rainbow = MadCat.colorManager.getRainbow();
                final Color color = mod.rainbow.getValue() ? new Color(rainbow.getRed(), rainbow.getGreen(), rainbow.getBlue(), mod.alpha.getValue()) : new Color(mod.red.getValue(), mod.green.getValue(), mod.blue.getValue(), mod.alpha.getValue());
                GL11.glPushAttrib(1048575);
                GL11.glDisable(3008);
                GL11.glDisable(3553);
                GL11.glDisable(2896);
                GL11.glEnable(3042);
                GL11.glBlendFunc(770, 771);
                GL11.glLineWidth(1.5f);
                GL11.glEnable(2960);
                if (mod.xqz.getValue()) {
                    GL11.glDisable(2929);
                    GL11.glDepthMask(false);
                }
                GL11.glEnable(10754);
                RenderUtil.glColor(color);
                model.render(entity, limbSwing, newLimbSwingAmount, newAgeInTicks, netHeadYaw, headPitch, scale);
                if (mod.xqz.getValue()) {
                    GL11.glEnable(2929);
                    GL11.glDepthMask(true);
                }
                GL11.glEnable(3042);
                GL11.glEnable(2896);
                GL11.glEnable(3553);
                GL11.glEnable(3008);
                GL11.glPopAttrib();
            }
            if (mod.glint.getValue() && entity instanceof EntityEnderCrystal) {
                final Color rainbow = MadCat.colorManager.getRainbow();
                final Color color = mod.rainbow.getValue() ? new Color(rainbow.getRed(), rainbow.getGreen(), rainbow.getBlue(), mod.alpha.getValue()) : new Color(mod.red.getValue(), mod.green.getValue(), mod.blue.getValue(), mod.alpha.getValue());
                GL11.glPushMatrix();
                GL11.glPushAttrib(1048575);
                GL11.glPolygonMode(1032, 6914);
                GL11.glDisable(2896);
                GL11.glDepthRange(0.0, 0.1);
                GL11.glEnable(3042);
                RenderUtil.glColor(color);
                GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_COLOR, GlStateManager.DestFactor.ONE);
                final float f = entity.ticksExisted + Minecraft.getMinecraft().getRenderPartialTicks();
                Minecraft.getMinecraft().getRenderManager().renderEngine.bindTexture(new ResourceLocation("textures/misc/enchanted_item_glint.png"));
                for (int i = 0; i < 2; ++i) {
                    GlStateManager.matrixMode(5890);
                    GlStateManager.loadIdentity();
                    GL11.glScalef(1.0f, 1.0f, 1.0f);
                    GlStateManager.rotate(30.0f - i * 60.0f, 0.0f, 0.0f, 1.0f);
                    GlStateManager.translate(0.0f, f * (0.001f + i * 0.003f) * 20.0f, 0.0f);
                    GlStateManager.matrixMode(5888);
                    model.render(entity, limbSwing, newLimbSwingAmount, newAgeInTicks, netHeadYaw, headPitch, scale);
                }
                GlStateManager.matrixMode(5890);
                GlStateManager.loadIdentity();
                GlStateManager.matrixMode(5888);
                GL11.glDisable(3042);
                GL11.glDepthRange(0.0, 1.0);
                GL11.glEnable(2896);
                GL11.glPopAttrib();
                GL11.glPopMatrix();
            }
            GlStateManager.scale(1.0f / mod.scale.getValue(), 1.0f / mod.scale.getValue(), 1.0f / mod.scale.getValue());
        }
        else {
            model.render(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
        }
    }

    @Inject(method = { "doRender(Lnet/minecraft/entity/item/EntityEnderCrystal;DDDFF)V" }, at = { @At("RETURN") }, cancellable = true)
    public void IdoRender(final EntityEnderCrystal var1, final double var2, final double var4, final double var6, final float var8, final float var9, final CallbackInfo var10) {
        final Minecraft mc = Minecraft.getMinecraft();
        mc.gameSettings.fancyGraphics = false;
        if (LegacyGlow.INSTANCE().isEnabled() && LegacyGlow.INSTANCE().other.getValue()) {
            final float var11 = var1.innerRotation + var9;
            GlStateManager.pushMatrix();
            GlStateManager.translate(var2, var4, var6);
            Minecraft.getMinecraft().getRenderManager().renderEngine.bindTexture(MixinRenderEnderCrystal.ENDER_CRYSTAL_TEXTURES);
            float var12 = MathHelper.sin(var11 * 0.2f) / 2.0f + 0.5f;
            var12 += var12 * var12;
            GL11.glLineWidth(5.0f);
            float ageInTicks2 = var12 * 0.2f;
            if (CrystalChams.INSTANCE.isEnabled()) {
                ageInTicks2 = (CrystalChams.INSTANCE.changeSpeed.getValue() ? ((CrystalChams.INSTANCE.floatFactor.getValue() == 0.0f) ? 0.15f : (var12 * 0.2f * CrystalChams.INSTANCE.floatFactor.getValue())) : (var12 * 0.2f));
            }
            if (var1.shouldShowBottom()) {
                this.modelEnderCrystal.render((Entity)var1, 0.0f, var11 * 3.0f, ageInTicks2, 0.0f, 0.0f, 0.0625f);
            }
            else {
                this.modelEnderCrystalNoBase.render((Entity)var1, 0.0f, var11 * 3.0f, ageInTicks2, 0.0f, 0.0f, 0.0625f);
            }
            RenderUtil.renderOne(LegacyGlow.INSTANCE().lineWidth.getValue());
            if (var1.shouldShowBottom()) {
                this.modelEnderCrystal.render((Entity)var1, 0.0f, var11 * 3.0f, ageInTicks2, 0.0f, 0.0f, 0.0625f);
            }
            else {
                this.modelEnderCrystalNoBase.render((Entity)var1, 0.0f, var11 * 3.0f, ageInTicks2, 0.0f, 0.0f, 0.0625f);
            }
            RenderUtil.renderTwo();
            if (var1.shouldShowBottom()) {
                this.modelEnderCrystal.render((Entity)var1, 0.0f, var11 * 3.0f, ageInTicks2, 0.0f, 0.0f, 0.0625f);
            }
            else {
                this.modelEnderCrystalNoBase.render((Entity)var1, 0.0f, var11 * 3.0f, ageInTicks2, 0.0f, 0.0f, 0.0625f);
            }
            final Color color = LegacyGlow.INSTANCE().colorSync.getValue() ? new Color(ColorUtil.rainbow(ClickGui.INSTANCE().rainbowHue.getValue()).getRed(), ColorUtil.rainbow(ClickGui.INSTANCE().rainbowHue.getValue()).getGreen(), ColorUtil.rainbow(ClickGui.INSTANCE().rainbowHue.getValue()).getBlue(), LegacyGlow.INSTANCE().alpha.getValue()) : new Color(LegacyGlow.INSTANCE().red.getValue(), LegacyGlow.INSTANCE().green.getValue(), LegacyGlow.INSTANCE().blue.getValue(), LegacyGlow.INSTANCE().alpha.getValue());
            RenderUtil.renderThree();
            RenderUtil.renderFour(color);
            RenderUtil.setColor(color);
            if (var1.shouldShowBottom()) {
                this.modelEnderCrystal.render((Entity)var1, 0.0f, var11 * 3.0f, ageInTicks2, 0.0f, 0.0f, 0.0625f);
            }
            else {
                this.modelEnderCrystalNoBase.render((Entity)var1, 0.0f, var11 * 3.0f, ageInTicks2, 0.0f, 0.0f, 0.0625f);
            }
            RenderUtil.renderFive();
            GlStateManager.popMatrix();
        }
    }
}
 