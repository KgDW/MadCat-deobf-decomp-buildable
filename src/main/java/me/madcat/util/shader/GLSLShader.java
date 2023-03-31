package me.madcat.util.shader;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import org.lwjgl.opengl.GL20;

public class GLSLShader {
    private final int mouseUniform;
    private final int timeUniform;
    private final int programId;
    private final int resolutionUniform;

    private String readStreamToString(InputStream inputStream) throws IOException {
        int n;
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] byArray = new byte[512];
        while ((n = inputStream.read(byArray, 0, byArray.length)) != -1) {
            byteArrayOutputStream.write(byArray, 0, n);
        }
        return new String(byteArrayOutputStream.toByteArray(), StandardCharsets.UTF_8);
    }

    public GLSLShader(String string) throws IOException {
        int n = GL20.glCreateProgram();
        GL20.glAttachShader(n, this.createShader("/assets/minecraft/textures/shader/vertex/vsh/passthrough.vsh", GLSLShader.class.getResourceAsStream("/assets/minecraft/textures/shader/vertex/vsh/passthrough.vsh"), 35633));
        GL20.glAttachShader(n, this.createShader(string, GLSLShader.class.getResourceAsStream(string), 35632));
        GL20.glLinkProgram(n);
        int n2 = GL20.glGetProgrami(n, 35714);
        if (n2 == 0) {
            throw new IllegalStateException("Shader failed to link");
        }
        this.programId = n;
        GL20.glUseProgram(n);
        this.timeUniform = GL20.glGetUniformLocation(n, "time");
        this.mouseUniform = GL20.glGetUniformLocation(n, "mouse");
        this.resolutionUniform = GL20.glGetUniformLocation(n, "resolution");
        GL20.glUseProgram(0);
    }

    private int createShader(String string, InputStream inputStream, int n) throws IOException {
        int n2 = GL20.glCreateShader(n);
        GL20.glShaderSource(n2, this.readStreamToString(inputStream));
        GL20.glCompileShader(n2);
        int n3 = GL20.glGetShaderi(n2, 35713);
        if (n3 == 0) {
            System.err.println(GL20.glGetShaderInfoLog(n2, GL20.glGetShaderi(n2, 35716)));
            System.err.println("Caused by " + string);
            throw new IllegalStateException("Failed to compile shader: " + string);
        }
        return n2;
    }

    public void useShader(int n, int n2, float f, float f2, float f3) {
        GL20.glUseProgram(this.programId);
        GL20.glUniform2f(this.resolutionUniform, (float)n, (float)n2);
        GL20.glUniform2f(this.mouseUniform, f / (float)n, 1.0f - f2 / (float)n2);
        GL20.glUniform1f(this.timeUniform, f3);
    }
}

