package me.madcat.mixin.mixins;

import java.io.IOException;
import me.madcat.util.shader.GLSLShader;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={GuiMainMenu.class})
public class MixinGuiMainMenu
        extends GuiScreen {
    public GLSLShader shader;
    public long initTime;

    @Inject(method={"drawPanorama"}, at={@At(value="TAIL")})
    public void drawPanoramaTailHook(int mouseX, int mouseY, float partialTicks, CallbackInfo info) {
        try {
            this.shader = new GLSLShader("/assets/minecraft/textures/shader/fragment/fsh/shader.fsh");
        }
        catch (IOException iOException) {
            // empty catch block
        }
        GlStateManager.disableCull();
        this.shader.useShader(this.width * 2, this.height * 2, mouseX * 2, mouseY * 2, (float)(System.currentTimeMillis() - this.initTime) / 1000.0f);
        GL11.glBegin(7);
        GL11.glVertex2f(-1.0f, -1.0f);
        GL11.glVertex2f(-1.0f, 1.0f);
        GL11.glVertex2f(1.0f, 1.0f);
        GL11.glVertex2f(1.0f, -1.0f);
        GL11.glEnd();
        GL20.glUseProgram(0);
    }

    @Inject(method={"initGui"}, at={@At(value="HEAD")})
    private void initHook(CallbackInfo info) {
        this.initTime = System.currentTimeMillis();
    }
}
 