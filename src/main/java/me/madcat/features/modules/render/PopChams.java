package me.madcat.features.modules.render;

import com.mojang.authlib.GameProfile;
import java.awt.Color;
import me.madcat.event.events.PacketEvent;
import me.madcat.features.Feature;
import me.madcat.features.modules.Module;
import me.madcat.features.modules.client.ClickGui;
import me.madcat.features.setting.Setting;
import me.madcat.util.ColorUtil;
import me.madcat.util.TotemPopChams;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.network.play.server.SPacketEntityStatus;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.opengl.GL11;

public class PopChams
extends Module {
    public static Setting<Boolean> self;
    public static Setting<Boolean> elevator;
    public static Setting<Integer> rL;
    public static Setting<Integer> gL;
    public static Setting<Integer> bL;
    public static Setting<Integer> aL;
    public static Setting<Integer> rF;
    public static Setting<Integer> gF;
    public static Setting<Integer> bF;
    public static Setting<Integer> aF;
    public static Setting<Integer> fadestart;
    public static Setting<Float> fadetime;
    public static Setting<Boolean> fillrainbow;
    public static Setting<Boolean> outlinerainbow;
    public static Setting<Boolean> onlyOneEsp;
    public static Setting<ElevatorMode> elevatorMode;
    EntityOtherPlayerMP player;
    ModelPlayer playerModel;
    Long startTime;
    double alphaFill;
    double alphaLine;

    public PopChams() {
        super("PopChams", "Pop rendering", Module.Category.RENDER);
        self = this.register(new Setting<>("Render Own Pops", true));
        elevator = this.register(new Setting<>("Travel", true));
        elevatorMode = this.register(new Setting<>("Elevator", ElevatorMode.UP, PopChams::new0));
        rL = this.register(new Setting<>("Outline Red", 30, 0, 255));
        bL = this.register(new Setting<>("Outline Green", 167, 0, 255));
        gL = this.register(new Setting<>("Outline Blue", 255, 0, 255));
        aL = this.register(new Setting<>("Outline Alpha", 255, 0, 255));
        outlinerainbow = this.register(new Setting<>("Outline Rainbow", true));
        rF = this.register(new Setting<>("Fill Red", 30, 0, 255));
        bF = this.register(new Setting<>("Fill Green", 167, 0, 255));
        gF = this.register(new Setting<>("Fill Blue", 255, 0, 255));
        aF = this.register(new Setting<>("Fill Alpha", 140, 0, 255));
        fillrainbow = this.register(new Setting<>("Fill Rainbow", true));
        fadestart = this.register(new Setting<>("Fade Start", 0, 0, 255));
        fadetime = this.register(new Setting<>("Fade Time", 0.5f, 0.0f, 2.0f));
        onlyOneEsp = this.register(new Setting<>("Only Render One", true));
    }

    public static void renderEntity(EntityLivingBase entityLivingBase, ModelBase modelBase, float f, float f2, float f3, float f4, float f5, int n) {
        if (mc.getRenderManager() == null) {
            return;
        }
        float f6 = mc.getRenderPartialTicks();
        double d = entityLivingBase.posX - PopChams.mc.getRenderManager().viewerPosX;
        double d2 = entityLivingBase.posY - PopChams.mc.getRenderManager().viewerPosY;
        double d3 = entityLivingBase.posZ - PopChams.mc.getRenderManager().viewerPosZ;
        GlStateManager.pushMatrix();
        if (entityLivingBase.isSneaking()) {
            d2 -= 0.125;
        }
        float f7 = PopChams.interpolateRotation(entityLivingBase.prevRenderYawOffset, entityLivingBase.renderYawOffset, f6);
        float f8 = PopChams.interpolateRotation(entityLivingBase.prevRotationYawHead, entityLivingBase.rotationYawHead, f6);
        float f9 = f8 - f7;
        float f10 = entityLivingBase.prevRotationPitch + (entityLivingBase.rotationPitch - entityLivingBase.prevRotationPitch) * f6;
        PopChams.renderLivingAt(d, d2, d3);
        float f11 = PopChams.handleRotationFloat(entityLivingBase, f6);
        PopChams.prepareRotations(entityLivingBase);
        float f12 = PopChams.prepareScale(entityLivingBase, n);
        GlStateManager.enableAlpha();
        modelBase.setLivingAnimations(entityLivingBase, f, f2, f6);
        modelBase.setRotationAngles(f, f2, f11, entityLivingBase.rotationYaw, entityLivingBase.rotationPitch, f12, entityLivingBase);
        modelBase.render(entityLivingBase, f, f2, f11, entityLivingBase.rotationYaw, entityLivingBase.rotationPitch, f12);
        GlStateManager.popMatrix();
    }

    public static void prepareTranslate(EntityLivingBase entityLivingBase, double d, double d2, double d3) {
        PopChams.renderLivingAt(d - PopChams.mc.getRenderManager().viewerPosX, d2 - PopChams.mc.getRenderManager().viewerPosY, d3 - PopChams.mc.getRenderManager().viewerPosZ);
    }

    public static void renderLivingAt(double d, double d2, double d3) {
        GlStateManager.translate(d, d2, d3);
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

    public static void prepareRotations(EntityLivingBase entityLivingBase) {
        GlStateManager.rotate(180.0f - entityLivingBase.rotationYaw, 0.0f, 1.0f, 0.0f);
    }

    public static float interpolateRotation(float f, float f2, float f3) {
        float f4;
        for (f4 = f2 - f; f4 < -180.0f; f4 += 360.0f) {
        }
        while (f4 >= 180.0f) {
            f4 -= 360.0f;
        }
        return f + f3 * f4;
    }

    public static Color newAlpha(Color color, int n) {
        return new Color(color.getRed(), color.getGreen(), color.getBlue(), n);
    }

    public static void glColor(Color color) {
        GL11.glColor4f((float)color.getRed() / 255.0f, (float)color.getGreen() / 255.0f, (float)color.getBlue() / 255.0f, (float)color.getAlpha() / 255.0f);
    }

    public static float handleRotationFloat(EntityLivingBase entityLivingBase, float f) {
        return 0.0f;
    }

    public static void prepareGL() {
        GL11.glBlendFunc(770, 771);
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.glLineWidth(1.5f);
        GlStateManager.disableTexture2D();
        GlStateManager.depthMask(false);
        GlStateManager.enableBlend();
        GlStateManager.disableDepth();
        GlStateManager.disableLighting();
        GlStateManager.disableCull();
        GlStateManager.enableAlpha();
        GlStateManager.color(1.0f, 1.0f, 1.0f);
    }

    public static void releaseGL() {
        GlStateManager.enableCull();
        GlStateManager.depthMask(true);
        GlStateManager.enableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.enableDepth();
    }

    @SubscribeEvent
    public void onUpdate(PacketEvent.Receive receive) {
        SPacketEntityStatus sPacketEntityStatus;
        if (Feature.fullNullCheck()) {
            return;
        }
        if (receive.getPacket() instanceof SPacketEntityStatus && (sPacketEntityStatus = receive.getPacket()).getOpCode() == 35 && sPacketEntityStatus.getEntity(PopChams.mc.world) != null && (self.getValue() || sPacketEntityStatus.getEntity(PopChams.mc.world).getEntityId() != PopChams.mc.player.getEntityId())) {
            GameProfile gameProfile = new GameProfile(PopChams.mc.player.getUniqueID(), "");
            this.player = new EntityOtherPlayerMP(PopChams.mc.world, gameProfile);
            this.player.copyLocationAndAnglesFrom(sPacketEntityStatus.getEntity(PopChams.mc.world));
            this.playerModel = new ModelPlayer(0.0f, false);
            this.startTime = System.currentTimeMillis();
            this.playerModel.bipedHead.showModel = false;
            this.playerModel.bipedBody.showModel = false;
            this.playerModel.bipedLeftArmwear.showModel = false;
            this.playerModel.bipedLeftLegwear.showModel = false;
            this.playerModel.bipedRightArmwear.showModel = false;
            this.playerModel.bipedRightLegwear.showModel = false;
            this.alphaFill = aF.getValue();
            this.alphaLine = aL.getValue();
            if (!onlyOneEsp.getValue()) {
                TotemPopChams totemPopChams = new TotemPopChams(this.player, this.playerModel, this.startTime, this.alphaFill, this.alphaLine);
            }
        }
    }

    @SubscribeEvent
    public void onRenderWorld(RenderWorldLastEvent renderWorldLastEvent) {
        if (onlyOneEsp.getValue()) {
            if (this.player == null || PopChams.mc.world == null || PopChams.mc.player == null) {
                return;
            }
            if (elevator.getValue()) {
                if (elevatorMode.getValue() == ElevatorMode.UP) {
                    this.player.posY += 0.05f * renderWorldLastEvent.getPartialTicks();
                } else if (elevatorMode.getValue() == ElevatorMode.DOWN) {
                    this.player.posY -= 0.05f * renderWorldLastEvent.getPartialTicks();
                }
            }
            GL11.glLineWidth(1.0f);
            Color color = outlinerainbow.getValue() ? ColorUtil.rainbow(ClickGui.INSTANCE().rainbowHue.getValue()) : new Color(rL.getValue(), bL.getValue(), gL.getValue(), aL.getValue());
            Color color2 = fillrainbow.getValue() ? ColorUtil.rainbow(ClickGui.INSTANCE().rainbowHue.getValue()) : new Color(rF.getValue(), bF.getValue(), gF.getValue(), aF.getValue());
            int n = color.getAlpha();
            int n2 = color2.getAlpha();
            long l = System.currentTimeMillis() - this.startTime - ((Number)fadestart.getValue()).longValue();
            if (System.currentTimeMillis() - this.startTime > ((Number)fadestart.getValue()).longValue()) {
                double d = this.normalize(l, ((Number)fadetime.getValue()).doubleValue());
                d = MathHelper.clamp(d, 0.0, 1.0);
                d = -d + 1.0;
                n *= (int)d;
                n2 *= (int)d;
            }
            Color color3 = PopChams.newAlpha(color, n);
            Color color4 = PopChams.newAlpha(color2, n2);
            if (this.player != null && this.playerModel != null) {
                PopChams.prepareGL();
                GL11.glPushAttrib(1048575);
                GL11.glEnable(2881);
                GL11.glEnable(2848);
                if (this.alphaFill > 1.0) {
                    this.alphaFill -= fadetime.getValue();
                }
                Color color5 = new Color(color4.getRed(), color4.getGreen(), color4.getBlue(), (int)this.alphaFill);
                if (this.alphaLine > 1.0) {
                    this.alphaLine -= fadetime.getValue();
                }
                Color color6 = new Color(color3.getRed(), color3.getGreen(), color3.getBlue(), (int)this.alphaLine);
                PopChams.glColor(color5);
                GL11.glPolygonMode(1032, 6914);
                PopChams.renderEntity(this.player, this.playerModel, this.player.limbSwing, this.player.limbSwingAmount, this.player.ticksExisted, this.player.rotationYawHead, this.player.rotationPitch, 1);
                PopChams.glColor(color6);
                GL11.glPolygonMode(1032, 6913);
                PopChams.renderEntity(this.player, this.playerModel, this.player.limbSwing, this.player.limbSwingAmount, this.player.ticksExisted, this.player.rotationYawHead, this.player.rotationPitch, 1);
                GL11.glPolygonMode(1032, 6914);
                GL11.glPopAttrib();
                PopChams.releaseGL();
            }
        }
    }

    double normalize(double d, double d2) {
        return (d - 0.0) / (d2 - 0.0);
    }

    private static boolean new0(Object object) {
        return elevator.getValue();
    }

    public enum ElevatorMode {
        UP,
        DOWN

    }
}

