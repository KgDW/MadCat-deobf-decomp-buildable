package me.madcat.features.modules.legacy;

import me.madcat.features.Feature;
import me.madcat.features.modules.Module;
import me.madcat.features.setting.Setting;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

public class GameShaders
extends Module {
    private static final GameShaders INSTANCE = new GameShaders();
    public final Setting<Mode> shader = this.register(new Setting<>("Mode", Mode.green));

    public GameShaders() {
        super("GameShaders", "legacy", Module.Category.LEGACY);
    }

    @Override
    public void onUpdate() {
        if (Feature.fullNullCheck()) {
            return;
        }
        if (OpenGlHelper.shadersSupported && mc.getRenderViewEntity() instanceof EntityPlayer) {
            if (GameShaders.mc.entityRenderer.getShaderGroup() != null) {
                GameShaders.mc.entityRenderer.getShaderGroup().deleteShaderGroup();
            }
            try {
                GameShaders.mc.entityRenderer.loadShader(new ResourceLocation("shaders/post/" + this.shader.getValue() + ".json"));
            }
            catch (Exception exception) {
                exception.printStackTrace();
            }
        } else if (GameShaders.mc.entityRenderer.getShaderGroup() != null && GameShaders.mc.currentScreen == null) {
            GameShaders.mc.entityRenderer.getShaderGroup().deleteShaderGroup();
        }
    }

    @Override
    public String getDisplayInfo() {
        return this.shader.currentEnumName();
    }

    @Override
    public void onDisable() {
        if (GameShaders.mc.entityRenderer.getShaderGroup() != null) {
            GameShaders.mc.entityRenderer.getShaderGroup().deleteShaderGroup();
        }
    }

    public enum Mode {
        notch,
        antialias,
        art,
        bits,
        blobs,
        blobs2,
        blur,
        bumpy,
        color_convolve,
        creeper,
        deconverge,
        desaturate,
        flip,
        fxaa,
        green,
        invert,
        ntsc,
        pencil,
        phosphor,
        sobel,
        spider,
        wobble

    }
}

