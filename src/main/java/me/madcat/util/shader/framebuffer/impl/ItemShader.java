package me.madcat.util.shader.framebuffer.impl;

import me.madcat.util.shader.framebuffer.FramebufferShader;
import org.lwjgl.opengl.GL20;

public class ItemShader
extends FramebufferShader {
    public static final ItemShader INSTANCE = new ItemShader();
    public boolean model;
    public float alpha = 1.0f;
    public float mix;

    @Override
    public void updateUniforms() {
        GL20.glUniform1i(this.getUniform("texture"), 0);
        GL20.glUniform1i(this.getUniform("inside"), this.model ? 1 : 0);
        GL20.glUniform2f(this.getUniform("texelSize"), 1.0f / (float)this.mc.displayWidth * (this.radius * this.quality), 1.0f / (float)this.mc.displayHeight * (this.radius * this.quality));
        GL20.glUniform3f(this.getUniform("color"), this.red, this.green, this.blue);
        GL20.glUniform1f(this.getUniform("divider"), 140.0f);
        GL20.glUniform1f(this.getUniform("radius"), this.radius);
        GL20.glUniform1f(this.getUniform("maxSample"), 10.0f);
        GL20.glUniform2f(this.getUniform("dimensions"), (float)this.mc.displayWidth, (float)this.mc.displayHeight);
        GL20.glUniform1f(this.getUniform("mixFactor"), this.mix);
        GL20.glUniform1f(this.getUniform("minAlpha"), this.alpha);
    }

    @Override
    public void setupUniforms() {
        this.setupUniform("texture");
        this.setupUniform("texelSize");
        this.setupUniform("color");
        this.setupUniform("divider");
        this.setupUniform("radius");
        this.setupUniform("maxSample");
        this.setupUniform("dimensions");
        this.setupUniform("mixFactor");
        this.setupUniform("minAlpha");
        this.setupUniform("inside");
    }

    public ItemShader() {
        super("glow.frag");
    }
}

