package me.madcat.util.shader.framebuffer;

import java.awt.Color;
import me.madcat.mixin.mixins.IEntityRenderer;
import me.madcat.util.shader.Shader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.shader.Framebuffer;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

public abstract class FramebufferShader
extends Shader {
    protected float quality = 1.0f;
    private static Framebuffer framebuffer;
    protected float green;
    protected float alpha = 1.0f;
    protected float radius = 2.0f;
    protected static int lastScaleWidth;
    protected static int lastScaleHeight;
    protected static int lastScale;
    private boolean entityShadows;
    protected float blue;
    protected float red;

    public void stopDraw(Color color, float f, float f2) {
        this.mc.gameSettings.entityShadows = this.entityShadows;
        GlStateManager.enableBlend();
        GL11.glBlendFunc(770, 771);
        this.mc.getFramebuffer().bindFramebuffer(true);
        this.red = (float)color.getRed() / 255.0f;
        this.green = (float)color.getGreen() / 255.0f;
        this.blue = (float)color.getBlue() / 255.0f;
        this.alpha = (float)color.getAlpha() / 255.0f;
        this.radius = f;
        this.quality = f2;
        this.mc.entityRenderer.disableLightmap();
        RenderHelper.disableStandardItemLighting();
        this.startShader();
        this.mc.entityRenderer.setupOverlayRendering();
        this.drawFramebuffer(framebuffer);
        this.stopShader();
        this.mc.entityRenderer.disableLightmap();
        GlStateManager.popMatrix();
        GlStateManager.popAttrib();
    }

    public FramebufferShader(String string) {
        super(string);
    }

    public Framebuffer setupFrameBuffer(Framebuffer framebuffer) {
        if (Display.isActive() || Display.isVisible()) {
            if (framebuffer != null) {
                framebuffer.framebufferClear();
                ScaledResolution scaledResolution = new ScaledResolution(Minecraft.getMinecraft());
                int n = scaledResolution.getScaleFactor();
                int n2 = scaledResolution.getScaledWidth();
                int n3 = scaledResolution.getScaledHeight();
                if (lastScale != n || lastScaleWidth != n2 || lastScaleHeight != n3) {
                    framebuffer.deleteFramebuffer();
                    framebuffer = new Framebuffer(this.mc.displayWidth, this.mc.displayHeight, true);
                    framebuffer.framebufferClear();
                }
                lastScale = n;
                lastScaleWidth = n2;
                lastScaleHeight = n3;
            } else {
                framebuffer = new Framebuffer(this.mc.displayWidth, this.mc.displayHeight, true);
            }
        } else if (framebuffer == null) {
            framebuffer = new Framebuffer(this.mc.displayWidth, this.mc.displayHeight, true);
        }
        return framebuffer;
    }

    public void startDraw(float f) {
        GlStateManager.enableAlpha();
        GlStateManager.pushMatrix();
        GlStateManager.pushAttrib();
        framebuffer = this.setupFrameBuffer(framebuffer);
        framebuffer.bindFramebuffer(true);
        this.entityShadows = this.mc.gameSettings.entityShadows;
        this.mc.gameSettings.entityShadows = false;
        ((IEntityRenderer)this.mc.entityRenderer).invokeSetupCameraTransform(f, 0);
    }

    public void drawFramebuffer(Framebuffer framebuffer) {
        ScaledResolution scaledResolution = new ScaledResolution(this.mc);
        GL11.glBindTexture(3553, framebuffer.framebufferTexture);
        GL11.glBegin(7);
        GL11.glTexCoord2d(0.0, 1.0);
        GL11.glVertex2d(0.0, 0.0);
        GL11.glTexCoord2d(0.0, 0.0);
        GL11.glVertex2d(0.0, scaledResolution.getScaledHeight());
        GL11.glTexCoord2d(1.0, 0.0);
        GL11.glVertex2d(scaledResolution.getScaledWidth(), scaledResolution.getScaledHeight());
        GL11.glTexCoord2d(1.0, 1.0);
        GL11.glVertex2d(scaledResolution.getScaledWidth(), 0.0);
        GL11.glEnd();
        GL20.glUseProgram(0);
    }
}

