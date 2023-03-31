package me.madcat.util;

import java.awt.Color;
import java.text.NumberFormat;
import me.madcat.util.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.opengl.GL11;

public class DrawHelper {
    private static final Minecraft mc = Minecraft.getMinecraft();
    public static final Frustum frustum = new Frustum();

    public static int fade(int n, int n2, float f) {
        float f2 = 1.0f - f;
        int n3 = (int)((float)(n >> 16 & 0xFF) * f2 + (float)(n2 >> 16 & 0xFF) * f);
        int n4 = (int)((float)(n >> 8 & 0xFF) * f2 + (float)(n2 >> 8 & 0xFF) * f);
        int n5 = (int)((float)(n & 0xFF) * f2 + (float)(n2 & 0xFF) * f);
        int n6 = (int)((float)(n >> 24 & 0xFF) * f2 + (float)(n2 >> 24 & 0xFF) * f);
        return (n6 & 0xFF) << 24 | (n3 & 0xFF) << 16 | (n4 & 0xFF) << 8 | n5 & 0xFF;
    }

    private static boolean isInViewFrustum(AxisAlignedBB axisAlignedBB) {
        Entity entity = mc.getRenderViewEntity();
        if (entity != null) {
            frustum.setPosition(entity.posX, entity.posY, entity.posZ);
        }
        return frustum.isBoundingBoxInFrustum(axisAlignedBB);
    }

    public static void disableGL2D() {
        GL11.glEnable(3553);
        GL11.glEnable(2929);
        GL11.glDisable(2848);
        GL11.glHint(3154, 4352);
        GL11.glHint(3155, 4352);
    }

    public static int getColor(int n, int n2, int n3) {
        return DrawHelper.getColor(n, n2, n3, 255);
    }

    public static void drawNewRect(double d, double d2, double d3, double d4, int n) {
        double d5;
        if (d < d3) {
            d5 = d;
            d = d3;
            d3 = d5;
        }
        if (d2 < d4) {
            d5 = d2;
            d2 = d4;
            d4 = d5;
        }
        float f = (float)(n >> 24 & 0xFF) / 255.0f;
        float f2 = (float)(n >> 16 & 0xFF) / 255.0f;
        float f3 = (float)(n >> 8 & 0xFF) / 255.0f;
        float f4 = (float)(n & 0xFF) / 255.0f;
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferBuilder = tessellator.getBuffer();
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.color(f2, f3, f4, f);
        bufferBuilder.begin(7, DefaultVertexFormats.POSITION);
        bufferBuilder.pos(d, d4, 0.0).endVertex();
        bufferBuilder.pos(d3, d4, 0.0).endVertex();
        bufferBuilder.pos(d3, d2, 0.0).endVertex();
        bufferBuilder.pos(d, d2, 0.0).endVertex();
        tessellator.draw();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }

    public static void drawColorBox(AxisAlignedBB axisAlignedBB, float f, float f2, float f3, float f4) {
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferBuilder = tessellator.getBuffer();
        bufferBuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
        bufferBuilder.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ).color(f, f2, f3, f4).endVertex();
        bufferBuilder.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.minZ).color(f, f2, f3, f4).endVertex();
        bufferBuilder.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.minZ).color(f, f2, f3, f4).endVertex();
        bufferBuilder.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.minZ).color(f, f2, f3, f4).endVertex();
        bufferBuilder.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.maxZ).color(f, f2, f3, f4).endVertex();
        bufferBuilder.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.maxZ).color(f, f2, f3, f4).endVertex();
        bufferBuilder.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.maxZ).color(f, f2, f3, f4).endVertex();
        bufferBuilder.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.maxZ).color(f, f2, f3, f4).endVertex();
        tessellator.draw();
        bufferBuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
        bufferBuilder.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.minZ).color(f, f2, f3, f4).endVertex();
        bufferBuilder.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.minZ).color(f, f2, f3, f4).endVertex();
        bufferBuilder.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.minZ).color(f, f2, f3, f4).endVertex();
        bufferBuilder.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ).color(f, f2, f3, f4).endVertex();
        bufferBuilder.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.maxZ).color(f, f2, f3, f4).endVertex();
        bufferBuilder.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.maxZ).color(f, f2, f3, f4).endVertex();
        bufferBuilder.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.maxZ).color(f, f2, f3, f4).endVertex();
        bufferBuilder.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.maxZ).color(f, f2, f3, f4).endVertex();
        tessellator.draw();
        bufferBuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
        bufferBuilder.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.minZ).color(f, f2, f3, f4).endVertex();
        bufferBuilder.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.minZ).color(f, f2, f3, f4).endVertex();
        bufferBuilder.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.maxZ).color(f, f2, f3, f4).endVertex();
        bufferBuilder.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.maxZ).color(f, f2, f3, f4).endVertex();
        bufferBuilder.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.minZ).color(f, f2, f3, f4).endVertex();
        bufferBuilder.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.maxZ).color(f, f2, f3, f4).endVertex();
        bufferBuilder.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.maxZ).color(f, f2, f3, f4).endVertex();
        bufferBuilder.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.minZ).color(f, f2, f3, f4).endVertex();
        tessellator.draw();
        bufferBuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
        bufferBuilder.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ).color(f, f2, f3, f4).endVertex();
        bufferBuilder.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.minZ).color(f, f2, f3, f4).endVertex();
        bufferBuilder.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.maxZ).color(f, f2, f3, f4).endVertex();
        bufferBuilder.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.maxZ).color(f, f2, f3, f4).endVertex();
        bufferBuilder.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ).color(f, f2, f3, f4).endVertex();
        bufferBuilder.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.maxZ).color(f, f2, f3, f4).endVertex();
        bufferBuilder.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.maxZ).color(f, f2, f3, f4).endVertex();
        bufferBuilder.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.minZ).color(f, f2, f3, f4).endVertex();
        tessellator.draw();
        bufferBuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
        bufferBuilder.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ).color(f, f2, f3, f4).endVertex();
        bufferBuilder.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.minZ).color(f, f2, f3, f4).endVertex();
        bufferBuilder.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.maxZ).color(f, f2, f3, f4).endVertex();
        bufferBuilder.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.maxZ).color(f, f2, f3, f4).endVertex();
        bufferBuilder.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.maxZ).color(f, f2, f3, f4).endVertex();
        bufferBuilder.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.maxZ).color(f, f2, f3, f4).endVertex();
        bufferBuilder.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.minZ).color(f, f2, f3, f4).endVertex();
        bufferBuilder.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.minZ).color(f, f2, f3, f4).endVertex();
        tessellator.draw();
        bufferBuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
        bufferBuilder.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.maxZ).color(f, f2, f3, f4).endVertex();
        bufferBuilder.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.maxZ).color(f, f2, f3, f4).endVertex();
        bufferBuilder.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.minZ).color(f, f2, f3, f4).endVertex();
        bufferBuilder.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ).color(f, f2, f3, f4).endVertex();
        bufferBuilder.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.minZ).color(f, f2, f3, f4).endVertex();
        bufferBuilder.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.minZ).color(f, f2, f3, f4).endVertex();
        bufferBuilder.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.maxZ).color(f, f2, f3, f4).endVertex();
        bufferBuilder.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.maxZ).color(f, f2, f3, f4).endVertex();
        tessellator.draw();
    }

    public static void drawPolygonPart(double d, double d2, int n, int n2, int n3, int n4) {
        float f = (float)(n3 >> 24 & 0xFF) / 255.0f;
        float f2 = (float)(n3 >> 16 & 0xFF) / 255.0f;
        float f3 = (float)(n3 >> 8 & 0xFF) / 255.0f;
        float f4 = (float)(n3 & 0xFF) / 255.0f;
        float f5 = (float)(n4 >> 24 & 0xFF) / 255.0f;
        float f6 = (float)(n4 >> 16 & 0xFF) / 255.0f;
        float f7 = (float)(n4 >> 8 & 0xFF) / 255.0f;
        float f8 = (float)(n4 & 0xFF) / 255.0f;
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.shadeModel(7425);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferBuilder = tessellator.getBuffer();
        bufferBuilder.begin(6, DefaultVertexFormats.POSITION_COLOR);
        bufferBuilder.pos(d, d2, 0.0).color(f2, f3, f4, f).endVertex();
        for (int i = n2 * 90; i <= n2 * 90 + 90; ++i) {
            double d3 = Math.PI * 2 * (double)i / 360.0 + Math.toRadians(180.0);
            bufferBuilder.pos(d + Math.sin(d3) * (double)n, d2 + Math.cos(d3) * (double)n, 0.0).color(f6, f7, f8, f5).endVertex();
        }
        tessellator.draw();
        GlStateManager.shadeModel(7424);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
    }

    public static void disableSmoothLine() {
        GL11.glEnable(3553);
        GL11.glEnable(2929);
        GL11.glDisable(3042);
        GL11.glEnable(3008);
        GL11.glDepthMask(true);
        GL11.glCullFace(1029);
        GL11.glDisable(2848);
        GL11.glHint(3154, 4352);
        GL11.glHint(3155, 4352);
    }

    public static Color astolfo(boolean bl, int n) {
        float f = bl ? 100.0f : 1000.0f;
        float f2 = System.currentTimeMillis() % (long)((int)f) + (long)n;
        if (f2 > f) {
            f2 -= f;
        }
        if ((f2 /= f) > 0.5f) {
            f2 = 0.5f - (f2 - 0.5f);
        }
        return Color.getHSBColor(f2 + 0.5f, 0.4f, 1.0f);
    }

    public static Color rainbow(int n, float f, float f2) {
        double d = Math.ceil((System.currentTimeMillis() + (long)n) / 16L);
        return Color.getHSBColor((float)(d % 360.0 / 360.0), f, f2);
    }

    public static Color fade(Color color, int n, int n2) {
        float[] fArray = new float[3];
        Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), fArray);
        float f = Math.abs(((float)(System.currentTimeMillis() % 2000L) / 1000.0f + (float)n / (float)n2 * 2.0f) % 2.0f - 1.0f);
        f = 0.5f + 0.5f * f;
        fArray[2] = f % 2.0f;
        return new Color(Color.HSBtoRGB(fArray[0], fArray[1], fArray[2]));
    }

    public static int getColor(Color color) {
        return DrawHelper.getColor(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
    }

    public static void color(double d, double d2, double d3, double d4) {
        GL11.glColor4d(d, d2, d3, d4);
    }

    public static Color setAlpha(Color color, int n) {
        n = MathHelper.clamp(n, 0, 255);
        return new Color(color.getRed(), color.getGreen(), color.getBlue(), n);
    }

    public static int getColor(int n) {
        return DrawHelper.getColor(n, n, n, 255);
    }

    public static void drawSelectionBoundingBox(AxisAlignedBB axisAlignedBB) {
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferBuilder = tessellator.getBuffer();
        bufferBuilder.begin(3, DefaultVertexFormats.POSITION);
        bufferBuilder.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ).endVertex();
        bufferBuilder.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.minZ).endVertex();
        bufferBuilder.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.maxZ).endVertex();
        bufferBuilder.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.maxZ).endVertex();
        bufferBuilder.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ).endVertex();
        tessellator.draw();
        bufferBuilder.begin(3, DefaultVertexFormats.POSITION);
        bufferBuilder.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.minZ).endVertex();
        bufferBuilder.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.minZ).endVertex();
        bufferBuilder.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.maxZ).endVertex();
        bufferBuilder.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.maxZ).endVertex();
        bufferBuilder.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.minZ).endVertex();
        tessellator.draw();
        bufferBuilder.begin(1, DefaultVertexFormats.POSITION);
        bufferBuilder.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ).endVertex();
        bufferBuilder.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.minZ).endVertex();
        bufferBuilder.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.minZ).endVertex();
        bufferBuilder.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.minZ).endVertex();
        bufferBuilder.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.maxZ).endVertex();
        bufferBuilder.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.maxZ).endVertex();
        bufferBuilder.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.maxZ).endVertex();
        bufferBuilder.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.maxZ).endVertex();
        tessellator.draw();
    }

    public static void drawRectWithGlow(double d, double d2, double d3, double d4, double d5, double d6, Color color) {
        float f = 1.0f;
        while ((double)f < d5) {
            DrawHelper.drawRoundedRect99(d - (d5 - (double)f), d2 - (d5 - (double)f), d3 + (d5 - (double)f), d4 + (d5 - (double)f), DrawHelper.injectAlpha(color, (int)Math.round((double)f * d6)).getRGB());
            f += 0.5f;
        }
    }

    public static void enableSmoothLine(float f) {
        GL11.glDisable(3008);
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glDisable(3553);
        GL11.glDisable(2929);
        GL11.glDepthMask(false);
        GL11.glEnable(2884);
        GL11.glEnable(2848);
        GL11.glHint(3154, 4354);
        GL11.glHint(3155, 4354);
        GL11.glLineWidth(f);
    }

    public static void drawSmoothRect(double d, double d2, double d3, double d4, int n) {
        GL11.glEnable(3042);
        GL11.glEnable(2848);
        DrawHelper.drawRect(d, d2, d3, d4, n);
        GL11.glScalef(0.5f, 0.5f, 0.5f);
        DrawHelper.drawRect(d * 2.0 - 1.0, d2 * 2.0, d * 2.0, d4 * 2.0 - 1.0, n);
        DrawHelper.drawRect(d * 2.0, d2 * 2.0 - 1.0, d3 * 2.0, d2 * 2.0, n);
        DrawHelper.drawRect(d3 * 2.0, d2 * 2.0, d3 * 2.0 + 1.0, d4 * 2.0 - 1.0, n);
        DrawHelper.drawRect(d * 2.0, d4 * 2.0 - 1.0, d3 * 2.0, d4 * 2.0, n);
        GL11.glDisable(3042);
        GL11.glScalef(2.0f, 2.0f, 2.0f);
    }

    public static void drawVGradientRect(float f, float f2, float f3, float f4, int n, int n2) {
        float f5 = (float)(n >> 24 & 0xFF) / 255.0f;
        float f6 = (float)(n >> 16 & 0xFF) / 255.0f;
        float f7 = (float)(n >> 8 & 0xFF) / 255.0f;
        float f8 = (float)(n & 0xFF) / 255.0f;
        float f9 = (float)(n2 >> 24 & 0xFF) / 255.0f;
        float f10 = (float)(n2 >> 16 & 0xFF) / 255.0f;
        float f11 = (float)(n2 >> 8 & 0xFF) / 255.0f;
        float f12 = (float)(n2 & 0xFF) / 255.0f;
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.shadeModel(7425);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferBuilder = tessellator.getBuffer();
        bufferBuilder.begin(7, DefaultVertexFormats.POSITION_COLOR);
        bufferBuilder.pos(f3, f2, 0.0).color(f6, f7, f8, f5).endVertex();
        bufferBuilder.pos(f, f2, 0.0).color(f6, f7, f8, f5).endVertex();
        bufferBuilder.pos(f, f4, 0.0).color(f10, f11, f12, f9).endVertex();
        bufferBuilder.pos(f3, f4, 0.0).color(f10, f11, f12, f9).endVertex();
        tessellator.draw();
        GlStateManager.shadeModel(7424);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
    }

    public static void drawEntityBox(Entity entity, Color color, boolean bl, float f) {
        GlStateManager.pushMatrix();
        GlStateManager.blendFunc(770, 771);
        GL11.glEnable(3042);
        GlStateManager.glLineWidth(2.0f);
        GlStateManager.disableTexture2D();
        GL11.glDisable(2929);
        GlStateManager.depthMask(false);
        AxisAlignedBB axisAlignedBB = entity.getEntityBoundingBox();
        GlStateManager.glLineWidth(2.0f);
        GL11.glEnable(2848);
        GlStateManager.color((float)color.getRed() / 255.0f, (float)color.getGreen() / 255.0f, (float)color.getBlue() / 255.0f, f);
        GlStateManager.glLineWidth(2.0f);
        GlStateManager.enableTexture2D();
        GL11.glEnable(2929);
        GlStateManager.depthMask(true);
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
    }

    public static void setColor(int n) {
        GL11.glColor4ub((byte)(n >> 16 & 0xFF), (byte)(n >> 8 & 0xFF), (byte)(n & 0xFF), (byte)(n >> 24 & 0xFF));
    }

    public static Color injectAlpha(Color color, int n) {
        int n2 = MathHelper.clamp(n, 0, 255);
        return new Color(color.getRed(), color.getGreen(), color.getBlue(), n2);
    }

    public static int color(int n, int n2, int n3) {
        int n4 = 255;
        return new Color(n, n2, n3, n4).getRGB();
    }

    public static void disableStandardItemLighting() {
        GlStateManager.disableLighting();
        GlStateManager.disableLight(0);
        GlStateManager.disableLight(1);
        GlStateManager.disableColorMaterial();
    }

    public static Color blend(Color color, Color color2, double d) {
        float f = (float)d;
        float f2 = 1.0f - f;
        float[] fArray = new float[3];
        float[] fArray2 = new float[3];
        color.getColorComponents(fArray);
        color2.getColorComponents(fArray2);
        float f3 = fArray[0] * f + fArray2[0] * f2;
        float f4 = fArray[1] * f + fArray2[1] * f2;
        float f5 = fArray[2] * f + fArray2[2] * f2;
        if (f3 < 0.0f) {
            f3 = 0.0f;
        } else if (f3 > 255.0f) {
            f3 = 255.0f;
        }
        if (f4 < 0.0f) {
            f4 = 0.0f;
        } else if (f4 > 255.0f) {
            f4 = 255.0f;
        }
        if (f5 < 0.0f) {
            f5 = 0.0f;
        } else if (f5 > 255.0f) {
            f5 = 255.0f;
        }
        Color color3 = null;
        try {
            color3 = new Color(f3, f4, f5);
        }
        catch (IllegalArgumentException illegalArgumentException) {
            NumberFormat numberFormat = NumberFormat.getNumberInstance();
        }
        return color3;
    }

    public static void enableGL2D() {
        GL11.glDisable(2929);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glDepthMask(true);
        GL11.glEnable(2848);
        GL11.glHint(3154, 4354);
        GL11.glHint(3155, 4354);
    }

    public static void drawGlow(double d, double d2, double d3, double d4, int n) {
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.shadeModel(7425);
        DrawHelper.drawVGradientRect((int)d, (int)d2, (int)d3, (int)(d2 + (d4 - d2) / 2.0), DrawHelper.setAlpha(new Color(n), 0).getRGB(), n);
        DrawHelper.drawVGradientRect((int)d, (int)(d2 + (d4 - d2) / 2.0), (int)d3, (int)d4, n, DrawHelper.setAlpha(new Color(n), 0).getRGB());
        int n2 = (int)((d4 - d2) / 2.0);
        DrawHelper.drawPolygonPart(d, d2 + (d4 - d2) / 2.0, n2, 0, n, DrawHelper.setAlpha(new Color(n), 0).getRGB());
        DrawHelper.drawPolygonPart(d, d2 + (d4 - d2) / 2.0, n2, 1, n, DrawHelper.setAlpha(new Color(n), 0).getRGB());
        DrawHelper.drawPolygonPart(d3, d2 + (d4 - d2) / 2.0, n2, 2, n, DrawHelper.setAlpha(new Color(n), 0).getRGB());
        DrawHelper.drawPolygonPart(d3, d2 + (d4 - d2) / 2.0, n2, 3, n, DrawHelper.setAlpha(new Color(n), 0).getRGB());
        GlStateManager.shadeModel(7424);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
    }

    public static void drawRoundedRect99(double d, double d2, double d3, double d4, int n) {
        RenderUtil.drawRect((float)(d + 0.5), (float)d2, (float)d3 - 0.5f, (float)d2 + 0.5f, n);
        RenderUtil.drawRect((float)(d + 0.5), (float)d4 - 0.5f, (float)d3 - 0.5f, (float)d4, n);
        RenderUtil.drawRect((float)d, (float)d2 + 0.5f, (float)d3, (float)d4 - 0.5f, n);
    }

    public static boolean isInViewFrustum(Entity entity) {
        return DrawHelper.isInViewFrustum(entity.getEntityBoundingBox()) || entity.ignoreFrustumCheck;
    }

    public static int getColor(int n, int n2, int n3, int n4) {
        int n5 = 0;
        n5 |= n4 << 24;
        n5 |= n << 16;
        n5 |= n2 << 8;
        return n5 | n3;
    }

    public static void drawRect(double d, double d2, double d3, double d4, int n) {
        double d5;
        if (d < d3) {
            d5 = d;
            d = d3;
            d3 = d5;
        }
        if (d2 < d4) {
            d5 = d2;
            d2 = d4;
            d4 = d5;
        }
        float f = (float)(n >> 24 & 0xFF) / 255.0f;
        float f2 = (float)(n >> 16 & 0xFF) / 255.0f;
        float f3 = (float)(n >> 8 & 0xFF) / 255.0f;
        float f4 = (float)(n & 0xFF) / 255.0f;
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferBuilder = tessellator.getBuffer();
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.color(f2, f3, f4, f);
        bufferBuilder.begin(7, DefaultVertexFormats.POSITION);
        bufferBuilder.pos(d, d4, 0.0).endVertex();
        bufferBuilder.pos(d3, d4, 0.0).endVertex();
        bufferBuilder.pos(d3, d2, 0.0).endVertex();
        bufferBuilder.pos(d, d2, 0.0).endVertex();
        tessellator.draw();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }

    public static int getColor(int n, int n2) {
        return DrawHelper.getColor(n, n, n, n2);
    }

    public static int astolfo(int n, int n2) {
        float f;
        float f2 = 1000.0f;
        for (f = (float)(System.currentTimeMillis() % (long)((int)f2) + (long)(n - n2) * 9L); f > f2; f -= f2) {
        }
        if ((double)(f /= f2) > 0.5) {
            f = 0.5f - (f - 0.5f);
        }
        return Color.HSBtoRGB(f + 0.5f, 0.6f, 1.0f);
    }

    public static void drawTriangle(float f, float f2, int n) {
        float f3 = f2 * 3.0f;
        float f4 = 0.0f;
        GL11.glPushMatrix();
        DrawHelper.enableGL2D();
        GL11.glLineWidth(2.0f);
        GL11.glScalef(0.5f, 0.5f, 0.5f);
        DrawHelper.setColor(n);
        GL11.glDisable(2929);
        GL11.glDepthMask(false);
        DrawHelper.enableSmoothLine(1.5f);
        GL11.glBegin(2);
        for (int i = 0; i < 3; ++i) {
            GL11.glVertex2f(f3 + f, f4);
            float f5 = f3;
            f3 = -0.4999999f * f3 - 0.8660254f * f4;
            f4 = 0.8660254f * f5 - 0.4999999f * f4;
        }
        GL11.glEnd();
        GL11.glScalef(4.0f, 4.0f, 4.0f);
        DrawHelper.disableSmoothLine();
        DrawHelper.disableGL2D();
        GL11.glEnable(2929);
        GL11.glDepthMask(true);
        GL11.glPopMatrix();
    }

    public static int getHealthColor(float f, float f2) {
        return Color.HSBtoRGB(Math.max(0.0f, Math.min(f, f2) / f2) / 3.0f, 1.0f, 0.8f) | 0xFF000000;
    }
}

