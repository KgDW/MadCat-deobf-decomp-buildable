package me.madcat.util.chams.shaders;

import me.madcat.util.chams.FramebufferShader;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.opengl.GL20;

public class AquaGlShader
extends FramebufferShader {
    private static AquaGlShader INSTANCE;
    public float time = 0.0f;

    @Override
    public void updateUniforms() {
        GL20.glUniform1f(this.getUniform("time"), this.time);
        GL20.glUniform2f(this.getUniform("resolution"), (float)new ScaledResolution(this.mc).getScaledWidth(), (float)new ScaledResolution(this.mc).getScaledHeight());
        GL20.glUniform1i(this.getUniform("texture"), 0);
        GL20.glUniform2f(this.getUniform("texelSize"), Float.intBitsToFloat(Float.floatToIntBits(1531.2186f) ^ 0x7B3F66FF) / (float)this.mc.displayWidth * (this.radius * this.quality), Float.intBitsToFloat(Float.floatToIntBits(103.132805f) ^ 0x7D4E43FF) / (float)this.mc.displayHeight * (this.radius * this.quality));
        GL20.glUniform3f(this.getUniform("color"), this.red, this.green, this.blue);
        GL20.glUniform1f(this.getUniform("divider"), this.divider);
        GL20.glUniform1f(this.getUniform("radius"), this.radius);
        GL20.glUniform1f(this.getUniform("maxSample"), this.maxSample);
        if (!this.animation) {
            return;
        }
        this.time = this.time > 100.0f ? 0.0f : (float)((double)this.time + 0.01 * (double)this.animationSpeed);
    }

    public AquaGlShader() {
        super("aquaglow.frag");
    }

    @Override
    public void setupUniforms() {
        this.setupUniform("resolution");
        this.setupUniform("time");
        this.setupUniform("texture");
        this.setupUniform("texelSize");
        this.setupUniform("color");
        this.setupUniform("divider");
        this.setupUniform("radius");
        this.setupUniform("maxSample");
    }

    public static FramebufferShader INSTANCE() {
        if (INSTANCE == null) {
            INSTANCE = new AquaGlShader();
        }
        return INSTANCE;
    }
}

