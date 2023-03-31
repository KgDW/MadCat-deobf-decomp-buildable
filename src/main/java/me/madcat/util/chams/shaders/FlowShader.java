package me.madcat.util.chams.shaders;

import me.madcat.util.chams.FramebufferShader;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.opengl.GL20;

public class FlowShader
extends FramebufferShader {
    public static FlowShader INSTANCE;
    protected float time = 0.0f;

    @Override
    public void setupUniforms() {
        this.setupUniform("resolution");
        this.setupUniform("time");
    }

    private FlowShader() {
        super("flow.frag");
    }

    @Override
    public void updateUniforms() {
        GL20.glUniform2f(this.getUniform("resolution"), (float)new ScaledResolution(this.mc).getScaledWidth(), (float)new ScaledResolution(this.mc).getScaledHeight());
        GL20.glUniform1f(this.getUniform("time"), this.time);
        if (!this.animation) {
            return;
        }
        this.time = this.time > 100.0f ? 0.0f : (float)((double)this.time + 0.001 * (double)this.animationSpeed);
    }

    public static FlowShader INSTANCE() {
        if (INSTANCE == null) {
            INSTANCE = new FlowShader();
        }
        return INSTANCE;
    }
}

