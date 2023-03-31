package me.madcat.features.modules.render;

import java.awt.Color;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.Arrays;
import java.util.List;
import javax.vecmath.Vector3d;
import javax.vecmath.Vector4d;
import me.madcat.event.events.Render2DEvent;
import me.madcat.event.events.Render3DEvent;
import me.madcat.features.modules.Module;
import me.madcat.features.setting.Setting;
import me.madcat.util.ColorUtil;
import me.madcat.util.DrawHelper;
import me.madcat.util.MathUtil;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

public class EntityESP
extends Module {
    private final Setting<Integer> espAlpha;
    private final Setting<colorModeEn> colorMode;
    private final Setting<healcolorModeEn> healcolorMode;
    private final Setting<Integer> espRed;
    private final int black;
    public final Setting<Boolean> fullBox;
    public final Setting<Boolean> border = this.register(new Setting<>("Border Rect", true));
    private final Setting<Integer> espGreen;
    private final Setting<Integer> healthAlpha;
    private final Setting<csgoModeEn> csgoMode;
    private final Setting<Integer> healthBlue;
    private final Setting<espModeEn> espMode;
    public final Setting<Boolean> ignoreInvisible;
    private final Setting<Integer> espBlue;
    public final Setting<Boolean> heathPercentage;
    private final Setting<rectModeEn> rectMode;
    private final Setting<Integer> healthRed;
    private final Setting<Integer> healthGreen;
    public final Setting<Boolean> healRect;

    public EntityESP() {
        super("EntityESP", "thunder", Module.Category.RENDER);
        this.fullBox = this.register(new Setting<>("Full Box", false));
        this.heathPercentage = this.register(new Setting<>("HealthPercent", true));
        this.healRect = this.register(new Setting<>("HealthRect", true));
        this.ignoreInvisible = this.register(new Setting<>("IgnoreInvisible", true));
        this.black = Color.BLACK.getRGB();
        this.healcolorMode = this.register(new Setting<>("HealthMode", healcolorModeEn.Custom));
        this.colorMode = this.register(new Setting<>("ColorBoxMode", colorModeEn.Custom));
        this.espMode = this.register(new Setting<>("espMode", espModeEn.Flat));
        this.rectMode = this.register(new Setting<>("RectMode", rectModeEn.Default));
        this.csgoMode = this.register(new Setting<>("csgoMode", csgoModeEn.Box));
        this.espRed = this.register(new Setting<>("espRed", 255, 0, 255));
        this.espGreen = this.register(new Setting<>("espGreen", 255, 0, 255));
        this.espBlue = this.register(new Setting<>("espBlue", 255, 0, 255));
        this.espAlpha = this.register(new Setting<>("espAlpha", 255, 0, 255));
        this.healthRed = this.register(new Setting<>("healthRed", 255, 0, 255));
        this.healthGreen = this.register(new Setting<>("healthGreen", 255, 0, 255));
        this.healthBlue = this.register(new Setting<>("healthBlue", 255, 0, 255));
        this.healthAlpha = this.register(new Setting<>("healthAlpha", 255, 0, 255));
    }

    @Override
    @SubscribeEvent
    public void onRender2D(Render2DEvent render2DEvent) {
        ScaledResolution scaledResolution = new ScaledResolution(mc);
        int n = scaledResolution.getScaleFactor();
        double d = (double)n / Math.pow(n, 2.0);
        GL11.glPushMatrix();
        GlStateManager.scale(d, d, d);
        int n2 = 0;
        switch (this.colorMode.getValue()) {
            case Custom: {
                n2 = ColorUtil.toRGBA(this.espRed.getValue(), this.espGreen.getValue(), this.espBlue.getValue(), this.espAlpha.getValue());
                break;
            }
            case Astolfo: {
                n2 = DrawHelper.astolfo(false, 1).getRGB();
                break;
            }
            case Rainbow: {
                n2 = DrawHelper.rainbow(300, 1.0f, 1.0f).getRGB();
            }
        }
        for (Entity entity : EntityESP.mc.world.loadedEntityList) {
            if (!(entity instanceof EntityPlayer) || entity == EntityESP.mc.player) continue;
            if (this.ignoreInvisible.getValue() && entity.isInvisible()) {
                continue;
            }
            if (!this.isValid(entity) || !DrawHelper.isInViewFrustum(entity)) continue;
            double d2 = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * (double)mc.getRenderPartialTicks();
            double d3 = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * (double)mc.getRenderPartialTicks();
            double d4 = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * (double)mc.getRenderPartialTicks();
            double d5 = (double)entity.width / 1.5;
            double d6 = (double)entity.height + (entity.isSneaking() || entity == EntityESP.mc.player && EntityESP.mc.player.isSneaking() ? -0.3 : 0.2);
            AxisAlignedBB axisAlignedBB = new AxisAlignedBB(d2 - d5, d3, d4 - d5, d2 + d5, d3 + d6, d4 + d5);
            List<Vector3d> list = Arrays.asList(new Vector3d(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ), new Vector3d(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.minZ), new Vector3d(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.minZ), new Vector3d(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.minZ), new Vector3d(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.maxZ), new Vector3d(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.maxZ), new Vector3d(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.maxZ), new Vector3d(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.maxZ));
            Vector4d vector4d = null;
            for (Vector3d vector3d : list) {
                vector3d = this.vectorRender2D(n, vector3d.x - EntityESP.mc.getRenderManager().viewerPosX, vector3d.y - EntityESP.mc.getRenderManager().viewerPosY, vector3d.z - EntityESP.mc.getRenderManager().viewerPosZ);
                if (vector3d == null || !(vector3d.z > 0.0) || !(vector3d.z < 1.0)) continue;
                if (vector4d == null) {
                    vector4d = new Vector4d(vector3d.x, vector3d.y, vector3d.z, 0.0);
                }
                vector4d.x = Math.min(vector3d.x, vector4d.x);
                vector4d.y = Math.min(vector3d.y, vector4d.y);
                vector4d.z = Math.max(vector3d.x, vector4d.z);
                vector4d.w = Math.max(vector3d.y, vector4d.w);
            }
            if (vector4d == null) continue;
            EntityESP.mc.entityRenderer.setupOverlayRendering();
            double d7 = vector4d.x;
            double d8 = vector4d.y;
            double d9 = vector4d.z;
            double d10 = vector4d.w;
            if (this.border.getValue()) {
                if (this.espMode.getValue() == espModeEn.Flat && this.csgoMode.getValue() == csgoModeEn.Box && this.rectMode.getValue() == rectModeEn.Smooth) {
                    DrawHelper.drawSmoothRect(d7 - 0.5, d8 - 0.5, d9 + 0.5, d8 + 0.5 + 1.0, this.black);
                    DrawHelper.drawSmoothRect(d7 - 0.5, d10 - 0.5 - 1.0, d9 + 0.5, d10 + 0.5, this.black);
                    DrawHelper.drawSmoothRect(d7 - 1.5, d8, d7 + 0.5, d10 + 0.5, this.black);
                    DrawHelper.drawSmoothRect(d9 - 0.5 - 1.0, d8, d9 + 0.5, d10 + 0.5, this.black);
                    DrawHelper.drawSmoothRect(d7 - 1.0, d8, d7 + 0.5 - 0.5, d10, n2);
                    DrawHelper.drawSmoothRect(d7, d10 - 1.0, d9, d10, n2);
                    DrawHelper.drawSmoothRect(d7 - 1.0, d8, d9, d8 + 1.0, n2);
                    DrawHelper.drawSmoothRect(d9 - 1.0, d8, d9, d10, n2);
                } else if (this.espMode.getValue() == espModeEn.Flat && this.csgoMode.getValue() == csgoModeEn.Corner && this.rectMode.getValue() == rectModeEn.Smooth) {
                    DrawHelper.drawSmoothRect(d7 + 1.0, d8, d7 - 1.0, d8 + (d10 - d8) / 4.0 + 0.5, this.black);
                    DrawHelper.drawSmoothRect(d7 - 1.0, d10, d7 + 1.0, d10 - (d10 - d8) / 4.0 - 0.5, this.black);
                    DrawHelper.drawSmoothRect(d7 - 1.0, d8 - 0.5, d7 + (d9 - d7) / 3.0, d8 + 1.0, this.black);
                    DrawHelper.drawSmoothRect(d9 - (d9 - d7) / 3.0 - 0.0, d8 - 0.5, d9, d8 + 1.5, this.black);
                    DrawHelper.drawSmoothRect(d9 - 1.5, d8, d9 + 0.5, d8 + (d10 - d8) / 4.0 + 0.5, this.black);
                    DrawHelper.drawSmoothRect(d9 - 1.5, d10, d9 + 0.5, d10 - (d10 - d8) / 4.0 - 0.5, this.black);
                    DrawHelper.drawSmoothRect(d7 - 1.0, d10 - 1.5, d7 + (d9 - d7) / 3.0 + 0.5, d10 + 0.5, this.black);
                    DrawHelper.drawSmoothRect(d9 - (d9 - d7) / 3.0 - 0.5, d10 - 1.5, d9 + 0.5, d10 + 0.5, this.black);
                    DrawHelper.drawSmoothRect(d7 + 0.5, d8, d7 - 0.5, d8 + (d10 - d8) / 4.0, n2);
                    DrawHelper.drawSmoothRect(d7 + 0.5, d10, d7 - 0.5, d10 - (d10 - d8) / 4.0, n2);
                    DrawHelper.drawSmoothRect(d7 - 0.5, d8, d7 + (d9 - d7) / 3.0, d8 + 1.0, n2);
                    DrawHelper.drawSmoothRect(d9 - (d9 - d7) / 3.0 + 0.5, d8, d9, d8 + 1.0, n2);
                    DrawHelper.drawSmoothRect(d9 - 1.0, d8, d9, d8 + (d10 - d8) / 4.0 + 0.5, n2);
                    DrawHelper.drawSmoothRect(d9 - 1.0, d10, d9, d10 - (d10 - d8) / 4.0, n2);
                    DrawHelper.drawSmoothRect(d7, d10 - 1.0, d7 + (d9 - d7) / 3.0, d10, n2);
                    DrawHelper.drawSmoothRect(d9 - (d9 - d7) / 3.0, d10 - 1.0, d9 - 0.5, d10, n2);
                } else if (this.espMode.getValue() == espModeEn.Flat && this.csgoMode.getValue() == csgoModeEn.Box && this.rectMode.getValue() == rectModeEn.Default) {
                    DrawHelper.drawNewRect(d7 - 0.5, d8 - 0.5, d9 + 0.5, d8 + 0.5 + 1.0, this.black);
                    DrawHelper.drawNewRect(d7 - 0.5, d10 - 0.5 - 1.0, d9 + 0.5, d10 + 0.5, this.black);
                    DrawHelper.drawNewRect(d7 - 1.5, d8, d7 + 0.5, d10 + 0.5, this.black);
                    DrawHelper.drawNewRect(d9 - 0.5 - 1.0, d8, d9 + 0.5, d10 + 0.5, this.black);
                    DrawHelper.drawNewRect(d7 - 1.0, d8, d7 + 0.5 - 0.5, d10, n2);
                    DrawHelper.drawNewRect(d7, d10 - 1.0, d9, d10, n2);
                    DrawHelper.drawNewRect(d7 - 1.0, d8, d9, d8 + 1.0, n2);
                    DrawHelper.drawNewRect(d9 - 1.0, d8, d9, d10, n2);
                } else if (this.espMode.getValue() == espModeEn.Flat && this.csgoMode.getValue() == csgoModeEn.Corner && this.rectMode.getValue() == rectModeEn.Default) {
                    DrawHelper.drawNewRect(d7 + 1.0, d8, d7 - 1.0, d8 + (d10 - d8) / 4.0 + 0.5, this.black);
                    DrawHelper.drawNewRect(d7 - 1.0, d10, d7 + 1.0, d10 - (d10 - d8) / 4.0 - 0.5, this.black);
                    DrawHelper.drawNewRect(d7 - 1.0, d8 - 0.5, d7 + (d9 - d7) / 3.0, d8 + 1.0, this.black);
                    DrawHelper.drawNewRect(d9 - (d9 - d7) / 3.0 - 0.0, d8 - 0.5, d9, d8 + 1.5, this.black);
                    DrawHelper.drawNewRect(d9 - 1.5, d8, d9 + 0.5, d8 + (d10 - d8) / 4.0 + 0.5, this.black);
                    DrawHelper.drawNewRect(d9 - 1.5, d10, d9 + 0.5, d10 - (d10 - d8) / 4.0 - 0.5, this.black);
                    DrawHelper.drawNewRect(d7 - 1.0, d10 - 1.5, d7 + (d9 - d7) / 3.0 + 0.5, d10 + 0.5, this.black);
                    DrawHelper.drawNewRect(d9 - (d9 - d7) / 3.0 - 0.5, d10 - 1.5, d9 + 0.5, d10 + 0.5, this.black);
                    DrawHelper.drawNewRect(d7 + 0.5, d8, d7 - 0.5, d8 + (d10 - d8) / 4.0, n2);
                    DrawHelper.drawNewRect(d7 + 0.5, d10, d7 - 0.5, d10 - (d10 - d8) / 4.0, n2);
                    DrawHelper.drawNewRect(d7 - 0.5, d8, d7 + (d9 - d7) / 3.0, d8 + 1.0, n2);
                    DrawHelper.drawNewRect(d9 - (d9 - d7) / 3.0 + 0.5, d8, d9, d8 + 1.0, n2);
                    DrawHelper.drawNewRect(d9 - 1.0, d8, d9, d8 + (d10 - d8) / 4.0 + 0.5, n2);
                    DrawHelper.drawNewRect(d9 - 1.0, d10, d9, d10 - (d10 - d8) / 4.0, n2);
                    DrawHelper.drawNewRect(d7, d10 - 1.0, d7 + (d9 - d7) / 3.0, d10, n2);
                    DrawHelper.drawNewRect(d9 - (d9 - d7) / 3.0, d10 - 1.0, d9 - 0.5, d10, n2);
                }
            }
            EntityLivingBase entityLivingBase = (EntityLivingBase)entity;
            float f2 = entityLivingBase.getHealth();
            f2 = MathHelper.clamp(f2, 0.0f, 24.0f);
            float f3 = entityLivingBase.getMaxHealth();
            double d11 = f2 / f3;
            double d12 = (d10 - d8) * d11;
            if (!this.healRect.getValue() || this.espMode.getValue() == espModeEn.Box) continue;
            int n3 = 0;
            switch (this.healcolorMode.getValue()) {
                case Custom: {
                    n3 = ColorUtil.toRGBA(this.healthRed.getValue(), this.healthGreen.getValue(), this.healthBlue.getValue(), this.healthAlpha.getValue());
                    break;
                }
                case Astolfo: {
                    n3 = DrawHelper.astolfo(false, (int)entity.height).getRGB();
                    break;
                }
                case Rainbow: {
                    n3 = DrawHelper.rainbow(300, 1.0f, 1.0f).getRGB();
                    break;
                }
                case Health: {
                    n3 = DrawHelper.getHealthColor(((EntityLivingBase)entity).getHealth(), ((EntityLivingBase)entity).getMaxHealth());
                }
            }
            if (!(f2 > 0.0f)) continue;
            String string = "" + MathUtil.round(entityLivingBase.getHealth() / entityLivingBase.getMaxHealth() * 20.0f, 1);
            if (this.heathPercentage.getValue() && this.espMode.getValue() != espModeEn.Box && this.heathPercentage.getValue()) {
                GlStateManager.pushMatrix();
                EntityESP.mc.fontRenderer.drawStringWithShadow(string, (float)d7 - 30.0f, (float)((double)((float)d10) - d12), -1);
                GlStateManager.popMatrix();
            }
            DrawHelper.drawRect(d7 - 5.0, d8 - 0.5, d7 - 2.5, d10 + 0.5, new Color(0, 0, 0, 125).getRGB());
            DrawHelper.drawRect(d7 - 4.5, d10, d7 - 3.0, d10 - d12, n3);
        }
        GL11.glEnable(2929);
        GlStateManager.enableBlend();
        GL11.glPopMatrix();
        EntityESP.mc.entityRenderer.setupOverlayRendering();
    }

    private boolean isValid(Entity entity) {
        if (EntityESP.mc.gameSettings.thirdPersonView == 0 && entity == EntityESP.mc.player) {
            return false;
        }
        if (entity.isDead) {
            return false;
        }
        if (entity instanceof EntityAnimal) {
            return false;
        }
        return entity instanceof EntityPlayer;
    }

    @Override
    @SubscribeEvent
    public void onRender3D(Render3DEvent render3DEvent) {
        int n = 0;
        switch (this.colorMode.getValue()) {
            case Custom: {
                n = ColorUtil.toRGBA(this.espRed.getValue(), this.espGreen.getValue(), this.espBlue.getValue(), this.espAlpha.getValue());
                break;
            }
            case Astolfo: {
                n = DrawHelper.astolfo(false, 10).getRGB();
                break;
            }
            case Rainbow: {
                n = DrawHelper.rainbow(300, 1.0f, 1.0f).getRGB();
            }
        }
        if (this.espMode.getValue() == espModeEn.Box) {
            GlStateManager.pushMatrix();
            for (Entity entity : EntityESP.mc.world.loadedEntityList) {
                if (!(entity instanceof EntityPlayer) || entity == EntityESP.mc.player) continue;
                DrawHelper.drawEntityBox(entity, new Color(n), this.fullBox.getValue(), this.fullBox.getValue() ? 0.15f : 0.9f);
            }
            GlStateManager.popMatrix();
        }
    }

    private Vector3d vectorRender2D(int n, double d, double d2, double d3) {
        float f = (float)d;
        float f2 = (float)d2;
        float f3 = (float)d3;
        IntBuffer intBuffer = GLAllocation.createDirectIntBuffer(16);
        FloatBuffer floatBuffer = GLAllocation.createDirectFloatBuffer(16);
        FloatBuffer floatBuffer2 = GLAllocation.createDirectFloatBuffer(16);
        FloatBuffer floatBuffer3 = GLAllocation.createDirectFloatBuffer(4);
        GL11.glGetFloat(2982, floatBuffer);
        GL11.glGetFloat(2983, floatBuffer2);
        GL11.glGetInteger(2978, intBuffer);
        if (GLU.gluProject(f, f2, f3, floatBuffer, floatBuffer2, intBuffer, floatBuffer3)) {
            return new Vector3d(floatBuffer3.get(0) / (float)n, ((float)Display.getHeight() - floatBuffer3.get(1)) / (float)n, floatBuffer3.get(2));
        }
        return null;
    }

    public enum triangleModeEn {
        Custom,
        Astolfo,
        Rainbow,
        Client

    }

    public enum csgoModeEn {
        Box,
        Corner

    }

    public enum healcolorModeEn {
        Custom,
        Astolfo,
        Health,
        Rainbow,
        Client

    }

    public enum espModeEn {
        Flat,
        Box

    }

    public enum colorModeEn {
        Custom,
        Astolfo,
        Rainbow,
        Client

    }

    public enum rectModeEn {
        Default,
        Smooth

    }
}

