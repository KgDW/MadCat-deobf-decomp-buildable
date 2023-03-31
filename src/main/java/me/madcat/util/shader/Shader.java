package me.madcat.util.shader;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import org.apache.commons.io.IOUtils;
import org.lwjgl.opengl.ARBShaderObjects;
import org.lwjgl.opengl.GL20;

public abstract class Shader {
    private int program;
    private Map<String, Integer> uniformsMap;
    public final Minecraft mc = Minecraft.getMinecraft();

    public void setupUniform(String string) {
        this.setUniform(string, GL20.glGetUniformLocation(this.program, string));
    }

    public void startShader() {
        GlStateManager.pushMatrix();
        GL20.glUseProgram(this.program);
        if (this.uniformsMap == null) {
            this.uniformsMap = new HashMap<>();
            this.setupUniforms();
        }
        this.updateUniforms();
    }

    public void stopShader() {
        GL20.glUseProgram(0);
        GlStateManager.popMatrix();
    }

    public void setUniform(String string, int n) {
        this.uniformsMap.put(string, n);
    }

    public int getUniform(String string) {
        return this.uniformsMap.get(string);
    }

    public abstract void updateUniforms();

    private int createShader(String string, int n) {
        int n2;
        block4: {
            n2 = 0;
            try {
                n2 = ARBShaderObjects.glCreateShaderObjectARB(n);
                if (n2 != 0) break block4;
                return 0;
            }
            catch (Exception exception) {
                ARBShaderObjects.glDeleteObjectARB(n2);
                throw exception;
            }
        }
        ARBShaderObjects.glShaderSourceARB(n2, string);
        ARBShaderObjects.glCompileShaderARB(n2);
        if (ARBShaderObjects.glGetObjectParameteriARB(n2, 35713) == 0) {
            throw new RuntimeException("Error creating shaders: " + this.getLogInfo(n2));
        }
        return n2;
    }

    private String getLogInfo(int n) {
        return ARBShaderObjects.glGetInfoLogARB(n, ARBShaderObjects.glGetObjectParameteriARB(n, 35716));
    }

    public abstract void setupUniforms();

    public Shader(String string) {
        int n = 0;
        int n2 = 0;
        try {
            InputStream inputStream = this.getClass().getResourceAsStream("/assets/minecraft/textures/shader/vertex/vert/vertex.vert");
            if (inputStream != null) {
                n = this.createShader(IOUtils.toString(inputStream), 35633);
            }
            IOUtils.closeQuietly(inputStream);
            InputStream inputStream2 = this.getClass().getResourceAsStream("/assets/minecraft/textures/shader/fragment/frag/" + string);
            if (inputStream2 != null) {
                n2 = this.createShader(IOUtils.toString(inputStream2), 35632);
            }
            IOUtils.closeQuietly(inputStream2);
        }
        catch (Exception exception) {
            exception.printStackTrace();
            return;
        }
        if (n == 0 || n2 == 0) {
            return;
        }
        this.program = ARBShaderObjects.glCreateProgramObjectARB();
        if (this.program == 0) {
            return;
        }
        ARBShaderObjects.glAttachObjectARB(this.program, n);
        ARBShaderObjects.glAttachObjectARB(this.program, n2);
        ARBShaderObjects.glLinkProgramARB(this.program);
        ARBShaderObjects.glValidateProgramARB(this.program);
    }
}

