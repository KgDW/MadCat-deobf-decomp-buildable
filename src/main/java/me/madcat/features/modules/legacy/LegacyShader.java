package me.madcat.features.modules.legacy;

import java.awt.Color;
import me.madcat.MadCat;
import me.madcat.event.events.RenderItemInFirstPersonEvent;
import me.madcat.features.Feature;
import me.madcat.features.modules.Module;
import me.madcat.features.setting.Setting;
import me.madcat.mixin.mixins.IEntityRenderer;
import me.madcat.util.EntityUtil;
import me.madcat.util.shader.framebuffer.impl.ItemShader;
import net.minecraft.client.gui.GuiDownloadTerrain;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.item.EntityExpBottle;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.opengl.Display;

public class LegacyShader
extends Module {
    public static LegacyShader INSTANCE;
    public final Setting<Integer> red = this.register(new Setting<>("Red", 255, 0, 255));
    public final Setting<Integer> green = this.register(new Setting<>("Green", 255, 0, 255));
    public final Setting<Integer> blue = this.register(new Setting<>("Blue", 255, 0, 255));
    public final Setting<Integer> alpha2 = this.register(new Setting<>("alpha", 255, 0, 255));
    private final Setting<Boolean> players = this.register(new Setting<>("Players", false));
    private final Setting<Boolean> crystals = this.register(new Setting<>("Crystals", false));
    private final Setting<Boolean> xp = this.register(new Setting<>("Exp", false));
    private final Setting<Boolean> items = this.register(new Setting<>("Items", false));
    private final Setting<Boolean> self = this.register(new Setting<>("Self", true));
    private final Setting<Boolean> glow = this.register(new Setting<>("Glow", true));
    private final Setting<Float> radius = this.register(new Setting<>("Radius", 4.0f, 0.1f, 6.0f, this::new0));
    private final Setting<Float> smoothness = this.register(new Setting<>("Smoothness", 1.0f, 0.1f, 1.0f, this::new1));
    private final Setting<Integer> alpha = this.register(new Setting<>("GlowAlpha", 50, 1, 50, this::new2));
    private final Setting<Boolean> model = this.register(new Setting<>("Model", true));
    private final Setting<Integer> range = this.register(new Setting<>("Range", 75, 5, 250));
    private final Setting<Boolean> fovOnly = this.register(new Setting<>("FOVOnly", false));
    private boolean forceRender;

    public LegacyShader() {
        super("LegacyShader", "Is in beta test stage", Module.Category.LEGACY);
        INSTANCE = this;
    }

    @SubscribeEvent
    public void renderItemInFirstPerson(RenderItemInFirstPersonEvent renderItemInFirstPersonEvent) {
        if (Feature.fullNullCheck() || !this.isOn() || renderItemInFirstPersonEvent.getStage() != 0 || this.forceRender || !this.self.getValue()) {
            return;
        }
        renderItemInFirstPersonEvent.setCanceled(true);
    }

    @SubscribeEvent
    public void onRenderWorldLastEvent(RenderWorldLastEvent renderWorldLastEvent) {
        if (Feature.fullNullCheck() || !this.isOn()) {
            return;
        }
        if ((Display.isActive() || Display.isVisible()) && LegacyShader.mc.gameSettings.thirdPersonView == 0 && !(LegacyShader.mc.currentScreen instanceof GuiDownloadTerrain)) {
            GlStateManager.pushMatrix();
            GlStateManager.pushAttrib();
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
            GlStateManager.enableDepth();
            GlStateManager.depthMask(true);
            GlStateManager.enableAlpha();
            ItemShader itemShader = ItemShader.INSTANCE;
            itemShader.mix = this.alpha2.getValue();
            itemShader.alpha = (float)(205 + this.alpha.getValue()) / 255.0f;
            itemShader.model = this.model.getValue();
            itemShader.startDraw(mc.getRenderPartialTicks());
            this.forceRender = true;
            LegacyShader.mc.world.loadedEntityList.stream().filter(this::onRenderWorldLastEvent3).forEach(arg_0 -> this.onRenderWorldLastEvent4(renderWorldLastEvent, arg_0));
            if (this.self.getValue()) {
                ((IEntityRenderer)LegacyShader.mc.entityRenderer).invokeRenderHand(mc.getRenderPartialTicks(), 2);
            }
            this.forceRender = false;
            itemShader.stopDraw(new Color(this.red.getValue(), this.green.getValue(), this.blue.getValue(), this.alpha2.getValue()), this.glow.getValue() ? this.radius.getValue() : 0.0f, this.smoothness.getValue());
            GlStateManager.disableBlend();
            GlStateManager.disableAlpha();
            GlStateManager.disableDepth();
            GlStateManager.popAttrib();
            GlStateManager.popMatrix();
        }
    }

    @Override
    public void onLogin() {
        if (this.isEnabled()) {
            this.disable();
            this.enable();
        }
    }

    private void onRenderWorldLastEvent4(RenderWorldLastEvent renderWorldLastEvent, Entity entity) {
        Render render;
        if (entity.getDistance(LegacyShader.mc.player) > (float) this.range.getValue() || this.fovOnly.getValue() && !MadCat.rotationManager.isInFov(entity.getPosition())) {
            return;
        }
        Vec3d vec3d = EntityUtil.getInterpolatedRenderPos(entity, renderWorldLastEvent.getPartialTicks());
        if (entity instanceof EntityPlayer) {
            ((EntityPlayer)entity).hurtTime = 0;
        }
        if ((render = mc.getRenderManager().getEntityRenderObject(entity)) != null) {
            try {
                render.doRender(entity, vec3d.x, vec3d.y, vec3d.z, entity.rotationYaw, renderWorldLastEvent.getPartialTicks());
            }
            catch (Exception exception) {
                // empty catch block
            }
        }
    }

    private boolean onRenderWorldLastEvent3(Entity entity) {
        return entity != null && (entity != LegacyShader.mc.player || entity != mc.getRenderViewEntity()) && mc.getRenderManager().getEntityRenderObject(entity) != null && (entity instanceof EntityPlayer && this.players.getValue() && !((EntityPlayer)entity).isSpectator() || entity instanceof EntityEnderCrystal && this.crystals.getValue() || entity instanceof EntityExpBottle && this.xp.getValue() || entity instanceof EntityItem && this.items.getValue());
    }

    private boolean new2(Integer n) {
        return this.glow.getValue();
    }

    private boolean new1(Float f) {
        return this.glow.getValue();
    }

    private boolean new0(Float f) {
        return this.glow.getValue();
    }
}

