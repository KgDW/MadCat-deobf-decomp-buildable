package me.madcat.util.chams;

import me.madcat.util.chams.FramebufferShader;

@FunctionalInterface
public interface ShaderProducer {
    FramebufferShader INSTANCE();
}

