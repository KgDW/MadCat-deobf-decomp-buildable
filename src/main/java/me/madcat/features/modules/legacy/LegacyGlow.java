package me.madcat.features.modules.legacy;

import java.awt.Color;
import me.madcat.event.events.RenderEntityModelEvent;
import me.madcat.features.modules.Module;
import me.madcat.features.modules.client.ClickGui;
import me.madcat.features.setting.Setting;
import me.madcat.util.ColorUtil;
import me.madcat.util.EntityUtil;
import me.madcat.util.RenderUtil;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import org.lwjgl.opengl.GL11;

public class LegacyGlow
extends Module {
    private static LegacyGlow INSTANCE = new LegacyGlow();
    public final Setting<Float> lineWidth = this.register(new Setting<>("LineWidth", 2.0f, 0.1f, 15.0f));
    public final Setting<Boolean> other = this.register(new Setting<>("Crystal", true));
    public final Setting<Integer> red = this.register(new Setting<>("Red", 255, 0, 255));
    public final Setting<Integer> green = this.register(new Setting<>("Green", 255, 0, 255));
    public final Setting<Integer> blue = this.register(new Setting<>("Blue", 255, 0, 255));
    public final Setting<Integer> alpha = this.register(new Setting<>("Alpha", 255, 0, 255));
    public final Setting<Boolean> colorSync = this.register(new Setting<>("Rainbow", true));
    private final Setting<Mode> mode = this.register(new Setting<>("Mode", Mode.OUTLINE));
    private final Setting<Boolean> players = this.register(new Setting<>("Players", true));
    private final Setting<Boolean> animals = this.register(new Setting<>("Animals", false));
    private final Setting<Boolean> mobs = this.register(new Setting<>("Mobs", false));
    private final Setting<Boolean> colorFriends = this.register(new Setting<>("Friends", true));
    private final Setting<Boolean> self = this.register(new Setting<>("Self", true));
    private final Setting<Boolean> onTop = this.register(new Setting<>("onTop", true));
    private final Setting<Boolean> invisibles = this.register(new Setting<>("Invisibles", false));

    public LegacyGlow() {
        super("LegacyGlow", "ESP", Module.Category.LEGACY);
        this.setInstance();
    }

    public static LegacyGlow INSTANCE() {
        if (INSTANCE == null) {
            INSTANCE = new LegacyGlow();
        }
        return INSTANCE;
    }

    private void setInstance() {
        INSTANCE = this;
    }

    public void onRenderModel(RenderEntityModelEvent renderEntityModelEvent) {
        if (renderEntityModelEvent.getStage() != 0 || renderEntityModelEvent.entity == null || renderEntityModelEvent.entity.isInvisible() && !this.invisibles.getValue() || !this.self.getValue() && renderEntityModelEvent.entity.equals(LegacyGlow.mc.player) || !this.players.getValue() && renderEntityModelEvent.entity instanceof EntityPlayer || !this.animals.getValue() && EntityUtil.isPassive(renderEntityModelEvent.entity) || !this.mobs.getValue() && !EntityUtil.isPassive(renderEntityModelEvent.entity) && !(renderEntityModelEvent.entity instanceof EntityPlayer)) {
            return;
        }
        Color color = this.colorSync.getValue() ? EntityUtil.getColor(renderEntityModelEvent.entity, ColorUtil.rainbow(ClickGui.INSTANCE().rainbowHue.getValue()).getRed(), ColorUtil.rainbow(ClickGui.INSTANCE().rainbowHue.getValue()).getGreen(), ColorUtil.rainbow(ClickGui.INSTANCE().rainbowHue.getValue()).getBlue(), this.alpha.getValue(), this.colorFriends.getValue()) : EntityUtil.getColor(renderEntityModelEvent.entity, this.red.getValue(), this.green.getValue(), this.blue.getValue(), this.alpha.getValue(), this.colorFriends.getValue());
        boolean bl = LegacyGlow.mc.gameSettings.fancyGraphics;
        LegacyGlow.mc.gameSettings.fancyGraphics = false;
        float f = LegacyGlow.mc.gameSettings.gammaSetting;
        LegacyGlow.mc.gameSettings.gammaSetting = 10000.0f;
        if (this.onTop.getValue()) {
            renderEntityModelEvent.modelBase.render(renderEntityModelEvent.entity, renderEntityModelEvent.limbSwing, renderEntityModelEvent.limbSwingAmount, renderEntityModelEvent.age, renderEntityModelEvent.headYaw, renderEntityModelEvent.headPitch, renderEntityModelEvent.scale);
        }
        if (this.mode.getValue() == Mode.OUTLINE) {
            RenderUtil.renderOne(this.lineWidth.getValue());
            renderEntityModelEvent.modelBase.render(renderEntityModelEvent.entity, renderEntityModelEvent.limbSwing, renderEntityModelEvent.limbSwingAmount, renderEntityModelEvent.age, renderEntityModelEvent.headYaw, renderEntityModelEvent.headPitch, renderEntityModelEvent.scale);
            GlStateManager.glLineWidth(this.lineWidth.getValue());
            RenderUtil.renderTwo();
            renderEntityModelEvent.modelBase.render(renderEntityModelEvent.entity, renderEntityModelEvent.limbSwing, renderEntityModelEvent.limbSwingAmount, renderEntityModelEvent.age, renderEntityModelEvent.headYaw, renderEntityModelEvent.headPitch, renderEntityModelEvent.scale);
            GlStateManager.glLineWidth(this.lineWidth.getValue());
            RenderUtil.renderThree();
            RenderUtil.renderFour(color);
            renderEntityModelEvent.modelBase.render(renderEntityModelEvent.entity, renderEntityModelEvent.limbSwing, renderEntityModelEvent.limbSwingAmount, renderEntityModelEvent.age, renderEntityModelEvent.headYaw, renderEntityModelEvent.headPitch, renderEntityModelEvent.scale);
            GlStateManager.glLineWidth(this.lineWidth.getValue());
            RenderUtil.renderFive();
        } else {
            GL11.glPushMatrix();
            GL11.glPushAttrib(1048575);
            if (this.mode.getValue() == Mode.WIREFRAME) {
                GL11.glPolygonMode(1032, 6913);
            } else {
                GL11.glPolygonMode(1028, 6913);
            }
            GL11.glDisable(3553);
            GL11.glDisable(2896);
            GL11.glDisable(2929);
            GL11.glEnable(2848);
            GL11.glEnable(3042);
            GlStateManager.blendFunc(770, 771);
            GlStateManager.color((float)color.getRed() / 255.0f, (float)color.getGreen() / 255.0f, (float)color.getBlue() / 255.0f, (float)color.getAlpha() / 255.0f);
            GlStateManager.glLineWidth(this.lineWidth.getValue());
            renderEntityModelEvent.modelBase.render(renderEntityModelEvent.entity, renderEntityModelEvent.limbSwing, renderEntityModelEvent.limbSwingAmount, renderEntityModelEvent.age, renderEntityModelEvent.headYaw, renderEntityModelEvent.headPitch, renderEntityModelEvent.scale);
            GL11.glPopAttrib();
            GL11.glPopMatrix();
        }
        if (!this.onTop.getValue()) {
            renderEntityModelEvent.modelBase.render(renderEntityModelEvent.entity, renderEntityModelEvent.limbSwing, renderEntityModelEvent.limbSwingAmount, renderEntityModelEvent.age, renderEntityModelEvent.headYaw, renderEntityModelEvent.headPitch, renderEntityModelEvent.scale);
        }
        try {
            LegacyGlow.mc.gameSettings.fancyGraphics = bl;
            LegacyGlow.mc.gameSettings.gammaSetting = f;
        }
        catch (Exception exception) {
            // empty catch block
        }
        renderEntityModelEvent.setCanceled(true);
    }

    public enum Mode {
        WIREFRAME,
        OUTLINE

    }
}

