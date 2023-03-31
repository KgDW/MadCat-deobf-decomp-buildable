package me.madcat.features.gui.particle;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import me.madcat.util.ColorUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector2f;

public class Particle {
    private static final Random random = new Random();
    static final Minecraft mc = Minecraft.getMinecraft();
    private final Vector2f pos;
    private final Vector2f velocity;
    private float alpha;
    private float size;

    public Particle(Vector2f vector2f, float f, float f2, float f3) {
        this.velocity = vector2f;
        this.pos = new Vector2f(f, f2);
        this.size = f3;
    }

    public static Particle getParticle() {
        Vector2f vector2f = new Vector2f((float)(Math.random() * 3.0 - 1.0), (float)(Math.random() * 3.0 - 1.0));
        float f = random.nextInt(Display.getWidth());
        float f2 = random.nextInt(Display.getHeight());
        float f3 = (float)(Math.random() * 4.0) + 2.0f;
        return new Particle(vector2f, f, f2, f3);
    }

    public float getAlpha() {
        return this.alpha;
    }

    public float getDistanceTo(Particle particle) {
        return this.getDistanceTo(particle.getX(), particle.getY());
    }

    public float getDistanceTo(float f, float f2) {
        return (float)Util.getDistance(this.getX(), this.getY(), f, f2);
    }

    public float getSize() {
        return this.size;
    }

    public void setSize(float f) {
        this.size = f;
    }

    public float getX() {
        return this.pos.getX();
    }

    public void setX(float f) {
        this.pos.setX(f);
    }

    public float getY() {
        return this.pos.getY();
    }

    public void setY(float f) {
        this.pos.setY(f);
    }

    public void setup(int n, float f) {
        this.pos.x += this.velocity.getX() * (float)n * (f / 2.0f);
        this.pos.y += this.velocity.getY() * (float)n * (f / 2.0f);
        if (this.alpha < 180.0f) {
            this.alpha += 0.75f;
        }
        if (this.pos.getX() > (float)Display.getWidth()) {
            this.pos.setX(0.0f);
        }
        if (this.pos.getX() < 0.0f) {
            this.pos.setX((float)Display.getWidth());
        }
        if (this.pos.getY() > (float)Display.getHeight()) {
            this.pos.setY(0.0f);
        }
        if (this.pos.getY() < 0.0f) {
            this.pos.setY((float)Display.getHeight());
        }
    }

    public static class Util {
        private final List<Particle> particles = new ArrayList<>();

        public Util(int n) {
            this.addParticle(n);
        }

        public static double getDistance(float f, float f2, float f3, float f4) {
            return Math.sqrt((f - f3) * (f - f3) + (f2 - f4) * (f2 - f4));
        }

        public void addParticle(int n) {
            for (int i = 0; i < n; ++i) {
                this.particles.add(Particle.getParticle());
            }
        }

        private void drawTracer(float f, float f2, float f3, float f4, Color color, Color color2, Color color3) {
            GL11.glPushMatrix();
            GL11.glDisable(3553);
            GL11.glEnable(3042);
            GL11.glBlendFunc(770, 771);
            GL11.glShadeModel(7425);
            GL11.glColor4f((float)color.getRed() / 255.0f, (float)color.getGreen() / 255.0f, (float)color.getBlue() / 255.0f, (float)color.getAlpha() / 255.0f);
            GL11.glLineWidth((float) 0.6);
            GL11.glBegin(1);
            GL11.glVertex2f(f, f2);
            GL11.glColor4f((float)color2.getRed() / 255.0f, (float)color2.getGreen() / 255.0f, (float)color2.getBlue() / 255.0f, (float)color2.getAlpha() / 255.0f);
            float f6 = f2 >= f4 ? f4 + (f2 - f4) / 2.0f : f2 + (f4 - f2) / 2.0f;
            float f7 = f >= f3 ? f3 + (f - f3) / 2.0f : f + (f3 - f) / 2.0f;
            GL11.glVertex2f(f7, f6);
            GL11.glEnd();
            GL11.glBegin(1);
            GL11.glColor4f((float)color2.getRed() / 255.0f, (float)color2.getGreen() / 255.0f, (float)color2.getBlue() / 255.0f, (float)color2.getAlpha() / 255.0f);
            GL11.glVertex2f(f7, f6);
            GL11.glColor4f((float)color3.getRed() / 255.0f, (float)color3.getGreen() / 255.0f, (float)color3.getBlue() / 255.0f, (float)color3.getAlpha() / 255.0f);
            GL11.glVertex2f(f3, f4);
            GL11.glEnd();
            GL11.glPopMatrix();
        }

        public void drawParticles() {
            GL11.glPushMatrix();
            GL11.glEnable(3042);
            GL11.glDisable(3553);
            GL11.glBlendFunc(770, 771);
            GL11.glDisable(2884);
            GL11.glDisable(2929);
            GL11.glDepthMask(false);
            if (Particle.mc.currentScreen == null) {
                return;
            }
            for (Particle particle : this.particles) {
                particle.setup(2, 0.1f);
                int n = Mouse.getEventX() * Particle.mc.currentScreen.width / Particle.mc.displayWidth;
                int n2 = Particle.mc.currentScreen.height - Mouse.getEventY() * Particle.mc.currentScreen.height / Particle.mc.displayHeight - 1;
                int n3 = 300;
                float f = (float)MathHelper.clamp((double)particle.getAlpha() - (double)(particle.getAlpha() / 300.0f) * Util.getDistance(n, n2, particle.getX(), particle.getY()), 0.0, particle.getAlpha());
                Color color = ColorUtil.injectAlpha(new Color(-1714829883), (int)f);
                GL11.glColor4f((float)color.getRed() / 255.0f, (float)color.getGreen() / 255.0f, (float)color.getBlue() / 255.0f, (float)color.getAlpha() / 255.0f);
                GL11.glPointSize(particle.getSize());
                GL11.glBegin(0);
                GL11.glVertex2f(particle.getX(), particle.getY());
                GL11.glEnd();
                float f2 = 0.0f;
                Particle particle2 = null;
                for (Particle particle3 : this.particles) {
                    float f3 = particle.getDistanceTo(particle3);
                    if (!(f3 <= 300.0f) || !(Util.getDistance(n, n2, particle.getX(), particle.getY()) <= 300.0) && !(Util.getDistance(n, n2, particle3.getX(), particle3.getY()) <= 300.0) || f2 > 0.0f && f3 > f2) continue;
                    f2 = f3;
                    particle2 = particle3;
                }
                if (particle2 == null) continue;
                this.drawTracer(particle.getX(), particle.getY(), particle2.getX(), particle2.getY(), color, ColorUtil.injectAlpha(new Color(0x838080), (int)f), color);
            }
            GL11.glPushMatrix();
            GL11.glTranslatef(0.5f, 0.5f, 0.5f);
            GL11.glNormal3f(0.0f, 1.0f, 0.0f);
            GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
            GL11.glEnable(3553);
            GL11.glPopMatrix();
            GL11.glDepthMask(true);
            GL11.glEnable(2884);
            GL11.glEnable(2929);
            GL11.glDisable(3042);
            GL11.glPopMatrix();
        }
    }
}

