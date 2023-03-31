package me.madcat.features.modules.combat;

import java.awt.Color;
import me.madcat.MadCat;
import me.madcat.event.events.Render3DEvent;
import me.madcat.features.modules.Module;
import me.madcat.features.setting.Setting;
import me.madcat.util.ColorUtil;
import me.madcat.util.DamageUtil;
import me.madcat.util.EntityUtil;
import me.madcat.util.MathUtil;
import me.madcat.util.RenderUtil;
import me.madcat.util.Timer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import org.lwjgl.opengl.GL11;

public class KillAura
extends Module {
    public final Setting<Boolean> players;
    private final Setting<RenderMode> render;
    public final Setting<Boolean> packet;
    public final Setting<Boolean> tps;
    public final Setting<Float> raytrace;
    public final Setting<Boolean> vehicles;
    public final Setting<Boolean> animals;
    public final Setting<Boolean> delay;
    public static Entity target;
    public final Setting<Boolean> mobs;
    public final Setting<Boolean> onlySharp;
    public final Setting<Boolean> projectiles;
    public final Setting<Float> range = this.register(new Setting<>("Range", 6.0f, 0.1f, 7.0f));
    private final Timer timer;

    private void doKillaura() {
        int n = 0;
        if (this.onlySharp.getValue() && !EntityUtil.holdingWeapon(KillAura.mc.player)) {
            target = null;
            return;
        }
        int n2 = !this.delay.getValue() ? 0 : (n = (int)((float)DamageUtil.getCooldownByWeapon(KillAura.mc.player) * (this.tps.getValue() ? MadCat.serverManager.getTpsFactor() : 1.0f)));
        if (!this.timer.passedMs(n)) {
            return;
        }
        target = this.getTarget();
        if (target == null) {
            return;
        }
        EntityUtil.attackEntity(target, this.packet.getValue(), true);
        this.timer.reset();
    }

    @Override
    public void onTick() {
        this.doKillaura();
    }

    public KillAura() {
        super("KillAura", "Kills aura", Module.Category.COMBAT);
        this.delay = this.register(new Setting<>("HitDelay", Boolean.TRUE));
        this.onlySharp = this.register(new Setting<>("SwordOnly", Boolean.TRUE));
        this.raytrace = this.register(new Setting<>("Raytrace", 6.0f, 0.1f, 7.0f, "Wall Range."));
        this.players = this.register(new Setting<>("Players", Boolean.TRUE));
        this.mobs = this.register(new Setting<>("Mobs", Boolean.FALSE));
        this.animals = this.register(new Setting<>("Animals", Boolean.FALSE));
        this.vehicles = this.register(new Setting<>("Entities", Boolean.FALSE));
        this.projectiles = this.register(new Setting<>("Projectiles", Boolean.FALSE));
        this.tps = this.register(new Setting<>("TpsSync", Boolean.TRUE));
        this.packet = this.register(new Setting<>("Packet", Boolean.FALSE));
        this.timer = new Timer();
        this.render = this.register(new Setting<>("Render", RenderMode.JELLO));
    }

    private double easeInOutQuad(double d) {
        return d < 0.5 ? 2.0 * d * d : 1.0 - Math.pow(-2.0 * d + 2.0, 2.0) / 2.0;
    }

    @Override
    public void onRender3D(Render3DEvent render3DEvent) {
        if (target != null) {
            if (this.render.getValue() == RenderMode.OLD) {
                RenderUtil.drawEntityBoxESP(target, MadCat.colorManager.getCurrent(), true, new Color(255, 255, 255, 130), 0.7f, true, true, 35);
            } else if (this.render.getValue() == RenderMode.JELLO) {
                double d = 1500.0;
                double d2 = (double)System.currentTimeMillis() % d;
                boolean bl = d2 > d / 2.0;
                double d3 = d2 / (d / 2.0);
                d3 = !bl ? 1.0 - d3 : d3 - 1.0;
                d3 = this.easeInOutQuad(d3);
                KillAura.mc.entityRenderer.disableLightmap();
                GL11.glPushMatrix();
                GL11.glDisable(3553);
                GL11.glBlendFunc(770, 771);
                GL11.glEnable(2848);
                GL11.glEnable(3042);
                GL11.glDisable(2929);
                GL11.glDisable(2884);
                GL11.glShadeModel(7425);
                KillAura.mc.entityRenderer.disableLightmap();
                double d4 = KillAura.target.width;
                double d5 = (double)KillAura.target.height + 0.1;
                double d6 = KillAura.target.lastTickPosX + (KillAura.target.posX - KillAura.target.lastTickPosX) * (double)mc.getRenderPartialTicks() - KillAura.mc.getRenderManager().viewerPosX;
                double d7 = KillAura.target.lastTickPosY + (KillAura.target.posY - KillAura.target.lastTickPosY) * (double)mc.getRenderPartialTicks() - KillAura.mc.getRenderManager().viewerPosY + d5 * d3;
                double d8 = KillAura.target.lastTickPosZ + (KillAura.target.posZ - KillAura.target.lastTickPosZ) * (double)mc.getRenderPartialTicks() - KillAura.mc.getRenderManager().viewerPosZ;
                double d9 = d5 / 3.0 * (d3 > 0.5 ? 1.0 - d3 : d3) * (double)(bl ? -1 : 1);
                for (int i = 0; i < 360; i += 5) {
                    Color color = MadCat.colorManager.getRainbow();
                    double d10 = d6 - Math.sin((double)i * Math.PI / 180.0) * d4;
                    double d11 = d8 + Math.cos((double)i * Math.PI / 180.0) * d4;
                    double d12 = d6 - Math.sin((double)(i - 5) * Math.PI / 180.0) * d4;
                    double d13 = d8 + Math.cos((double)(i - 5) * Math.PI / 180.0) * d4;
                    GL11.glBegin(7);
                    GL11.glColor4f((float)ColorUtil.pulseColor(color, 200, 1).getRed() / 255.0f, (float)ColorUtil.pulseColor(color, 200, 1).getGreen() / 255.0f, (float)ColorUtil.pulseColor(color, 200, 1).getBlue() / 255.0f, 0.0f);
                    GL11.glVertex3d(d10, d7 + d9, d11);
                    GL11.glVertex3d(d12, d7 + d9, d13);
                    GL11.glColor4f((float)ColorUtil.pulseColor(color, 200, 1).getRed() / 255.0f, (float)ColorUtil.pulseColor(color, 200, 1).getGreen() / 255.0f, (float)ColorUtil.pulseColor(color, 200, 1).getBlue() / 255.0f, 200.0f);
                    GL11.glVertex3d(d12, d7, d13);
                    GL11.glVertex3d(d10, d7, d11);
                    GL11.glEnd();
                    GL11.glBegin(2);
                    GL11.glVertex3d(d12, d7, d13);
                    GL11.glVertex3d(d10, d7, d11);
                    GL11.glEnd();
                }
                GL11.glEnable(2884);
                GL11.glShadeModel(7424);
                GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
                GL11.glEnable(2929);
                GL11.glDisable(2848);
                GL11.glDisable(3042);
                GL11.glEnable(3553);
                GL11.glPopMatrix();
            }
        }
    }

    @Override
    public String getDisplayInfo() {
        if (target instanceof EntityPlayer) {
            return target.getName();
        }
        return null;
    }

    private Entity getTarget() {
        Entity entity = null;
        double d = this.range.getValue();
        double d2 = 36.0;
        for (Entity entity2 : KillAura.mc.world.loadedEntityList) {
            if (!(this.players.getValue() && entity2 instanceof EntityPlayer || this.animals.getValue() && EntityUtil.isPassive(entity2) || this.mobs.getValue() && !EntityUtil.isMobAggressive(entity2) || this.vehicles.getValue() && EntityUtil.isVehicle(entity2)) && (!this.projectiles.getValue() || !EntityUtil.isProjectile(entity2))) continue;
            if (entity2 != null && EntityUtil.isntValid(entity2, d)) {
                continue;
            }
            if (!KillAura.mc.player.canEntityBeSeen(entity2) && !EntityUtil.canEntityFeetBeSeen(entity2) && KillAura.mc.player.getDistanceSq(entity2) > MathUtil.square(this.raytrace.getValue())) {
                continue;
            }
            if (entity == null) {
                entity = entity2;
                d = KillAura.mc.player.getDistanceSq(entity2);
                d2 = EntityUtil.getHealth(entity2);
                continue;
            }
            if (entity2 instanceof EntityPlayer && DamageUtil.isArmorLow((EntityPlayer)entity2, 18)) {
                entity = entity2;
                break;
            }
            if (KillAura.mc.player.getDistanceSq(entity2) < d) {
                entity = entity2;
                d = KillAura.mc.player.getDistanceSq(entity2);
                d2 = EntityUtil.getHealth(entity2);
            }
            if (!((double)EntityUtil.getHealth(entity2) < d2)) continue;
            entity = entity2;
            d = KillAura.mc.player.getDistanceSq(entity2);
            d2 = EntityUtil.getHealth(entity2);
        }
        return entity;
    }

    private enum RenderMode {
        OLD,
        JELLO,
        OFF

    }
}

