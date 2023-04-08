package me.madcat.features.modules.render;

import java.awt.Color;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;
import me.madcat.event.events.ConnectionEvent;
import me.madcat.event.events.Render3DEvent;
import me.madcat.features.Feature;
import me.madcat.features.command.Command;
import me.madcat.features.modules.Module;
import me.madcat.features.setting.Setting;
import me.madcat.util.ColorUtil;
import me.madcat.util.MathUtil;
import me.madcat.util.RenderUtil;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class LogESP
extends Module {
    public final Setting<Float> range = this.register(new Setting<>("Range", 300.0f, 50.0f, 500.0f));
    public final Setting<Boolean> message = this.register(new Setting<>("Message", false));
    private final Setting<Integer> red = this.register(new Setting<>("Red", 255, 0, 255));
    private final Setting<Integer> green = this.register(new Setting<>("Green", 0, 0, 255));
    private final Setting<Integer> blue = this.register(new Setting<>("Blue", 0, 0, 255));
    private final Setting<Integer> alpha = this.register(new Setting<>("Alpha", 255, 0, 255));
    private final Setting<Boolean> rainbow = this.register(new Setting<>("Rainbow", false));
    private final Setting<Integer> rainbowhue = this.register(new Setting<>("Brightness", 255, 0, 255, this::new0));
    private final Setting<Boolean> scaleing = this.register(new Setting<>("Scale", false));
    private final Setting<Float> scaling = this.register(new Setting<>("Size", 4.0f, 0.1f, 20.0f));
    private final Setting<Float> factor = this.register(new Setting<Object>("Factor", 0.3f, 0.1f, 1.0f, this::new1));
    private final Setting<Boolean> smartScale = this.register(new Setting<Object>("SmartScale", Boolean.FALSE, this::new2));
    private final Setting<Boolean> rect = this.register(new Setting<>("Rectangle", true));
    private final Setting<Boolean> coords = this.register(new Setting<>("Coords", true));
    private final List<LogoutPos> spots = new CopyOnWriteArrayList<>();
    static final boolean $assertionsDisabled = !LogESP.class.desiredAssertionStatus();

    public LogESP() {
        super("LogEsp", "Render player log coords", Module.Category.RENDER);
    }

    @Override
    public void onLogout() {
        this.spots.clear();
    }

    @Override
    public void onDisable() {
        this.spots.clear();
    }
    
    @Override
    public void onRender3D(Render3DEvent render3DEvent) {
        if (this.spots.isEmpty()) return;
        synchronized (this.spots) {
            this.spots.forEach(arg_0 -> this.onRender3D3(render3DEvent, arg_0));
        }
    }

    @Override
    public void onUpdate() {
        if (Feature.fullNullCheck()) {
            return;
        }
        this.spots.removeIf(this::onUpdate4);
    }

    @SubscribeEvent
    public void onConnection(ConnectionEvent connectionEvent) {
        if (LogESP.mc.world.getPlayerEntityByUUID(connectionEvent.getUuid()) != null && LogESP.mc.world.getPlayerEntityByUUID(connectionEvent.getUuid()).getName().equals("\u00a7\u0061\u00a7\u006c\u005b\u8d5e\u52a9\u672c\u670d\u005d")) {
            return;
        }
        if (connectionEvent.getStage() == 0) {
            UUID uUID = connectionEvent.getUuid();
            EntityPlayer entityPlayer = LogESP.mc.world.getPlayerEntityByUUID(uUID);
            if (entityPlayer != null && this.message.getValue()) {
                Command.sendMessage("§a" + entityPlayer.getName() + " just logged in" + (this.coords.getValue() ? " at (" + (int)entityPlayer.posX + ", " + (int)entityPlayer.posY + ", " + (int)entityPlayer.posZ + ")!" : "!"));
            }
            this.spots.removeIf(arg_0 -> LogESP.onConnection5(connectionEvent, arg_0));
        } else if (connectionEvent.getStage() == 1) {
            EntityPlayer entityPlayer = connectionEvent.getEntity();
            UUID uUID = connectionEvent.getUuid();
            String string = connectionEvent.getName();
            if (this.message.getValue()) {
                Command.sendMessage("§c" + connectionEvent.getName() + " just logged out" + (this.coords.getValue() ? " at (" + (int)entityPlayer.posX + ", " + (int)entityPlayer.posY + ", " + (int)entityPlayer.posZ + ")!" : "!"));
            }
            if (string != null && entityPlayer != null && uUID != null) {
                this.spots.add(new LogoutPos(string, entityPlayer));
            }
        }
    }

    private void renderNameTag(String string, double d, double d2, double d3, float f, double d4, double d5, double d6) {
        double d7 = d2 + 0.7;
        Entity entity = mc.getRenderViewEntity();
        if (!$assertionsDisabled && entity == null) {
            throw new AssertionError();
        }
        double d8 = entity.posX;
        double d9 = entity.posY;
        double d10 = entity.posZ;
        entity.posX = this.interpolate(entity.prevPosX, entity.posX, f);
        entity.posY = this.interpolate(entity.prevPosY, entity.posY, f);
        entity.posZ = this.interpolate(entity.prevPosZ, entity.posZ, f);
        String string2 = string + " XYZ: " + (int)d4 + ", " + (int)d5 + ", " + (int)d6;
        double d11 = entity.getDistance(d + LogESP.mc.getRenderManager().viewerPosX, d7 + LogESP.mc.getRenderManager().viewerPosY, d3 + LogESP.mc.getRenderManager().viewerPosZ);
        int n = this.renderer.getStringWidth(string2) / 2;
        double d12 = (0.0018 + (double) this.scaling.getValue() * (d11 * (double) this.factor.getValue())) / 1000.0;
        if (d11 <= 8.0 && this.smartScale.getValue()) {
            d12 = 0.0245;
        }
        if (!this.scaleing.getValue()) {
            d12 = (double) this.scaling.getValue() / 100.0;
        }
        GlStateManager.pushMatrix();
        RenderHelper.enableStandardItemLighting();
        GlStateManager.enablePolygonOffset();
        GlStateManager.doPolygonOffset(1.0f, -1500000.0f);
        GlStateManager.disableLighting();
        GlStateManager.translate((float)d, (float)d7 + 1.4f, (float)d3);
        GlStateManager.rotate(-LogESP.mc.getRenderManager().playerViewY, 0.0f, 1.0f, 0.0f);
        GlStateManager.rotate(LogESP.mc.getRenderManager().playerViewX, LogESP.mc.gameSettings.thirdPersonView == 2 ? -1.0f : 1.0f, 0.0f, 0.0f);
        GlStateManager.scale(-d12, -d12, d12);
        GlStateManager.disableDepth();
        GlStateManager.enableBlend();
        GlStateManager.enableBlend();
        if (this.rect.getValue()) {
            RenderUtil.drawRect(-n - 2, -(this.renderer.getFontHeight() + 1), (float)n + 2.0f, 1.5f, 0x55000000);
        }
        GlStateManager.disableBlend();
        this.renderer.drawStringWithShadow(string2, -n, -(this.renderer.getFontHeight() - 1), ColorUtil.toRGBA(new Color(this.red.getValue(), this.green.getValue(), this.blue.getValue(), this.alpha.getValue())));
        entity.posX = d8;
        entity.posY = d9;
        entity.posZ = d10;
        GlStateManager.enableDepth();
        GlStateManager.disableBlend();
        GlStateManager.disablePolygonOffset();
        GlStateManager.doPolygonOffset(1.0f, 1500000.0f);
        GlStateManager.popMatrix();
    }

    private double interpolate(double d, double d2, float f) {
        return d + (d2 - d) * (double)f;
    }

    private static boolean onConnection5(ConnectionEvent connectionEvent, LogoutPos logoutPos) {
        return logoutPos.getName().equalsIgnoreCase(connectionEvent.getName());
    }

    private boolean onUpdate4(LogoutPos logoutPos) {
        return LogESP.mc.player.getDistanceSq(logoutPos.getEntity()) >= MathUtil.square(this.range.getValue());
    }

    private void onRender3D3(Render3DEvent render3DEvent, LogoutPos logoutPos) {
        if (logoutPos.getEntity() != null) {
            AxisAlignedBB axisAlignedBB = RenderUtil.interpolateAxis(logoutPos.getEntity().getEntityBoundingBox());
            RenderUtil.drawBlockOutline(axisAlignedBB, this.rainbow.getValue() ? ColorUtil.rainbow(this.rainbowhue.getValue()) : new Color(this.red.getValue(), this.green.getValue(), this.blue.getValue(), this.alpha.getValue()), 1.0f);
            double d = this.interpolate(logoutPos.getEntity().lastTickPosX, logoutPos.getEntity().posX, render3DEvent.getPartialTicks()) - LogESP.mc.getRenderManager().viewerPosX;
            double d2 = this.interpolate(logoutPos.getEntity().lastTickPosY, logoutPos.getEntity().posY, render3DEvent.getPartialTicks()) - LogESP.mc.getRenderManager().viewerPosY;
            double d3 = this.interpolate(logoutPos.getEntity().lastTickPosZ, logoutPos.getEntity().posZ, render3DEvent.getPartialTicks()) - LogESP.mc.getRenderManager().viewerPosZ;
            this.renderNameTag(logoutPos.getName(), d, d2, d3, render3DEvent.getPartialTicks(), logoutPos.getX(), logoutPos.getY(), logoutPos.getZ());
        }
    }

    private boolean new2(Object object) {
        return this.scaleing.getValue();
    }

    private boolean new1(Object object) {
        return this.scaleing.getValue();
    }

    private boolean new0(Integer n) {
        return this.rainbow.getValue();
    }

    private static class LogoutPos {
        private final String name;
        private final EntityPlayer entity;
        private final double x;
        private final double y;
        private final double z;

        public LogoutPos(String string, EntityPlayer entityPlayer) {
            this.name = string;
            this.entity = entityPlayer;
            this.x = entityPlayer.posX;
            this.y = entityPlayer.posY;
            this.z = entityPlayer.posZ;
        }

        public String getName() {
            return this.name;
        }

        public EntityPlayer getEntity() {
            return this.entity;
        }

        public double getX() {
            return this.x;
        }

        public double getY() {
            return this.y;
        }

        public double getZ() {
            return this.z;
        }
    }
}

