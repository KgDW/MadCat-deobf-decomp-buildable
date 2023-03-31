package me.madcat.features.modules.render;

import me.madcat.MadCat;
import me.madcat.features.modules.Module;
import me.madcat.features.setting.Setting;
import me.madcat.mixin.mixins.IEntityRenderer;
import me.madcat.util.BlockUtil;
import me.madcat.util.EntityUtil;
import me.madcat.util.chams.FramebufferShader;
import me.madcat.util.chams.ShaderMode;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.item.EntityExpBottle;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

public class Chams
extends Module {
    public final Setting<Float> maxSample;
    public final Setting<Boolean> animation;
    private Boolean criticalSection = false;
    private final Setting<Integer> range;
    private final Setting<Boolean> crystals;
    public final Setting<Boolean> behind = this.register(new Setting<>("Behind", false));
    public final Setting<Boolean> reset;
    private final Setting<Boolean> xp;
    private final Setting<Boolean> items;
    public final Setting<Integer> animationSpeed;
    private final Setting<Boolean> self;
    public final Setting<ShaderMode> shader;
    private final Setting<Boolean> fovOnly;
    public final Setting<Float> radius;
    public final Setting<Float> divider;
    private final Setting<Boolean> players = this.register(new Setting<>("Player", false));
    private static Chams INSTANCE;

    @SubscribeEvent
    public void onRenderHand(RenderHandEvent renderHandEvent) {
        if (!this.criticalSection && this.self.getValue()) {
            renderHandEvent.setCanceled(true);
        }
    }

    public static Chams INSTANCE() {
        if (INSTANCE == null) {
            INSTANCE = new Chams();
        }
        return INSTANCE;
    }

    private void onRenderWorldLastEvent1(RenderWorldLastEvent renderWorldLastEvent, Entity entity) {
        Render render;
        if (entity.getDistance(Chams.mc.player) > (float) this.range.getValue() || this.fovOnly.getValue() && MadCat.rotationManager.isInFov(entity.getPosition())) {
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

    @Override
    public void onLogin() {
        if (this.isEnabled()) {
            this.disable();
            this.enable();
        }
    }

    @SubscribeEvent
    public void onRender2D(RenderWorldLastEvent renderWorldLastEvent) {
        if (Display.isActive() || Display.isVisible()) {
            if (BlockUtil.getBlock(EntityUtil.getPlayerPos(Chams.mc.player).up()).equals(Blocks.WATER)) {
                return;
            }
            FramebufferShader framebufferShader = this.shader.getValue().getShader();
            if (framebufferShader == null) {
                return;
            }
            GlStateManager.pushMatrix();
            GlStateManager.disableCull();
            GlStateManager.enableBlend();
            GL11.glEnable(2848);
            GL11.glBlendFunc(770, 771);
            GlStateManager.popMatrix();
            GL11.glPolygonMode(1032, 6914);
            framebufferShader.setShaderParams(this.animation.getValue(), this.animationSpeed.getValue(), MadCat.colorManager.getCurrent(), this.radius.getValue(), this.divider.getValue(), this.maxSample.getValue());
            this.criticalSection = true;
            framebufferShader.startDraw(mc.getRenderPartialTicks());
            Chams.mc.world.loadedEntityList.stream().filter(this::onRenderWorldLastEvent0).forEach(arg_0 -> this.onRenderWorldLastEvent1(renderWorldLastEvent, arg_0));
            if (this.self.getValue()) {
                ((IEntityRenderer)Chams.mc.entityRenderer).invokeRenderHand(mc.getRenderPartialTicks(), 2);
            }
            framebufferShader.stopDraw();
            this.criticalSection = false;
        }
    }

    public Chams() {
        super("Chams", "god render", Module.Category.RENDER);
        this.crystals = this.register(new Setting<>("Crystal", false));
        this.xp = this.register(new Setting<>("Exp", false));
        this.items = this.register(new Setting<>("DroppedItem", false));
        this.self = this.register(new Setting<>("ItemChams", true));
        this.fovOnly = this.register(new Setting<>("Fov only", false));
        this.range = this.add(new Setting<>("Range", 50, 5, 250));
        this.shader = this.register(new Setting<>("Shader Mode", ShaderMode.SNOW));
        this.animation = this.register(new Setting<>("Animation", true));
        this.animationSpeed = this.register(new Setting<>("Animation Speed", 1, 1, 10));
        this.radius = this.register(new Setting<>("Glow Radius", 3.3f, 1.0f, 10.0f));
        this.divider = this.register(new Setting<>("Glow Divider", 158.6f, 1.0f, 1000.0f));
        this.maxSample = this.register(new Setting<>("Glow MaxSample", 10.0f, 1.0f, 20.0f));
        this.reset = this.register(new Setting<>("Reset", false));
        this.setInstance();
    }

    @Override
    public String getDisplayInfo() {
        return this.shader.getValue().getName();
    }

    @Override
    public void onUpdate() {
        if (this.reset.getValue()) {
            this.shader.setValue(ShaderMode.SNOW);
            this.animation.setValue(true);
            this.animationSpeed.setValue(1);
            this.radius.setValue(3.3f);
            this.divider.setValue(158.6f);
            this.maxSample.setValue(10.0f);
            this.reset.setValue(false);
        }
    }

    private boolean onRenderWorldLastEvent0(Entity entity) {
        return entity != null && (entity != Chams.mc.player || entity != mc.getRenderViewEntity()) && mc.getRenderManager().getEntityRenderObject(entity) != null && (entity instanceof EntityPlayer && this.players.getValue() && !((EntityPlayer)entity).isSpectator() || entity instanceof EntityEnderCrystal && this.crystals.getValue() || entity instanceof EntityExpBottle && this.xp.getValue() || entity instanceof EntityItem && this.items.getValue());
    }

    private void setInstance() {
        INSTANCE = this;
    }
}

