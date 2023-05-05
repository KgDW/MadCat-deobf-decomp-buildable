package me.madcat.mixin.mixins;

import java.awt.Color;
import me.madcat.MadCat;
import me.madcat.features.modules.client.ClickGui;
import me.madcat.features.modules.legacy.LegacyGlow;
import me.madcat.features.modules.render.CrystalChams;
import me.madcat.util.ColorUtil;
import me.madcat.util.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderEnderCrystal;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={RenderEnderCrystal.class})
public class MixinRenderEnderCrystal {
    @Final
    @Shadow
    private static ResourceLocation ENDER_CRYSTAL_TEXTURES;
    @Shadow
    public ModelBase modelEnderCrystal;
    @Shadow
    public ModelBase modelEnderCrystalNoBase;

    @Redirect(method={"doRender(Lnet/minecraft/entity/item/EntityEnderCrystal;DDDFF)V"}, at=@At(value="INVOKE", target="Lnet/minecraft/client/model/ModelBase;render(Lnet/minecraft/entity/Entity;FFFFFF)V"))
    public void renderModelBaseHook(ModelBase model, Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        float newAgeInTicks = 0;
        float newLimbSwingAmount;
        CrystalChams mod = CrystalChams.INSTANCE;
        float f = newLimbSwingAmount = (Boolean)mod.changeSpeed.getValue() != false ? limbSwingAmount * ((Float)mod.spinSpeed.getValue()).floatValue() : limbSwingAmount;
        float f2 = ((Boolean)mod.changeSpeed.getValue()).booleanValue() ? (((Float)mod.floatFactor.getValue()).floatValue() == 0.0f ? 0.15f : ageInTicks * ((Float)mod.floatFactor.getValue()).floatValue()) : (newAgeInTicks = ageInTicks);
        if (mod.isOn()) {
            Color color;
            Color rainbow;
            GlStateManager.scale(((Float)mod.scale.getValue()).floatValue(), ((Float)mod.scale.getValue()).floatValue(), ((Float)mod.scale.getValue()).floatValue());
            if (mod.model.getValue() == CrystalChams.Model.VANILLA) {
                model.render(entity, limbSwing, newLimbSwingAmount, newAgeInTicks, netHeadYaw, headPitch, scale);
            } else if (mod.model.getValue() == CrystalChams.Model.XQZ) {
                GL11.glEnable(32823);
                GlStateManager.enablePolygonOffset();
                GL11.glPolygonOffset(1.0f, -1000000.0f);
                if (((Boolean)mod.modelColor.getValue()).booleanValue()) {
                    rainbow = MadCat.colorManager.getRainbow();
                    color = (Boolean)mod.rainbow.getValue() != false ? new Color(rainbow.getRed(), rainbow.getGreen(), rainbow.getBlue(), (Integer)mod.modelAlpha.getValue()) : new Color((Integer)mod.modelRed.getValue(), (Integer)mod.modelGreen.getValue(), (Integer)mod.modelBlue.getValue(), (Integer)mod.modelAlpha.getValue());
                    RenderUtil.glColor(color);
                }
                model.render(entity, limbSwing, newLimbSwingAmount, newAgeInTicks, netHeadYaw, headPitch, scale);
                GL11.glDisable(32823);
                GlStateManager.disablePolygonOffset();
                GL11.glPolygonOffset(1.0f, 1000000.0f);
            }
            if (((Boolean)mod.wireframe.getValue()).booleanValue()) {
                rainbow = MadCat.colorManager.getRainbow();
                color = (Boolean)mod.rainbow.getValue() != false ? new Color(rainbow.getRed(), rainbow.getGreen(), rainbow.getBlue(), (Integer)mod.alpha.getValue()) : ((Boolean)mod.lineColor.getValue() != false ? new Color((Integer)mod.lineRed.getValue(), (Integer)mod.lineGreen.getValue(), (Integer)mod.lineBlue.getValue(), (Integer)mod.lineAlpha.getValue()) : new Color((Integer)mod.red.getValue(), (Integer)mod.green.getValue(), (Integer)mod.blue.getValue(), (Integer)mod.alpha.getValue()));
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
                GlStateManager.glLineWidth(((Float)mod.lineWidth.getValue()).floatValue());
                model.render(entity, limbSwing, newLimbSwingAmount, newAgeInTicks, netHeadYaw, headPitch, scale);
                GL11.glPopAttrib();
                GL11.glPopMatrix();
            }
            if (((Boolean)mod.fill.getValue()).booleanValue()) {
                rainbow = MadCat.colorManager.getRainbow();
                color = (Boolean)mod.rainbow.getValue() != false ? new Color(rainbow.getRed(), rainbow.getGreen(), rainbow.getBlue(), (Integer)mod.alpha.getValue()) : new Color((Integer)mod.red.getValue(), (Integer)mod.green.getValue(), (Integer)mod.blue.getValue(), (Integer)mod.alpha.getValue());
                GL11.glPushAttrib(1048575);
                GL11.glDisable(3008);
                GL11.glDisable(3553);
                GL11.glDisable(2896);
                GL11.glEnable(3042);
                GL11.glBlendFunc(770, 771);
                GL11.glLineWidth(1.5f);
                GL11.glEnable(2960);
                if (((Boolean)mod.xqz.getValue()).booleanValue()) {
                    GL11.glDisable(2929);
                    GL11.glDepthMask(false);
                }
                GL11.glEnable(10754);
                RenderUtil.glColor(color);
                model.render(entity, limbSwing, newLimbSwingAmount, newAgeInTicks, netHeadYaw, headPitch, scale);
                if (((Boolean)mod.xqz.getValue()).booleanValue()) {
                    GL11.glEnable(2929);
                    GL11.glDepthMask(true);
                }
                GL11.glEnable(3042);
                GL11.glEnable(2896);
                GL11.glEnable(3553);
                GL11.glEnable(3008);
                GL11.glPopAttrib();
            }
            if (((Boolean)mod.glint.getValue()).booleanValue() && entity instanceof EntityEnderCrystal) {
                rainbow = MadCat.colorManager.getRainbow();
                color = (Boolean)mod.rainbow.getValue() != false ? new Color(rainbow.getRed(), rainbow.getGreen(), rainbow.getBlue(), (Integer)mod.alpha.getValue()) : new Color((Integer)mod.red.getValue(), (Integer)mod.green.getValue(), (Integer)mod.blue.getValue(), (Integer)mod.alpha.getValue());
                GL11.glPushMatrix();
                GL11.glPushAttrib(1048575);
                GL11.glPolygonMode(1032, 6914);
                GL11.glDisable(2896);
                GL11.glDepthRange(0.0, 0.1);
                GL11.glEnable(3042);
                RenderUtil.glColor(color);
                GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_COLOR, GlStateManager.DestFactor.ONE);
                float f3 = (float)entity.ticksExisted + Minecraft.getMinecraft().getRenderPartialTicks();
                Minecraft.getMinecraft().getRenderManager().renderEngine.bindTexture(new ResourceLocation("textures/misc/enchanted_item_glint.png"));
                for (int i = 0; i < 2; ++i) {
                    GlStateManager.matrixMode(5890);
                    GlStateManager.loadIdentity();
                    GL11.glScalef(1.0f, 1.0f, 1.0f);
                    GlStateManager.rotate(30.0f - (float)i * 60.0f, 0.0f, 0.0f, 1.0f);
                    GlStateManager.translate(0.0f, f3 * (0.001f + (float)i * 0.003f) * 20.0f, 0.0f);
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
            GlStateManager.scale(1.0f / ((Float)mod.scale.getValue()).floatValue(), 1.0f / ((Float)mod.scale.getValue()).floatValue(), 1.0f / ((Float)mod.scale.getValue()).floatValue());
        } else {
            model.render(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
        }
    }

    @Inject(method={"doRender(Lnet/minecraft/entity/item/EntityEnderCrystal;DDDFF)V"}, at={@At(value="RETURN")}, cancellable=true)
    public void IdoRender(EntityEnderCrystal var1, double var2, double var4, double var6, float var8, float var9, CallbackInfo var10) {
        Minecraft mc = Minecraft.getMinecraft();
        mc.gameSettings.fancyGraphics = false;
        if (LegacyGlow.INSTANCE().isEnabled() && ((Boolean)LegacyGlow.INSTANCE().other.getValue()).booleanValue()) {
            float var11 = (float)var1.innerRotation + var9;
            GlStateManager.pushMatrix();
            GlStateManager.translate(var2, var4, var6);
            Minecraft.getMinecraft().getRenderManager().renderEngine.bindTexture(ENDER_CRYSTAL_TEXTURES);
            float var12 = MathHelper.sin(var11 * 0.2f) / 2.0f + 0.5f;
            var12 += var12 * var12;
            GL11.glLineWidth(5.0f);
            float ageInTicks2 = var12 * 0.2f;
            if (CrystalChams.INSTANCE.isEnabled()) {
                float f = ((Boolean)CrystalChams.INSTANCE.changeSpeed.getValue()).booleanValue() ? (((Float)CrystalChams.INSTANCE.floatFactor.getValue()).floatValue() == 0.0f ? 0.15f : var12 * 0.2f * ((Float)CrystalChams.INSTANCE.floatFactor.getValue()).floatValue()) : (ageInTicks2 = var12 * 0.2f);
            }
            if (var1.shouldShowBottom()) {
                this.modelEnderCrystal.render(var1, 0.0f, var11 * 3.0f, ageInTicks2, 0.0f, 0.0f, 0.0625f);
            } else {
                this.modelEnderCrystalNoBase.render(var1, 0.0f, var11 * 3.0f, ageInTicks2, 0.0f, 0.0f, 0.0625f);
            }
            RenderUtil.renderOne(((Float)LegacyGlow.INSTANCE().lineWidth.getValue()).floatValue());
            if (var1.shouldShowBottom()) {
                this.modelEnderCrystal.render(var1, 0.0f, var11 * 3.0f, ageInTicks2, 0.0f, 0.0f, 0.0625f);
            } else {
                this.modelEnderCrystalNoBase.render(var1, 0.0f, var11 * 3.0f, ageInTicks2, 0.0f, 0.0f, 0.0625f);
            }
            RenderUtil.renderTwo();
            if (var1.shouldShowBottom()) {
                this.modelEnderCrystal.render(var1, 0.0f, var11 * 3.0f, ageInTicks2, 0.0f, 0.0f, 0.0625f);
            } else {
                this.modelEnderCrystalNoBase.render(var1, 0.0f, var11 * 3.0f, ageInTicks2, 0.0f, 0.0f, 0.0625f);
            }
            Color color = (Boolean)LegacyGlow.INSTANCE().colorSync.getValue() != false ? new Color(ColorUtil.rainbow((Integer)ClickGui.INSTANCE().rainbowHue.getValue()).getRed(), ColorUtil.rainbow((Integer)ClickGui.INSTANCE().rainbowHue.getValue()).getGreen(), ColorUtil.rainbow((Integer)ClickGui.INSTANCE().rainbowHue.getValue()).getBlue(), (Integer)LegacyGlow.INSTANCE().alpha.getValue()) : new Color((Integer)LegacyGlow.INSTANCE().red.getValue(), (Integer)LegacyGlow.INSTANCE().green.getValue(), (Integer)LegacyGlow.INSTANCE().blue.getValue(), (Integer)LegacyGlow.INSTANCE().alpha.getValue());
            RenderUtil.renderThree();
            RenderUtil.renderFour(color);
            RenderUtil.setColor(color);
            if (var1.shouldShowBottom()) {
                this.modelEnderCrystal.render(var1, 0.0f, var11 * 3.0f, ageInTicks2, 0.0f, 0.0f, 0.0625f);
            } else {
                this.modelEnderCrystalNoBase.render(var1, 0.0f, var11 * 3.0f, ageInTicks2, 0.0f, 0.0f, 0.0625f);
            }
            RenderUtil.renderFive();
            GlStateManager.popMatrix();
        }
    }
}
 