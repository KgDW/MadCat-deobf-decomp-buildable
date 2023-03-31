package me.madcat.util.chams.shaders;

import me.madcat.util.chams.FramebufferShader;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.opengl.GL20;

public class HolyFuckShader
extends FramebufferShader {
    protected float time = 0.0f;
    private static HolyFuckShader INSTANCE;

    @Override
    public void setupUniforms() {
        this.setupUniform("time");
        this.setupUniform("resolution");
        this.setupUniform("speed");
        this.setupUniform("shift");
        this.setupUniform("color");
        this.setupUniform("radius");
        this.setupUniform("quality");
        this.setupUniform("divider");
        this.setupUniform("texelSize");
        this.setupUniform("maxSample");
    }

    @Override
    public void updateUniforms() {
        GL20.glUniform1f(this.getUniform("time"), this.time);
        GL20.glUniform2f(this.getUniform("resolution"), (float)new ScaledResolution(this.mc).getScaledWidth(), (float)new ScaledResolution(this.mc).getScaledHeight());
        GL20.glUniform3f(this.getUniform("color"), this.red, this.green, this.blue);
        GL20.glUniform1f(this.getUniform("radius"), this.radius);
        GL20.glUniform1f(this.getUniform("quality"), this.quality);
        GL20.glUniform1f(this.getUniform("divider"), this.divider);
        GL20.glUniform2f(this.getUniform("speed"), (float)this.animationSpeed, (float)this.animationSpeed);
        GL20.glUniform1f(this.getUniform("shift"), 1.0f);
        GL20.glUniform1f(this.getUniform("maxSample"), this.maxSample);
        if (!this.animation) {
            return;
        }
        this.time = this.time > 100.0f ? 0.0f : this.time + 0.01f * (float) this.animationSpeed;
    }

    public static HolyFuckShader INSTANCE() {
        if (INSTANCE == null) {
            INSTANCE = new HolyFuckShader();
        }
        return INSTANCE;
    }

    private HolyFuckShader() {
        super("holyfuck.frag");
    }
}

