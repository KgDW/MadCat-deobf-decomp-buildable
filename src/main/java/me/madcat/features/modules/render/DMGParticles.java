package me.madcat.features.modules.render;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.ibm.icu.math.BigDecimal;
import java.awt.Color;
import java.util.List;
import java.util.Map;
import me.madcat.event.events.Render3DEvent;
import me.madcat.features.modules.Module;
import me.madcat.features.setting.Setting;
import me.madcat.util.Timer;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.opengl.GL11;

public class DMGParticles
extends Module {
    private final List<Particle> particles;
    private final Map<Integer, Float> hpData = Maps.newHashMap();
    public final Setting<Integer> deleteAfter;
    private final Timer timer;

    public DMGParticles() {
        super("DMGParticles", "show damage", Module.Category.RENDER);
        this.particles = Lists.newArrayList();
        this.timer = new Timer();
        this.deleteAfter = this.register(new Setting<>("Remove Ticks", 7, 1, 60));
    }

    @Override
    public void onDisable() {
        this.particles.clear();
    }

    @Override
    @SubscribeEvent
    public void onRender3D(Render3DEvent render3DEvent) {
        if (!this.particles.isEmpty()) {
            for (Particle particle : this.particles) {
                if (particle != null) {
                    if (particle.ticks > this.deleteAfter.getValue()) {
                        continue;
                    }
                    GlStateManager.pushMatrix();
                    GlStateManager.disableDepth();
                    Tessellator tessellator = Tessellator.getInstance();
                    BufferBuilder bufferBuilder = tessellator.getBuffer();
                    bufferBuilder.begin(3, DefaultVertexFormats.POSITION_COLOR);
                    tessellator.draw();
                    GL11.glDisable(2848);
                    GlStateManager.depthMask(true);
                    GlStateManager.enableDepth();
                    GlStateManager.enableTexture2D();
                    GlStateManager.disableBlend();
                    GlStateManager.popMatrix();
                    GlStateManager.pushMatrix();
                    GlStateManager.enablePolygonOffset();
                    GlStateManager.doPolygonOffset(1.0f, -1500000.0f);
                    GlStateManager.translate(particle.posX - DMGParticles.mc.getRenderManager().viewerPosX, particle.posY - DMGParticles.mc.getRenderManager().viewerPosY, particle.posZ - DMGParticles.mc.getRenderManager().viewerPosZ);
                    GlStateManager.rotate(-DMGParticles.mc.getRenderManager().playerViewY, 0.0f, 1.0f, 0.0f);
                    GlStateManager.rotate(DMGParticles.mc.getRenderManager().playerViewX, DMGParticles.mc.gameSettings.thirdPersonView == 2 ? -1.0f : 1.0f, 0.0f, 0.0f);
                    GlStateManager.scale(-0.03, -0.03, 0.03);
                    GL11.glDepthMask(false);
                    DMGParticles.mc.fontRenderer.drawStringWithShadow(particle.str, (float)((double)(-DMGParticles.mc.fontRenderer.getStringWidth(particle.str)) * 0.5), (float)(-DMGParticles.mc.fontRenderer.FONT_HEIGHT + 1), particle.color.getRGB());
                    GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
                    GL11.glDepthMask(true);
                    GlStateManager.doPolygonOffset(1.0f, 1500000.0f);
                    GlStateManager.disablePolygonOffset();
                    GlStateManager.resetColor();
                    GlStateManager.popMatrix();
                }
            }
        }
    }

    @Override
    public void onUpdate() {
        if (this.timer.passedMs(12000L)) {
            this.particles.clear();
            this.timer.reset();
        }
        if (!this.particles.isEmpty()) {
            for (Particle particle : this.particles) {
                if (particle != null) {
                    ++particle.ticks;
                }
            }
        }
        for (Entity particle : DMGParticles.mc.world.loadedEntityList) {
            if (!(particle instanceof EntityLivingBase)) continue;
            EntityLivingBase entityLivingBase = (EntityLivingBase)particle;
            double d = this.hpData.getOrDefault(entityLivingBase.getEntityId(), entityLivingBase.getMaxHealth());
            this.hpData.remove(particle.getEntityId());
            this.hpData.put(particle.getEntityId(), entityLivingBase.getHealth());
            if (d == (double)entityLivingBase.getHealth()) {
                continue;
            }
            Color color = Color.YELLOW;
            Vec3d vec3d = new Vec3d(particle.posX + Math.random() * 0.5 * (double)(Math.random() > 0.5 ? -1 : 1), particle.getEntityBoundingBox().minY + (particle.getEntityBoundingBox().maxY - particle.getEntityBoundingBox().minY) * 0.5, particle.posZ + Math.random() * 0.5 * (double)(Math.random() > 0.5 ? -1 : 1));
            double d2 = new BigDecimal(Math.abs(d - (double)entityLivingBase.getHealth())).setScale(1, 4).doubleValue();
            this.particles.add(new Particle(String.valueOf(d2), vec3d.x, vec3d.y, vec3d.z, color));
        }
    }

    static class Particle {
        public final double posY;
        public final String str;
        public final Color color;
        public final double posX;
        public final double posZ;
        public int ticks;

        public Particle(String string, double d, double d2, double d3, Color color) {
            this.str = string;
            this.posX = d;
            this.posY = d2;
            this.posZ = d3;
            this.color = color;
            this.ticks = 0;
        }
    }
}

