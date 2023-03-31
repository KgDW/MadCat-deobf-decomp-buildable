package me.madcat.util.chams.shaders;

import me.madcat.util.chams.FramebufferShader;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.opengl.GL20;

public class AquaShader
extends FramebufferShader {
    public float time = 0.0f;
    private static AquaShader INSTANCE;

    @Override
    public void setupUniforms() {
        this.setupUniform("resolution");
        this.setupUniform("time");
    }

    private AquaShader() {
        super("aqua.frag");
    }

    @Override
    public void updateUniforms() {
        GL20.glUniform1f(this.getUniform("time"), this.time);
        GL20.glUniform2f(this.getUniform("resolution"), (float)new ScaledResolution(this.mc).getScaledWidth(), (float)new ScaledResolution(this.mc).getScaledHeight());
        if (!this.animation) {
            return;
        }
        this.time = this.time > 100.0f ? 0.0f : (float)((double)this.time + 0.01 * (double)this.animationSpeed);
    }

    public static FramebufferShader INSTANCE() {
        if (INSTANCE == null) {
            INSTANCE = new AquaShader();
        }
        return INSTANCE;
    }
}

