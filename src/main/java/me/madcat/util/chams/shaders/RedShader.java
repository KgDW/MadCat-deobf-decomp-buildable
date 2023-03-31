package me.madcat.util.chams.shaders;

import me.madcat.util.chams.FramebufferShader;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.opengl.GL20;

public class RedShader
extends FramebufferShader {
    private static RedShader INSTANCE;
    protected float time = 0.0f;

    @Override
    public void updateUniforms() {
        GL20.glUniform1f(this.getUniform("time"), this.time);
        GL20.glUniform2f(this.getUniform("resolution"), (float)new ScaledResolution(this.mc).getScaledWidth(), (float)new ScaledResolution(this.mc).getScaledHeight());
        GL20.glUniform2f(this.getUniform("texelSize"), 1.0f / (float)this.mc.displayWidth * (this.radius * this.quality), 1.0f / (float)this.mc.displayHeight * (this.radius * this.quality));
        if (!this.animation) {
            return;
        }
        this.time = this.time > 100.0f ? 0.0f : (float)((double)this.time + 0.05 * (double)this.animationSpeed);
    }

    private RedShader() {
        super("red.frag");
    }

    public static RedShader INSTANCE() {
        if (INSTANCE == null) {
            INSTANCE = new RedShader();
        }
        return INSTANCE;
    }

    @Override
    public void setupUniforms() {
        this.setupUniform("time");
        this.setupUniform("resolution");
        this.setupUniform("texelSize");
    }
}

