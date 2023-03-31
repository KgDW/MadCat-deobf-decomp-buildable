package me.madcat.util.chams.shaders;

import me.madcat.util.chams.FramebufferShader;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.opengl.GL20;

public class BasicShader
extends FramebufferShader {
    private float time = 0.0f;
    private float timeMult = 0.1f;
    private static BasicShader INSTANCE;

    public static FramebufferShader INSTANCE(String string) {
        if (INSTANCE == null || !BasicShader.INSTANCE.fragmentShader.equals(string)) {
            INSTANCE = new BasicShader(string);
        }
        return INSTANCE;
    }

    public static FramebufferShader INSTANCE(String string, float f) {
        if (INSTANCE == null || !BasicShader.INSTANCE.fragmentShader.equals(string)) {
            INSTANCE = new BasicShader(string, f);
        }
        return INSTANCE;
    }

    private BasicShader(String string) {
        super(string);
    }

    @Override
    public void setupUniforms() {
        this.setupUniform("time");
        this.setupUniform("resolution");
    }

    @Override
    public void updateUniforms() {
        GL20.glUniform1f(this.getUniform("time"), this.time);
        GL20.glUniform2f(this.getUniform("resolution"), (float)new ScaledResolution(this.mc).getScaledWidth(), (float)new ScaledResolution(this.mc).getScaledHeight());
        if (!this.animation) {
            return;
        }
        int n = 10000;
        this.time = this.time > 10000.0f ? 0.0f : this.time + this.timeMult * (float)this.animationSpeed;
    }

    private BasicShader(String string, float f) {
        super(string);
        this.timeMult = f;
    }
}

