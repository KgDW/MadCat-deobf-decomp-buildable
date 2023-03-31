package me.madcat.util;

import java.awt.Color;
import me.madcat.features.modules.render.PopChams;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.opengl.GL11;

public class TotemPopChams {
    private static final Minecraft mc = Minecraft.getMinecraft();
    final ModelPlayer playerModel;
    final EntityOtherPlayerMP player;
    final Long startTime;
    double alphaFill;
    double alphaLine;

    public static float interpolateRotation(float f, float f2, float f3) {
        float f4;
        for (f4 = f2 - f; f4 < -180.0f; f4 += 360.0f) {
        }
        while (f4 >= 180.0f) {
            f4 -= 360.0f;
        }
        return f + f3 * f4;
    }

    public static void renderEntity(EntityLivingBase entityLivingBase, ModelBase modelBase, float f, float f2, float f3, float f4, float f5, float f6) {
        if (mc.getRenderManager() == null) {
            return;
        }
        float f7 = mc.getRenderPartialTicks();
        double d = entityLivingBase.posX - TotemPopChams.mc.getRenderManager().viewerPosX;
        double d2 = entityLivingBase.posY - TotemPopChams.mc.getRenderManager().viewerPosY;
        double d3 = entityLivingBase.posZ - TotemPopChams.mc.getRenderManager().viewerPosZ;
        GlStateManager.pushMatrix();
        if (entityLivingBase.isSneaking()) {
            d2 -= 0.125;
        }
        float f8 = TotemPopChams.interpolateRotation(entityLivingBase.prevRenderYawOffset, entityLivingBase.renderYawOffset, f7);
        float f9 = TotemPopChams.interpolateRotation(entityLivingBase.prevRotationYawHead, entityLivingBase.rotationYawHead, f7);
        float f10 = f9 - f8;
        float f11 = entityLivingBase.prevRotationPitch + (entityLivingBase.rotationPitch - entityLivingBase.prevRotationPitch) * f7;
        TotemPopChams.renderLivingAt(d, d2, d3);
        float f12 = TotemPopChams.handleRotationFloat(entityLivingBase, f7);
        TotemPopChams.prepareRotations(entityLivingBase);
        float f13 = TotemPopChams.prepareScale(entityLivingBase, f6);
        GlStateManager.enableAlpha();
        modelBase.setLivingAnimations(entityLivingBase, f, f2, f7);
        modelBase.setRotationAngles(f, f2, f12, entityLivingBase.rotationYawHead, entityLivingBase.rotationPitch, f13, entityLivingBase);
        modelBase.render(entityLivingBase, f, f2, f12, entityLivingBase.rotationYawHead, entityLivingBase.rotationPitch, f13);
        GlStateManager.popMatrix();
    }

    public static void renderLivingAt(double d, double d2, double d3) {
        GlStateManager.translate((float)d, (float)d2, (float)d3);
    }

    @SubscribeEvent
    public void onRenderWorld(RenderWorldLastEvent renderWorldLastEvent) {
        if (this.player == null || TotemPopChams.mc.world == null || TotemPopChams.mc.player == null) {
            return;
        }
        GL11.glLineWidth(1.0f);
        Color color = new Color(PopChams.rL.getValue(), PopChams.gL.getValue(), PopChams.bL.getValue(), PopChams.aL.getValue());
        Color color2 = new Color(PopChams.rF.getValue(), PopChams.gF.getValue(), PopChams.bF.getValue(), PopChams.aF.getValue());
        int n = color.getAlpha();
        int n2 = color2.getAlpha();
        long l = System.currentTimeMillis() - this.startTime - PopChams.fadestart.getValue().longValue();
        if (System.currentTimeMillis() - this.startTime > PopChams.fadestart.getValue().longValue()) {
            double d = this.normalize(l, PopChams.fadetime.getValue().doubleValue());
            d = MathHelper.clamp(d, 0.0, 1.0);
            d = -d + 1.0;
            n *= (int)d;
            n2 *= (int)d;
        }
        Color color3 = TotemPopChams.newAlpha(color, n);
        Color color4 = TotemPopChams.newAlpha(color2, n2);
        if (this.player != null && this.playerModel != null) {
            PopChams.prepareGL();
            GL11.glPushAttrib(1048575);
            GL11.glEnable(2881);
            GL11.glEnable(2848);
            if (this.alphaFill > 1.0) {
                this.alphaFill -= PopChams.fadetime.getValue();
            }
            Color color5 = new Color(color4.getRed(), color4.getGreen(), color4.getBlue(), (int)this.alphaFill);
            if (this.alphaLine > 1.0) {
                this.alphaLine -= PopChams.fadetime.getValue();
            }
            Color color6 = new Color(color3.getRed(), color3.getGreen(), color3.getBlue(), (int)this.alphaLine);
            TotemPopChams.glColor(color5);
            GL11.glPolygonMode(1032, 6914);
            TotemPopChams.renderEntity(this.player, this.playerModel, this.player.limbSwing, this.player.limbSwingAmount, this.player.ticksExisted, this.player.rotationYawHead, this.player.rotationPitch, 1.0f);
            TotemPopChams.glColor(color6);
            GL11.glPolygonMode(1032, 6913);
            TotemPopChams.renderEntity(this.player, this.playerModel, this.player.limbSwing, this.player.limbSwingAmount, this.player.ticksExisted, this.player.rotationYawHead, this.player.rotationPitch, 1.0f);
            GL11.glPolygonMode(1032, 6914);
            GL11.glPopAttrib();
            PopChams.releaseGL();
        }
    }

    public static void prepareRotations(EntityLivingBase entityLivingBase) {
        GlStateManager.rotate(180.0f - entityLivingBase.rotationYaw, 0.0f, 1.0f, 0.0f);
    }

    public static Color newAlpha(Color color, int n) {
        return new Color(color.getRed(), color.getGreen(), color.getBlue(), n);
    }

    public static float prepareScale(EntityLivingBase entityLivingBase, float f) {
        GlStateManager.enableRescaleNormal();
        GlStateManager.scale(-1.0f, -1.0f, 1.0f);
        double d = entityLivingBase.getRenderBoundingBox().maxX - entityLivingBase.getRenderBoundingBox().minX;
        double d2 = entityLivingBase.getRenderBoundingBox().maxZ - entityLivingBase.getRenderBoundingBox().minZ;
        GlStateManager.scale((double)f + d, f * entityLivingBase.height, (double)f + d2);
        float f2 = 0.0625f;
        GlStateManager.translate(0.0f, -1.501f, 0.0f);
        return 0.0625f;
    }

    public static void prepareTranslate(EntityLivingBase entityLivingBase, double d, double d2, double d3) {
        TotemPopChams.renderLivingAt(d - TotemPopChams.mc.getRenderManager().viewerPosX, d2 - TotemPopChams.mc.getRenderManager().viewerPosY, d3 - TotemPopChams.mc.getRenderManager().viewerPosZ);
    }

    public static float handleRotationFloat(EntityLivingBase entityLivingBase, float f) {
        return 0.0f;
    }

    public TotemPopChams(EntityOtherPlayerMP entityOtherPlayerMP, ModelPlayer modelPlayer, Long l, double d, double d2) {
        MinecraftForge.EVENT_BUS.register(this);
        this.player = entityOtherPlayerMP;
        this.playerModel = modelPlayer;
        this.startTime = l;
        this.alphaFill = d;
        this.alphaLine = d;
    }

    public static void glColor(Color color) {
        GL11.glColor4f((float)color.getRed() / 255.0f, (float)color.getGreen() / 255.0f, (float)color.getBlue() / 255.0f, (float)color.getAlpha() / 255.0f);
    }

    double normalize(double d, double d2) {
        return (d - 0.0) / (d2 - 0.0);
    }
}

