package me.madcat.features.modules.render;

import me.madcat.features.modules.Module;
import me.madcat.features.setting.Setting;
import me.madcat.util.EntityUtil;
import me.madcat.util.MathUtil;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import org.lwjgl.opengl.GL11;

public class Tracer
extends Module {
    public final Setting<Boolean> players = this.register(new Setting<>("Players", true));
    public final Setting<Boolean> mobs = this.register(new Setting<>("Mobs", false));
    public final Setting<Boolean> animals = this.register(new Setting<>("Animals", false));
    public final Setting<Boolean> invisibles = this.register(new Setting<>("Invisibles", false));
    public final Setting<Boolean> drawFromSky = this.register(new Setting<>("DrawFromSky", false));
    public final Setting<Float> width = this.register(new Setting<>("Width", 1.0f, 0.1f, 5.0f));
    public final Setting<Integer> distance = this.register(new Setting<>("Radius", 300, 0, 300));
    public final Setting<Boolean> crystalCheck = this.register(new Setting<>("CrystalCheck", false));

    public Tracer() {
        super("Tracers", "Draws lines to other players", Module.Category.RENDER);
    }

    public void drawLineFromPosToPos(double d, double d2, double d3, double d4, double d5, double d6, float f, float f2, float f3, float f4) {
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(3042);
        GL11.glLineWidth(this.width.getValue());
        GL11.glDisable(3553);
        GL11.glDisable(2929);
        GL11.glDepthMask(false);
        GL11.glColor4f(f, f2, f3, f4);
        GlStateManager.disableLighting();
        GL11.glLoadIdentity();
        GL11.glBegin(1);
        GL11.glVertex3d(d, d2, d3);
        GL11.glVertex3d(d4, d5, d6);
        GL11.glVertex3d(d4, d5, d6);
        GL11.glEnd();
        GL11.glEnable(3553);
        GL11.glEnable(2929);
        GL11.glDepthMask(true);
        GL11.glDisable(3042);
        GL11.glColor3d(1.0, 1.0, 1.0);
        GlStateManager.enableLighting();
    }

    private boolean onRender3D2(Entity entity) {
        return this.invisibles.getValue() || !entity.isInvisible();
    }

    private boolean onRender3D(Entity entity) {
        return Tracer.mc.player.getDistanceSq(entity) < MathUtil.square(this.distance.getValue());
    }

    private boolean onRender3D0(Entity entity) {
        return entity instanceof EntityPlayer ? this.players.getValue() && Tracer.mc.player != entity : (EntityUtil.isPassive(entity) ? this.animals.getValue() : this.mobs.getValue());
    }
}

