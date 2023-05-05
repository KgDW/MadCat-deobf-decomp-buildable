package me.madcat.util;

import java.awt.Color;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.Objects;
import me.madcat.MadCat;
import me.madcat.util.ColorUtil;
import me.madcat.util.EntityUtil;
import me.madcat.util.Wrapper;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.EXTFramebufferObject;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

public class RenderUtil
implements Wrapper {
    private static boolean bind;
    private static boolean clean;
    private static final FloatBuffer projection;
    private static final IntBuffer viewport;
    static final boolean $assertionsDisabled;
    public static final ICamera camera;
    private static boolean override;
    private static final FloatBuffer modelView;
    private static final Frustum frustrum;
    public static final RenderItem itemRender;
    private static boolean texture;
    private static final FloatBuffer screenCoords;
    private static boolean depth;

    public static void drawSexyBoxPhobosIsRetardedFuckYouESP(AxisAlignedBB axisAlignedBB, Color color, Color color2, float f, boolean bl, boolean bl2, float f2, float f3, float f4) {
        double d = 0.5 * (double)(1.0f - f3);
        AxisAlignedBB axisAlignedBB2 = RenderUtil.interpolateAxis(new AxisAlignedBB(axisAlignedBB.minX + d, axisAlignedBB.minY + d + (double)(1.0f - f4), axisAlignedBB.minZ + d, axisAlignedBB.maxX - d, axisAlignedBB.maxY - d, axisAlignedBB.maxZ - d));
        float f5 = (float)color.getRed() / 255.0f;
        float f6 = (float)color.getGreen() / 255.0f;
        float f7 = (float)color.getBlue() / 255.0f;
        float f8 = (float)color.getAlpha() / 255.0f;
        float f9 = (float)color2.getRed() / 255.0f;
        float f10 = (float)color2.getGreen() / 255.0f;
        float f11 = (float)color2.getBlue() / 255.0f;
        float f12 = (float)color2.getAlpha() / 255.0f;
        if (f2 > 1.0f) {
            f2 = 1.0f;
        }
        f8 *= f2;
        f12 *= f2;
        if (bl2) {
            GlStateManager.pushMatrix();
            GlStateManager.enableBlend();
            GlStateManager.disableDepth();
            GlStateManager.tryBlendFuncSeparate(770, 771, 0, 1);
            GlStateManager.disableTexture2D();
            GlStateManager.depthMask(false);
            GL11.glEnable(2848);
            GL11.glHint(3154, 4354);
            RenderGlobal.renderFilledBox(axisAlignedBB2, f5, f6, f7, f8);
            GL11.glDisable(2848);
            GlStateManager.depthMask(true);
            GlStateManager.enableDepth();
            GlStateManager.enableTexture2D();
            GlStateManager.disableBlend();
            GlStateManager.popMatrix();
        }
        if (bl) {
            GlStateManager.pushMatrix();
            GlStateManager.enableBlend();
            GlStateManager.disableDepth();
            GlStateManager.tryBlendFuncSeparate(770, 771, 0, 1);
            GlStateManager.disableTexture2D();
            GlStateManager.depthMask(false);
            GL11.glEnable(2848);
            GL11.glHint(3154, 4354);
            GL11.glLineWidth(f);
            Tessellator tessellator = Tessellator.getInstance();
            BufferBuilder bufferBuilder = tessellator.getBuffer();
            bufferBuilder.begin(3, DefaultVertexFormats.POSITION_COLOR);
            bufferBuilder.pos(axisAlignedBB2.minX, axisAlignedBB2.minY, axisAlignedBB2.minZ).color(f9, f10, f11, f12).endVertex();
            bufferBuilder.pos(axisAlignedBB2.minX, axisAlignedBB2.minY, axisAlignedBB2.maxZ).color(f9, f10, f11, f12).endVertex();
            bufferBuilder.pos(axisAlignedBB2.maxX, axisAlignedBB2.minY, axisAlignedBB2.maxZ).color(f9, f10, f11, f12).endVertex();
            bufferBuilder.pos(axisAlignedBB2.maxX, axisAlignedBB2.minY, axisAlignedBB2.minZ).color(f9, f10, f11, f12).endVertex();
            bufferBuilder.pos(axisAlignedBB2.minX, axisAlignedBB2.minY, axisAlignedBB2.minZ).color(f9, f10, f11, f12).endVertex();
            bufferBuilder.pos(axisAlignedBB2.minX, axisAlignedBB2.maxY, axisAlignedBB2.minZ).color(f9, f10, f11, f12).endVertex();
            bufferBuilder.pos(axisAlignedBB2.minX, axisAlignedBB2.maxY, axisAlignedBB2.maxZ).color(f9, f10, f11, f12).endVertex();
            bufferBuilder.pos(axisAlignedBB2.minX, axisAlignedBB2.minY, axisAlignedBB2.maxZ).color(f9, f10, f11, f12).endVertex();
            bufferBuilder.pos(axisAlignedBB2.maxX, axisAlignedBB2.minY, axisAlignedBB2.maxZ).color(f9, f10, f11, f12).endVertex();
            bufferBuilder.pos(axisAlignedBB2.maxX, axisAlignedBB2.maxY, axisAlignedBB2.maxZ).color(f9, f10, f11, f12).endVertex();
            bufferBuilder.pos(axisAlignedBB2.minX, axisAlignedBB2.maxY, axisAlignedBB2.maxZ).color(f9, f10, f11, f12).endVertex();
            bufferBuilder.pos(axisAlignedBB2.maxX, axisAlignedBB2.maxY, axisAlignedBB2.maxZ).color(f9, f10, f11, f12).endVertex();
            bufferBuilder.pos(axisAlignedBB2.maxX, axisAlignedBB2.maxY, axisAlignedBB2.minZ).color(f9, f10, f11, f12).endVertex();
            bufferBuilder.pos(axisAlignedBB2.maxX, axisAlignedBB2.minY, axisAlignedBB2.minZ).color(f9, f10, f11, f12).endVertex();
            bufferBuilder.pos(axisAlignedBB2.maxX, axisAlignedBB2.maxY, axisAlignedBB2.minZ).color(f9, f10, f11, f12).endVertex();
            bufferBuilder.pos(axisAlignedBB2.minX, axisAlignedBB2.maxY, axisAlignedBB2.minZ).color(f9, f10, f11, f12).endVertex();
            tessellator.draw();
            GL11.glDisable(2848);
            GlStateManager.depthMask(true);
            GlStateManager.enableDepth();
            GlStateManager.enableTexture2D();
            GlStateManager.disableBlend();
            GlStateManager.popMatrix();
        }
    }

    public static boolean isInViewFrustrum(Entity entity) {
        return RenderUtil.isInViewFrustrum(entity.getEntityBoundingBox()) || entity.ignoreFrustumCheck;
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

    public static void prepareGL3D() {
        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.disableDepth();
        GlStateManager.disableAlpha();
        GlStateManager.disableCull();
        GL11.glEnable(2848);
        GL11.glHint(3154, 4354);
        GlStateManager.shadeModel(7425);
        GlStateManager.disableTexture2D();
        GlStateManager.disableLighting();
        GlStateManager.depthMask(false);
    }

    private static void GLPost(boolean bl, boolean bl2, boolean bl3, boolean bl4, boolean bl5) {
        GlStateManager.depthMask(true);
        if (!bl5) {
            GL11.glDisable(2848);
        }
        if (bl4) {
            GL11.glEnable(2929);
        }
        if (bl3) {
            GL11.glEnable(3553);
        }
        if (!bl2) {
            GL11.glDisable(3042);
        }
        if (bl) {
            GL11.glEnable(2896);
        }
    }

    public static void drawGradientRect(int n, int n2, int n3, int n4, int n5, int n6) {
        float f = (float)(n5 >> 24 & 0xFF) / 255.0f;
        float f2 = (float)(n5 >> 16 & 0xFF) / 255.0f;
        float f3 = (float)(n5 >> 8 & 0xFF) / 255.0f;
        float f4 = (float)(n5 & 0xFF) / 255.0f;
        float f5 = (float)(n6 >> 24 & 0xFF) / 255.0f;
        float f6 = (float)(n6 >> 16 & 0xFF) / 255.0f;
        float f7 = (float)(n6 >> 8 & 0xFF) / 255.0f;
        float f8 = (float)(n6 & 0xFF) / 255.0f;
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.shadeModel(7425);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferBuilder = tessellator.getBuffer();
        bufferBuilder.begin(7, DefaultVertexFormats.POSITION_COLOR);
        bufferBuilder.pos((double)n + (double)n3, n2, 0.0).color(f2, f3, f4, f).endVertex();
        bufferBuilder.pos(n, n2, 0.0).color(f2, f3, f4, f).endVertex();
        bufferBuilder.pos(n, (double)n2 + (double)n4, 0.0).color(f6, f7, f8, f5).endVertex();
        bufferBuilder.pos((double)n + (double)n3, (double)n2 + (double)n4, 0.0).color(f6, f7, f8, f5).endVertex();
        tessellator.draw();
        GlStateManager.shadeModel(7424);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
    }

    public static void drawFilledBox(AxisAlignedBB axisAlignedBB, int n) {
        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        GlStateManager.disableDepth();
        GlStateManager.tryBlendFuncSeparate(770, 771, 0, 1);
        GlStateManager.disableTexture2D();
        GlStateManager.depthMask(false);
        float f = (float)(n >> 24 & 0xFF) / 255.0f;
        float f2 = (float)(n >> 16 & 0xFF) / 255.0f;
        float f3 = (float)(n >> 8 & 0xFF) / 255.0f;
        float f4 = (float)(n & 0xFF) / 255.0f;
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferBuilder = tessellator.getBuffer();
        bufferBuilder.begin(7, DefaultVertexFormats.POSITION_COLOR);
        bufferBuilder.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ).color(f2, f3, f4, f).endVertex();
        bufferBuilder.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.minZ).color(f2, f3, f4, f).endVertex();
        bufferBuilder.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.maxZ).color(f2, f3, f4, f).endVertex();
        bufferBuilder.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.maxZ).color(f2, f3, f4, f).endVertex();
        bufferBuilder.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.minZ).color(f2, f3, f4, f).endVertex();
        bufferBuilder.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.maxZ).color(f2, f3, f4, f).endVertex();
        bufferBuilder.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.maxZ).color(f2, f3, f4, f).endVertex();
        bufferBuilder.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.minZ).color(f2, f3, f4, f).endVertex();
        bufferBuilder.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ).color(f2, f3, f4, f).endVertex();
        bufferBuilder.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.minZ).color(f2, f3, f4, f).endVertex();
        bufferBuilder.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.minZ).color(f2, f3, f4, f).endVertex();
        bufferBuilder.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.minZ).color(f2, f3, f4, f).endVertex();
        bufferBuilder.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.minZ).color(f2, f3, f4, f).endVertex();
        bufferBuilder.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.minZ).color(f2, f3, f4, f).endVertex();
        bufferBuilder.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.maxZ).color(f2, f3, f4, f).endVertex();
        bufferBuilder.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.maxZ).color(f2, f3, f4, f).endVertex();
        bufferBuilder.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.maxZ).color(f2, f3, f4, f).endVertex();
        bufferBuilder.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.maxZ).color(f2, f3, f4, f).endVertex();
        bufferBuilder.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.maxZ).color(f2, f3, f4, f).endVertex();
        bufferBuilder.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.maxZ).color(f2, f3, f4, f).endVertex();
        bufferBuilder.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ).color(f2, f3, f4, f).endVertex();
        bufferBuilder.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.maxZ).color(f2, f3, f4, f).endVertex();
        bufferBuilder.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.maxZ).color(f2, f3, f4, f).endVertex();
        bufferBuilder.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.minZ).color(f2, f3, f4, f).endVertex();
        tessellator.draw();
        GlStateManager.depthMask(true);
        GlStateManager.enableDepth();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
    }

    public static void drawOpenGradientBox(BlockPos blockPos, Color color, Color color2, double d) {
        for (EnumFacing enumFacing : EnumFacing.values()) {
            if (enumFacing == EnumFacing.UP) continue;
            RenderUtil.drawGradientPlane(blockPos, enumFacing, color, color2, d);
        }
    }

    public static void drawBoxESP(BlockPos blockPos, Color color, float f, boolean bl, boolean bl2, int n) {
        AxisAlignedBB axisAlignedBB = new AxisAlignedBB((double)blockPos.getX() - RenderUtil.mc.getRenderManager().viewerPosX, (double)blockPos.getY() - RenderUtil.mc.getRenderManager().viewerPosY, (double)blockPos.getZ() - RenderUtil.mc.getRenderManager().viewerPosZ, (double)(blockPos.getX() + 1) - RenderUtil.mc.getRenderManager().viewerPosX, (double)(blockPos.getY() + 1) - RenderUtil.mc.getRenderManager().viewerPosY, (double)(blockPos.getZ() + 1) - RenderUtil.mc.getRenderManager().viewerPosZ);
        camera.setPosition(Objects.requireNonNull(RenderUtil.mc.getRenderViewEntity()).posX, RenderUtil.mc.getRenderViewEntity().posY, RenderUtil.mc.getRenderViewEntity().posZ);
        if (camera.isBoundingBoxInFrustum(new AxisAlignedBB(axisAlignedBB.minX + RenderUtil.mc.getRenderManager().viewerPosX, axisAlignedBB.minY + RenderUtil.mc.getRenderManager().viewerPosY, axisAlignedBB.minZ + RenderUtil.mc.getRenderManager().viewerPosZ, axisAlignedBB.maxX + RenderUtil.mc.getRenderManager().viewerPosX, axisAlignedBB.maxY + RenderUtil.mc.getRenderManager().viewerPosY, axisAlignedBB.maxZ + RenderUtil.mc.getRenderManager().viewerPosZ))) {
            GlStateManager.pushMatrix();
            GlStateManager.enableBlend();
            GlStateManager.disableDepth();
            GlStateManager.tryBlendFuncSeparate(770, 771, 0, 1);
            GlStateManager.disableTexture2D();
            GlStateManager.depthMask(false);
            GL11.glEnable(2848);
            GL11.glHint(3154, 4354);
            GL11.glLineWidth(f);
            if (bl2) {
                RenderGlobal.renderFilledBox(axisAlignedBB, (float)color.getRed() / 255.0f, (float)color.getGreen() / 255.0f, (float)color.getBlue() / 255.0f, (float)n / 255.0f);
            }
            if (bl) {
                RenderGlobal.drawBoundingBox(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ, axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.maxZ, (float)color.getRed() / 255.0f, (float)color.getGreen() / 255.0f, (float)color.getBlue() / 255.0f, (float)color.getAlpha() / 255.0f);
            }
            GL11.glDisable(2848);
            GlStateManager.depthMask(true);
            GlStateManager.enableDepth();
            GlStateManager.enableTexture2D();
            GlStateManager.disableBlend();
            GlStateManager.popMatrix();
        }
    }

    public static void drawText(AxisAlignedBB axisAlignedBB, String string) {
        if (axisAlignedBB == null || string == null) {
            return;
        }
        GlStateManager.pushMatrix();
        RenderUtil.glBillboardDistanceScaled((float)axisAlignedBB.minX + 0.5f, (float)axisAlignedBB.minY + 0.5f, (float)axisAlignedBB.minZ + 0.5f, RenderUtil.mc.player, 1.0f);
        GlStateManager.disableDepth();
        GlStateManager.translate(-((double)MadCat.textManager.getStringWidth(string) / 2.0), 0.0, 0.0);
        MadCat.textManager.drawStringWithShadow(string, 0.0f, 0.0f, -5592406);
        GlStateManager.popMatrix();
    }

    public static void glScissor(float f, float f2, float f3, float f4, ScaledResolution scaledResolution) {
        GL11.glScissor((int)(f * (float)scaledResolution.getScaleFactor()), (int)((float)RenderUtil.mc.displayHeight - f4 * (float)scaledResolution.getScaleFactor()), (int)((f3 - f) * (float)scaledResolution.getScaleFactor()), (int)((f4 - f2) * (float)scaledResolution.getScaleFactor()));
    }

    public static void drawBlockOutline(AxisAlignedBB axisAlignedBB, Color color, float f) {
        float f2 = (float)color.getRed() / 255.0f;
        float f3 = (float)color.getGreen() / 255.0f;
        float f4 = (float)color.getBlue() / 255.0f;
        float f5 = (float)color.getAlpha() / 255.0f;
        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        GlStateManager.disableDepth();
        GlStateManager.tryBlendFuncSeparate(770, 771, 0, 1);
        GlStateManager.disableTexture2D();
        GlStateManager.depthMask(false);
        GL11.glEnable(2848);
        GL11.glHint(3154, 4354);
        GL11.glLineWidth(f);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferBuilder = tessellator.getBuffer();
        bufferBuilder.begin(3, DefaultVertexFormats.POSITION_COLOR);
        bufferBuilder.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ).color(f2, f3, f4, f5).endVertex();
        bufferBuilder.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.maxZ).color(f2, f3, f4, f5).endVertex();
        bufferBuilder.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.maxZ).color(f2, f3, f4, f5).endVertex();
        bufferBuilder.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.minZ).color(f2, f3, f4, f5).endVertex();
        bufferBuilder.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ).color(f2, f3, f4, f5).endVertex();
        bufferBuilder.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.minZ).color(f2, f3, f4, f5).endVertex();
        bufferBuilder.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.maxZ).color(f2, f3, f4, f5).endVertex();
        bufferBuilder.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.maxZ).color(f2, f3, f4, f5).endVertex();
        bufferBuilder.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.maxZ).color(f2, f3, f4, f5).endVertex();
        bufferBuilder.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.maxZ).color(f2, f3, f4, f5).endVertex();
        bufferBuilder.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.maxZ).color(f2, f3, f4, f5).endVertex();
        bufferBuilder.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.maxZ).color(f2, f3, f4, f5).endVertex();
        bufferBuilder.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.minZ).color(f2, f3, f4, f5).endVertex();
        bufferBuilder.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.minZ).color(f2, f3, f4, f5).endVertex();
        bufferBuilder.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.minZ).color(f2, f3, f4, f5).endVertex();
        bufferBuilder.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.minZ).color(f2, f3, f4, f5).endVertex();
        tessellator.draw();
        GL11.glDisable(2848);
        GlStateManager.depthMask(true);
        GlStateManager.enableDepth();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
    }

    public static Vec3d to2D(double d, double d2, double d3) {
        GL11.glGetFloat(2982, modelView);
        GL11.glGetFloat(2983, projection);
        GL11.glGetInteger(2978, viewport);
        boolean bl = GLU.gluProject((float)d, (float)d2, (float)d3, modelView, projection, viewport, screenCoords);
        if (bl) {
            return new Vec3d(screenCoords.get(0), (float)Display.getHeight() - screenCoords.get(1), screenCoords.get(2));
        }
        return null;
    }

    public static void drawBlockOutline(BlockPos blockPos, Color color, float f, boolean bl) {
        IBlockState iBlockState = RenderUtil.mc.world.getBlockState(blockPos);
        if ((bl || iBlockState.getMaterial() != Material.AIR) && RenderUtil.mc.world.getWorldBorder().contains(blockPos)) {
            Vec3d vec3d = EntityUtil.interpolateEntity(RenderUtil.mc.player, mc.getRenderPartialTicks());
            RenderUtil.drawBlockOutline(iBlockState.getSelectedBoundingBox(RenderUtil.mc.world, blockPos).grow(0.002f).offset(-vec3d.x, -vec3d.y, -vec3d.z), color, f);
        }
    }

    public static void drawGradientPlane(BlockPos blockPos, EnumFacing enumFacing, Color color, Color color2, double d) {
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferBuilder = tessellator.getBuffer();
        IBlockState iBlockState = RenderUtil.mc.world.getBlockState(blockPos);
        Vec3d vec3d = EntityUtil.interpolateEntity(RenderUtil.mc.player, mc.getRenderPartialTicks());
        AxisAlignedBB axisAlignedBB = iBlockState.getSelectedBoundingBox(RenderUtil.mc.world, blockPos).grow(0.002f).offset(-vec3d.x, -vec3d.y, -vec3d.z).expand(0.0, d, 0.0);
        float f = (float)color.getRed() / 255.0f;
        float f2 = (float)color.getGreen() / 255.0f;
        float f3 = (float)color.getBlue() / 255.0f;
        float f4 = (float)color.getAlpha() / 255.0f;
        float f5 = (float)color2.getRed() / 255.0f;
        float f6 = (float)color2.getGreen() / 255.0f;
        float f7 = (float)color2.getBlue() / 255.0f;
        float f8 = (float)color2.getAlpha() / 255.0f;
        double d2 = 0.0;
        double d3 = 0.0;
        double d4 = 0.0;
        double d5 = 0.0;
        double d6 = 0.0;
        double d7 = 0.0;
        if (enumFacing == EnumFacing.DOWN) {
            d2 = axisAlignedBB.minX;
            d5 = axisAlignedBB.maxX;
            d3 = axisAlignedBB.minY;
            d6 = axisAlignedBB.minY;
            d4 = axisAlignedBB.minZ;
            d7 = axisAlignedBB.maxZ;
        } else if (enumFacing == EnumFacing.UP) {
            d2 = axisAlignedBB.minX;
            d5 = axisAlignedBB.maxX;
            d3 = axisAlignedBB.maxY;
            d6 = axisAlignedBB.maxY;
            d4 = axisAlignedBB.minZ;
            d7 = axisAlignedBB.maxZ;
        } else if (enumFacing == EnumFacing.EAST) {
            d2 = axisAlignedBB.maxX;
            d5 = axisAlignedBB.maxX;
            d3 = axisAlignedBB.minY;
            d6 = axisAlignedBB.maxY;
            d4 = axisAlignedBB.minZ;
            d7 = axisAlignedBB.maxZ;
        } else if (enumFacing == EnumFacing.WEST) {
            d2 = axisAlignedBB.minX;
            d5 = axisAlignedBB.minX;
            d3 = axisAlignedBB.minY;
            d6 = axisAlignedBB.maxY;
            d4 = axisAlignedBB.minZ;
            d7 = axisAlignedBB.maxZ;
        } else if (enumFacing == EnumFacing.SOUTH) {
            d2 = axisAlignedBB.minX;
            d5 = axisAlignedBB.maxX;
            d3 = axisAlignedBB.minY;
            d6 = axisAlignedBB.maxY;
            d4 = axisAlignedBB.maxZ;
            d7 = axisAlignedBB.maxZ;
        } else if (enumFacing == EnumFacing.NORTH) {
            d2 = axisAlignedBB.minX;
            d5 = axisAlignedBB.maxX;
            d3 = axisAlignedBB.minY;
            d6 = axisAlignedBB.maxY;
            d4 = axisAlignedBB.minZ;
            d7 = axisAlignedBB.minZ;
        }
        GlStateManager.pushMatrix();
        GlStateManager.disableDepth();
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.depthMask(false);
        bufferBuilder.begin(5, DefaultVertexFormats.POSITION_COLOR);
        if (enumFacing == EnumFacing.EAST || enumFacing == EnumFacing.WEST || enumFacing == EnumFacing.NORTH || enumFacing == EnumFacing.SOUTH) {
            bufferBuilder.pos(d2, d3, d4).color(f, f2, f3, f4).endVertex();
            bufferBuilder.pos(d2, d3, d4).color(f, f2, f3, f4).endVertex();
            bufferBuilder.pos(d2, d3, d4).color(f, f2, f3, f4).endVertex();
            bufferBuilder.pos(d2, d3, d7).color(f, f2, f3, f4).endVertex();
            bufferBuilder.pos(d2, d6, d4).color(f5, f6, f7, f8).endVertex();
            bufferBuilder.pos(d2, d6, d7).color(f5, f6, f7, f8).endVertex();
            bufferBuilder.pos(d2, d6, d7).color(f5, f6, f7, f8).endVertex();
            bufferBuilder.pos(d2, d3, d7).color(f, f2, f3, f4).endVertex();
            bufferBuilder.pos(d5, d6, d7).color(f5, f6, f7, f8).endVertex();
            bufferBuilder.pos(d5, d3, d7).color(f, f2, f3, f4).endVertex();
            bufferBuilder.pos(d5, d3, d7).color(f, f2, f3, f4).endVertex();
            bufferBuilder.pos(d5, d3, d4).color(f, f2, f3, f4).endVertex();
            bufferBuilder.pos(d5, d6, d7).color(f5, f6, f7, f8).endVertex();
            bufferBuilder.pos(d5, d6, d4).color(f5, f6, f7, f8).endVertex();
            bufferBuilder.pos(d5, d6, d4).color(f5, f6, f7, f8).endVertex();
            bufferBuilder.pos(d5, d3, d4).color(f, f2, f3, f4).endVertex();
            bufferBuilder.pos(d2, d6, d4).color(f5, f6, f7, f8).endVertex();
            bufferBuilder.pos(d2, d3, d4).color(f, f2, f3, f4).endVertex();
            bufferBuilder.pos(d2, d3, d4).color(f, f2, f3, f4).endVertex();
            bufferBuilder.pos(d5, d3, d4).color(f, f2, f3, f4).endVertex();
            bufferBuilder.pos(d2, d3, d7).color(f, f2, f3, f4).endVertex();
            bufferBuilder.pos(d5, d3, d7).color(f, f2, f3, f4).endVertex();
            bufferBuilder.pos(d5, d3, d7).color(f, f2, f3, f4).endVertex();
            bufferBuilder.pos(d2, d6, d4).color(f5, f6, f7, f8).endVertex();
            bufferBuilder.pos(d2, d6, d4).color(f5, f6, f7, f8).endVertex();
            bufferBuilder.pos(d2, d6, d7).color(f5, f6, f7, f8).endVertex();
            bufferBuilder.pos(d5, d6, d4).color(f5, f6, f7, f8).endVertex();
            bufferBuilder.pos(d5, d6, d7).color(f5, f6, f7, f8).endVertex();
            bufferBuilder.pos(d5, d6, d7).color(f5, f6, f7, f8).endVertex();
            bufferBuilder.pos(d5, d6, d7).color(f5, f6, f7, f8).endVertex();
        } else if (enumFacing == EnumFacing.UP) {
            bufferBuilder.pos(d2, d3, d4).color(f5, f6, f7, f8).endVertex();
            bufferBuilder.pos(d2, d3, d4).color(f5, f6, f7, f8).endVertex();
            bufferBuilder.pos(d2, d3, d4).color(f5, f6, f7, f8).endVertex();
            bufferBuilder.pos(d2, d3, d7).color(f5, f6, f7, f8).endVertex();
            bufferBuilder.pos(d2, d6, d4).color(f5, f6, f7, f8).endVertex();
            bufferBuilder.pos(d2, d6, d7).color(f5, f6, f7, f8).endVertex();
            bufferBuilder.pos(d2, d6, d7).color(f5, f6, f7, f8).endVertex();
            bufferBuilder.pos(d2, d3, d7).color(f5, f6, f7, f8).endVertex();
            bufferBuilder.pos(d5, d6, d7).color(f5, f6, f7, f8).endVertex();
            bufferBuilder.pos(d5, d3, d7).color(f5, f6, f7, f8).endVertex();
            bufferBuilder.pos(d5, d3, d7).color(f5, f6, f7, f8).endVertex();
            bufferBuilder.pos(d5, d3, d4).color(f5, f6, f7, f8).endVertex();
            bufferBuilder.pos(d5, d6, d7).color(f5, f6, f7, f8).endVertex();
            bufferBuilder.pos(d5, d6, d4).color(f5, f6, f7, f8).endVertex();
            bufferBuilder.pos(d5, d6, d4).color(f5, f6, f7, f8).endVertex();
            bufferBuilder.pos(d5, d3, d4).color(f5, f6, f7, f8).endVertex();
            bufferBuilder.pos(d2, d6, d4).color(f5, f6, f7, f8).endVertex();
            bufferBuilder.pos(d2, d3, d4).color(f5, f6, f7, f8).endVertex();
            bufferBuilder.pos(d2, d3, d4).color(f5, f6, f7, f8).endVertex();
            bufferBuilder.pos(d5, d3, d4).color(f5, f6, f7, f8).endVertex();
            bufferBuilder.pos(d2, d3, d7).color(f5, f6, f7, f8).endVertex();
            bufferBuilder.pos(d5, d3, d7).color(f5, f6, f7, f8).endVertex();
            bufferBuilder.pos(d5, d3, d7).color(f5, f6, f7, f8).endVertex();
            bufferBuilder.pos(d2, d6, d4).color(f5, f6, f7, f8).endVertex();
            bufferBuilder.pos(d2, d6, d4).color(f5, f6, f7, f8).endVertex();
            bufferBuilder.pos(d2, d6, d7).color(f5, f6, f7, f8).endVertex();
            bufferBuilder.pos(d5, d6, d4).color(f5, f6, f7, f8).endVertex();
            bufferBuilder.pos(d5, d6, d7).color(f5, f6, f7, f8).endVertex();
            bufferBuilder.pos(d5, d6, d7).color(f5, f6, f7, f8).endVertex();
            bufferBuilder.pos(d5, d6, d7).color(f5, f6, f7, f8).endVertex();
        } else if (enumFacing == EnumFacing.DOWN) {
            bufferBuilder.pos(d2, d3, d4).color(f, f2, f3, f4).endVertex();
            bufferBuilder.pos(d2, d3, d4).color(f, f2, f3, f4).endVertex();
            bufferBuilder.pos(d2, d3, d4).color(f, f2, f3, f4).endVertex();
            bufferBuilder.pos(d2, d3, d7).color(f, f2, f3, f4).endVertex();
            bufferBuilder.pos(d2, d6, d4).color(f, f2, f3, f4).endVertex();
            bufferBuilder.pos(d2, d6, d7).color(f, f2, f3, f4).endVertex();
            bufferBuilder.pos(d2, d6, d7).color(f, f2, f3, f4).endVertex();
            bufferBuilder.pos(d2, d3, d7).color(f, f2, f3, f4).endVertex();
            bufferBuilder.pos(d5, d6, d7).color(f, f2, f3, f4).endVertex();
            bufferBuilder.pos(d5, d3, d7).color(f, f2, f3, f4).endVertex();
            bufferBuilder.pos(d5, d3, d7).color(f, f2, f3, f4).endVertex();
            bufferBuilder.pos(d5, d3, d4).color(f, f2, f3, f4).endVertex();
            bufferBuilder.pos(d5, d6, d7).color(f, f2, f3, f4).endVertex();
            bufferBuilder.pos(d5, d6, d4).color(f, f2, f3, f4).endVertex();
            bufferBuilder.pos(d5, d6, d4).color(f, f2, f3, f4).endVertex();
            bufferBuilder.pos(d5, d3, d4).color(f, f2, f3, f4).endVertex();
            bufferBuilder.pos(d2, d6, d4).color(f, f2, f3, f4).endVertex();
            bufferBuilder.pos(d2, d3, d4).color(f, f2, f3, f4).endVertex();
            bufferBuilder.pos(d2, d3, d4).color(f, f2, f3, f4).endVertex();
            bufferBuilder.pos(d5, d3, d4).color(f, f2, f3, f4).endVertex();
            bufferBuilder.pos(d2, d3, d7).color(f, f2, f3, f4).endVertex();
            bufferBuilder.pos(d5, d3, d7).color(f, f2, f3, f4).endVertex();
            bufferBuilder.pos(d5, d3, d7).color(f, f2, f3, f4).endVertex();
            bufferBuilder.pos(d2, d6, d4).color(f, f2, f3, f4).endVertex();
            bufferBuilder.pos(d2, d6, d4).color(f, f2, f3, f4).endVertex();
            bufferBuilder.pos(d2, d6, d7).color(f, f2, f3, f4).endVertex();
            bufferBuilder.pos(d5, d6, d4).color(f, f2, f3, f4).endVertex();
            bufferBuilder.pos(d5, d6, d7).color(f, f2, f3, f4).endVertex();
            bufferBuilder.pos(d5, d6, d7).color(f, f2, f3, f4).endVertex();
            bufferBuilder.pos(d5, d6, d7).color(f, f2, f3, f4).endVertex();
        }
        tessellator.draw();
        GlStateManager.depthMask(true);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
        GlStateManager.enableDepth();
        GlStateManager.popMatrix();
    }

    public static void drawBoxESP(BlockPos blockPos, Color color, boolean bl, Color color2, float f, boolean bl2, boolean bl3, int n, boolean bl4) {
        if (bl3) {
            RenderUtil.drawBox(blockPos, new Color(color.getRed(), color.getGreen(), color.getBlue(), n));
        }
        if (bl2) {
            RenderUtil.drawBlockOutline(blockPos, bl ? color2 : color, f, bl4);
        }
    }

    public static void drawArc(float f, float f2, float f3, float f4, float f5, int n) {
        GL11.glBegin(4);
        int n2 = (int)((float)n / (360.0f / f4)) + 1;
        while ((float)n2 <= (float)n / (360.0f / f5)) {
            double d = Math.PI * 2 * (double)(n2 - 1) / (double)n;
            double d2 = Math.PI * 2 * (double)n2 / (double)n;
            GL11.glVertex2d(f, f2);
            GL11.glVertex2d((double)f + Math.cos(d2) * (double)f3, (double)f2 + Math.sin(d2) * (double)f3);
            GL11.glVertex2d((double)f + Math.cos(d) * (double)f3, (double)f2 + Math.sin(d) * (double)f3);
            ++n2;
        }
        RenderUtil.glEnd();
    }

    public static void drawOutlinedRoundedRectangle(int n, int n2, int n3, int n4, float f, float f2, float f3, float f4, float f5, float f6) {
        RenderUtil.drawRoundedRectangle(n, n2, n3, n4, f);
        GL11.glColor4f(f2, f3, f4, f5);
        RenderUtil.drawRoundedRectangle((float)n + f6, (float)n2 + f6, (float)n3 - f6 * 2.0f, (float)n4 - f6 * 2.0f, f);
    }

    public static void drawGradientSideways(double d, double d2, double d3, double d4, int n, int n2) {
        float f = (float)(n >> 24 & 0xFF) / 255.0f;
        float f2 = (float)(n >> 16 & 0xFF) / 255.0f;
        float f3 = (float)(n >> 8 & 0xFF) / 255.0f;
        float f4 = (float)(n & 0xFF) / 255.0f;
        float f5 = (float)(n2 >> 24 & 0xFF) / 255.0f;
        float f6 = (float)(n2 >> 16 & 0xFF) / 255.0f;
        float f7 = (float)(n2 >> 8 & 0xFF) / 255.0f;
        float f8 = (float)(n2 & 0xFF) / 255.0f;
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(2848);
        GL11.glShadeModel(7425);
        GL11.glPushMatrix();
        GL11.glBegin(7);
        GL11.glColor4f(f2, f3, f4, f);
        GL11.glVertex2d(d, d2);
        GL11.glVertex2d(d, d4);
        GL11.glColor4f(f6, f7, f8, f5);
        GL11.glVertex2d(d3, d4);
        GL11.glVertex2d(d3, d2);
        GL11.glEnd();
        GL11.glPopMatrix();
        GL11.glEnable(3553);
        GL11.glDisable(3042);
        GL11.glDisable(2848);
        GL11.glShadeModel(7424);
    }

    public static void drawEntityBoxESP(Entity entity, Color color, boolean bl, Color color2, float f, boolean bl2, boolean bl3, int n) {
        Vec3d vec3d = RenderUtil.getInterpolatedPos(entity, mc.getRenderPartialTicks(), true);
        AxisAlignedBB axisAlignedBB = new AxisAlignedBB(entity.getEntityBoundingBox().minX - 0.05 - entity.posX + vec3d.x, entity.getEntityBoundingBox().minY - 0.0 - entity.posY + vec3d.y, entity.getEntityBoundingBox().minZ - 0.05 - entity.posZ + vec3d.z, entity.getEntityBoundingBox().maxX + 0.05 - entity.posX + vec3d.x, entity.getEntityBoundingBox().maxY + 0.1 - entity.posY + vec3d.y, entity.getEntityBoundingBox().maxZ + 0.05 - entity.posZ + vec3d.z);
        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        GlStateManager.disableDepth();
        GlStateManager.tryBlendFuncSeparate(770, 771, 0, 1);
        GlStateManager.disableTexture2D();
        GlStateManager.depthMask(false);
        GL11.glEnable(2848);
        GL11.glHint(3154, 4354);
        GL11.glLineWidth(f);
        if (bl3) {
            RenderGlobal.renderFilledBox(axisAlignedBB, (float)color.getRed() / 255.0f, (float)color.getGreen() / 255.0f, (float)color.getBlue() / 255.0f, (float)n / 255.0f);
        }
        GL11.glDisable(2848);
        GlStateManager.depthMask(true);
        GlStateManager.enableDepth();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
        if (bl2) {
            RenderUtil.drawBlockOutline(axisAlignedBB, bl ? color2 : color, f, false);
        }
    }

    public static void drawLine(float f, float f2, float f3, float f4, float f5, int n) {
        float f6 = (float)(n >> 16 & 0xFF) / 255.0f;
        float f7 = (float)(n >> 8 & 0xFF) / 255.0f;
        float f8 = (float)(n & 0xFF) / 255.0f;
        float f9 = (float)(n >> 24 & 0xFF) / 255.0f;
        GlStateManager.pushMatrix();
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.shadeModel(7425);
        GL11.glLineWidth(f5);
        GL11.glEnable(2848);
        GL11.glHint(3154, 4354);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferBuilder = tessellator.getBuffer();
        bufferBuilder.begin(3, DefaultVertexFormats.POSITION_COLOR);
        bufferBuilder.pos(f, f2, 0.0).color(f6, f7, f8, f9).endVertex();
        bufferBuilder.pos(f3, f4, 0.0).color(f6, f7, f8, f9).endVertex();
        tessellator.draw();
        GlStateManager.shadeModel(7424);
        GL11.glDisable(2848);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
        GlStateManager.popMatrix();
    }

    public static void drawText(BlockPos blockPos, String string) {
        if (blockPos == null || string == null) {
            return;
        }
        GlStateManager.pushMatrix();
        RenderUtil.glBillboard((float)blockPos.getX() + 0.5f, (float)blockPos.getY() + 0.5f, (float)blockPos.getZ() + 0.5f);
        GlStateManager.disableDepth();
        GlStateManager.translate(-((double)MadCat.textManager.getStringWidth(string) / 2.0), 0.0, 0.0);
        MadCat.textManager.drawStringWithShadow(string, 0.0f, 0.0f, -5592406);
        GlStateManager.popMatrix();
    }

    public static void drawRect(float f, float f2, float f3, float f4, int n) {
        float f5 = (float)(n >> 24 & 0xFF) / 255.0f;
        float f6 = (float)(n >> 16 & 0xFF) / 255.0f;
        float f7 = (float)(n >> 8 & 0xFF) / 255.0f;
        float f8 = (float)(n & 0xFF) / 255.0f;
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferBuilder = tessellator.getBuffer();
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        bufferBuilder.begin(7, DefaultVertexFormats.POSITION_COLOR);
        bufferBuilder.pos(f, f4, 0.0).color(f6, f7, f8, f5).endVertex();
        bufferBuilder.pos(f3, f4, 0.0).color(f6, f7, f8, f5).endVertex();
        bufferBuilder.pos(f3, f2, 0.0).color(f6, f7, f8, f5).endVertex();
        bufferBuilder.pos(f, f2, 0.0).color(f6, f7, f8, f5).endVertex();
        tessellator.draw();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }

    public static void drawBlockOutline(BlockPos blockPos, Color color, float f, boolean bl, double d, boolean bl2, boolean bl3, int n, boolean bl4) {
        if (bl2) {
            Color color2 = new Color(color.getRed(), color.getGreen(), color.getBlue(), n);
            RenderUtil.drawFadingOutline(blockPos, bl3 ? color : color2, bl3 ? color2 : color, f, d);
            return;
        }
        IBlockState iBlockState = RenderUtil.mc.world.getBlockState(blockPos);
        if ((bl || iBlockState.getMaterial() != Material.AIR) && RenderUtil.mc.world.getWorldBorder().contains(blockPos)) {
            AxisAlignedBB axisAlignedBB = new AxisAlignedBB((double)blockPos.getX() - RenderUtil.mc.getRenderManager().viewerPosX, (double)blockPos.getY() - RenderUtil.mc.getRenderManager().viewerPosY, (double)blockPos.getZ() - RenderUtil.mc.getRenderManager().viewerPosZ, (double)(blockPos.getX() + 1) - RenderUtil.mc.getRenderManager().viewerPosX, (double)(blockPos.getY() + 1) - RenderUtil.mc.getRenderManager().viewerPosY + d, (double)(blockPos.getZ() + 1) - RenderUtil.mc.getRenderManager().viewerPosZ);
            RenderUtil.drawBlockOutline(axisAlignedBB.grow(0.002f), color, f, bl4);
        }
    }

    public static boolean isInViewFrustrum(AxisAlignedBB axisAlignedBB) {
        Entity entity = Minecraft.getMinecraft().getRenderViewEntity();
        if (!$assertionsDisabled && entity == null) {
            throw new AssertionError();
        }
        frustrum.setPosition(entity.posX, entity.posY, entity.posZ);
        return frustrum.isBoundingBoxInFrustum(axisAlignedBB);
    }

    public static void drawBoxESP(BlockPos blockPos, Color color, boolean bl, Color color2, float f, boolean bl2, boolean bl3, int n, boolean bl4, double d) {
        if (bl3) {
            RenderUtil.drawBox(blockPos, new Color(color.getRed(), color.getGreen(), color.getBlue(), n), d, false, false, 0);
        }
        if (bl2) {
            RenderUtil.drawBlockOutline(blockPos, bl ? color2 : color, f, bl4, d, false, false, 0, false);
        }
    }

    public static void drawCircle(float f, float f2, float f3, float f4, Color color) {
        AxisAlignedBB axisAlignedBB = new AxisAlignedBB((double)f - RenderUtil.mc.getRenderManager().viewerPosX, (double)f2 - RenderUtil.mc.getRenderManager().viewerPosY, (double)f3 - RenderUtil.mc.getRenderManager().viewerPosZ, (double)(f + 1.0f) - RenderUtil.mc.getRenderManager().viewerPosX, (double)(f2 + 1.0f) - RenderUtil.mc.getRenderManager().viewerPosY, (double)(f3 + 1.0f) - RenderUtil.mc.getRenderManager().viewerPosZ);
        camera.setPosition(Objects.requireNonNull(RenderUtil.mc.getRenderViewEntity()).posX, RenderUtil.mc.getRenderViewEntity().posY, RenderUtil.mc.getRenderViewEntity().posZ);
        if (camera.isBoundingBoxInFrustum(new AxisAlignedBB(axisAlignedBB.minX + RenderUtil.mc.getRenderManager().viewerPosX, axisAlignedBB.minY + RenderUtil.mc.getRenderManager().viewerPosY, axisAlignedBB.minZ + RenderUtil.mc.getRenderManager().viewerPosZ, axisAlignedBB.maxX + RenderUtil.mc.getRenderManager().viewerPosX, axisAlignedBB.maxY + RenderUtil.mc.getRenderManager().viewerPosY, axisAlignedBB.maxZ + RenderUtil.mc.getRenderManager().viewerPosZ))) {
            RenderUtil.drawCircleVertices(axisAlignedBB, f4, color);
        }
    }

    public static void GlPost() {
        RenderUtil.GLPost(depth, texture, clean, bind, override);
    }

    public static void drawBBFill(AxisAlignedBB axisAlignedBB, Color color, int n) {
        AxisAlignedBB axisAlignedBB2 = new AxisAlignedBB(axisAlignedBB.minX - RenderUtil.mc.getRenderManager().viewerPosX, axisAlignedBB.minY - RenderUtil.mc.getRenderManager().viewerPosY, axisAlignedBB.minZ - RenderUtil.mc.getRenderManager().viewerPosZ, axisAlignedBB.maxX - RenderUtil.mc.getRenderManager().viewerPosX, axisAlignedBB.maxY - RenderUtil.mc.getRenderManager().viewerPosY, axisAlignedBB.maxZ - RenderUtil.mc.getRenderManager().viewerPosZ);
        camera.setPosition(Objects.requireNonNull(RenderUtil.mc.getRenderViewEntity()).posX, RenderUtil.mc.getRenderViewEntity().posY, RenderUtil.mc.getRenderViewEntity().posZ);
        if (camera.isBoundingBoxInFrustum(new AxisAlignedBB(axisAlignedBB2.minX + RenderUtil.mc.getRenderManager().viewerPosX, axisAlignedBB2.minY + RenderUtil.mc.getRenderManager().viewerPosY, axisAlignedBB2.minZ + RenderUtil.mc.getRenderManager().viewerPosZ, axisAlignedBB2.maxX + RenderUtil.mc.getRenderManager().viewerPosX, axisAlignedBB2.maxY + RenderUtil.mc.getRenderManager().viewerPosY, axisAlignedBB2.maxZ + RenderUtil.mc.getRenderManager().viewerPosZ))) {
            GlStateManager.pushMatrix();
            GlStateManager.enableBlend();
            GlStateManager.disableDepth();
            GlStateManager.tryBlendFuncSeparate(770, 771, 0, 1);
            GlStateManager.disableTexture2D();
            GlStateManager.depthMask(false);
            GL11.glEnable(2848);
            GL11.glHint(3154, 4354);
            RenderGlobal.renderFilledBox(axisAlignedBB2, (float)color.getRed() / 255.0f, (float)color.getGreen() / 255.0f, (float)color.getBlue() / 255.0f, (float)n / 255.0f);
            GL11.glDisable(2848);
            GlStateManager.depthMask(true);
            GlStateManager.enableDepth();
            GlStateManager.enableTexture2D();
            GlStateManager.disableBlend();
            GlStateManager.popMatrix();
        }
    }

    public static void drawBBBox(BlockPos blockPos, Color color) {
        AxisAlignedBB axisAlignedBB = RenderUtil.mc.world.getBlockState(blockPos).getSelectedBoundingBox(RenderUtil.mc.world, blockPos);
        double d = axisAlignedBB.minX + (axisAlignedBB.maxX - axisAlignedBB.minX) / 2.0;
        double d2 = axisAlignedBB.minY + (axisAlignedBB.maxY - axisAlignedBB.minY) / 2.0;
        double d3 = axisAlignedBB.minZ + (axisAlignedBB.maxZ - axisAlignedBB.minZ) / 2.0;
        double d4 = 10.01 * ((axisAlignedBB.maxX - d) / 10.0);
        double d5 = 10.01 * ((axisAlignedBB.maxY - d2) / 10.0);
        double d6 = 10.01 * ((axisAlignedBB.maxZ - d3) / 10.0);
        AxisAlignedBB axisAlignedBB2 = new AxisAlignedBB(d - d4, d2 - d5, d3 - d6, d + d4, d2 + d5, d3 + d6);
        AxisAlignedBB axisAlignedBB3 = new AxisAlignedBB(axisAlignedBB2.minX - RenderUtil.mc.getRenderManager().viewerPosX, axisAlignedBB2.minY - RenderUtil.mc.getRenderManager().viewerPosY, axisAlignedBB2.minZ - RenderUtil.mc.getRenderManager().viewerPosZ, axisAlignedBB2.maxX - RenderUtil.mc.getRenderManager().viewerPosX, axisAlignedBB2.maxY - RenderUtil.mc.getRenderManager().viewerPosY, axisAlignedBB2.maxZ - RenderUtil.mc.getRenderManager().viewerPosZ);
        camera.setPosition(Objects.requireNonNull(RenderUtil.mc.getRenderViewEntity()).posX, RenderUtil.mc.getRenderViewEntity().posY, RenderUtil.mc.getRenderViewEntity().posZ);
        if (camera.isBoundingBoxInFrustum(new AxisAlignedBB(axisAlignedBB3.minX + RenderUtil.mc.getRenderManager().viewerPosX, axisAlignedBB3.minY + RenderUtil.mc.getRenderManager().viewerPosY, axisAlignedBB3.minZ + RenderUtil.mc.getRenderManager().viewerPosZ, axisAlignedBB3.maxX + RenderUtil.mc.getRenderManager().viewerPosX, axisAlignedBB3.maxY + RenderUtil.mc.getRenderManager().viewerPosY, axisAlignedBB3.maxZ + RenderUtil.mc.getRenderManager().viewerPosZ))) {
            GlStateManager.pushMatrix();
            GlStateManager.enableBlend();
            GlStateManager.disableDepth();
            GlStateManager.tryBlendFuncSeparate(770, 771, 0, 1);
            GlStateManager.disableTexture2D();
            GlStateManager.depthMask(false);
            GL11.glEnable(2848);
            GL11.glHint(3154, 4354);
            RenderGlobal.drawSelectionBoundingBox(axisAlignedBB3, (float)color.getRed() / 255.0f, (float)color.getGreen() / 255.0f, (float)color.getBlue() / 255.0f, (float)color.getAlpha() / 255.0f);
            GL11.glDisable(2848);
            GlStateManager.depthMask(true);
            GlStateManager.enableDepth();
            GlStateManager.enableTexture2D();
            GlStateManager.disableBlend();
            GlStateManager.popMatrix();
        }
    }

    public static void drawBlockOutline(BlockPos blockPos, Color color, float f, boolean bl, double d, boolean bl2, boolean bl3, int n) {
        if (bl2) {
            Color color2 = new Color(color.getRed(), color.getGreen(), color.getBlue(), n);
            RenderUtil.drawGradientBlockOutline(blockPos, bl3 ? color2 : color, bl3 ? color : color2, f, d);
            return;
        }
        IBlockState iBlockState = RenderUtil.mc.world.getBlockState(blockPos);
        if ((bl || iBlockState.getMaterial() != Material.AIR) && RenderUtil.mc.world.getWorldBorder().contains(blockPos)) {
            AxisAlignedBB axisAlignedBB = new AxisAlignedBB((double)blockPos.getX() - RenderUtil.mc.getRenderManager().viewerPosX, (double)blockPos.getY() - RenderUtil.mc.getRenderManager().viewerPosY, (double)blockPos.getZ() - RenderUtil.mc.getRenderManager().viewerPosZ, (double)(blockPos.getX() + 1) - RenderUtil.mc.getRenderManager().viewerPosX, (double)(blockPos.getY() + 1) - RenderUtil.mc.getRenderManager().viewerPosY + d, (double)(blockPos.getZ() + 1) - RenderUtil.mc.getRenderManager().viewerPosZ);
            RenderUtil.drawBlockOutline(axisAlignedBB.grow(0.002f), color, f);
        }
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
        double d3 = Math.PI * 2;
        for (int i = n2 * 90; i <= n2 * 90 + 90; ++i) {
            double d4 = Math.PI * 2 * (double)i / 360.0 + Math.toRadians(180.0);
            bufferBuilder.pos(d + Math.sin(d4) * (double)n, d2 + Math.cos(d4) * (double)n, 0.0).color(f6, f7, f8, f5).endVertex();
        }
        tessellator.draw();
        GlStateManager.shadeModel(7424);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
    }

    public static void glBillboardDistanceScaled(float f, float f2, float f3, EntityPlayer entityPlayer, float f4) {
        RenderUtil.glBillboard(f, f2, f3);
        int n = (int)entityPlayer.getDistance(f, f2, f3);
        float f5 = (float)n / 2.0f / (2.0f + (2.0f - f4));
        if (f5 < 1.0f) {
            f5 = 1.0f;
        }
        GlStateManager.scale(f5, f5, f5);
    }

    public static void drawBoundingBox(AxisAlignedBB axisAlignedBB, float f, int n) {
        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        GlStateManager.disableDepth();
        GlStateManager.tryBlendFuncSeparate(770, 771, 0, 1);
        GlStateManager.disableTexture2D();
        GlStateManager.depthMask(false);
        GL11.glEnable(2848);
        GL11.glHint(3154, 4354);
        GL11.glLineWidth(f);
        float f2 = (float)(n >> 24 & 0xFF) / 255.0f;
        float f3 = (float)(n >> 16 & 0xFF) / 255.0f;
        float f4 = (float)(n >> 8 & 0xFF) / 255.0f;
        float f5 = (float)(n & 0xFF) / 255.0f;
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferBuilder = tessellator.getBuffer();
        bufferBuilder.begin(3, DefaultVertexFormats.POSITION_COLOR);
        bufferBuilder.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ).color(f3, f4, f5, f2).endVertex();
        bufferBuilder.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.maxZ).color(f3, f4, f5, f2).endVertex();
        bufferBuilder.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.maxZ).color(f3, f4, f5, f2).endVertex();
        bufferBuilder.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.minZ).color(f3, f4, f5, f2).endVertex();
        bufferBuilder.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ).color(f3, f4, f5, f2).endVertex();
        bufferBuilder.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.minZ).color(f3, f4, f5, f2).endVertex();
        bufferBuilder.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.maxZ).color(f3, f4, f5, f2).endVertex();
        bufferBuilder.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.maxZ).color(f3, f4, f5, f2).endVertex();
        bufferBuilder.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.maxZ).color(f3, f4, f5, f2).endVertex();
        bufferBuilder.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.maxZ).color(f3, f4, f5, f2).endVertex();
        bufferBuilder.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.maxZ).color(f3, f4, f5, f2).endVertex();
        bufferBuilder.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.maxZ).color(f3, f4, f5, f2).endVertex();
        bufferBuilder.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.minZ).color(f3, f4, f5, f2).endVertex();
        bufferBuilder.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.minZ).color(f3, f4, f5, f2).endVertex();
        bufferBuilder.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.minZ).color(f3, f4, f5, f2).endVertex();
        bufferBuilder.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.minZ).color(f3, f4, f5, f2).endVertex();
        tessellator.draw();
        GL11.glDisable(2848);
        GlStateManager.depthMask(true);
        GlStateManager.enableDepth();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
    }

    public static void setColor(Color color) {
        GL11.glColor4d((double)color.getRed() / 255.0, (double)color.getGreen() / 255.0, (double)color.getBlue() / 255.0, (double)color.getAlpha() / 255.0);
    }

    public static void glColor(Color color) {
        GL11.glColor4f((float)color.getRed() / 255.0f, (float)color.getGreen() / 255.0f, (float)color.getBlue() / 255.0f, (float)color.getAlpha() / 255.0f);
    }

    public static int getRainbow(int n, int n2, float f, float f2) {
        float f3 = (System.currentTimeMillis() + (long)n2) % (long)n;
        return Color.getHSBColor(f3 / (float)n, f, f2).getRGB();
    }

    public static void renderThree() {
        GL11.glStencilFunc(514, 1, 15);
        GL11.glStencilOp(7680, 7680, 7680);
        GL11.glPolygonMode(1032, 6913);
    }

    public static void drawHGradientRect(float f, float f2, float f3, float f4, int n, int n2) {
        float f5 = (float)(n >> 16 & 0xFF) / 255.0f;
        float f6 = (float)(n >> 8 & 0xFF) / 255.0f;
        float f7 = (float)(n & 0xFF) / 255.0f;
        float f8 = (float)(n2 >> 24 & 0xFF) / 255.0f;
        float f9 = (float)(n2 >> 16 & 0xFF) / 255.0f;
        float f10 = (float)(n2 >> 8 & 0xFF) / 255.0f;
        float f11 = (float)(n2 & 0xFF) / 255.0f;
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.shadeModel(7425);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferBuilder = tessellator.getBuffer();
        bufferBuilder.begin(7, DefaultVertexFormats.POSITION_COLOR);
        bufferBuilder.pos(f, f2, 0.0).color(f5, f6, f7, f8).endVertex();
        bufferBuilder.pos(f, f4, 0.0).color(f5, f6, f7, f8).endVertex();
        bufferBuilder.pos(f3, f4, 0.0).color(f9, f10, f11, f8).endVertex();
        bufferBuilder.pos(f3, f2, 0.0).color(f9, f10, f11, f8).endVertex();
        tessellator.draw();
        GlStateManager.shadeModel(7424);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
    }

    public static void drawGlow(double d, double d2, double d3, double d4, int n) {
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.shadeModel(7425);
        RenderUtil.drawVGradientRect((int)d, (int)d2, (int)d3, (int)(d2 + (d4 - d2) / 2.0), ColorUtil.toRGBA(new Color(n).getRed(), new Color(n).getGreen(), new Color(n).getBlue(), 0), n);
        RenderUtil.drawVGradientRect((int)d, (int)(d2 + (d4 - d2) / 2.0), (int)d3, (int)d4, n, ColorUtil.toRGBA(new Color(n).getRed(), new Color(n).getGreen(), new Color(n).getBlue(), 0));
        int n2 = (int)((d4 - d2) / 2.0);
        RenderUtil.drawPolygonPart(d, d2 + (d4 - d2) / 2.0, n2, 0, n, ColorUtil.toRGBA(new Color(n).getRed(), new Color(n).getGreen(), new Color(n).getBlue(), 0));
        RenderUtil.drawPolygonPart(d, d2 + (d4 - d2) / 2.0, n2, 1, n, ColorUtil.toRGBA(new Color(n).getRed(), new Color(n).getGreen(), new Color(n).getBlue(), 0));
        RenderUtil.drawPolygonPart(d3, d2 + (d4 - d2) / 2.0, n2, 2, n, ColorUtil.toRGBA(new Color(n).getRed(), new Color(n).getGreen(), new Color(n).getBlue(), 0));
        RenderUtil.drawPolygonPart(d3, d2 + (d4 - d2) / 2.0, n2, 3, n, ColorUtil.toRGBA(new Color(n).getRed(), new Color(n).getGreen(), new Color(n).getBlue(), 0));
        GlStateManager.shadeModel(7424);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
    }

    public static void drawCircleOutline(float f, float f2, float f3) {
        RenderUtil.drawCircleOutline(f, f2, f3, 0, 360, 40);
    }

    public static void drawCircleVertices(AxisAlignedBB axisAlignedBB, float f, Color color) {
        float f2 = (float)color.getRed() / 255.0f;
        float f3 = (float)color.getGreen() / 255.0f;
        float f4 = (float)color.getBlue() / 255.0f;
        float f5 = (float)color.getAlpha() / 255.0f;
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferBuilder = tessellator.getBuffer();
        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        GlStateManager.disableDepth();
        GlStateManager.tryBlendFuncSeparate(770, 771, 0, 1);
        GlStateManager.disableTexture2D();
        GlStateManager.depthMask(false);
        GL11.glEnable(2848);
        GL11.glHint(3154, 4354);
        GL11.glLineWidth(1.0f);
        for (int i = 0; i < 360; ++i) {
            bufferBuilder.begin(3, DefaultVertexFormats.POSITION_COLOR);
            bufferBuilder.pos(axisAlignedBB.getCenter().x + Math.sin((double)i * 3.1415926 / 180.0) * (double)f, axisAlignedBB.minY, axisAlignedBB.getCenter().z + Math.cos((double)i * 3.1415926 / 180.0) * (double)f).color(f2, f3, f4, f5).endVertex();
            bufferBuilder.pos(axisAlignedBB.getCenter().x + Math.sin((double)(i + 1) * 3.1415926 / 180.0) * (double)f, axisAlignedBB.minY, axisAlignedBB.getCenter().z + Math.cos((double)(i + 1) * 3.1415926 / 180.0) * (double)f).color(f2, f3, f4, f5).endVertex();
            tessellator.draw();
        }
        GL11.glDisable(2848);
        GlStateManager.depthMask(true);
        GlStateManager.enableDepth();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
    }

    public static void drawBBBox(AxisAlignedBB axisAlignedBB, Color color, int n) {
        AxisAlignedBB axisAlignedBB2 = new AxisAlignedBB(axisAlignedBB.minX - RenderUtil.mc.getRenderManager().viewerPosX, axisAlignedBB.minY - RenderUtil.mc.getRenderManager().viewerPosY, axisAlignedBB.minZ - RenderUtil.mc.getRenderManager().viewerPosZ, axisAlignedBB.maxX - RenderUtil.mc.getRenderManager().viewerPosX, axisAlignedBB.maxY - RenderUtil.mc.getRenderManager().viewerPosY, axisAlignedBB.maxZ - RenderUtil.mc.getRenderManager().viewerPosZ);
        camera.setPosition(Objects.requireNonNull(RenderUtil.mc.getRenderViewEntity()).posX, RenderUtil.mc.getRenderViewEntity().posY, RenderUtil.mc.getRenderViewEntity().posZ);
        if (camera.isBoundingBoxInFrustum(new AxisAlignedBB(axisAlignedBB2.minX + RenderUtil.mc.getRenderManager().viewerPosX, axisAlignedBB2.minY + RenderUtil.mc.getRenderManager().viewerPosY, axisAlignedBB2.minZ + RenderUtil.mc.getRenderManager().viewerPosZ, axisAlignedBB2.maxX + RenderUtil.mc.getRenderManager().viewerPosX, axisAlignedBB2.maxY + RenderUtil.mc.getRenderManager().viewerPosY, axisAlignedBB2.maxZ + RenderUtil.mc.getRenderManager().viewerPosZ))) {
            GlStateManager.pushMatrix();
            GlStateManager.enableBlend();
            GlStateManager.disableDepth();
            GlStateManager.tryBlendFuncSeparate(770, 771, 0, 1);
            GlStateManager.disableTexture2D();
            GlStateManager.depthMask(false);
            GL11.glEnable(2848);
            GL11.glHint(3154, 4354);
            RenderGlobal.drawSelectionBoundingBox(axisAlignedBB2, (float)color.getRed() / 255.0f, (float)color.getGreen() / 255.0f, (float)color.getBlue() / 255.0f, (float)n / 255.0f);
            GL11.glDisable(2848);
            GlStateManager.depthMask(true);
            GlStateManager.enableDepth();
            GlStateManager.enableTexture2D();
            GlStateManager.disableBlend();
            GlStateManager.popMatrix();
        }
    }

    public static void glBillboard(float f, float f2, float f3) {
        float f4 = 0.02666667f;
        Vec3d renderPos = RenderUtil.mc.getRenderManager().renderViewEntity.getPositionVector();
        Vec3d entityPos = new Vec3d(f, f2, f3);
        Vec3d diffVec = entityPos.subtract(renderPos);
        GlStateManager.translate(diffVec.x, diffVec.y, diffVec.z);
        GlStateManager.glNormal3f(0.0f, 1.0f, 0.0f);
        GlStateManager.rotate(-RenderUtil.mc.player.rotationYaw, 0.0f, 1.0f, 0.0f);
        GlStateManager.rotate(RenderUtil.mc.player.rotationPitch, RenderUtil.mc.gameSettings.thirdPersonView == 2 ? -1.0f : 1.0f, 0.0f, 0.0f);
        GlStateManager.scale(-0.02666667f, -0.02666667f, 0.02666667f);
    }


    public static void GLPre(float f) {
        depth = GL11.glIsEnabled(2896);
        texture = GL11.glIsEnabled(3042);
        clean = GL11.glIsEnabled(3553);
        bind = GL11.glIsEnabled(2929);
        override = GL11.glIsEnabled(2848);
        RenderUtil.GLPre(depth, texture, clean, bind, override, f);
    }

    public static void drawCircleOutline(float f, float f2, float f3, int n, int n2, int n3) {
        RenderUtil.drawArcOutline(f, f2, f3, n, n2, n3);
    }

    public static AxisAlignedBB interpolateAxis(AxisAlignedBB axisAlignedBB) {
        return new AxisAlignedBB(axisAlignedBB.minX - RenderUtil.mc.getRenderManager().viewerPosX, axisAlignedBB.minY - RenderUtil.mc.getRenderManager().viewerPosY, axisAlignedBB.minZ - RenderUtil.mc.getRenderManager().viewerPosZ, axisAlignedBB.maxX - RenderUtil.mc.getRenderManager().viewerPosX, axisAlignedBB.maxY - RenderUtil.mc.getRenderManager().viewerPosY, axisAlignedBB.maxZ - RenderUtil.mc.getRenderManager().viewerPosZ);
    }

    public static void drawRectangleCorrectly(int n, int n2, int n3, int n4, int n5) {
        GL11.glLineWidth(1.0f);
        Gui.drawRect(n, n2, n + n3, n2 + n4, n5);
    }

    public static void checkSetupFBO() {
        Framebuffer framebuffer = RenderUtil.mc.getFramebuffer();
        if (framebuffer.depthBuffer > -1) {
            RenderUtil.setupFBO(framebuffer);
            framebuffer.depthBuffer = -1;
        }
    }

    private static void colorVertex(double d, double d2, double d3, Color color, int n, BufferBuilder bufferBuilder) {
        bufferBuilder.pos(d - RenderUtil.mc.getRenderManager().viewerPosX, d2 - RenderUtil.mc.getRenderManager().viewerPosY, d3 - RenderUtil.mc.getRenderManager().viewerPosZ).color(color.getRed(), color.getGreen(), color.getBlue(), n).endVertex();
    }

    public static void drawBox(BlockPos blockPos, Color color) {
        AxisAlignedBB axisAlignedBB = new AxisAlignedBB((double)blockPos.getX() - RenderUtil.mc.getRenderManager().viewerPosX, (double)blockPos.getY() - RenderUtil.mc.getRenderManager().viewerPosY, (double)blockPos.getZ() - RenderUtil.mc.getRenderManager().viewerPosZ, (double)(blockPos.getX() + 1) - RenderUtil.mc.getRenderManager().viewerPosX, (double)(blockPos.getY() + 1) - RenderUtil.mc.getRenderManager().viewerPosY, (double)(blockPos.getZ() + 1) - RenderUtil.mc.getRenderManager().viewerPosZ);
        camera.setPosition(Objects.requireNonNull(RenderUtil.mc.getRenderViewEntity()).posX, RenderUtil.mc.getRenderViewEntity().posY, RenderUtil.mc.getRenderViewEntity().posZ);
        if (camera.isBoundingBoxInFrustum(new AxisAlignedBB(axisAlignedBB.minX + RenderUtil.mc.getRenderManager().viewerPosX, axisAlignedBB.minY + RenderUtil.mc.getRenderManager().viewerPosY, axisAlignedBB.minZ + RenderUtil.mc.getRenderManager().viewerPosZ, axisAlignedBB.maxX + RenderUtil.mc.getRenderManager().viewerPosX, axisAlignedBB.maxY + RenderUtil.mc.getRenderManager().viewerPosY, axisAlignedBB.maxZ + RenderUtil.mc.getRenderManager().viewerPosZ))) {
            GlStateManager.pushMatrix();
            GlStateManager.enableBlend();
            GlStateManager.disableDepth();
            GlStateManager.tryBlendFuncSeparate(770, 771, 0, 1);
            GlStateManager.disableTexture2D();
            GlStateManager.depthMask(false);
            GL11.glEnable(2848);
            GL11.glHint(3154, 4354);
            RenderGlobal.renderFilledBox(axisAlignedBB, (float)color.getRed() / 255.0f, (float)color.getGreen() / 255.0f, (float)color.getBlue() / 255.0f, (float)color.getAlpha() / 255.0f);
            GL11.glDisable(2848);
            GlStateManager.depthMask(true);
            GlStateManager.enableDepth();
            GlStateManager.enableTexture2D();
            GlStateManager.disableBlend();
            GlStateManager.popMatrix();
        }
    }

    public static void glrendermethod() {
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(2848);
        GL11.glLineWidth(2.0f);
        GL11.glDisable(3553);
        GL11.glEnable(2884);
        GL11.glDisable(2929);
        double d = RenderUtil.mc.getRenderManager().viewerPosX;
        double d2 = RenderUtil.mc.getRenderManager().viewerPosY;
        double d3 = RenderUtil.mc.getRenderManager().viewerPosZ;
        GL11.glPushMatrix();
        GL11.glTranslated(-d, -d2, -d3);
    }

    public static void renderTwo() {
        GL11.glStencilFunc(512, 0, 15);
        GL11.glStencilOp(7681, 7681, 7681);
        GL11.glPolygonMode(1032, 6914);
    }

    public static Vec3d getInterpolatedPos(Entity entity, float f, boolean bl) {
        Vec3d vec3d = entity.getPositionVector().subtract(entity.prevPosX, entity.prevPosY, entity.prevPosZ).scale(f);
        Vec3d vec3d2 = entity.getPositionVector().add(vec3d);
        if (bl) {
            return vec3d2.subtract(RenderUtil.mc.getRenderManager().renderViewEntity.getPositionVector());
        }
        return vec3d2;
    }


    public static void drawGradientBlockOutline(BlockPos blockPos, Color color, Color color2, float f, double d) {
        IBlockState iBlockState = RenderUtil.mc.world.getBlockState(blockPos);
        Vec3d vec3d = EntityUtil.interpolateEntity(RenderUtil.mc.player, mc.getRenderPartialTicks());
        RenderUtil.drawGradientBlockOutline(iBlockState.getSelectedBoundingBox(RenderUtil.mc.world, blockPos).grow(0.002f).offset(-vec3d.x, -vec3d.y, -vec3d.z).expand(0.0, d, 0.0), color, color2, f);
    }

    public static void glEnd() {
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        GL11.glPopMatrix();
        GL11.glEnable(2929);
        GL11.glEnable(3553);
        GL11.glDisable(3042);
        GL11.glDisable(2848);
    }

    public static void renderFive() {
        GL11.glPolygonOffset(1.0f, 2000000.0f);
        GL11.glDisable(10754);
        GL11.glEnable(2929);
        GL11.glDepthMask(true);
        GL11.glDisable(2960);
        GL11.glDisable(2848);
        GL11.glHint(3154, 4352);
        GL11.glEnable(3042);
        GL11.glEnable(2896);
        GL11.glEnable(3553);
        GL11.glEnable(3008);
        GL11.glPopAttrib();
    }

    public static void drawTracerPointer(float f, float f2, float f3, float f4, float f5, boolean bl, float f6, int n) {
        boolean bl2 = GL11.glIsEnabled(3042);
        float f7 = (float)(n >> 24 & 0xFF) / 255.0f;
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(2848);
        GL11.glPushMatrix();
        RenderUtil.hexColor(n);
        GL11.glBegin(7);
        GL11.glVertex2d(f, f2);
        GL11.glVertex2d(f - f3 / f4, f2 + f3);
        GL11.glVertex2d(f, f2 + f3 / f5);
        GL11.glVertex2d(f + f3 / f4, f2 + f3);
        GL11.glVertex2d(f, f2);
        GL11.glEnd();
        if (bl) {
            GL11.glLineWidth(f6);
            GL11.glColor4f(0.0f, 0.0f, 0.0f, f7);
            GL11.glBegin(2);
            GL11.glVertex2d(f, f2);
            GL11.glVertex2d(f - f3 / f4, f2 + f3);
            GL11.glVertex2d(f, f2 + f3 / f5);
            GL11.glVertex2d(f + f3 / f4, f2 + f3);
            GL11.glVertex2d(f, f2);
            GL11.glEnd();
        }
        GL11.glPopMatrix();
        GL11.glEnable(3553);
        if (!bl2) {
            GL11.glDisable(3042);
        }
        GL11.glDisable(2848);
    }

    public static void drawFadingOutline(AxisAlignedBB axisAlignedBB, Color color, Color color2, float f, double d) {
        float f2 = (float)color.getRed() / 255.0f;
        float f3 = (float)color.getGreen() / 255.0f;
        float f4 = (float)color.getBlue() / 255.0f;
        float f5 = (float)color.getAlpha() / 255.0f;
        float f6 = (float)color2.getRed() / 255.0f;
        float f7 = (float)color2.getGreen() / 255.0f;
        float f8 = (float)color2.getBlue() / 255.0f;
        float f9 = (float)color2.getAlpha() / 255.0f;
        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        GlStateManager.disableDepth();
        GlStateManager.tryBlendFuncSeparate(770, 771, 0, 1);
        GlStateManager.disableTexture2D();
        GlStateManager.depthMask(false);
        GL11.glEnable(2848);
        GL11.glHint(3154, 4354);
        GL11.glLineWidth(f);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferBuilder = tessellator.getBuffer();
        bufferBuilder.begin(3, DefaultVertexFormats.POSITION_COLOR);
        bufferBuilder.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ).color(f6, f7, f8, f9).endVertex();
        bufferBuilder.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.maxZ).color(f6, f7, f8, f9).endVertex();
        bufferBuilder.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.maxZ).color(f6, f7, f8, f9).endVertex();
        bufferBuilder.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.minZ).color(f6, f7, f8, f9).endVertex();
        bufferBuilder.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ).color(f6, f7, f8, f9).endVertex();
        bufferBuilder.pos(axisAlignedBB.minX, axisAlignedBB.maxY + d, axisAlignedBB.minZ).color(f2, f3, f4, f5).endVertex();
        bufferBuilder.pos(axisAlignedBB.minX, axisAlignedBB.maxY + d, axisAlignedBB.maxZ).color(f2, f3, f4, f5).endVertex();
        bufferBuilder.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.maxZ).color(f6, f7, f8, f9).endVertex();
        bufferBuilder.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.maxZ).color(f6, f7, f8, f9).endVertex();
        bufferBuilder.pos(axisAlignedBB.maxX, axisAlignedBB.maxY + d, axisAlignedBB.maxZ).color(f2, f3, f4, f5).endVertex();
        bufferBuilder.pos(axisAlignedBB.minX, axisAlignedBB.maxY + d, axisAlignedBB.maxZ).color(f2, f3, f4, f5).endVertex();
        bufferBuilder.pos(axisAlignedBB.maxX, axisAlignedBB.maxY + d, axisAlignedBB.maxZ).color(f2, f3, f4, f5).endVertex();
        bufferBuilder.pos(axisAlignedBB.maxX, axisAlignedBB.maxY + d, axisAlignedBB.minZ).color(f2, f3, f4, f5).endVertex();
        bufferBuilder.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.minZ).color(f6, f7, f8, f9).endVertex();
        bufferBuilder.pos(axisAlignedBB.maxX, axisAlignedBB.maxY + d, axisAlignedBB.minZ).color(f2, f3, f4, f5).endVertex();
        bufferBuilder.pos(axisAlignedBB.minX, axisAlignedBB.maxY + d, axisAlignedBB.minZ).color(f2, f3, f4, f5).endVertex();
        tessellator.draw();
        GL11.glDisable(2848);
        GlStateManager.depthMask(true);
        GlStateManager.enableDepth();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
    }

    public static void drawBlockOutline(AxisAlignedBB axisAlignedBB, Color color, float f, boolean bl) {
        float f2 = (float)color.getRed() / 255.0f;
        float f3 = (float)color.getGreen() / 255.0f;
        float f4 = (float)color.getBlue() / 255.0f;
        float f5 = (float)color.getAlpha() / 255.0f;
        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        if (bl) {
            GlStateManager.enableDepth();
            GlStateManager.tryBlendFuncSeparate(770, 771, 0, 1);
            GlStateManager.disableTexture2D();
            GlStateManager.depthMask(true);
        } else {
            GlStateManager.disableDepth();
            GlStateManager.tryBlendFuncSeparate(770, 771, 0, 1);
            GlStateManager.disableTexture2D();
            GlStateManager.depthMask(false);
        }
        GL11.glEnable(2848);
        GL11.glHint(3154, 4354);
        GL11.glLineWidth(f);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferBuilder = tessellator.getBuffer();
        bufferBuilder.begin(3, DefaultVertexFormats.POSITION_COLOR);
        bufferBuilder.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ).color(f2, f3, f4, f5).endVertex();
        bufferBuilder.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.maxZ).color(f2, f3, f4, f5).endVertex();
        bufferBuilder.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.maxZ).color(f2, f3, f4, f5).endVertex();
        bufferBuilder.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.minZ).color(f2, f3, f4, f5).endVertex();
        bufferBuilder.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ).color(f2, f3, f4, f5).endVertex();
        bufferBuilder.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.minZ).color(f2, f3, f4, f5).endVertex();
        bufferBuilder.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.maxZ).color(f2, f3, f4, f5).endVertex();
        bufferBuilder.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.maxZ).color(f2, f3, f4, f5).endVertex();
        bufferBuilder.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.maxZ).color(f2, f3, f4, f5).endVertex();
        bufferBuilder.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.maxZ).color(f2, f3, f4, f5).endVertex();
        bufferBuilder.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.maxZ).color(f2, f3, f4, f5).endVertex();
        bufferBuilder.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.maxZ).color(f2, f3, f4, f5).endVertex();
        bufferBuilder.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.minZ).color(f2, f3, f4, f5).endVertex();
        bufferBuilder.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.minZ).color(f2, f3, f4, f5).endVertex();
        bufferBuilder.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.minZ).color(f2, f3, f4, f5).endVertex();
        bufferBuilder.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.minZ).color(f2, f3, f4, f5).endVertex();
        tessellator.draw();
        GL11.glDisable(2848);
        GlStateManager.depthMask(true);
        GlStateManager.enableDepth();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
    }

    public static void releaseGL3D() {
        GlStateManager.depthMask(true);
        GlStateManager.enableLighting();
        GlStateManager.enableTexture2D();
        GL11.glDisable(2848);
        GlStateManager.shadeModel(7424);
        GlStateManager.enableAlpha();
        GlStateManager.enableCull();
        GlStateManager.enableDepth();
        GlStateManager.disableBlend();
        GlStateManager.glLineWidth(1.0f);
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        GlStateManager.popMatrix();
    }

    private static void setupFBO(Framebuffer framebuffer) {
        EXTFramebufferObject.glDeleteRenderbuffersEXT(framebuffer.depthBuffer);
        int n = EXTFramebufferObject.glGenRenderbuffersEXT();
        EXTFramebufferObject.glBindRenderbufferEXT(36161, n);
        EXTFramebufferObject.glRenderbufferStorageEXT(36161, 34041, RenderUtil.mc.displayWidth, RenderUtil.mc.displayHeight);
        EXTFramebufferObject.glFramebufferRenderbufferEXT(36160, 36128, 36161, n);
        EXTFramebufferObject.glFramebufferRenderbufferEXT(36160, 36096, 36161, n);
    }

    public static void renderOne(float f) {
        RenderUtil.checkSetupFBO();
        GL11.glPushAttrib(1048575);
        GL11.glDisable(3008);
        GL11.glDisable(3553);
        GL11.glDisable(2896);
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glLineWidth(f);
        GL11.glEnable(2848);
        GL11.glEnable(2960);
        GL11.glClear(1024);
        GL11.glClearStencil(15);
        GL11.glStencilFunc(512, 1, 15);
        GL11.glStencilOp(7681, 7681, 7681);
        GL11.glPolygonMode(1032, 6913);
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

    public static void drawFadingOutline(BlockPos blockPos, Color color, Color color2, float f, double d) {
        IBlockState iBlockState = RenderUtil.mc.world.getBlockState(blockPos);
        Vec3d vec3d = RenderUtil.getInterpolatedPos(RenderUtil.mc.player, mc.getRenderPartialTicks(), false);
        RenderUtil.drawFadingOutline(iBlockState.getSelectedBoundingBox(RenderUtil.mc.world, blockPos).grow(0.002f).offset(-vec3d.x, -vec3d.y, -vec3d.z).expand(0.0, 0.0, 0.0), color, color2, f, d);
    }

    public static void drawTexturedRect(int n, int n2, int n3, int n4, int n5, int n6, int n7) {
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferBuilder = tessellator.getBuffer();
        bufferBuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
        bufferBuilder.pos(n, n2 + n6, n7).tex((float)n3 * 0.00390625f, (float)(n4 + n6) * 0.00390625f).endVertex();
        bufferBuilder.pos(n + n5, n2 + n6, n7).tex((float)(n3 + n5) * 0.00390625f, (float)(n4 + n6) * 0.00390625f).endVertex();
        bufferBuilder.pos(n + n5, n2, n7).tex((float)(n3 + n5) * 0.00390625f, (float)n4 * 0.00390625f).endVertex();
        bufferBuilder.pos(n, n2, n7).tex((float)n3 * 0.00390625f, (float)n4 * 0.00390625f).endVertex();
        tessellator.draw();
    }

    public static void drawGradientBlockOutline(AxisAlignedBB axisAlignedBB, Color color, Color color2, float f) {
        float f2 = (float)color.getRed() / 255.0f;
        float f3 = (float)color.getGreen() / 255.0f;
        float f4 = (float)color.getBlue() / 255.0f;
        float f5 = (float)color.getAlpha() / 255.0f;
        float f6 = (float)color2.getRed() / 255.0f;
        float f7 = (float)color2.getGreen() / 255.0f;
        float f8 = (float)color2.getBlue() / 255.0f;
        float f9 = (float)color2.getAlpha() / 255.0f;
        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        GlStateManager.disableDepth();
        GlStateManager.tryBlendFuncSeparate(770, 771, 0, 1);
        GlStateManager.disableTexture2D();
        GlStateManager.depthMask(false);
        GL11.glEnable(2848);
        GL11.glHint(3154, 4354);
        GL11.glLineWidth(f);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferBuilder = tessellator.getBuffer();
        bufferBuilder.begin(3, DefaultVertexFormats.POSITION_COLOR);
        bufferBuilder.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ).color(f6, f7, f8, f9).endVertex();
        bufferBuilder.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.maxZ).color(f6, f7, f8, f9).endVertex();
        bufferBuilder.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.maxZ).color(f6, f7, f8, f9).endVertex();
        bufferBuilder.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.minZ).color(f6, f7, f8, f9).endVertex();
        bufferBuilder.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ).color(f6, f7, f8, f9).endVertex();
        bufferBuilder.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.minZ).color(f2, f3, f4, f5).endVertex();
        bufferBuilder.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.maxZ).color(f2, f3, f4, f5).endVertex();
        bufferBuilder.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.maxZ).color(f6, f7, f8, f9).endVertex();
        bufferBuilder.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.maxZ).color(f6, f7, f8, f9).endVertex();
        bufferBuilder.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.maxZ).color(f2, f3, f4, f5).endVertex();
        bufferBuilder.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.maxZ).color(f2, f3, f4, f5).endVertex();
        bufferBuilder.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.maxZ).color(f2, f3, f4, f5).endVertex();
        bufferBuilder.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.minZ).color(f2, f3, f4, f5).endVertex();
        bufferBuilder.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.minZ).color(f6, f7, f8, f9).endVertex();
        bufferBuilder.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.minZ).color(f2, f3, f4, f5).endVertex();
        bufferBuilder.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.minZ).color(f2, f3, f4, f5).endVertex();
        tessellator.draw();
        GL11.glDisable(2848);
        GlStateManager.depthMask(true);
        GlStateManager.enableDepth();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
    }

    private static void GLPre(boolean bl, boolean bl2, boolean bl3, boolean bl4, boolean bl5, float f) {
        if (bl) {
            GL11.glDisable(2896);
        }
        if (!bl2) {
            GL11.glEnable(3042);
        }
        GL11.glLineWidth(f);
        if (bl3) {
            GL11.glDisable(3553);
        }
        if (bl4) {
            GL11.glDisable(2929);
        }
        if (!bl5) {
            GL11.glEnable(2848);
        }
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        GL11.glHint(3154, 4354);
        GlStateManager.depthMask(false);
    }

    public static void drawCircle(float f, float f2, float f3, int n, int n2, int n3) {
        RenderUtil.drawArc(f, f2, f3, n, n2, n3);
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

    public static void drawBoxESP(AxisAlignedBB axisAlignedBB, Color color, boolean bl, Color color2, float f, boolean bl2, boolean bl3, int n, boolean bl4) {
        AxisAlignedBB axisAlignedBB2 = axisAlignedBB.offset(-RenderUtil.mc.getRenderManager().viewerPosX, -RenderUtil.mc.getRenderManager().viewerPosY, -RenderUtil.mc.getRenderManager().viewerPosZ);
        camera.setPosition(Objects.requireNonNull(RenderUtil.mc.getRenderViewEntity()).posX, RenderUtil.mc.getRenderViewEntity().posY, RenderUtil.mc.getRenderViewEntity().posZ);
        if (camera.isBoundingBoxInFrustum(axisAlignedBB)) {
            GlStateManager.pushMatrix();
            GlStateManager.enableBlend();
            if (bl4) {
                GlStateManager.enableDepth();
                GlStateManager.tryBlendFuncSeparate(770, 771, 0, 1);
                GlStateManager.disableTexture2D();
                GlStateManager.depthMask(true);
            } else {
                GlStateManager.disableDepth();
                GlStateManager.tryBlendFuncSeparate(770, 771, 0, 1);
                GlStateManager.disableTexture2D();
                GlStateManager.depthMask(false);
            }
            GL11.glEnable(2848);
            GL11.glHint(3154, 4354);
            GL11.glLineWidth(f);
            if (bl3) {
                RenderGlobal.renderFilledBox(axisAlignedBB2, (float)color.getRed() / 255.0f, (float)color.getGreen() / 255.0f, (float)color.getBlue() / 255.0f, (float)n / 255.0f);
            }
            if (bl2) {
                RenderUtil.drawBlockOutline(axisAlignedBB2, bl ? color2 : color, f, bl4);
            }
            GL11.glDisable(2848);
            GlStateManager.depthMask(true);
            GlStateManager.enableDepth();
            GlStateManager.enableTexture2D();
            GlStateManager.disableBlend();
            GlStateManager.popMatrix();
        }
    }

    public static void renderFour(Color color) {
        RenderUtil.setColor(color);
        GL11.glDepthMask(false);
        GL11.glDisable(2929);
        GL11.glEnable(10754);
        GL11.glPolygonOffset(1.0f, -2000000.0f);
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240.0f, 240.0f);
    }

    public static void drawArcOutline(float f, float f2, float f3, float f4, float f5, int n) {
        GL11.glBegin(2);
        int n2 = (int)((float)n / (360.0f / f4)) + 1;
        while ((float)n2 <= (float)n / (360.0f / f5)) {
            double d = Math.PI * 2 * (double)n2 / (double)n;
            GL11.glVertex2d((double)f + Math.cos(d) * (double)f3, (double)f2 + Math.sin(d) * (double)f3);
            ++n2;
        }
        RenderUtil.glEnd();
    }

    private static void doVerticies(AxisAlignedBB axisAlignedBB, Color color, int n, BufferBuilder bufferBuilder, int n2, boolean bl) {
        if ((n2 & 0x20) != 0) {
            RenderUtil.colorVertex(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.maxZ, color, color.getAlpha(), bufferBuilder);
            RenderUtil.colorVertex(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.minZ, color, color.getAlpha(), bufferBuilder);
            RenderUtil.colorVertex(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.minZ, color, n, bufferBuilder);
            RenderUtil.colorVertex(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.maxZ, color, n, bufferBuilder);
            if (bl) {
                RenderUtil.colorVertex(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.maxZ, color, color.getAlpha(), bufferBuilder);
            }
        }
        if ((n2 & 0x10) != 0) {
            RenderUtil.colorVertex(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ, color, color.getAlpha(), bufferBuilder);
            RenderUtil.colorVertex(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.maxZ, color, color.getAlpha(), bufferBuilder);
            RenderUtil.colorVertex(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.maxZ, color, n, bufferBuilder);
            RenderUtil.colorVertex(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.minZ, color, n, bufferBuilder);
            if (bl) {
                RenderUtil.colorVertex(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ, color, color.getAlpha(), bufferBuilder);
            }
        }
        if ((n2 & 4) != 0) {
            RenderUtil.colorVertex(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.minZ, color, color.getAlpha(), bufferBuilder);
            RenderUtil.colorVertex(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ, color, color.getAlpha(), bufferBuilder);
            RenderUtil.colorVertex(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.minZ, color, n, bufferBuilder);
            RenderUtil.colorVertex(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.minZ, color, n, bufferBuilder);
            if (bl) {
                RenderUtil.colorVertex(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.minZ, color, color.getAlpha(), bufferBuilder);
            }
        }
        if ((n2 & 8) != 0) {
            RenderUtil.colorVertex(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.maxZ, color, color.getAlpha(), bufferBuilder);
            RenderUtil.colorVertex(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.maxZ, color, color.getAlpha(), bufferBuilder);
            RenderUtil.colorVertex(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.maxZ, color, n, bufferBuilder);
            RenderUtil.colorVertex(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.maxZ, color, n, bufferBuilder);
            if (bl) {
                RenderUtil.colorVertex(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.maxZ, color, color.getAlpha(), bufferBuilder);
            }
        }
        if ((n2 & 2) != 0) {
            RenderUtil.colorVertex(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.minZ, color, n, bufferBuilder);
            RenderUtil.colorVertex(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.minZ, color, n, bufferBuilder);
            RenderUtil.colorVertex(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.maxZ, color, n, bufferBuilder);
            RenderUtil.colorVertex(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.maxZ, color, n, bufferBuilder);
            if (bl) {
                RenderUtil.colorVertex(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.minZ, color, n, bufferBuilder);
            }
        }
        if ((n2 & 1) != 0) {
            RenderUtil.colorVertex(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.minZ, color, color.getAlpha(), bufferBuilder);
            RenderUtil.colorVertex(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.maxZ, color, color.getAlpha(), bufferBuilder);
            RenderUtil.colorVertex(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.maxZ, color, color.getAlpha(), bufferBuilder);
            RenderUtil.colorVertex(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ, color, color.getAlpha(), bufferBuilder);
            if (bl) {
                RenderUtil.colorVertex(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.minZ, color, color.getAlpha(), bufferBuilder);
            }
        }
    }

    public static void drawBox(BlockPos blockPos, Color color, double d, boolean bl, boolean bl2, int n) {
        if (bl) {
            Color color2 = new Color(color.getRed(), color.getGreen(), color.getBlue(), n);
            RenderUtil.drawOpenGradientBox(blockPos, bl2 ? color2 : color, bl2 ? color : color2, d);
            return;
        }
        AxisAlignedBB axisAlignedBB = new AxisAlignedBB((double)blockPos.getX() - RenderUtil.mc.getRenderManager().viewerPosX, (double)blockPos.getY() - RenderUtil.mc.getRenderManager().viewerPosY, (double)blockPos.getZ() - RenderUtil.mc.getRenderManager().viewerPosZ, (double)(blockPos.getX() + 1) - RenderUtil.mc.getRenderManager().viewerPosX, (double)(blockPos.getY() + 1) - RenderUtil.mc.getRenderManager().viewerPosY + d, (double)(blockPos.getZ() + 1) - RenderUtil.mc.getRenderManager().viewerPosZ);
        camera.setPosition(Objects.requireNonNull(RenderUtil.mc.getRenderViewEntity()).posX, RenderUtil.mc.getRenderViewEntity().posY, RenderUtil.mc.getRenderViewEntity().posZ);
        if (camera.isBoundingBoxInFrustum(new AxisAlignedBB(axisAlignedBB.minX + RenderUtil.mc.getRenderManager().viewerPosX, axisAlignedBB.minY + RenderUtil.mc.getRenderManager().viewerPosY, axisAlignedBB.minZ + RenderUtil.mc.getRenderManager().viewerPosZ, axisAlignedBB.maxX + RenderUtil.mc.getRenderManager().viewerPosX, axisAlignedBB.maxY + RenderUtil.mc.getRenderManager().viewerPosY, axisAlignedBB.maxZ + RenderUtil.mc.getRenderManager().viewerPosZ))) {
            GlStateManager.pushMatrix();
            GlStateManager.enableBlend();
            GlStateManager.disableDepth();
            GlStateManager.tryBlendFuncSeparate(770, 771, 0, 1);
            GlStateManager.disableTexture2D();
            GlStateManager.depthMask(false);
            GL11.glEnable(2848);
            GL11.glHint(3154, 4354);
            RenderGlobal.renderFilledBox(axisAlignedBB, (float)color.getRed() / 255.0f, (float)color.getGreen() / 255.0f, (float)color.getBlue() / 255.0f, (float)color.getAlpha() / 255.0f);
            GL11.glDisable(2848);
            GlStateManager.depthMask(true);
            GlStateManager.enableDepth();
            GlStateManager.enableTexture2D();
            GlStateManager.disableBlend();
            GlStateManager.popMatrix();
        }
    }

    static {
        $assertionsDisabled = !RenderUtil.class.desiredAssertionStatus();
        itemRender = mc.getRenderItem();
        camera = new Frustum();
        frustrum = new Frustum();
        depth = GL11.glIsEnabled(2896);
        texture = GL11.glIsEnabled(3042);
        clean = GL11.glIsEnabled(3553);
        bind = GL11.glIsEnabled(2929);
        override = GL11.glIsEnabled(2848);
        screenCoords = BufferUtils.createFloatBuffer(3);
        viewport = BufferUtils.createIntBuffer(16);
        modelView = BufferUtils.createFloatBuffer(16);
        projection = BufferUtils.createFloatBuffer(16);
    }

    public static void drawCircle(float f, float f2, float f3) {
        RenderUtil.drawCircle(f, f2, f3, 0, 360, 64);
    }

    public static void drawRoundedRectangle(float f, float f2, float f3, float f4, float f5) {
        GL11.glEnable(3042);
        RenderUtil.drawArc(f + f3 - f5, f2 + f4 - f5, f5, 0.0f, 90.0f, 16);
        RenderUtil.drawArc(f + f5, f2 + f4 - f5, f5, 90.0f, 180.0f, 16);
        RenderUtil.drawArc(f + f5, f2 + f5, f5, 180.0f, 270.0f, 16);
        RenderUtil.drawArc(f + f3 - f5, f2 + f5, f5, 270.0f, 360.0f, 16);
        GL11.glBegin(4);
        GL11.glVertex2d(f + f3 - f5, f2);
        GL11.glVertex2d(f + f5, f2);
        GL11.glVertex2d(f + f3 - f5, f2 + f5);
        GL11.glVertex2d(f + f3 - f5, f2 + f5);
        GL11.glVertex2d(f + f5, f2);
        GL11.glVertex2d(f + f5, f2 + f5);
        GL11.glVertex2d(f + f3, f2 + f5);
        GL11.glVertex2d(f, f2 + f5);
        GL11.glVertex2d(f, f2 + f4 - f5);
        GL11.glVertex2d(f + f3, f2 + f5);
        GL11.glVertex2d(f, f2 + f4 - f5);
        GL11.glVertex2d(f + f3, f2 + f4 - f5);
        GL11.glVertex2d(f + f3 - f5, f2 + f4 - f5);
        GL11.glVertex2d(f + f5, f2 + f4 - f5);
        GL11.glVertex2d(f + f3 - f5, f2 + f4);
        GL11.glVertex2d(f + f3 - f5, f2 + f4);
        GL11.glVertex2d(f + f5, f2 + f4 - f5);
        GL11.glVertex2d(f + f5, f2 + f4);
        RenderUtil.glEnd();
    }

    public static void hexColor(int n) {
        float f = (float)(n >> 16 & 0xFF) / 255.0f;
        float f2 = (float)(n >> 8 & 0xFF) / 255.0f;
        float f3 = (float)(n & 0xFF) / 255.0f;
        float f4 = (float)(n >> 24 & 0xFF) / 255.0f;
        GL11.glColor4f(f, f2, f3, f4);
    }

    public static void drawBoundingBox(AxisAlignedBB axisAlignedBB, double d, Color color, int n) {
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferBuilder = tessellator.getBuffer();
        GlStateManager.glLineWidth((float)d);
        bufferBuilder.begin(3, DefaultVertexFormats.POSITION_COLOR);
        RenderUtil.colorVertex(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ, color, color.getAlpha(), bufferBuilder);
        RenderUtil.colorVertex(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.maxZ, color, color.getAlpha(), bufferBuilder);
        RenderUtil.colorVertex(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.maxZ, color, color.getAlpha(), bufferBuilder);
        RenderUtil.colorVertex(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.minZ, color, color.getAlpha(), bufferBuilder);
        RenderUtil.colorVertex(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ, color, color.getAlpha(), bufferBuilder);
        RenderUtil.colorVertex(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.minZ, color, n, bufferBuilder);
        RenderUtil.colorVertex(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.maxZ, color, n, bufferBuilder);
        RenderUtil.colorVertex(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.maxZ, color, color.getAlpha(), bufferBuilder);
        RenderUtil.colorVertex(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.maxZ, color, color.getAlpha(), bufferBuilder);
        RenderUtil.colorVertex(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.maxZ, color, n, bufferBuilder);
        RenderUtil.colorVertex(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.maxZ, color, n, bufferBuilder);
        RenderUtil.colorVertex(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.maxZ, color, n, bufferBuilder);
        RenderUtil.colorVertex(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.minZ, color, n, bufferBuilder);
        RenderUtil.colorVertex(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.minZ, color, color.getAlpha(), bufferBuilder);
        RenderUtil.colorVertex(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.minZ, color, n, bufferBuilder);
        RenderUtil.colorVertex(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.minZ, color, n, bufferBuilder);
        tessellator.draw();
    }

}

